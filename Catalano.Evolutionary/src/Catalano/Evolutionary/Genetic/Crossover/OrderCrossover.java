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

import Catalano.Evolutionary.Genetic.Chromosome.BinaryChromosome;
import Catalano.Evolutionary.Genetic.Chromosome.IChromosome;
import Catalano.Evolutionary.Genetic.Chromosome.PermutationChromosome;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Order Crossover (OX)
 * 
 * Support: Permutation/Integer Chromosome.
 * 
 * @author Diego Catalano
 */
public class OrderCrossover implements ICrossover<IChromosome>{

    /**
     * Initializes a new instance of the OrderCrossover class.
     */
    public OrderCrossover() {}

    @Override
    public List<IChromosome> Compute(IChromosome chromosome1, IChromosome chromosome2) {

        if(chromosome1 instanceof BinaryChromosome){
            throw new IllegalArgumentException("Support only Permutation and Integer chromosomes");
        } else {
            return ComputeIC((PermutationChromosome)chromosome1,(PermutationChromosome)chromosome2);
        }
        
    }
    
    private List<IChromosome> ComputeIC(IChromosome chromosome1, IChromosome chromosome2){
        Random rand = new Random();
        
        int length = chromosome1.getLength();
        
        //Cut points
        int[] cuts = {rand.nextInt(length), rand.nextInt(length)};
        Arrays.sort(cuts, 0, cuts.length);
        
        IChromosome c1 = chromosome1.Clone();
        IChromosome c2 = chromosome2.Clone();
        
        int[] v1 = new int[length - (cuts[1] - cuts[0]) - 1];
        int[] v2 = new int[length - (cuts[1] - cuts[0]) - 1];
        
        int index = 0;
        for (int i = 0; i < length; i++) {
            if(i < cuts[0] || i > cuts[1]){
                v1[index] = (Integer)chromosome1.getGene(i);
                v2[index] = (Integer)chromosome2.getGene(i);
                index++;
            }
        }
        
        Arrays.sort(v1);
        Arrays.sort(v2);
        
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