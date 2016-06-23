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
 * Cauchy Kernel.
 * 
 * <para>The Cauchy kernel comes from the Cauchy distribution (Basak, 2008). It is a
 * long-tailed kernel and can be used to give long-range influence and sensitivity
 * over the high dimension space.</para>
 * 
 * @author Diego Catalano
 */
public class Cauchy implements IMercerKernel<double[]> {
    
    private double sigma;

    /**
     * Get the kernel's sigma value.
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
     * Constructs a new Cauchy Kernel.
     * Default sigma = 1.
     */
    public Cauchy(){
        this.sigma = 1;
    }

    /**
     * Constructs a new Cauchy Kernel.
     * @param sigma The value for sigma.
     */
    public Cauchy(double sigma) {
        this.sigma = sigma;
    }

    @Override
    public double Function(double[] x, double[] y) {
        // Optimization in case x and y are
        // exactly the same object reference.
        if (x == y) return 1.0;

        double norm = 0.0, d;
        for (int i = 0; i < x.length; i++)
        {
            d = x[i] - y[i];
            norm += d * d;
        }

        return (1.0 / (1.0 + norm / sigma));
    }
}