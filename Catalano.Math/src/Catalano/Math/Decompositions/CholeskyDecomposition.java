// Catalano Math Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
// Original work copyright © JAMA: A Java Matrix Package
// http://math.nist.gov/javanumerics/jama/
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

package Catalano.Math.Decompositions;

import Catalano.Math.Matrix;

   /** Cholesky Decomposition.
   <P>
   For a symmetric, positive definite matrix A, the Cholesky decomposition
   is an lower triangular matrix L so that A = L*L'.
   <P>
   If the matrix is not symmetric or positive definite, the constructor
   returns a partial decomposition and sets an internal flag that may
   be queried by the isSPD() method.
   */

public class CholeskyDecomposition implements java.io.Serializable {

   /** Array for internal storage of decomposition.
   @serial internal array storage.
   */
   private double[][] L;

   /** Row and column dimension (square matrix).
   @serial matrix dimension.
   */
   private int n;

   /** Symmetric and positive definite flag.
   @serial is symmetric and positive definite flag.
   */
   private boolean isspd;

   /**
    * Initializes a new instance of the CholeskyDecomposition class.
    * Cholesky algorithm for symmetric and positive definite matrix.
    * @param matrix Square, symmetric matrix.
    */
   public CholeskyDecomposition (double[][] matrix) {
       
     // Initialize.
      double[][] A = matrix;
      n = matrix.length;
      L = new double[n][n];
      isspd = (matrix[0].length == n);
      // Main loop.
      for (int j = 0; j < n; j++) {
         double[] Lrowj = L[j];
         double d = 0.0;
         for (int k = 0; k < j; k++) {
            double[] Lrowk = L[k];
            double s = 0.0;
            for (int i = 0; i < k; i++) {
               s += Lrowk[i]*Lrowj[i];
            }
            Lrowj[k] = s = (A[j][k] - s)/L[k][k];
            d = d + s*s;
            isspd = isspd & (A[k][j] == A[j][k]); 
         }
         d = A[j][j] - d;
         isspd = isspd & (d > 0.0);
         L[j][j] = Math.sqrt(Math.max(d,0.0));
         for (int k = j+1; k < n; k++) {
            L[j][k] = 0.0;
         }
      }
   }

   /** Is the matrix symmetric and positive definite?
   @return     true if A is symmetric and positive definite.
   */
   public boolean isSPD () {
      return isspd;
   }

   /** Return triangular factor.
   @return     L
   */
   public double[][] getL () {
      return Matrix.SubMatrix(L, n, n);
   }

   /** Solve A*X = B
   @param  B   A Matrix with as many rows as A and any number of columns.
   @return     X so that L*L'*X = B
   @exception  IllegalArgumentException  Matrix row dimensions must agree.
   @exception  RuntimeException  Matrix is not symmetric positive definite.
   */
   public double[][] solve (double[][] B) {
       
        if (B.length != n) {
           throw new IllegalArgumentException("Matrix row dimensions must agree.");
        }
        if (!isspd) {
           throw new RuntimeException("Matrix is not symmetric positive definite.");
        }

        // Copy right hand side.
        double[][] X = Matrix.Copy(B);
        int nx = B[0].length;

        // Solve L*Y = B;
        for (int k = 0; k < n; k++) {
          for (int j = 0; j < nx; j++) {
             for (int i = 0; i < k ; i++) {
                 X[k][j] -= X[i][j]*L[k][i];
             }
             X[k][j] /= L[k][k];
          }
        }

        // Solve L'*X = Y;
        for (int k = n-1; k >= 0; k--) {
          for (int j = 0; j < nx; j++) {
             for (int i = k+1; i < n ; i++) {
                 X[k][j] -= X[i][j]*L[i][k];
             }
             X[k][j] /= L[k][k];
          }
        }
        
        return Matrix.SubMatrix(X, n, nx);
   }
}