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
 * Spherical Kernel.
 * The spherical kernel comes from a statistics perspective. It is an example of an isotropic stationary kernel and is positive definite in R^3.
 * @author Diego Catalano
 */
public class Spherical implements IMercerKernel<double[]>{
    
    private double sigma;

    /**
     * Get the kernel's sigma value.
     * @return The kernel's sigma value.
     */
    public double getSigma() {
        return sigma;
    }

    /**
     * Set the kernel's sigma value.
     * @param value The kernel's sigma value.
     */
    public void setDegree(double value) {
        this.sigma = value;
    }

    /**
     * Constructs a new Spherical kernel.
     */
    public Spherical() {}

    /**
     * Constructs a new Spherical kernel.
     * @param sigma Sigma value.
     */
    public Spherical(double sigma) {
        this.sigma = sigma;
    }

    @Override
    public double Function(double[] x, double[] y) {
        double norm = 0.0;
        for (int i = 0; i < x.length; i++){
            double d = x[i] - y[i];
            norm += d * d;
        }

        norm = Math.sqrt(norm);

        if (norm >= sigma){
            return 0;
        }
        else{
            norm = norm / sigma;
            return 1.0 - 1.5 * norm + 0.5 * norm * norm * norm;
        }
    }
}