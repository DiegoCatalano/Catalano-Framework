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

import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.IBaseInPlace;

/**
 * Invert image.
 * @author Diego Catalano
 */
public class Invert implements IBaseInPlace{
    
    
    /**
     * Initialize a new instance of the Invert class.
     */
    public Invert(){}
    
    @Override
    public void applyInPlace(FastBitmap fastBitmap){
        int width = fastBitmap.getWidth();
        int height = fastBitmap.getHeight();
        
        if (fastBitmap.isGrayscale()){
            int l;
            for (int x = 0; x < height; x++) {
                for (int y = 0; y < width; y++) {
                    l = 255 - fastBitmap.getGray(x, y);
                    fastBitmap.setGray(x, y, l);
                }
            }
        }
        else if(fastBitmap.isRGB()){
            int r,g,b;
            for (int x = 0; x < height; x++) {
                for (int y = 0; y < width; y++) {
                    r = 255 - fastBitmap.getRed(x, y);
                    g = 255 - fastBitmap.getGreen(x, y);
                    b = 255 - fastBitmap.getBlue(x, y);
                    fastBitmap.setRGB(x, y, r, g, b);
                }
            }
        }
    }
}