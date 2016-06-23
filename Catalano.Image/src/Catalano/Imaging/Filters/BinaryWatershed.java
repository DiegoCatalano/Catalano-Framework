// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
// Code adapted from ImageJ, thanks to Wayne Rasband.
//
//    This library is free software; you can redistribute it and/or
//    modify it under the terms of the GNU Lesser General Public
//    License as published by the Free Software Foundation; either
//    version 2.1 of the License, or (at your option) any later version.
//
//    This library is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
//    Lesser General Public License for more details.
//
//    You should have received a copy of the GNU Lesser General Public
//    License along with this library; if not, write to the Free Software
//    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
//

package Catalano.Imaging.Filters;

import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.IApplyInPlace;
import Catalano.Imaging.Tools.ImageStatistics;
import java.util.Arrays;

/**
 * Binary Watershed.
 * 
 * Binary Watershed lines (e.g. the continental divide) mark the boundaries of catchment regions in a topographical map.
 The height of a point on this map can have a direct correlation to its pixel intensity. WIth this analogy, the morphological
 operations of closing (or opening) can be understood as smoothing the ridges (or filling in the valleys).
 Develops a new algorithm for obtaining the watershed lines in a graph, and then uses this in developing a new segmentation approach
 based on the depth of immersion.
 * 
 * @author Diego Catalano
 */
public class BinaryWatershed implements IApplyInPlace{
    
    private final int[] DIR_X_OFFSET = new int[] {  0,  1,  1,  1,  0, -1, -1, -1 };
    private final int[] DIR_Y_OFFSET = new int[] { -1, -1,  0,  1,  1,  1,  0, -1 };
    private int[]     dirOffset;
    private final float SQRT2 = 1.4142135624f;
    private int       intEncodeXMask;               // needed for encoding x & y in a single int (watershed): mask for x
    private int       intEncodeYMask;               // needed for encoding x & y in a single int (watershed): mask for y
    private int       intEncodeShift;               // needed for encoding x & y in a single int (watershed): shift of y
    
    private DistanceTransform.Distance distance = DistanceTransform.Distance.Euclidean;
    private float tolerance = 0.5f;

    /**
     * Initializes a new instance of the BinaryWatershed class.
     */
    public BinaryWatershed() {}
    
    /**
     * Initializes a new instance of the BinaryWatershed class.
     * @param tolerance Tolerance.
     */
    public BinaryWatershed(float tolerance){
        this.tolerance = tolerance;
    }
    
    /**
     * Initializes a new instance of the BinaryWatershed class.
     * @param tolerance Tolerance.
     * @param distance Distance.
     */
    public BinaryWatershed(float tolerance, DistanceTransform.Distance distance){
        this.tolerance = tolerance;
        this.distance = distance;
    }
    
    /**
     * Initializes a new instance of the BinaryWatershed class.
     * @param distance Distance.
     */
    public BinaryWatershed(DistanceTransform.Distance distance){
        this.distance = distance;
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        if(!fastBitmap.isGrayscale())
            throw new IllegalArgumentException("Binary Watershed only works in grayscale (binary) images");
        
        Watershed(fastBitmap);
        
    }
    
