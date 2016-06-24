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

import Catalano.Core.ArraysUtil;

/**
 * Used to compare two vectors in feature space by finding the similarity between the two.
 * @author Diego Catalano
 */
public final class Correlations {
    
    /**
     * Don't let anyone instantiate this class.
     */
    private Correlations(){};
    
    /**
     * Inner Product
     * @param p Vector 1
     * @param q Vector 2
     * @return Inner Product
     */
    public static double VectorInnerProduct(double[] p, double[] q){
        double product = 0;
        for (int i = 0; i < p.length; i++) {
            product += p[i] * q[i];
        }
        return product;
    }
    
    /**
     * Takes on values between 0 and 1; 1 for identical vectors
     * @param p Vector 1
     * @param q Vector 2
     * @return Tanimoto metric
     */
    public static double Tanimoto(double[] p, double[] q){
        double product = 0;
        double x = 0,y = 0;
        for (int i = 0; i < p.length; i++) {
            product += p[i] * q[i];
            x += p[i]*p[i];
            y += q[i]*q[i];
        }
        return product/(x+y-product);
    }
    
    public static double PearsonCorrelation(double[] x, double[] y){
        double meanX = Tools.Mean(x);
        double meanY = Tools.Mean(y);
        return PearsonCorrelation(x, y, meanX, meanY);
    }
    
    public static double PearsonCorrelation(double[] x, double[] y, double meanX, double meanY){
        double sumNum = 0, sumDenX = 0, sumDenY = 0;
        for (int i = 0; i < x.length; i++) {
            sumNum += (x[i] - meanX) * (y[i] - meanY);
            sumDenX += Math.pow((x[i] - meanX), 2);
            sumDenY += Math.pow((y[i] - meanY), 2);
        }
        double sumDen = Math.sqrt(sumDenX * sumDenY);
        return sumNum/sumDen;
    }
    
    public static double SpearmanCorrelation(double[] x, double[] y){
        
        int[] r1 = ArraysUtil.Argsort(x, true);
        int[] r2 = ArraysUtil.Argsort(y, true);
        
        double diff = 0;
        for (int i = 0; i < r1.length; i++) {
            diff += r1[i] - r2[i];
        }
        diff *= 6;
        
        double den = r1.length * (Math.pow(r1.length,2) - 1);
        
        return 1 - (diff / den);
    }
}
