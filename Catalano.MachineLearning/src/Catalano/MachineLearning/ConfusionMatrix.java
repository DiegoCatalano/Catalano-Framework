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

package Catalano.MachineLearning;

import Catalano.MachineLearning.Classification.IClassifier;
import java.util.HashMap;

/**
 * Confusion matrix.
 * 
 * In the field of machine learning, a confusion matrix, also known as a contingency table or an error matrix, is a specific table layout
 * that allows visualization of the performance of an algorithm, typically a supervised learning.
 * 
 * @author Diego Catalano
 */
public class ConfusionMatrix {
    
    /**
     * Create confusion matrix.
     * @param classifier Classifier.
     * @param validation Data validation (input).
     * @param labels Labels (output).
     * @return Confusion matrix.
     */
    public static int[][] Create(IClassifier classifier, double[][] validation, int[] labels){
        HashMap<Integer, Integer> unique = new HashMap<Integer, Integer>();
        int idx = 0;
        for (int i = 0; i < labels.length; i++) {
            if(!unique.containsKey(labels[i]))
                unique.put(labels[i], idx++);
        }
        
        int[][] confusion = new int[unique.size()][unique.size()];
        
        for (int i = 0; i < validation.length; i++) {
            int obj = classifier.Predict(validation[i]);
            confusion[unique.get(labels[i])][unique.get(obj)]++;
        }
        
        return confusion;
    }
    
    //Confusion matrix.
    private int[][] confusion;
    
    private double[] tp;
    private double[] fp;
    private double[] precision;
    private double[] fMeasure;

    /**
     * Get True Positive rate.
     * @return TP rate.
     */
    public double[] getTruePositiveRate() {
        return tp;
    }
    
    /**
     * Get False Positive rate.
     * @return FP rate.
     */
    public double[] getFalsePositiveRate(){
        return fp;
    }
    
    /**
     * Get Precision.
     * @return Precision.
     */
    public double[] getPrecision(){
        return precision;
    }
    
    /**
     * Get F-Measure.
     * @return F-Measure.
     */
    public double[] getFMeasure(){
        return fMeasure;
    }

    /**
     * Initializes a new instance of the ConfusionMatrix class.
     * @param confusionMatrix ConfusionMatrix.
     */
    public ConfusionMatrix(int[][] confusionMatrix) {
        this.confusion = confusionMatrix;
        Compute(confusionMatrix);
    }
    
    private void Compute(int[][] confusionMatrix){
        
        int[] classes = new int[confusionMatrix[0].length];
        
        tp = new double[confusionMatrix[0].length];
        fp = new double[confusionMatrix[0].length];
        precision = new double[confusionMatrix[0].length];
        fMeasure = new double[confusionMatrix[0].length];
        
        
        //Compute classes
        for (int i = 0; i < confusionMatrix.length; i++) {
            int sum = 0;
            for (int j = 0; j < confusionMatrix[0].length; j++) {
                sum += confusionMatrix[i][j];
            }
            classes[i] = sum;
        }
        
        //Compute true positive / percentage correctly classified
        for (int i = 0; i < tp.length; i++) {
            tp[i] = confusionMatrix[i][i] / (double)classes[i];
        }
        
        //Compute false positive / precision
        for (int j = 0; j < confusionMatrix[0].length; j++) {
            int sum = 0;
            int sumC = 0;
            int sumP = 0;
            for (int i = 0; i < confusionMatrix.length; i++) {
                if(i != j){
                    sum += confusionMatrix[i][j];
                    sumC += classes[i];
                }
                sumP += confusionMatrix[i][j];
            }
            fp[j] = sum / (double)sumC;
            sumP = sumP == 0 ? 1 : sumP;
            precision[j] = confusionMatrix[j][j] / (double)sumP;
        }
        
        //Compute F-Measure
        for (int j = 0; j < confusionMatrix[0].length; j++) {
            int sumFP = 0;
            int sumFN = 0;
            for (int i = 0; i < confusionMatrix.length; i++) {
                if(i != j){
                    sumFP += confusionMatrix[i][j];
                }
            }
            for (int k = 0; k < confusionMatrix[0].length; k++) {
                if(k != j){
                    sumFN += confusionMatrix[j][k];
                }
            }
            fMeasure[j] = 2 * confusionMatrix[j][j] / (double)(2 * confusionMatrix[j][j] + sumFP + sumFN);
        }
    }
}