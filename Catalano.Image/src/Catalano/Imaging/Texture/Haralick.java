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

package Catalano.Imaging.Texture;

/**
 * Haralick's texture classification metrics.
 * @author Diego Catalano
 */
public class Haralick {
    
    /**
     * Don't let anyone instantiate this class.
     */
    private Haralick(){};
    
    /**
     * Compute energy.
     * @param coocurrenceMatrix Coocurrence matrix.
     * @return Energy.
     */
    public static double Energy(double[][] coocurrenceMatrix){
        double r = 0;
        for (int i = 0; i < coocurrenceMatrix.length; i++) {
            for (int j = 0; j < coocurrenceMatrix[0].length; j++) {
                r += coocurrenceMatrix[i][j] * coocurrenceMatrix[i][j];
            }
        }
        return r;
    }
    
    /**
     * Compute entropy.
     * @param coocurrenceMatrix Coocurrence matrix.
     * @return Entropy.
     */
    public static double Entropy(double[][] coocurrenceMatrix){
        double r = 0;
        for (int i = 0; i < coocurrenceMatrix.length; i++) {
            for (int j = 0; j < coocurrenceMatrix[0].length; j++) {
                r += coocurrenceMatrix[i][j] * Catalano.Math.Tools.Log(coocurrenceMatrix[i][j], 2);
            }
        }
        return -r;
    }
    
    /**
     * Weighs probabilities by their distance to the diagonal.
     * @param coocurrenceMatrix Coocurrence matrix.
     * @return Contrast.
     */
    public static double Contrast(double[][] coocurrenceMatrix){
        double r = 0;
        for (int i = 0; i < coocurrenceMatrix.length; i++) {
            for (int j = 0; j < coocurrenceMatrix[0].length; j++) {
                r += Math.abs(i - j) * coocurrenceMatrix[i][j];
            }
        }
        return r;
    }
    
    /**
     * An exaggeration of the contrast metric, as it weighs probabilities by the square of the distance to the diagonal.
     * @param coocurrenceMatrix Coocurrence matrix.
     * @return Inertia.
     */
    public static double Inertia(double[][] coocurrenceMatrix){
        double r = 0;
        for (int i = 0; i < coocurrenceMatrix.length; i++) {
            for (int j = 0; j < coocurrenceMatrix[0].length; j++) {
                r += Math.pow((i - j),2) * coocurrenceMatrix[i][j];
            }
        }
        
        return r;
    }
    
    /**
     * Increases in the presence of large homogeneous regions.
     * @param coocurrenceMatrix Coocurrence matrix.
     * @return Correlation.
     */
    public static double Correlation(double[][] coocurrenceMatrix){
        double meanI = 0;
        double stdI = 0;
        for (int i = 0; i < coocurrenceMatrix.length; i++) {
            for (int j = 0; j < coocurrenceMatrix[0].length; j++) {
                meanI += coocurrenceMatrix[i][j];
            }
            for (int j = 0; j < coocurrenceMatrix[0].length; j++) {
                stdI += Math.pow(i - meanI, 2) * coocurrenceMatrix[i][j];
            }
        }
        
        double meanJ = 0;
        double stdJ = 0;
        for (int j = 0; j < coocurrenceMatrix[0].length; j++) {
            for (int i = 0; i < coocurrenceMatrix.length; i++) {
                meanJ += coocurrenceMatrix[i][j];
            }
            for (int i = 0; i < coocurrenceMatrix.length; i++) {
                stdJ += Math.pow(j - meanJ, 2) * coocurrenceMatrix[i][j];
            }
        }
        
        double r = 0;
        for (int i = 0; i < coocurrenceMatrix.length; i++) {
            for (int j = 0; j < coocurrenceMatrix[0].length; j++) {
                r += (i * j * coocurrenceMatrix[i][j] - meanI * meanJ) / stdI * stdJ;
            }
        }
        return r;
    }
    
    /**
     * Weighs the probabilities by proximity to the diagonal and is therefore the complement of contrast.
     * @param coocurrenceMatrix Coocurrence matrix.
     * @return Texture homogeneity.
     */
    public static double TextureHomogeneity(double[][] coocurrenceMatrix){
        double r = 0;
        for (int i = 0; i < coocurrenceMatrix.length; i++) {
            for (int j = 0; j < coocurrenceMatrix[0].length; j++) {
                r += coocurrenceMatrix[i][j] / (1 + Math.abs(i - j));
            }
        }
        return r;
    }
    
