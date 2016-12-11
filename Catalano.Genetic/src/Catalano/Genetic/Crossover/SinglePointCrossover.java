/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Catalano.Genetic.Crossover;

import Catalano.Genetic.BinaryChromosome;
import Catalano.Genetic.IChromosome;
import Catalano.Genetic.PermutationChromosome;
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
        
        if(chromosome1 instanceof BinaryChromosome){
            return ComputeBC((BinaryChromosome)chromosome1,(BinaryChromosome)chromosome2);
        }
        return ComputePC((PermutationChromosome)chromosome1,(PermutationChromosome)chromosome2);
        
    }
    
    private List<IChromosome> ComputeBC(BinaryChromosome chromosome1, BinaryChromosome chromosome2) {
        
        Random rand = new Random();
        
        //Cut point
        int cut = rand.nextInt(chromosome1.getLength());
        
        String a = chromosome1.toBinary();
        String b = chromosome2.toBinary();
        
        String newA = a.substring(0,cut) + b.substring(cut, a.length());
        String newB = b.substring(0,cut) + a.substring(cut, a.length());
        
        List<IChromosome> lst = new ArrayList<IChromosome>(2);
        lst.add(new BinaryChromosome(a.length(), newA));
        lst.add(new BinaryChromosome(b.length(), newB));
        
        return lst;
        
    }
    
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
