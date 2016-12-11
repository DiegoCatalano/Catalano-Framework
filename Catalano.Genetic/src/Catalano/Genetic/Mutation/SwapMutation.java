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
public class SwapMutation implements IMutation<IChromosome>{

    public SwapMutation() {}

    @Override
    public IChromosome Compute(IChromosome chromossome) {
        
        Random rand = new Random();
        
        IChromosome c = chromossome.Clone();
        
        int indexA = rand.nextInt(c.getLength());
        int indexB = rand.nextInt(c.getLength());
        
        Object t1 = c.getGene(indexA);
        Object t2 = c.getGene(indexB);
        
        c.setGene(indexA, t2);
        c.setGene(indexB, t1);
        
        return c;
        
    }
    
    
    
}