    private void Watershed(FastBitmap fastBitmap){
        
        DistanceTransform dt = new DistanceTransform(distance);
        float[][] distance = dt.Compute(fastBitmap);
        
        //Convert 2D to 1D - ImageJ Compatibility
        float[] distance1D = new float[distance.length * distance[0].length];
        int p = 0;
        for (int i = 0; i < fastBitmap.getHeight(); i++) {
            for (int j = 0; j < fastBitmap.getWidth(); j++) {
                distance1D[p++] = distance[i][j];
            }
        }
        
        //Make directions offsets
        makeDirectionOffsets(distance[0].length);
        
        int width = fastBitmap.getWidth();
        int height = fastBitmap.getHeight();
        
        FastBitmap back = new FastBitmap(width, height, FastBitmap.ColorSpace.Grayscale);
        
        //Pegue as maxima
        long[] maxPoints = getSortedMaxPoints(distance, distance1D, back, 0, dt.getMaximumDistance(), -808080.0);
        
        //Analise e marque as maxima em imagem de background
        float maxSortingError = 1.1f * SQRT2/2f;
        analyseAndMarkMaxima(distance1D, back, maxPoints, tolerance, maxSortingError);
        
        //Transform em 8bit 0..255
        FastBitmap outImage = make8Bit(distance, back, dt.getMaximumDistance(), -808080.0);
        
        cleanupMaxima(outImage, back, maxPoints);
        watershedSegment(outImage);
        watershedPostProcess(outImage);
        
        fastBitmap.setImage(outImage);
    }
    
    private void makeDirectionOffsets(int width) {
        int shift = 0, mult=1;
        do {
            shift++; mult*=2;
        }
        while (mult < width);
        intEncodeXMask = mult-1;
        intEncodeYMask = ~intEncodeXMask;
        intEncodeShift = shift;
        
        dirOffset  = new int[] {-width, -width+1, +1, +width+1, +width, +width-1,   -1, -width-1 };
        //dirOffset is created last, so check for it being null before makeDirectionOffsets
        //(in case we have multiple threads using the same MaximumFinder)
    }
    
    private long[] getSortedMaxPoints(float[][] distance, float[] distance1D, FastBitmap back, float globalMin, float globalMax, double threshold){
        
        //Create the back image
        byte[] types = back.getGrayData();
        
        int nMax = 0;
        for (int y = 0; y < distance.length; y++) {
            for (int x = 0, i = x+y*distance[0].length; x < distance[0].length; x++, i++) {
                float v = distance[y][x];
                float vTrue = trueEdmHeight(x, y, distance1D, distance[0].length, distance.length);
                if(!(v==globalMin)){
                    if (!(x==0 || x==distance[0].length-1 || y==0 || y==distance.length-1)){
                        if (!(v<threshold)){
                            boolean isMax = true;
                            /* check wheter we have a local maximum.
                             Note: For an EDM, we need all maxima: those of the EDM-corrected values
                             (needed by findMaxima) and those of the raw values (needed by cleanupMaxima) */
                            boolean isInner = (y!=0 && y!=distance.length-1) && (x!=0 && x!=distance[0].length-1); //not necessary, but faster than isWithin
                            for (int d=0; d<8; d++) {                         // compare with the 8 neighbor pixels
                                if (isInner || isWithin(x, y, d, distance[0].length, distance.length)) {
                                    float vNeighbor = distance[y+DIR_Y_OFFSET[d]][x+DIR_X_OFFSET[d]];
                                    float vNeighborTrue = trueEdmHeight(x+DIR_X_OFFSET[d], y+DIR_Y_OFFSET[d], distance1D, distance[0].length, distance.length);
                                    if (vNeighbor > v && vNeighborTrue > vTrue) {
                                        isMax = false;
                                        break;
                                    }
                                }
                            }
                            if (isMax) {
                                types[i] = (byte)1;
                                nMax++;
                            }
                        }
                    }
                }
            }
        }
        
        float vFactor = (float)(2e9/(globalMax-globalMin)); //for converting float values into a 32-bit int
        long[] maxPoints = new long[nMax];                  //value (int) is in the upper 32 bit, pixel offset in the lower
        int iMax = 0;
        for (int y=0; y<distance.length; y++)           //enter all maxima into an array
            for (int x=0, pp=x+y*distance[0].length; x<distance[0].length; x++, pp++)
                if (types[pp]==(byte)1) {
                    float fValue = trueEdmHeight(x,y,distance1D, distance[0].length, distance.length);
                    int iValue = (int)((fValue-globalMin)*vFactor); //32-bit int, linear function of float value
                    maxPoints[iMax++] = (long)iValue<<32|pp;
                }
        Arrays.sort(maxPoints);                                 //sort the maxima by value
        return maxPoints;
        
    }
    
