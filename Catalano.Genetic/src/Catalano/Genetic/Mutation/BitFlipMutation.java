/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Catalano.Genetic.Mutation;

import Catalano.Genetic.BinaryChromossome;
import java.util.Random;

/**
 *
 * @author Diego
 */
public class BitFlipMutation implements IMutation{
    
    private float prob;
    private long seed;

    public BitFlipMutation() {
        this(0.2f);
    }

    public BitFlipMutation(float prob) {
        this(prob, 0);
    }

    public BitFlipMutation(float prob, long seed) {
        this.prob = prob;
        this.seed = seed;
    }

    @Override
    public BinaryChromossome Compute(BinaryChromossome chromossome) {
        
        Random r = new Random();
        if(seed != 0) r.setSeed(seed);
        
        String bin = chromossome.toBinary();
        String newBin = "";
        for (int i = 0; i < bin.length(); i++) {
            char v = bin.charAt(i);
            if(r.nextFloat() < prob){
                if(v == '0') newBin += "1";
                if(v == '1') newBin += "0";
            }
            else{
                newBin += v;
            }
        }
        
        return new BinaryChromossome(chromossome.getNumberOfBits(), newBin);
        
    }
    
}