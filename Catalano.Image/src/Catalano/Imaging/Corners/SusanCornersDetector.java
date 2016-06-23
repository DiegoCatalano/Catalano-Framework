// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalan at live.com
//
// Copyright © Andrew Kirillov, 2007-2013
// andrew.kirillov@gmail.com
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

package Catalano.Imaging.Corners;

import Catalano.Core.IntPoint;
import Catalano.Imaging.FastBitmap;
import java.util.ArrayList;
import java.util.List;

/**
 * Susan corners detector.
 * The class implements Susan corners detector, which is described by S.M. Smith in: <b>S.M. Smith, "SUSAN - a new approach to low level image processing", Internal Technical Report TR95SMS1, Defense Research Agency, Chobham Lane, Chertsey, Surrey, UK, 1995</b>.
 * @author Diego Catalano
 */
public class SusanCornersDetector implements ICornersDetector{
    
    private int differenceThreshold = 25;
    private int geometricalThreshold = 18;
    private List<IntPoint> corners = new ArrayList<IntPoint>();
    private int[] rowRadius = { 1, 2, 3, 3, 3, 2, 1 };

    /**
     * Initialize a new instance of the SusanCornersDetector class.
     */
    public SusanCornersDetector() {
        
    }

    /**
     * Initialize a new instance of the SusanCornersDetector class.
     * @param differenceThreshold Brightness difference threshold.
     * @param geometricalThreshold Geometrical threshold.
     */
    public SusanCornersDetector(int differenceThreshold, int geometricalThreshold) {
        this.differenceThreshold = differenceThreshold;
        this.geometricalThreshold = geometricalThreshold;
    }

    /**
     * Get Brightness difference threshold.
     * @return Brightness difference threshold.
     */
    public int getDifferenceThreshold() {
        return differenceThreshold;
    }

    /**
     * Set Brightness difference threshold.
     * @param differenceThreshold Brightness difference threshold.
     */
    public void setDifferenceThreshold(int differenceThreshold) {
        this.differenceThreshold = differenceThreshold;
    }

    /**
     * Get Geometrical threshold.
     * @return Geometrical threshold.
     */
    public int getGeometricalThreshold() {
        return geometricalThreshold;
    }

    /**
     * Set Geometrical threshold.
     * @param geometricalThreshold Geometrical threshold.
     */
    public void setGeometricalThreshold(int geometricalThreshold) {
        this.geometricalThreshold = geometricalThreshold;
    }
    
    /**
     * Process image looking for corners.
     * @param fastBitmap FastBitmap for find corners.
     * @return A list of points considered corners.
     */
    @Override
    public List<IntPoint> ProcessImage(FastBitmap fastBitmap){
        
        FastBitmap l = new FastBitmap(fastBitmap);
        
        if (fastBitmap.isRGB()) l.toGrayscale();
        
        int width = l.getWidth();
        int height = l.getHeight();
        int[][] susanMap = new int[height][width];
        
        for (int x = 3; x < height - 3; x++) {
            for (int y = 3; y < width - 3; y++) {
                int nucleusValue = l.getGray(x, y);
                int usan = 0;
                int cx = 0, cy = 0;
                for (int i = -3; i <= 3; i++) {
                    int r = rowRadius[i + 3];
                    for ( int j = -r; j <= r; j++ ){
                        int gray = l.getGray(x + i, y + j);
                        // differenceThreshold
                        if ( Math.abs( nucleusValue - gray ) <= differenceThreshold ){
                            usan++;
                            cx += x + j;
                            cy += y + i;
                        }
                    }
                }
                
                // check usan size
                if ( usan < geometricalThreshold ){
                    cx /= usan;
                    cy /= usan;

                    if ( ( x != cx ) || ( y != cy ) ){
                        usan = ( geometricalThreshold - usan );
                    }
                    else{
                        usan = 0;
                    }
                }
                else{
                    usan = 0;
                }

                // usan = ( usan < geometricalThreshold ) ? ( geometricalThreshold - usan ) : 0;
                susanMap[x][y] = usan;
                }
            }
        
            // for each row
            for ( int x = 2; x < height - 2; x++ ){
                // for each pixel
                for ( int y = 2; y < width - 2; y++ ){
                    int currentValue = susanMap[x][y];

                    // for each windows' row
                    for ( int i = -2; ( currentValue != 0 ) && ( i <= 2 ); i++ ){
                        // for each windows' pixel
                        for ( int j = -2; j <= 2; j++ ){
                            if ( susanMap[x+i][y+j] > currentValue ){
                                currentValue = 0;
                                break;
                            }
                        }
                    }

                    // check if this point is really interesting
                    if ( currentValue != 0 ){
                        corners.add( new IntPoint( x, y ) );
                    }
                }
            }
            return corners;
        }
    }