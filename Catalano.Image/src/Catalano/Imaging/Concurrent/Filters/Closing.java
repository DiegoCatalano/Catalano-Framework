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

package Catalano.Imaging.Concurrent.Filters;

import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.IApplyInPlace;

/**
 * Closing operator from Mathematical Morphology.
 * <br /> Applied to binary image, the filter may be used connect or fill objects. Since dilatation is used first, it may connect/fill object areas. Then erosion restores objects. But since dilatation may connect something before, erosion may not remove after that because of the formed connection.
 * @author Diego Catalano
 */
public class Closing implements IApplyInPlace{
    private int[][] kernel;
    private int radius = 0;

    /**
     * Initializes a new instance of the Closing class.
     */
    public Closing() {
        this.radius = 1;
    }
    
    /**
     * Initializes a new instance of the Closing class.
     * @param se Structuring element.
     */
    public Closing(int[][] se) {
        this.kernel = se;
    }
    
    /**
     * Initializes a new instance of the Closing class.
     * @param radius Radius.
     */
    public Closing(int radius) {
        radius = radius < 1 ? 1 : radius;
        this.radius = radius;
    }
    
    @Override
    public void applyInPlace(FastBitmap fastBitmap){
        if (radius != 0) {
            ApplyInPlace(fastBitmap, radius);
        }
        else{
            ApplyInPlace(fastBitmap, kernel);
        }
    }
    
    private void ApplyInPlace(FastBitmap fastBitmap, int[][] se){
        Dilatation dil = new Dilatation(se);
        Erosion ero = new Erosion(se);
        dil.applyInPlace(fastBitmap);
        ero.applyInPlace(fastBitmap);
    }
    
    private void ApplyInPlace(FastBitmap fastBitmap, int radius){
        Dilatation dil = new Dilatation(radius);
        Erosion ero = new Erosion(radius);
        dil.applyInPlace(fastBitmap);
        ero.applyInPlace(fastBitmap);
    }
}
