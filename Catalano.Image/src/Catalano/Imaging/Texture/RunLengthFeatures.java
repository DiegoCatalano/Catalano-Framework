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

import Catalano.Imaging.Tools.*;

/**
 * Run-Length Metrics.
 * @author Diego Catalano
 */
public final class RunLengthFeatures {
    
    /**
     * Don't let anyone instantiate this class.
     */
    private RunLengthFeatures(){};
    
    /**
     * This metric increases when short runs dominate, for example, in fine-grained textures.
     * @param runMatrix Run length matrix.
     * @param numberPrimitives Number of primitives.
     * @return 
     */
    public static double ShortRunEmphasis(double[][] runMatrix, int numberPrimitives){
        double r = 0;
        for (int i = 0; i < runMatrix.length; i++) {
            for (int j = 1; j < runMatrix[0].length; j++) {
                r += runMatrix[i][j] / (j * j);
            }
        }
        return r / numberPrimitives;
    }
    
    /**
     * This metric increases when long runs dominate, for example, in textures with large homogeneous areas or coarse textures.
     * @param runMatrix Run length matrix.
     * @param numberPrimitives Number of primitives.
     * @return 
     */
    public static double LongRunEmphasis(double[][] runMatrix, int numberPrimitives){
        double r = 0;
        for (int i = 0; i < runMatrix.length; i++) {
            for (int j = 1; j < runMatrix[0].length; j++) {
                r += runMatrix[i][j] * j * j;
            }
        }
        
        return r / numberPrimitives;
    }
    
    /**
     * Emphasis is orthogonal to SRE, and the metric increases when the texture is dominated by many runs of low gray value.
     * @param runMatrix Run length matrix.
     * @param numberPrimitives Number of primitives.
     * @return 
     */
    public static double LowGrayLevelEmphasis(double[][] runMatrix, int numberPrimitives){
        double r = 0;
        for (int i = 1; i < runMatrix.length; i++) {
            for (int j = 0; j < runMatrix[0].length; j++) {
                r += runMatrix[i][j] / (i * i);
            }
        }
        return r / numberPrimitives;
    }
    
    /**
     * Emphasis is orthogonal to LRE, and the metric increases when the texture is dominated by many runs of high gray value.
     * @param runMatrix Run length matrix.
     * @param numberPrimitives Number of primitives.
     * @return 
     */
    public static double HighGrayLevelEmphasis(double[][] runMatrix, int numberPrimitives){
        double r = 0;
        for (int i = 1; i < runMatrix.length; i++) {
            for (int j = 0; j < runMatrix[0].length; j++) {
                r += runMatrix[i][j] * i * i;
            }
        }
        return r / numberPrimitives;
    }
    
    /**
     * This is a diagonal metric that combines SRE and LGRE. The metric increases when the texture is dominated by many short runs of low gray value.
     * @param runMatrix Run length matrix.
     * @param numberPrimitives Number of primitives.
     * @return 
     */
    public static double ShortRunLowGrayLevelEmphasis(double[][] runMatrix, int numberPrimitives){
        double r = 0;
        for (int i = 1; i < runMatrix.length; i++) {
            for (int j = 1; j < runMatrix[0].length; j++) {
                r += runMatrix[i][j] / ((i * i) * (j * j));
            }
        }
        return r / numberPrimitives;
    }
    
    /**
     * This metric is orthogonal to SRLGE and LRHGE and increases when the texture is dominated by short runs with high intensity levels.
     * @param runMatrix Run length matrix.
     * @param numberPrimitives Number of primitives.
     * @return 
     */
    public static double ShortRunHighGrayLevelEmphasis(double[][] runMatrix, int numberPrimitives){
        double r = 0;
        for (int i = 1; i < runMatrix.length; i++) {
            for (int j = 1; j < runMatrix[0].length; j++) {
                r += (runMatrix[i][j] * i * i) / (j * j);
            }
        }
        return r / numberPrimitives;
    }
    
