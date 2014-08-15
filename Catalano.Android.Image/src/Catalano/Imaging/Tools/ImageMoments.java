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
 * Image Moments.
 * <para>Moment invariants are properties of connected regions in binary images that are invariant to translation, rotation and scale.
 * They are useful because they define a simply calculated set of region properties that can be used for shape classification and part recognition.</para>
 * 
 * @see HuMoments
 * @see ZernikeMoments
 * @author Diego Catalano
 */
public final class ImageMoments {

    /**
     * Don't let anyone instantiate this class.
     */
    private ImageMoments() {}
    
    /**
     * Compute Raw moment.
     * @param p Order p.
     * @param q Order q.
     * @param image Image as array.
     * @return Raw moment.
     */
    public static double getRawMoment(int p, int q, double[][] image) {
        double m = 0;
        for (int i = 0, k = image.length; i < k; i++) {
                for (int j = 0, l = image[i].length; j < l; j++) {
                    m += Math.pow(i, p) * Math.pow(j, q) * image[i][j];
                }
        }
        return m;
    }

    /**
     * Compute Central moment.
     * @param p Order p.
     * @param q Order q.
     * @param img Image as array.
     * @return Central moment.
     */
    public static double getCentralMoment(int p, int q, double[][] img) {
            double mc = 0;
            double m00 = ImageMoments.getRawMoment(0, 0, img);
            double m10 = ImageMoments.getRawMoment(1, 0, img);
            double m01 = ImageMoments.getRawMoment(0, 1, img);
            double x0 = m10 / m00;
            double y0 = m01 / m00;
            for (int i = 0, k = img.length; i < k; i++) {
                    for (int j = 0, l = img[i].length; j < l; j++) {
                            mc += Math.pow((i - x0), p) * Math.pow((j - y0), q) * img[i][j];
                    }
            }
            return mc;
    }

    /**
     * Compute Covariance XY.
     * @param p Order p.
     * @param q Order q.
     * @param image Image as array.
     * @return Covariance.
     */
    public static double getCovarianceXY(int p, int q, double[][] image) {
            double mc00 = ImageMoments.getCentralMoment(0, 0, image);
            double mc11 = ImageMoments.getCentralMoment(1, 1, image);
            return mc11 / mc00;
    }

    /**
     * Compute Variance X.
     * @param p Order p.
     * @param q Order q.
     * @param image Image as array.
     * @return Variance.
     */
    public static double getVarianceX(int p, int q, double[][] image) {
            double mc00 = ImageMoments.getCentralMoment(0, 0, image);
            double mc20 = ImageMoments.getCentralMoment(2, 0, image);
            return mc20 / mc00;
    }

    /**
     * Compute Variance Y.
     * @param p Order p.
     * @param q Order q.
     * @param image Image as array.
     * @return Variace.
     */
    public static double getVarianceY(int p, int q, double[][] image) {
            double mc00 = ImageMoments.getCentralMoment(0, 0, image);
            double mc02 = ImageMoments.getCentralMoment(0, 2, image);
            return mc02 / mc00;
    }
    
    /**
     * Compute Normalized Central Moment.
     * @param p Order p.
     * @param q Order q.
     * @param image Image as array.
     * @return Normalized Central Moment.
     */
    public static double getNormalizedCentralMoment(int p, int q, double[][] image) {
            double gama = ((p + q) / 2) + 1;
            double mpq = ImageMoments.getCentralMoment(p, q, image);
            double m00gama = Math.pow(ImageMoments.getCentralMoment(0, 0, image), gama);
            return mpq / m00gama;
    }
}