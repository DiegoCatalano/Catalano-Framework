// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
// Copyright © Andrew Kirillov, 2007-2008
// andrew.kirillov at gmail.com
//
// Alejandro Pirola, 2008
// alejamp@gmail.com
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

import Catalano.Imaging.FastBitmap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Skew angle checker for scanned documents.
 * @author Diego Catalano
 */
public class DocumentSkewChecker {
    
    // Hough transformation: quality settings
    private int     stepsPerDegree;
    private int     houghHeight;
    private double  thetaStep;
    private double  maxSkewToDetect;

    // Hough transformation: precalculated Sine and Cosine values
    private double[]	sinMap;
    private double[]	cosMap;
    private boolean     needToInitialize = true;

    // Hough transformation: Hough map
    private int[][]	      houghMap;
    private int		      maxMapIntensity = 0;

    private int 	      localPeakRadius = 4;
    private List<HoughLine>   lines = new ArrayList<HoughLine>();

    /**
     * Get steps per degree.
     * @return Steps per degree.
     */
    public int getStepsPerDegree() {
        return stepsPerDegree;
    }

    /**
     * Set steps per degree, [1, 10].
     * 
     * The value defines quality of Hough transform and its ability to detect
     * line slope precisely.
     * 
     * Default value is set to 1.
     * @param stepsPerDegree Steps per degree.
     */
    public void setStepsPerDegree(int stepsPerDegree) {
        this.stepsPerDegree = Math.max( 1, Math.min( 10, stepsPerDegree ) );
    }

    /**
     * Get maximum skew angle to detect.
     * Default value is set to 30.
     * @return Maximum skew angle to detect.
     */
    public double getMaxSkewToDetect() {
        return maxSkewToDetect;
    }

    /**
     * Maximum skew angle to detect, [0, 45] degrees.
     * The value sets maximum document's skew angle to detect.
     * Document's skew angle can be as positive (rotated counter clockwise), as negative
     * (rotated clockwise). So setting this value to 25, for example, will lead to
     * [-25, 25] degrees detection range.
     * 
     * Scanned documents usually have skew in the [-20, 20] degrees range.
     *
     * @param maxSkewToDetect Maximum skew to detect.
     */
    public void setMaxSkewToDetect(double maxSkewToDetect) {
        this.maxSkewToDetect = Math.max( 0, Math.min( 45, maxSkewToDetect ) );;
    }

    /**
     * Get local peak radius.
     * Default value is set to 4.
     * @return Local peak radius.
     */
    public int getLocalPeakRadius() {
        return localPeakRadius;
    }

    /**
     * Radius for searching local peak value, [1, 10].
     * 
     * The value determines radius around a map's value, which is analyzed to determine
     * if the map's value is a local maximum in specified area.
     * 
     * @param localPeakRadius Local peak radius.
     */
    public void setLocalPeakRadius(int localPeakRadius) {
        this.localPeakRadius = Math.max( 1, Math.min( 10, localPeakRadius ) );;
    }

    /**
     * Initializes a new instance of the DocumentSkewChecker class.
     */
    public DocumentSkewChecker() {
        this.stepsPerDegree = 10;
        this.maxSkewToDetect = 30;
    }
    
