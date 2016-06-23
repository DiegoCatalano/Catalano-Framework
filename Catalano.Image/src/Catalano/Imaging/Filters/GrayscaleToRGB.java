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
 * Convert grayscale image in RGB space.
 * @author Diego Catalano
 */
public class GrayscaleToRGB implements IApplyInPlace{

    /**
     * Initializes a new instance of the GrayscaleToRGB class.
     */
    public GrayscaleToRGB() {}

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        if(!fastBitmap.isGrayscale())
            throw new IllegalArgumentException("GrayscaleToRGB only works in grayscale images.");
        
        fastBitmap.toRGB();   
    }   
}