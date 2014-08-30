// Catalano Neuro Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2013
// diego.catalano at live.com
//
// Copyright © Andrew Kirillov, 2007-2008
// andrew.kirillov at gmail.com
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

package Catalano.Neuro;

/**
 * Bipolar sigmoid activation function.
 * @author Diego Catalano
 */
public class BipolarSigmoidFunction implements IActivationFunction {
    private double alpha = 2;

    /**
     * Initializes a new instance of the BipolarSigmoidFunction class.
     */
    public BipolarSigmoidFunction() {}

    /**
     * Initializes a new instance of the BipolarSigmoidFunction class.
     * @param alpha Sigmoid's alpha value.
     */
    public BipolarSigmoidFunction(double alpha) {
        this.alpha = alpha;
    }

    /**
     * Get Sigmoid's alpha value.
     * <para>The value determines steepness of the function. Increasing value of
     * this property changes sigmoid to look more like a threshold function. Decreasing
     * value of this property makes sigmoid to be very smooth (slowly growing from its
     * minimum value to its maximum value).</para>
     * @return Alpha value.
     */
    public double getAlpha() {
        return alpha;
    }

    /**
     * Set Sigmoid's alpha value.
     * <para>The value determines steepness of the function. Increasing value of
     * this property changes sigmoid to look more like a threshold function. Decreasing
     * value of this property makes sigmoid to be very smooth (slowly growing from its
     * minimum value to its maximum value).</para>
     * @param alpha Alpha value.
     */
    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    @Override
    public double Function(double x) {
        return ((2 / (1 + Math.exp(-alpha * x))) - 1);
    }

    @Override
    public double Derivative(double x) {
        double y = Function(x);
        return (alpha * (1 - y * y) / 2);
    }

    @Override
    public double Derivative2(double y) {
        return (alpha * ( 1 - y * y ) / 2);
    }
}