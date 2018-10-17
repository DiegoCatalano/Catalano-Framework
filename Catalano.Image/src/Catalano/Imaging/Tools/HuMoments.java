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

import Catalano.Core.DoublePoint;
import Catalano.Imaging.FastBitmap;

/**
 * Hu Moments.
 * Compute 7 moments from hu, + 1 the last was proposed by J. Flusser and T. Suk.
 * 
 * Compute RST invariants.
 */
public class HuMoments {
    
    private boolean normalize;

    /**
     * Check if the moments are normalized.
     * @return Check if the moments are normalized.
     */
    public boolean isNormalize() {
        return normalize;
    }

    /**
     * Normalize the moments.
     * @param normalize True if needs to normalize the moments, otherwise return false.
     */
    public void setNormalize(boolean normalize) {
        this.normalize = normalize;
    }
    
    /**
     * Initialize a new instance of the HuMoments class.
     */
    public HuMoments(){
        this(false);
    };

    /**
     * Initialize a new instance of the HuMoments class.
     * 
     * Normalize by sign(x) * log10(x).
     * 
     * @param normalize Normalize.
     */
    public HuMoments(boolean normalize) {
        this.normalize = normalize;
    }
    
    /**
     * Compute Hu moments.
     * @param fastBitmap Image.
     * @return 8 Moments.
     */
    public double[] Compute(FastBitmap fastBitmap){
        
        double[] moments = new double[8];
        
        double m00 = ImageMoments.getRawMoment(fastBitmap, 0, 0);
        DoublePoint centroid = ImageMoments.getCentroid(fastBitmap, m00);

        double n20 = ImageMoments.getNormalizedCentralMoment(fastBitmap, 2, 0, centroid, m00);
        double n02 = ImageMoments.getNormalizedCentralMoment(fastBitmap, 0, 2, centroid, m00);
        double n30 = ImageMoments.getNormalizedCentralMoment(fastBitmap, 3, 0, centroid, m00);
        double n12 = ImageMoments.getNormalizedCentralMoment(fastBitmap, 1, 2, centroid, m00);
        double n21 = ImageMoments.getNormalizedCentralMoment(fastBitmap, 2, 1, centroid, m00);
        double n03 = ImageMoments.getNormalizedCentralMoment(fastBitmap, 0, 3, centroid, m00);
        double n11 = ImageMoments.getNormalizedCentralMoment(fastBitmap, 1, 1, centroid, m00);
        
        //First moment
        moments[0] = n20 + n02;
        
        //Second moment
        moments[1] = Math.pow(n20 - n02, 2) + (4 * Math.pow(n11, 2));
        
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
        
        //Normalize
        if(normalize){
            for (int i = 0; i < moments.length; i++) {
                moments[i] = Math.signum(moments[i]) * Math.log10(1 + Math.abs(moments[i]));
            }
        }
        
        return moments;
    }
}