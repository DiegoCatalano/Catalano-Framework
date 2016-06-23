// Catalano Statistics Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
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

package Catalano.Statistics.Regression;

import Catalano.Math.Decompositions.QRDecomposition;
import Catalano.Statistics.Correlations;

/**
 * Polynomial Regression.
 * @author Diego Catalano
 */
public class PolynomialRegression implements ISimpleRegression{
    
    private double[] x;
    private double[] y;
    private double[][] beta;
    private int degree = 2;

    /**
     * Get degree.
     * @return Degree.
     */
    public int getDegree() {
        return degree;
    }

    /**
     * Set degree.
     * @param degree Degree.
     */
    public void setDegree(int degree) {
        this.degree = Math.max(degree, 2);
    }

    /**
     * Initializes a new instance of the PolynomialRegression class.
     * @param x Data.
     * @param y Data.
     */
    public PolynomialRegression(double[] x, double[] y) {
        this(x,y,2);
    }
    
    /**
     * Initializes a new instance of the PolynomialRegression class.
     * @param x Data.
     * @param y Data.
     * @param degree Degree.
     */
    public PolynomialRegression(double[] x, double[] y, int degree){
        this.x = x;
        this.y = y;
        this.degree = degree;

        // create vendermonde matrix
        double[][] m = new double[x.length][degree+1];
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j <= degree; j++) {
                m[i][j] = Math.pow(x[i], j);
            }
        }

        // create matrix from vector
        double[][] Y = CreateMatrix(y, y.length);

        // find least squares solution
        QRDecomposition qr = new QRDecomposition(m);
        beta = qr.solve(Y);
        
    }
    
    private double[][] CreateMatrix(double[] v, int n){
        int m = v.length;
        n = (m != 0 ? v.length/m : 0);
        if (m*n != v.length) {
           throw new IllegalArgumentException("Array length must be a multiple of m.");
        }
        double[][] A = new double[m][n];
        for (int i = 0; i < m; i++) {
           for (int j = 0; j < n; j++) {
              A[i][j] = v[i+j*m];
           }
        }
        return A;
    }

    @Override
    public double Regression(double x) {
        
        double r = beta[0][0];
        double xx = x;
        for (int i = 1; i < beta.length; i++) {
            r += beta[i][0] * xx;
            xx *= x;
        }
        
        return r;
    }

    @Override
    public double[] Regression(double[] x) {
        double[] result = new double[x.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = beta[0][0];
            double xx = x[i];
            for (int j = 1; j < beta.length; j++) {
                result[i] += beta[j][0] * xx;
                xx *= x[i];
            }
        }
        return result;
    }

    @Override
    public double CoefficientOfDetermination() {
        double[] r = Regression(x);
        return Math.pow(Correlations.PearsonCorrelation(r, y), 2);
    }

    @Override
    public String toString() {
        
        String equation = "y = ";
        
        int p = beta.length - 1;
        for (int i = 0; i < beta.length; i++) {
            if (p != 0)
                equation += String.format("%.4f", beta[p][0]) + "x^" + p + " ";
            else
                equation += String.format("%.4f", beta[p][0]);
            p--;
        }
        
        return equation;
    }
}