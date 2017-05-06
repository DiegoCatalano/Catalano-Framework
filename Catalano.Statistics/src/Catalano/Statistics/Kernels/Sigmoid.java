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

public class Sigmoid implements IMercerKernel<double[]>{
    
    private double alpha;
    private double constant;

    /**
     * Constructs a new Linear Kernel.
     */
    public Sigmoid(){
        this(0.01, -Math.E);
    }
    
    /**
     * Constructs a new Sigmoid kernel.
     * @param constant A constant intercept term. Default is 1.
     */
    public Sigmoid(double alpha, double constant){
        this.alpha = alpha;
        this.constant = constant;
    }

    /**
     * Gets the kernel's constant.
     * @return Kernel's constant.
     */
    public double getConstant(){
        return constant;
    }

    /**
     * Sets the kernel's intercept term.
     * @param constant Kernel's intercept term. Default: -e
     */
    public void setConstant(double constant){
        constant = constant;
    }

    /**
     * Gets the kernel's alpha.
     * @return Kernel's alpha.
     */
    public double getAlpha() {
        return alpha;
    }

    /**
     * Sets the kernel's alpha.
     * @param alpha Kernel's alpha. Default: 0.01
     */
    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    /**
     * Sigmoid kernel function.
     * @param x Vector <c>x</c> in input space.
     * @param y Vector <c>y</c> in input space.
     * @return Dot product in feature (kernel) space.
     */
    public double Function(double[] x, double[] y){
        double sum = 0;
        for (int i = 0; i < x.length; i++)
            sum += x[i] * y[i];

        return Math.tanh(alpha * sum + constant);
    }
}