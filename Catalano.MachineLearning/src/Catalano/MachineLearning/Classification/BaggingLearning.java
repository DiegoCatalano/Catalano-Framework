// Catalano Machine Learning Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2016
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

package Catalano.MachineLearning.Classification;

import Catalano.MachineLearning.DatasetClassification;
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
public class BaggingLearning implements IClassifier{
    
    private int times;
    private List<IClassifier> classifiers;
    private IClassifier classifier;
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
     * @param classifier Classifier.
     */
    public BaggingLearning(IClassifier classifier) {
        this(classifier, 11);
    }
    
    /**
     * Initializes a new instance of the BaggingLearning class.
     * @param classifier Classifier.
     * @param times Times to train.
     */
    public BaggingLearning(IClassifier classifier, int times){
        this(classifier, times, false);
    }

    /**
     * Initializes a new instance of the BaggingLearning class.
     * @param classifier Classifier.
     * @param times Times to train.
     * @param includeAttributes Include attributes in the bagging process.
     */
    public BaggingLearning(IClassifier classifier, int times, boolean includeAttributes) {
        this.classifier = classifier;
        this.classifiers = new ArrayList<IClassifier>(times);
        this.times = times;
        this.includeAttributes = includeAttributes;
    }

    @Override
    public void Learn(DatasetClassification dataset) {
        Learn(dataset.getInput(), dataset.getOutput());
    }
    
    @Override
    public void Learn(double[][] input, int[] labels){
        
        for (int i = 0; i < times; i++) {
            
            //Create random trainning sample
            Random r = new Random();
            int[] index = new int[input.length];
            for (int j = 0; j < index.length; j++) {
                index[j] = r.nextInt(input.length);
            }
            
            double[][] train = Matrix.getRows(input, index);
            int[] labelsTrain = Matrix.getColumns(labels, index);
            
            //Create random features subspace
            if(includeAttributes){
                index = new int[input[0].length];
                for (int j = 0; j < index.length; j++) {
                    index[j] = r.nextInt(input[0].length);
                }
                
                train = Matrix.getColumns(train, index);
            }
            
            classifier.Learn(train, labelsTrain);
            classifiers.add(classifier.clone());
        }
    }
    
    @Override
    public int Predict(double[] sample){
        int[] map = new int[classifiers.size()];
        for (int i = 0; i < map.length; i++) {
            map[i] = classifiers.get(i).Predict(sample);
        }
        
        return Catalano.Statistics.Tools.Mode(map);
    }

    @Override
    public IClassifier clone() {
        try {
            return (IClassifier)super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new IllegalArgumentException("Clone not supported: " + ex.getMessage());
        }
    }
}