   private void analyseAndMarkMaxima(float[] edmPixels, FastBitmap back, long[] maxPoints, float tolerance, float maxSortingError) {
        int width = back.getWidth();
        int height = back.getHeight();
        byte[] types =  (byte[])back.getGrayData();
        int nMax = maxPoints.length;
        int [] pList = new int[width*height];       //here we enter points starting from a maximum
      
        for (int iMax=nMax-1; iMax>=0; iMax--) {    //process all maxima now, starting from the highest
            int offset0 = (int)maxPoints[iMax];     //type cast gets 32 lower bits, where pixel index is encoded
            //int offset0 = maxPoints[iMax].offset;
            if ((types[offset0]&(byte)4)!=0)      //this maximum has been reached from another one, skip it
                continue;
            //we create a list of connected points and start the list at the current maximum
            int x0 = offset0 % width;               
            int y0 = offset0 / width;
            float v0 = trueEdmHeight(x0,y0,edmPixels, width, height);
            boolean sortingError;
            do {                                    //repeat if we have encountered a sortingError
                pList[0] = offset0;
                types[offset0] |= ((byte)16|(byte)2);   //mark first point as equal height (to itself) and listed
                int listLen = 1;                    //number of elements in the list
                int listI = 0;                      //index of current element in the list
                sortingError = false;       //if sorting was inaccurate: a higher maximum was not handled so far
                boolean maxPossible = true;         //it may be a true maximum
                double xEqual = x0;                 //for creating a single point: determine average over the
                double yEqual = y0;                 //  coordinates of contiguous equal-height points
                int nEqual = 1;                     //counts xEqual/yEqual points that we use for averaging
                do {                                //while neigbor list is not fully processed (to listLen)
                    int offset = pList[listI];
                    int x = offset % width;
                    int y = offset / width;
                    
                    boolean isInner = (y!=0 && y!=height-1) && (x!=0 && x!=width-1); //not necessary, but faster than isWithin
                    for (int d=0; d<8; d++) {       //analyze all neighbors (in 8 directions) at the same level
                        int offset2 = offset+dirOffset[d];
                        if ((isInner || isWithin(x, y, d, width, height)) && (types[offset2]&(byte)2)==0) {
                        if (edmPixels[offset2]<=0) continue;   //ignore the background (non-particles)
                            if ((types[offset2]&(byte)4)!=0) {
                                maxPossible = false; //we have reached a point processed previously, thus it is no maximum now
                                
                                break;
                            }
                            int x2 = x+DIR_X_OFFSET[d];
                            int y2 = y+DIR_Y_OFFSET[d];
                            float v2 = trueEdmHeight(x2, y2, edmPixels, width, height);
                            if (v2 > v0 + maxSortingError) {
                                maxPossible = false;    //we have reached a higher point, thus it is no maximum
                                //if(x0<25&&y0<20)IJ.write("x0,y0="+x0+","+y0+":stop at higher neighbor from x,y="+x+","+y+", dir="+d+",value,value2,v2-v="+v0+","+v2+","+(v2-v0));
                                break;
                            } else if (v2 >= v0-(float)tolerance) {
                                if (v2 > v0) {          //maybe this point should have been treated earlier
                                    sortingError = true;
                                    offset0 = offset2;
                                    v0 = v2;
                                    x0 = x2;
                                    y0 = y2;

                                }
                                pList[listLen] = offset2;
                                listLen++;              //we have found a new point within the tolerance
                                types[offset2] |= (byte)2;
                                if (v2==v0) {           //prepare finding center of equal points (in case single point needed)
                                    types[offset2] |= (byte)16;
                                    xEqual += x2;
                                    yEqual += y2;
                                    nEqual ++;
                                }
                            }
                        } // if isWithin & not (byte)2
                    } // for directions d
                    listI++;
                } while (listI < listLen);

				if (sortingError)  {				  //if x0,y0 was not the true maximum but we have reached a higher one
					for (listI=0; listI<listLen; listI++)
						types[pList[listI]] = 0;	//reset all points encountered, then retry
				} else {
					int resetMask = ~(maxPossible?(byte)2:((byte)2|(byte)16));
					xEqual /= nEqual;
					yEqual /= nEqual;
					double minDist2 = 1e20;
					int nearestI = 0;
					for (listI=0; listI<listLen; listI++) {
						int offset = pList[listI];
						int x = offset % width;
						int y = offset / width;
						types[offset] &= resetMask;		//reset attributes no longer needed
						types[offset] |= (byte)4;		//mark as processed
						if (maxPossible) {
							types[offset] |= (byte)8;
							if ((types[offset]&(byte)16)!=0) {
								double dist2 = (xEqual-x)*(double)(xEqual-x) + (yEqual-y)*(double)(yEqual-y);
								if (dist2 < minDist2) {
									minDist2 = dist2;	//this could be the best "single maximum" point
									nearestI = listI;
								}
							}
						}
					} // for listI
					if (maxPossible) {
						int offset = pList[nearestI];
						types[offset] |= (byte)32;
					}
				} //if !sortingError
			} while (sortingError);				//redo if we have encountered a higher maximum: handle it now.
        } // for all maxima iMax
    }
    
