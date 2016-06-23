// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
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
import Catalano.Imaging.IApplyInPlace;

/**
 * Color filtering.
 * <p> The filter filters pixels inside/outside of specified RGB color range - it keeps pixels with colors inside/outside of specified range and fills the rest with specified color</p>.
 * 
 * <p><li>Supported types: RGB.
 * <br><li>Coordinate System: Independent.
 * 
 * @author Diego Catalano
 */
public class ColorFiltering implements IApplyInPlace{
    
    private IntRange red, green, blue;

    /**
     * Get range from red channel.
     * @return Red range.
     */
    public IntRange getRed() {
        return red;
    }

    /**
     * Set range of the red channel.
     * @param red Red range.
     */
    public void setRed(IntRange red) {
        this.red = red;
    }

    /**
     * Get range from green channel.
     * @return Green range.
     */
    public IntRange getGreen() {
        return green;
    }

    /**
     * Set range of the green channel.
     * @param green Green range.
     */
    public void setGreen(IntRange green) {
        this.green = green;
    }

    /**
     * Get range of the blue channel.
     * @return Blue range.
     */
    public IntRange getBlue() {
        return blue;
    }

    /**
     * Set range of the blue channel.
     * @param blue Blue range.
     */
    public void setBlue(IntRange blue) {
        this.blue = blue;
    }

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
            int size = fastBitmap.getSize();
            for (int i = 0; i < size; i++) {
                r = fastBitmap.getRed(i);
                g = fastBitmap.getGreen(i);
                b = fastBitmap.getBlue(i);

                if (
                        (r >= red.getMin()) && (r <= red.getMax()) && 
                        (g >= green.getMin()) && (g <= green.getMax()) && 
                        (b >= blue.getMin()) && (b <= blue.getMax())
                ){
                    fastBitmap.setRGB(i, r, g, b);
                }
                else{
                    fastBitmap.setRGB(i, 0, 0, 0);
                }
            }
        }
        else{
            throw new IllegalArgumentException("Color filtering only works in RGB images.");
        }
    }
}