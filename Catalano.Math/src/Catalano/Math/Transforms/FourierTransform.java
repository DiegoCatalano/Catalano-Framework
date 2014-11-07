// Catalano Imaging Learning Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2014
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

package Catalano.Math.Transforms;

import Catalano.Math.ComplexNumber;
import Catalano.Math.Tools;

/**
 * Fourier transformation.
 * <br /> The class implements one dimensional and two dimensional Discrete and Fast Fourier Transformation.
 * @author Diego Catalano
 */
public class FourierTransform {
    
    /**
     * Scaling expoent.
     */
    public enum Scale {
        /**
         * No scaling.
         */
        NoScaling,
        
        /**
         * Scale only in the backward (1/n). Used in Matlab.
         */
        AsymetricScaling,
    }

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
        ComplexNumber[] c = new ComplexNumber[n];
        
        // for each destination element
        for ( int i = 0; i < n; i++ ){
                c[i] = new ComplexNumber(0, 0);
                double sumRe = 0;
                double sumIm = 0;
                double phim = 2 * Math.PI * i / n;

                // sum source elements
                for ( int j = 0; j < n; j++ ){
                        double gRe = data[j].real;
                        double gIm = data[j].imaginary;
                        double cosw = Math.cos(phim * j);
                        double sinw = Math.sin(phim * j);
                        if(direction == Direction.Backward)
                            sinw = -sinw;

                        sumRe += ( gRe * cosw + data[j].imaginary * sinw );
                        sumIm += ( gIm * cosw - data[j].real * sinw );
                }
                
                c[i] = new ComplexNumber(sumRe, sumIm);
        }
        
