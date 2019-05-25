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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Partially Mapped Crossover (PMX).
 * 
 * Support: Binary/Permutation/Integer/Double/Float Chromosome.
 * 
 * @author Diego Catalano
 */
public class PartiallyMappedCrossover implements ICrossover<IChromosome>{

    /**
     * Initializes a new instance of the PartiallyMappedCrossover class.
     */
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
                v1[index] = chromosome1.getGene(i);
                v2[index] = chromosome2.getGene(i);
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
        
        List<IChromosome> lst = new ArrayList<>(2);
        lst.add(c1);
        lst.add(c2);
        
        return lst;
        
    }
    
}