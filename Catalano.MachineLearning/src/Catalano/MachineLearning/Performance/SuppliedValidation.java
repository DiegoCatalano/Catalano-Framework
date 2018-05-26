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

package Catalano.MachineLearning.Performance;

import Catalano.MachineLearning.Classification.IClassifier;
import Catalano.MachineLearning.Dataset.DatasetClassification;

/**
 * Supplied Validation.
 * Run test in an user specified dataset.
 * 
 * @author Diego Catalano
 */
public class SuppliedValidation implements IValidation{

    /**
     * Initializes a new instance of the SuppliedValidation class.
     */
    public SuppliedValidation() {}

    @Override
    public double Run(IClassifier classifier, DatasetClassification dataset) {
        return Run(classifier, dataset.getInput(), dataset.getOutput());
    }

    @Override
    public double Run(IClassifier classifier, double[][] data, int[] labels) {
        
        //Predict
        int pos = 0;
        for (int i = 0; i < data.length; i++) {
            if(classifier.Predict(data[i]) == labels[i])
                pos++;
        }
        return pos / (double)data.length;
    }
    
}