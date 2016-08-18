// Catalano Statistics Library
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

package Catalano.Statistics;

import Catalano.Math.Matrix;
import java.util.Arrays;

/**
 * Common tools used in statistics.
 * @author Diego Catalano
 */
public class Tools {

    private Tools() {}
    
    /**
     * Coefficient of variation.
     * @param x Vector.
     * @return Coefficient of variation.
     */
    public static double CoefficientOfVariation(double[] x){
        double mean = Mean(x);
        double std = Math.sqrt(Variance(x, mean));
        return std / mean;
    }
    
    /**
     * Create a pearson correlation matrix.
     * @param data Data.
     * @return Correlation matrix.
     */
    public static double[][] Correlation(double[][] data){
        double[][] co = new double[data[0].length][data[0].length];
        for (int i = 0; i < co.length; i++) {
            for (int j = 0; j < co[0].length; j++) {
                if(i == j){
                    co[i][j] = 1;
                }
                else{
                    double[] colX = Matrix.getColumn(data, i);
                    double[] colY = Matrix.getColumn(data, j);
                    co[i][j] = Correlations.PearsonCorrelation(colX, colY);
                }
            }
        }
        return co;
    }
    
    /**
     * Covariance between vector x and y.
     * @param x Vector.
     * @param y Vector.
     * @return Covariance between x and y.
     */
    public static double Covariance(double[] x, double[] y){
        if (x.length != y.length)
            throw new IllegalArgumentException("The size of both matrix needs be equal");
        
        double meanX = 0, meanY = 0;
        for (int i = 0; i < x.length; i++) {
            meanX += x[i];
            meanY += y[i];
        }
        meanX /= x.length;
        meanY /= y.length;
        
        return Covariance(x,y,meanX,meanY);
    }
    
    /**
     * Covariance between vector x and y.
     * @param x Vector.
     * @param y Vector.
     * @param meanX X mean.
     * @param meanY Y mean.
     * @return Covariance between x and y.
     */
    public static double Covariance(double[] x, double[] y, double meanX, double meanY){
        double result = 0;
        for (int i = 0; i < x.length; i++) {
            result += (x[i] - meanX) * (y[i] - meanY);
        }
        
        return result / (double)(x.length - 1);
    }
    
    /**
     * Matrix of covariance.
     * @param matrix Matrix.
     * @return Matrix of covariance.
     */
    public static double[][] Covariance(double[][] matrix){
        double[] means = new double[matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                means[j] += matrix[i][j];
            }
        }
        
        for (int i = 0; i < means.length; i++) {
            means[i] /= means.length;
        }
        
