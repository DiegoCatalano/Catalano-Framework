// Catalano Machine Learning Library
// The Catalano Framework
//
// Copyright 2015 Diego Catalano
// diego.catalano at live.com
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

package Catalano.MachineLearning.Clustering;

import Catalano.MachineLearning.Dataset.DatasetClassification;

/**
 * Common interface for centroid based clustering algorithms.
 * @author Diego Catalano
 */
public interface ICentroidClustering {
    
    /**
     * Compute the data.
     * @param dataset Dataset.
     */
    public void Compute(DatasetClassification dataset);
    
    /**
     * Compute the data.
     * @param input Data.
     */
    public void Compute(double[][] input);
    
    /**
     * Cluster a new instance.
     * @param x a new instance.
     * @return the cluster label.
     */
    public int Predict(double[] x);
    
    /**
     * Get the centroids of the clustering.
     * @return Centroids.
     */
    public double[][] getCentroids();
}