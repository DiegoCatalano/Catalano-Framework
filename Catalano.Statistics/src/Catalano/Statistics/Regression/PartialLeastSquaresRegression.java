// Catalano Statistics Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2013-2016
// diego.catalano at live.com
//
// Copyright © César Souza, 2009-2013
// cesarsouza at gmail.com
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

import Catalano.Math.Matrix;
import java.util.Arrays;

/**
 * Partial Least-Squares Regression (PLS).
 * @author Diego Catalano
 */
public class PartialLeastSquaresRegression {
    
    private double tolerance;
    private int factors;
    
    private double[][] P; //loadings matrix for X
    private double[][] Q; //loadings matrix for Y
    private double[][] T; //score matrix for X
    private double[][] U; //score matrix for Y
    private double[][] W; //PLS weight matrix
    private double[][] coeff;
    
    private double[] B; //diagonal matrix of diagonal coefficients
    
    public double[][] getScoreMatrixX(){return T;}
    public double[][] getScoreMatrixY(){return U;}
    public double[][] getLoadingMatrixX(){return P;}
    public double[][] getLoadingMatrixY(){return Q;}
    public double[][] getW(){return W;}
    public double[] getB(){return B;}
    public double[][] getCoefficients(){return coeff;}

    /**
     * Get tolerance.
     * @return Tolerance.
     */
    public double getTolerance() {
        return tolerance;
    }

    /**
     * Set tolerance.
     * @param tolerance Tolerance.
     */
    public void setTolerance(double tolerance) {
        this.tolerance = tolerance;
    }
    
    /**
     * Initializes a new instance of the PartialLeastSquaresRegression class.
     * @param factors Factors.
     */
    public PartialLeastSquaresRegression(int factors){
        this(factors, 1e-14);
    }

    /**
     * Initializes a new instance of the PartialLeastSquaresRegression class.
     * @param factors Factors.
     * @param tolerance Tolerance.
     */
    public PartialLeastSquaresRegression(int factors, double tolerance) {
        this.tolerance = tolerance;
    }
    
