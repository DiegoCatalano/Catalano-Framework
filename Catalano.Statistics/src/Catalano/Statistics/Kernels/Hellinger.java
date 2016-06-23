// Catalano Statistics Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
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

package Catalano.Statistics.Kernels;

/**
 * Hellinger kernel is an euclidean norm of linear kernel.
 * @author Diego Catalano
 */
public class Hellinger implements IMercerKernel<double[]>{

    /**
     * Constructs a new Hellinger Kernel.
     */
    public Hellinger() {}

    @Override
    public double Function(double[] x, double[] y) {
        double r = 0;
        for (int i = 0; i < x.length; i++)
        {
            r += Math.sqrt(x[i] * y[i]);
        }

        return r;
    }
}