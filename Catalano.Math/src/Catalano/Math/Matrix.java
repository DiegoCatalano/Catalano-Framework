// Catalano Math Library
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

package Catalano.Math;

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