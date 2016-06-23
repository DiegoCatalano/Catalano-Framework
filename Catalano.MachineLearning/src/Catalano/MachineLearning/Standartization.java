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

package Catalano.MachineLearning;

import Catalano.Core.DoubleRange;
import Catalano.Math.Matrix;
import java.util.ArrayList;
import java.util.List;

/**
 * Standartize matrix.
 * Standartize each column in the matrix.
 * x = (x - u) / s;
 * 
 * @author Diego Catalano
 */
public class Standartization {
    
    private double mean;
    private double std;
    private double[][] range;
    
    boolean find;

    public double getMean() {
        return mean;
    }

    public void setMean(double mean) {
        this.mean = mean;
    }

    public double getStd() {
        return std;
    }

    public void setStd(double std) {
        this.std = std;
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
     * Initializes a new instance of the Standartization class.
     */
    public Standartization() {
        this.find = true;
    }

    public Standartization(double mean, double standardDeviation){
        this.mean = mean;
        this.std = standardDeviation;
        this.find = false;
    }
    
    public double[][] Standartize(double[][] data){
        return Standartize(null, data);
    }
    
    /**
     * Normalize matrix.
     * @param attributes Attributes.
     * @param data Matrix.
     * @return Normalized matrix.
     */
    public double[][] Standartize(DecisionVariable[] attributes, double[][] data){
        
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
        
        if(find == true){
            for (int i = 0; i < data[0].length; i++) {
                int idx = 0;
                if(attributes[i].type == DecisionVariable.Type.Continuous){
                    double[] temp = Matrix.getColumn(data, i);
                    double _mean = Catalano.Statistics.Tools.Mean(temp);
                    double _std = Catalano.Statistics.Tools.StandartDeviation(temp, mean);
                    range[0][idx] = _mean;
                    range[1][idx] = _std;
                    idx++;
                    for (int j = 0; j < temp.length; j++) {
                        matrix[j][i] = (data[j][i] - _mean) / _std;
                    }
                }
            }
        }
        else{
            for (int i = 0; i < data[0].length; i++) {
                int idx = 0;
                if(attributes[i].type == DecisionVariable.Type.Continuous){
                    double[] temp = Matrix.getColumn(data, i);
                    range[0][idx] = mean;
                    range[1][idx] = std;
                    idx++;
                    for (int j = 0; j < temp.length; j++) {
                        matrix[j][i] = (data[j][i] - mean) / std;
                    }
                }
            }
        }
        
        return matrix;
    }
    
    /**
     * Apply range normalization in the specified feature.
     * @param coefficients Standartization coefficients.
     * @param feature Feature.
     * @return Normalized feature.
     */
    public double[] StandartizeFeature(double[][] coefficients, double[] feature){
        return StandartizeFeature(null, coefficients, feature);
    }
    
    public double[] StandartizeFeature(DecisionVariable[] attributes, double[][] coefficients, double[] feature){
        
        if(attributes == null){
            attributes = new DecisionVariable[feature.length];
            for (int i = 0; i < attributes.length; i++) {
                attributes[i] = new DecisionVariable("F" + i, DecisionVariable.Type.Continuous);
            }
        }
        
        double[] norm = new double[feature.length];
        for (int i = 0; i < norm.length; i++){
            int idx = 0;
            if(attributes[i].type == DecisionVariable.Type.Continuous){
                norm[i] = (feature[i] - coefficients[0][idx]) / coefficients[1][idx];
                idx++;
            }
        }
        
        return norm;
    }
}