// Catalano Math Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
// Copyright © César Souza, 2009-2014
// cesarsouza at gmail.com
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
 * Fast Hilbert Transform.
 * @author Diego Catalano
 */
public class HilbertTransform {
    
    /**
     * 1-D Fast Hilbert Transform.
     * @param data Data.
     * @param direction Direction.
     */
    public static void FHT(double[] data, FourierTransform.Direction direction){
        
        int N = data.length;

        // Forward operation
        if (direction == FourierTransform.Direction.Forward){
            
            // Copy the input to a complex array which can be processed
            //  in the complex domain by the FFT
            ComplexNumber[] cdata = new ComplexNumber[N];
            for (int i = 0; i < N; i++)
                    cdata[i] = new ComplexNumber(data[i], 0);

            // Perform FFT
            FourierTransform.FFT(cdata, FourierTransform.Direction.Forward);

            //double positive frequencies
            for (int i = 1; i < (N/2); i++)
            {
                    cdata[i].real *= 2.0;
                    cdata[i].imaginary *= 2.0;
            }

            // zero out negative frequencies
            //  (leaving out the dc component)
            for (int i = (N/2)+1; i < N; i++)
            {
                    cdata[i].real = 0.0;
                    cdata[i].imaginary = 0.0;
            }

            // Reverse the FFT
            FourierTransform.FFT(cdata, FourierTransform.Direction.Backward);

            // Convert back to our initial double array
            for (int i = 0; i < N; i++)
                    data[i] = cdata[i].imaginary;
            
            }
        // Backward operation
        else {
            // The inverse Hilbert can be calculated by
            //  negating the transform and reapplying the
            //  transformation.
            //
            // H^–1{h(t)} = –H{h(t)}

            FHT(data, FourierTransform.Direction.Forward);

            for (int i = 0; i < data.length; i++)
                data[i] = -data[i];
        }
    }
    
    /**
     * 1-D Fast Hilbert Transform.
     * @param data Data.
     * @param direction Direction.
     */
    public static void FHT(ComplexNumber[] data, FourierTransform.Direction direction){
        int N = data.length;

        // Forward operation
        if (direction == FourierTransform.Direction.Forward){
            // Makes a copy of the data so we don't lose the
            //  original information to build our final signal
            ComplexNumber[] shift = (ComplexNumber[])data.clone();

            // Perform FFT
            FourierTransform.FFT(shift, FourierTransform.Direction.Backward);

            //double positive frequencies
            for (int i = 1; i < (N/2); i++){
                    shift[i].real *= 2.0;
                    shift[i].imaginary *= 2.0;
            }

            // zero out negative frequencies
            //  (leaving out the dc component)
            for (int i = (N/2)+1; i < N; i++){
                    shift[i].real = 0.0;
                    shift[i].imaginary = 0.0;
            }

            // Reverse the FFT
            FourierTransform.FFT(shift, FourierTransform.Direction.Forward);

            // Put the Hilbert transform in the Imaginary part
            //  of the input signal, creating a Analytic Signal
            for (int i = 0; i < N; i++)
                    data[i].imaginary = shift[i].imaginary;
        }
        // Backward operation
        else{
            // Just discard the imaginary part
            for (int i = 0; i < data.length; i++)
                    data[i].imaginary = 0.0;
        }
    }
    
    /**
     * 2-D Fast Hilbert Transform.
     * @param data Data.
     * @param direction Direction.
     */
    public static void FHT2(ComplexNumber[][] data, FourierTransform.Direction direction){
        
        int n = data.length;
        int m = data[0].length;
        ComplexNumber[] row = new ComplexNumber[Math.max(m, n)];
        
        for ( int i = 0; i < n; i++ ){
                // copy row
                for ( int j = 0; j < n; j++ )
                        row[j] = data[i][j];
                // transform it
                FHT( row, direction );
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
                FHT( col, direction );
                // copy back
                for ( int i = 0; i < n; i++ )
                        data[i][j] = col[i];
        }
    }
}