    /**
     * Complementary to SRHGE, it increases when the texture is dominated by long runs that have low gray levels.
     * @param runMatrix Run length matrix.
     * @param numberPrimitives Number of primitives.
     * @return 
     */
    public static double LongRunLowGrayLevelEmphasis(double[][] runMatrix, int numberPrimitives){
        double r = 0;
        for (int i = 1; i < runMatrix.length; i++) {
            for (int j = 1; j < runMatrix[0].length; j++) {
                r += runMatrix[i][j] * j * j / (i * i);
            }
        }
        return r / numberPrimitives;
    }
    
    /**
     * This is the complementary metric to SRLGE and increases with a combination of long, high-gray value runs.
     * @param runMatrix Run length matrix.
     * @param numberPrimitives Number of primitives.
     * @return 
     */
    public static double LongRunHighGrayLevelEmphasis(double[][] runMatrix, int numberPrimitives){
        double r = 0;
        for (int i = 1; i < runMatrix.length; i++) {
            for (int j = 1; j < runMatrix[0].length; j++) {
                r += runMatrix[i][j] * j * j * i * i;
            }
        }
        return r / numberPrimitives;
    }
    
    /**
     * This metric increases when gray-level outliers dominate the histogram.
     * @param runMatrix Run length matrix.
     * @param numberPrimitives Number of primitives.
     * @return 
     */
    public static double GrayLevelNonUniformity(double[][] runMatrix, int numberPrimitives){
        double r = 0;
        double sumJ = 0;
        for (int i = 1; i < runMatrix.length; i++) {
            r += sumJ * sumJ;
            for (int j = 1; j < runMatrix[0].length; j++) {
                sumJ += runMatrix[i][j];
            }
        }
        return r / numberPrimitives;
    }
    
    /**
     * This metric increases when few run-length outliers dominate the histogram.
     * @param runMatrix Run length matrix.
     * @param numberPrimitives Number of primitives.
     * @return 
     */
    public static double RunLengthNonUniformity(double[][] runMatrix, int numberPrimitives){
        double r = 0;
        double sumI = 0;
        for (int j = 1; j < runMatrix[0].length; j++) {
            r += sumI * sumI;
            for (int i = 1; i < runMatrix.length; i++) {
                sumI += runMatrix[i][j];
            }
        }
        return r / numberPrimitives;
    }
    
    /**
     * 
     * @param runMatrix Run length matrix.
     * @param numberPrimitives Number of primitives.
     * @return 
     */
    public static double GrayLevelDistribution(double[][] runMatrix, int numberPrimitives){
        double r = 0;
        for (int i = 0; i < runMatrix.length; i++) {
            for (int j = 0; j < runMatrix[0].length; j++) {
                r += Math.pow(runMatrix[i][j] * j * j, 2);
            }
        }
        return r / numberPrimitives;
    }
    
    /**
     * 
     * @param runMatrix Run length matrix.
     * @param numberPrimitives Number of primitives.
     * @return 
     */
    public static double RunLenghtDistribution(double[][] runMatrix, int numberPrimitives){
        double r = 0;
        for (int j = 0; j < runMatrix[0].length; j++) {
            for (int i = 0; i < runMatrix.length; i++) {
                r += Math.pow(runMatrix[i][j] * i * i, 2);
            }
        }
        return r / numberPrimitives;
    }
    
    /**
     * This metric provides information on the overall homogeneity of the histogram and is maximal when all runs are of unity length irrespective of the gray level.
     * @param runMatrix Run length matrix.
     * @param numberPossiblePrimitives Number of primitives.
     * @return 
     */
    public static double RunPercentage(double[][] runMatrix, int numberPossiblePrimitives){
        double r = 0;
        for (int i = 0; i < runMatrix.length; i++) {
            for (int j = 0; j < runMatrix[0].length; j++) {
                r += runMatrix[i][j];
            }
        }
        return r / numberPossiblePrimitives;
    }
}