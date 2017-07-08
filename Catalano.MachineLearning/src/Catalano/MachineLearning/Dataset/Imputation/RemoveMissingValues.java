/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Catalano.MachineLearning.Dataset.Imputation;

import Catalano.MachineLearning.Dataset.IDataset;
import Catalano.Math.Matrix;
import java.util.ArrayList;
import java.util.List;

/**
 * Remove any instance where is missing values.
 * Support: Continuous and Discrete values.
 * 
 * @author Diego Catalano
 */
public class RemoveMissingValues implements IImputation{

    public RemoveMissingValues() {}

    @Override
    public void ApplyInPlace(IDataset dataset) {
        
        double[][] input = (double[][])dataset.getInput();
        
        List<Integer> indexes = new ArrayList<Integer>();
        for (int i = 0; i < input.length; i++) {
            boolean isMissing = false;
            for (int j = 0; j < input[0].length; j++)
                if(input[i][j] == Double.NaN) isMissing = true;
            
            if(isMissing) indexes.add(i);
        }
        
        int[] v = new int[indexes.size()];
        for (int i = 0; i < v.length; i++) {
            v[i] = indexes.get(i);
        }
        
        input = Matrix.RemoveRows(input, v);
        
    }
    
}
