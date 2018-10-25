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

/**
 * Logarithm Normalization.
 * 
 * sign(x) * log10(abs(x)+1)
 * 
 * @author Diego Catalano
 */
public class LogarithmNormalization implements IFeatureScaling{

    /**
     * Initializes a new instance of the LogarithmNormalization class.
     */
    public LogarithmNormalization() {}

    @Override
    public double[][] Apply(double[][] data) {
        return Apply(null, data);
    }

    @Override
    public double[][] Apply(DecisionVariable[] variables, double[][] data) {
        
        double[][] m = new double[data.length][data[0].length];
        
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                if(variables[j].type == DecisionVariable.Type.Continuous)
                    m[i][j] = Math.signum(data[i][j]) * Math.log10(Math.abs(data[i][j] + 1));
                else
                    m[i][j] = data[i][j];
            }
        }
        
        return m;
        
    }

    @Override
    public void ApplyInPlace(double[][] data) {
        ApplyInPlace(null, data);
    }

    @Override
    public void ApplyInPlace(DecisionVariable[] variables, double[][] data) {
        
        if(variables == null){
            variables = new DecisionVariable[data[0].length + 1];
            for (int i = 0; i < variables.length - 1; i++) {
                variables[i] = new DecisionVariable("F" + i, DecisionVariable.Type.Continuous);
            }
            variables[variables.length - 1] = new DecisionVariable("Class", DecisionVariable.Type.Discrete);
        }
        
        //Apply
        for (int i = 0; i < data.length; i++)
            for (int j = 0; j < data[0].length; j++) 
                if(variables[j].type == DecisionVariable.Type.Continuous)
                    data[i][j] = Math.signum(data[i][j]) * Math.log10(Math.abs(data[i][j] + 1));
        
    }

    @Override
    public double[] Compute(double[] feature) {
        
        double[] v = new double[feature.length];
        for (int i = 0; i < v.length; i++)
            v[i] = Math.signum(feature[i]) * Math.log10(Math.abs(feature[i] + 1));
        
        return v;
    }

    @Override
    public double[] Compute(DecisionVariable[] variables, double[] feature) {
        double[] v = new double[feature.length];
        for (int i = 0; i < v.length; i++) {
            if(variables[i].type == DecisionVariable.Type.Continuous)
                v[i] = Math.signum(feature[i]) * Math.log10(Math.abs(feature[i] + 1));
            else
                v[i] = feature[i];
        }
        return v;
    }
}