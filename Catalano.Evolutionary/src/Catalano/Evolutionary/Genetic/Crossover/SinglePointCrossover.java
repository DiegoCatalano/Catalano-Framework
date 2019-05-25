// Catalano Genetic Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2019
// diego.catalano at live.com
//
//
//    This library is free software; you can redistribute it and/or
//    modify it under the terms of the GNU Lesser General Public
//    License as published by the Free Software Foundation; either
//    version 2.1 of the License, or (at your option) any later version.
//
//    This library is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
//    Lesser General Public License for more details.
//
//    You should have received a copy of the GNU Lesser General Public
//    License along with this library; if not, write to the Free Software
//    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
//

package Catalano.Evolutionary.Genetic.Crossover;

import Catalano.Evolutionary.Genetic.Chromosome.IChromosome;
import Catalano.Evolutionary.Genetic.Chromosome.PermutationChromosome;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Single Point Crossover.
 * 
 * Support: Binary/Permutation/Integer/Double/Float Chromosome.
 * 
 * @author Diego Catalano
 */
public class SinglePointCrossover implements ICrossover<IChromosome> {

    /**
     * Initialize a new instance of the SinglePointCrossover class.
     */
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
        IChromosome c2 = chromosome2.Clone();
        
        //First chromosome
        for (int i = 0; i < cut; i++) {
            c1.setGene(i, chromosome1.getGene(i));
            c2.setGene(i, chromosome2.getGene(i));
        }
        
        for (int i = cut; i < length; i++) {
            c1.setGene(i, chromosome2.getGene(i));
            c2.setGene(i, chromosome1.getGene(i));
        }
        
        List<IChromosome> lst = new ArrayList<>(2);
        lst.add(c1);
        lst.add(c2);
        
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
