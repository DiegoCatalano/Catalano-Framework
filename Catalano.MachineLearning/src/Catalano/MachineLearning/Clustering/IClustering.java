// Catalano Machine Learning Library
// The Catalano Framework
//
// Copyright 2015 Diego Catalano
// diego.catalano at live.com
//
// Copyright 2015 Haifeng Li
// haifeng.hli at gmail.com
//
// Based on Smile (Statistical Machine Intelligence & Learning Engine)
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package Catalano.MachineLearning.Clustering;

import Catalano.MachineLearning.Dataset.DatasetClassification;

/**
 * Clustering interface.
 * 
 * @param <T> the type of input object.
 * 
 * @author Haifeng Li
 */
public interface IClustering <T> {
    /**
     * Cluster label for outliers or noises.
     */
    public static final int OUTLIER = Integer.MAX_VALUE;
    
    /**
     * Compute the data.
     * @param dataset Dataset.
     */
    public void Compute(DatasetClassification dataset);
    
    /**
     * Compute the data.
     * @param input Data.
     */
    public void Compute(T[] input);
    
    /**
     * Cluster a new instance.
     * @param x a new instance.
     * @return the cluster label.
     */
    public int Predict(T x);
}