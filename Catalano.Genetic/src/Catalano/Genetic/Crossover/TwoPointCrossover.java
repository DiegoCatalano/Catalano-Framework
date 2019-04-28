/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Catalano.Genetic.Crossover;

import Catalano.Genetic.Chromosome.IChromosome;
import Catalano.Genetic.Chromosome.PermutationChromosome;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Diego
 */
public class TwoPointCrossover implements ICrossover<IChromosome> {

    public TwoPointCrossover() {}

    @Override
    public List<IChromosome> Compute(IChromosome chromosome1, IChromosome chromosome2) {
        
        if(chromosome1 instanceof PermutationChromosome){
            throw new IllegalArgumentException("Permutation Chromosome is not supported.");
        }
        
        Random rand = new Random();
        
        int length = chromosome1.getLength();
        
        //Cut points
        int[] cuts = {rand.nextInt(length), rand.nextInt(length)};
        Arrays.sort(cuts, 0, cuts.length);
        
        IChromosome c1 = chromosome1.Clone();
        IChromosome c2 = chromosome2.Clone();
        
        //Operations of change
        for (int i = 0; i < cuts[0]; i++) {
            c1.setGene(i, chromosome1.getGene(i));
            c2.setGene(i, chromosome2.getGene(i));
        }
        
        for (int i = cuts[0]; i < cuts[1]; i++) {
            c1.setGene(i, chromosome2.getGene(i));
            c2.setGene(i, chromosome1.getGene(i));
        }
        
        for (int i = cuts[1]; i < length; i++) {
            c1.setGene(i, chromosome1.getGene(i));
            c2.setGene(i, chromosome2.getGene(i));
        }
        
        List<IChromosome> lst = new ArrayList<IChromosome>(2);
        lst.add(c1);
        lst.add(c2);
        
        return lst;
        
    }
    
}