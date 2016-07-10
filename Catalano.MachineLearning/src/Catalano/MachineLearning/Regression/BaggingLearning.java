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
import Catalano.Math.Matrix;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Bagging Learning (Bootstrap aggregation).
 * 
 * Bootstrap aggregating, also called bagging, is a machine learning ensemble meta-algorithm designed
 * to improve the stability and accuracy of machine learning algorithms used in statistical classification and regression.
 * 
 * @author Diego Catalano
 */
public class BaggingLearning implements IRegression{
    
    private int times;
    private List<IRegression> regressions;
    private IRegression regression;
    private boolean includeAttributes;
    
    /**
     * Check if features are drawn with replacement.
     * @return True, if the feature are included, otherwise false.
     */
    public boolean isIncludeAttributes() {
        return includeAttributes;
    }

    /**
     * Set attributes in the bagging model.
     * @param includeAttributes True, if the feature are included, otherwise false.
     */
    public void setIncludeAttributes(boolean includeAttributes) {
        this.includeAttributes = includeAttributes;
    }

    /**
     * Initializes a new instance of the BaggingLearning class.
     * @param regression Regression.
     */
    public BaggingLearning(IRegression regression) {
        this(regression, 11);
    }

    /**
     * Initializes a new instance of the BaggingLearning class.
     * @param regression Regression.
     * @param times Times to train.
     */
    public BaggingLearning(IRegression regression, int times) {
        this(regression, times, false);
    }
    
    /**
     * Initializes a new instance of the BaggingLearning class.
     * @param regression regression.
     * @param times Times to train.
     * @param includeAttributes Include attributes in the bagging process.
     */
    public BaggingLearning(IRegression regression, int times, boolean includeAttributes) {
        this.regression = regression;
        this.regressions = new ArrayList<IRegression>(times);
        this.times = times;
        this.includeAttributes = includeAttributes;
    }
    
    @Override
    public void Learn(DatasetRegression dataset) {
        Learn(dataset.getInput(), dataset.getOutput());
    }
    
    @Override
    public void Learn(double[][] input, double[] output){
        
        for (int i = 0; i < times; i++) {
            
            //Create random trainning sample
            Random r = new Random();
            int[] index = new int[input.length];
            for (int j = 0; j < index.length; j++) {
                index[j] = r.nextInt(input.length);
            }
            
            double[][] train = Matrix.getRows(input, index);
            double[] outputTrain = Matrix.getColumns(output, index);
            
            //Create random features subspace
            if(includeAttributes){
                index = new int[input[0].length];
                for (int j = 0; j < index.length; j++) {
                    index[j] = r.nextInt(input[0].length);
                }
                
                train = Matrix.getColumns(train, index);
            }
            
            regression.Learn(train, outputTrain);
            regressions.add(regression.clone());
        }
    }
    
    @Override
    public double Predict(double[] sample){
        double[] map = new double[regressions.size()];
        for (int i = 0; i < map.length; i++) {
            map[i] = regressions.get(i).Predict(sample);
        }
        
        return Catalano.Statistics.Tools.Mean(map);
    }
    
    @Override
    public IRegression clone() {
        try {
            return (IRegression)super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new IllegalArgumentException("Clone not supported: " + ex.getMessage());
        }
    }
}