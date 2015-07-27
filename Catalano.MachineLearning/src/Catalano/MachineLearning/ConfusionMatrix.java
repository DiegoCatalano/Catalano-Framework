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

package Catalano.MachineLearning;

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
     * @param <T> Labels type.
     * @param classifier Classifier.
     * @param validation Data validation (input).
     * @param labels Labels (output).
     * @return Confusion matrix.
     */
    public static <T> int[][] Create(IClassifier classifier, double[][] validation, T[] labels){
        HashMap<T, Integer> unique = new HashMap<T, Integer>();
        int idx = 0;
        for (int i = 0; i < labels.length; i++) {
            if(!unique.containsKey(labels[i]))
                unique.put(labels[i], idx++);
        }
        
        int[][] confusion = new int[unique.size()][unique.size()];
        
        for (int i = 0; i < validation.length; i++) {
            T obj = classifier.Predict(validation[i]);
            confusion[unique.get(labels[i])][unique.get(obj)]++;
        }
        
        return confusion;
    }
    
    //Confusion matrix.
    private int[][] confusion;

    /**
     * Initializes a new instance of the ConfusionMatrix class.
     * @param confusionMatrix ConfusionMatrix.
     */
    public ConfusionMatrix(int[][] confusionMatrix) {
        this.confusion = confusionMatrix;
        Compute(confusionMatrix);
    }
    
    private void Compute(int[][] confusionMatrix){
        
        
        
    }
    
}
