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

import Catalano.MachineLearning.FeatureScaling.IFeatureScaling;
import Catalano.MachineLearning.FeatureScaling.VectorNormalization;

/**
 * Vlad (Vector Locally Agreggate Descriptors).
 * @author Diego Catalano
 */
public class Vlad implements IFeatureEncoder{
    
    private double[][] centroids;
    private boolean normalize;
    private boolean softmax;
    private IFeatureScaling scaleVlad;
    
    /**
     * Initializes a new instance of the Vlad class.
     * @param centroids Centroids.
     */
    public Vlad(double[][] centroids){
        this(centroids, true);
    }

    /**
     * Initializes a new instance of the Vlad class.
     * @param centroids Centroids.
     * @param normalize ||x||^2 normalization.
     */
    public Vlad(double[][] centroids, boolean normalize) {
        this(centroids, normalize, false);
    }
    
    /**
     * Initializes a new instance of the Vlad class.
     * @param centroids Centroids.
     * @param normalize ||x||^2 normalization.
     * @param softmax Apply softmax function.
     */
    public Vlad(double[][] centroids, boolean normalize, boolean softmax){
        this(centroids, normalize, softmax, null);
    }
    
    /**
     * Initializes a new instance of the Vlad class.
     * @param centroids Centroids.
     * @param normalize ||x||^2 normalization.
     * @param softmax Apply softmax function.
     * @param scaleVlad Scale(Vlad).
     */
    public Vlad(double[][] centroids, boolean normalize, boolean softmax, IFeatureScaling scaleVlad){
        this.centroids = centroids;
        this.normalize = normalize;
        this.scaleVlad = scaleVlad;
    }
    
    /**
     * Compute VLAD.
     * @param features Features.
     * @return Descriptors.
     */
    @Override
    public double[] Compute(double[][] features){
        
        //If doesn't exists centroids, we need to compute
        if(centroids == null)
            throw new IllegalArgumentException("Centroids cant be null");
        
        int k = centroids.length;
        int d = features[0].length;
        
        double[] result = new double[k * d];
        double[] current = new double[k * d];

        //v(i,j) = sum(xj - cij)
        for (int f = 0; f < features.length; f++) {
            int idx = 0;
            for (int c = 0; c < k; c++) {
                for (int l = 0; l < d; l++) {
                    current[idx++] = features[f][l] - centroids[c][l];
                }
            }
            
            //Should apply softmax ?
            if(softmax){
                //Euclidean distances
                double[] dist = new double[centroids.length];
                for (int j = 0; j < centroids.length; j++) {
                    dist[j] = Catalano.Math.Distances.Distance.Euclidean(features[f], centroids[j]);
                }

                //Softmax
                dist = Softmax(dist, dist);

                //Multiply by softmax of distance
                idx = 0;
                for (int c = 0; c < k; c++) {
                    for (int l = 0; l < d; l++) {
                        current[idx++] *= dist[c];
                    }
                }
            }

            //sum
            idx = 0;
            for (int i = 0; i < current.length; i++) {
                result[idx+i] += current[i];
            }
        }
        
        //Should apply feature scaling ?
        if(scaleVlad != null){
            result = scaleVlad.Compute(result);
        }
        
        //Should apply ||x||^2 ?
        if(normalize){
            VectorNormalization vn = new VectorNormalization();
            result = vn.Compute(result);
        }
        
        return result;
    }
    
    private double[] Softmax(double[] input, double[] result)
    {
        double sum = 0;
        for (int i = 0; i < input.length; i++)
        {
            double u = Math.exp(input[i]);
            result[i] = u;
            sum += u;
        }

        for (int i = 0; i < result.length; i++)
            result[i] /= sum;

        return result;
    }
}