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
 * Power normalization.
 * f(x) = sign(x)|x|^p
 * 
 * @author Diego Catalano
 */
public class PowerNormalization implements IFeatureScaling{
    
    private double power;

    /**
     * Get power.
     * @return Power.
     */
    public double getPower() {
        return power;
    }

    /**
     * Set power.
     * @param power Power.
     */
    public void setPower(double power) {
        this.power = power;
    }

    /**
     * Initializes a new instance of the PowerNormalization class.
     */
    public PowerNormalization() {
        this(1);
    }

    /**
     * Initializes a new instance of the PowerNormalization class.
     * @param power Power.
     */
    public PowerNormalization(double power) {
        this.power = power;
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
    public void ApplyInPlace(double[][] data) {
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                data[i][j] = Math.signum(data[i][j]) * Math.pow(Math.abs(data[i][j]), power);
            }
        }
    }

    @Override
    public void ApplyInPlace(DecisionVariable[] variables, double[][] data) {
        for (int i = 0; i < data.length; i++)
            for (int j = 0; j < data[0].length; j++)
                if(variables[j].type == DecisionVariable.Type.Continuous)
                    data[i][j] = Math.signum(data[i][j]) * Math.pow(Math.abs(data[i][j]), power);
    }

    @Override
    public double[] Compute(double[] feature) {
        double[] result = new double[feature.length];
        for (int i = 0; i < feature.length; i++)
            result[i] = Math.signum(feature[i]) * Math.pow(Math.abs(feature[i]), power);
        
        return result;
    }

    @Override
    public double[] Compute(DecisionVariable[] variables, double[] feature) {
        double[] result = new double[feature.length];
        for (int i = 0; i < feature.length; i++)
            if(variables[i].type == DecisionVariable.Type.Continuous)
                result[i] = Math.signum(feature[i]) * Math.pow(Math.abs(feature[i]), power);
        
        return result;
    }
}