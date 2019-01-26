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
 * Binary Opening operator from Mathematical Morphology.
 * <p> Applied to binary image, the filter may be used for removing small object keeping big objects unchanged. Since erosion is used first, it removes all small objects. Then dilatation restores big objects, which were not removed by erosion.</p>
 * 
 * <p><li>Supported types: Grayscale.
 * <br><li>Coordinate System: Matrix.
 * 
 * @author Diego Catalano
 */
public class BinaryOpening implements IApplyInPlace{
    
    private int[][] kernel;
    private int radius = 0;

    /**
     * Initializes a new instance of the Binary Opening class.
     */
    public BinaryOpening() {
        this.radius = 1;
    }
    
    /**
     * Initializes a new instance of the Binary Opening class.
     * @param se Structuring element.
     */
    public BinaryOpening(int[][] se) {
        this.kernel = se;
    }
    
    /**
     * Initializes a new instance of the Binary Opening class.
     * @param radius Radius.
     */
    public BinaryOpening(int radius) {
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
            throw new IllegalArgumentException("Binary Opening only works in grayscale images.");
        }
    }
    
    private void ApplyInPlace(FastBitmap fastBitmap, int[][] se){
        BinaryErosion ero = new BinaryErosion(se);
        BinaryDilatation dil = new BinaryDilatation(se);
        ero.applyInPlace(fastBitmap);
        dil.applyInPlace(fastBitmap);
    }
    
    private void ApplyInPlace(FastBitmap fastBitmap, int radius){
        BinaryErosion ero = new BinaryErosion(radius);
        BinaryDilatation dil = new BinaryDilatation(radius);
        ero.applyInPlace(fastBitmap);
        dil.applyInPlace(fastBitmap);
    }
}