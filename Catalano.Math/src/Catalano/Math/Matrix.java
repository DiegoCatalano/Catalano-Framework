// Catalano Math Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2014
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

package Catalano.Math;

import Catalano.Core.IntPoint;

/**
 * Defines a set of methods that works on multidimensional arrays and vectors.
 * @author Diego Catalano
 */
public final class Matrix {
    
    /**
     * Don't let anyone instantiate this class.
     */
    private Matrix (){};
    
    /**
     * Elementwise absolute value.
     * @param A Matrix.
     * @return Elementwise absolute matrix.
     */
    public static double[][] Abs(double[][] A){
        
        int rows = A.length;
        int cols = A[0].length;
        double[][] r = new double[rows][cols];
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                r[i][j] = Math.abs(A[i][j]);
            }
        }
        
        return r;
    }
    
    /**
     * Elementwise absolute value.
     * @param A Matrix.
     * @return Elementwise absolute matrix.
     */
    public static int[][] Abs(int[][] A){
        
        int rows = A.length;
        int cols = A[0].length;
        int[][] r = new int[rows][cols];
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                r[i][j] = Math.abs(A[i][j]);
            }
        }
        
        return r;
    }
    
    /**
     * Elementwise absolute value.
     * @param A Matrix.
     * @return Elementwise absolute matrix.
     */
    public static float[][] Abs(float[][] A){
        
        int rows = A.length;
        int cols = A[0].length;
        float[][] r = new float[rows][cols];
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                r[i][j] = Math.abs(A[i][j]);
            }
        }
        
        return A;
    }
    
    /**
     * Creates a index vector.
     * @param from From.
     * @param to To.
     * @return Matrix
     */
    public static int[] Indices(int from, int to){
        int[] vector = new int[to - from];
        for (int i = 0; i < vector.length; i++)
            vector[i] = from++;
        return vector;
    }
    
    /**
     * Elementwise Log operation.
     * @param A Matrix.
     * @return Log(Matrix).
     */
    public static double[][] Log(double[][] A){
        
        int rows = A.length;
        int cols = A[0].length;
        double[][] r = new double[rows][cols];
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                r[i][j] = Math.log(A[i][j]);
            }
        }
        
        return r;
    }
    
    /**
     * Adds two vectors.
     * @param A Vector.
     * @param B Vector.
     * @return The sum of the given vectors.
     */
    public static double[] Add(double[] A, double[] B){
        for (int i = 0; i < A.length; i++) {
            A[i] += B[i];
        }
        return B;
    }
    
    /**
     * Adds two vectors.
     * @param A Vector.
     * @param B Vector.
     * @return The sum of the given vectors.
     */
    public static int[] Add(int[] A, int[] B){
        for (int i = 0; i < A.length; i++) {
            A[i] += B[i];
        }
        return B;
    }
    
    /**
     * Adds two vectors.
     * @param A Vector.
     * @param B Vector.
     * @return The sum of the given vectors.
     */
    public static float[] Add(float[] A, float[] B){
        for (int i = 0; i < A.length; i++) {
            A[i] += B[i];
        }
        return B;
    }
    
    /**
     * Adds two matrices.
     * @param A Matrix.
     * @param B Matrix.
     * @return The sum of the given matrices.
     */
    public static double[][] Add(double[][] A, double[][] B){
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                A[i][j] += B[i][j];
            }
        }
        return A;
    }
    
    /**
     * Adds two matrices.
     * @param A Matrix.
     * @param B Matrix.
     * @return The sum of the given matrices.
     */
    public static int[][] Add(int[][] A, int[][] B){
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                A[i][j] += B[i][j];
            }
        }
        return A;
    }
    
    /**
     * Adds two matrices.
     * @param A Matrix.
     * @param B Matrix.
     * @return The sum of the given matrices.
     */
    public static float[][] Add(float[][] A, float[][] B){
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                A[i][j] += B[i][j];
            }
        }
        return A;
    }
    
    /**
     * Adds scalar value in a vector.
     * @param A Vector.
     * @param scalar Scalar value.
     */
    public static void Add(double[] A, double scalar){
        for (int i = 0; i < A.length; i++) {
            A[i] += scalar;
        }
    }
    
    /**
     * Adds scalar value in a vector.
     * @param A Vector.
     * @param scalar Scalar value.
     */
    public static void Add(int[] A, int scalar){
        for (int i = 0; i < A.length; i++) {
            A[i] += scalar;
        }
    }
    
    /**
     * Adds scalar value in a vector.
     * @param A Vector.
     * @param scalar Scalar value.
     */
    public static void Add(float[] A, float scalar){
        for (int i = 0; i < A.length; i++) {
            A[i] += scalar;
        }
    }
    
    /**
     * Adds scalar value in a matrix.
     * @param A Matrix.
     * @param scalar Scalar value.
     */
    public static void Add(double[][] A, double scalar){
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                A[i][j] += scalar;
            }
        }
    }
    
    /**
     * Adds scalar value in a matrix.
     * @param A Matrix.
     * @param scalar Scalar value.
     */
    public static void Add(int[][] A, int scalar){
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                A[i][j] += scalar;
            }
        }
    }
    
    /**
     * Adds scalar value in a matrix.
     * @param A Matrix.
     * @param scalar Scalar value.
     */
    public static void Add(float[][] A, float scalar){
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                A[i][j] += scalar;
            }
        }
    }
    
    /**
     * Set all elements in the vector equal 0.
     * @param A Vector.
     */
    public static void Clear(int[] A){
        for (int i = 0; i < A.length; i++) {
            A[i] = 0;
        }
    }
    
    /**
     * Set all elements in the vector equal 0.
     * @param A Vector.
     */
    public static void Clear(float[] A){
        for (int i = 0; i < A.length; i++) {
            A[i] = 0;
        }
    }
    
    /**
     * Set all elements in the vector equal 0.
     * @param A Vector.
     */
    public static void Clear(double[] A){
        for (int i = 0; i < A.length; i++) {
            A[i] = 0;
        }
    }
    
    /**
     * Set all elements in the vector equal 0.
     * @param A Matrix.
     */
    public static void Clear(int[][] A){
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                A[i][j] = 0;
            }
        }
    }
    
    /**
     * Set all elements in the vector equal 0.
     * @param A Matrix.
     */
    public static void Clear(float[][] A){
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                A[i][j] = 0;
            }
        }
    }
    
    /**
     * Set all elements in the vector equal 0.
     * @param A Matrix.
     */
    public static void Clear(double[][] A){
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                A[i][j] = 0;
            }
        }
    }
    
    /**
     * Divides a matrix with a scalar value.
     * @param A Matrix.
     * @param scalar Scalar value.
     */
    public static void Divide(double[][] A, double scalar){
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                A[i][j] /= scalar;
            }
        }
    }
    
    /**
     * Elementwise Exp operation.
     * @param A Matrix.
     * @return Exp(Matrix).
     */
    public static double[][] Exp(double[][] A){
        
        int rows = A.length;
        int cols = A[0].length;
        double[][] r = new double[rows][cols];
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                r[i][j] = Math.exp(A[i][j]);
            }
        }
        
        return r;
    }
    
    /**
     * Divides a matrix with a scalar value.
     * @param A Matrix.
     * @param scalar Scalar value.
     */
    public static void Divide(int[][] A, int scalar){
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                A[i][j] /= scalar;
            }
        }
    }
    
    /**
     * Divides a matrix with a scalar value.
     * @param A Matrix.
     * @param scalar Scalar value.
     */
    public static void Divide(float[][] A, float scalar){
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                A[i][j] /= scalar;
            }
        }
    }
    
    /**
     * Fill a vector with specified number.
     * @param A Vector.
     * @param number Number.
     */
    public static void Fill(double[] A, double number){
        for (int i = 0; i < A.length; i++) {
            A[i] = number;
        }
    }
    
    /**
     * Return a vector from a specified column in a matrix.
     * @param A Matrix.
     * @param n Number of column.
     * @return Vector.
     */
    public static double[] getColumn(double[][] A, int n){
        int rows = A.length;
        double[] v = new double[rows];
        
        for (int i = 0; i < rows; i++) {
            v[i] = A[i][n];
        }
        return v;
    }
    
    /**
     * Return a vector from a specified column in a matrix.
     * @param A Matrix.
     * @param n Number of column.
     * @return Vector.
     */
    public static int[] getColumn(int[][] A, int n){
        int rows = A.length;
        int[] v = new int[rows];
        
        for (int i = 0; i < rows; i++) {
            v[i] = A[i][n];
        }
        return v;
    }
    
    /**
     * Return a vector from a specified column in a matrix.
     * @param A Matrix.
     * @param n Number of column.
     * @return Vector.
     */
    public static float[] getColumn(float[][] A, int n){
        int rows = A.length;
        float[] v = new float[rows];
        
        for (int i = 0; i < rows; i++) {
            v[i] = A[i][n];
        }
        return v;
    }
    
    /**
     * Return a vector from a specified row in a matrix.
     * @param A Matrix.
     * @param n Number of row.
     * @return Vector.
     */
    public static double[] getRow(double[][] A, int n){
        int cols = A[0].length;
        double[] v = new double[cols];
        
        for (int i = 0; i < cols; i++) {
            v[i] = A[n][i];
        }
        return v;
    }
    
    /**
     * Return a vector from a specified row in a matrix.
     * @param A Matrix.
     * @param n Number of row.
     * @return Vector.
     */
    public static int[] getRow(int[][] A, int n){
        int cols = A[0].length;
        int[] v = new int[cols];
        
        for (int i = 0; i < cols; i++) {
            v[i] = A[n][i];
        }
        return v;
    }
    
    /**
     * Return a vector from a specified row in a matrix.
     * @param A Matrix.
     * @param n Number of row.
     * @return Vector.
     */
    public static float[] getRow(float[][] A, int n){
        int cols = A[0].length;
        float[] v = new float[cols];
        
        for (int i = 0; i < cols; i++) {
            v[i] = A[n][i];
        }
        return v;
    }
    
    /**
     * Fill a vector with specified number.
     * @param A Vector.
     * @param number Number.
     */
    public static void Fill(int[] A, int number){
        for (int i = 0; i < A.length; i++) {
            A[i] = number;
        }
    }
    
    /**
     * Fill a vector with specified number.
     * @param A Vector.
     * @param number Number.
     */
    public static void Fill(float[] A, float number){
        for (int i = 0; i < A.length; i++) {
            A[i] = number;
        }
    }
    
    /**
     * Fill a matrix with specified number.
     * @param A Vector.
     * @param number Number.
     */
    public static void Fill(double[][] A, double number){
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                A[i][j] = number;
            }
        }
    }
    
    /**
     * Fill a matrix with specified number.
     * @param A Vector.
     * @param number Number.
     */
    public static void Fill(int[][] A, int number){
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                A[i][j] = number;
            }
        }
    }
    
    /**
     * Fill a matrix with specified number.
     * @param A Vector.
     * @param number Number.
     */
    public static void Fill(float[][] A, float number){
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                A[i][j] = number;
            }
        }
    }
    
    /**
     * Subtracts two vectors.
     * @param A Vector.
     * @param B Vector.
     * @return The subtract of the given vectors.
     */
    public static double[] Subtract(double[] A, double[] B){
        for (int i = 0; i < A.length; i++) {
            A[i] -= B[i];
        }
        return B;
    }
    
    /**
     * Subtracts two vectors.
     * @param A Vector.
     * @param B Vector.
     * @return The subtract of the given vectors.
     */
    public static int[] Subtract(int[] A, int[] B){
        for (int i = 0; i < A.length; i++) {
            A[i] -= B[i];
        }
        return B;
    }
    
    /**
     * Subtracts two vectors.
     * @param A Vector.
     * @param B Vector.
     * @return The subtract of the given vectors.
     */
    public static float[] Subtract(float[] A, float[] B){
        for (int i = 0; i < A.length; i++) {
            A[i] -= B[i];
        }
        return B;
    }
    
    /**
     * Subtracts two matrices.
     * @param A Matrix.
     * @param B Matrix.
     * @return The subtract of the given matrices.
     */
    public static double[][] Subtract(double[][] A, double[][] B){
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                A[i][j] *= B[i][j];
            }
        }
        return A;
    }
    
    /**
     * Subtracts two matrices.
     * @param A Matrix.
     * @param B Matrix.
     * @return The subtract of the given matrices.
     */
    public static int[][] Subtract(int[][] A, int[][] B){
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                A[i][j] *= B[i][j];
            }
        }
        return A;
    }
    
    /**
     * Subtracts two matrices.
     * @param A Matrix.
     * @param B Matrix.
     * @return The subtract of the given matrices.
     */
    public static float[][] Subtract(float[][] A, float[][] B){
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                A[i][j] *= B[i][j];
            }
        }
        return A;
    }
    
    /**
     * Subtract vector with a scalar value.
     * @param A Vector.
     * @param scalar Scalar value.
     */
    public static void Subtract(double[] A, double scalar){
        for (int i = 0; i < A.length; i++) {
            A[i] -= scalar;
        }
    }
    
    /**
     * Subtract vector with a scalar value.
     * @param A Vector.
     * @param scalar Scalar value.
     */
    public static void Subtract(int[] A, int scalar){
        for (int i = 0; i < A.length; i++) {
            A[i] -= scalar;
        }
    }
    
    /**
     * Subtract vector with a scalar value.
     * @param A Vector.
     * @param scalar Scalar value.
     */
    public static void Subtract(float[] A, float scalar){
        for (int i = 0; i < A.length; i++) {
            A[i] -= scalar;
        }
    }
    
    /**
     * Subtract matrix with a scalar value.
     * @param A Vector.
     * @param scalar Scalar value.
     */
    public static void Subtract(double[][] A, double scalar){
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                A[i][j] -= scalar;
            }
        }
    }
    
    /**
     * Subtract matrix with a scalar value.
     * @param A Vector.
     * @param scalar Scalar value.
     */
    public static void Subtract(int[][] A, int scalar){
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                A[i][j] -= scalar;
            }
        }
    }
    
    /**
     * Subtract matrix with a scalar value.
     * @param A Vector.
     * @param scalar Scalar value.
     */
    public static void Subtract(float[][] A, float scalar){
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                A[i][j] -= scalar;
            }
        }
    }
    
    /**
     * Multiply two matrices.
     * @param A Matrix.
     * @param B Matrix.
     * @return The multiply of the given matrices.
     */
    public static double[][] Multiply(double[][] A, double[][] B){
        
        double[][] C = new double[A.length][B[0].length];
        
        if (A[0].length == B.length) {
            for (int i = 0; i < A.length; i++) {
                double Aik = A[i][0];
                for (int j = 0; j < A[0].length; j++)
                    C[i][j] = Aik * B[0][j];
                for (int k = 1; k < B.length; k++) {
                    Aik = A[i][k];
                    for (int j = 0; j < B[0].length; j++) {
                        C[i][j] += Aik * B[k][j];
                    }
                }
            }
            return C;
            
        }
        else{
            try {
                throw new IllegalArgumentException("Illegal size of matrix");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return C;
    }
    
    /**
     * Multiply two matrices.
     * @param A Matrix.
     * @param B Matrix.
     * @return The multiply of the given matrices.
     */
    public static int[][] Multiply(int[][] A, int[][] B){
        
        int[][] C = new int[A.length][B[0].length];
        
        if (A[0].length == B.length) {
            for (int i = 0; i < A.length; i++) {
                int Aik = A[i][0];
                for (int j = 0; j < A[0].length; j++)
                    C[i][j] = Aik * B[0][j];
                for (int k = 1; k < B.length; k++) {
                    Aik = A[i][k];
                    for (int j = 0; j < B[0].length; j++) {
                        C[i][j] += Aik * B[k][j];
                    }
                }
            }
            return C;
            
        }
        else{
            try {
                throw new IllegalArgumentException("Illegal size of matrix");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return C;
    }
    
    /**
     * Multiply two matrices.
     * @param A Matrix.
     * @param B Matrix.
     * @return The multiply of the given matrices.
     */
    public static float[][] Multiply(float[][] A, float[][] B){
        
        float[][] C = new float[A.length][B[0].length];
        
        if (A[0].length == B.length) {
            for (int i = 0; i < A.length; i++) {
                float Aik = A[i][0];
                for (int j = 0; j < A[0].length; j++)
                    C[i][j] = Aik * B[0][j];
                for (int k = 1; k < B.length; k++) {
                    Aik = A[i][k];
                    for (int j = 0; j < B[0].length; j++) {
                        C[i][j] += Aik * B[k][j];
                    }
                }
            }
            return C;
            
        }
        else{
            try {
                throw new IllegalArgumentException("Illegal size of matrix");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return C;
    }
    
    /**
     * Multiply a Matrix with scalar value.
     * @param A Matrix.
     * @param value Value.
     * @return The multiply of the matrix with scalar value.
     */
    public static double[][] Multiply(double[][] A, double value){
        
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                A[i][j] *= value;
            }
        }
        return A;
        
    }
    
    /**
     * Multiply a Matrix with scalar value.
     * @param A Matrix.
     * @param value Value.
     * @return The multiply of the matrix with scalar value.
     */
    public static float[][] Multiply(float[][] A, float value){
        
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                A[i][j] *= value;
            }
        }
        return A;
        
    }
    
    /**
     * Multiply a Matrix with scalar value.
     * @param A Matrix.
     * @param value Value.
     * @return The multiply of the matrix with scalar value.
     */
    public static int[][] Multiply(int[][] A, int value){
        
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                A[i][j] *= value;
            }
        }
        return A;
        
    }
    
    /**
     * Gets the transpose of the matrix.
     * @param A Matrix.
     * @return Transposed matrix.
     */
    public static double[][] Transpose(double[][] A){
        double[][] t = new double[A[0].length][A.length];
        
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                t[j][i] = A[i][j];
            }
        }
        return t;
    }
    
    /**
     * Gets the transpose of the matrix.
     * @param A Matrix.
     * @return Transposed matrix.
     */
    public static int[][] Transpose(int[][] A){
        int[][] t = new int[A[0].length][A.length];
        
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                t[j][i] = A[i][j];
            }
        }
        return t;
    }
    
    /**
     * Gets the transpose of the matrix.
     * @param A Matrix.
     * @return Transposed matrix.
     */
    public static float[][] Transpose(float[][] A){
        float[][] t = new float[A[0].length][A.length];
        
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                t[j][i] = A[i][j];
            }
        }
        return t;
    }
    
    /**
     * Gets the Identity matrix of the given size.
     * @param order Size.
     * @return Identity matrix.
     */
    public static double[][] Identity(int order){
        order = Math.max(order, 2);
        
        double[][] matrix = new double[order][order];
        
        for (int i = 0; i < order; i++) {
            for (int j = 0; j < order; j++) {
                matrix[i][j] = 0;
            }
        }
        
        for (int i = 0; i < order; i++) matrix[i][i] = 1;
        return matrix;
    }
    
    /**
     * Get the maximum value from array.
     * @param matrix Array.
     * @return Maximum value.
     */
    public static double Max(double[] matrix){
        double max = Integer.MIN_VALUE;
        for (int i = 0; i < matrix.length; i++) {
            max = Math.max(max, matrix[i]);
        }
        return max;
    }
    
    /**
     * Get the maximum value from array.
     * @param matrix Array.
     * @return Maximum value.
     */
    public static double Max(double[][] matrix){
        double max = Integer.MIN_VALUE;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                max = Math.max(max, matrix[i][j]);
            }
        }
        return max;
    }
    
    /**
     * Get the maximum value from array.
     * @param matrix Array.
     * @return Maximum value.
     */
    public static int Max(int[] matrix){
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < matrix.length; i++) {
            max = Math.max(max, matrix[i]);
        }
        return max;
    }
    
    /**
     * Get the maximum value from array.
     * @param matrix Array.
     * @return Maximum value.
     */
    public static int Max(int[][] matrix){
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                max = Math.max(max, matrix[i][j]);
            }
        }
        return max;
    }
    
    /**
     * Get the maximum value from array.
     * @param matrix Array.
     * @return Maximum value.
     */
    public static float Max(float[] matrix){
        float max = Integer.MIN_VALUE;
        for (int i = 0; i < matrix.length; i++) {
            max = Math.max(max, matrix[i]);
        }
        return max;
    }
    
    /**
     * Get the maximum value from array.
     * @param matrix Array.
     * @return Maximum value.
     */
    public static float Max(float[][] matrix){
        float max = Integer.MIN_VALUE;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                max = Math.max(max, matrix[i][j]);
            }
        }
        return max;
    }
    
    /**
     * Get the index of maximum value from array.
     * @param matrix Array.
     * @return Index.
     */
    public static int MaxIndex(double[] matrix){
        int index = 0;
        double max = Double.MIN_VALUE;
        for (int i = 0; i < matrix.length; i++) {
            double currentValue = Math.max(max, matrix[i]);
            if (currentValue > max){
                max = currentValue;
                index = i;
            }
        }
        return index;
    }
    
    /**
     * Get the index of maximum value from array.
     * @param matrix Array.
     * @return Index.
     */
    public static IntPoint MaxIndex(double[][] matrix){
        IntPoint index = new IntPoint();
        double max = Double.MIN_VALUE;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                double currentValue = Math.max(max, matrix[i][j]);
                if (currentValue > max){
                    max = currentValue;
                    index.setXY(i, j);
                }
            }
        }
        return index;
    }
    
    /**
     * Get the index of maximum value from array.
     * @param matrix Array.
     * @return Index.
     */
    public static int MaxIndex(int[] matrix){
        int index = 0;
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < matrix.length; i++) {
            int currentValue = Math.max(max, matrix[i]);
            if (currentValue > max){
                max = currentValue;
                index = i;
            }
        }
        return index;
    }
    
    /**
     * Get the index of maximum value from array.
     * @param matrix Array.
     * @return Index.
     */
    public static IntPoint MaxIndex(int[][] matrix){
        IntPoint index = new IntPoint();
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                int currentValue = Math.max(max, matrix[i][j]);
                if (currentValue > max){
                    max = currentValue;
                    index.setXY(i, j);
                }
            }
        }
        return index;
    }
    
    /**
     * Get the index of maximum value from array.
     * @param matrix Array.
     * @return Index.
     */
    public static int MaxIndex(float[] matrix){
        int index = 0;
        float max = Float.MIN_VALUE;
        for (int i = 0; i < matrix.length; i++) {
            float currentValue = Math.max(max, matrix[i]);
            if (currentValue > max){
                max = currentValue;
                index = i;
            }
        }
        return index;
    }
    
    /**
     * Get the index of maximum value from array.
     * @param matrix Array.
     * @return Index.
     */
    public static IntPoint MaxIndex(float[][] matrix){
        IntPoint index = new IntPoint();
        float max = Float.MIN_VALUE;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                float currentValue = Math.max(max, matrix[i][j]);
                if (currentValue > max){
                    max = currentValue;
                    index.setXY(i, j);
                }
            }
        }
        return index;
    }
    
    public static int[][] MemberwiseClone(int[][] data){
        int[][] clone = new int[data.length][];
        for (int i = 0; i < data.length; i++)
            clone[i] = (int[])data[i].clone();
        return clone;
    }
    
    public static float[][] MemberwiseClone(float[][] data){
        float[][] clone = new float[data.length][];
        for (int i = 0; i < data.length; i++)
            clone[i] = (float[])data[i].clone();
        return clone;
    }
    
    public static double[][] MemberwiseClone(double[][] data){
        double[][] clone = new double[data.length][];
        for (int i = 0; i < data.length; i++)
            clone[i] = (double[])data[i].clone();
        return clone;
    }
    
    /**
     * Get the minimum value from array.
     * @param matrix Array.
     * @return Minimum value.
     */
    public static double Min(double[] matrix){
        double min = Integer.MAX_VALUE;
        for (int i = 0; i < matrix.length; i++) {
            min = Math.min(min, matrix[i]);
        }
        return min;
    }
    
    /**
     * Get the minimum value from array.
     * @param matrix Array.
     * @return Minimum value.
     */
    public static double Min(double[][] matrix){
        double min = Integer.MAX_VALUE;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                min = Math.min(min, matrix[i][j]);
            }
        }
        return min;
    }
    
    /**
     * Get the minimum value from array.
     * @param matrix Array.
     * @return Minimum value.
     */
    public static double Min(int[] matrix){
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < matrix.length; i++) {
            min = Math.min(min, matrix[i]);
        }
        return min;
    }
    
    /**
     * Get the minimum value from array.
     * @param matrix Array.
     * @return Minimum value.
     */
    public static int Min(int[][] matrix){
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                min = Math.min(min, matrix[i][j]);
            }
        }
        return min;
    }
    
    /**
     * Get the minimum value from array.
     * @param matrix Array.
     * @return Minimum value.
     */
    public static double Min(float[] matrix){
        float min = Integer.MAX_VALUE;
        for (int i = 0; i < matrix.length; i++) {
            min = Math.min(min, matrix[i]);
        }
        return min;
    }
    
    /**
     * Get the minimum value from array.
     * @param matrix Array.
     * @return Minimum value.
     */
    public static float Min(float[][] matrix){
        float min = Integer.MAX_VALUE;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                min = Math.min(min, matrix[i][j]);
            }
        }
        return min;
    }
    
    /**
     * Get the index of minimum value from array.
     * @param matrix Matrix.
     * @return Index.
     */
    public static int MinIndex(double[] matrix){
        int index = 0;
        double min = Double.MAX_VALUE;
        for (int i = 0; i < matrix.length; i++) {
            double currentValue = Math.min(min, matrix[i]);
            if (currentValue < min){
                min = currentValue;
                index = i;
            }
        }
        return index;
    }
    
    /**
     * Get the index of minimum value from array.
     * @param matrix Matrix.
     * @return Index.
     */
    public static IntPoint MinIndex(double[][] matrix){
        IntPoint index = new IntPoint();
        double min = Double.MAX_VALUE;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                double currentValue = Math.min(min, matrix[i][j]);
                if (currentValue < min){
                    min = currentValue;
                    index.setXY(i, j);
                }
            }
        }
        return index;
    }
    
    /**
     * Get the index of minimum value from array.
     * @param matrix Matrix.
     * @return Index.
     */
    public static int MinIndex(int[] matrix){
        int index = 0;
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < matrix.length; i++) {
            int currentValue = Math.min(min, matrix[i]);
            if (currentValue < min){
                min = currentValue;
                index = i;
            }
        }
        return index;
    }
    
    /**
     * Get the index of minimum value from array.
     * @param matrix Matrix.
     * @return Index.
     */
    public static IntPoint MinIndex(int[][] matrix){
        IntPoint index = new IntPoint();
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                int currentValue = Math.min(min, matrix[i][j]);
                if (currentValue < min){
                    min = currentValue;
                    index.setXY(i, j);
                }
            }
        }
        return index;
    }
    
    /**
     * Get the index of minimum value from array.
     * @param matrix Matrix.
     * @return Index.
     */
    public static int MinIndex(float[] matrix){
        int index = 0;
        float min = Float.MAX_VALUE;
        for (int i = 0; i < matrix.length; i++) {
            float currentValue = Math.min(min, matrix[i]);
            if (currentValue < min){
                min = currentValue;
                index = i;
            }
        }
        return index;
    }
    
    /**
     * Get the index of minimum value from array.
     * @param matrix Matrix.
     * @return Index.
     */
    public static IntPoint MinIndex(float[][] matrix){
        IntPoint index = new IntPoint();
        float min = Float.MAX_VALUE;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                float currentValue = Math.min(min, matrix[i][j]);
                if (currentValue < min){
                    min = currentValue;
                    index.setXY(i, j);
                }
            }
        }
        return index;
    }
    
    /**
     * Check if the matrix is square.
     * @param A Matrix.
     * @return Returns true if the matrix is square, otherwise returns false.
     */
    public static boolean isSquare(double[][] A){
        if ((A.length * A.length) == (A.length * A[0].length)) {
            return true;
        }
        return false;
    }
    
    /**
     * Check if the matrix is square.
     * @param A Matrix.
     * @return Returns true if the matrix is square, otherwise returns false.
     */
    public static boolean isSquare(int[][] A){
        if ((A.length * A.length) == (A.length * A[0].length)) {
            return true;
        }
        return false;
    }
    
    /**
     * Check if the matrix is square.
     * @param A Matrix.
     * @return Returns true if the matrix is square, otherwise returns false.
     */
    public static boolean isSquare(float[][] A){
        if ((A.length * A.length) == (A.length * A[0].length)) {
            return true;
        }
        return false;
    }
    
    /**
     * Check if the vector contains only 0 values.
     * @param A Vector.
     * @return True if the all elements is 0.
     */
    public static boolean isZero(int[] A){
        for (int i = 0; i < A.length; i++)
            if (A[i] != 0) return false;
        return true;
    }
    
    /**
     * Check if the vector contains only 0 values.
     * @param A Vector.
     * @return True if the all elements is 0.
     */
    public static boolean isZero(float[] A){
        for (int i = 0; i < A.length; i++)
            if (A[i] != 0) return false;
        return true;
    }
    
    /**
     * Check if the vector contains only 0 values.
     * @param A Vector.
     * @return True if the all elements is 0.
     */
    public static boolean isZero(double[] A){
        for (int i = 0; i < A.length; i++)
            if (A[i] != 0) return false;
        return true;
    }
    
    /**
     * Check if the matrix contains only 0 values.
     * @param A Matrix.
     * @return True if the all elements is 0.
     */
    public static boolean isZero(int[][] A){
        for (int i = 0; i < A.length; i++){
            for (int j = 0; j < A[0].length; j++) {
                if(A[i][j] != 0) return false;
            }
        }
        return true;
    }
    
    /**
     * Check if the matrix contains only 0 values.
     * @param A Matrix.
     * @return True if the all elements is 0.
     */
    public static boolean isZero(float[][] A){
        for (int i = 0; i < A.length; i++){
            for (int j = 0; j < A[0].length; j++) {
                if(A[i][j] != 0) return false;
            }
        }
        return true;
    }
    
    /**
     * Check if the matrix contains only 0 values.
     * @param A Matrix.
     * @return True if the all elements is 0.
     */
    public static boolean isZero(double[][] A){
        for (int i = 0; i < A.length; i++){
            for (int j = 0; j < A[0].length; j++) {
                if(A[i][j] != 0) return false;
            }
        }
        return true;
    }
    
    public static int[] SubMatrix(int[] data, int first){
        if (first < 0 || first > data.length)
            throw new IllegalArgumentException("first");

        if (first == 0)
            return data;

        return Submatrix(data, 0, first - 1);
    }
    
    public static double[] SubMatrix(double[] data, int first){
        if (first < 0 || first > data.length)
            throw new IllegalArgumentException("first");

        if (first == 0)
            return data;

        return Submatrix(data, 0, first - 1);
    }
    
    public static int[] Submatrix(int[] data, int startRow, int endRow){
        if (startRow < 0)
            throw new IllegalArgumentException("startRow");
        if (endRow >= data.length)
            throw new IllegalArgumentException("endRow");

        int[] X = new int[endRow - startRow + 1];

        for (int i = startRow; i <= endRow; i++)
            X[i - startRow] = data[i];

        return X;
    }
    
    public static double[] Submatrix(double[] data, int startRow, int endRow){
        if (startRow < 0)
            throw new IllegalArgumentException("startRow");
        if (endRow >= data.length)
            throw new IllegalArgumentException("endRow");

        double[] X = new double[endRow - startRow + 1];

        for (int i = startRow; i <= endRow; i++)
            X[i - startRow] = data[i];

        return X;
    }
    
    public static int[][] Submatrix(int[][] data, int startRow, int endRow, int[] columnIndexes){
        if ((startRow > endRow) || (startRow < 0) || (startRow >= data.length)
            || (endRow < 0) || (endRow >= data.length)){
            throw new IllegalArgumentException("Argument out of range.");
        }

        if (columnIndexes == null)
            columnIndexes = Indices(0, data[0].length);

        int[][] X = new int[endRow - startRow + 1][columnIndexes.length];

        for (int i = startRow; i <= endRow; i++){
            for (int j = 0; j < columnIndexes.length; j++){
                if ((columnIndexes[j] < 0) || (columnIndexes[j] >= data[0].length)){
                    throw new IllegalArgumentException("Argument out of range.");
                }

                X[i - startRow][j] = data[i][columnIndexes[j]];
            }
        }

        return X;
    }
    
    public static double[][] Submatrix(double[][] data, int startRow, int endRow, int[] columnIndexes){
        if ((startRow > endRow) || (startRow < 0) || (startRow >= data.length)
            || (endRow < 0) || (endRow >= data.length)){
            throw new IllegalArgumentException("Argument out of range.");
        }

        if (columnIndexes == null)
            columnIndexes = Indices(0, data[0].length);

        double[][] X = new double[endRow - startRow + 1][columnIndexes.length];

        for (int i = startRow; i <= endRow; i++){
            for (int j = 0; j < columnIndexes.length; j++){
                if ((columnIndexes[j] < 0) || (columnIndexes[j] >= data[0].length)){
                    throw new IllegalArgumentException("Argument out of range.");
                }

                X[i - startRow][j] = data[i][columnIndexes[j]];
            }
        }

        return X;
    }
    
    public static int[][] Submatrix(int[][] data, int[] rowIndexes){
        int[][] X = new int[rowIndexes.length][data[0].length];

        for (int i = 0; i < rowIndexes.length; i++)
        {
            for (int j = 0; j < data[0].length; j++)
            {
                if ((rowIndexes[i] < 0) || (rowIndexes[i] >= data.length))
                    throw new IllegalArgumentException("Argument out of range.");

                X[i][j] = data[rowIndexes[i]][j];
            }
        }

        return X;
    }
    
    public static double[][] Submatrix(double[][] data, int[] rowIndexes){
        double[][] X = new double[rowIndexes.length][data[0].length];

        for (int i = 0; i < rowIndexes.length; i++)
        {
            for (int j = 0; j < data[0].length; j++)
            {
                if ((rowIndexes[i] < 0) || (rowIndexes[i] >= data.length))
                    throw new IllegalArgumentException("Argument out of range.");

                X[i][j] = data[rowIndexes[i]][j];
            }
        }

        return X;
    }
    
    /**
     * Convert Matrix to Vector.
     * @param A Matrix.
     * @return Vector.
     */
    public static double[] toDoubleArray(int[][] A){
        double[] m = new double[A.length * A[0].length];
        
        int index = 0;
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                m[index] = A[i][j];
                index++;
            }
        }
        return m;
    }
    
    /**
     * Convert Matrix to Vector.
     * @param A Matrix.
     * @return Vector.
     */
    public static double[] toDoubleArray(float[][] A){
        double[] m = new double[A.length * A[0].length];
        
        int index = 0;
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                m[index] = A[i][j];
                index++;
            }
        }
        return m;
    }
    
    /**
     * Convert Matrix to Vector.
     * @param A Matrix.
     * @return Vector.
     */
    public static int[] toIntArray(double[][] A){
        int[] m = new int[A.length * A[0].length];
        
        int index = 0;
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                m[index] = (int)A[i][j];
                index++;
            }
        }
        return m;
    }
    
    /**
     * Convert Matrix to Vector.
     * @param A Matrix.
     * @return Vector.
     */
    public static int[] toIntArray(float[][] A){
        int[] m = new int[A.length * A[0].length];
        
        int index = 0;
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                m[index] = (int)A[i][j];
                index++;
            }
        }
        return m;
    }
    
    /**
     * Convert Matrix to Vector.
     * @param A Matrix.
     * @return Vector.
     */
    public static float[] toFloatArray(double[][] A){
        float[] m = new float[A.length * A[0].length];
        
        int index = 0;
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                m[index] = (float)A[i][j];
                index++;
            }
        }
        return m;
    }
    
    /**
     * Convert Matrix to Vector.
     * @param A Matrix.
     * @return Vector.
     */
    public static float[] toFloatArray(int[][] A){
        float[] m = new float[A.length * A[0].length];
        
        int index = 0;
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                m[index] = (float)A[i][j];
                index++;
            }
        }
        return m;
    }
}