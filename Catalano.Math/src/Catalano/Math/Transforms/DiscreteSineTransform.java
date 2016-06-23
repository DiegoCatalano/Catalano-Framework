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

package Catalano.Math.Transforms;

/**
 * Discrete Sine Transform.
 * The sine transform (DST) is a Fourier-related transform similar to the discrete Fourier transform (DFT), but using a purely real matrix.
 * Unlike the other sinusoidal transforms, the DST is most conveniently  computed for N = 2^p - 1, where p is an integer.
 * DSTs are widely employed in solving partial differential equations by spectral methods, where the different variants of the DST correspond to slightly different odd/even boundary conditions at the two ends of the array.
 * @see Digital Image Processing - Kenneth R. Castleman - Chapter 13 - p.288
 * @author Diego Catalano
 */
public class DiscreteSineTransform {

    /**
     * Don't let anyone instantiate this class.
     */
    private DiscreteSineTransform() {}
    
    /**
     * 1-D Forward Discrete Sine Transform.
     * @param data Data.
     */
    public static void Forward(double[] data){
        Forward(data, 1.0);
    }
    
    private static void Forward(double[] data, double inverse){
        double[] result = new double[data.length];
        
        for (int k = 1; k < result.length + 1; k++) {
            double sum = 0;
            for (int i = 1; i < data.length + 1; i++) {
                sum += data[i - 1] * Math.sin(Math.PI * ((k * i) / (data.length + 1.0)));
            }
            result[k - 1] = sum * inverse;
        }
        
        for (int i = 0; i < data.length; i++) {
            data[i] = result[i];
        }
    }
    
    /**
     * 1-D Backward Discrete Sine Transform.
     * @param data Data.
     */
    public static void Backward(double[] data){
        double inverse = 2.0 / (data.length + 1);
        Forward(data, inverse);
    }
    
    /**
     * 2-D Forward Discrete Sine Transform.
     * @param data Data.
     */
    public static void Forward(double[][] data){
        Forward(data, 1.0);
    }
    
    private static void Forward(double[][] data, double inverse){
        int rows = data.length;
        int cols = data[0].length;
        
        double[] row = new double[cols];
        double[] col = new double[rows];
        
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < row.length; j++)
                row[j] = data[i][j];

            Forward(row, inverse);

            for (int j = 0; j < row.length; j++)
                data[i][j] = row[j];
        }

        for (int j = 0; j < cols; j++)
        {
            for (int i = 0; i < col.length; i++)
                col[i] = data[i][j];

            Forward(col, inverse);

            for (int i = 0; i < col.length; i++)
                data[i][j] = col[i];
        }
    }
    
    /**
     * 2-D Backward Discrete Sine Transform.
     * @param data Data.
     */
    public static void Backward(double[][] data) {
        double inverse = 2.0 / (data.length + 1);
        Forward(data, inverse);
    }
}
