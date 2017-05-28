// Catalano Machine Learning Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
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

package Catalano.Neuro.ActivationFunction;

/**
 * Linear activation function.
 * @author Diego Catalano
 */
public class LinearFunction implements IActivationFunction{
    
    private double c;

    public double getConstant() {
        return c;
    }

    public void setConstant(double c) {
        this.c = c;
    }

    /**
     * Initializes a new instance of the LinearFunction class.
     */
    public LinearFunction() {
        this(1);
    }

    /**
     * Initializes a new instance of the LinearFunction class.
     * @param a Constant.
     */
    public LinearFunction(double a) {
        this.c = a;
    }

    @Override
    public double Function(double x) {
        return c * x;
    }

    @Override
    public double Derivative(double x) {
        return Function(x);
    }

    @Override
    public double Derivative2(double x) {
        return c;
    }
    
}