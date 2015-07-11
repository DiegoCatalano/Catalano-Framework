// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2015
// diego.catalano at live.com
//
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
import Catalano.Imaging.IBaseInPlace;

/**
 * Unsharp Filter.
 * @author Diego Catalano
 */
public class UnsharpMasking implements IBaseInPlace{
    
    private int radius = 1;
    private float weight = 0.6f;

    /**
     * Get Radius.
     * @return Radius.
     */
    public int getRadius() {
        return radius;
    }

    /**
     * Set Radius.
     * @param radius Radius.
     */
    public void setRadius(int radius) {
        this.radius = Math.max(radius, 1);
    }

    /**
     * Get Weight.
     * @return Weight.
     */
    public float getWeight() {
        return weight;
    }

    /**
     * Set Weight.
     * @param weight Weight.
     */
    public void setWeight(float weight) {
        this.weight = Math.max(0, Math.min(weight, 1));
    }

    /**
     * Initialize a new instance of the UnsharpMasking class.
     */
    public UnsharpMasking() {}

    /**
     * Initialize a new instance of the UnsharpMasking class.
     * @param radius Radius.
     * @param weight Weight.
     */
    public UnsharpMasking(int radius, float weight) {
        setRadius(radius);
        setWeight(weight);
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        FastBitmap blur = new FastBitmap(fastBitmap);
        GaussianBoxBlur gb = new GaussianBoxBlur(radius);
        gb.applyInPlace(blur);
        
        int width = fastBitmap.getWidth();
        int height = fastBitmap.getHeight();
        
        if(fastBitmap.isGrayscale()){
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    int g = (int)((fastBitmap.getGray(i, j) - weight*(float)blur.getGray(i, j)) / (1f - weight));
                    g = g < 0 ? 0 : g;
                    g = g > 255 ? 255 : g;
                    fastBitmap.setGray(i, j, g);
                }
            }
            
        }
        else if (fastBitmap.isRGB()){
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    int r = (int)((fastBitmap.getRed(i, j) - weight*(float)blur.getRed(i, j)) / (1f - weight));
                    int g = (int)((fastBitmap.getGreen(i, j) - weight*(float)blur.getGreen(i, j)) / (1f - weight));
                    int b = (int)((fastBitmap.getBlue(i, j) - weight*(float)blur.getBlue(i, j)) / (1f - weight));
                    
                    r = r < 0 ? 0 : r;
                    g = g < 0 ? 0 : g;
                    b = b < 0 ? 0 : b;
                    
                    r = r > 255 ? 255 : r;
                    g = g > 255 ? 255 : g;
                    b = b > 255 ? 255 : b;
                    
                    fastBitmap.setRGB(i, j, r, g, b);
                }
            }
        }
        else{
            throw new IllegalArgumentException("Unsharp mask only works in grayscale or rgb images.");
        }
    }
}