/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Catalano.Genetic.Mutation;

import Catalano.Genetic.IChromosome;
import java.util.Random;

/**
 *
 * @author Diego
 */
public class SimpleInversionMutation implements IMutation<IChromosome>{

    public SimpleInversionMutation() {}

    @Override
    public IChromosome Compute(IChromosome chromossome) {
        
        Random rand = new Random();
        
        IChromosome c = chromossome.Clone();
        
        int lenght = c.getLength();
        
        int size = rand.nextInt(lenght - 1) + 2;
        int pos = rand.nextInt(lenght-size);
        
        for (int i = 0; i < size; i++)
            c.setGene(pos+i,chromossome.getGene(pos+size-i-1));
        
        return c;
        
    }
    
}
