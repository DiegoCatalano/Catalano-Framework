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
 * Log filter.
 * 
 * Supported types: Grayscale, RGB.
 * Coordinate System: Independent.
 * 
 * @author Diego Catalano
 */
public class Log implements IApplyInPlace{

    /**
     * Initialize a new instance of the Log class.
     */
    public Log() {}

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        // Scale log
        double scale = 256 / Math.log(256);
        if (fastBitmap.isGrayscale()){
            byte[] pixels = fastBitmap.getGrayData();
            for (int i = 0; i < pixels.length; i++) {
                double v = pixels[i] & 0xFF;
                
                //Compute log
                v = Math.log(1+v) * scale;

                // Clip value
                if (v < 0) v = 0;
                if (v > 255) v = 255;
                
                pixels[i] = (byte)v;
            }
        }
        if (fastBitmap.isRGB()){
            int[] pixelsRGB = fastBitmap.getRGBData();
            for (int i = 0; i < pixelsRGB.length; i++) {
                double r = pixelsRGB[i] >> 16 & 0xff;
                double g = pixelsRGB[i] >> 8 & 0xff;
                double b = pixelsRGB[i] & 0xff;

                //Compute log
                r = Math.log(r+1) * scale;
                g = Math.log(g+1) * scale;
                b = Math.log(b+1) * scale;

                //Clip value
                if (r < 0) r = 0;
                if (r > 255) r = 255;

                if (g < 0) g = 0;
                if (g > 255) g = 255;

                if (b < 0) b = 0;
                if (b > 255) b = 255;


                pixelsRGB[i] = (int)r << 16 | (int)g << 8 | (int)b;
            }
        }
    }
}