// Catalano Statistics Library
// The Catalano Framework
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

import Catalano.Math.Matrix;

public class Additive implements IMercerKernel<double[]>{
    
    private IMercerKernel[] kernels;
    private double[] weights;

    /**
     * Constructs a new Additive kernel.
     * @param kernels Kernels.
     */
    public Additive(IMercerKernel[] kernels){
        this(kernels, Matrix.CreateMatrix1D(kernels.length, 1D));
    }

    /**
     * Constructs a new Additive kernel.
     * @param kernels Kernels.
     * @param weights Weights.
     */
    public Additive(IMercerKernel[] kernels, double[] weights){
        this.kernels = kernels;
        this.weights = weights;
    }

    /**
     * Linear kernel function.
     * @param x Vector <c>x</c> in input space.
     * @param y Vector <c>y</c> in input space.
     * @return Dot product in feature (kernel) space.
     */
    public double Function(double[] x, double[] y){
        double sum = 0;
        for (int i = 0; i < kernels.length; i++)
            sum += weights[i] * kernels[i].Function(x, y);

        return sum;
    }
}