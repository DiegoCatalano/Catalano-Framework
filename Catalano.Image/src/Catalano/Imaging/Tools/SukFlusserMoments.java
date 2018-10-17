// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
// Copyright © Asad Ali
// asad_82 at yahoo.com
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

import Catalano.Core.DoublePoint;
import Catalano.Imaging.FastBitmap;

/**
 * Combined Blur and Affine Moment Invariants.
 * Paper: "Combined blur and affine moment invariants and their use in pattern recognition. Tomas Suk, Jan Flusser"
 * Link: http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.85.7431&rep=rep1&type=pdf
 * @author Diego Catalano
 */
public class SukFlusserMoments {
    
    private boolean normalize;

    /**
     * Check if the moments are normalized.
     * @return True if the moments are normalized, otherwise false.
     */
    public boolean isNormalize() {
        return normalize;
    }

    /**
     * Set if the moments needs to be normalized by log.
     * @param normalize Normalize.
     */
    public void setNormalize(boolean normalize) {
        this.normalize = normalize;
    }

    /**
     * Initialize a new instance of the SukFlusserMoments class.
     */
    public SukFlusserMoments() {
        this(false);
    }

    /**
     * Initialize a new instance of the SukFlusserMoments class.
     * @param normalize Normalize by log scale.
     */
    public SukFlusserMoments(boolean normalize) {
        this.normalize = normalize;
    }
    
