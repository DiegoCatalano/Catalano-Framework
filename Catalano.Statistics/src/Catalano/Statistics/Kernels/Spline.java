// Catalano Statistics Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
// Copyright © César Souza, 2009-2013
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

package Catalano.Statistics.Kernels;

/**
 * Spline Kernel.
 * The Spline kernel is given as a piece-wise cubic polynomial, as derived in the works by Gunn (1998).
 * @author Diego Catalano
 */
public class Spline implements IMercerKernel<double[]>{

    /**
     * Constructs a new Spline kernel.
     */
    public Spline() {}

    @Override
    public double Function(double[] x, double[] y) {
        double k = 1;
        for (int i = 0; i < x.length; i++){
            double min = Math.min(x[i], y[i]);
            double xy = x[i] * y[i];

            // prod{1}^d 1 + xy + xy*min - (x+y)/2 min² + min³/3} 
            k *= 1.0 + xy + xy * min - ((x[i] + y[i]) / 2.0) * min * min + (min * min * min) / 3.0;
        }

        return k;
    }
}