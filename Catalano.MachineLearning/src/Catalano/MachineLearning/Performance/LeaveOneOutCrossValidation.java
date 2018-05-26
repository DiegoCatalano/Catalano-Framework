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
import Catalano.MachineLearning.Dataset.DatasetRegression;
import Catalano.MachineLearning.Regression.IRegression;
import Catalano.MachineLearning.Regression.RegressionMeasure;
import Catalano.Math.Matrix;

/**
 * Leave One Out cross validation.
 * Leave one out for validation and the rest for train.
 * 
 * @author Diego Catalano
 */
public class LeaveOneOutCrossValidation implements IValidation, IRegressionValidation{

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

    @Override
    public RegressionMeasure Run(IRegression regression, DatasetRegression dataset) {
        return Run(regression, dataset.getInput(), dataset.getOutput());
    }

    @Override
    public RegressionMeasure Run(IRegression regression, double[][] input, double[] output) {
        double[] predicted = new double[input.length];
        for (int i = 0; i < input.length; i++) {
            double[][] tempInput = Matrix.RemoveRow(input, i);
            double[] tempOutput = Matrix.RemoveColumn(output, i);
            
            regression.Learn(tempInput, tempOutput);
            predicted[i] = regression.Predict(input[i]);
        }
        
        double mae = RegressionMeasure.MeanAbsoluteError(output, predicted);
        double mse = RegressionMeasure.MeanSquaredError(output, predicted);
        double rmse = Math.sqrt(mse);
        double coef = RegressionMeasure.CoefficientOfDetermination(output, predicted);
        double mda = RegressionMeasure.MeanDirectionalAccuracy(output, predicted);
        
        return new RegressionMeasure(mae, mse, rmse, coef, mda);
    }
}