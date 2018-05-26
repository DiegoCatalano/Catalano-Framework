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
 * Maximum Normalization.
 * Normalize the data dividing by maximum value.
 * 
 * @author Diego Catalano
 */
public class MaximumNormalization implements IFeatureScaling{
    
    private double[] range;

    /**
     * Get range normalization.
     * @return Range normalization.
     */
    public double[] getRangeNormalization() {
        return range;
    }

    /**
     * Set range normalization.
     * @param range Range normalization.
     */
    public void setRangeNormalization(double[] range) {
        this.range = range;
    }

    /**
     * Initializes a new instance of the Maximum Normalization class.
     */
    public MaximumNormalization() {}

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
        range = new double[data[0].length];
        
        int idx = 0;
        for (int i = 0; i < data[0].length; i++) {
            double[] temp = Matrix.getColumn(data, i);
            double max = Catalano.Statistics.Tools.Max(temp);
            range[idx] = max;
            idx++;
            for (int j = 0; j < temp.length; j++) {
                data[j][i] = temp[j] / max;
            }
        }
    }

    @Override
    public void ApplyInPlace(DecisionVariable[] attributes, double[][] data){
        int continuous = 0;
        if(attributes == null){
            attributes = new DecisionVariable[data[0].length + 1];
            for (int i = 0; i < attributes.length - 1; i++) {
                attributes[i] = new DecisionVariable("F" + i, DecisionVariable.Type.Continuous);
            }
            attributes[attributes.length - 1] = new DecisionVariable("Class", DecisionVariable.Type.Discrete);
            continuous += data[0].length;
        }
        else{
            for (int i = 0; i < attributes.length; i++) {
                if(attributes[i].type == DecisionVariable.Type.Continuous)
                    continuous++;
            }
        }
        
        range = new double[continuous];
        
        int idx = 0;
        for (int i = 0; i < data[0].length; i++) {
            if(attributes[i].type == DecisionVariable.Type.Continuous){
                double[] temp = Matrix.getColumn(data, i);
                double max = Catalano.Statistics.Tools.Max(temp);
                range[idx] = max;
                idx++;
                for (int j = 0; j < temp.length; j++) {
                    data[j][i] = temp[j] / max;
                }
            }
        }
    }

    @Override
    public double[] Compute(double[] feature){
        double[] norm = new double[feature.length];
        for (int i = 0; i < norm.length; i++) {
            double v = feature[i] / range[i];
            norm[i] = v;
        }
        
        return norm;
    }
    
    @Override
    public double[] Compute(DecisionVariable[] attributes, double[] feature){
        double[] norm = new double[feature.length];
        for (int i = 0; i < norm.length; i++) {
            int idx = 0;
            if(attributes[i].type == DecisionVariable.Type.Continuous){
                double v = feature[i] / range[idx];
                idx++;

                v = v > 1 ? 1 : v;
                v = v < 0 ? 0 : v;
                norm[i] = v;
            }
        }
        return norm;
    }
}