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

import Catalano.Core.ArraysUtil;
import Catalano.MachineLearning.Dataset.DatasetClassification;
import Catalano.Math.Distances.IDivergence;
import Catalano.Math.Distances.SquaredEuclideanDistance;
import Catalano.Math.Matrix;
import Catalano.Statistics.Kernels.IMercerKernel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * K Nearest Neighbour classifier.
 * @author Diego Catalano
 */
public class KNearestNeighbors implements IClassifier, Serializable {
    
    private int k;
    private double[][] input;
    private int[] output;
    private IDivergence divergence = new SquaredEuclideanDistance();
    private IMercerKernel kernel;
    private boolean useKernel = false;

    /**
     * Get number of neighbours.
     * @return Number of neighbours.
     */
    public int getK() {
        return k;
    }

    /**
     * Set number of neighbours.
     * @param k Number of neighbours.
     */
    public void setK(int k) {
        this.k = Math.max(1, k);
    }

    /**
     * Get the kernel metric.
     * @return Kernel.
     */
    public IMercerKernel getKernel() {
        return kernel;
    }

    /**
     * Set the kernel metric.
     * @param kernel Kernel.
     */
    public void setMercerKernel(IMercerKernel kernel) {
        this.kernel = kernel;
        this.useKernel = true;
    }

    /**
     * Get the divergence function.
     * @return Divergence.
     */
    public IDivergence getDistance() {
        return divergence;
    }

    /**
     * Set the divergence function.
     * @param divergence Divergence.
     */
    public void setDistance(IDivergence divergence) {
        this.divergence = divergence;
        this.useKernel = false;
    }
    
    /**
     * Initializes a new instance of the KNearestNeighbors class.
     */
    public KNearestNeighbors(){
        this(3);
    }
    
    /**
     * Initializes a new instance of the KNearestNeighbors class.
     * @param k Number of neighbors.
     */
    public KNearestNeighbors(int k){
        this(3, new SquaredEuclideanDistance());
    }
    
    /**
     * Initializes a new instance of the KNearestNeighbors class.
     * @param k Number of neighbors.
     * @param divergence Distance.
     */
    public KNearestNeighbors(int k, IDivergence divergence){
        this.k = k;
        this.divergence = divergence;
    }
    
    /**
     * Initializes a new instance of the KNearestNeighbors class.
     * @param k Number of neighbors.
     * @param kernel Kernel.
     */
    public KNearestNeighbors(int k, IMercerKernel kernel){
        this.k = k;
        this.kernel = kernel;
        this.useKernel = true;
    }

    @Override
    public void Learn(DatasetClassification dataset) {
        Learn(dataset.getInput(), dataset.getOutput());
    }
    
    @Override
    public void Learn(double[][] input, int[] output){
        this.input = input;
        this.output = output;
    }
    
    /**
     * Compute.
     * @param feature Feature to compute.
     * @return Object.
     */
    @Override
    public int Predict(double[] feature){
        
        int sizeF = input.length;
        double[] dist = new double[sizeF];
        
        //Compute distance.
        if(useKernel){
            for (int i = 0; i < sizeF; i++)
                dist[i] = this.kernel.Function(feature, input[i]);
        }else{
            for (int i = 0; i < sizeF; i++)
                dist[i] = this.divergence.Compute(feature, input[i]);
        }
        
        //If k is 1, we can retrive the object quickly.
        if(k == 1) return output[Matrix.MinIndex(dist)];
        
        //Sort indexes based on score
        int[] indexes = ArraysUtil.Argsort(dist, true);
        
        //Compute vote majority
        int classes = Matrix.Max(output) + 1;
        int[] votes = new int[classes];
        for (int i = 0; i < k; i++) {
            votes[output[indexes[i]]]++;
        }
        
        return Matrix.MaxIndex(votes);

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