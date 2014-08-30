// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2014
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
import Catalano.Imaging.IBaseInPlace;

/**
 * Poisson Noise.
 * @author Diego Catalano
 */
public class PoissonNoise implements IBaseInPlace{

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        if(fastBitmap.isGrayscale()){
            
            int width = fastBitmap.getWidth();
            int height = fastBitmap.getHeight();
            
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    int g = fastBitmap.getGray(i, j);
                    g = Poisson(g);
                    fastBitmap.setGray(i, j, g);
                }
            }
            
        }
        else if(fastBitmap.isRGB()){
            int width = fastBitmap.getWidth();
            int height = fastBitmap.getHeight();
            
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    int r = fastBitmap.getRed(i, j);
                    int g = fastBitmap.getGreen(i, j);
                    int b = fastBitmap.getBlue(i, j);
                    
                    r = Poisson(r);
                    g = Poisson(g);
                    b = Poisson(b);
                    
                    r = r > 255 ? 255 : r;
                    g = g > 255 ? 255 : g;
                    b = b > 255 ? 255 : b;
                    
                    r = r < 0 ? 0 : r;
                    g = g < 0 ? 0 : g;
                    b = b < 0 ? 0 : b;
                    
                    fastBitmap.setRGB(i, j, r, g, b);
                }
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