        if(direction == Direction.Backward){
            for (int i = 0; i < c.length; i++) {
                data[i].real = c[i].real / n;
                data[i].imaginary = c[i].imaginary / n;
            }
        }
        else{
            for (int i = 0; i < c.length; i++) {
                data[i].real = c[i].real;
                data[i].imaginary = c[i].imaginary;
            }
        }
    }
    
    /**
     * 2-D Discrete Fourier Transform.
     * @param data Data to transform.
     * @param direction Transformation direction.
     */
    public static void DFT2(ComplexNumber[][] data, Direction direction){
        
        int n = data.length;
        int m = data[0].length;
        ComplexNumber[] row = new ComplexNumber[Math.max(m, n)];
        
        for ( int i = 0; i < n; i++ ){
                // copy row
                for ( int j = 0; j < n; j++ )
                        row[j] = data[i][j];
                // transform it
                FourierTransform.DFT( row, direction );
                // copy back
                for ( int j = 0; j < n; j++ )
                        data[i][j] = row[j];
        }

        // process columns
        ComplexNumber[]	col = new ComplexNumber[n];

        for ( int j = 0; j < n; j++ ){
                // copy column
                for ( int i = 0; i < n; i++ )
                        col[i] = data[i][j];
                // transform it
                FourierTransform.DFT( col, direction );
                // copy back
                for ( int i = 0; i < n; i++ )
                        data[i][j] = col[i];
        }
    }
    
    /**
     * 1-D Fast Fourier Transform.
     * @param data Data to transform.
     * @param direction Transformation direction.
     */
    public static void FFT( ComplexNumber[] data, Direction direction){
        FFT(data, direction, Scale.NoScaling);
    }
    
    /**
     * 1-D Fast Fourier Transform.
     * @param data Data to transform.
     * @param direction Transformation direction.
     * @param scale Scale expoent.
     */
    public static void FFT( ComplexNumber[] data, Direction direction, Scale scale ) {
        int n = data.length;
        int m = Tools.Log2( n );

        // reorder data first
        ReorderData( data );

        // compute FFT
        int tn = 1, tm;

        for ( int k = 1; k <= m; k++ ){
                ComplexNumber[] rotation = FourierTransform.GetComplexRotation( k, direction );

                tm = tn;
                tn <<= 1;

                for ( int i = 0; i < tm; i++ ){
                        ComplexNumber t = rotation[i];

                        for ( int even = i; even < n; even += tn )
                        {
                                int odd = even + tm;
                                ComplexNumber ce = new ComplexNumber(data[even]);
                                ComplexNumber co = new ComplexNumber(data[odd]);

                                double	tr = co.real * t.real - co.imaginary * t.imaginary;
                                double	ti = co.real * t.imaginary + co.imaginary * t.real;

                                data[even].real += tr;
                                data[even].imaginary += ti;

                                data[odd].real = ce.real - tr;
                                data[odd].imaginary = ce.imaginary - ti;
                        }
                }
        }
        
        if (scale == Scale.AsymetricScaling){
            if ( direction == Direction.Backward ) {
                for (int i = 0; i < n; i++) {
                    data[i].real /= (double) n;
                    data[i].imaginary /= (double) n;
                }
            }
        }
        
    }
    
    /**
     * 2-D Fast Fourier Transform.
     * @param data Data to transform.
     * @param direction Transformation direction.
     */
    public static void FFT2( ComplexNumber[][] data, Direction direction){
        FFT2(data, direction, Scale.NoScaling);
    }
    
    /**
     * 2-D Fast Fourier Transform.
     * @param data Data to transform.
     * @param direction Transformation direction.
     * @param scale Scale expoent.
     */
    public static void FFT2( ComplexNumber[][] data, Direction direction, Scale scale ){
        int k = data.length;
        int n = data[0].length;

        // check data size
        if (
                ( !Tools.IsPowerOf2( k ) ) ||
                ( !Tools.IsPowerOf2( n ) ) ||
                ( k < minLength ) || ( k > maxLength ) ||
                ( n < minLength ) || ( n > maxLength )
                )
        {
                throw new IllegalArgumentException( "Incorrect data length." );
        }

        // process rows
        ComplexNumber[]	row = new ComplexNumber[n];

        for ( int i = 0; i < k; i++ ){
                // copy row
                for ( int j = 0; j < n; j++ )
                        row[j] = data[i][j];
                // transform it
                FourierTransform.FFT( row, direction, scale );
                // copy back
                for ( int j = 0; j < n; j++ )
                        data[i][j] = row[j];
        }

        // process columns
        ComplexNumber[]	col = new ComplexNumber[k];

        for ( int j = 0; j < n; j++ ){
                // copy column
                for ( int i = 0; i < k; i++ )
                        col[i] = data[i][j];
                // transform it
                FourierTransform.FFT( col, direction, scale );
                // copy back
                for ( int i = 0; i < k; i++ )
                        data[i][j] = col[i];
        }
    }
    
    private static void ReorderData( ComplexNumber[] data ){
        int len = data.length;

        // check data length
        if ( ( len < minLength ) || ( len > maxLength ) || ( !Tools.IsPowerOf2( len ) ) )
                throw new IllegalArgumentException( "Incorrect data length." );

        int[] rBits = GetReversedBits( Tools.Log2( len ) );

        for ( int i = 0; i < len; i++ )
        {
                int s = rBits[i];

                if ( s > i )
                {
                        ComplexNumber t = data[i];
                        data[i] = data[s];
                        data[s] = t;
                }
        }
    }
    
    private static final int minLength	= 2;
    private static final int maxLength	= 16384;
    private static final int minBits = 1;
    private static final int maxBits = 14;
    private static int[][] reversedBits = new int[maxBits][];
    private static ComplexNumber[][][]	complexRotation = new ComplexNumber[maxBits][2][];
    
    private static int[] GetReversedBits( int numberOfBits ){
            if ( ( numberOfBits < minBits ) || ( numberOfBits > maxBits ) )
                    throw new IllegalArgumentException("Fourier out of range.");

            // check if the array is already calculated
            if ( reversedBits[numberOfBits - 1] == null ){
                    int		n = Tools.Pow2( numberOfBits );
                    int[]	rBits = new int[n];

                    // calculate the array
                    for ( int i = 0; i < n; i++ ){
                            int oldBits = i;
                            int newBits = 0;

                            for ( int j = 0; j < numberOfBits; j++ ){
                                    newBits = ( newBits << 1 ) | ( oldBits & 1 );
                                    oldBits = ( oldBits >> 1 );
                            }
                            rBits[i] = newBits;
                    }
                    reversedBits[numberOfBits - 1] = rBits;
            }
            return reversedBits[numberOfBits - 1];
    }
    
    private static ComplexNumber[] GetComplexRotation( int numberOfBits, Direction direction ){
        int directionIndex = ( direction == Direction.Forward ) ? 0 : 1;
        
        // check if the array is already calculated
        if ( complexRotation[numberOfBits - 1][directionIndex] == null ){
            int dir = direction == Direction.Forward ? 1 : -1;
            int n = 1 << ( numberOfBits - 1 );
            double uR = 1.0;
            double uI = 0.0;
            double angle = Math.PI / n * dir;
            double wR = Math.cos( angle );
            double wI = -Math.sin( angle );
            double t;
            ComplexNumber[]	rotation = new ComplexNumber[n];

            for ( int i = 0; i < n; i++ )
            {
                    rotation[i] = new ComplexNumber( uR, uI );
                    t = uR * wI + uI * wR;
                    uR = uR * wR - uI * wI;
                    uI = t;
            }

            complexRotation[numberOfBits - 1][directionIndex] = rotation;
        }
        return complexRotation[numberOfBits - 1][directionIndex];
    }
}