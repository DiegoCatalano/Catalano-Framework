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

import Catalano.Core.ArraysUtil;
import Catalano.MachineLearning.Dataset.DatasetClassification;
import Catalano.Math.Matrix;

/**
 * Mean Variance feature selection.
 * @author Diego Catalano
 */
public class MeanVarianceFeatureSelection implements ISupervisionedFeatureSelection{
    
    private double[] rank;
    private int[] features;

    /**
     * Initializes a new instance of the MeanVarianceFeatureSelection class.
     */
    public MeanVarianceFeatureSelection() {}

    @Override
    public void Compute(DatasetClassification dataset) {
        Compute(dataset.getInput(), dataset.getOutput());
    }

    @Override
    public void Compute(double[][] input, int[] labels) {
        
        //Initialize the rank
        this.rank = new double[input[0].length];
        
        int n = Matrix.Max(labels) + 1;
        int[] groups = CountGroups(labels, n);
        
        //Compute means
        double[][] means = new double[n][input[0].length];
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[0].length; j++) {
                means[labels[i]][j] += input[i][j]; 
            }
        }
        
        for (int i = 0; i < means.length; i++) {
            for (int j = 0; j < means[0].length; j++) {
                means[i][j] /= groups[i];
            }
        }
        
        //Compute variances
        double[][] vars = new double[n][input[0].length];
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[0].length; j++) {
                vars[labels[i]][j] += Math.pow(input[i][j] - means[labels[i]][j],2); 
            }
        }
        
        for (int i = 0; i < vars.length; i++) {
            for (int j = 0; j < vars[0].length; j++) {
                vars[i][j] /= (groups[i] - 1);
            }
        }
        
        //Compute SE
        double[] se = new double[input[0].length];
        for (int j = 0; j < se.length; j++) {
            double t = 0;
            for (int i = 0; i < vars.length; i++) {
                t += vars[i][j] / groups[i];
                //se[i] += vars[i][j] / groups[i];
            }
            se[j] = Math.sqrt(t);
        }
        
        //Compute the rank
        double mean;
        for (int j = 0; j < means[0].length; j++) {
            mean = means[0][j];
            for (int i = 1; i < means.length; i++) {
                mean -= means[i][j];
            }
            rank[j] = Math.abs(mean) / se[j];
        }
        
        this.features = ArraysUtil.Argsort(rank, false);
        
    }

    @Override
    public int[] getFeatureIndex() {
        return features;
    }

    @Override
    public double[] getRank() {
        return rank;
    }
    
    private int[] CountGroups(int[] labels, int classes){
        int[] groups = new int[classes];
        for (int i = 0; i < labels.length; i++) {
            groups[labels[i]]++;
        }
        return groups;
    }
    
    private double[][] SplitInGroups(double[][] data, int columnIndex, int[] labels, int groups){
        double[][] newData = new double[groups][];
        
        for (int i = 0; i < data.length; i++) {
            newData[labels[i]][i] = data[i][columnIndex];
        }
        
        return newData;
    }
    
}