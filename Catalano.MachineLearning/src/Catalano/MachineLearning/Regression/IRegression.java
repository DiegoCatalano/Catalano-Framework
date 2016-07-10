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

package Catalano.MachineLearning.Regression;

import Catalano.MachineLearning.Dataset.DatasetRegression;

/**
 * The interface describe the predict of the regression.
 * @author Diego Catalano
 */
public interface IRegression extends Cloneable{
    
    /**
     * Learn.
     * @param dataset Dataset regression.
     */
    public void Learn(DatasetRegression dataset);
    
    /**
     * Learn.
     * @param input Input.
     * @param output Output.
     */
    public void Learn(double[][] input, double[] output);
    
    /**
     * Predict.
     * @param feature Feature.
     * @return Value.
     */
    public double Predict(double[] feature);
    
    /**
     * Clone of the object.
     * @return A new copy of the object.
     */
    public IRegression clone();
}