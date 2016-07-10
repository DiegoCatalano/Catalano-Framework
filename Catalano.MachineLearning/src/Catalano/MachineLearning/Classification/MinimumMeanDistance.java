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

package Catalano.MachineLearning.Classification;

import Catalano.MachineLearning.Dataset.DatasetClassification;
import Catalano.MachineLearning.Dataset.IDataset;
import Catalano.Math.Distances.IDivergence;
import Catalano.Math.Distances.SquaredEuclideanDistance;
import Catalano.Math.Matrix;

/**
 * Minimum Mean Distance classifier.
 * @author Diego Catalano
 */
public class MinimumMeanDistance implements IClassifier{
    
    private IDivergence divergence;
    private double means[][];

    /**
     * Initialize a new instance of the MinimumMeanDistance class.
     */
    public MinimumMeanDistance() {
        this(new SquaredEuclideanDistance());
    }

    /**
     * Initialize a new instance of the MinimumMeanDistance class.
     * @param divergence Divergence.
     */
    public MinimumMeanDistance(IDivergence divergence) {
        this.divergence = divergence;
    }

    @Override
    public void Learn(DatasetClassification dataset) {
        Learn(dataset.getInput(), dataset.getOutput());
    }

    @Override
    public void Learn(double[][] input, int[] output) {
        
        int classes = Matrix.Max(output) + 1;
        this.means = new double[classes][input[0].length];
        
        //Count labels of the groups.
        int[] groups = CountGroups(output, classes);
        
        //Sum the values in the respective class / attribute. 
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[0].length; j++) {
                means[output[i]][j] += input[i][j];
            }
        }
        
        //Mean
        for (int i = 0; i < means.length; i++) {
            for (int j = 0; j < means[0].length; j++) {
                means[i][j] = means[i][j] / groups[i];
            }
        }
    }

    @Override
    public int Predict(double[] feature) {
        double[] distance = new double[means.length];
        for (int i = 0; i < distance.length; i++) {
            distance[i] = divergence.Compute(means[i], feature);
        }
        return Matrix.MinIndex(distance);
    }
    
    @Override
    public IClassifier clone() {
        try {
            return (IClassifier)super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new IllegalArgumentException("Clone not supported: " + ex.getMessage());
        }
    }
    
    private int[] CountGroups(int[] labels, int classes){
        int[] groups = new int[classes];
        for (int i = 0; i < labels.length; i++) {
            groups[labels[i]]++;
        }
        return groups;
    }
}