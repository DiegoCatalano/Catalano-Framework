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

package Catalano.MachineLearning.Regression;

import Catalano.MachineLearning.DatasetRegression;
import Catalano.Math.Distances.IDivergence;
import Catalano.Math.Distances.SquaredEuclideanDistance;
import Catalano.Math.Matrix;
import Catalano.Statistics.Kernels.IMercerKernel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * K Nearest Neighbors for regression.
 * @author Diego Catalano
 */
public class KNearestNeighbors implements IRegression, Serializable{
    
    private int k = 3;
    private double[][] input;
    private double[] output;
    private IDivergence divergence;
    private IMercerKernel kernel;

    /**
     * Get number of neighbors.
     * @return Number of neighbors.
     */
    public int getK() {
        return k;
    }

    /**
     * Set number of neighbors.
     * @param k Number of neighbors.
     */
    public void setK(int k) {
        this.k = k;
    }

    /**
     * Initializes a new instance of the KNearestNeighbors class.
     */
    public KNearestNeighbors() {
        this(3);
    }
    
    /**
     * Initializes a new instance of the KNearestNeighbors class.
     * @param k Number of neighbors.
     */
    public KNearestNeighbors(int k) {
        this(k, new SquaredEuclideanDistance());
    }
    
    /**
     * Initializes a new instance of the KNearestNeighbors class.
     * @param k Number of neighbors.
     * @param divergence Divergence.
     */
    public KNearestNeighbors(int k, IDivergence divergence) {
        this.k = k;
        this.divergence = divergence;
    }
    
    /**
     * Initializes a new instance of the KNearestNeighbors class.
     * @param k Number of neighbors.
     * @param kernel Kernel.
     */
    public KNearestNeighbors(int k, IMercerKernel kernel) {
        this.k = k;
        this.kernel = kernel;
    }
    
    @Override
    public void Learn(DatasetRegression dataset) {
        Learn(dataset.getInput(), dataset.getOutput());
    }

    @Override
    public void Learn(double[][] input, double[] output) {
        this.input = input;
        this.output = output;
    }
    
    @Override
    public double Predict(double[] feature){
        double[] dist = new double[input.length];
        if(kernel == null)
            for (int i = 0; i < input.length; i++){
                double[] temp = input[i];
                //temp = Matrix.RemoveColumn(temp, temp.length - 1);
                dist[i] = divergence.Compute(temp, feature);
            }
        else
            for (int i = 0; i < input.length; i++){
                double[] temp = input[i];
                //temp = Matrix.RemoveColumn(temp, temp.length - 1);
                dist[i] = kernel.Function(temp, feature);
            }
        
        //Sort indexes based on score
        int[] indexes = Matrix.Indices(0, dist.length);
        List<Score> lst = new ArrayList<Score>(dist.length);
        for (int i = 0; i < dist.length; i++) {
            lst.add(new Score(dist[i], indexes[i]));
        }
        
        Collections.sort(lst);
        
        double result = 0;
        for (int i = 0; i < k; i++) {
            result += output[lst.get(i).index];
        }
        
        return result / (double)k;
    }
    
    @Override
    public IRegression clone() {
        try {
            return (IRegression)super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new IllegalArgumentException("Clone not supported: " + ex.getMessage());
        }
    }
    
    private class Score implements Comparable<Score> {
        double score;
        int index;

        public Score(double score, int index) {
            this.score = score;
            this.index = index;
        }

        @Override
        public int compareTo(Score o) {
            return score < o.score ? -1 : score > o.score ? 1 : 0;
        }
    }
}