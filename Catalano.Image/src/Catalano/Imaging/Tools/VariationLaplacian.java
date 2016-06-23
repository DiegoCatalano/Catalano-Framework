// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
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

package Catalano.Imaging.Tools;

import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.Filters.Convolution;

/**
 * Variation Laplacian.
 * Used for to detection amount of the blur in the image.
 * References: Variation of the Laplacian, Pech-Pacheco et al. in their 2000 ICPR paper, Diatom autofocusing in brightfield microscopy: a comparative study.
 * @author Diego Catalano
 */
public class VariationLaplacian {

    /**
     * Initialize a new instance of the VariationLaplacian class.
     */
    public VariationLaplacian() {}
    
    /**
     * Measure the variance of laplacian filter.
     * @param fastBitmap Image to be processed.
     * @return Variation.
     */
    public double Compute(FastBitmap fastBitmap){
        
        if(fastBitmap.isGrayscale()){
        
            int[][] kernel = new int[][]{
                {0, 1, 0},
                {1, -4, 1},
                {0, 1, 0}
            };

            Convolution conv = new Convolution(kernel);
            conv.applyInPlace(fastBitmap);

            return ImageStatistics.Variance(fastBitmap);
            
        }
        else{
            throw new IllegalArgumentException("Variation laplacian only works in grayscale images.");
        }
        
    }
}