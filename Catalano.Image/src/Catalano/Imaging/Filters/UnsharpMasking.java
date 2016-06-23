// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
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
import Catalano.Imaging.IApplyInPlace;

/**
 * Unsharp Filter.
 * @author Diego Catalano
 */
public class UnsharpMasking implements IApplyInPlace{
    
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
        
        int size = fastBitmap.getSize();
        if(fastBitmap.isGrayscale()){
            for (int i = 0; i < size; i++) {
                int g = (int)((fastBitmap.getGray(i) - weight*(float)blur.getGray(i)) / (1f - weight));
                fastBitmap.setGray(i, fastBitmap.clampValues(g, 0, 255));
            }
            
        }
        else if (fastBitmap.isRGB()){
            for (int i = 0; i < size; i++) {
                int r = (int)((fastBitmap.getRed(i) - weight*(float)blur.getRed(i)) / (1f - weight));
                int g = (int)((fastBitmap.getGreen(i) - weight*(float)blur.getGreen(i)) / (1f - weight));
                int b = (int)((fastBitmap.getBlue(i) - weight*(float)blur.getBlue(i)) / (1f - weight));

                r = fastBitmap.clampValues(r, 0, 255);
                g = fastBitmap.clampValues(g, 0, 255);
                b = fastBitmap.clampValues(b, 0, 255);

                fastBitmap.setRGB(i, r, g, b);
            }
        }
        else{
            throw new IllegalArgumentException("Unsharp mask only works in grayscale or rgb images.");
        }
    }
}