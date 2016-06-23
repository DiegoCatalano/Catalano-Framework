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
 * Logarithm Kernel.
 * The Log kernel seems to be particularly interesting for images, but is only conditionally positive definite.
 * @author Diego Catalano
 */
public class Log implements IMercerKernel<double[]>{
    
    private double degree;

    /**
     * Get the kernel's degree.
     * @return The kernel's degree.
     */
    public double getDegree() {
        return degree;
    }

    /**
     * Set the kernel's degree.
     * @param degree The kernel's degree.
     */
    public void setDegree(double degree) {
        this.degree = degree;
    }

    /**
     * Constructs a new Log kernel.
     */
    public Log() {}

    @Override
    public double Function(double[] x, double[] y) {
        double norm = 0.0, d;

        for (int k = 0; k < x.length; k++)
        {
            d = x[k] - y[k];
            norm += d * d;
        }

        return -Math.log(Math.pow(norm, degree / 2.0) + 1);
    }
}