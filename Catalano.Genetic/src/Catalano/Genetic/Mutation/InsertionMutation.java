/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Catalano.Genetic.Mutation;

import Catalano.Genetic.IChromosome;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Diego
 */
public class InsertionMutation implements IMutation<IChromosome>{

    public InsertionMutation() {}

    @Override
    public IChromosome Compute(IChromosome chromossome) {
        
        Random rand = new Random();
        
        IChromosome c = chromossome.Clone();
        
        //Random choose one position
        int pos = rand.nextInt(chromossome.getLength());
        
        //Random choose one position to insert
        int posIn = rand.nextInt(chromossome.getLength() - 1);
        
        List<Object> genes = new ArrayList<Object>(chromossome.getLength() - 1);
        for (int i = 0; i < chromossome.getLength(); i++)
            if(i != pos) genes.add(chromossome.getGene(i));
        
        int idx = 0;
        for (int i = 0; i < c.getLength(); i++) {
            if(i != posIn){
                c.setGene(i, genes.get(idx));
                idx++;
            }
        }
        
        c.setGene(posIn, chromossome.getGene(pos));
        return c;
        
    }
    
}
