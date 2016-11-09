/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Catalano.Genetic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Diego
 */
public class Population {
    
    private List<BinaryChromossome> chromossomes;
    
    public static Population Generate(int nBits, int size, long seed){
        
        Random r = new Random();
        if(seed != 0) r.setSeed(seed);
        
        List<BinaryChromossome> pop = new ArrayList<BinaryChromossome>(size);
        for (int i = 0; i < size; i++) {
            pop.add(BinaryChromossome.Generate(nBits, seed));
        }
        
        return new Population(pop);
        
    }

    public List<BinaryChromossome> getChromossomes() {
        return chromossomes;
    }

    public void setChromossomes(List<BinaryChromossome> chromossomes) {
        this.chromossomes = chromossomes;
    }

    public Population(List<BinaryChromossome> chromossomes) {
        this.chromossomes = chromossomes;
    }
    
    public void Sort(){
        Sort(false);
    }
    
    public void Sort(boolean ascending){
        if(ascending)
            Collections.reverse(chromossomes);
        else
            Collections.sort(chromossomes);
    }
    
}