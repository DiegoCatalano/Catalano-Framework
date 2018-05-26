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

package Catalano.MachineLearning.FeatureSelection;

import Catalano.MachineLearning.Classification.IClassifier;
import Catalano.MachineLearning.Performance.IValidation;
import Catalano.MachineLearning.Dataset.DatasetClassification;
import Catalano.Math.Matrix;
import java.util.ArrayList;
import java.util.List;

/**
 * Combinatorial Feature Selection.
 * @author Diego Catalano
 */
public class CombinatorialFeatureSelection {
    
    private IClassifier classifier;
    private IValidation validation;
    private double score;
    private int[] bestIndexes;

    /**
     * Get classifier.
     * @return Classifier.
     */
    public IClassifier getClassifier() {
        return classifier;
    }

    /**
     * Set classifier.
     * @param classifier Classifier.
     */
    public void setClassifier(IClassifier classifier) {
        this.classifier = classifier;
    }

    /**
     * Get validation method.
     * @return Validation.
     */
    public IValidation getValidation() {
        return validation;
    }

    /**
     * Set validation method.
     * @param validation Validation method.
     */
    public void setValidation(IValidation validation) {
        this.validation = validation;
    }
    
    /**
     * Best score.
     * @return Best score.
     */
    public double bestScore(){
        return score;
    }


    /**
     * Selected feature index.
     * @return Feature index.
     */
    public int[] getFeatureIndex() {
        return bestIndexes;
    }

    /**
     * Initializes a new instance of the CombinatorialFeatureSelection class.
     * @param classifier Classifier.
     * @param validation Validation method.
     */
    public CombinatorialFeatureSelection(IClassifier classifier, IValidation validation) {
        this.classifier = classifier;
        this.validation = validation;
    }

    /**
     * Compute.
     * @param dataset Dataset.
     */
    public void Compute(DatasetClassification dataset) {
        Compute(dataset.getInput(), dataset.getOutput());
    }

    /**
     * Compute.
     * @param input Data.
     * @param labels Labels.
     */
    public void Compute(double[][] input, int[] labels) {
        
        //Number of combinations
        int n = (int)Math.pow(2, input[0].length);
        
        score = 0;
        for (int i = 0; i < n; i++) {
            
            boolean[] usage = toBinary(i+1, input[0].length);
            int[] index = getIndex(usage);
            
            double[][] features = Matrix.getColumns(input, index);
            
            double val = validation.Run(classifier, features, labels);
            if(val > score){
                score = val;
                bestIndexes = index;
            }
//            System.out.println("Combination: " + (n-i));
//            System.out.println("Score: " + val);
//            System.out.println();
        }
        //System.out.println("Best Score: " + score);
    }
    
    private boolean[] toBinary(int number, int bits) {
        final boolean[] ret = new boolean[bits];
        for (int i = 0; i < bits; i++) {
            ret[bits - 1 - i] = (1 << i & number) != 0;
        }
        return ret;
    }
    
    private int[] getIndex(boolean[] bits){
        
        List<Integer> lst = new ArrayList<Integer>();
        for (int i = 0; i < bits.length; i++) {
            if(bits[i] == true) lst.add(new Integer(i));
        }
        
        int[] elem = new int[lst.size()];
        for (int i = 0; i < elem.length; i++) {
            elem[i] = lst.get(i);
        }
        
        return elem;
    }
    
    
}