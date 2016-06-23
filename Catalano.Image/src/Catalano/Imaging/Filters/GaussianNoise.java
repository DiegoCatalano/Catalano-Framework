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
import java.util.Random;

/**
 * Gaussian Noise.
 * @author Diego Catalano
 */
public class GaussianNoise implements IApplyInPlace{
    
    private double stdDev = 10;

    /**
     * Get standart deviation.
     * @return Standart deviation.
     */
    public double getStdDev() {
        return stdDev;
    }

    /**
     * Set standart deviation.
     * @param stdDev Standart deviation.
     */
    public void setStdDev(double stdDev) {
        this.stdDev = stdDev;
    }

    /**
     * Initialize a new instance of the GaussianNoise class.
     */
    public GaussianNoise() {}

    /**
     * Initialize a new instance of the GaussianNoise class.
     * @param stdDev Standart deviation.
     */
    public GaussianNoise(double stdDev) {
        this.stdDev = stdDev;
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        if(fastBitmap.isGrayscale()){
            
            int size = fastBitmap.getSize();
            
            Random r = new Random();
            for (int i = 0; i < size; i++) {
                int g = fastBitmap.getGray(i);
                g += stdDev * r.nextGaussian();

                g = g > 255 ? 255 : g;
                g = g < 0 ? 0 : g;

                fastBitmap.setGray(i, g);
            }
            
        }
        else if(fastBitmap.isRGB()){
            int size = fastBitmap.getSize();
            
            Random rand = new Random();
            for (int i = 0; i < size; i++) {
                int r = fastBitmap.getRed(i);
                int g = fastBitmap.getGreen(i);
                int b = fastBitmap.getBlue(i);

                r += stdDev * rand.nextGaussian();
                g += stdDev * rand.nextGaussian();
                b += stdDev * rand.nextGaussian();

                r = r > 255 ? 255 : r;
                r = r < 0 ? 0 : r;

                g = g > 255 ? 255 : g;
                g = g < 0 ? 0 : g;

                b = b > 255 ? 255 : b;
                b = b < 0 ? 0 : b;

                fastBitmap.setRGB(i, r, g, b);
            }
        }
        else{
            throw new IllegalArgumentException("Gaussian noise only works in grayscale and rgb images.");
        }
    }
}