    /**
     * Compute.
     * @param X Matrix data.
     * @param Y Matrix data.
     */
    public void Compute(double[][] X, double[][] Y){
        
        int rows = X.length;
        int xcols = X[0].length;
        int ycols = Y[0].length;
      
        double[][] E = Matrix.Copy(X);
        double[][] F = Matrix.Copy(Y);
      
        T = new double[rows][factors];
        U = new double[rows][factors];
        P = new double[xcols][factors];
        Q = new double[ycols][factors];
        W = new double[xcols][factors];
        B = new double[factors];
      
        double[] varX = new double[factors];
        double[] varY = new double[factors];
      
        // Initialize the algorithm
        for (int factor = 0; factor < factors; factor++){
            
            // Select t as the largest column from X,
            double[] t = Matrix.getColumn(E, largest(E));//largest(E);
            
            // Select u as the largest column from Y.
            double[] u = Matrix.getColumn(F, largest(F));
            
            // Store weights for X and Y
            double[] w = new double[xcols];
            double[] c = new double[ycols];

            double norm_t = Euclidean(t);
          
            // Iteration region
            while (norm_t > tolerance){
                
                // Store initial t to check convergence
                double[] t0 = Arrays.copyOf(t, t.length);
                
                // Step 1: Estimate w (X weights)
                // Compute w = E'*u;
                w = new double[xcols];
                for (int j = 0; j < w.length; j++){
                    for (int i = 0; i < u.length; i++){
                        w[j] = w[j] + E[i][j] * u[i];
                    }
                }
                
                // Step 1.2: Normalize w (w = w/norm(w))
                double Ew = Euclidean(w);
                for (int i = 0; i < w.length; i++){
                    w[i] = w[i]/Ew;
                }
                
                // Step 2: Estimate t (X factor scores)
                // Compute t = E*w
                t = new double[rows];
                for (int i = 0; i < t.length; i++){
                    for (int j = 0; j < w.length; j++){
                        t[i] = t[i] + E[i][j] * w[j];
                    }
                }
                
                // Step 2.2: Normalize t: t = t/norm(t)
                double Et = Euclidean(t);
                for (int i = 0; i < t.length; i ++){
                    t[i] = t[i]/Et;
                }
                
                // Step 3: Estimate c (Y weights): c = F't
                // Compute c = F'*t0;
                c = new double[ycols];
                for (int j = 0; j < c.length; j++){
                    for (int i = 0; i < t.length; i++){
                        c[j] = c[j] + F[i][j] * t[i];
                    }
                }
                
                // Normalize q: c = c/norm(q)
                double Ec = Euclidean(c);
                for (int i = 0; i < c.length; i++){c[i] = c[i]/Ec;}
                
                // Step 4: Estimate u (Y scores)
                // Compute u = F*q;
                u = new double[rows];
                for (int i = 0; i < u.length; i++){
                    for (int j = 0; j < c.length; j++){
                        u[i] = u[i] + F[i][j] * c[j];
                    }
                }
                
                // Recalculate norm of the difference
                norm_t = 0.0;
                for (int i = 0; i < t.length; i++){
                    double d = (t0[i] - t[i]);
                    norm_t += d * d;
                }
                norm_t = Math.sqrt(norm_t);
            }
          
            // Compute the value of b which is used to
            // predict Y from t as b = t'u [Abdi, 2010]ű
            double b = 0;
            for (int i = 0; i < t.length; i++){
                b = b + t[i] * u[i];
            }
            
            // Compute factor loadings for X as p = E'*t [Abdi, 2010]
            double[] p = new double[xcols];
            for (int j = 0; j < p.length; j++){
                for (int i = 0; i < rows; i++){
                    p[j] = p[j] + E[i][j] * t[i];
                }
            }
            
            // Perform deflaction of X and Y
            for (int i = 0; i < t.length; i++){
                // Deflate X as X = X - t*p';
                for (int j = 0; j < p.length; j++){
                     E[i][j] = E[i][j] - t[i] * p[j];
                }
                // Deflate Y as Y = Y - b*t*q';
                for (int j = 0; j < c.length; j++){
                    F[i][j] = F[i][j] - b * t[i] * c[j];
                }
            }
            
            // Calculate explained variances
            varY[factor] = b * b;
            double temp = 0;
            for (int i = 0; i < p.length; i++){
                temp = temp + p[i] * p[i];
            }
            varX[factor] = temp;
            
            // Save iteration results
            for (int i = 0; i < t.length; i++){T[i][factor] = t[i];}
            for (int i = 0; i < p.length; i++){P[i][factor] = p[i];}
            for (int i = 0; i < u.length; i++){U[i][factor] = u[i];}
            for (int i = 0; i < c.length; i++){Q[i][factor] = c[i];}
            for (int i = 0; i < w.length; i++){W[i][factor] = w[i];}
            B[factor] = b;
          
        }
      
        // Calculate the coefficient vector
        double[][] temp = new double[B.length][B.length];
        for (int i = 0; i < B.length; i++){
            temp[i][i] = B[i];
        }
        double[][] mat = Matrix.PseudoInverse(Matrix.Transpose(P));
        mat = Matrix.Multiply(mat, temp);
        mat = Matrix.Multiply(mat, Matrix.Transpose(Q));
      
        coeff = mat;
    }
    
    private double Euclidean(double[] v){
        double r = 0;
        for (int i = 0; i < v.length; i++){
            r += Math.pow(v[i], 2);
        }
        return Math.sqrt(r);
    }
    
    private int largest(double[][] matrix){
        int index = 0;
        double max = 0;
        for (int i = 0; i < matrix[0].length; i++){
            double sum = 0;
            for (int j = 0; j < matrix.length; j++){
                sum += matrix[j][i] * matrix[j][i];
            }
            if (sum > max){
                max = sum;
                index = i;
            }
        }
        return index;
    }
}