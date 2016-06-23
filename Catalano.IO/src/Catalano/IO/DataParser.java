// Catalano IO Library
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

package Catalano.IO;

import java.util.ArrayList;
import java.util.List;

/**
 * Data parse.
 * @author Diego Catalano
 */
public final class DataParser {

    /**
     * Don't let anyone instantiate this class.
     */
    private DataParser() {}
    
    /**
     * Convert string matrix to double matrix.
     * @param A Matrix.
     * @param startRow Initial row index.
     * @param endRow End row index.
     * @param startCol Initial column index.
     * @param endCol End column index.
     * @return Matrix.
     */
    public static double[][] toDouble(String[][] A, int startRow, int endRow, int startCol, int endCol){
        
        int rows = endRow - startRow;
        int cols = endCol - startCol;
        double[][] data = new double[rows][cols];
        
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                data[i][j] = Double.parseDouble(A[i + startRow][j + startCol]);
            }
        }
        
        return data;
        
    }
    
    /**
     * Convert string matrix to double array list.
     * @param A Matrix.
     * @param startRow Initial row index.
     * @param endRow End row index.
     * @param startCol Initial column index.
     * @param endCol End column index.
     * @return Matrix.
     */
    public static List<double[]> toDoubleList(String[][] A, int startRow, int endRow, int startCol, int endCol){
        List<double[]> lst = new ArrayList<double[]>(A.length);
        
        for (int i = 0; i < A.length; i++) {
            double[] data = new double[A[0].length];
            for (int j = 0; j < A[0].length; j++) {
                data[j] = Double.parseDouble(A[i + startRow][j + startCol]);
            }
            lst.add(data);
        }
        
        return lst;
        
    }
    
    /**
     * Convert string matrix to int matrix.
     * @param A Matrix.
     * @param startRow Initial row index.
     * @param endRow End row index.
     * @param startCol Initial column index.
     * @param endCol End column index.
     * @return Matrix.
     */
    public static int[][] toInt(String[][] A, int startRow, int endRow, int startCol, int endCol){
        
        int rows = endRow - startRow;
        int cols = endCol - startCol;
        int[][] data = new int[rows][cols];
        
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                data[i][j] = Integer.parseInt(A[i + startRow][j + startCol]);
            }
        }
        
        return data;
        
    }
    
    /**
     * Convert string matrix to int array matrix.
     * @param A Matrix.
     * @param startRow Initial row index.
     * @param endRow End row index.
     * @param startCol Initial column index.
     * @param endCol End column index.
     * @return Matrix.
     */
    public static List<int[]> toIntList(String[][] A, int startRow, int endRow, int startCol, int endCol){
        
        List<int[]> lst = new ArrayList<int[]>(A.length);
        
        for (int i = 0; i < A.length; i++) {
            int[] data = new int[A[0].length];
            for (int j = 0; j < A[0].length; j++) {
                data[j] = Integer.parseInt(A[i + startRow][j + startCol]);
            }
            lst.add(data);
        }
        
        return lst;
        
    }
    
    /**
     * Convert string matrix to float matrix.
     * @param A Matrix.
     * @param startRow Initial row index.
     * @param endRow End row index.
     * @param startCol Initial column index.
     * @param endCol End column index.
     * @return Matrix.
     */
    public static float[][] toFloat(String[][] A, int startRow, int endRow, int startCol, int endCol){
        
        int rows = endRow - startRow;
        int cols = endCol - startCol;
        float[][] data = new float[rows][cols];
        
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                data[i][j] = Float.parseFloat(A[i + startRow][j + startCol]);
            }
        }
        
        return data;
        
    }
    
    /**
     * Convert string matrix to float matrix.
     * @param A Matrix.
     * @param startRow Initial row index.
     * @param endRow End row index.
     * @param startCol Initial column index.
     * @param endCol End column index.
     * @return Matrix.
     */
    public static List<float[]> toFloatList(String[][] A, int startRow, int endRow, int startCol, int endCol){
        
        List<float[]> lst = new ArrayList<float[]>(A.length);
        
        for (int i = 0; i < A.length; i++) {
            float[] data = new float[A[0].length];
            for (int j = 0; j < A[0].length; j++) {
                data[j] = Float.parseFloat(A[i + startRow][j + startCol]);
            }
            lst.add(data);
        }
        
        return lst;
        
    }
    
}