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

/**
 * Maitra moments.
 * 
 * References: Maitra, Sidhartha. "Moment invariants." Proceedings of the IEEE 67.4 (1979): 697-699.
 * 
 * @author Diego Catalano
 */
public class MaitraMoments {

    /**
     * Initialize a new instance of the MaitraMoments class.
     */
    public MaitraMoments() {}
    
    /**
     * Compute Maitra moments.
     * @param fastBitmap Image.
     * @return 6 Moments.
     */
    public double[] Compute(FastBitmap fastBitmap){
        double[] result = new double[6];
        
        HuMoments hu = new HuMoments();
        double[] m = hu.Compute(fastBitmap);
        
        double u00 = ImageMoments.getNormalizedCentralMoment(fastBitmap, 0, 0);
        
        result[0] = Math.sqrt(m[1]) / m[0];
        result[1] = (m[2] * u00) / (m[1] * m[0]);
        result[2] = m[3] / m[2];
        result[3] = Math.sqrt(m[4]) / m[3];
        result[4] = m[5] / (m[3] * m[0]);
        result[5] = m[6] / m[4];
        
        return result;
    }
    
}