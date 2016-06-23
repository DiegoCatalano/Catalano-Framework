// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
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

/**
 * Hysteresis Threshold.
 * 
 * <para>Hysteresis thresholding for edge detection using two thresholds.
 * Low thresholded edges which are connected to high thresholded edges are retained.
 * Low thresholded edges which are non connected to high thresholded edges are removed.</para>
 * @author Diego Catalano
 */
public class HysteresisThreshold implements IApplyInPlace{
    
    int lowThreshold = 20;
    int highThreshold = 100;

    /**
     * Get Low threshold.
     * @return Threshold value.
     */
    public int getLowThreshold() {
        return lowThreshold;
    }

    /**
     * Set Low threshold.
     * @param lowThreshold Threshold value.
     */
    public void setLowThreshold(int lowThreshold) {
        this.lowThreshold = lowThreshold;
    }

    /**
     * Get High threshold.
     * @return Threshold value.
     */
    public int getHighThreshold() {
        return highThreshold;
    }

    /**
     * Set High threshold.
     * @param highThreshold Threshold value.
     */
    public void setHighThreshold(int highThreshold) {
        this.highThreshold = highThreshold;
    }

    /**
     * Initialize a new instance of the HysteresisThreshold class.
     */
    public HysteresisThreshold() {}

    /**
     * Initialize a new instance of the HysteresisThreshold class.
     * @param lowThreshold Low threshold.
     * @param highThreshold High threshold.
     */
    public HysteresisThreshold(int lowThreshold, int highThreshold) {
        this.lowThreshold = lowThreshold;
        this.highThreshold = highThreshold;
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        if (fastBitmap.isGrayscale()){
            int width = fastBitmap.getWidth();
            int height = fastBitmap.getHeight();
            
            for (int i = 1; i < height - 1; i++) {
                for (int j = 1; j < width - 1; j++) {
                    if ( fastBitmap.getGray(i, j) < highThreshold ){
                        if ( fastBitmap.getGray(i, j) < lowThreshold ){
                            // non edge
                            fastBitmap.setGray(i, j, 0);
                        }
                        else{
                            
                            // check 8 neighboring pixels
                            if (( fastBitmap.getGray(i, j - 1) < highThreshold ) &&
                                ( fastBitmap.getGray(i, j + 1) < highThreshold ) &&
                                ( fastBitmap.getGray(i - 1, j - 1) < highThreshold ) &&
                                ( fastBitmap.getGray(i - 1, j) < highThreshold ) &&
                                ( fastBitmap.getGray(i - 1, j + 1) < highThreshold ) &&
                                ( fastBitmap.getGray(i + 1, j - 1) < highThreshold ) &&
                                ( fastBitmap.getGray(i + 1, j) < highThreshold ) &&
                                ( fastBitmap.getGray(i + 1, j + 1) < highThreshold ) )
                            {
                                fastBitmap.setGray(i, j, 0);
                            }
                        }
                    }
                }
            }
            
        }
        else{
            throw new IllegalArgumentException("Hysteresis Threshold only works with grayscale images.");
        }
    }
}