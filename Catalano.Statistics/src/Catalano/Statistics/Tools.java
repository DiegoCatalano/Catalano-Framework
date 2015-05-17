// Catalano Statistics Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2015
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

/**
 *
 * @author Diego Catalano
 */
public class Tools {

    private Tools() {}
    
    public static double CoefficientOfVariation(double[] x){
        double mean = Mean(x);
        double std = Math.sqrt(Variance(x, mean));
        return std / mean;
    }
    
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
        
        double result = 0;
        for (int i = 0; i < x.length; i++) {
            result += (x[i] - meanX) * (y[i] - meanY);
        }
        
        result = (1/(double)x.length) * result;
        
        return result;
        
    }
    
    public static double Fisher(double n){
        
        if ((n <= -1) || (n >= 1))
            throw new IllegalArgumentException("Fisher works with number between -1 < x < 1");
        
        double r = (1 + n) / (1 - n);
        return 0.5 * Math.log(r);
    }
    
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
    
    public static double InverseFisher(double n){
        if ((n <= -1) || (n >= 1))
            throw new IllegalArgumentException("Fisher works with number between -1 < x < 1");
        
        double r = (Math.pow(Math.E,2 * n) - 1) / (Math.pow(Math.E,2 * n) + 1);
        return r;
    }
    
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
    
    public static double JensenShannonDivergence(double[] x, double[] y) {
        double[] m = new double[x.length];
        for (int i = 0; i < m.length; i++) {
            m[i] = (x[i] + y[i]) / 2;
        }

        return (KullbackLeiblerDivergence(x, m) + KullbackLeiblerDivergence(y, m)) / 2;
    }
    
    public static double KullbackLeiblerDivergence(double[] x, double[] y) {
        
        if(x.length != y.length)
            throw new IllegalArgumentException("The size of x and y must be equal.");
        
        boolean intersection = false;
        double kl = 0.0;

        for (int i = 0; i < x.length; i++) {
            if (x[i] != 0.0 && y[i] != 0.0) {
                intersection = true;
                kl += x[i] * Math.log(x[i] / y[i]);
            }
        }

        if (intersection) {
            return kl;
        } else {
            return Double.POSITIVE_INFINITY;
        }
    }
    
    public static double Max(double[] x){
        double m = x[0];
        for (int i = 1; i < x.length; i++)
            if(x[i] > m) m = x[i];
        
        return m;
    }
    
    public static double Mean(double[] x){
        double r = 0;
        for (int i = 0; i < x.length; i++) {
            r += x[i];
        }
        
        return r / x.length;
    }
    
    public static double Min(double[] x){
        double m = x[0];
        for (int i = 1; i < x.length; i++)
            if(x[i] < m) m = x[i];
        
        return m;
    }
    
    public static double GeometricMean(double[] x){
        
        double r = 1;
        for (int i = 0; i < x.length; i++) {
            r *= x[i];
        }
        
        return Math.pow(r,(double)1/x.length);
    }
    
    public static double HarmonicMean(double[] x){
        
        double r = 0;
        for (int i = 0; i < x.length; i++) {
            r += 1 / x[i];
        }
        
        return x.length / r;
    }
    
    public static double ContraHarmonicMean(double[] x, int order){
        
        double r1 = 0, r2 = 0;
        for (int i = 0; i < x.length; i++) {
            r1 += Math.pow(x[i], order + 1);
            r2 += Math.pow(x[i], order);
        }
        
        return r1 / r2;
    }
    
    public static double Sum(double[] x){
        double sum = 0;
        for (int i = 0; i < x.length; i++) {
            sum += x[i];
        }
        return sum;
    }
    
    public static double Variance(double[] x){
        return Variance(x, Mean(x));
    }
    
    public static double Variance(double[] x, double mean){
        double sum = 0;
        for (int i = 0; i < x.length; i++) {
            sum += Math.pow((x[i] - mean), 2);
        }
        double var = sum / ((double)x.length - 1);
        return var;
    }
    
    public static double StandartDeviation(double[] x){
        return Math.sqrt(Variance(x));
    }
}