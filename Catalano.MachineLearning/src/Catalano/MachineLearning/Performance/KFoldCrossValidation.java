// Catalano Machine Learning Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2018
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

import Catalano.Core.ArraysUtil;
import Catalano.MachineLearning.Classification.IClassifier;
import Catalano.MachineLearning.Dataset.DatasetClassification;
import Catalano.Math.Matrix;

/**
 * KFold Cross Validation.
 * @author Diego Catalano
 */
public class KFoldCrossValidation implements IValidation{
    
    private int nFolds;
    private boolean shuffle;
    private long seed;
    
    /**
     * Get number of the folds.
     * @return Number of the folds.
     */
    public int getNumberOfFolds(){
        return nFolds;
    }
    
    /**
     * Set number of the folds.
     * @param folds Folds.
     */
    public void setNumberOfFolds(int folds){
        Math.max(folds, 2);
    }

    /**
     * Checks if the indexes are going to be shuffled.
     * @return True if the indexes are going to be shuffled, othwerwise return false.
     */
    public boolean isShuffle() {
        return shuffle;
    }

    /**
     * Set true if the indexes are going to be shuffled.
     * @param shuffle Indexes are going to be shuffled.
     */
    public void setShuffle(boolean shuffle) {
        this.shuffle = shuffle;
    }

    /**
     * Initializes a new instance of the KFoldCrossValidation class.
     */
    public KFoldCrossValidation() {
        this(10);
    }

    /**
     * Initializes a new instance of the KFoldCrossValidation class.
     * @param nFolds Number of folds.
     */
    public KFoldCrossValidation(int nFolds) {
        this(nFolds, true);
    }

    /**
     * Initializes a new instance of the KFoldCrossValidation class.
     * @param nFolds Number of folds.
     * @param shuffle Shuffle the indexes.
     */
    public KFoldCrossValidation(int nFolds, boolean shuffle) {
        this(nFolds, shuffle, -1);
    }
    
    /**
     * Initializes a new instance of the KFoldCrossValidation class.
     * @param nFolds Number of folds.
     * @param shuffle Shuffle the indexes.
     * @param seed Random seed generator.
     */
    public KFoldCrossValidation(int nFolds, boolean shuffle, long seed){
        this.nFolds = nFolds;
        this.shuffle = shuffle;
        this.seed = seed;
    }

    @Override
    public double Run(IClassifier classifier, DatasetClassification dataset) {
        return Run(classifier, dataset.getInput(), dataset.getOutput());
    }

    @Override
    public double Run(IClassifier classifier, double[][] data, int[] labels) {
        
        if(nFolds > data.length)
            throw new IllegalArgumentException("The number of folds must be less or equal than number of samples.");
        
        int parts;
        double p = (labels.length / (double)nFolds) - labels.length / nFolds;
        if(p > 0.5)
            parts = (labels.length / nFolds) + 1;
        else
            parts = (labels.length / nFolds);
        
        int start = 0;
        int end = 0;
        
        int[] indexes = Matrix.Indices(0, labels.length);
        if(shuffle && seed == 0){
            ArraysUtil.Shuffle(indexes);
        }
        else if(shuffle && seed != 0){
            ArraysUtil.Shuffle(indexes, seed);
        }    
        
        double mean = 0;
        for (int i = 0; i < nFolds; i++) {
            
            //The last fold, we need to select all the rest.
            if(i == nFolds - 1){
                end = indexes.length;
            }
            else{
                end += parts;
            }
            
            //Get the indexes for train/test
            int[] ind = Matrix.getColumns(indexes, Matrix.Indices(start, end));
            
            //Train data
            double[][] inputTrain = Matrix.RemoveRows(data, ind);
            int[] outputTrain = Matrix.RemoveColumns(labels, ind);
            
            //Test data
            double[][] inputTest = Matrix.getRows(data, Matrix.Indices(start, end));
            int[] outputTest = Matrix.getRows(labels, Matrix.Indices(start, end));
            
            classifier.Learn(inputTrain, outputTrain);
            
            SuppliedValidation sv = new SuppliedValidation();
            mean += sv.Run(classifier, inputTest, outputTest);
            
            start = end;
            
        }
        
        return mean/nFolds;
        
    }
    
}
