/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Catalano.Genetic.Selection;

import Catalano.Core.ArraysUtil;
import Catalano.Genetic.BinaryChromossome;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Diego
 */
public class ElitismSelection implements ISelection{
    
    private int size;

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void setSize(int size) {
        this.size = size;
    }

    public ElitismSelection(int size) {
        this.size = size;
    }

    @Override
    public int[] Compute(List<BinaryChromossome> chromossomes) {
        
        //Sort the chromossomes
        double[] fitness = new double[chromossomes.size()];
        for (int i = 0; i < chromossomes.size(); i++) {
            fitness[i] = chromossomes.get(i).getFitness();
        }
        
        int[] index = ArraysUtil.Argsort(fitness, false);
        
        return Arrays.copyOfRange(index, 0, size);
        
    }

    
    
}
