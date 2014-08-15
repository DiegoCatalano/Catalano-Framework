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
 * @author Diego Catalano
 */
public class ColorFiltering implements IBaseInPlace{
    private IntRange red, green, blue;

    /**
     * Initialize a new instance of the BottomHat class.
     */
    public ColorFiltering() {}

    /**
     * Initialize a new instance of the BottomHat class.
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
        int width = fastBitmap.getWidth();
        int height = fastBitmap.getHeight();
        int r,g,b;
        
        
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                r = fastBitmap.getRed(x, y);
                g = fastBitmap.getGreen(x, y);
                b = fastBitmap.getBlue(x, y);
                
                if (
                        (r >= red.getMin()) && (r <= red.getMax()) && 
                        (g >= green.getMin()) && (g <= green.getMax()) && 
                        (b >= blue.getMin()) && (b <= blue.getMax())
                ){
                    fastBitmap.setRGB(x, y, r, g, b);
                }
                else{
                    fastBitmap.setRGB(x, y, 0, 0, 0);
                }
                
            }
        }
    }
    
}
