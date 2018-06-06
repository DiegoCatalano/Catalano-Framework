/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Catalano.MachineLearning.FeatureEncoder;

import Catalano.Math.Matrix;

/**
 *
 * @author Diego
 */
public class OneHotEncoder {
    
    public boolean keepDummyVariable;

    public OneHotEncoder() {
        this(true);
    }

    public OneHotEncoder(boolean keepDummyVariable) {
        this.keepDummyVariable = keepDummyVariable;
    }
    
    public double[][] Process(double[][] input, int index){
        
        int max = 0;
        for (int i = 0; i < input.length; i++) {
            max = (int)Math.max(max, input[i][index]);
        }
        
        double[][] data = new double[input.length][max + 1];
        for (int i = 0; i < input.length; i++) {
            data[i][(int)input[i][index]] = 1;
        }
        
        if(!keepDummyVariable){
            data = Matrix.RemoveColumn(data, 0);
        }
        
        input = Matrix.RemoveColumn(input, index);
        return Matrix.InsertColumns(input, data, index);
        
    }
    
    
}