    /**
     * Valid only for nondiagonal elements i != j . Closely related to texture homogeneity.
     * @param coocurrenceMatrix Coocurrence matrix.
     * @return Inverse difference.
     */
    public static double InverseDifference(double[][] coocurrenceMatrix){
        double r = 0;
        for (int i = 0; i < coocurrenceMatrix.length; i++) {
            for (int j = 0; j < coocurrenceMatrix[0].length; j++) {
                r += coocurrenceMatrix[i][j] / Math.abs(i - j);
            }
        }
        return r;
    }
    
    /**
     * The metric complementary to inertia. Probabilities are weighted by proximity to the diagonal.
     * @param coocurrenceMatrix Coocurrence matrix.
     * @return Invere difference moment.
     */
    public static double InverseDifferenceMoment(double[][] coocurrenceMatrix){
        double r = 0;
        for (int i = 0; i < coocurrenceMatrix.length; i++) {
            for (int j = 0; j < coocurrenceMatrix[0].length; j++) {
                r += coocurrenceMatrix[i][j] / (1 + Math.pow((i - j), 2));
            }
        }
        return r;
    }
    
    /**
     * Probabilities are weighted by their deviation from the mean values.
     * @param coocurrenceMatrix Coocurrence matrix.
     * @return Cluster tendency.
     */
    public static double ClusterTendency(double[][] coocurrenceMatrix){
        double[] meanI = new double[coocurrenceMatrix.length];
        double[] meanJ = new double[coocurrenceMatrix[0].length];
        
        for (int i = 0; i < meanI.length; i++) {
            for (int j = 0; j < coocurrenceMatrix.length; j++) {
                meanI[i] += coocurrenceMatrix[i][j];
            }
            meanI[i] /= coocurrenceMatrix.length;
        }
        
        for (int j = 0; j < meanJ.length; j++) {
            for (int i = 0; i < coocurrenceMatrix.length; i++) {
                meanJ[i] += coocurrenceMatrix[i][j];
            }
            meanJ[j] /= coocurrenceMatrix[0].length;
        }
        
        double r = 0;
        for (int i = 0; i < coocurrenceMatrix.length; i++) {
            for (int j = 0; j < coocurrenceMatrix[0].length; j++) {
                r += Math.pow((i - meanI[i]) + (j - meanJ[j]), 2) * coocurrenceMatrix[i][j];
            }
        }
        return r;
    }
    
    /**
     * Compute cluster shade.
     * @param coocurrenceMatrix Coocurrence matrix.
     * @return Cluster shade.
     */
    public static double ClusterShade(double[][] coocurrenceMatrix){
        double[] meanI = new double[coocurrenceMatrix.length];
        double[] meanJ = new double[coocurrenceMatrix[0].length];
        
        for (int i = 0; i < meanI.length; i++) {
            for (int j = 0; j < coocurrenceMatrix.length; j++) {
                meanI[i] += coocurrenceMatrix[i][j];
            }
            meanI[i] /= coocurrenceMatrix.length;
        }
        
        for (int j = 0; j < meanJ.length; j++) {
            for (int i = 0; i < coocurrenceMatrix.length; i++) {
                meanJ[i] += coocurrenceMatrix[i][j];
            }
            meanJ[j] /= coocurrenceMatrix[0].length;
        }
        
        double r = 0;
        for (int i = 0; i < coocurrenceMatrix.length; i++) {
            for (int j = 0; j < coocurrenceMatrix[0].length; j++) {
                r += Math.pow((i - meanI[i]) + (j - meanJ[j]), 3) * coocurrenceMatrix[i][j];
            }
        }
        return r;
    }
    
    /**
     * Compute cluster prominence.
     * @param coocurrenceMatrix Coocurrence matrix.
     * @return Cluster priminence.
     */
    public static double ClusterProminence(double[][] coocurrenceMatrix){
        double[] meanI = new double[coocurrenceMatrix.length];
        double[] meanJ = new double[coocurrenceMatrix[0].length];
        
        for (int i = 0; i < meanI.length; i++) {
            for (int j = 0; j < coocurrenceMatrix.length; j++) {
                meanI[i] += coocurrenceMatrix[i][j];
            }
            meanI[i] /= coocurrenceMatrix.length;
        }
        
        for (int j = 0; j < meanJ.length; j++) {
            for (int i = 0; i < coocurrenceMatrix.length; i++) {
                meanJ[i] += coocurrenceMatrix[i][j];
            }
            meanJ[j] /= coocurrenceMatrix[0].length;
        }
        
        double r = 0;
        for (int i = 0; i < coocurrenceMatrix.length; i++) {
            for (int j = 0; j < coocurrenceMatrix[0].length; j++) {
                r += Math.pow((i - meanI[i]) + (j - meanJ[j]), 4) * coocurrenceMatrix[i][j];
            }
        }
        return r;
    }
}