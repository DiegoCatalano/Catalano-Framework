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

package Catalano.MachineLearning.FeatureScaling;

import Catalano.MachineLearning.Dataset.DecisionVariable;
import java.io.Serializable;

/**
 * Common interface to feature scaling algorithms.
 * @author Diego Catalano
 */
public interface IFeatureScaling extends Serializable{
    
    /**
     * Apply the normalization.
     * @param data Data to be normalized.
     * @return Normalized data.
     */
    public double[][] Apply(double[][] data);
    
    /**
     * Apply the normalization.
     * @param variables Decision variables.
     * @param data Data to be normalized.
     * @return Normalized. data.
     */
    public double[][] Apply(DecisionVariable[] variables, double[][] data);
    
    /**
     * Apply the normalization in place of the original data.
     * @param data Data to be normalized.
     */
    public void ApplyInPlace(double[][] data);
    
    /**
     * Apply the normalization in place of the original data.
     * @param variables Decision variables.
     * @param data Data to be normalized.
     */
    public void ApplyInPlace(DecisionVariable[] variables, double[][] data);
    
    /**
     * Normalize the feature.
     * @param feature Feature to be normalized.
     * @return Normalized feature.
     */
    public double[] Compute(double[] feature);
    
    /**
     * Normalize the feature.
     * @param variables Decision variables.
     * @param feature Feature.
     * @return Normalized feature.
     */
    public double[] Compute(DecisionVariable[] variables, double[] feature);
}