        return Covariance(matrix, means);
    }
    
    /**
     * Matrix of covariance.
     * @param matrix Matrix.
     * @param means Means.
     * @return Matrix of covariance.
     */
    public static double[][] Covariance(double[][] matrix, double[] means){
        double[][] cov = new double[means.length][means.length];
        
        for (int i = 0; i < cov.length; i++) {
            for (int j = 0; j < cov[0].length; j++) {
                cov[i][j] = Covariance(Matrix.getColumn(matrix, i), Matrix.getColumn(matrix, j), means[i], means[j]);
            }
        }
        
        return cov;
        
    }
    
    /**
     * Fisher.
     * @param n Number.
     * @return Fisher number.
     */
    public static double Fisher(double n){
        
        if ((n <= -1) || (n >= 1))
            throw new IllegalArgumentException("Fisher works with number between -1 < x < 1");
        
        double r = (1 + n) / (1 - n);
        return 0.5 * Math.log(r);
    }
    
    /**
     * Inclination.
     * @param x Vector.
     * @param y Vector.
     * @return Inclination between the vector x and y.
     */
    public static double Inclination(double[] x, double[] y){
        if (x.length != y.length)
            throw new IllegalArgumentException("The size of both matrix needs be equal");
        
        double meanX = 0; double meanY = 0;
        for (int i = 0; i < x.length; i++) {
            meanX += x[i];
            meanY += y[i];
        }
        
        meanX /= x.length;
        meanY /= y.length;
        
        double num = 0, den = 0;
        for (int i = 0; i < x.length; i++) {
            num += (x[i] - meanX) * (y[i] - meanY);
            den += Math.pow((x[i] - meanX),2);
        }
        
        return num/den;
    }
    
    /**
     * Inverse fisher.
     * @param n Number.
     * @return Inverse fisher of the number.
     */
    public static double InverseFisher(double n){
        if ((n <= -1) || (n >= 1))
            throw new IllegalArgumentException("Fisher works with number between -1 < x < 1");
        
        double r = (Math.pow(Math.E,2 * n) - 1) / (Math.pow(Math.E,2 * n) + 1);
        return r;
    }
    
    /**
     * Interception.
     * @param x Vector.
     * @param y Vector.
     * @return Interception between the vector x and y.
     */
    public static double Interception(double[] x, double[] y){
        if (x.length != y.length)
            throw new IllegalArgumentException("The size of both matrix needs be equal");
        
        double meanX = 0; double meanY = 0;
        for (int i = 0; i < x.length; i++) {
            meanX += x[i];
            meanY += y[i];
        }
        
        meanX /= x.length;
        meanY /= y.length;
        
        double b = Inclination(x, y);
        double a = meanY - (b * meanX);
        return a;
    }
    
    /**
     * Maximum element.
     * @param x Vector.
     * @return Maximum element of the vector,
     */
    public static double Max(double[] x){
        double m = x[0];
        for (int i = 1; i < x.length; i++)
            if(x[i] > m) m = x[i];
        
        return m;
    }
    
    /**
     * Mean.
     * @param x Vector.
     * @return Mean of the vector.
     */
    public static double Mean(double[] x){
        double r = 0;
        for (int i = 0; i < x.length; i++) {
            r += x[i];
        }
        
        return r / (double)x.length;
    }
    
    /**
     * Mean of the matrix for each column.
     * @param data Data.
     * @return Mean of the each column.
     */
    public static double[] Mean(double[][] data){
        
        double[] means = new double[data[0].length];
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                means[j] += data[i][j];
            }
        }

        for (int i = 0; i < means.length; i++) {
            means[i] /= (double)data.length;
        }
        return means;
        
    }
    
    /**
     * Minimum element.
     * @param x Vector.
     * @return Minimum element of the vector.
     */
    public static double Min(double[] x){
        double m = x[0];
        for (int i = 1; i < x.length; i++)
            if(x[i] < m) m = x[i];
        
        return m;
    }
    
    /**
     * Mode of the vector.
     * @param values Values.
     * @return Mode.
     */
    public static double Mode(double[] values){
        Arrays.sort(values);
        double v = values[0];
        int index = 0, x = 0, rep = 0;
        for (int i = 1; i < values.length; i++) {
            if (values[i] == v) {
                x++;
                if (x > rep) {
                    rep = x;
                    index = i;
                }
                v = values[i];
                x = 0;
            }
            else{
                if (x > rep) {
                    rep = x;
                    index = i;
                }
                v = values[i];
                x = 0;
            }
        }
        return values[index];
    }
    
    /**
     * Mode of the vector.
     * @param values Values.
     * @return Mode.
     */
    public static int Mode(int[] values){
        Arrays.sort(values);
        int v = values[0];
        int index = 0, x = 0, rep = 0;
        for (int i = 1; i < values.length; i++) {
            if (values[i] == v) {
                x++;
                if (x > rep) {
                    rep = x;
                    index = i;
                }
                v = values[i];
                x = 0;
            }
            else{
                if (x > rep) {
                    rep = x;
                    index = i;
                }
                v = values[i];
                x = 0;
            }
        }
        return values[index];
    }
    
    /**
     * Geometric mean.
     * @param x Vector.
     * @return Geometric mean.
     */
    public static double GeometricMean(double[] x){
        
        double r = 1;
        for (int i = 0; i < x.length; i++) {
            r *= x[i];
        }
        
        return Math.pow(r,(double)1/x.length);
    }
    
    /**
     * Harmonic mean.
     * @param x Vector.
     * @return Harmonic mean.
     */
    public static double HarmonicMean(double[] x){
        
        double r = 0;
        for (int i = 0; i < x.length; i++) {
            r += 1 / x[i];
        }
        
        return x.length / r;
    }
    
    /**
     * Contra Harmonic mean.
     * @param x Vector.
     * @param order Order.
     * @return Contra Harmonic mean.
     */
    public static double ContraHarmonicMean(double[] x, int order){
        
        double r1 = 0, r2 = 0;
        for (int i = 0; i < x.length; i++) {
            r1 += Math.pow(x[i], order + 1);
            r2 += Math.pow(x[i], order);
        }
        
        return r1 / r2;
    }
    
    /**
     * Sum.
     * @param x Vector.
     * @return Sum of the all elements.
     */
    public static double Sum(double[] x){
        double sum = 0;
        for (int i = 0; i < x.length; i++) {
            sum += x[i];
        }
        return sum;
    }
    
    /**
     * Variance
     * @param x Vector.
     * @return Variance of the vector.
     */
    public static double Variance(double[] x){
        return Variance(x, Mean(x));
    }
    
    /**
     * Variance.
     * @param x Vector.
     * @param mean Mean.
     * @return Variance of the vector.
     */
    public static double Variance(double[] x, double mean){
        double sum = 0;
        for (int i = 0; i < x.length; i++) {
            sum += Math.pow((x[i] - mean), 2);
        }
        double var = sum / ((double)x.length - 1);
        return var;
    }
    
    /**
     * Standart deviation.
     * @param x Vector.
     * @return Standart deviation of the vector.
     */
    public static double StandartDeviation(double[] x){
        return Math.sqrt(Variance(x));
    }
    
    /**
     * Standart deviation.
     * @param x Vector.
     * @param mean Mean.
     * @return Standart deviation of the vector.
     */
    public static double StandartDeviation(double[] x, double mean){
        return Math.sqrt(Variance(x, mean));
    }
    
    /**
     * Standart deviation for each column.
     * @param data Data.
     * @return Standart deviation of the data.
     */
    public static double[] StandartDeviation(double[][] data){
        double[] means = Mean(data);
        return StandartDeviation(data, means);
    }
    
    /**
     * Standart deviation for each column.
     * @param data Data.
     * @param means Means of the columns.
     * @return Standart deviation of the data.
     */
    public static double[] StandartDeviation(double[][] data, double[] means){
        
        double[] std = new double[means.length];
        
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                std[j] += Math.pow(data[i][j] - means[j], 2);
            }
        }

        for (int i = 0; i < std.length; i++) {
            std[i] = Math.sqrt(std[i] / ((double)data.length - 1D));
        }
        
        return std;
    }
}