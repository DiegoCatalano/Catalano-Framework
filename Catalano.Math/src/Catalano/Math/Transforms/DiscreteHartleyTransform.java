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
 * Hartley Transform.
 * In 1942, Hartley introduced a continuous integral transform as an alternative to the Fourier Transform.
 * @see Digital Image Processing - Kenneth R. Castleman - Chapter 13 - p.289 (2-D)
 * @see Poularikas A.D. "The Hartley Transform" - p.15 (1-D)
 * @link http://dsp-book.narod.ru/HFTSP/8579ch14.pdf
 * @author Diego Catalano
 */
public final class DiscreteHartleyTransform {
    
    /**
     * Don't let anyone instantiate this class.
     */
    private DiscreteHartleyTransform() {}
    
    /**
     * 1-D Forward Discrete Hartley Transform.
     * @param data Data.
     */
    public static void Forward(double[] data){
        double[] result = new double[data.length];
        
        for (int k = 0; k < result.length; k++) {
            double sum = 0;
            for (int n = 0; n < data.length; n++) {
                double theta = ((2.0 * Math.PI) / data.length) * k * n;
                sum += data[n] * cas(theta);
            }
            result[k] = (1.0 / Math.sqrt(data.length)) * sum;
        }
        
        for (int i = 0; i < result.length; i++) {
            data[i] = result[i];
        }
        
    }
    
    /**
     * 1-D Backward Discrete Hartley Transform.
     * @param data Data.
     */
    public static void Backward(double[] data){
        Forward(data);
    }
    
    /**
     * 2-D Forward Discrete Hartley Transform.
     * @param data Data.
     */
    public static void Forward(double[][] data){
        double[][] result = new double[data.length][data[0].length];
        
        for (int m = 0; m < data.length; m++) {
            for (int n = 0; n < data[0].length; n++) {
                double sum = 0;
                for (int i = 0; i < result.length; i++) {
                    for (int k = 0; k < data.length; k++) {
                        sum += data[i][k] * cas(((2.0 * Math.PI) / data.length) * (i * m + k * n));
                    }
                    result[m][n] = (1.0 / data.length) * sum;
                }
            }
        }
        
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                data[i][j] = result[i][j];
            }
        }
    }
    
    /**
     * 2-D Forward Discrete Hartley Transform.
     * @param data Data.
     */
    public static void Backward(double[][] data){
        Forward(data);
    }
    
    /**
     * Basis function.
     * The cas can be computed following two ways.
     * 1: Math.cos(theta) + Math.sin(theta)
     * 2: sqrt(2) * Math.cos(theta - Math.PI / 4)
     * @param theta Theta.
     * @return Result.
     */
    private static double cas(double theta){
        //return Math.cos(theta) + Math.sin(theta);
        
        // sqrt(2) = 1.4142135623730950488016887242097
        // PI / 4 = 0.78539816339744830961566084581988
        return 1.4142135623730950488016887242097 * Math.cos(theta - 0.78539816339744830961566084581988);
    }
    
}
