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

package Catalano.Imaging.Tools;

import Catalano.Core.IntPoint;
import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.Filters.DistanceTransform;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Ultimate Eroded Points (UEP).
 * 
 * 
 * @author Diego Catalano
 */
public class UltimateErodedPoints {
    
    private final int[] DIR_X_OFFSET = new int[] {  0,  1,  1,  1,  0, -1, -1, -1 };
    private final int[] DIR_Y_OFFSET = new int[] { -1, -1,  0,  1,  1,  1,  0, -1 };
    private int[]     dirOffset;
    private final float SQRT2 = 1.4142135624f;
    private float tolerance = 0.5f;
    private int       intEncodeXMask;               // needed for encoding x & y in a single int (watershed): mask for x
//    private int       intEncodeYMask;               // needed for encoding x & y in a single int (watershed): mask for y
//    private int       intEncodeShift;               // needed for encoding x & y in a single int (watershed): shift of y

    public UltimateErodedPoints() {}
    
    public List<IntPoint> Process(FastBitmap fastBitmap){
        
        if(!fastBitmap.isGrayscale())
            throw new IllegalArgumentException("UED only works in grayscale images.");
        
        DistanceTransform dt = new DistanceTransform();
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
        
        //Get all maximum points
        long[] maxPoints = getSortedMaxPoints(distance, distance1D, back, 0, dt.getMaximumDistance(), -808080.0);
        
        //Analise e marque as maxima em imagem de background
        float maxSortingError = 1.1f * SQRT2/2f;
        return analyseAndMarkMaxima(distance1D, back, maxPoints, tolerance, maxSortingError);
        
    }
    
    private void makeDirectionOffsets(int width) {
        int shift = 0, mult=1;
        do {
            shift++; mult*=2;
        }
        while (mult < width);
        intEncodeXMask = mult-1;
//        intEncodeYMask = ~intEncodeXMask;
//        intEncodeShift = shift;
        
        dirOffset  = new int[] {-width, -width+1, +1, +width+1, +width, +width-1,   -1, -width-1 };
        //dirOffset is created last, so check for it being null before makeDirectionOffsets
        //(in case we have multiple threads using the same MaximumFinder)
    }
    
    private long[] getSortedMaxPoints(float[][] distance, float[] distance1D, FastBitmap back, float globalMin, float globalMax, double threshold){
        
        //Create the back image
        int[] types = back.getData();
        
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
    
   private List<IntPoint> analyseAndMarkMaxima(float[] edmPixels, FastBitmap back, long[] maxPoints, float tolerance, float maxSortingError) {
       
       List<IntPoint> uep = new ArrayList<IntPoint>();
       int width = back.getWidth();
        int height = back.getHeight();
        int[] types =  back.getData();
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
                                                uep.add(new IntPoint(offset / width, offset % width));
						types[offset] |= (byte)32;
					}
				} //if !sortingError
			} while (sortingError);				//redo if we have encountered a higher maximum: handle it now.
        } // for all maxima iMax
        return uep;
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
    
}