    private FastBitmap make8Bit(float[][] distance, FastBitmap back, float globalMax, double threshold){
        
        int width = distance[0].length;
        int height = distance.length;
        byte[] types = back.getGrayData();
        threshold = 0.5;
        double minValue = 1;
        
        double offset = minValue - (globalMax-minValue)*(1./253/2-1e-6); //everything above minValue should become >(byte)0
        double factor = 253/(globalMax-minValue);
        
        if (factor>1)
            factor = 1;   // with EDM, no better resolution
        
        FastBitmap outIp = new FastBitmap(width, height, FastBitmap.ColorSpace.Grayscale);
        //convert possibly calibrated image to byte without damaging threshold (setMinAndMax would kill threshold)
        byte[] pixels = outIp.getGrayData();
        long v;
        for (int y=0, i=0; y<height; y++) {
            for (int x=0; x<width; x++, i++) {
                float rawValue = distance[y][x];//ip.getPixelValue(x, y);
                if (rawValue<threshold)
                    pixels[i] = (byte)0;
                else if ((types[i]&(byte)8)!=0)
                    pixels[i] = (byte)255;  //prepare watershed by setting "true" maxima+surroundings to 255
                else {
                    v = 1 + Math.round((rawValue-offset)*factor);
                    if (v < 1) pixels[i] = (byte)1;
                    else if (v<=254) pixels[i] = (byte)(v&255);
                    else pixels[i] = (byte)254;
                }
            }
        }
        return outIp;

    }
    
