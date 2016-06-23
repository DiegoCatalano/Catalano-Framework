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
 * Inverse Multiquadric Kernel.
 * The inverse multiquadric kernel is only conditionally positive definite.
 * @author Diego Catalano
 */
public class InverseMultiquadric implements IMercerKernel<double[]>{
    
    private double constant;

    /**
     * Gets the kernel's constant value.
     * @return Constant value.
     */
    public double getConstant() {
        return constant;
    }

    /**
     * Sets the kernel's constant value.
     * @param constant Constant value.
     */
    public void setConstant(double constant) {
        this.constant = constant;
    }

    /**
     * Constructs a new Inverse Multiquadric Kernel.
     */
    public InverseMultiquadric() {
        this(1);
    }

    /**
     * Constructs a new Inverse Multiquadric Kernel.
     * @param constant The constant term theta.
     */
    public InverseMultiquadric(double constant) {
        this.constant = constant;
    }

    @Override
    public double Function(double[] x, double[] y) {
        double norm = 0.0;
        for (int i = 0; i < x.length; i++)
        {
            double d = x[i] - y[i];
            norm += d * d;
        }

        return 1.0 / (norm + constant * constant);
    }
}