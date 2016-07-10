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

package Catalano.MachineLearning.Dataset.Filters;

import Catalano.MachineLearning.Dataset.DecisionVariable;
import Catalano.MachineLearning.Dataset.IDataset;
import Catalano.MachineLearning.Dataset.StatisticsDataset;

/**
 * Fill missing values in the dataset.
 * @author Diego Catalano
 */
public class FillMissingValues implements IDatasetFilter{
    
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
    public void Apply(IDataset dataset){
        
        StatisticsDataset[] stat = dataset.getStatistics();
        DecisionVariable[] dv = dataset.getDecisionVariables();
        
        double[][] input = (double[][])dataset.getInput();
        for (int i = 0; i < dv.length; i++) {
            if(dv[i].type == DecisionVariable.Type.Continuous){
                if(stat[i].isMissingValues()){
                    double value = 0;
                    if(mode == Mode.Mean)
                        value = stat[i].getMean();
                    else if(mode == Mode.Median)
                        value = stat[i].getMedian();
                    for (int b = 0; b < input[0].length; b++) {
                        for (int a = 0; a < input.length; a++) {
                            if(input[a][b] == Double.NaN)
                                input[a][b] = value;
                        }
                    }
                }
            }
        }
    }
}