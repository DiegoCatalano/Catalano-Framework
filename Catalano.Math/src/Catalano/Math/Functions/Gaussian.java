// Catalano Math Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
// Copyright © Andrew Kirillov, 2007-2008
// andrew.kirillov at gmail.com
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

package Catalano.Math.Functions;

/**
 * Gaussian function.
 * <br /> The class is used to calculate 1D and 2D Gaussian functions for specified Sigma (s) value:
 * <br />
 * <code lang = "none">
 * <br /> 1-D: f(x) = exp( x * x / ( -2 * s * s ) ) / ( s * sqrt( 2 * PI ) )
 * <br />
 * <br /> 2-D: f(x, y) = exp( x * x + y * y / ( -2 * s * s ) ) / ( s * s * 2 * PI )
 * </code>
 * 
 * @author Diego Catalano
 */
public class Gaussian {
    
    private double sigma = 1.0;
    private double sqrSigma = 1.0;

    /**
     * Initializes a new instance of the Gaussian class.
     * @param sigma Sigma value.
     */
    public Gaussian(double sigma) {
        setSigma(sigma);
    }

    /**
     * Sigma value.
     * @return Sigma value.
     */
    public double getSigma() {
        return sigma;
    }

    /**
     * Sigma value.
     * <br /> Default value is set to 1. Minimum allowed value is 0.00000001.
     * @param sigma Sigma value.
     */
    public void setSigma(double sigma) {
        
        this.sigma = Math.max(0.00000001, sigma);
        this.sqrSigma = sigma*sigma;
    }
    
    /**
     * 1-D Gaussian function.
     * @param x value.
     * @return Function's value at point x.
     */
    public double Function1D(double x){
        return Math.exp( x * x / ( -2 * sqrSigma ) ) / ( Math.sqrt( 2 * Math.PI ) * sigma );
    }
    
    /**
     * 2-D Gaussian function.
     * @param x value.
     * @param y value.
     * @return Function's value at point (x,y).
     */
    public double Function2D( double x, double y ){
        return Math.exp(-(x*x + y*y) / (2 * sqrSigma));
    }
    
    /**
     * 1-D Gaussian kernel.
     * @param size Kernel size (should be odd), [3, 101].
     * @return Returns 1-D Gaussian kernel of the specified size.
     */
    public double[] Kernel1D(int size){
        if ( ( ( size % 2 ) == 0 ) || ( size < 3 ) || ( size > 101 ) ){
            try {
                throw new Exception("Wrong size");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        int r = size / 2;
        // kernel
        double[] kernel = new double[size];

        // compute kernel
        for ( int x = -r, i = 0; i < size; x++, i++ )
        {
            kernel[i] = Function1D( x );
        }

        return kernel;
    }
    
    /**
     * 2-D Gaussian kernel.
     * @param size Kernel size (should be odd), [3, 101].
     * @return Returns 2-D Gaussian kernel of specified size.
     */
    public double[][] Kernel2D(int size){
        if ( ( ( size % 2 ) == 0 ) || ( size < 3 ) || ( size > 101 ) ){
            try {
                throw new Exception("Wrong size");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        int r = size / 2;
        double[][] kernel = new double[size][size];

        // compute kernel
        double sum = 0;
        for ( int y = -r, i = 0; i < size; y++, i++ )
        {
            for ( int x = -r, j = 0; j < size; x++, j++ )
            {
                kernel[i][j] = Function2D(x, y);
                sum += kernel[i][j];
            }
        }
        
        for (int i = 0; i < kernel.length; i++) {
            for (int j = 0; j < kernel[0].length; j++) {
                kernel[i][j] /= sum;
            }
        }

        return kernel;
    }
}