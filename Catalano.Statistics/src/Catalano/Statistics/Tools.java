// Catalano Statistics Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2013
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
    
    public static double Covariance(double[] x, double[] y){
        if (x.length != y.length) {
            try {
                throw new IllegalArgumentException("The size of both matrix needs be equal");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
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
        
        if ((n <= -1) || (n >= 1)) {
            try {
                throw new IllegalArgumentException("Fisher works with number between -1 < x < 1");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        double r = (1 + n) / (1 - n);
        return 0.5 * Math.log(r);
    }
    
    public static double Inclination(double[] x, double[] y){
        if (x.length != y.length) {
            try {
                throw new IllegalArgumentException("The size of both matrix needs be equal");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
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
        if ((n <= -1) || (n >= 1)) {
            try {
                throw new IllegalArgumentException("Fisher works with number between -1 < x < 1");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        double r = (Math.pow(Math.E,2 * n) - 1) / (Math.pow(Math.E,2 * n) + 1);
        return r;
    }
    
    public static double Interception(double[] x, double[] y){
        if (x.length != y.length) {
            try {
                throw new IllegalArgumentException("The size of both matrix needs be equal");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
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
    
    public static double Mean(double[] x){
        double r = 0;
        for (int i = 0; i < x.length; i++) {
            r += x[i];
        }
        
        return r / x.length;
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
    
    public static double Variance(double[] x){
        double sum = 0;
        double mean = Mean(x);
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