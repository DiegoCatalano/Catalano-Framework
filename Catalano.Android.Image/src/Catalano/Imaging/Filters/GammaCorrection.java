// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2014
// diego.catalan at yahoo.com.br
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
 * The filter performs gamma correction of specified image in RGB color space.
 * @author Diego Catalano
 */
public class GammaCorrection implements IBaseInPlace{
    
    private double gamma;

    /**
     * Initialize a new instance of the GammaCorrection class.
     */
    public GammaCorrection() {}

    /**
     * Initialize a new instance of the GammaCorrection class.
     * @param gamma Gamma.
     */
    public GammaCorrection(double gamma) {
        this.gamma = gamma;
    }

    /**
     * Gamma value: [0.1, 5.0].
     * @return Gamma.
     */
    public double getGamma() {
        return gamma;
    }

    /**
     * Gamma value: [0.1, 5.0].
     * @param gamma Gamma.
     */
    public void setGamma(double gamma) {
        this.gamma = gamma;
    }
    
    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        if (fastBitmap.isRGB()){
            int width = fastBitmap.getWidth();
            int height = fastBitmap.getHeight();

            gamma=gamma<0.1?0.1:gamma;
            gamma=gamma>5.0?5.0:gamma;

            double gamma_new = 1 / gamma;
            int[] gamma_LUT = gamma_LUT(gamma_new);

            int r, g, b;

            for(int x = 0; x < height; x++) {
                for(int y = 0; y < width; y++) {

                    // Get pixels by R, G, B
                    r = fastBitmap.getRed(x, y);
                    g = fastBitmap.getGreen(x, y);
                    b = fastBitmap.getBlue(x, y);

                    r = gamma_LUT[r];
                    g = gamma_LUT[g];
                    b = gamma_LUT[b];

                    // Write pixels into image
                    fastBitmap.setRGB(x, y, r, g, b);
                }
            }
            
        }
        else{
            throw new IllegalArgumentException("Gamma correction only works with RGB images.");
        }
}
 
    /**
     * Create the gamma correction lookup table
     * @param gamma_new New gamma value.
     * @return Lookup table
     */
    private static int[] gamma_LUT(double gamma_new) {
        int[] gamma_LUT = new int[256];

        for(int i=0; i<gamma_LUT.length; i++) {
            gamma_LUT[i] = (int) (255 * (Math.pow((double) i / (double) 255, gamma_new)));
        }

        return gamma_LUT;
    }
}