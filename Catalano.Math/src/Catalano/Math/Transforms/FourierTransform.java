// Catalano Imaging Learning Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2013
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

package Catalano.Math.Transforms;

import Catalano.Math.ComplexNumber;

/**
 * Fourier transformation.
 * <br /> The class implements one dimensional and two dimensional Discrete and Fast Fourier Transformation.
 * @author Diego Catalano
 */
public class FourierTransform {

    /**
     * Transformation direction.
     */
    public enum Direction {

        /**
         * Forward direction of Fourier transformation.
         */
        Forward,
        /**
         * Backward direction of Fourier transformation.
         */
        Backward
    };
    
    /**
     * 1-D Discrete Fourier Transform.
     * @param data Data to transform.
     * @param direction Transformation direction.
     */
    public static void DFT(ComplexNumber[] data, Direction direction){
        int n = data.length;
        double arg, cos, sin;
        ComplexNumber[] c = new ComplexNumber[n];
        int d = 1;
        if (direction == Direction.Backward) {
            d = -1;
        }
        
        
        // for each destination element
        for ( int i = 0; i < n; i++ ){
                c[i] = new ComplexNumber(0, 0);

                arg = - (int) d * 2.0 * Math.PI * (double) i / (double) n;

                // sum source elements
                for ( int j = 0; j < n; j++ ){
                        cos = Math.cos( j * arg );
                        sin = Math.sin( j * arg );
                        
                        if (data[j] == null) {
                            data[j] = new ComplexNumber(0, 0);
                        }

                        c[i].real += ( data[j].real * cos - data[j].imaginary * sin );
                        c[i].imaginary += ( data[j].real * sin + data[j].imaginary * cos );
                }
        }

        // copy elements
        if ( direction == Direction.Forward ){
                // devide also for forward transform
                for ( int i = 0; i < n; i++ ){
                        data[i].real = c[i].real / n;
                        data[i].imaginary = c[i].imaginary / n;
                }
        }
        else{
                for ( int i = 0; i < n; i++ ){
                        data[i].real = c[i].real;
                        data[i].imaginary = c[i].imaginary;
                }
        }
    }
    
    /**
     * 2-D Discrete Fourier Transform.
     * @param data Data to transform.
     * @param width Width of matrix.
     * @param height Height of matrix.
     * @param direction Transformation direction.
     */
    public static void DFT2(ComplexNumber[][] data, Direction direction){
        
        int n = data.length;
        int m = data[0].length;
        double arg, cos, sin;
        ComplexNumber[] c = new ComplexNumber[Math.max(m, n)];
        int d = 1;
        if (direction == Direction.Backward) {
            d = -1;
        }
        
        // process rows
        for ( int i = 0; i < n; i++ ){
            for ( int j = 0; j < m; j++ ){
                c[j] = new ComplexNumber(0, 0);

                arg = - (int) d * 2.0 * Math.PI * (double) j / (double) m;

                // sum source elements
                for ( int k = 0; k < m; k++ ){
                    
                    if (data[i][k] == null) {
                        data[i][k] = new ComplexNumber(0, 0);
                    }
                    
                    cos = Math.cos( k * arg );
                    sin = Math.sin( k * arg );

                    c[j].real += ( data[i][k].real * cos - data[i][k].imaginary * sin );
                    c[j].imaginary += ( data[i][k].real * sin + data[i][k].imaginary * cos );
                }
            }

            // copy elements
            if ( direction == Direction.Forward ){
                // devide also for forward transform
                for ( int j = 0; j < m; j++ ){
                    data[i][j].real = c[j].real / m;
                    data[i][j].imaginary = c[j].imaginary / m;
                }
            }
            else{
                for ( int j = 0; j < m; j++ ){
                    data[i][j].real = c[j].real;
                    data[i][j].imaginary = c[j].imaginary;
                }
            }
        }
        
        // process columns
        for ( int j = 0; j < m; j++ ){
            for ( int i = 0; i < n; i++ ){
                c[i] = new ComplexNumber(0, 0);

                arg = - (int) d * 2.0 * Math.PI * (double) i / (double) n;

                // sum source elements
                for ( int k = 0; k < n; k++ ){
                    cos = Math.cos( k * arg );
                    sin = Math.sin( k * arg );

                    c[i].real += ( data[k][j].real * cos - data[k][j].imaginary * sin );
                    c[i].imaginary += ( data[k][j].real * sin + data[k][j].imaginary * cos );
                }
            }

            // copy elements
            if ( direction == Direction.Forward ){
                // devide also for forward transform
                for ( int i = 0; i < n; i++ ){
                    data[i][j].real = c[i].real / n;
                    data[i][j].imaginary = c[i].imaginary / n;
                }
            }
            else{
                for ( int i = 0; i < n; i++ ){
                    data[i][j].real = c[i].real;
                    data[i][j].imaginary = c[i].imaginary;
                }
            }
        }
    }
}