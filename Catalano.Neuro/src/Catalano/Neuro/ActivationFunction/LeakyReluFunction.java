// Catalano Machine Learning Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
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

package Catalano.Neuro.ActivationFunction;

/**
 * Leaky ReLU activation function.
 * @author Diego Catalano
 */
public class LeakyReluFunction implements IActivationFunction{
    
    private double alpha;

    /**
     * Get alpha parameter.
     * @return Parameter alpha.
     */
    public double getAlpha() {
        return alpha;
    }

    /**
     * Set alpha parameter.
     * @param alpha Alpha parameter (1, +Inf).
     */
    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    /**
     * Initializes a new instance of the LeakyReluFunction class.
     */
    public LeakyReluFunction() {
        this(0.01);
    }

    /**
     * Initializes a new instance of the LeakyReluFunction class.
     * @param alpha Parameter alpha.
     */
    public LeakyReluFunction(double alpha) {
        setAlpha(alpha);
    }

    @Override
    public double Function(double x) {
        if(x < 0)
            return alpha * x;
        return x;
    }
    
    @Override
    public double Derivative(double x){
        if(x < 0)
            return alpha;
        return 1;
    }

    @Override
    public double Derivative2(double x) {
        return Derivative(x);
    }
    
}