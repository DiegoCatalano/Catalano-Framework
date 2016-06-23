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
 * Discrete Cosine Transform.
 * The cosine transform, like the Fourier Transform, uses sinusoidal basis function. The difference is
 * that the cosine transform basis functions are not complex; they use only cosine functions, and not sine functions.
 * 
 * @see Computer Imaging - Scott E Umbaugh. Chapter 5, p. 220.
 * @author Diego Catalano
 */
public final class DiscreteCosineTransform {

    /**
     * Don't let anyone instantiate this class.
     */
    private DiscreteCosineTransform() {}
    
    /**
     * 1-D Forward Discrete Cosine Transform.
     * @param data Data.
     */
    public static void Forward(double[] data){
        
        double[] result = new double[data.length];
        double sum;
        double scale = Math.sqrt(2.0 / data.length);
        for (int f = 0; f < data.length; f++) {
            sum = 0;
            for (int t = 0; t < data.length; t++) {
                double cos =  Math.cos(((2.0 * t + 1.0) * f * Math.PI) / (2.0 * data.length));
                sum += data[t] * cos * alpha(f);
            }
            result[f] = scale * sum;
        }
        for (int i = 0; i < data.length; i++) {
            data[i] = result[i];
        }
    }
    
    /**
     * 2-D Forward Discrete Cosine Transform.
     * @param data Data.
     */
    public static void Forward(double[][] data) {
        int rows = data.length;
        int cols = data[0].length;
        
        double[] row = new double[cols];
        double[] col = new double[rows];
        
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < row.length; j++)
                row[j] = data[i][j];

            Forward(row);

            for (int j = 0; j < row.length; j++)
                data[i][j] = row[j];
        }

        for (int j = 0; j < cols; j++)
        {
            for (int i = 0; i < col.length; i++)
                col[i] = data[i][j];

            Forward(col);

            for (int i = 0; i < col.length; i++)
                data[i][j] = col[i];
        }
    }
    
    /**
     * 1-D Backward Discrete Cosine Transform.
     * @param data Data.
     */
    public static void Backward(double[] data){
        
        double[] result = new double[data.length];
        double sum;
        double scale = Math.sqrt(2.0 / data.length);
        for (int t = 0; t < data.length; t++) {
            sum = 0;
            for (int j = 0; j < data.length; j++) {
                double cos = Math.cos(((2 * t + 1) * j * Math.PI) / (2 * data.length));
                sum += alpha(j) * data[j] * cos;
            }
            result[t] = scale * sum;
        }
        for (int i = 0; i < data.length; i++) {
            data[i] = result[i];
        }
    }
    
    /**
     * 2-D Backward Discrete Cosine Transform.
     * @param data Data.
     */
    public static void Backward(double[][] data) {
        int rows = data.length;
        int cols = data[0].length;
        
        double[] row = new double[cols];
        double[] col = new double[rows];
        
        for (int j = 0; j < cols; j++){
            for (int i = 0; i < row.length; i++)
                col[i] = data[i][j];

            Backward(col);

            for (int i = 0; i < col.length; i++)
                data[i][j] = col[i];
        }

        for (int i = 0; i < rows; i++){
            for (int j = 0; j < row.length; j++)
                row[j] = data[i][j];

            Backward(row);

            for (int j = 0; j < row.length; j++)
                data[i][j] = row[j];
        }
    }
    
    private static double alpha(double v){
        if (v == 0) {
            return 1 / Math.sqrt(2);
        }
        return 1;
    }
}