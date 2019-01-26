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

package Catalano.Imaging.Tools;

import Catalano.Imaging.FastBitmap;
import Catalano.Math.Matrix;

/**
 * Orthogonal Variant Moments.
 * 
 * Refences: Santos, Matilde, and Javier de Lope. "Orthogonal variant moments features in image analysis." Information Sciences 180.6 (2010): 846-860.
 * @author Diego Catalano
 */
public class OrthogonalVariantMoments {

    /**
     * Initialize a new instance of the OrthogonalVariantMoments class.
     */
    public OrthogonalVariantMoments() {}
    
    /**
     * Compute OVM.
     * @param fastBitmap Image to be processed.
     * @return 5 OV moments.
     */
    public double[] Compute(FastBitmap fastBitmap){
        
        if(!fastBitmap.isGrayscale())
            throw new IllegalArgumentException("Orthogonal Variant Moments only works in grayscale images.");
        
        // 5 moments: A, Lx, Ly, Px, Py
        double[] moments = new double[5];
        
        // Moment: Area
        moments[0] = ImageMoments.getRawMoment(fastBitmap, 0, 0);
        
        int width = fastBitmap.getWidth();
        int height = fastBitmap.getHeight();
        double box = width * height;
        
        // Moment: Ly
        double[][] diff = new double[height - 1][width];
        for (int i = 0; i < height - 1; i++) {
            for (int j = 0; j < width; j++) {
                diff[i][j] = fastBitmap.getGray(i + 1, j) - fastBitmap.getGray(i, j);
            }
        }
        
        double m = Matrix.SumAbs(diff);
        
        double ly = 0;
        for (int i = 0; i < diff.length; i++) {
            for (int j = 0; j < diff[0].length; j++) {
                ly += Math.sqrt((diff[i][j] * diff[i][j]) + 1);
            }
        }
        
        moments[2] = (ly + 3) / box;
        
        // Moment: Py
        double den = (m / 8.0) + 0.1;
        double dsum = 1;
        for (int i = 0; i < diff.length; i++) {
            for (int j = 0; j < diff[0].length; j++) {
                dsum += Math.abs(diff[i][j]) * (i+1);
            }
        }
        
        moments[4] = (dsum + 1) / den;
       
        // Moment: Lx
        diff = new double[height][width - 1];
         for (int i = 0; i < diff.length; i++) {
             for (int j = 0; j < diff[0].length; j++) {
                 diff[i][j] = fastBitmap.getGray(i, j + 1) - fastBitmap.getGray(i, j);
             }
         }
       
         m = Matrix.SumAbs(diff);
        
        double lx = 0;
        for (int i = 0; i < diff.length; i++) {
            for (int j = 0; j < diff[0].length; j++) {
                lx += Math.sqrt((diff[i][j] * diff[i][j]) + 1);
            }
        }
        
        moments[1] = (lx + 3) / box;
        
        // Moment: Px
        den = (m / 4.5) + 0.1;
        dsum = 1;
        for (int i = 0; i < diff.length; i++) {
            for (int j = 0; j < diff[0].length; j++) {
                dsum += Math.abs(diff[i][j]) * (j+1);
            }
        }
        
       moments[3] = (dsum + 1) / den;
        
       return moments;
        
    }
}