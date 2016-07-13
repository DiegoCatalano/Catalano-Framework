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
 * Standartize matrix.
 * Standartize each column in the matrix.
 * f(x) = (x - u) / s;
 * 
 * @author Diego Catalano
 */
public class Standartization implements IFeatureScaling{
    
    private double[][] range;

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
    public Standartization() {}

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
    public void ApplyInPlace(double[][] data) {
        range = new double[2][data[0].length];
        
        for (int i = 0; i < data[0].length; i++) {
            int idx = 0;
            double[] temp = Matrix.getColumn(data, i);
            double _mean = Catalano.Statistics.Tools.Mean(temp);
            double _std = Catalano.Statistics.Tools.StandartDeviation(temp, _mean);
            range[0][idx] = _mean;
            range[1][idx] = _std;
            idx++;
            for (int j = 0; j < temp.length; j++) {
                data[j][i] = (data[j][i] - _mean) / _std;
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
        
        range = new double[2][continuous];
        
        for (int i = 0; i < data[0].length; i++) {
            int idx = 0;
            if(attributes[i].type == DecisionVariable.Type.Continuous){
                double[] temp = Matrix.getColumn(data, i);
                double _mean = Catalano.Statistics.Tools.Mean(temp);
                double _std = Catalano.Statistics.Tools.StandartDeviation(temp, _mean);
                range[0][idx] = _mean;
                range[1][idx] = _std;
                idx++;
                for (int j = 0; j < temp.length; j++) {
                    data[j][i] = (data[j][i] - _mean) / _std;
                }
            }
        }
    }

    @Override
    public double[] Compute(double[] feature) {
        double[] norm = new double[feature.length];
        for (int i = 0; i < norm.length; i++){
            norm[i] = (feature[i] - range[0][i]) / range[1][i];
        }
        
        return norm;
    }

    @Override
    public double[] Compute(DecisionVariable[] variables, double[] feature) {
        double[] norm = new double[feature.length];
        for (int i = 0; i < norm.length; i++){
            int idx = 0;
            if(variables[i].type == DecisionVariable.Type.Continuous){
                norm[i] = (feature[i] - range[0][idx]) / range[1][idx];
                idx++;
            }
        }
        
        return norm;
    }
}