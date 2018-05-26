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

import Catalano.MachineLearning.Dataset.DatasetClassification;
import Catalano.MachineLearning.Classification.IClassifier;
import Catalano.MachineLearning.Performance.IValidation;
import java.util.List;

/**
 * Benchmark classifier.
 * @author Diego Catalano
 */
public class BenchmarkClassifier {
    
    private final double[][] input;
    private final int[] output;
    
    private IClassifier bestClassifier;
    private double[] rank;

    /**
     * Get the interface of the best classifier.
     * @return Classifier.
     */
    public IClassifier getBestClassifier() {
        return bestClassifier;
    }

    /**
     * Get the rank of classifiers from the validation method.
     * @return Rank of classifiers.
     */
    public double[] getRank() {
        return rank;
    }
    
    /**
     * Initializes a new instance of the BenchmarkClassifier class.
     * @param dataset Dataset.
     */
    public BenchmarkClassifier(DatasetClassification dataset){
        this(dataset.getInput(), dataset.getOutput());
    }

    /**
     * Initializes a new instance of the BenchmarkClassifier class.
     * @param input Data train.
     * @param output Data output.
     */
    public BenchmarkClassifier(double[][] input, int[] output) {
        this.input = input;
        this.output = output;
    }
    
    /**
     * Run the benchmark.
     * @param classifiers List of classifiers.
     * @param validation Validation method.
     */
    public void Compute(List<IClassifier> classifiers, IValidation validation){
        
        if(classifiers.size() < 1)
            throw new IllegalArgumentException("Need at least one more classifiers.");
        
        //Initialize rank.
        rank = new double[classifiers.size()];
        
        double bestC = 0;
        for (int i = 0; i < classifiers.size(); i++) {
            IClassifier c = classifiers.get(i);
            rank[i] = validation.Run(c, input, output);
            if(rank[i] > bestC){
                bestC = rank[i];
                bestClassifier = c;
            }
        }
    }   
}