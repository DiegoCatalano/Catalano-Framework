// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
// Copyright © Andrew Kirillov, 2007-2008
// andrew.kirillov at gmail.com
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

import Catalano.Core.IntRange;
import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.IApplyInPlace;

/**
 * Additive noise filter.
 * 
 * <p>The filter adds random value to each pixel of the source image.</p>
 * 
 * <p><b>Properties:</b>
 * <li>Supported types: Grayscale, RGB.
 * <br><li>Coordinate System: Matrix.</p>
 * 
 * @author Diego Catalano
 */
public class AdditiveNoise implements IApplyInPlace{
    
    private int min = -10;
    private int max = 10;

    /**
     * Get minimum value.
     * @return Minimum value.
     */
    public int getMin() {
        return min;
    }

    /**
     * Set minimum value.
     * @param min Minimum value.
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * Get maximum value.
     * @return Maximum value.
     */
    public int getMax() {
        return max;
    }

    /**
     * Set maximum value.
     * @param max Maximum value.
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * Initialize a new instance of the AdditiveNoise class.
     */
    public AdditiveNoise() {}
    
    /**
     * Initialize a new instance of the AdditiveNoise class.
     * @param min Min value.
     * @param max Max value.
     */
    public AdditiveNoise(int min, int max){
        this.min = min;
        this.max = max;
    }
    
    /**
     * Initialize a new instance of the AdditiveNoise class.
     * @param range Range.
     */
    public AdditiveNoise(IntRange range){
        this.min = range.getMin();
        this.max = range.getMax();
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        if (fastBitmap.isGrayscale()){
            
            int size = fastBitmap.getSize();
            
            for (int i = 0; i < size; i++) {
                int g = fastBitmap.getGray(i);
                g = Math.min(255, Math.max(0, g + generateNumber(min, max)));
                fastBitmap.setGray(i, g);
            }
            
        }
        else if (fastBitmap.isRGB()){
            int size = fastBitmap.getSize();
            
            for (int i = 0; i < size; i++) {
                int r = fastBitmap.getRed(i);
                int g = fastBitmap.getGreen(i);
                int b = fastBitmap.getBlue(i);

                r = Math.min(255, Math.max(0, r + generateNumber(min, max)));
                g = Math.min(255, Math.max(0, g + generateNumber(min, max)));
                b = Math.min(255, Math.max(0, b + generateNumber(min, max)));

                fastBitmap.setRGB(i, r, g, b);
            }
        }
        else{
            throw new IllegalArgumentException("Additive Noise only supports Grayscale and RGB images.");
        }
        
    }
    
    /**
     * Generate a number between min and max value.
     * @param range Range.
     * @return Number generated.
     */
    private int generateNumber(int min, int max){
        return Math.min(min, max) + (int)Math.round(-0.5f+(1+Math.abs(min - max))*Math.random());
    }
    
}