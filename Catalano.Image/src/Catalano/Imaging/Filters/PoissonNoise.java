// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
// Copyright © Ignazio Gallo
// ignazio.gallo at gmail.com
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
 * Poisson Noise.
 * @author Diego Catalano
 */
public class PoissonNoise implements IApplyInPlace{

    public PoissonNoise() {}

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        if(fastBitmap.isGrayscale()){
            
            int size = fastBitmap.getSize();
            
            for (int i = 0; i < size; i++) {
                int g = fastBitmap.getGray(i);
                g = Poisson(g);
                fastBitmap.setGray(i, g);
            }
            
        }
        else if(fastBitmap.isRGB()){
            int size = fastBitmap.getSize();
            
            for (int i = 0; i < size; i++) {
                int r = fastBitmap.getRed(i);
                int g = fastBitmap.getGreen(i);
                int b = fastBitmap.getBlue(i);

                r = Poisson(r);
                g = Poisson(g);
                b = Poisson(b);

                r = r > 255 ? 255 : r;
                g = g > 255 ? 255 : g;
                b = b > 255 ? 255 : b;

                r = r < 0 ? 0 : r;
                g = g < 0 ? 0 : g;
                b = b < 0 ? 0 : b;

                fastBitmap.setRGB(i, r, g, b);
            }
        }
        else{
            throw new IllegalArgumentException("Poisson Noise only works in Grayscale and RGB images.");
        }
        
    }
    
   private int Poisson(float value) {

      double L = Math.exp(-(value));
      int k = 0;
      double p = 1;
      do {
         k++;
         // Generate uniform random number u in [0,1] and let p ← p × u.
         p *= Math.random();
      } while (p >= L);
      return (k - 1);
   }
}