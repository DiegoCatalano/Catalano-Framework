// Catalano Android Imaging Library
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

import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.IApplyInPlace;

/**
 * Sepia filter - old brown photo.
 * @author Diego Catalano
 */
public class Sepia implements IApplyInPlace{
    
    /**
     * Initializes a new instance of the Sepia class.
     */
    public Sepia(){}
    
    @Override
    public void applyInPlace(FastBitmap fastBitmap){
        
        int size = fastBitmap.getSize();
        int r,g,b;
        double Y,I,Q;
        
        for (int x = 0; x < size; x++) {
            r = fastBitmap.getRed(x);
            g = fastBitmap.getGreen(x);
            b = fastBitmap.getBlue(x);

            //YIQ Color Space
            Y = (0.299 * r) + (0.587 * g) + (0.114 * b);
            //I = (0.596 * r) - (0.274 * g) - (0.322 * b);
            //Q = (0.212 * r) - (0.523 * g) + (0.311 * b);

            //Update it
            I = 51;
            Q = 0;

            //Transform to RGB
            r = (int)((1.0 * Y) + (0.956 * I) + (0.621 * Q));
            g = (int)((1.0 * Y) - (0.272 * I) - (0.647 * Q));
            b = (int)((1.0 * Y) - (1.105 * I) + (1.702 * Q));

            //Fix values
            r = r < 0 ? 0 : r;
            r = r > 255 ? 255 : r;

            g = g < 0 ? 0 : g;
            g = g > 255 ? 255 : g;

            b = b < 0 ? 0 : b;
            b = b > 255 ? 255 : b;

            //Set pixels now
            fastBitmap.setRGB(x, r, g, b);
        }
    }
}