    private void cleanupMaxima(FastBitmap outIp, FastBitmap typeP, long[] maxPoints) {
        int width = outIp.getWidth();
        int height = outIp.getHeight();
        byte[] pixels = outIp.getGrayData();
        byte[] types = typeP.getGrayData();
        int nMax = maxPoints.length;
        int[] pList = new int[width*height];
        for (int iMax = nMax-1; iMax>=0; iMax--) {
            int offset0 = (int)maxPoints[iMax];     //type cast gets lower 32 bits where pixel offset is encoded
            if ((types[offset0]&((byte)8|(byte)64))!=0) continue;
            int level = pixels[offset0]&255;
            int loLevel = level+1;
            pList[0] = offset0;                     //we start the list at the current maximum
            
            types[offset0] |= (byte)2;               //mark first point as listed
            int listLen = 1;                        //number of elements in the list
            int lastLen = 1;
            int listI = 0;                          //index of current element in the list
            boolean saddleFound = false;
            while (!saddleFound && loLevel >0) {
                loLevel--;
                lastLen = listLen;                  //remember end of list for previous level
                listI = 0;                          //in each level, start analyzing the neighbors of all pixels
                do {                                //for all pixels listed so far
                    int offset = pList[listI];
                    int x = offset % width;
                    int y = offset / width;
                    boolean isInner = (y!=0 && y!=height-1) && (x!=0 && x!=width-1); //not necessary, but faster than isWithin
                    for (int d=0; d<8; d++) {       //analyze all neighbors (in 8 directions) at the same level
                        int offset2 = offset+dirOffset[d];
                        if ((isInner || isWithin(x, y, d, width, height)) && (types[offset2]&(byte)2)==0) {
                            if ((types[offset2]&(byte)8)!=0 || (((types[offset2]&(byte)64)!=0) && (pixels[offset2]&255)>=loLevel)) {
                                saddleFound = true; //we have reached a point touching a "true" maximum...
                                //if (xList[0]==122) IJ.write("saddle found at level="+loLevel+"; x,y="+xList[listI]+","+yList[listI]+", dir="+d);
                                break;              //...or a level not lower, but touching a "true" maximum
                            } else if ((pixels[offset2]&255)>=loLevel && (types[offset2]&(byte)64)==0) {
                                pList[listLen] = offset2;
                                //xList[listLen] = x+DIR_X_OFFSET[d];
                                //yList[listLen] = x+DIR_Y_OFFSET[d];
                                listLen++;          //we have found a new point to be processed
                                types[offset2] |= (byte)2;
                            }
                        } // if isWithin & not (byte)2
                    } // for directions d
                    if (saddleFound) break;         //no reason to search any further
                    listI++;
                } while (listI < listLen);
            } // while !levelFound && loLevel>=0
            for (listI=0; listI<listLen; listI++)   //reset attribute since we may come to this place again
                types[pList[listI]] &= ~(byte)2;
            for (listI=0; listI<lastLen; listI++) { //for all points higher than the level of the saddle point
                int offset = pList[listI];
                pixels[offset] = (byte)loLevel;     //set pixel value to the level of the saddle point
                types[offset] |= (byte)64;        //mark as processed: there can't be a local maximum in this area
            }
        } // for all maxima iMax
    }
    
