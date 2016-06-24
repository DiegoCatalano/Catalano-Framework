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

package Catalano.MachineLearning.Classification.ELM.Functions;

/**
 * Triangular basis activation function.
 * @author Diego Catalano
 */
public class TriangularBasisFunction implements IActivationFunction{

    /**
     * Initializes a new instance of the TriangularBasisFunction class.
     */
    public TriangularBasisFunction() {}

    @Override
    public double Compute(double x) {
        if(x < 0)
            return 0;
        else if (x > 1)
            return 0;
        return 1 - Math.abs(x);
    }
    
}