    /**
     * Compute Blur and Affine moment invariants.
     * @param fastBitmap Image to be processed.
     * @return 6 Moments.
     */
     public double[] Compute(FastBitmap fastBitmap){
        
        if(fastBitmap.isGrayscale()){

            double m00 = ImageMoments.getRawMoment(fastBitmap, 0, 0);
            DoublePoint centroid = ImageMoments.getCentroid(fastBitmap, m00);

            // Second order central moments
            double m20 = ImageMoments.getCentralMoment(fastBitmap, 2, 0, centroid);
            double m02 = ImageMoments.getCentralMoment(fastBitmap, 0, 2, centroid);
            double m11 = ImageMoments.getCentralMoment(fastBitmap, 1, 1, centroid);

            // Third order central moments
            double m30 = ImageMoments.getCentralMoment(fastBitmap, 3, 0, centroid);
            double m03 = ImageMoments.getCentralMoment(fastBitmap, 0, 3, centroid);
            double m21 = ImageMoments.getCentralMoment(fastBitmap, 2, 1, centroid);
            double m12 = ImageMoments.getCentralMoment(fastBitmap, 1, 2, centroid);

            // Fouth order central moments
            double m40 = ImageMoments.getCentralMoment(fastBitmap, 4, 0, centroid);
            double m04 = ImageMoments.getCentralMoment(fastBitmap, 0, 4, centroid);
            double m31 = ImageMoments.getCentralMoment(fastBitmap, 3, 1, centroid);
            double m13 = ImageMoments.getCentralMoment(fastBitmap, 1, 3, centroid);
            double m22 = ImageMoments.getCentralMoment(fastBitmap, 2, 2, centroid);

            // Fifth order central moments
            double m50 = ImageMoments.getCentralMoment(fastBitmap, 5, 0, centroid);
            double m05 = ImageMoments.getCentralMoment(fastBitmap, 0, 5, centroid);
            double m41 = ImageMoments.getCentralMoment(fastBitmap, 4, 1, centroid);
            double m14 = ImageMoments.getCentralMoment(fastBitmap, 1, 4, centroid);
            double m32 = ImageMoments.getCentralMoment(fastBitmap, 3, 2, centroid);
            double m23 = ImageMoments.getCentralMoment(fastBitmap, 2, 3, centroid);

            // Seventh order central moments
            double m70 = ImageMoments.getCentralMoment(fastBitmap, 7, 0, centroid);
            double m07 = ImageMoments.getCentralMoment(fastBitmap, 0, 7, centroid);
            double m16 = ImageMoments.getCentralMoment(fastBitmap, 1, 6, centroid);
            double m61 = ImageMoments.getCentralMoment(fastBitmap, 6, 1, centroid);
            double m52 = ImageMoments.getCentralMoment(fastBitmap, 5, 2, centroid);
            double m25 = ImageMoments.getCentralMoment(fastBitmap, 2, 5, centroid);
            double m43 = ImageMoments.getCentralMoment(fastBitmap, 4, 3, centroid);
            double m34 = ImageMoments.getCentralMoment(fastBitmap, 3, 4, centroid);

            // For blur invariance we recompute certain values
            m50 = m50 - (10*m30*m20/m00);
            m41 = m41 - (2*(3*m21*m20 + 2*m30*m11)/m00);
            m32 = m32 - ((3*m12*m20 + m30*m02 + 6*m21*m11)/m00);
            m23 = m23 - ((3*m21*m02 + m03*m20 + 6*m12*m11)/m00);
            m14 = m14 - (2*(3*m12*m02 + 2*m03*m11)/m00);
            m05 = m05 - (10*m03*m02/m00);

            // For blur invariance seventh order moments recomputed
            m70 = m70 - 7 * (3*m50*m20 + 5*m30*m40)/m00 + (210*m30*(m20*m20) / (m00*m00));

            m61 = m61 - (6*m50*m11 + 15*m41*m20 + 15*m40*m21 + 20*m31*m30)/m00 +
                30*(3*m21*(m20*m20) + 4*m30*m20*m11)/(m00*m00);

            m52 = m52 - (m50*m02 +10*m30*m22 + 10*m32*m20 + 20*m31*m21 +10*m41*m11 + 5*m40*m12)/m00 +
                10* (3*m12*(m20*m20) + 2*m30*m20*m02 + 4*m30*(m11*m11) + 12*m21*m20*m11)/(m00*m00);

            m43 = m43 - (m40*m03 + 18*m21*m22 + 12*m31*m12 + 4*m30*m13 + 3*m41*m02 + 12*m32*m11 +
                6*m23*m20)/m00 + 6*(m03*(m20*m20) + 4*m30*m11*m02 + 12*m21*(m11*m11) + 12*m12*m20*m11 + 6*m21*m02*m20);

            m34 = m34 - (m04*m30 + 18*m12*m22 + 12*m13*m21 + 4*m03*m31 + 3*m14*m20 + 12*m23*m11
                + 6*m32*m02)/m00 + 6 *(m30*(m02*m02) + 4*m03*m11*m20 + 12*m12*(m11*m11) + 12*m21*m02*m11 +
                6*m12*m20*m02)/(m00*m00);

            m25 = m25 - (m05*m20 + 10*m03*m22 + 10*m23*m02 + 20*m13*m12 + 10*m14*m11 + 5*m04*m21)/m00 +
                10*(3*m21*(m02*m02) + 2*m03*m02*m20 +4*m03*(m11*m11) + 12*m12*m02*m11)/(m00*m00);

            m16 = m16 - (6*m05*m11 + 15*m14*m02 + 15*m04*m12 + 20*m13*m03)/m00 + 30*(3*m12*(m02*m02) +
                4*m03*m02*m11)/(m00*m00);

            m07 = m07 - 7*(3*m05*m02 + 5*m03*m04)/m00 + (210*m03*(m02*m02) / (m00*m00));

            //First invariant computed from the determinant of the polynomial
            double I1 = ((m30*m30)*(m03*m03) - 6*m30*m21*m12*m03 + 4*m30*(m12*m12*m12) +
                 4*(m21*m21*m21)*m03 - 3*(m21*m21)*(m12*m12)) / Math.pow(m00, 10);

            double I2 = ((m50*m50)*(m05*m05) - 10*m50*m41*m14*m05 + 4*m50*m32*m23*m05 +
                16*m50*m32*(m14*m14) - 12*m50*(m23*m23)*m14 + 16*(m41*m41)*m23*m05 +
                9*(m41*m41)*(m14*m14) - 12*m41*(m32*m32)*m05 - 76*m41*m32*m23*m14 +
                48*m41*(m23*m23*m23) + 48*(m32*m32*m32)*m14 - 32*(m32*m32)*(m23*m23))/Math.pow(m00, 14);

            double I3 = ((m30*m30)*m12*m05 - (m30*m30)*m03*m14 - m30*(m21*m21)*m05 - 2*m30*m21*m12*m14 +
                4*m30*m21*m03*m23 + 2*m30*(m12*m12)*m23 - 4*m30*m12*m03*m32 +
                m30*(m03*m03)*m41 + 3*(m21*m21*m21)*m14 - 6*(m21*m21)*m12*m23 - 2*(m21*m21)*m03*m32 +
                6*m21*(m12*m12)*m32 + 2*m21*m12*m03*m41 - m21*(m03*m03)*m50 - 3*(m12*m12*m12)*m41 +
                (m12*m12)*m03*m50) / Math.pow(m00, 11);

            double I4 = (2*m30*m12*m41*m05 - 8*m30*m12*m32*m14 + 6*m30*m12*(m23*m23) -
                m30*m03*m50*m05 + 3*m30*m03*m41*m14 - 2*m30*m03*m32*m23 -
                2*(m21*m21)*m41*m05 + 8*(m21*m21)*m32*m14 - 6*(m21*m21)*(m23*m23) +
                m21*m12*m50*m05 - 3*m21*m12*m41*m14 + 2*m21*m12*m32*m23 +
                2*m21*m03*m50*m14 - 8*m21*m03*m41*m23 + 6*m21*m03*(m32*m32) -
                2*(m12*m12)*m50*m14 + 8*(m12*m12)*m41*m23 - 6*(m12*m12)*(m32*m32))/Math.pow(m00, 12);

            double I5 = (m30*m41*m23*m05 - m30*m41*(m14*m14) - m30*(m32*m32)*m05 + 2*m30*m32*m23*m14 -
                m30*(m23*m23*m23) - m21*m50*m23*m05 + m21*m50*(m14*m14) + m21*m41*m32*m05 -
                m21*m41*m23*m14 - m21*(m32*m32)*m14 + m21*m32*(m23*m23) + m12*m50*m32*m05 -
                m12*m50*m23*m14 - m12*(m41*m41)*m05 + m12*m41*m32*m14 + m12*m41*(m23*m23) -
                m12*(m32*m32)*m23 - m03*m50*m32*m14 + m03*m50*(m23*m23) +
                m03*(m41*m41)*m14 - 2*m03*m41*m32*m23 + m03*(m32*m32*m32))/Math.pow(m00, 13);

            double I6 = ((m70*m70)*(m07*m07) - 14*m70*m61*m16*m07 + 18*m70*m52*m25*m07 + 24*m70*m52*(m16*m16) -
                10*m70*m43*m34*m07 - 60*m70*m43*m25*m16 + 40*m70*(m34*m34)*m16 + 24*(m61*m61)*m25*m07 +
                25*(m61*m61)*(m16*m16) - 60*m61*m52*m34*m07 - 234*m61*m52*m25*m16 + 40*m61*(m43*m43)*m07 +
                50*m61*m43*m34*m16 + 360*m61*m43*(m25*m25) - 240*m61*(m34*m34)*m25 + 360*(m52*m52)*m34*m16 +
                81*(m52*m52)*(m25*m25) - 240*m52*(m43*m43)*m16 - 990*m52*m43*m34*m25 + 600*m52*(m34*m34*m34) +
                600*(m43*m43*m43)*m25 - 375*(m43*m43)*(m34*m34))/Math.pow(m00, 18);

            double[] moments = new double[6];
            moments[0] = I1;
            moments[1] = I2;
            moments[2] = I3;
            moments[3] = I4;
            moments[4] = I5;
            moments[5] = I6;
            
            //Normalize by log
            if(normalize){
                for (int i = 0; i < moments.length; i++) {
                    moments[i] = Math.signum(moments[i]) * Math.log10(Math.abs(moments[i]) + 1);
                }
            }

            return moments;
        }
        else{
            throw new IllegalArgumentException("Suk Flusser Moments only works with grayscale images.");
        }
    }
}