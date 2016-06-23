// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
// Copyright © Arlington, 2013
// Copyright © Saulo, 2013
// scsm at ecmp.poli.br
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
 * Hu Moments.
 * Compute 8 moments from hu, the last was proposed by J. Flusser and T. Suk.
 * 
 * Compute RST invariants.
 */
public class HuMoments {
    
    /**
     * Initialize a new instance of the HuMoments class.
     */
    public HuMoments(){};
    
    /**
     * Compute Hu moments.
     * @param fastBitmap Image.
     * @return 8 Moments.
     */
    public double[] Compute(FastBitmap fastBitmap){
        
        double[] moments = new double[8];

        double
        n20 = ImageMoments.getNormalizedCentralMoment(fastBitmap, 2, 0),
        n02 = ImageMoments.getNormalizedCentralMoment(fastBitmap, 0, 2),
        n30 = ImageMoments.getNormalizedCentralMoment(fastBitmap, 3, 0),
        n12 = ImageMoments.getNormalizedCentralMoment(fastBitmap, 1, 2),
        n21 = ImageMoments.getNormalizedCentralMoment(fastBitmap, 2, 1),
        n03 = ImageMoments.getNormalizedCentralMoment(fastBitmap, 0, 3),
        n11 = ImageMoments.getNormalizedCentralMoment(fastBitmap, 1, 1);
        
        //First moment
        moments[0] = n20 + n02;
        
        //Second moment
        moments[1] = Math.pow((n20 - 02), 2) + Math.pow(2 * n11, 2);
        
        //Third moment
        moments[2] = Math.pow(n30 - (3 * (n12)), 2)
                   + Math.pow((3 * n21 - n03), 2);
        
        //Fourth moment
        moments[3] = Math.pow((n30 + n12), 2) + Math.pow((n12 + n03), 2);
        
        //Fifth moment
        moments[4] = (n30 - 3 * n12) * (n30 + n12)
                     * (Math.pow((n30 + n12), 2) - 3 * Math.pow((n21 + n03), 2))
                     + (3 * n21 - n03) * (n21 + n03)
                     * (3 * Math.pow((n30 + n12), 2) - Math.pow((n21 + n03), 2));
        
        //Sixth moment
        moments[5] = (n20 - n02)
                     * (Math.pow((n30 + n12), 2) - Math.pow((n21 + n03), 2))
                     + 4 * n11 * (n30 + n12) * (n21 + n03);
        
        //Seventh moment
        moments[6] = (3 * n21 - n03) * (n30 + n12)
                     * (Math.pow((n30 + n12), 2) - 3 * Math.pow((n21 + n03), 2))
                     + (n30 - 3 * n12) * (n21 + n03)
                     * (3 * Math.pow((n30 + n12), 2) - Math.pow((n21 + n03), 2));
        
        //Eighth moment
        moments[7] = n11 * (Math.pow((n30 + n12), 2) - Math.pow((n03 + n21), 2))
                         - (n20 - n02) * (n30 + n12) * (n03 + n21);
        
        
        return moments;
    }

    /**
     * Compute Hu moment.
     * @param fastBitmap Image.
     * @param n Invariant Moment. [1..8].
     * @return Hu moment.
     */
    public static double getHuMoment (FastBitmap fastBitmap, int n) {
        
        //Ensures the values are in the range [1..8].
        n = Math.min(8, Math.max(1, n));
        
        double result = 0.0;

        double
        n20 = ImageMoments.getNormalizedCentralMoment(fastBitmap, 2, 0),
        n02 = ImageMoments.getNormalizedCentralMoment(fastBitmap, 0, 2),
        n30 = ImageMoments.getNormalizedCentralMoment(fastBitmap, 3, 0),
        n12 = ImageMoments.getNormalizedCentralMoment(fastBitmap, 1, 2),
        n21 = ImageMoments.getNormalizedCentralMoment(fastBitmap, 2, 1),
        n03 = ImageMoments.getNormalizedCentralMoment(fastBitmap, 0, 3),
        n11 = ImageMoments.getNormalizedCentralMoment(fastBitmap, 1, 1);

        switch (n) {
        case 1:
            result = n20 + n02;
            break;
        case 2:
            result = Math.pow((n20 - 02), 2) + Math.pow(2 * n11, 2);
            break;
        case 3:
            result = Math.pow(n30 - (3 * (n12)), 2)
                    + Math.pow((3 * n21 - n03), 2);
            break;
        case 4:
            result = Math.pow((n30 + n12), 2) + Math.pow((n12 + n03), 2);
            break;
        case 5:
            result = (n30 - 3 * n12) * (n30 + n12)
                    * (Math.pow((n30 + n12), 2) - 3 * Math.pow((n21 + n03), 2))
                    + (3 * n21 - n03) * (n21 + n03)
                    * (3 * Math.pow((n30 + n12), 2) - Math.pow((n21 + n03), 2));
            break;
        case 6:
            result = (n20 - n02)
                    * (Math.pow((n30 + n12), 2) - Math.pow((n21 + n03), 2))
                    + 4 * n11 * (n30 + n12) * (n21 + n03);
            break;
        case 7:
            result = (3 * n21 - n03) * (n30 + n12)
                    * (Math.pow((n30 + n12), 2) - 3 * Math.pow((n21 + n03), 2))
                    + (n30 - 3 * n12) * (n21 + n03)
                    * (3 * Math.pow((n30 + n12), 2) - Math.pow((n21 + n03), 2));
            break;
        case 8:
            result = n11 * (Math.pow((n30 + n12), 2) - Math.pow((n03 + n21), 2))
                     - (n20 - n02) * (n30 + n12) * (n03 + n21);
            break;

        default:
            throw new IllegalArgumentException("Invalid number for Hu moment.");
        }
        
        return result;
    }
}