    private boolean watershedSegment(FastBitmap ip) {
        int width = ip.getWidth();
        int height = ip.getHeight();
        byte[] pixels = ip.getGrayData();
        // Create an array with the coordinates of all points between value 1 and 254
        // This method, suggested by Stein Roervik (stein_at_kjemi-dot-unit-dot-no),
        // greatly speeds up the watershed segmentation routine.
        
        ImageStatistics is = new ImageStatistics(ip);
        
        int[] histogram = is.getHistogramGray().getValues();
        int arraySize = width*height - histogram[0] -histogram[255];
        int[] coordinates = new int[arraySize];    //from pixel coordinates, low bits x, high bits y
        int highestValue = 0;
        int maxBinSize = 0;
        int offset = 0;
        int[] levelStart = new int[256];
        for (int v=1; v<255; v++) {
            levelStart[v] = offset;
            offset += histogram[v];
            if (histogram[v] > 0) highestValue = v;
            if (histogram[v] > maxBinSize) maxBinSize = histogram[v];
        }
        int[] levelOffset = new int[highestValue + 1];
        for (int y=0, i=0; y<height; y++) {
            for (int x=0; x<width; x++, i++) {
                int v = pixels[i]&255;
                if (v>0 && v<255) {
                    offset = levelStart[v] + levelOffset[v];
                    coordinates[offset] = x | y<<intEncodeShift;
                    levelOffset[v] ++;
                }
           } //for x
        } //for y
        // Create an array of the points (pixel offsets) that we set to 255 in one pass.
        // If we remember this list we need not create a snapshot of the ImageProcessor. 
        int[] setPointList = new int[Math.min(maxBinSize, (width*height+2)/3)];
        // now do the segmentation, starting at the highest level and working down.
        // At each level, dilate the particle (set pixels to 255), constrained to pixels
        // whose values are at that level and also constrained (by the fateTable)
        // to prevent features from merging.
        int[] table = makeFateTable();
        final int[] directionSequence = new int[] {7, 3, 1, 5, 0, 4, 2, 6}; // diagonal directions first
        for (int level=highestValue; level>=1; level--) {
            int remaining = histogram[level];  //number of points in the level that have not been processed
            int idle = 0;
            while (remaining>0 && idle<8) {
                int dIndex = 0;
                do {                        // expand each level in 8 directions
                    int n = processLevel(directionSequence[dIndex%8], ip, table,
                            levelStart[level], remaining, coordinates, setPointList);
                    //IJ.log("level="+level+" direction="+directionSequence[dIndex%8]+" remain="+remaining+"-"+n);
                    remaining -= n;         // number of points processed
                    if (n > 0) idle = 0;    // nothing processed in this direction?
                    dIndex++;
                } while (remaining>0 && idle++<8);
            }
            if (remaining>0 && level>1) {   // any pixels that we have not reached?
                int nextLevel = level;      // find the next level to process
                do
                    nextLevel--;
                while (nextLevel>1 && histogram[nextLevel]==0);
                // in principle we should add all unprocessed pixels of this level to the
                // tasklist of the next level. This would make it very slow for some images,
                // however. Thus we only add the pixels if they are at the border (of the
                // image or a thresholded area) and correct unprocessed pixels at the very
                // end by CleanupExtraLines
                if (nextLevel > 0) {
                    int newNextLevelEnd = levelStart[nextLevel] + histogram[nextLevel];
                    for (int i=0, p=levelStart[level]; i<remaining; i++, p++) {
                        int xy = coordinates[p];
                        int x = xy&intEncodeXMask;
                        int y = (xy&intEncodeYMask)>>intEncodeShift;
                        int pOffset = x + y*width;
                        boolean addToNext = false;
                        if (x==0 || y==0 || x==width-1 || y==height-1)
                            addToNext = true;           //image border
                        else for (int d=0; d<8; d++)
                            if (isWithin(x, y, d, width, height) && pixels[pOffset+dirOffset[d]]==0) {
                                addToNext = true;       //border of area below threshold
                                break;
                            }
                        if (addToNext)
                            coordinates[newNextLevelEnd++] = xy;
                    }
                    //tasklist for the next level to process becomes longer by this:
                    histogram[nextLevel] = newNextLevelEnd - levelStart[nextLevel];
                }
            }
        }
        return true;
    }
    
    private int processLevel(int pass, FastBitmap ip, int[] fateTable,
            int levelStart, int levelNPoints, int[] coordinates, int[] setPointList) {
        int width = ip.getWidth();
        int height = ip.getHeight();
        int xmax = width - 1;
        int ymax = height - 1;
        byte[] pixels = ip.getGrayData();
        
        int nChanged = 0;
        int nUnchanged = 0;
        for (int i=0, p=levelStart; i<levelNPoints; i++, p++) {
            int xy = coordinates[p];
            int x = xy&intEncodeXMask;
            int y = (xy&intEncodeYMask)>>intEncodeShift;
            int offset = x + y*width;
            int index = 0;      //neighborhood pixel ocupation: index in fateTable
            if (y>0 && (pixels[offset-width]&255)==255)
                index ^= 1;
            if (x<xmax && y>0 && (pixels[offset-width+1]&255)==255)
                index ^= 2;
            if (x<xmax && (pixels[offset+1]&255)==255)
                index ^= 4;
            if (x<xmax && y<ymax && (pixels[offset+width+1]&255)==255)
                index ^= 8;
            if (y<ymax && (pixels[offset+width]&255)==255)
                index ^= 16;
            if (x>0 && y<ymax && (pixels[offset+width-1]&255)==255)
                index ^= 32;
            if (x>0 && (pixels[offset-1]&255)==255)
                index ^= 64;
            if (x>0 && y>0 && (pixels[offset-width-1]&255)==255)
                index ^= 128;
            int mask = 1<<pass;
            if ((fateTable[index]&mask)==mask)
                setPointList[nChanged++] = offset;  //remember to set pixel to 255
            else
                coordinates[levelStart+(nUnchanged++)] = xy; //keep this pixel for future passes

        } // for pixel i
        for (int i=0; i<nChanged; i++)
            pixels[setPointList[i]] = (byte)255;
        return nChanged;
    }
    
