// Catalano Machine Learning Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2018
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

import java.util.ArrayList;
import java.util.List;

/**
 * Aggregate Features.
 * 
 * Aggregate all the features into a unique array.
 * 
 * @author Diego Catalano
 */
public class AggregateFeatures {
    
    private List<double[]> features = new ArrayList<double[]>();

    /**
     * Initializes a new instance of the AggregateFeatures class.
     */
    public AggregateFeatures() {}

    /**
     * Initializes a new instance of the AggregateFeatures class.
     * @param features Features.
     */
    public AggregateFeatures(List<double[]> features) {
        this.features = features;
    }
    
    /**
     * Add a new feature.
     * @param feature Feature.
     */
    public void addFeature(int feature){
        addFeature(new double[] {feature});
    }
    
    /**
     * Add a new feature.
     * @param feature Feature.
     */
    public void addFeature(float feature){
        addFeature(new double[] {feature});
    }
    
    /**
     * Add a new feature.
     * @param feature Feature.
     */
    public void addFeature(double feature){
        addFeature(new double[] {feature});
    }
    
    /**
     * Add a new features.
     * @param features Features.
     */
    public void addFeature(double[] features){
        this.features.add(features);
    }
    
    /**
     * Get all features into a unique array.
     * @return Array of features.
     */
    public double[] getFeatures(){
        
        int size = 0;
        for (double[] f : features) {
            size += f.length;
        }
        
        double[] allFeatures = new double[size];
        
        int index = 0;
        for (double[] f : features) {
            for (int i = 0; i < f.length; i++) {
                allFeatures[index++] = f[i];
            }
        }
        
        return allFeatures;
    }
    
    /**
     * Clear all the features.
     */
    public void clear(){
        features.clear();
    }
    
}