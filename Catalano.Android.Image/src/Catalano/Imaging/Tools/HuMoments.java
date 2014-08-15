// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2014
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

/**
 * Compute Hu Moments.
 * Compute RST invariants.
 */
public final class HuMoments {
    
    /**
     * Don't let anyone instantiate this class.
     */
    private HuMoments(){};

    /**
     * Compute Hu moment.
     * @param image Image as Array.
     * @param n Invariant Moment. [1..7].
     * @return Hu moment.
     */
    public static double getHuMoment (double[][] image,int n) {
        //Ensures the values are in the range [1..7].
        n = Math.min(7, Math.max(1, n));
        
        double result = 0.0;

        double
        n20 = ImageMoments.getNormalizedCentralMoment(2, 0, image),
        n02 = ImageMoments.getNormalizedCentralMoment(0, 2, image),
        n30 = ImageMoments.getNormalizedCentralMoment(3, 0, image),
        n12 = ImageMoments.getNormalizedCentralMoment(1, 2, image),
        n21 = ImageMoments.getNormalizedCentralMoment(2, 1, image),
        n03 = ImageMoments.getNormalizedCentralMoment(0, 3, image),
        n11 = ImageMoments.getNormalizedCentralMoment(1, 1, image);

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

        default:
                throw new IllegalArgumentException("Invalid number for Hu moment.");
        }
        return result;
    }
}