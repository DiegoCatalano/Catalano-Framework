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

import Catalano.MachineLearning.Classification.*;
import Catalano.MachineLearning.Dataset.DatasetClassification;
import Catalano.MachineLearning.Dataset.DatasetRegression;
import Catalano.MachineLearning.Regression.IRegression;
import Catalano.MachineLearning.Regression.RegressionMeasure;
import Catalano.Math.Matrix;
import java.util.HashMap;
import java.util.Map;

/**
 * Holdout Validation.
 * Split percentage for training and the rest for the validation.
 * @author Diego Catalano
 */
public class HoldoutValidation implements IValidation, IRegressionValidation{
    
    private float p = .66f;

    /**
     * Get Train percentage.
     * @return Train percentage.
     */
    public float getTrainPercentage() {
        return p;
    }

    /**
     * Set Train percentage.
     * @param percentage Train percentage.
     */
    public void setTrainPercetange(float percentage) {
        this.p = Math.max(0.1f, Math.min(1f, percentage));
    }

    /**
     * Initializes a new instance of the HoldoutValidation class.
     */
    public HoldoutValidation() {}
    
    /**
     * Initializes a new instance of the HoldoutValidation class.
     * @param percentage Train percentage.
     */
    public HoldoutValidation(float percentage){
        setTrainPercetange(percentage);
    }

    @Override
    public double Run(IClassifier classifier, DatasetClassification dataset) {
        return Run(classifier, dataset.getInput(), dataset.getOutput());
    }
    
    @Override
    public double Run(IClassifier classifier, double[][] data, int[] labels){
        
        //Count labels and amount.
        HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
        for (int i = 0; i < labels.length; i++) {
            if(!map.containsKey(labels[i])){
                map.put(labels[i], 1);
            }
            else{
                int t = map.get(labels[i]) + 1;
                map.put(labels[i], t);
            }
        }
        
        //Define size of training
        int[] sizeClass = new int[map.size()];
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            sizeClass[entry.getKey()] = (int)(entry.getValue() * p);
        }
        
        //Get the index of training and validation features
        int size = 0;
        for (int i = 0; i < sizeClass.length; i++) {
            size += sizeClass[i];
        }
        
        int[] indexTraining = new int[size];
        int[] indexValidation = new int[data.length - size];
        
        int idxT = 0;
        int idxV = 0;
        for (int i = 0; i < data.length; i++) {
            if(sizeClass[labels[i]] > 0){
                indexTraining[idxT++] = i;
                sizeClass[labels[i]]--;
            }
            else{
                indexValidation[idxV++] = i;
            }
        }
        
        //Build data training
        double[][] train = Matrix.getRows(data, indexTraining);
        int[] labelsTrain = Matrix.getRows(labels, indexTraining);
        
        //Set the model in the classifier
        classifier.Learn(train, labelsTrain);
        
        int pos = 0;
        for (int i = 0; i < indexValidation.length; i++) {
            int v = classifier.Predict(data[indexValidation[i]]);
            if(v == labels[indexValidation[i]]) pos++;
        }
        
        return pos / (double)indexValidation.length;
    }

    @Override
    public RegressionMeasure Run(IRegression regression, DatasetRegression dataset) {
        return Run(regression, dataset.getInput(), dataset.getOutput());
    }

    @Override
    public RegressionMeasure Run(IRegression regression, double[][] input, double[] output) {
        int size = (int)(input.length * p);
        
        //Train data
        double[][] tempInput = Matrix.Submatrix(input, 0, size, 0, input[0].length - 1);
        double[] tempOutput = Matrix.getColumns(output, 0, size);
        
        regression.Learn(tempInput, tempOutput);
        
        //Train validation
        int rest = input.length - size;
        double[] predicted = new double[rest];
        for (int i = 0; i < rest; i++) {
            predicted[i] = regression.Predict(input[size + i]);
        }
        
        //Original labels
        double[] actual = Matrix.getColumns(output, size, output.length - 1);
        
        double mae = RegressionMeasure.MeanAbsoluteError(actual, predicted);
        double mse = RegressionMeasure.MeanSquaredError(actual, predicted);
        double rmse = Math.sqrt(mse);
        double coef = RegressionMeasure.CoefficientOfDetermination(actual, predicted);
        double mda = RegressionMeasure.MeanDirectionalAccuracy(actual, predicted);
        
        return new RegressionMeasure(mae, mse, rmse, coef, mda);
    }
}