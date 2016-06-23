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
import Catalano.Math.Approximation;

/**
 * Exp filter.
 * 
 * <p><b>Properties:</b>
 * <li>Supported types: Grayscale, RGB.
 * <br><li>Coordinate System: Independent.</p>
 * 
 * @author Diego Catalano
 */
public class Exp implements IApplyInPlace{

    /**
     * Initialize a new instance of the Exp class.
     */
    public Exp() {}

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        // Scale log
        double scale = 255 / Math.log(255);
        if (fastBitmap.isGrayscale()){
            byte[] pixels = fastBitmap.getGrayData();
            for (int i = 0; i < pixels.length; i++) {
                
                double v = pixels[i] < 0 ? pixels[i] + 256 : pixels[i];
                v = Approximation.Highprecision_Exp(v/scale);

                // Clip value
                if (v < 0) v = 0;
                if (v > 255) v = 255;
                
                pixels[i] = (byte)v;
                
            }
        }
        if (fastBitmap.isRGB()){
            int[] pixels = fastBitmap.getRGBData();
            for (int i = 0; i < pixels.length; i++) {
                
                double r = pixels[i] >> 16 & 0xFF;
                double g = pixels[i] >> 8 & 0xFF;
                double b = pixels[i] & 0xFF;
                
                r = Approximation.Highprecision_Exp(r/scale);
                g = Approximation.Highprecision_Exp(g/scale);
                b = Approximation.Highprecision_Exp(b/scale);

                //Clip value
                if (r < 0) r = 0;
                if (r > 255) r = 255;

                if (g < 0) g = 0;
                if (g > 255) g = 255;

                if (b < 0) b = 0;
                if (b > 255) b = 255;
                
                pixels[i] = (int)r << 16 | (int)g << 8 | (int)b;
                
            }
        }
    }
}