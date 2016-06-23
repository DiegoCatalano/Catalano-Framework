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
 * Binary Closing operator from Mathematical Morphology.
 * <p> Applied to binary image, the filter may be used connect or fill objects. Since dilatation is used first, it may connect/fill object areas. Then erosion restores objects. But since dilatation may connect something before, erosion may not remove after that because of the formed connection.</p>
 * 
 * <p><b>Properties:</b>
 * <li>Supported types: Grayscale.
 * <br><li>Coordinate System: Matrix.</p>
 * 
 * @author Diego Catalano
 */
public class BinaryClosing implements IApplyInPlace{
    
    private int[][] kernel;
    private int radius = 0;

    /**
     * Initializes a new instance of the BinaryClosing class.
     */
    public BinaryClosing() {
        this.radius = 1;
    }
    
    /**
     * Initializes a new instance of the BinaryClosing class.
     * @param se Structuring element.
     */
    public BinaryClosing(int[][] se) {
        this.kernel = se;
    }
    
    /**
     * Initializes a new instance of the BinaryClosing class.
     * @param radius Radius.
     */
    public BinaryClosing(int radius) {
        radius = radius < 1 ? 1 : radius;
        this.radius = radius;
    }
    
    @Override
    public void applyInPlace(FastBitmap fastBitmap){
        if(fastBitmap.isGrayscale()){
            if (radius != 0) {
                ApplyInPlace(fastBitmap, radius);
            }
            else{
                ApplyInPlace(fastBitmap, kernel);
            }
        }
        else{
            throw new IllegalArgumentException("Binary Closing only works in grayscale images.");
        }
    }
    
    private void ApplyInPlace(FastBitmap fastBitmap, int[][] se){
        BinaryDilatation dil = new BinaryDilatation(se);
        BinaryErosion ero = new BinaryErosion(se);
        dil.applyInPlace(fastBitmap);
        ero.applyInPlace(fastBitmap);
    }
    
    private void ApplyInPlace(FastBitmap fastBitmap, int radius){
        BinaryDilatation dil = new BinaryDilatation(radius);
        BinaryErosion ero = new BinaryErosion(radius);
        dil.applyInPlace(fastBitmap);
        ero.applyInPlace(fastBitmap);
    }
}
