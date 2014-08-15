// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2014
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

import Catalano.Core.IntRange;
import Catalano.Imaging.FastBitmap;

/**
 * Replace color.
 * @author Diego Catalano
 */
public class ReplaceColor {
    private int oldRed, oldGreen, oldBlue;
    private IntRange oldRedRange, oldGreenRange, oldBlueRange;
    private boolean isRange;

    /**
     * Initialize a new instance of the ReplaceColor class.
     * @param oldRed Old red.
     * @param oldGreen Old green.
     * @param oldBlue Old blue.
     */
    public ReplaceColor(int oldRed, int oldGreen, int oldBlue) {
        this.oldRed = oldRed;
        this.oldGreen = oldGreen;
        this.oldBlue = oldBlue;
        this.isRange = false;
    }
    
    /**
     * Initialize a new instance of the ReplaceColor class.
     * @param oldRed Old red range.
     * @param oldGreen Old green range.
     * @param oldBlue Old blue range.
     */
    public ReplaceColor(IntRange oldRed, IntRange oldGreen, IntRange oldBlue){
        this.oldRedRange = oldRed;
        this.oldGreenRange = oldGreen;
        this.oldBlueRange = oldBlue;
        this.isRange = true;
    }
    
    /**
     * Apply filter to an image.
     * @param fastBitmap FastBitmap.
     * @param red New red.
     * @param green New green.
     * @param blue New blue.
     */
    public void ApplyInPlace(FastBitmap fastBitmap, int red, int green, int blue){
        int width = fastBitmap.getWidth();
        int height = fastBitmap.getHeight();
        
        int r,g,b;
        if (isRange == false) {
            for (int x = 0; x < height; x++) {
                for (int y = 0; y < width; y++) {
                    r = fastBitmap.getRed(x, y);
                    g = fastBitmap.getGreen(x, y);
                    b = fastBitmap.getBlue(x, y);

                    if ((r == oldRed) && (g == oldGreen) && (b == oldBlue)) {
                        fastBitmap.setRed(x, y, red);
                        fastBitmap.setGreen(x, y, green);
                        fastBitmap.setBlue(x, y, blue);
                    }
                }
            }
        }
        else{
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    r = fastBitmap.getRed(x, y);
                    g = fastBitmap.getGreen(x, y);
                    b = fastBitmap.getBlue(x, y);

                    if (
                        (r >= oldRedRange.getMin()) && (r <= oldRedRange.getMax()) && 
                        (g >= oldGreenRange.getMin()) && (g <= oldGreenRange.getMax()) && 
                        (b >= oldBlueRange.getMin()) && (b <= oldBlueRange.getMax())
                    ) {
                        fastBitmap.setRed(x, y, red);
                        fastBitmap.setGreen(x, y, green);
                        fastBitmap.setBlue(x, y, blue);
                    }
                }
            }
        }
    }
    
}