    private int[] makeFateTable() {
        int[] table = new int[256];
        boolean[] isSet = new boolean[8];
        for (int item=0; item<256; item++) {        //dissect into pixels
            for (int i=0, mask=1; i<8; i++) {
                isSet[i] = (item&mask)==mask;
                mask*=2;
            }
            for (int i=0, mask=1; i<8; i++) {       //we dilate in the direction opposite to the direction of the existing neighbors
                if (isSet[(i+4)%8]) table[item] |= mask;
                mask*=2;
            }
            for (int i=0; i<8; i+=2)                //if side pixels are set, for counting transitions it is as good as if the adjacent edges were also set
                if (isSet[i]) {
                    isSet[(i+1)%8] = true;
                    isSet[(i+7)%8] = true;
                }
            int transitions=0;
            for (int i=0, mask=1; i<8; i++) {
                if (isSet[i] != isSet[(i+1)%8])
                    transitions++;
            }
            if (transitions>=4) {                   //if neighbors contain more than one region, dilation ito this pixel is forbidden
                table[item] = 0;
            } else {
            }
        }
        return table;
    }
    
    private boolean isWithin(int x, int y, int direction, int width, int height) {
        int xmax = width - 1;
        int ymax = height -1;
        switch(direction) {
            case 0:
                return (y>0);
            case 1:
                return (x<xmax && y>0);
            case 2:
                return (x<xmax);
            case 3:
                return (x<xmax && y<ymax);
            case 4:
                return (y<ymax);
            case 5:
                return (x>0 && y<ymax);
            case 6:
                return (x>0);
            case 7:
                return (x>0 && y>0);
        }
        return false;   //to make the compiler happy :-)
    }
    
    private float trueEdmHeight(int x, int y, float[] pixels, int width, int height){
        int xmax = width - 1;
        int ymax = height - 1;
        int offset = x + y*width;
        float v =  pixels[offset];
        if (x==0 || y==0 || x==xmax || y==ymax || v==0) {
            return v;                               //don't recalculate for edge pixels or background
        } else {
            float trueH = v + 0.5f*SQRT2;           //true height can never by higher than this
            boolean ridgeOrMax = false;
            for (int d=0; d<4; d++) {               //for all directions halfway around:
                int d2 = (d+4)%8;                   //get the opposite direction and neighbors
                float v1 = pixels[offset+dirOffset[d]];
                float v2 = pixels[offset+dirOffset[d2]];
                float h;
                if (v>=v1 && v>=v2) {
                    ridgeOrMax = true;
                    h = (v1 + v2)/2;
                } else {
                    h = Math.min(v1, v2);
                }
                h += (d%2==0) ? 1 : SQRT2;          //in diagonal directions, distance is sqrt2
                if (trueH > h) trueH = h;
            }
            if (!ridgeOrMax) trueH = v;
            return trueH;
        }
    }
    
    private static void watershedPostProcess(FastBitmap ip) {
        byte[] pixels = ip.getGrayData();
        int size = ip.getWidth()*ip.getHeight();
        for (int i=0; i<size; i++) {
           if ((pixels[i]&255)<255)
                pixels[i] = (byte)0;
        }
    }
    
}