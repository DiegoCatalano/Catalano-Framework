// Catalano Machine Learning Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
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

package Catalano.MachineLearning.Dataset;

import java.io.Serializable;

/**
 * Common interface to a dataset.
 * @author Diego Catalano
 * @param <T> Input data type.
 * @param <U> Output data type.
 */
public interface IDataset<T,U> extends Serializable{
    
    /**
     * Get the decision variables.
     * @return Decision Variables.
     */
    public DecisionVariable[] getDecisionVariables();
    
    /**
     * Get the statistics from the dataset.
     * @return Statistics.
     */
    public StatisticsDataset[] getStatistics();
    
    /**
     * Get the input data.
     * @return Input data.
     */
    public T getInput();
    
    /**
     * Set the input data.
     * @param input Input.
     */
    public void setInput(T input, DecisionVariable[] variables);
    
    /**
     * Get the output data.
     * @return Output data.
     */
    public U getOutput();
}