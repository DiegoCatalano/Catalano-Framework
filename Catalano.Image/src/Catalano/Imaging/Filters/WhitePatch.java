// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
// Copyright © Juan Manuel Perez Rua, 2013
// juanmanpr at gmail.com
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
 * White Patch algorithm.
 * @author Diego Catalano
 */
public class WhitePatch implements IApplyInPlace{

    /**
     * Initialize a new instance of the WhitePatch class.
     */
    public WhitePatch() {}

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        if (fastBitmap.isRGB()){
            
            int size = fastBitmap.getSize();
            
            double maxR = 1, maxG = 1, maxB = 1;
            for (int i = 0; i < size; i++) {
                if (fastBitmap.getRed(i) > maxR)
                    maxR = fastBitmap.getRed(i);

                if (fastBitmap.getGreen(i) > maxG)
                    maxG = fastBitmap.getGreen(i);

                if (fastBitmap.getBlue(i) > maxB)
                    maxB = fastBitmap.getBlue(i);
            }
            
            maxR = 255D / maxR;
            maxG = 255D / maxG;
            maxB = 255D / maxB;
            
            for (int i = 0; i < size; i++) {
                double r = maxR * fastBitmap.getRed(i);
                double g = maxG * fastBitmap.getGreen(i);
                double b = maxB * fastBitmap.getBlue(i);

                if (r > 255) r = 255;
                if (g > 255) g = 255;
                if (b > 255) b = 255;

                fastBitmap.setRGB(i, (int)r, (int)g, (int)b);
            }
        }
        else{
            throw new IllegalArgumentException("White Patch only works in RGB space.");
        }
    }
}