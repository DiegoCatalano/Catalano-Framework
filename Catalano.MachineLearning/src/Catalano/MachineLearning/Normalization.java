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

package Catalano.MachineLearning;

import Catalano.Math.Matrix;

/**
 * Normalization data.
 * Normalize in the specified given range.
 * 
 * @author Diego Catalano
 */
public class Normalization {
    
    private double min = 0;
    private double max = 1;
    private double[][] range;

    /**
     * Get minimum value.
     * @return Minimum value.
     */
    public double getMin() {
        return min;
    }

    /**
     * Set minimum value.
     * @param min Minimum value.
     */
    public void setMin(double min) {
        this.min = min;
    }

    /**
     * Get maximum value.
     * @return Maximum value.
     */
    public double getMax() {
        return max;
    }

    /**
     * Set maximum value.
     * @param max Maximum value.
     */
    public void setMax(double max) {
        this.max = max;
    }

    /**
     * Get range normalization.
     * @return Range normalization.
     */
    public double[][] getRangeNormalization() {
        return range;
    }

    /**
     * Set range normalization.
     * @param range Range normalization.
     */
    public void setRangeNormalization(double[][] range) {
        this.range = range;
    }

    /**
     * Initializes a new instance of the Normalization class.
     */
    public Normalization() {}
    
    /**
     * Initializes a new instance of the Normalization class.
     * @param min Minimum value.
     * @param max Maximum value.
     */
    public Normalization(double min, double max){
        this.min = min;
        this.max = max;
    }
    
    /**
     * Normalize data.
     * All the columns will be normalized.
     * @param data Data.
     * @return Normalized data.
     */
    public double[][] Normalize(double[][] data){
        return Normalize(null, data);
    }
    
    /**
     * Normalize matrix.
     * Only the continuous data will be normalized.
     * @param attributes Attributes.
     * @param data Data.
     * @return Normalized data.
     */
    public double[][] Normalize(DecisionVariable[] attributes, double[][] data){
        
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
        
        range = new double[2][continuous];
        double[][] matrix = new double[data.length][data[0].length];
        
        int idx = 0;
        for (int i = 0; i < data[0].length; i++) {
            if(attributes[i].type == DecisionVariable.Type.Continuous){
                double[] temp = Matrix.getColumn(data, i);
                double _min = Catalano.Statistics.Tools.Min(temp);
                double _max = Catalano.Statistics.Tools.Max(temp);
                range[0][idx] = _min;
                range[i][idx] = _max;
                idx++;
                for (int j = 0; j < temp.length; j++) {
                    matrix[j][i] = Catalano.Math.Tools.Scale(_min, _max, min, max, temp[j]);
                }
            }
        }
        
        return matrix;
    }
    
    /**
     * Normalize the feature for to be used in the some classifier.
     * @param normalization Normalization coefficients.
     * @param feature Feature.
     * @return Normalized feature.
     */
    public double[] NormalizeFeature(double[][] normalization, double[] feature){
        return NormalizeFeature(null, normalization, feature);
    }
    
    /**
     * Normalize the feature for to be used in the some classifier.
     * @param attributes Attributes.
     * @param normalization Normalization coefficients.
     * @param feature Feature.
     * @return Normalized feature.
     */
    public double[] NormalizeFeature(DecisionVariable[] attributes, double[][] normalization, double[] feature){
        
        if(attributes == null){
            attributes = new DecisionVariable[feature.length];
            for (int i = 0; i < attributes.length; i++) {
                attributes[i] = new DecisionVariable("F" + i, DecisionVariable.Type.Continuous);
            }
        }
        
        double[] norm = new double[feature.length];
        for (int i = 0; i < norm.length; i++) {
            int idx = 0;
            if(attributes[i].type == DecisionVariable.Type.Continuous){
                double v = Catalano.Math.Tools.Scale(normalization[0][idx], normalization[1][idx], min, max, feature[i]);
                idx++;

                v = v > 1 ? 1 : v;
                v = v < 0 ? 0 : v;
                norm[i] = v;
            }
        }
        
        return norm;
    }
}