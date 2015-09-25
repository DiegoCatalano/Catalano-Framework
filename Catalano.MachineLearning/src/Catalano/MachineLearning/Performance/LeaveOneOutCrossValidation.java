// Catalano Machine Learning Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2015
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
import Catalano.MachineLearning.DatasetClassification;
import Catalano.Math.Matrix;

/**
 * Leave One Out cross validation.
 * Leave one out for validation and the rest for train.
 * 
 * @author Diego Catalano
 */
public class LeaveOneOutCrossValidation implements IValidation{

    /**
     * Initializes a new instance of the LeaveOneOutCrossValidation class.
     */
    public LeaveOneOutCrossValidation() {}

    @Override
    public double Run(IClassifier classifier, DatasetClassification dataset) {
        return Run(classifier, dataset.getInput(), dataset.getOutput());
    }

    @Override
    public double Run(IClassifier classifier, final double[][] data, final int[] labels) {
        int p = 0;
        for (int i = 0; i < data.length; i++) {
            double[][] a = Matrix.RemoveRow(data, i);
            int[] b = Matrix.RemoveColumn(labels, i);
            classifier.Learn(a, b);
            if(classifier.Predict(data[i]) == labels[i])
                p++;
        }
        
        return p / (double)data.length;
    }
}