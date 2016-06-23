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
 * High Boost Filter.
 * @author Diego Catalano
 */
public class HighBoost implements IApplyInPlace{
    private int[][] kernel = {
        {-1, -1, -1},
        {-1, 8, -1},
        {-1, -1, -1}};
    private int boost = 8;
    private int windowSize = 3;

    /**
     * Get Boost value.
     * @return boost value.
     */
    public int getBoost() {
        return boost;
    }
    
    /**
     * Set Boost value.
     * @param boost boost value.
     */
    public void setBoost(int boost){
        this.boost = boost;
        kernel[kernel.length / 2][kernel[0].length / 2] = boost;
    }

    /**
     * Get Window size.
     * @return Window size.
     */
    public int getWindowSize() {
        return windowSize;
    }

    /**
     * Set Window size.
     * @param windowSize Window size.
     */
    public void setWindowSize(int windowSize) {
        this.windowSize = windowSize;
        kernel = new int[windowSize][windowSize];
        for (int i = 0; i < kernel.length; i++) {
            for (int j = 0; j < kernel[0].length; j++) {
                kernel[i][j] = - 1;
            }
        }
        setBoost(boost);
    }

    /**
    * Initializes a new instance of the HighBoost class.
    */
    public HighBoost() {}
    
    /**
     * Initializes a new instance of the HighBoost class.
     * @param boost Bopst value.
     */
    public HighBoost(int boost) {
        setBoost(boost);
    }
    
    /**
     * Initializes a new instance of the HighBoost class.
     * @param windowSize Window size.
     * @param boost Boost value.
     */
    public HighBoost(int windowSize, int boost) {
        setWindowSize(windowSize);
        setBoost(boost);
    }
        
    /**
     * Apply filter to a FastBitmap.
     * @param fastBitmap Image to be processed.
     */
    @Override
    public void applyInPlace(FastBitmap fastBitmap){
        Convolution c = new Convolution(kernel);
        c.applyInPlace(fastBitmap);
    }    
}