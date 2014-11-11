// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2014
// diego.catalano at live.com
//
// Copyright © Andrew Kirillov, 2007-2008
// andrew.kirillov at gmail.com
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

import Catalano.Core.IntRange;
import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.IBaseInPlace;

/**
 * Color filtering.
 * <p> The filter filters pixels inside/outside of specified RGB color range - it keeps pixels with colors inside/outside of specified range and fills the rest with specified color</p>.
 * 
 * <p><li>Supported types: RGB.
 * <br><li>Coordinate System: Independent.
 * 
 * @author Diego Catalano
 */
public class ColorFiltering implements IBaseInPlace{
    private IntRange red, green, blue;

    /**
     * Initialize a new instance of the ColorFiltering class.
     */
    public ColorFiltering() {}

    /**
     * Initialize a new instance of the ColorFiltering class.
     * @param red Range of red color component.
     * @param green Range of green color component.
     * @param blue Range of blue color component.
     */
    public ColorFiltering(IntRange red, IntRange green, IntRange blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }
    
    @Override
    public void applyInPlace(FastBitmap fastBitmap){
        
        if(fastBitmap.isRGB()){
            int r,g,b;
            int[] pixels = fastBitmap.getRGBData();
            for (int i = 0; i < pixels.length; i++) {
                r = pixels[i] >> 16 & 0xFF;
                g = pixels[i] >> 8 & 0xFF;
                b = pixels[i] & 0xFF;

                if (
                        (r >= red.getMin()) && (r <= red.getMax()) && 
                        (g >= green.getMin()) && (g <= green.getMax()) && 
                        (b >= blue.getMin()) && (b <= blue.getMax())
                ){
                    pixels[i] = r << 16 | g << 8 | b;
                }
                else{
                    pixels[i] = 0;
                }
            }
        }
        else{
            throw new IllegalArgumentException("Color filtering only works in RGB images.");
        }
    }
}