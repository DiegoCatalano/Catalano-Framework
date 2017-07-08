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

package Catalano.MachineLearning.Dataset.Imputation;

import Catalano.MachineLearning.Dataset.DecisionVariable;
import Catalano.MachineLearning.Dataset.IDataset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Fill missing values in the dataset.
 * Support: Continuous values.
 * 
 * @author Diego Catalano
 */
public class FillMissingValues implements IImputation{
    
    public static enum Mode{
        
        /**
         * Mean.
         */
        Mean,
        
        /**
         * Median.
         */
        Median
    };
    
    private Mode mode;

    /**
     * Initializes a new instance of the FillMissingValues class.
     */
    public FillMissingValues() {
        this(Mode.Mean);
    }

    /**
     * Initializes a new instance of the FillMissingValues class.
     * @param mode Mode.
     */
    public FillMissingValues(Mode mode) {
        this.mode = mode;
    }
    
    @Override
    public void ApplyInPlace(IDataset dataset){
        
        DecisionVariable[] dv = dataset.getDecisionVariables();
        
        double[][] input = (double[][])dataset.getInput();
        for (int j = 0; j < dv.length; j++) {
            if(dv[j].type == DecisionVariable.Type.Continuous){
                
                double value = 0;
                if(mode == Mode.Mean){
                    double max = 0;
                    for (int i = 0; i < input.length; i++) {
                        if(!Double.isNaN(input[i][j])){
                            value += input[i][j];
                            max++;
                        }
                    }
                    value /= max;
                }
                else if(mode == Mode.Median){
                    List<Double> elem = new ArrayList<Double>();
                    for (int i = 0; i < input.length; i++) {
                        if(!Double.isNaN(input[i][j]))
                            elem.add(input[i][j]);
                    }
                    Collections.sort(elem);
                    value = elem.get(elem.size()/2);
                }
                
                for (int i = 0; i < input.length; i++)
                    if(input[i][j] == Double.NaN) input[i][j] = value;
                
            }
        }
    }
}