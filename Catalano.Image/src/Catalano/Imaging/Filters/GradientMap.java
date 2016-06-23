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
 * Gradient Map.
 * 
 * <p> Apply LUTs in the image. </p>
 * 
 * @author Diego Catalano
 */
public class GradientMap implements IApplyInPlace{
    
    private FastBitmap gradient;
    private int[][] lut;
    private boolean useLut = false;
    private boolean invert = false;

    /**
     * Get Gradient image.
     * @return Gradient image.
     */
    public FastBitmap getGradient() {
        return gradient;
    }

    /**
     * Set Gradient image.
     * @param gradient Gradient image.
     */
    public void setGradient(FastBitmap gradient) {
        this.gradient = gradient;
        this.useLut = false;
    }

    /**
     * Get Gradient LUT.
     * @return Gradient LUT.
     */
    public int[][] getLut() {
        return lut;
    }

    /**
     * Set Gradient LUT.
     * @param lut Gradient LUT.
     */
    public void setLut(int[][] lut) {
        this.lut = lut;
        this.useLut = true;
    }

    /**
     * Verify if the lut is inverted.
     * @return True if the lut is inverted, otherwise false.
     */
    public boolean isInvert() {
        return invert;
    }

    /**
     * Invert LUT.
     * @param invert Invert lut.
     */
    public void setInvert(boolean invert) {
        this.invert = invert;
    }
    
    /**
     * Initialize a new instance of the GradientMap class.
     * @param gradient Gradient image.
     */
    public GradientMap(FastBitmap gradient) {
        this.gradient = gradient;
    }
    
    /**
     * Initialize a new instance of the GradientMap class.
     * 
     * @param lut Gradient LUT.
     */
    public GradientMap(int[][] lut) {
        this(lut, false);
    }
    
    /**
     * Initialize a new instance of the GradientMap class.
     * 
     * @param lut Gradient LUT.
     */
    public GradientMap(int[][] lut, boolean invert) {
        setLut(lut);
        this.invert = invert;
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        if (fastBitmap.isGrayscale()){
            
            if(!useLut)
                lut = LUT(gradient);

            int width = fastBitmap.getWidth();
            int height = fastBitmap.getHeight();

            fastBitmap.toRGB();
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {

                    int r = fastBitmap.getRed(i, j);
                    if(!invert)
                        fastBitmap.setRGB(i, j, lut[r][1], lut[r][2], lut[r][3]);
                    else
                        fastBitmap.setRGB(i, j, lut[255 - r][1], lut[255 - r][2], lut[255 - r][3]);

                }
            }
            
        }
        else{
            throw new IllegalArgumentException("Gradient Map only works in grayscale images.");
        }
        
    }
    
    /**
     * Compute LUT.
     * @param gradient Image contains gradient.
     * @return LUT.
     */
    private int[][] LUT(FastBitmap gradient){
        
        int[][] lut = new int[256][3];
        
        if(gradient.getWidth() == 256){
            for (int j = 0; j < 256; j++) {
                lut[j][0] = gradient.getRed(0, j);
                lut[j][1] = gradient.getGreen(0, j);
                lut[j][2] = gradient.getBlue(0, j);
            }
        }
        else{
            throw new IllegalArgumentException("Gradient Map needs at least a gradient image with 1 row and exactly 256 columns.");
        }
        
        return lut;
    }
}