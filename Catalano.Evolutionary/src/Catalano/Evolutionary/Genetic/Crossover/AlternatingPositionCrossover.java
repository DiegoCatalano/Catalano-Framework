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
import java.util.List;

/**
 * Alternating Position Crossover (AP).
 * 
 * Support: Binary/Integer/Double Chromosome.
 * 
 * @author Diego Catalano
 */
public class AlternatingPositionCrossover implements ICrossover<IChromosome>{

    /**
     * Initialize a new instance of the AlternatingPositionCrossover class.
     */
    public AlternatingPositionCrossover() {}

    @Override
    public List<IChromosome> Compute(IChromosome chromosome1, IChromosome chromosome2) {
        
        if(chromosome1 instanceof PermutationChromosome)
            throw new IllegalArgumentException("Alternating Position doesn't work with Permutation chromosome yet.");
        
        int length = chromosome1.getLength();
        
        IChromosome c1 = chromosome1.Clone();
        IChromosome c2 = chromosome2.Clone();
        
        for (int i = 1; i < length; i+=2) {
            c1.setGene(i-1, chromosome1.getGene(i-1));
            c1.setGene(i, chromosome2.getGene(i));
            
            c2.setGene(i-1, chromosome2.getGene(i-1));
            c2.setGene(i, chromosome1.getGene(i));
        }
        
        List<IChromosome> lst = new ArrayList<>(2);
        lst.add(c1);
        lst.add(c2);
        
        return lst;
        
    }
    
}