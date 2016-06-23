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

import Catalano.Core.IntPoint;
import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.IApplyInPlace;
import java.util.ArrayList;

/**
 * Extract Boundary using approach with morphology operators.
 * @author Diego Catalano
 */
public class ExtractBoundary implements IApplyInPlace{
    
    /**
     * Morphology operators.
     */
    public enum Algorithm {

        /**
         * Erosion.
         */
        Erosion,
        /**
         * Dilatation.
         */
        Dilatation
    };
    private Algorithm algorithm = Algorithm.Erosion;
    private ArrayList<IntPoint> points;

    /**
     * Initialize a new instance of the ExtractBoundary class.
     */
    public ExtractBoundary() {}

    /**
     * Initialize a new instance of the ExtractBoundary class.
     * @param algorithm Morphology algotithm.
     */
    public ExtractBoundary(Algorithm algorithm) {
        this.algorithm = algorithm;
    }
    
    @Override
    public void applyInPlace(FastBitmap fastBitmap){
        FastBitmap l = new FastBitmap(fastBitmap);
        
        if (algorithm == Algorithm.Erosion) {
            BinaryErosion ero = new BinaryErosion();
            ero.applyInPlace(l);
        }
        else{
            BinaryDilatation dil = new BinaryDilatation();
            dil.applyInPlace(l);
        }
        
        Difference dif = new Difference(fastBitmap);
        dif.applyInPlace(l);
        fastBitmap.setImage(l);
    }
    
    /**
     * Get points after extract boundary.
     * @param fastBitmap Image to be processed.
     * @return List of points.
     */
    public ArrayList<IntPoint> ProcessImage(FastBitmap fastBitmap){
        FastBitmap l = new FastBitmap(fastBitmap);
        if (points == null) {
            applyInPlace(l);
        }
        
        int width = l.getWidth();
        int height = l.getHeight();
        points = new ArrayList<IntPoint>();
        
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                if (l.getGray(x, y) == 255) points.add(new IntPoint(x,y));
            }
        }
        return points;
    }
}