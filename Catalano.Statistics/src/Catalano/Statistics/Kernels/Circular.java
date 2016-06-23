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
 * Circular Kernel.
 * 
 * The circular kernel comes from a statistics perspective. It is an example
 * of an isotropic stationary kernel and is positive definite in R^2.
 * 
 * @author Diego Catalano
 */
public class Circular implements IMercerKernel<double[]> {
    
    private final double c2dPI = 2.0 / Math.PI;
    private double sigma;

    /**
     * Gets the kernel's sigma value.
     * @return Sigma value.
     */
    public double getSigma() {
        return sigma;
    }

    /**
     * Sets the kernel's sigma value.
     * @param sigma Sigma value.
     */
    public void setSigma(double sigma) {
        this.sigma = sigma;
    }

    /**
     * Constructs a new Circular Kernel.
     * @param sigma Value for sigma.
     */
    public Circular(double sigma) {
        this.sigma = sigma;
    }

    @Override
    public double Function(double[] x, double[] y) {
            double norm = 0.0, a, b, c;
            for (int i = 0; i < x.length; i++)
            {
                double d = x[i] - y[i];
                norm += d * d;
            }

            norm = Math.sqrt(norm);

            if (norm >= sigma)
            {
                return 0;
            }
            else
            {
                norm = norm / sigma;
                a = c2dPI * Math.acos(-norm);
                b = c2dPI * norm;
                c = 1.0 - norm * norm;

                return a - b * Math.sqrt(c);
            }
    }
}