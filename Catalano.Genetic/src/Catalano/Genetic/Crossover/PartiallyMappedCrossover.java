/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Catalano.Genetic.Crossover;

import Catalano.Genetic.Chromosome.IChromosome;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Partially Mapped Crossover (PMX).
 * @author Diego Catalano
 */
public class PartiallyMappedCrossover implements ICrossover<IChromosome>{

    public PartiallyMappedCrossover() {}

    @Override
    public List<IChromosome> Compute(IChromosome chromosome1, IChromosome chromosome2) {

        Random rand = new Random();
        
        int length = chromosome1.getLength();
        
        //Cut points
        int[] cuts = {rand.nextInt(length), rand.nextInt(length)};
        Arrays.sort(cuts, 0, cuts.length);
        
        IChromosome c1 = chromosome1.Clone();
        IChromosome c2 = chromosome2.Clone();
        
        Object[] v1 = new Object[length - (cuts[1] - cuts[0]) - 1];
        Object[] v2 = new Object[length - (cuts[1] - cuts[0]) - 1];
        
        int index = 0;
        for (int i = 0; i < length; i++) {
            if(i < cuts[0] || i > cuts[1]){
                v1[index] = (Integer)chromosome1.getGene(i);
                v2[index] = (Integer)chromosome2.getGene(i);
                index++;
            }
        }
        
        index = 0;
        for (int i = 0; i < length; i++) {
            if(i < cuts[0] || i > cuts[1]){
                c1.setGene(i, v1[index]);
                c2.setGene(i, v2[index]);
                index++;
            }
        }
        
        List<IChromosome> lst = new ArrayList<IChromosome>(2);
        lst.add(c1);
        lst.add(c2);
        
        return lst;
        
    }
    
}