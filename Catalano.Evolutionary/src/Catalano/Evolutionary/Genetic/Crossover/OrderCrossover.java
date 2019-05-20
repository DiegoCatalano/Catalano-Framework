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

import Catalano.Core.ArraysUtil;
import Catalano.Evolutionary.Genetic.Chromosome.PermutationChromosome;
import Catalano.Math.Matrix;
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
public class OrderCrossover implements ICrossover<PermutationChromosome>{

    /**
     * Initializes a new instance of the OrderCrossover class.
     */
    public OrderCrossover() {}

    @Override
    public List<PermutationChromosome> Compute(PermutationChromosome chromosome1, PermutationChromosome chromosome2) {

        Random rand = new Random();
        
        int length = chromosome1.getLength();
        
        //Cut points
        int[] cuts = {rand.nextInt(length), rand.nextInt(length)};
        Arrays.sort(cuts, 0, cuts.length);
        
        int[] c1 = Matrix.CreateMatrix1D(length, -1);
        int[] c2 = Matrix.CreateMatrix1D(length, -1);
        
        int idx;
        int[] order;
        
        //Offspring 1: Swath of consecutive genes
        for (int i = 0; i < length; i++) {
            int e = (Integer)chromosome1.getGene(i);
            if(i >= cuts[0] && i <= cuts[1]){
                c1[i] = e;
            }
        }
        
        //Offspring 1: Copy elements from second parent
        idx = 0;
        order = empty(c1);
        for (int i = 0; i < length; i++) {
            int e = (Integer)chromosome2.getGene(i);
            if(ArraysUtil.Contains(c1, e) == false){
                c1[order[idx++]] = e;
            }
        }
        
        //Offspring 2: Swath of consecutive genes
        for (int i = 0; i < length; i++) {
            int e = (Integer)chromosome2.getGene(i);
            if(i >= cuts[0] && i <= cuts[1]){
                c2[i] = e;
            }
        }
        
        //Offspring 2: Copy elements from second parent
        idx = 0;
        order = empty(c2);
        for (int i = 0; i < length; i++) {
            int e = (Integer)chromosome1.getGene(i);
            if(ArraysUtil.Contains(c2, e) == false){
                c2[order[idx++]] = e;
            }
        }
        
        List<PermutationChromosome> lst = new ArrayList<>(2);
        lst.add(new PermutationChromosome(c1));
        lst.add(new PermutationChromosome(c2));
        
        return lst;
        
    }
    
    private int[] empty(final int[] array) {
        int c = 0;
        for (int i = 0; i < array.length; i++)
            if(array[i] == -1) c++;
        
        
        int[] order = new int[c];
        int idx = 0;
        for (int i = 0; i < array.length; i++)
            if(array[i] == -1) order[idx++] = i;

        return order;
    }
    
}