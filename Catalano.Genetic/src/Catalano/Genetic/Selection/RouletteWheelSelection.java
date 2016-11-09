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
import java.util.Random;

/**
 *
 * @author Diego
 */
public class RouletteWheelSelection implements ISelection{
    
    private int size;
    private long seed;

    public RouletteWheelSelection() {}
    
    public RouletteWheelSelection(int size){
        this(size, 0);
    }

    public RouletteWheelSelection(int size, long seed) {
        this.size = size;
        this.seed = seed;
    }

    @Override
    public int getSize() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setSize(int size) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int[] Compute(List<BinaryChromossome> chromossomes) {
        
        //Scale fitness [0..1]
        int[] index = new int[size];
        double[] fitness = new double[chromossomes.size()];
        
        double max = 0;
        for (int i = 0; i < fitness.length; i++) {
            fitness[i] = chromossomes.get(i).getFitness();
            max = Math.max(max, fitness[i]);
        }
        
        for (int i = 0; i < fitness.length; i++) {
            fitness[i] /= max;
        }
        
        //Sort the fitness values
        int[] oriIndex = ArraysUtil.Argsort(fitness, true);
        Arrays.sort(fitness);
        
        //Select the chromossomes
        Random r = new Random();
        if(seed != 0) r.setSeed(seed);
        
        for (int i = 0; i < index.length; i++) {
            double v = r.nextDouble();
            for (int j = 0; j < fitness.length; j++) {
                if(v >= fitness[j]){
                    index[i] = oriIndex[j];
                }
            }
        }
        
        return index;
        
    }
    
}
