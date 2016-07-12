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

package Catalano.MachineLearning.FeatureEncoder;

import Catalano.MachineLearning.Clustering.ICentroidClustering;
import Catalano.MachineLearning.FeatureScaling.VectorNormalization;

/**
 * Vlad (Vector Locally Agreggate Descriptors).
 * @author Diego Catalano
 */
public class Vlad {
    
    private ICentroidClustering clustering;
    private double[][] centroids;
    private boolean posNormalize;
    private boolean preNormalize;
    
    /**
     * Initializes a new instance of the Vlad class.
     * @param centroids Centroids.
     */
    public Vlad(double[][] centroids){
        this(centroids, false, true);
    }

    /**
     * Initializes a new instance of the Vlad class.
     * @param centroids Centroids.
     * @param preNormalize Sqrt(vlad).
     * @param posNormalize ||x||^2 normalization.
     */
    public Vlad(double[][] centroids, boolean preNormalize, boolean posNormalize) {
        this.centroids = centroids;
        this.preNormalize = preNormalize;
        this.posNormalize = posNormalize;
    }
    
    /**
     * Initializes a new instance of the Vlad class.
     * @param clustering Clustering algorithm.
     */
    public Vlad(ICentroidClustering clustering){
        this(clustering, false, true);
    }
    
    /**
     * Initializes a new instance of the Vlad class.
     * @param clustering CLustering algorithm.
     * @param preNormalize sqrt(vlad).
     * @param posNormalize ||x||^2 normalization.
     */
    public Vlad(ICentroidClustering clustering, boolean preNormalize, boolean posNormalize){
        this.clustering = clustering;
        this.preNormalize = preNormalize;
        this.posNormalize = posNormalize;
    }
    
    /**
     * Compute VLAD.
     * @param features Features.
     * @return Descriptors.
     */
    public double[][] Compute(double[][] features){
        
        //If doesn't exists centroids, we need to compute
        if(centroids == null){
            clustering.Compute(features);
            this.centroids = clustering.getCentroids();
        }
        
        int k = centroids.length;
        int d = features[0].length;
        int D = k*d;
        
        double[][] result = new double[features.length][D];
        
        //v(i,j) = sum(xj - cij)
        int idx;
        for (int f = 0; f < features.length; f++) {
            idx = 0;
            for (int i = 0; i < k; i++) {
                for (int j = 0; j < d; j++) {
                    result[f][idx++] = features[f][j] - centroids[i][j];
                }
            }
        }
        
        //Should apply sqrt normalization before ?
        if(preNormalize){
            for (int i = 0; i < result.length; i++) {
                for (int j = 0; j < result[0].length; j++) {
                    result[i][j] = Math.sqrt(result[i][j]);
                }
            }
        }
        
        //Should apply ||x||^2 ?
        if(posNormalize){
            VectorNormalization vn = new VectorNormalization();
            vn.ApplyInPlace(result);
        }
        
        return result;
    }
    
    /**
     * Compute a specified feature.
     * @param feature Feature.
     * @return Descriptors.
     */
    public double[] ComputeFeature(double[] feature){
        double[] result = new double[feature.length * centroids.length];
        int idx;
        for (int i = 0; i < centroids.length; i++) {
            idx = 0;
            for (int j = 0; j < feature.length; j++) {
                result[idx++] = feature[j] - centroids[i][j];
            }
        }
        
        //Should apply sqrt ?
        if(preNormalize){
            for (int i = 0; i < result.length; i++) {
                result[i] = Math.sqrt(result[i]);
            }
        }
        
        //Should apply ||x||^2 ?
        if(posNormalize){
            double sum = 0;
            for (int i = 0; i < result.length; i++) {
                sum += result[i] * result[i];
            }
            sum = Math.sqrt(sum);
            for (int i = 0; i < result.length; i++) {
                result[i] /= sum;
            }
        }
        
        return result;
    }
}