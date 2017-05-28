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
 * ReLU activation function.
 * @author Diego Catalano
 */
public class ReluFunction implements IActivationFunction{

    /**
     * Initializes a new instance of the ReluFunction function.
     */
    public ReluFunction() {}

    @Override
    public double Function(double x) {
        if(x > 0)
            return x;
        return 0;
    }
    
    @Override
    public double Derivative(double x){
        if(x > 0)
            return 1;
        return 0;
    }
    
    @Override
    public double Derivative2(double x){
        if(x > 0)
            return 1;
        return 0;
    }
    
}