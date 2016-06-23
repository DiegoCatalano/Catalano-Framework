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

package Catalano.Imaging.Filters.Integral;

import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.IApplyInPlace;
import Catalano.Imaging.Tools.IntegralImage;

/**
 * Variance filter using integral images.
 * @author Diego Catalano
 */
public class IntegralVariance implements IApplyInPlace{
    
    private int radius;
    
    /**
     * Get radius.
     * @return Radius.
     */
    public int getRadius() {
        return radius;
    }

    /**
     * Set radius.
     * @param radius Radius.
     */
    public void setRadius(int radius) {
        this.radius = Math.max(1, radius);
    }

    /**
     * Initialize a new instance of the IntegralVariance class.
     */
    public IntegralVariance() {
        this(1);
    }

    /**
     * Initialize a new instance of the IntegralVariance class.
     * @param radius Radius.
     */
    public IntegralVariance(int radius) {
        this.radius = radius;
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        if(fastBitmap.isGrayscale()){
            //Compute the integral image with power 1.
            IntegralImage intImage = new IntegralImage(fastBitmap);

            //Compute the integral image with power 2.
            IntegralImage intImage2 = new IntegralImage(fastBitmap, 2);

            int width = fastBitmap.getWidth();
            int height = fastBitmap.getHeight();
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    float m1 = intImage.getRectangleMean(i, j, radius);
                    float m2 = intImage2.getRectangleMean(i, j, radius);
                    float val = m2 - (m1*m1);
                    fastBitmap.setGray(i, j, fastBitmap.clampValues((int)val, 0, 255));
                }
            }
        }
        else{
            throw new IllegalArgumentException("Integral variance only works in grayscale images.");
        }
    }
}