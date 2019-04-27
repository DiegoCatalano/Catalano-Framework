/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Catalano.Genetic.Crossover;

import Catalano.Genetic.Chromosome.IChromosome;
import Catalano.Genetic.Chromosome.PermutationChromosome;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Diego
 */
public class SinglePointCrossover implements ICrossover<IChromosome> {

    public SinglePointCrossover() {}

    @Override
    public List<IChromosome> Compute(IChromosome chromosome1, IChromosome chromosome2) {
        
        if(chromosome1 instanceof PermutationChromosome){
            return ComputePC((PermutationChromosome)chromosome1,(PermutationChromosome)chromosome2);
        }
        
        return ComputeGeneric(chromosome1, chromosome2);
        
    }
    
    private List<IChromosome> ComputeGeneric(IChromosome chromosome1, IChromosome chromosome2) {
        
        Random rand = new Random();
        
        //Cut point
        int cut = rand.nextInt(chromosome1.getLength());
        
        int length = chromosome1.getLength();
        
        IChromosome c1 = chromosome1.Clone();
        IChromosome c2 = chromosome1.Clone();
        
        //First chromosome
        for (int i = 0; i < cut; i++) {
            c1.setGene(i, chromosome1.getGene(i));
        }
        
        for (int i = cut; i < length; i++) {
            c1.setGene(i, chromosome2.getGene(i));
        }
        
        //Second chromosome
        int index = 0;
        for (int i = cut; i < length; i++) {
            c2.setGene(index++, chromosome1.getGene(i));
        }
        
        for (int i = 0; i < cut; i++) {
            c2.setGene(index++, chromosome2.getGene(i));
        }
        
        List<IChromosome> lst = new ArrayList<IChromosome>(2);
        lst.add(c1);
        lst.add(c2);
        
        return lst;
        
    }
    
//    private List<IChromosome> ComputeBC(BinaryChromosome chromosome1, BinaryChromosome chromosome2) {
//        
//        Random rand = new Random();
//        
//        //Cut point
//        int cut = rand.nextInt(chromosome1.getLength());
//        
//        String a = chromosome1.toBinary();
//        String b = chromosome2.toBinary();
//        
//        String newA = a.substring(0,cut) + b.substring(cut, a.length());
//        String newB = b.substring(0,cut) + a.substring(cut, a.length());
//        
//        List<IChromosome> lst = new ArrayList<IChromosome>(2);
//        lst.add(new BinaryChromosome(a.length(), newA));
//        lst.add(new BinaryChromosome(b.length(), newB));
//        
//        return lst;
//        
//    }
    
    private List<IChromosome> ComputePC(PermutationChromosome chromosome1, PermutationChromosome chromosome2) {
        
        Random rand = new Random();
        int size = chromosome1.getLength();
        
        //Cut point
        int cut = rand.nextInt(chromosome1.getLength());
        
        List<Integer> set1 = new ArrayList<Integer>(chromosome1.getLength());
        List<Integer> set2 = new ArrayList<Integer>(chromosome1.getLength());
        for (int i = cut; i < size; i++) {
            set1.add((Integer)chromosome1.getGene(i));
            set2.add((Integer)chromosome2.getGene(i));
        }
        Collections.shuffle(set1);
        Collections.shuffle(set2);
        
        int[] c1 = new int[size];
        int[] c2 = new int[size];
        for (int i = 0; i < cut; i++) {
            c1[i] = (Integer)chromosome1.getGene(i);
            c2[i] = (Integer)chromosome2.getGene(i);
        }
        for (int i = cut; i < size; i++) {
            c1[i] = set1.get(i - cut);
            c2[i] = set2.get(i - cut);
        }
        
        List<IChromosome> lst = new ArrayList<IChromosome>(2);
        lst.add(new PermutationChromosome(c1));
        lst.add(new PermutationChromosome(c2));
        
        return lst;
        
    }
    
}
