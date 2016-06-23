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
 * Invert image.
 * @author Diego Catalano
 */
public class Invert implements IApplyInPlace{
    
    
    /**
     * Initialize a new instance of the Invert class.
     */
    public Invert(){}
    
    @Override
    public void applyInPlace(FastBitmap fastBitmap){
        
        int size = fastBitmap.getSize();
        
        if (fastBitmap.isGrayscale()){
            int l;
            for (int i = 0; i < size; i++) {
                l = 255 - fastBitmap.getGray(i);
                fastBitmap.setGray(i, l);
            }
        }
        else if(fastBitmap.isRGB()){
            int r,g,b;
            for (int i = 0; i < size; i++) {
                r = 255 - fastBitmap.getRed(i);
                g = 255 - fastBitmap.getGreen(i);
                b = 255 - fastBitmap.getBlue(i);
                fastBitmap.setRGB(i, r, g, b);
            }
        }
    }
}