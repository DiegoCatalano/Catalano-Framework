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
import Catalano.Math.Matrix;

/**
 * Ensemble Threshold.
 * The filter does image binarization using ensemble approach.
 * Perform several threshold operations and compute majority vote in the response.
 * 
 * Supported types: Grayscale.
 * Coordinate System: Independent.
 * 
 * @author Diego Catalano
 */
public class EnsembleThreshold implements IApplyInPlace{
    private int[] threshold;
    private boolean invert;

    /**
     * Get threshold values.
     * @return Threshold values.
     */
    public int[] getThreshold() {
        return threshold;
    }

    /**
     * Set threshold values.
     * @param threshold Threshold values.
     */
    public void setThreshold(int[] threshold) {
        this.threshold = threshold;
    }

    /**
     * Check if is inverted.
     * @return True if invert the threshold, otherwise false.
     */
    public boolean isInvert() {
        return invert;
    }

    /**
     * Set invert.
     * @param invert True if need to invert the threshold.
     */
    public void setInvert(boolean invert) {
        this.invert = invert;
    }

    /**
     * Initializes a new instance of the EnsembleThreshold class.
     * @param threshold Threshold values.
     */
    public EnsembleThreshold(int[] threshold) {
        this(threshold, false);
    }

    /**
     * Initializes a new instance of the EnsembleThreshold class.
     * @param threshold Threshold values.
     * @param invert Invert the threshold.
     */
    public EnsembleThreshold(int[] threshold, boolean invert) {
        this.threshold = threshold;
        this.invert = invert;
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        if(fastBitmap.isGrayscale()){
            
            int[] map;
            int size = fastBitmap.getSize();
            for (int i = 0; i < size; i++) {
                int g = fastBitmap.getGray(i);
                map = new int[2];
                for (int j = 0; j < threshold.length; j++) {
                    if(invert == false){
                        if(g >= threshold[j])
                            map[1]++;
                        else
                            map[0]++;
                    }
                    else{
                        if(g >= threshold[j])
                            map[0]++;
                        else
                            map[1]++;
                    }
                }
                
                //Majority vote
                int index = Matrix.MaxIndex(map);
                if(index == 0)
                    fastBitmap.setGray(i, 0);
                else
                    fastBitmap.setGray(i, 255);
            }
            
        }
        else{
            throw new IllegalArgumentException("Ensemble Threshold only works in grayscale images.");
        }
    }
}