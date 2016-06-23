// Catalano Math Library
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

package Catalano.Math;

import Catalano.Core.ArraysUtil;
import Catalano.Core.IntPoint;
import Catalano.Math.Decompositions.LUDecomposition;
import Catalano.Math.Decompositions.QRDecomposition;
import Catalano.Math.Decompositions.SingularValueDecomposition;
import java.lang.reflect.Array;
import java.util.List;

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
        
        return r;
    }
    
    /**
     * Create 1D Matrix.
     * @param size Size of the matrix.
     * @param val Initial value of all elements.
     * @return Matrix.
     */
    public static double[] CreateMatrix1D(int size, double val){
        
        double[] v = new double[size];
        
        for (int i = 0; i < size; i++) {
            v[i] = val;
        }
        
        return v;
    }
    
    /**
     * Create 1D Matrix.
     * @param size Size of the matrix.
     * @param val Initial value of all elements.
     * @return Matrix.
     */
    public static int[] CreateMatrix1D(int size, int val){
        
        int[] v = new int[size];
        
        for (int i = 0; i < size; i++) {
            v[i] = val;
        }
        
        return v;
    }
    
    /**
     * Create 1D Matrix.
     * @param size Size of the matrix.
     * @param val Initial value of all elements.
     * @return Matrix.
     */
    public static float[] CreateMatrix1D(int size, float val){
        
        float[] v = new float[size];
        
        for (int i = 0; i < size; i++) {
            v[i] = val;
        }
        
        return v;
    }
    
    /**
     * Create 2D Matrix.
     * @param height Height of the matrix.
     * @param width Width of the matrix.
     * @param val Initial value of all elements.
     * @return Matrix.
     */
    public static double[][] CreateMatrix2D(int height, int width, double val){
        
        double[][] v = new double[height][width];
        
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                v[i][j] = val;
            }
        }
        
        return v;
        
    }
    
    /**
     * Create 2D Matrix.
     * @param height Height of the matrix.
     * @param width Width of the matrix.
     * @param val Initial value of all elements.
     * @return Matrix.
     */
    public static int[][] CreateMatrix2D(int height, int width, int val){
        
        int[][] v = new int[height][width];
        
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                v[i][j] = val;
            }
        }
        
        return v;
        
    }
    
    /**
     * Create 2D Matrix.
     * @param height Height of the matrix.
     * @param width Width of the matrix.
     * @param val Initial value of all elements.
     * @return Matrix.
     */
    public static float[][] CreateMatrix2D(int height, int width, float val){
        
        float[][] v = new float[height][width];
        
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                v[i][j] = val;
            }
        }
        
        return v;
        
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
     * Inner product (dot or scalar) between two vectors A'*B.
     * @param A Vector A.
     * @param B Vector B.
     * @return Dot product between A and B.
     */
    public static double InnerProduct(double[] A, double[] B){
        double sum = 0;
        for (int i = 0; i < A.length; i++) {
            sum += A[i] * B[i];
        }
        return sum;
    }
    
    /**
     * Inner product (dot or scalar) between two vectors.
     * @param A Vector A.
     * @param B Vector B.
     * @return Dot product between A and B.
     */
    public static int InnerProduct(int[] A, int[] B){
        int sum = 0;
        for (int i = 0; i < A.length; i++) {
            sum += A[i] * B[i];
        }
        return sum;
    }
    
    /**
     * Inner product (dot or scalar) between two vectors.
     * @param A Vector A.
     * @param B Vector B.
     * @return Dot product between A and B.
     */
    public static float InnerProduct(float[] A, float[] B){
        float sum = 0;
        for (int i = 0; i < A.length; i++) {
            sum += A[i] * B[i];
        }
        return sum;
    }
    
    /**
     * Elementwise Log operation.
     * @param A Vector.
     * @return Log(Vector).
     */
    public static double[] Log(double[] A){
        int size = A.length;
        double[] r = new double[size];
        for (int i = 0; i < size; i++) {
            r[i] = Math.log(A[i]);
        }
        return r;
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
     * Create a new copy.
     * @param A Matrix to be copied.
     * @return New matrix.
     */
    public static double[][] Copy(double[][] A){
        double[][] m = new double[A.length][A[0].length];
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                m[i][j] = A[i][j];
            }
        }
        return m;
    }
    
    /**
     * Create a new copy.
     * @param A Matrix to be copied.
     * @return New matrix.
     */
    public static int[][] Copy(int[][] A){
        int[][] m = new int[A.length][A[0].length];
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                m[i][j] = A[i][j];
            }
        }
        return m;
    }
    
    /**
     * Create a new copy.
     * @param A Matrix to be copied.
     * @return New matrix.
     */
    public static float[][] Copy(float[][] A){
        float[][] m = new float[A.length][A[0].length];
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                m[i][j] = A[i][j];
            }
        }
        return m;
    }
    
    /**
     * Determinant of matrix.
     * @param A Matrix.
     * @return Determinant of matrix.
     */
    public static double Determinant(double[][] A){
        return new LUDecomposition(A).determinant();
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
     * Dot product.
     * @param A Matrix.
     * @param B Matrix.
     */
    public static double[][] DotProduct(double[][] A, double[][] B){
        double[][] result = new double[A.length][A[0].length];
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                result[i][j] = A[i][j] * B[i][j];
            }
        }
        return result;
    }
    
    /**
     * Dot product.
     * @param A Matrix.
     * @param B Matrix.
     */
    public static int[][] DotProduct(int[][] A, int[][] B){
        int[][] result = new int[A.length][A[0].length];
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                result[i][j] = A[i][j] * B[i][j];
            }
        }
        return result;
    }
    
    /**
     * Dot product.
     * @param A Matrix.
     * @param B Matrix.
     */
    public static float[][] DotProduct(float[][] A, float[][] B){
        float[][] result = new float[A.length][A[0].length];
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                result[i][j] = A[i][j] * B[i][j];
            }
        }
        return result;
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
     * @param n Index of column.
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
     * Return a vector from a specified column in a matrix.
     * @param <T> Type.
     * @param A Matrix.
     * @param n Index of column.
     * @return Vector.
     */
    public static <T> T[] getColumn(T[][] A, int n){
        int rows = A.length;
        T[] v = (T[])Array.newInstance(A[0][n].getClass(), rows);
        
        for (int i = 0; i < rows; i++) {
            v[i] = A[i][n];
        }
        
        return v;
    }
    
    public static double[] getColumns(double[] A, int[] indexes){
        double[] v = new double[indexes.length];
        for (int i = 0; i < v.length; i++) {
            v[i] = A[indexes[i]];
        }
        return v;
    }
    
    public static int[] getColumns(int[] A, int[] indexes){
        int[] v = new int[indexes.length];
        for (int i = 0; i < v.length; i++) {
            v[i] = A[indexes[i]];
        }
        return v;
    }
    
    public static float[] getColumns(float[] A, int[] indexes){
        float[] v = new float[indexes.length];
        for (int i = 0; i < v.length; i++) {
            v[i] = A[indexes[i]];
        }
        return v;
    }
    
    public static <T> T[] getColumns(T[] A, int[] indexes){
        T[] v = (T[])Array.newInstance(A[0].getClass(), indexes.length);
        for (int i = 0; i < v.length; i++) {
            v[i] = A[indexes[i]];
        }
        return v;
    }
    
    /**
     * Return a vector from specified range.
     * @param A Vector.
     * @param startIndex Start index.
     * @param endIndex End index.
     * @return Vector.
     */
    public static double[] getColumns(double[] A, int startIndex, int endIndex){
        
        double[] v = new double[endIndex - startIndex + 1];
        for (int i = 0; i < v.length; i++) {
            v[i] = A[startIndex + i];
        }
        return v;
        
    }
    
    /**
     * Return a vector from specified range.
     * @param A Vector.
     * @param startIndex Start index.
     * @param endIndex End index.
     * @return Vector.
     */
    public static int[] getColumns(int[] A, int startIndex, int endIndex){
        
        int[] v = new int[endIndex - startIndex + 1];
        for (int i = 0; i < v.length; i++) {
            v[i] = A[startIndex + i];
        }
        return v;
        
    }
    
    /**
     * Return a vector from specified range.
     * @param A Vector.
     * @param startIndex Start index.
     * @param endIndex End index.
     * @return Vector.
     */
    public static float[] getColumns(float[] A, int startIndex, int endIndex){
        
        float[] v = new float[endIndex - startIndex + 1];
        for (int i = 0; i < v.length; i++) {
            v[i] = A[startIndex + i];
        }
        return v;
        
    }
    
    /**
     * Return a vector from specified range.
     * @param A Vector.
     * @param startIndex Start index.
     * @param endIndex End index.
     * @return Vector.
     */
    public static <T> T[] getColumns(T[] A, int startIndex, int endIndex){
        
        T[] v = (T[])Array.newInstance(A[0].getClass(), A.length);
        for (int i = 0; i < v.length; i++) {
            v[i] = A[startIndex + i];
        }
        return v;
        
    }
    
    /**
     * Return a matrix from specified columns.
     * @param A Matrix.
     * @param indexes Indexes.
     * @return Matrix.
     */
    public static double[][] getColumns(double[][] A, int[] indexes){
        double[][] m = new double[A.length][indexes.length];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                m[i][j] = A[i][indexes[j]];
            }
        }
        return m;
    }
    
    /**
     * Return a matrix from specified columns.
     * @param A Matrix.
     * @param indexes Indexes.
     * @return Matrix.
     */
    public static int[][] getColumns(int[][] A, int[] indexes){
        int[][] m = new int[A.length][indexes.length];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                m[i][j] = A[i][indexes[j]];
            }
        }
        return m;
    }
    
    /**
     * Return a matrix from specified columns.
     * @param A Matrix.
     * @param indexes Indexes.
     * @return Matrix.
     */
    public static float[][] getColumns(float[][] A, int[] indexes){
        float[][] m = new float[A.length][indexes.length];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                m[i][j] = A[i][indexes[j]];
            }
        }
        return m;
    }
    
    /**
     * Return a matrix from specified columns.
     * @param <T> Type.
     * @param A Matrix.
     * @param indexes Indexes.
     * @return Matrix.
     */
    public static <T> T[][] getColumns(T[][] A, int[] indexes){
        T[][] m = (T[][])Array.newInstance(A[0][0].getClass(), A.length, indexes.length);
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                m[i][j] = A[i][indexes[j]];
            }
        }
        return m;
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
     * Return a matrix from a specified row indexes.
     * @param A Matrix.
     * @param indexes Indexes.
     * @return Matrix.
     */
    public static double[][] getRows(double[][] A, int[] indexes){
        double[][] m = new double[indexes.length][A[0].length];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                m[i][j] = A[indexes[i]][j];
            }
        }
        return m;
    }
    
    /**
     * Return a matrix from a specified row indexes.
     * @param A Matrix.
     * @param indexes Indexes.
     * @return Matrix.
     */
    public static int[][] getRows(int[][] A, int[] indexes){
        int[][] m = new int[indexes.length][A[0].length];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                m[i][j] = A[indexes[i]][j];
            }
        }
        return m;
    }
    
    /**
     * Return a matrix from a specified row indexes.
     * @param A Matrix.
     * @param indexes Indexes.
     * @return Matrix.
     */
    public static float[][] getRows(float[][] A, int[] indexes){
        float[][] m = new float[indexes.length][A[0].length];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                m[i][j] = A[indexes[i]][j];
            }
        }
        return m;
    }
    
    /**
     * Return an array from a specified row indexes.
     * @param A Array.
     * @param indexes Indexes.
     * @return Array.
     */
    public static double[] getRows(double[] A, int[] indexes){
        double[] v = new double[indexes.length];
        for (int i = 0; i < v.length; i++) {
            v[i] = A[indexes[i]];
        }
        return v;
    }
    
    /**
     * Return an array from a specified row indexes.
     * @param A Array.
     * @param indexes Indexes.
     * @return Array.
     */
    public static int[] getRows(int[] A, int[] indexes){
        int[] v = new int[indexes.length];
        for (int i = 0; i < v.length; i++) {
            v[i] = A[indexes[i]];
        }
        return v;
    }
    
    /**
     * Return an array from a specified row indexes.
     * @param A Array.
     * @param indexes Indexes.
     * @return Array.
     */
    public static float[] getRows(float[] A, int[] indexes){
        float[] v = new float[indexes.length];
        for (int i = 0; i < v.length; i++) {
            v[i] = A[indexes[i]];
        }
        return v;
    }
    
    /**
     * Return an array from a specified row indexes.
     * @param <T> User definied type.
     * @param A Array.
     * @param indexes Indexes.
     * @return Array.
     */
    public static<T> T[] getRows(T[] A, int[] indexes){
        T[] v = (T[])Array.newInstance(A[0].getClass(), indexes.length);
        for (int i = 0; i < v.length; i++) {
            v[i] = A[indexes[i]];
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
                A[i][j] -= B[i][j];
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
                A[i][j] -= B[i][j];
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
                A[i][j] -= B[i][j];
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
     * Sum all the elements in the matrix.
     * @param A Matrix.
     * @return Sum of the Matrix.
     */
    public static double Sum(double[][] A){
        double sum = 0;
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                sum += A[i][j];
            }
        }
        
        return sum;
    }
    
    /**
     * Sum all the elements in the matrix.
     * @param A Matrix.
     * @return Sum of the Matrix.
     */
    public static int Sum(int[][] A){
        int sum = 0;
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                sum += A[i][j];
            }
        }
        
        return sum;
    }
    
    /**
     * Sum all the elements in the matrix.
     * @param A Matrix.
     * @return Sum of the Matrix.
     */
    public static float Sum(float[][] A){
        float sum = 0;
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                sum += A[i][j];
            }
        }
        
        return sum;
    }
    
    /**
     * Sum absolute of all the elements in the matrix.
     * @param A Matrix.
     * @return Sum of the Matrix.
     */
    public static double SumAbs(double[][] A){
        double sum = 0;
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                sum += Math.abs(A[i][j]);
            }
        }
        
        return sum;
    }
    
    /**
     * Sum absolute of all the elements in the matrix.
     * @param A Matrix.
     * @return Sum of the Matrix.
     */
    public static int SumAbs(int[][] A){
        int sum = 0;
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                sum += Math.abs(A[i][j]);
            }
        }
        
        return sum;
    }
    
    /**
     * Sum absolute of all the elements in the matrix.
     * @param A Matrix.
     * @return Sum of the Matrix.
     */
    public static float SumAbs(float[][] A){
        float sum = 0;
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                sum += Math.abs(A[i][j]);
            }
        }
        
        return sum;
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
     * Swap column.
     * @param A Matrix.
     * @param a Origin index.
     * @param b Destiny index.
     */
    public static void SwapColumn(double[][] A, int a, int b){
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                double t1 = A[i][a];
                double t2 = A[i][b];
                A[i][a] = t2;
                A[i][b] = t1;
            }
        }
    }
    
    /**
     * Swap column.
     * @param A Matrix.
     * @param a Origin index.
     * @param b Destiny index.
     */
    public static void SwapColumn(int[][] A, int a, int b){
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                int t1 = A[i][a];
                int t2 = A[i][b];
                A[i][a] = t2;
                A[i][b] = t1;
            }
        }
    }
    
    /**
     * Swap column.
     * @param A Matrix.
     * @param a Origin index.
     * @param b Destiny index.
     */
    public static void SwapColumn(float[][] A, int a, int b){
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                float t1 = A[i][a];
                float t2 = A[i][b];
                A[i][a] = t2;
                A[i][b] = t1;
            }
        }
    }
    
    /**
     * Swap row.
     * @param A Matrix.
     * @param a Origin index.
     * @param b Destiny index.
     */
    public static void SwapRow(double[][] A, int a, int b){
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                double t1 = A[a][j];
                double t2 = A[b][j];
                A[a][j] = t2;
                A[b][j] = t1;
            }
        }
    }
    
    /**
     * Swap row.
     * @param A Matrix.
     * @param a Origin index.
     * @param b Destiny index.
     */
    public static void SwapRow(int[][] A, int a, int b){
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                int t1 = A[a][j];
                int t2 = A[b][j];
                A[a][j] = t2;
                A[b][j] = t1;
            }
        }
    }
    
    /**
     * Swap row.
     * @param A Matrix.
     * @param a Origin index.
     * @param b Destiny index.
     */
    public static void SwapRow(float[][] A, int a, int b){
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                float t1 = A[a][j];
                float t2 = A[b][j];
                A[a][j] = t2;
                A[b][j] = t1;
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
        
        if(A[0].length != B.length)
            throw new IllegalArgumentException("Illegal matrix dimensions.");
        
        double[][] result = new double[A.length][B[0].length];
        
        int n = A[0].length;
        int m = A.length;
        int p = B[0].length;

        double[] Bcolj = new double[n];
        for (int j = 0; j < p; j++)
        {
            for (int k = 0; k < n; k++)
                Bcolj[k] = B[k][j];

            for (int i = 0; i < m; i++)
            {
                double[] Arowi = A[i];

                double s = 0;
                for (int k = 0; k < n; k++)
                    s += Arowi[k] * Bcolj[k];

                result[i][j] = s;
            }
        }

        return result;
    }
    
    /**
     * Multiply two matrices.
     * @param A Matrix.
     * @param B Matrix.
     * @return The multiply of the given matrices.
     */
    public static int[][] Multiply(int[][] A, int[][] B){
        
        if(A[0].length != B.length)
            throw new IllegalArgumentException("Illegal matrix dimensions.");
        
        int[][] result = new int[A.length][B[0].length];
        
        int n = A[0].length;
        int m = A.length;
        int p = B[0].length;

        int[] Bcolj = new int[n];
        for (int j = 0; j < p; j++)
        {
            for (int k = 0; k < n; k++)
                Bcolj[k] = B[k][j];

            for (int i = 0; i < m; i++)
            {
                int[] Arowi = A[i];

                int s = 0;
                for (int k = 0; k < n; k++)
                    s += Arowi[k] * Bcolj[k];

                result[i][j] = s;
            }
        }

        return result;
    }
    
    /**
     * Multiply A vector by a matrix B.
     * @param A Vector.
     * @param B Matrix.
     * @return Result of the multiplication.
     */
    public static double[] Multiply(double[] A, double[][] B){
        
        double[] r = new double[B[0].length];
        for (int j = 0; j < B[0].length; j++) {
            for (int i = 0; i < B.length; i++) {
                r[j] += A[i] * B[i][j];
            }
        }
        return r;
    }
    
    /**
     * Multiply A vector by a matrix B.
     * @param A Vector.
     * @param B Matrix.
     * @return Result of the multiplication.
     */
    public static int[] Multiply(int[] A, int[][] B){
        
        int[] r = new int[B[0].length];
        for (int j = 0; j < B[0].length; j++) {
            for (int i = 0; i < B.length; i++) {
                r[j] += A[i] * B[i][j];
            }
        }
        return r;
    }
    
    /**
     * Multiply A vector by a matrix B.
     * @param A Vector.
     * @param B Matrix.
     * @return Result of the multiplication.
     */
    public static float[] Multiply(float[] A, float[][] B){
        
        float[] r = new float[B[0].length];
        for (int j = 0; j < B[0].length; j++) {
            for (int i = 0; i < B.length; i++) {
                r[j] += A[i] * B[i][j];
            }
        }
        return r;
    }
    
    /**
     * Multiply two matrices.
     * @param A Matrix.
     * @param B Matrix.
     * @return The multiply of the given matrices.
     */
    public static float[][] Multiply(float[][] A, float[][] B){
        
        if(A[0].length != B.length)
            throw new IllegalArgumentException("Illegal matrix dimensions.");
        
        float[][] result = new float[A.length][B[0].length];
        
        int n = A[0].length;
        int m = A.length;
        int p = B[0].length;

        float[] Bcolj = new float[n];
        for (int j = 0; j < p; j++)
        {
            for (int k = 0; k < n; k++)
                Bcolj[k] = B[k][j];

            for (int i = 0; i < m; i++)
            {
                float[] Arowi = A[i];

                float s = 0;
                for (int k = 0; k < n; k++)
                    s += Arowi[k] * Bcolj[k];

                result[i][j] = s;
            }
        }

        return result;
    }
    
    /**
     * Multiply a Matrix with scalar value.
     * @param A Matrix.
     * @param value Value.
     * @return The multiply of the matrix with scalar value.
     */
    public static double[][] Multiply(double[][] A, double value){
        
        double[][] result = new double[A.length][A[0].length];
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                result[i][j] = A[i][j] * value;
            }
        }
        return result;
        
    }
    
    /**
     * Multiply a Matrix with scalar value.
     * @param A Matrix.
     * @param value Value.
     * @return The multiply of the matrix with scalar value.
     */
    public static float[][] Multiply(float[][] A, float value){
        
        float[][] result = new float[A.length][A[0].length];
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                result[i][j] = A[i][j] * value;
            }
        }
        return result;
        
    }
    
    /**
     * Multiply a Matrix with scalar value.
     * @param A Matrix.
     * @param value Value.
     * @return The multiply of the matrix with scalar value.
     */
    public static int[][] Multiply(int[][] A, int value){
        
        int[][] result = new int[A.length][A[0].length];
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                result[i][j] = A[i][j] * value;
            }
        }
        return result;
        
    }
    
    public static double[][] MultiplyByDiagonal(double[][] A, double[] B){
        double[][] r = new double[A.length][B.length];
        
        for (int i = 0; i < r.length; i++) {
            for (int j = 0; j < r[0].length; j++) {
                r[i][j] = A[i][j] * B[j];
            }
        }
        
        return r;
    }
    
    /**
     * Multiply a Matrix with the transpose. A * A'
     * @param A Matrix.
     * @return The multiply of the matrix with transpose of itself.
     */
    public static double[][] MultiplyByTranspose(double[][] A){
        return Multiply(A,Transpose(A));
    }
    
    /**
     * Multiply a Matrix with the transpose. A * A'
     * @param A Matrix.
     * @return The multiply of the matrix with transpose of itself.
     */
    public static int[][] MultiplyByTranspose(int[][] A){
        return Multiply(A,Transpose(A));
    }
    
    /**
     * Multiply a Matrix with the transpose. A * A'
     * @param A Matrix.
     * @return The multiply of the matrix with transpose of itself.
     */
    public static float[][] MultiplyByTranspose(float[][] A){
        return Multiply(A,Transpose(A));
    }
    
    /**
     * Multiply a Matrix with the transpose of other. A * B'
     * @param A Matrix.
     * @param B Matrix to be transposed.
     * @return The multiply of the matrix with transpose of the B.
     */
    public static double[][] MultiplyByTranspose(double[][] A, double[][] B){
        return Multiply(A,Transpose(B));
    }
    
    /**
     * Multiply a Matrix with the transpose of other. A * B'
     * @param A Matrix.
     * @param B Matrix to be transposed.
     * @return The multiply of the matrix with transpose of the B.
     */
    public static int[][] MultiplyByTranspose(int[][] A, int[][] B){
        return Multiply(A,Transpose(B));
    }
    
    /**
     * Multiply a Matrix with the transpose of other. A * B'
     * @param A Matrix.
     * @param B Matrix to be transposed.
     * @return The multiply of the matrix with transpose of the B.
     */
    public static float[][] MultiplyByTranspose(float[][] A, float[][] B){
        return Multiply(A,Transpose(B));
    }
    
    /**
     * Multiply a Matrix with the transpose of other. A * B'
     * @param A Matrix.
     * @param B Vector.
     * @return The multiply of the matrix with transpose of the B.
     */
    public static double[] MultiplyByTranspose(double[][] A, double[] B){
        
        if(A[0].length != B.length)
            throw new IllegalArgumentException("The columns of the matrix A must be the same of the vector B");
        
        double[] result = new double[A.length];
        for (int i = 0; i < A.length; i++) {
            double r = 0;
            for (int j = 0; j < A[0].length; j++) {
                r += A[i][j] * B[j];
            }
            result[i] = r;
        }
        return result;
    }
    
    /**
     * Multiply a Matrix with the transpose of other. A * B'
     * @param A Matrix.
     * @param B Vector.
     * @return The multiply of the matrix with transpose of the B.
     */
    public static int[] MultiplyByTranspose(int[][] A, int[] B){
        
        if(A[0].length != B.length)
            throw new IllegalArgumentException("The columns of the matrix A must be the same of the vector B");
        
        int[] result = new int[A.length];
        for (int i = 0; i < A.length; i++) {
            int r = 0;
            for (int j = 0; j < A[0].length; j++) {
                r += A[i][j] * B[j];
            }
            result[i] = r;
        }
        return result;
    }
    
    /**
     * Multiply a Matrix with the transpose of other. A * B'
     * @param A Matrix.
     * @param B Vector.
     * @return The multiply of the matrix with transpose of the B.
     */
    public static float[] MultiplyByTranspose(float[][] A, float[] B){
        
        if(A[0].length != B.length)
            throw new IllegalArgumentException("The columns of the matrix A must be the same of the vector B");
        
        float[] result = new float[A.length];
        for (int i = 0; i < A.length; i++) {
            float r = 0;
            for (int j = 0; j < A[0].length; j++) {
                r += A[i][j] * B[j];
            }
            result[i] = r;
        }
        return result;
    }
    
    /**
     * Calculate 1-norm of a matrix.
     * @param A Matrix.
     * @return 1-norm of a matrix.
     */
    public static double Norm1(double[][] A){
        
        double max = 0;
        for (int j = 0; j < A[0].length; j++) {
            double sum = 0;
            for (int i = 0; i < A.length; i++) {
                sum += Math.abs(A[i][j]);
            }
            max = Math.max(max, sum);
        }
        
        return max;
        
    }
    
    /**
     * Calculate 1-norm of a matrix.
     * @param A Matrix.
     * @return 1-norm of a matrix.
     */
    public static int Norm1(int[][] A){
        
        int max = 0;
        for (int j = 0; j < A[0].length; j++) {
            int sum = 0;
            for (int i = 0; i < A.length; i++) {
                sum += Math.abs(A[i][j]);
            }
            max = Math.max(max, sum);
        }
        
        return max;
        
    }
    
    /**
     * Calculate 1-norm of a matrix.
     * @param A Matrix.
     * @return 1-norm of a matrix.
     */
    public static float Norm1(float[][] A){
        
        float max = 0;
        for (int j = 0; j < A[0].length; j++) {
            float sum = 0;
            for (int i = 0; i < A.length; i++) {
                sum += Math.abs(A[i][j]);
            }
            max = Math.max(max, sum);
        }
        
        return max;
        
    }
    
    /**
     * Calculate 2-norm of a matrix.
     * @param A Matrix.
     * @return 2-norm of a matrix.
     */
    public static double Norm2(double[][] A){
        return new SingularValueDecomposition(A).getS()[0][0];
    }
    
    /**
     * Calculate Frobenius norm of a matrix.
     * @param A Matrix.
     * @return Frobenius norm.
     */
    public static double NormF(double[][] A){
        
        double sum = 0;
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                sum += Math.pow(Math.abs(A[i][j]), 2);
            }
        }
        
        return Math.sqrt(sum);
        
    }
    
    /**
     * Calculate Frobenius norm of a matrix.
     * @param A Matrix.
     * @return Frobenius norm.
     */
    public static double NormF(int[][] A){
        
        double sum = 0;
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                sum += Math.pow(Math.abs(A[i][j]), 2);
            }
        }
        
        return Math.sqrt(sum);
        
    }
    
    /**
     * Calculate Frobenius norm of a matrix.
     * @param A Matrix.
     * @return Frobenius norm.
     */
    public static float NormF(float[][] A){
        
        float sum = 0;
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                sum += Math.pow(Math.abs(A[i][j]), 2);
            }
        }
        
        return (float)Math.sqrt(sum);
        
    }
    
    /**
     * Calculate P norm of a vector.
     * @param v Vector.
     * @param p P must be an integer greater than 1.
     * @return P norm.
     */
    public static double NormP(double[] v, int p){
        double sum = 0;
        for (int i = 0; i < v.length; i++) {
            sum += Math.pow(Math.abs(v[i]), p);
        }
        return Math.pow(sum, 1.0/(double)p);
    }
    
    /**
     * Outer Product.
     * Outer Product u x u is equivalent to a matrix multiplication uu^t.
     * @param u Coordinate vector.
     * @return Matrix.
     */
    public static double[][] OuterProduct(double[] u){
        return OuterProduct(u,u);
    }
    
    /**
     * Outer Product.
     * Outer Product u x v is equivalent to a matrix multiplication uv^t.
     * @param u Coordinate vector.
     * @param v Coordinate vector.
     * @return Matrix.
     */
    public static double[][] OuterProduct(double[] u, double[] v){
        double[][] r = new double[u.length][v.length];
        
        for (int i = 0; i < u.length; i++) {
            for (int j = 0; j < v.length; j++) {
                r[i][j] = u[i] * v[j];
            }
        }
        
        return r;
    }
    
    /**
     * Outer Product.
     * Outer Product u x u is equivalent to a matrix multiplication uu^t.
     * @param u Coordinate vector.
     * @return Matrix.
     */
    public static int[][] OuterProduct(int[] u){
        return OuterProduct(u,u);
    }
    
    /**
     * Outer Product.
     * Outer Product u x v is equivalent to a matrix multiplication uv^t.
     * @param u Coordinate vector.
     * @param v Coordinate vector.
     * @return Matrix.
     */
    public static int[][] OuterProduct(int[] u, int[] v){
        int[][] r = new int[u.length][v.length];
        
        for (int i = 0; i < u.length; i++) {
            for (int j = 0; j < v.length; j++) {
                r[i][j] = u[i] * v[j];
            }
        }
        
        return r;
    }
    
    /**
     * Outer Product.
     * Outer Product u x u is equivalent to a matrix multiplication uu^t.
     * @param u Coordinate vector.
     * @return Matrix.
     */
    public static float[][] OuterProduct(float[] u){
        return OuterProduct(u,u);
    }
    
    /**
     * Outer Product.
     * Outer Product u x v is equivalent to a matrix multiplication uv^t.
     * @param u Coordinate vector.
     * @param v Coordinate vector.
     * @return Matrix.
     */
    public static float[][] OuterProduct(float[] u, float[] v){
        float[][] r = new float[u.length][v.length];
        
        for (int i = 0; i < u.length; i++) {
            for (int j = 0; j < v.length; j++) {
                r[i][j] = u[i] * v[j];
            }
        }
        
        return r;
    }
    
    /**
     * Calculate the pseudo-inverse of the matrix.
     * @param A Matrix.
     * @return Pseudo-inverse of the matrix.
     */
    public static double[][] PseudoInverse(double[][] A){
        return new SingularValueDecomposition(A).inverse();
    }
    
    /**
     * Is defined to be the sum of the elements on the main diagonal.
     * @param A Matrix NxN.
     * @return Trace(A).
     */
    public static double Trace(double[][] A){
        if(isSquare(A)){
            double sum = 0;
            for (int i = 0; i < A.length; i++) {
                sum += A[i][i];
            }
            return sum;
        }
        else{
            throw new IllegalArgumentException("The matrix must be square.");
        }
    }
    
    /**
     * Is defined to be the sum of the elements on the main diagonal.
     * @param A Matrix NxN.
     * @return Trace(A).
     */
    public static int Trace(int[][] A){
        if(isSquare(A)){
            int sum = 0;
            for (int i = 0; i < A.length; i++) {
                sum += A[i][i];
            }
            return sum;
        }
        else{
            throw new IllegalArgumentException("The matrix must be square.");
        }
    }
    
    /**
     * Is defined to be the sum of the elements on the main diagonal.
     * @param A Matrix NxN.
     * @return Trace(A).
     */
    public static float Trace(float[][] A){
        if(isSquare(A)){
            float sum = 0;
            for (int i = 0; i < A.length; i++) {
                sum += A[i][i];
            }
            return sum;
        }
        else{
            throw new IllegalArgumentException("The matrix must be square.");
        }
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
     * Gets the transpose of the matrix.
     * @param <E> Object.
     * @param A Matrix.
     * @return Transposed matrix.
     */
    public static <E> E[][] Transpose(E[][] A){
        E[][] t = (E[][])Array.newInstance(A[0][0].getClass(), A[0].length, A.length);
        
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
     * Gets the Identity matrix of the given size.
     * @param m Number of rows.
     * @param n Number of columns.
     * @return Identity matrix.
     */
    public static double[][] Identity(int m, int n){
        double[][] A = new double[m][n];
        for (int i = 0; i < m; i++) {
          for (int j = 0; j < n; j++) {
            A[i][j] = (i == j ? 1.0 : 0.0);
          }
        }
        return A;
    }
    
    /**
     * Matrix inverse or pseudoinverse.
     * @param A Matrix.
     * @return Matrix inverse.
     */
    public static double[][] Inverse(double[][] A){
        return new LUDecomposition(A).inverse();
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
    
    /**
     * Get the mean of the all elements in the matrix.
     * @param matrix Matrix.
     * @return Mean.
     */
    public static double Mean(double[][] matrix){
        double mean = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                mean += matrix[i][j];
            }
        }
        return mean /= (double)(matrix.length * matrix[0].length);
    }
    
    /**
     * Get the mean of the all elements in the matrix.
     * @param matrix Matrix.
     * @return Mean.
     */
    public static double Mean(int[][] matrix){
        double mean = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                mean += matrix[i][j];
            }
        }
        return mean /= (double)(matrix.length * matrix[0].length);
    }
    
    /**
     * Get the mean of the all elements in the matrix.
     * @param matrix Matrix.
     * @return Mean.
     */
    public static float Mean(float[][] matrix){
        float mean = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                mean += matrix[i][j];
            }
        }
        return mean /= (float)(matrix.length * matrix[0].length);
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
    public static int Min(int[] matrix){
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
    public static float Min(float[] matrix){
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
     * Get the minimum and the maximum of the matrix.
     * @param matrix Matrix.
     * @return Min and maximum in the matrix.
     */
    public static double[] MinMax(double[][] matrix){
        double min = Double.MAX_VALUE;
        double max = -Double.MAX_VALUE;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                min = Math.min(min, matrix[i][j]);
                max = Math.max(max, matrix[i][j]);
            }
        }
        return new double[] {min, max};
    }
    
    /**
     * Get the minimum and the maximum of the matrix.
     * @param matrix Matrix.
     * @return Min and maximum in the matrix.
     */
    public static int[] MinMax(int[][] matrix){
        int min = Integer.MAX_VALUE;
        int max = -Integer.MAX_VALUE;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                min = Math.min(min, matrix[i][j]);
                max = Math.max(max, matrix[i][j]);
            }
        }
        return new int[] {min, max};
    }
    
    /**
     * Get the minimum and the maximum of the matrix.
     * @param matrix Matrix.
     * @return Min and maximum in the matrix.
     */
    public static float[] MinMax(float[][] matrix){
        float min = Float.MAX_VALUE;
        float max = -Float.MAX_VALUE;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                min = Math.min(min, matrix[i][j]);
                max = Math.max(max, matrix[i][j]);
            }
        }
        return new float[] {min, max};
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
     * Check if the matrix A is the same of B.
     * @param A Matrix.
     * @param B Matrix.
     * @return True if is the same, otherwise false
     */
    public static boolean isEqual(double[][] A, double[][] B){
        
        if((A.length != B.length) || (A[0].length != B[0].length))
            throw new IllegalArgumentException("The matrix A must be the same size of the B.");
        
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                if (A[i][j] != B[i][j])
                    return false;
            }
        }
        
        return true;
        
    }
    
    /**
     * Check if the matrix A is the same of B.
     * @param A Matrix.
     * @param B Matrix.
     * @return True if is the same, otherwise false
     */
    public static boolean isEqual(int[][] A, int[][] B){
        
        if((A.length != B.length) || (A[0].length != B[0].length))
            throw new IllegalArgumentException("The matrix A must be the same size of the B.");
        
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                if (A[i][j] != B[i][j])
                    return false;
            }
        }
        
        return true;
        
    }
    
    /**
     * Check if the matrix A is the same of B.
     * @param A Matrix.
     * @param B Matrix.
     * @return True if is the same, otherwise false
     */
    public static boolean isEqual(float[][] A, float[][] B){
        
        if((A.length != B.length) || (A[0].length != B[0].length))
            throw new IllegalArgumentException("The matrix A must be the same size of the B.");
        
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                if (A[i][j] != B[i][j])
                    return false;
            }
        }
        
        return true;
        
    }
    
    /**
     * Check if all values are positive.
     * @param A Matrix.
     * @return True if all the values are positive.
     */
    public static boolean isNonNegative(double[][] A){
        
        for (int i = 0; i < A.length; i++)
            for (int j = 0; j < A[0].length; j++)
                if(A[i][j] < 0) return false;
        
        return true;
    }
    
    /**
     * Check if all values are positive.
     * @param A Matrix.
     * @return True if all the values are positive.
     */
    public static boolean isNonNegative(int[][] A){
        
        for (int i = 0; i < A.length; i++)
            for (int j = 0; j < A[0].length; j++)
                if(A[i][j] < 0) return false;
        
        return true;
    }
    
    /**
     * Check if all values are positive.
     * @param A Matrix.
     * @return True if all the values are positive.
     */
    public static boolean isNonNegative(float[][] A){
        
        for (int i = 0; i < A.length; i++)
            for (int j = 0; j < A[0].length; j++)
                if(A[i][j] < 0) return false;
        
        return true;
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
     * Check if the matrix is symmetric. A = A'
     * @param A Matrix.
     * @return True if is symmetric, otherwise return false.
     */
    public static boolean isSymmetric(double[][] A){
        double[][] B = Transpose(A);
        return isEqual(A, B);
    }
    
    /**
     * Check if the matrix is symmetric. A = A'
     * @param A Matrix.
     * @return True if is symmetric, otherwise return false.
     */
    public static boolean isSymmetric(int[][] A){
        int[][] B = Transpose(A);
        return isEqual(A, B);
    }
    
    /**
     * Check if the matrix is symmetric. A = A'
     * @param A Matrix.
     * @return True if is symmetric, otherwise return false.
     */
    public static boolean isSymmetric(float[][] A){
        float[][] B = Transpose(A);
        return isEqual(A, B);
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
    
    /**
     * Calculate the rank of the matrix.
     * @param A matrix.
     * @return Rank of the matrix.
     */
    public static int Rank(double[][] A){
        SingularValueDecomposition svd = new SingularValueDecomposition(A, false, false);
        return svd.rank();
    }
    
    /**
     * Calculate the rank of the matrix.
     * @param A Matrix.
     * @return Rank in the matrix.
     */
    public static int Rank(int[][] A){
        SingularValueDecomposition svd = new SingularValueDecomposition(ArraysUtil.toDouble(A), false, false);
        return svd.rank();
    }
    
    /**
     * Calculate the rank of the matrix.
     * @param A Matrix.
     * @return Rank in the matrix.
     */
    public static int Rank(float[][] A){
        SingularValueDecomposition svd = new SingularValueDecomposition(ArraysUtil.toDouble(A), false, false);
        return svd.rank();
    }
    
    /**
     * Remove columns from the matrix.
     * @param A Matrix.
     * @param index Indexes.
     * @return Matrix.
     */
    public static int[][] RemoveColumns(int[][] A, int[] index){
        
        if(A[0].length - index.length <= 0)
            throw new IllegalArgumentException("The number of columns is less or equal zero.");
        
        int[][] B = new int[A.length][A[0].length - index.length];
        int idx;
        int p;
        int c;
        for (int i = 0; i < A.length; i++) {
            idx = index[0];
            p = 0;
            c = 0;
            for (int j = 0; j < A[0].length; j++) {
                if(j == idx){
                    if(p < index.length - 1)
                        idx = index[++p];
                }
                else{
                    B[i][c++] = A[i][j];
                }
            }
        }
        return B;
    }
    
    /**
     * Remove column from the matrix.
     * @param A Matrix.
     * @param index Index.
     * @return Matrix.
     */
    public static double[][] RemoveColumn(double[][] A, int index){
        double[][] B = new double[A.length][A[0].length - 1];
        int idx;
        for (int i = 0; i < A.length; i++) {
            idx = 0;
            for (int j = 0; j < A[0].length; j++) {
                if(j != index){
                    B[i][idx] = A[i][j];
                    idx++;
                }
            }
        }
        return B;
    }
    
    /**
     * Remove column from the matrix.
     * @param A Matrix.
     * @param index Index.
     * @return Matrix.
     */
    public static int[][] RemoveColumn(int[][] A, int index){
        int[][] B = new int[A.length][A[0].length - 1];
        int idx;
        for (int i = 0; i < A.length; i++) {
            idx = 0;
            for (int j = 0; j < A[0].length; j++) {
                if(j != index){
                    B[i][idx] = A[i][j];
                    idx++;
                }
            }
        }
        return B;
    }
    
    /**
     * Remove column from the matrix.
     * @param A Matrix.
     * @param index Index.
     * @return Matrix.
     */
    public static float[][] RemoveColumn(float[][] A, int index){
        float[][] B = new float[A.length][A[0].length - 1];
        int idx;
        for (int i = 0; i < A.length; i++) {
            idx = 0;
            for (int j = 0; j < A[0].length; j++) {
                if(j != index){
                    B[i][idx] = A[i][j];
                    idx++;
                }
            }
        }
        return B;
    }
    
    /**
     * Remove columns from the matrix.
     * @param A Matrix.
     * @param index Indexes.
     * @return Matrix.
     */
    public static double[][] RemoveColumns(double[][] A, int[] index){
        
        if(A[0].length - index.length <= 0)
            throw new IllegalArgumentException("The number of columns is less or equal zero.");
        
        double[][] B = new double[A.length][A[0].length - index.length];
        int idx;
        int p;
        int c;
        for (int i = 0; i < A.length; i++) {
            idx = index[0];
            p = 0;
            c = 0;
            for (int j = 0; j < A[0].length; j++) {
                if(j == idx){
                    if(p < index.length - 1)
                        idx = index[++p];
                }
                else{
                    B[i][c++] = A[i][j];
                }
            }
        }
        return B;
    }
    
    /**
     * Remove columns from the matrix.
     * @param A Matrix.
     * @param index Indexes.
     * @return Matrix.
     */
    public static float[][] RemoveColumns(float[][] A, int[] index){
        
        if(A[0].length - index.length <= 0)
            throw new IllegalArgumentException("The number of columns is less or equal zero.");
        
        float[][] B = new float[A.length][A[0].length - index.length];
        int idx;
        int p;
        int c;
        for (int i = 0; i < A.length; i++) {
            idx = index[0];
            p = 0;
            c = 0;
            for (int j = 0; j < A[0].length; j++) {
                if(j == idx){
                    if(p < index.length - 1)
                        idx = index[++p];
                }
                else{
                    B[i][c++] = A[i][j];
                }
            }
        }
        return B;
    }
    
    /**
     * Remove column from the vector.
     * @param A Vector.
     * @param index Index.
     * @return Vector.
     */
    public static double[] RemoveColumn(double[] A, int index){
        if(A.length - index <= 0)
            throw new IllegalArgumentException("The number of columns is less or equal zero.");
        
        double[] B = new double[A.length - 1];
        int idx = 0;
        for (int i = 0; i < A.length; i++) {
            if(i != index)
                B[idx++] = A[i];
            
        }
        
        return B;
    }
    
    /**
     * Remove column from the vector.
     * @param A Vector.
     * @param index Index.
     * @return Vector.
     */
    public static int[] RemoveColumn(int[] A, int index){
        if(A.length - index <= 0)
            throw new IllegalArgumentException("The number of columns is less or equal zero.");
        
        int[] B = new int[A.length - 1];
        int idx = 0;
        for (int i = 0; i < A.length; i++) {
            if(i != index)
                B[idx++] = A[i];
            
        }
        
        return B;
    }
    
    /**
     * Remove column from the vector.
     * @param A Vector.
     * @param index Index.
     * @return Vector.
     */
    public static float[] RemoveColumn(float[] A, int index){
        if(A.length - index <= 0)
            throw new IllegalArgumentException("The number of columns is less or equal zero.");
        
        float[] B = new float[A.length - 1];
        int idx = 0;
        for (int i = 0; i < A.length; i++) {
            if(i != index)
                B[idx++] = A[i];
            
        }
        
        return B;
    }
    
    /**
     * Remove column from the vector.
     * @param <T> Type.
     * @param A Vector.
     * @param index Index.
     * @return Vector.
     */
    public static <T> T[] RemoveColumn(T[] A, int index){
        if(A.length - index <= 0)
            throw new IllegalArgumentException("The number of columns is less or equal zero.");
        
        T[] B = (T[])Array.newInstance(A[0].getClass(), A.length - 1);
        int idx = 0;
        for (int i = 0; i < A.length; i++) {
            if(i != index)
                B[idx++] = A[i];
            
        }
        
        return B;
    }
    
    /**
     * Remove columns from the vector.
     * @param A Vector.
     * @param index Array of index.
     * @return Vector.
     */
    public static double[] RemoveColumns(double[] A, int[] index){
        if(A.length - index.length <= 0)
            throw new IllegalArgumentException("The number of columns is less or equal zero.");
        
        double[] B = new double[A.length - index.length];
        int idx = 0;
        for (int i = 0; i < A.length; i++) {
            boolean has = false;
            for (int j = 0; j < index.length; j++)
                if(index[j] == i) has = true;
            if(!has){
                B[idx] = A[i];
                idx++;
            }
        }
        
        return B;
    }
    
    /**
     * Remove columns from the vector.
     * @param A Vector.
     * @param index Array of index.
     * @return Vector.
     */
    public static int[] RemoveColumns(int[] A, int[] index){
        if(A.length - index.length <= 0)
            throw new IllegalArgumentException("The number of columns is less or equal zero.");
        
        int[] B = new int[A.length - index.length];
        int idx = 0;
        for (int i = 0; i < A.length; i++) {
            boolean has = false;
            for (int j = 0; j < index.length; j++)
                if(index[j] == i) has = true;
            if(!has){
                B[idx] = A[i];
                idx++;
            }
        }
        
        return B;
    }
    
    /**
     * Remove columns from the vector.
     * @param A Vector.
     * @param index Array of index.
     * @return Vector.
     */
    public static float[] RemoveColumns(float[] A, int[] index){
        if(A.length - index.length <= 0)
            throw new IllegalArgumentException("The number of columns is less or equal zero.");
        
        float[] B = new float[A.length - index.length];
        int idx = 0;
        for (int i = 0; i < A.length; i++) {
            boolean has = false;
            for (int j = 0; j < index.length; j++)
                if(index[j] == i) has = true;
            if(!has){
                B[idx] = A[i];
                idx++;
            }
        }
        
        return B;
    }
    
    /**
     * Remove columns from the vector.
     * @param <T> Type.
     * @param A Vector.
     * @param index Array of index.
     * @return Vector.
     */
    public static <T> T[] RemoveColumns(T[] A, int[] index){
        if(A.length - index.length <= 0)
            throw new IllegalArgumentException("The number of columns is less or equal zero.");
        
        T[] B = (T[])Array.newInstance(A[0].getClass(), A.length - index.length);
        int idx = 0;
        for (int i = 0; i < A.length; i++) {
            boolean has = false;
            for (int j = 0; j < index.length; j++)
                if(index[j] == i) has = true;
            if(!has){
                B[idx] = A[i];
                idx++;
            }
        }
        
        return B;
    }
    
    /**
     * Remove a specified row from the matrix.
     * @param A Matrix.
     * @param index Index.
     * @return New matrix.
     */
    public static double[][] RemoveRow(double[][] A, int index){
        if(A.length - 1 <= 0)
            throw new IllegalArgumentException("The number of rows is less or equal zero.");
        
        double[][] B = new double[A.length - 1][A[0].length];
        int idx = 0;
        for (int i = 0; i < A.length; i++) {
            if(i != index){
                System.arraycopy(A[i], 0, B[idx], 0, A[0].length);
                idx++;
            }
        }
        
        return B;
    }
    
    /**
     * Remove a specified row from the matrix.
     * @param A Matrix.
     * @param index Index.
     * @return New matrix.
     */
    public static int[][] RemoveRow(int[][] A, int index){
        if(A.length - 1 <= 0)
            throw new IllegalArgumentException("The number of rows is less or equal zero.");
        
        int[][] B = new int[A.length - 1][A[0].length];
        int idx = 0;
        for (int i = 0; i < A.length; i++) {
            if(i != index){
                System.arraycopy(A[i], 0, B[idx], 0, A[0].length);
                idx++;
            }
        }
        
        return B;
    }
    
    /**
     * Remove a specified row from the matrix.
     * @param A Matrix.
     * @param index Index.
     * @return New matrix.
     */
    public static float[][] RemoveRow(float[][] A, int index){
        if(A.length - 1 <= 0)
            throw new IllegalArgumentException("The number of rows is less or equal zero.");
        
        float[][] B = new float[A.length - 1][A[0].length];
        int idx = 0;
        for (int i = 0; i < A.length; i++) {
            if(i != index){
                System.arraycopy(A[i], 0, B[idx], 0, A[0].length);
                idx++;
            }
        }
        
        return B;
    }
    
    /**
     * Remove a specified row from the matrix.
     * @param A Matrix.
     * @param index Index.
     * @return New matrix.
     */
    public static <T> T[][] RemoveRow(T[][] A, int index){
        if(A.length - 1 <= 0)
            throw new IllegalArgumentException("The number of rows is less or equal zero.");
        
        T[][] B = (T[][])Array.newInstance(A[0][0].getClass(), new int[] {A.length - 1, A[0].length});
        int idx = 0;
        for (int i = 0; i < A.length; i++) {
            if(i != index){
                System.arraycopy(A[i], 0, B[idx], 0, A[0].length);
                idx++;
            }
        }
        
        return B;
    }
    
    /**
     * Remove rows from the matrix.
     * @param A Matrix.
     * @param index Indexes.
     * @return Matrix.
     */
    public static int[][] RemoveRows(int[][] A, int[] index){
        if(A.length - index.length <= 0)
            throw new IllegalArgumentException("The number of rows is less or equal zero.");
        
        int[][] B = new int[A.length - index.length][A[0].length];
        int idx;
        int p;
        int c;
        
        idx = index[0];
        p = c = 0;
        for (int i = 0; i < A.length; i++) {
            if(i == idx){
                if(p < index.length - 1)
                    idx = index[++p];
            }
            else{
                for (int j = 0; j < A[0].length; j++) {
                    B[c][j] = A[i][j];
                }
                c++;
            }
        }
        return B;
    }
    
    /**
     * Remove rows from the matrix.
     * @param A Matrix.
     * @param index Indexes.
     * @return Matrix.
     */
    public static double[][] RemoveRows(double[][] A, int[] index){
        if(A.length - index.length <= 0)
            throw new IllegalArgumentException("The number of rows is less or equal zero.");
        
        double[][] B = new double[A.length - index.length][A[0].length];
        int idx;
        int p;
        int c;
        
        idx = index[0];
        p = c = 0;
        for (int i = 0; i < A.length; i++) {
            if(i == idx){
                if(p < index.length - 1)
                    idx = index[++p];
            }
            else{
                for (int j = 0; j < A[0].length; j++) {
                    B[c][j] = A[i][j];
                }
                c++;
            }
        }
        return B;
    }
    
    /**
     * Remove rows from the matrix.
     * @param A Matrix.
     * @param index Indexes.
     * @return Matrix.
     */
    public static float[][] RemoveRows(float[][] A, int[] index){
        if(A.length - index.length <= 0)
            throw new IllegalArgumentException("The number of rows is less or equal zero.");
        
        float[][] B = new float[A.length - index.length][A[0].length];
        int idx;
        int p;
        int c;
        
        idx = index[0];
        p = c = 0;
        for (int i = 0; i < A.length; i++) {
            if(i == idx){
                if(p < index.length - 1)
                    idx = index[++p];
            }
            else{
                for (int j = 0; j < A[0].length; j++) {
                    B[c][j] = A[i][j];
                }
                c++;
            }
        }
        return B;
    }
    
    /**
     * Convert vector to matrix.
     * @param vector Vector.
     * @param m Size of rows.
     * @param n Size of cols.
     * @return Matrix.
     */
    public static double[][] Reshape(double[] vector, int m, int n){
        
        if(vector.length != m*n)
            throw new IllegalArgumentException("The size of vector must be the same of product of m and n.");
        
        int x = 0;
        double[][] result = new double[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                result[i][j] = vector[x++];
            }
        }
        
        return result;
        
    }
    
    /**
     * Convert vector to matrix.
     * @param vector Vector.
     * @param m Size of rows.
     * @param n Size of cols.
     * @return Matrix.
     */
    public static int[][] Reshape(int[] vector, int m, int n){
        
        if(vector.length != m*n)
            throw new IllegalArgumentException("The size of vector must be the same of product of m and n.");
        
        int x = 0;
        int[][] result = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                result[i][j] = vector[x++];
            }
        }
        
        return result;
        
    }
    
    /**
     * Convert vector to matrix.
     * @param vector Vector.
     * @param m Size of rows.
     * @param n Size of cols.
     * @return Matrix.
     */
    public static float[][] Reshape(float[] vector, int m, int n){
        
        if(vector.length != m*n)
            throw new IllegalArgumentException("The size of vector must be the same of product of m and n.");
        
        int x = 0;
        float[][] result = new float[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                result[i][j] = vector[x++];
            }
        }
        
        return result;
        
    }
    
    /**
     * Convert matrix to vector.
     * @param A Matrix.
     * @return Vector.
     */
    public static double[] Reshape(double[][] A){
        
        double[] vector = new double[A.length * A[0].length];
        int x = 0;
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                vector[x++] = A[i][j];
            }
        }
        
        return vector;
    }
    
    /**
     * Convert matrix to vector.
     * @param A Matrix.
     * @return Vector.
     */
    public static int[] Reshape(int[][] A){
        
        int[] vector = new int[A.length * A[0].length];
        int x = 0;
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                vector[x++] = A[i][j];
            }
        }
        
        return vector;
    }
    
    /**
     * Convert matrix to vector.
     * @param A Matrix.
     * @return Vector.
     */
    public static float[] Reshape(float[][] A){
        
        float[] vector = new float[A.length * A[0].length];
        int x = 0;
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                vector[x++] = A[i][j];
            }
        }
        
        return vector;
    }
    
    public static double[][] SubMatrix(double[][] data, int rows, int cols){
        double[][] m = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                m[i][j] = data[i][j];
            }
        }
        return m;
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
    
    /**
     * Get the submatrix from the matrix.
     * @param data Original data.
     * @param startRow Start row.
     * @param endRow End row.
     * @param startColumn Start Column.
     * @param endColumn End Column.
     * @return Submatrix.
     */
    public static double[][] Submatrix(double[][] data, int startRow, int endRow, int startColumn, int endColumn){
        if ((startRow > endRow) || (startRow < 0) || (startRow >= data.length)
            || (endRow < 0) || (endRow >= data.length)){
            throw new IllegalArgumentException("Argument out of range.");
        }

        double[][] X = new double[endRow - startRow + 1][endColumn - startColumn + 1];

        for (int i = startRow; i <= endRow; i++){
            for (int j = startColumn; j <= endColumn; j++){

                X[i - startRow][j - startColumn] = data[i][j];
            }
        }

        return X;
    }
    
    /**
     * Get the submatrix from the matrix.
     * @param data Original data.
     * @param startRow Start row.
     * @param endRow End row.
     * @param startColumn Start Column.
     * @param endColumn End Column.
     * @return Submatrix.
     */
    public static int[][] Submatrix(int[][] data, int startRow, int endRow, int startColumn, int endColumn){
        if ((startRow > endRow) || (startRow < 0) || (startRow >= data.length)
            || (endRow < 0) || (endRow >= data.length)){
            throw new IllegalArgumentException("Argument out of range.");
        }

        int[][] X = new int[endRow - startRow + 1][endColumn - startColumn + 1];

        for (int i = startRow; i <= endRow; i++){
            for (int j = startColumn; j <= endColumn; j++){

                X[i - startRow][j - startColumn] = data[i][j];
            }
        }

        return X;
    }
    
    /**
     * Get the submatrix from the matrix.
     * @param data Original data.
     * @param startRow Start row.
     * @param endRow End row.
     * @param startColumn Start Column.
     * @param endColumn End Column.
     * @return Submatrix.
     */
    public static float[][] Submatrix(float[][] data, int startRow, int endRow, int startColumn, int endColumn){
        if ((startRow > endRow) || (startRow < 0) || (startRow >= data.length)
            || (endRow < 0) || (endRow >= data.length)){
            throw new IllegalArgumentException("Argument out of range.");
        }

        float[][] X = new float[endRow - startRow + 1][endColumn - startColumn + 1];

        for (int i = startRow; i <= endRow; i++){
            for (int j = startColumn; j <= endColumn; j++){

                X[i - startRow][j - startColumn] = data[i][j];
            }
        }

        return X;
    }
    
    /**
     * Get the submatrix from the matrix.
     * @param data Original data.
     * @param startRow Start row.
     * @param endRow End row.
     * @param columnIndexes Column indexes.
     * @return Submatrix.
     */
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
    
    /**
     * Get the submatrix from the matrix.
     * @param data Original data.
     * @param startRow Start row.
     * @param endRow End row.
     * @param columnIndexes Column indexes.
     * @return Submatrix.
     */
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
    
    /**
     * Get the submatrix from the matrix.
     * @param data Original data.
     * @param startRow Start row.
     * @param endRow End row.
     * @param columnIndexes Column indexes.
     * @return Submatrix.
     */
    public static float[][] Submatrix(float[][] data, int startRow, int endRow, int[] columnIndexes){
        if ((startRow > endRow) || (startRow < 0) || (startRow >= data.length)
            || (endRow < 0) || (endRow >= data.length)){
            throw new IllegalArgumentException("Argument out of range.");
        }

        if (columnIndexes == null)
            columnIndexes = Indices(0, data[0].length);

        float[][] X = new float[endRow - startRow + 1][columnIndexes.length];

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
    
    /**
     * Get the submatrix from the matrix.
     * @param data Original matrix.
     * @param rowIndexes Row indexes.
     * @return Submatrix.
     */
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
    
    /**
     * Get the submatrix from the matrix.
     * @param data Original matrix.
     * @param rowIndexes Row indexes.
     * @return Submatrix.
     */
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
     * Get the submatrix from the matrix.
     * @param data Original matrix.
     * @param rowIndexes Row indexes.
     * @param startColumn Initial column index.
     * @param endColumn Final column index.
     * @return Submatrix.
     */
    public static int[][] Submatrix(int[][] data, int[] rowIndexes, int startColumn, int endColumn){
        int[][] X = new int[rowIndexes.length][endColumn-startColumn+1];

        for (int i = 0; i < X.length; i++)
        {
            for (int j = 0; j < X[0].length; j++)
            {
                if ((rowIndexes[i] < 0) || (rowIndexes[i] >= data.length))
                    throw new IllegalArgumentException("Argument out of range.");

                X[i][j] = data[rowIndexes[i]][startColumn+j];
            }
        }

        return X;
    }
    
    /**
     * Get the submatrix from the matrix.
     * @param data Original matrix.
     * @param rowIndexes Row indexes.
     * @param startColumn Initial column index.
     * @param endColumn Final column index.
     * @return Submatrix.
     */
    public static double[][] Submatrix(double[][] data, int[] rowIndexes, int startColumn, int endColumn){
        double[][] X = new double[rowIndexes.length][endColumn-startColumn+1];

        for (int i = 0; i < X.length; i++)
        {
            for (int j = 0; j < X[0].length; j++)
            {
                if ((rowIndexes[i] < 0) || (rowIndexes[i] >= data.length))
                    throw new IllegalArgumentException("Argument out of range.");

                X[i][j] = data[rowIndexes[i]][startColumn+j];
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
    public static double[] toDoubleArray(double[][] A){
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
     * Convert List of array to Matrix.
     * @param list List of array.
     * @return Matrix.
     */
    public static double[][] toDoubleMatrix(List<double[]> list){
        double[][] m = new double[list.size()][list.get(0).length];
        for (int i = 0; i < m.length; i++) {
            m[i] = list.get(i);
        }
        
        return m;
    }
    
    /**
     * Convert Matrix to Vector.
     * @param A Matrix.
     * @return Vector.
     */
    public static int[] toIntArray(int[][] A){
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
     * Convert List of array to Matrix.
     * @param list List of array.
     * @return Matrix.
     */
    public static int[][] toIntMatrix(List<int[]> list){
        int[][] m = new int[list.size()][list.get(0).length];
        for (int i = 0; i < m.length; i++) {
            m[i] = list.get(i);
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
    public static float[] toFloatArray(float[][] A){
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
     * Convert List of array to Matrix.
     * @param list List of array.
     * @return Matrix.
     */
    public static float[][] toFloatMatrix(List<float[]> list){
        float[][] m = new float[list.size()][list.get(0).length];
        for (int i = 0; i < m.length; i++) {
            m[i] = list.get(i);
        }
        
        return m;
    }
}