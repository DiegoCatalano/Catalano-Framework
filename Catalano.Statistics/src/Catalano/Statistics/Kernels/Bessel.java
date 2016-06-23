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
 * Bessel Kernel.
 * The Bessel kernel is well known in the theory of function spaces of fractional smoothness.
 * @author Diego Catalano
 */
public class Bessel implements IMercerKernel<double[]>{
    
    private int order;
    private double sigma;

    /**
     * Gets the order of the Bessel function.
     * @return Order of the bessel function.
     */
    public int getOrder() {
        return order;
    }

    /**
     * Sets the order of the Bessel function.
     * @param order Order of the bessel function.
     */
    public void setOrder(int order) {
        this.order = order;
    }

    /**
     * Gets the sigma constant for this kernel.
     * @return Sigma.
     */
    public double getSigma() {
        return sigma;
    }

    /**
     * Sets the sigma constant for this kernel.
     * @param sigma Sigma.
     */
    public void setSigma(double sigma) {
        this.sigma = sigma;
    }

    /**
     * Constructs a new Bessel Kernel.
     * @param order The order for the Bessel function.
     * @param sigma The value for sigma.
     */
    public Bessel(int order, double sigma) {
        this.order = order;
        this.sigma = sigma;
    }

    @Override
    public double Function(double[] x, double[] y) {
            double norm = 0.0;

            for (int k = 0; k < x.length; k++)
            {
                double d = x[k] - y[k];
                norm += d * d;
            }
            norm = Math.sqrt(norm);

            return Catalano.Math.Functions.Bessel.J(order, sigma * norm) /
                Math.pow(norm, -norm * order);
    }
    
}