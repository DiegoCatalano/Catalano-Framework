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

public class Linear implements IMercerKernel<double[]>{
    
    private double constant;

    /**
     * Constructs a new Linear kernel.
     * @param constant A constant intercept term. Default is 1.
     */
    public Linear(double constant){
        this.constant = constant;
    }

    /**
     * Constructs a new Linear Kernel.
     */
    public Linear(){
        constant = 1;
    }

    /**
     * Gets the kernel's intercept term.
     * @return Kernel's intercept term.
     */
    public double getConstant(){
        return constant;
    }

    /**
     * Sets the kernel's intercept term.
     * @param value Kernel's intercept term.
     */
    public void setConstant(double value){
        constant = value;
    }

    /**
     * Linear kernel function.
     * @param x Vector <c>x</c> in input space.
     * @param y Vector <c>y</c> in input space.
     * @return Dot product in feature (kernel) space.
     */
    public double Function(double[] x, double[] y){
        double sum = constant;
        for (int i = 0; i < x.length; i++)
            sum += x[i] * y[i];

        return sum;
    }
}