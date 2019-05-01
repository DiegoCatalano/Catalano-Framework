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
public class MultiPointCrossover implements ICrossover<IChromosome> {
    
    private int n;

    public int getNumberOfPoints() {
        return n;
    }
    
    public void setNumberOfPoints(int n){
        this.n = Math.max(1, n);
    }

    public MultiPointCrossover() {
        this(3);
    }

    public MultiPointCrossover(int n) {
        this.n = n;
    }

    @Override
    public List<IChromosome> Compute(IChromosome chromosome1, IChromosome chromosome2) {
        
        if(chromosome1 instanceof PermutationChromosome){
            throw new IllegalArgumentException("Permutation Chromosome is not supported.");
        }
        
        Random rand = new Random();
        
        int length = chromosome1.getLength();
        
        //Cut points
        int[] cuts = new int[n];
        for (int i = 0; i < cuts.length; i++) {
            cuts[i] = rand.nextInt(length);
        }
        
        Arrays.sort(cuts, 0, cuts.length);
        
        IChromosome c1 = chromosome1.Clone();
        IChromosome c2 = chromosome2.Clone();
        
        //Operations of change
        int start = 0;
        for (int c = 0; c < n; c++) {
            if(c % 2 == 0){
                for (int i = start; i < cuts[c]; i++) {
                    c1.setGene(i, chromosome1.getGene(i));
                    c2.setGene(i, chromosome2.getGene(i));
                }
            } else{
                for (int i = start; i < cuts[c]; i++) {
                    c1.setGene(i, chromosome2.getGene(i));
                    c2.setGene(i, chromosome1.getGene(i));
                }
            }
            
            start = cuts[c];
        }
        
        List<IChromosome> lst = new ArrayList<IChromosome>(2);
        lst.add(c1);
        lst.add(c2);
        
        return lst;
        
    }
    
}