    /**
     * Get skew angle of the provided document image.
     * @param fastBitmap FastBitmap.
     * @return Returns document's skew angle. If the returned angle equals to -90, then document skew detection has failed.
     */
    public double getSkewAngle(FastBitmap fastBitmap){
        
        if(fastBitmap.isGrayscale()){
            // init hough transformation settings
            InitHoughMap();

            // get source image size
            int width       = fastBitmap.getWidth();
            int height      = fastBitmap.getHeight();
            int halfWidth   = width / 2;
            int halfHeight  = height / 2;

            // make sure the specified rectangle recides with the source image
            //rect.Intersect( new Rectangle( 0, 0, width, height ) );

            int startX = -halfWidth;
            int startY = -halfHeight;
            int stopX  = width - halfWidth;
            int stopY  = height - halfHeight - 1;

            // calculate Hough map's width
            int halfHoughWidth = (int) Math.sqrt( halfWidth * halfWidth + halfHeight * halfHeight );
            int houghWidth = halfHoughWidth * 2;

            houghMap = new int[houghHeight][houghWidth];
            
            int indexG = 0;
            for (int y = startY; y < stopY; y++) {
                for (int x = startX; x < stopX; x++,indexG++) {
                    if ( ( fastBitmap.getGray(indexG) < 128 ) && ( fastBitmap.getGray(indexG+width) >= 128 ) ){
                        // for each Theta value
                        for ( int theta = 0; theta < houghHeight; theta++ ){
                            int radius = (int) ( cosMap[theta] * x - sinMap[theta] * y ) + halfHoughWidth;

                            if ( ( radius < 0 ) || ( radius >= houghWidth ) )
                                continue;

                            houghMap[theta][radius]++;
                        }
                    }
                }
            }
            
            // find max value in Hough map
            maxMapIntensity = 0;
            for ( int i = 0; i < houghHeight; i++ )
            {
                for ( int j = 0; j < houghWidth; j++ )
                {
                    if ( houghMap[i][j] > maxMapIntensity )
                    {
                        maxMapIntensity = houghMap[i][j];
                    }
                }
            }
            
            CollectLines( ( width / 10 ) );
            
            // get skew angle
            HoughLine[] hls = this.GetMostIntensiveLines( 5 );

            double skewAngle = 0;
            double sumIntensity = 0;
            
            for ( HoughLine hl : hls )
            {
                if ( hl.getRelativeIntensity() > 0.5 )
                {
                    skewAngle += ( hl.getTheta() * hl.getRelativeIntensity() );
                    sumIntensity += hl.getRelativeIntensity();
                }
            }
            if ( hls.length > 0 ) skewAngle = skewAngle / sumIntensity;

            return skewAngle - 90.0;
            
        }
        else{
            throw new IllegalArgumentException("Document Skew Checker only works in grayscale images.");
        }
        
    }
    
    // Collect lines with intesities greater or equal then specified
    private void CollectLines( int minLineIntensity ){
        int		maxTheta = houghMap.length;
        int		maxRadius = houghMap[0].length;

        int	intensity;
        boolean	foundGreater;

        int     halfHoughWidth = maxRadius >> 1;

        // clean lines collection
        lines.clear( );

        // for each Theta value
        for ( int theta = 0; theta < maxTheta; theta++ ){
            // for each Radius value
            for ( int radius = 0; radius < maxRadius; radius++ ){
                // get current value
                intensity = houghMap[theta][radius];

                if ( intensity < minLineIntensity )
                    continue;

                foundGreater = false;

                // check neighboors
                for ( int tt = theta - localPeakRadius, ttMax = theta + localPeakRadius; tt < ttMax; tt++ ){
                    // skip out of map values
                    if ( tt < 0 )
                        continue;
                    if ( tt >= maxTheta )
                        break;

                    // break if it is not local maximum
                    if ( foundGreater == true )
                        break;

                    for ( int tr = radius - localPeakRadius, trMax = radius + localPeakRadius; tr < trMax; tr++ ) {
                        // skip out of map values
                        if ( tr < 0 )
                            continue;
                        if ( tr >= maxRadius )
                            break;

                        // compare the neighboor with current value
                        if ( houghMap[tt][tr] > intensity ){
                            foundGreater = true;
                            break;
                        }
                    }
                }

                // was it local maximum ?
                if ( !foundGreater ){
                    // we have local maximum
                    lines.add( new HoughLine( 90.0 - maxSkewToDetect + (double) theta / stepsPerDegree, (int) ( radius - halfHoughWidth ), intensity, (double) intensity / maxMapIntensity ) );
                }
            }
        }
        Collections.sort(lines);
    }
        
    private HoughLine[] GetMostIntensiveLines( int count )
    {
        // lines count
        int n = Math.min( count, lines.size() );

        // result array
        HoughLine[] dst = new HoughLine[n];
        for (int i = 0; i < n; i++) {
            dst[i] = lines.get(i);
        }

        return dst;
    }
    
    // Init Hough settings and map
    private void InitHoughMap( ){
        if ( needToInitialize ){
            needToInitialize = false;

            this.houghHeight = (int) ( 2 * maxSkewToDetect * stepsPerDegree );
            this.thetaStep = ( 2 * maxSkewToDetect * Math.PI / 180 ) / houghHeight;

            // precalculate Sine and Cosine values
            this.sinMap = new double[houghHeight];
            this.cosMap = new double[houghHeight];

            double minTheta = 90.0 - maxSkewToDetect;

            for ( int i = 0; i < houghHeight; i++ ){
                sinMap[i] = Math.sin( ( minTheta * Math.PI / 180 ) + ( i * thetaStep ) );
                cosMap[i] = Math.cos( ( minTheta * Math.PI / 180 ) + ( i * thetaStep ) );
            }
        }
    }
}
