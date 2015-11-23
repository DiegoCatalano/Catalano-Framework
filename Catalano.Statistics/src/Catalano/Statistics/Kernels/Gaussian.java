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

public class Gaussian implements IMercerKernel<double[]>{
    
    private double gamma;
    
    /**
     * Gets the gamma value for the kernel.
     * @return Gamma value.
     */
    public double getGamma(){
        return gamma;
    }

    /**
     * Sets the gamma value for the kernel.
     * @param gamma Gamma value.
     */
    public void setGamma(double gamma){
            this.gamma = gamma / 100;
    }

    /**
     * Constructs a new Gaussian Kernel.
     */
    public Gaussian(){
        this(1);
    }

    /**
     * Constructs a new Gaussian Kernel.
     * @param gamma The smooth of the Gaussian Kernel.
     */
    public Gaussian(double gamma){
        setGamma(gamma);
    }

    /**
     * Gaussian Kernel function.
     * @param x Vector <c>x</c> in input space.
     * @param y Vector <c>y</c> in input space.
     * @return Dot product in feature (kernel) space.
     */
    public double Function(double[] x, double[] y){
        // Optimization in case x and y are
        // exactly the same object reference.
        if (x == y) return 1.0;

        double norm = 0.0, d;
        for (int i = 0; i < x.length; i++){
            d = x[i] - y[i];
            norm += d * d;
        }

        return Math.exp(-gamma * norm);
    }
}