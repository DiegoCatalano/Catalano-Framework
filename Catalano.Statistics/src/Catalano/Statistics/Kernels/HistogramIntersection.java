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
 * Generalized Histogram Intersection Kernel.
 * 
 * The Generalized Histogram Intersection kernel is built based on the
 * Histogram Intersection Kernel for image classification but applies
 * in a much larger variety of contexts (Boughorbel, 2005).
 * 
 * @author Diego Catalano
 */
public class HistogramIntersection implements IMercerKernel<double[]>{
    
    private double alpha = 1;
    private double beta = 1;

    /**
     * Constructs a new Generalized Histogram Intersection Kernel.
     * @param alpha Vector x in input space.
     * @param beta Vector y in input space.
     */
    public HistogramIntersection(double alpha, double beta) {
        this.alpha = alpha;
        this.beta = beta;
    }

    @Override
    public double Function(double[] x, double[] y) {
        double sum = 0.0;

        for (int i = 0; i < x.length; i++)
        {
            sum += Math.min(
                Math.pow(Math.abs(x[i]), alpha),
                Math.pow(Math.abs(y[i]), beta));
        }

        return sum;
    }
}