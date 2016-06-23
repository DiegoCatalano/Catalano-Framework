// Catalano Math Library
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

package Catalano.Math;

/**
 * Padding the matrix.
 * @author Diego Catalano
 */
public class PaddingMatrix {
    
    private int rows;
    private int cols;
    private double value;
    private boolean replicate;

    /**
     * Initializes a new instance of the PaddingMatrix class.
     * @param rows Number of rows.
     * @param cols Number of columns.
     */
    public PaddingMatrix(int rows, int cols) {
        this(rows, cols, 0);
    }
    
    /**
     * Initializes a new instance of the PaddingMatrix class.
     * @param rows Number of rows.
     * @param cols Number of columns.
     * @param value Default value to fill.
     */
    public PaddingMatrix(int rows, int cols, double value){
        this.rows = rows;
        this.cols = cols;
        this.value = value;
    }
    
    /**
     * Initializes a new instance of the PaddingMatrix class.
     * @param rows Number of rows.
     * @param cols Number of columns.
     * @param replicate Replicate the elements of the border.
     */
    public PaddingMatrix(int rows, int cols, boolean replicate){
        this.rows = rows;
        this.cols = cols;
        this.replicate = replicate;
    }
    
    /**
     * Create a new matrix with the padding values.
     * @param matrix Matrix to be padded.
     * @return New matrix.
     */
    public double[][] Create(double[][] matrix){
        
        double[][] result;
        if(replicate == true){
            result = Matrix.CreateMatrix2D(matrix.length+2*rows, matrix[0].length+2*cols, value);
            
            
            for (int i = 0; i < result.length; i++) {
                int r = i - rows;
                for (int j = 0; j < result[0].length; j++) {
                    int c = j - cols;
                    if(r >= 0 && r < matrix.length && c >= 0 && c < matrix[0].length){
                        result[i][j] = matrix[r][c];
                    }
                    else{
                        int rr = r;
                        int cc = c;

                        if (rr < 0) rr = 0;
                        if (rr >= matrix.length) rr = matrix.length - 1;

                        if (cc < 0) cc = 0;
                        if (cc >= matrix[0].length) cc = matrix[0].length - 1;
                        
                        result[i][j] = matrix[rr][cc];
                    }
                }
            }
            return result;
        }
        else{
            result = Matrix.CreateMatrix2D(matrix.length+2*rows, matrix[0].length+2*cols, value);
            for (int i = 0; i < matrix.length; i++)
                System.arraycopy(matrix[i], 0, result[i+rows], cols, matrix[0].length);
            
            return result;
        }
    }
}