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
import Catalano.Math.Matrix;

/**
 * Vector normalization.
 * f(x) = 1 / ||x||
 * 
 * @author Diego Catalano
 */
public class VectorNormalization implements IFeatureScaling{
    
    private int l;

    /**
     * Get the order of the normalization.
     * @return Order.
     */
    public int getOrder() {
        return l;
    }

    /**
     * Set the order of the normalization.
     * @param l Order.
     */
    public void setOrder(int l) {
        this.l = l;
    }

    /**
     * Initializes a new instance of the VectorNormalization class.
     */
    public VectorNormalization() {
        this(2);
    }

    /**
     * Initializes a new instance of the VectorNormalization class.
     * @param l Order.
     */
    public VectorNormalization(int l) {
        this.l = l;
    }

    @Override
    public double[][] Apply(double[][] data) {
        double[][] result = Matrix.Copy(data);
        ApplyInPlace(result);
        return result;
    }

    @Override
    public double[][] Apply(DecisionVariable[] variables, double[][] data) {
        double[][] result = Matrix.Copy(data);
        ApplyInPlace(variables, result);
        return result;
    }
    
    @Override
    public void ApplyInPlace(double[][] data){
        for (int i = 0; i < data.length; i++) {
            double sum = 0;
            for (int j = 0; j < data[0].length; j++) {
                sum += Math.pow(Math.abs(data[i][j]),l);

            }
            sum = Math.pow(sum, 1D/l);
            for (int j = 0; j < data[0].length; j++) {
                data[i][j] /= sum;
            }
        }
    }
    
    @Override
    public void ApplyInPlace(DecisionVariable[] variables, double[][] data){
        
        for (int i = 0; i < data.length; i++) {
            double sum = 0;
            for (int j = 0; j < data[0].length; j++) {
                if(variables[j].type == DecisionVariable.Type.Continuous){
                    sum += Math.pow(Math.abs(data[i][j]),l);
                }
            }
            sum = Math.pow(sum, 1D/l);
            for (int j = 0; j < data[0].length; j++) {
                if(variables[j].type == DecisionVariable.Type.Continuous){
                    data[i][j] /= sum;
                }
            }
        }
    }
    
    @Override
    public double[] Compute(double[] feature){
        double[] result = new double[feature.length];
        
        double sum = 0;
        for (int i = 0; i < feature.length; i++) {
            sum += Math.pow(Math.abs(feature[i]),l);
        }
        sum = Math.pow(sum, 1D/l);
        for (int i = 0; i < feature.length; i++) {
            result[i] = feature[i] / sum;
        }
        
        return result;
    }
    
    @Override
    public double[] Compute(DecisionVariable[] variables, double[] feature){
        double[] result = new double[feature.length];
        
        double sum = 0;
        for (int i = 0; i < feature.length; i++) {
            if(variables[i].type == DecisionVariable.Type.Continuous)
                sum += Math.pow(Math.abs(feature[i]),l);
        }
        sum = Math.pow(sum, 1D/l);
        for (int i = 0; i < feature.length; i++) {
            if(variables[i].type == DecisionVariable.Type.Continuous)
                result[i] = feature[i] / sum;
        }
        
        return result;
    }
}