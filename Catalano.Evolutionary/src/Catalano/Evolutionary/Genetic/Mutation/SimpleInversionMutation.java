// Catalano Genetic Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
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

package Catalano.Evolutionary.Genetic.Mutation;

import Catalano.Evolutionary.Genetic.Chromosome.IChromosome;
import java.util.Random;

/**
 * Simple Inversion Mutation.
 * 
 * Support: Binary/Permutation/Integer/Double Chromosome.
 * 
 * @author Diego Catalano
 */
public class SimpleInversionMutation implements IMutation<IChromosome>{

    /**
     * Initializes a new instance of the SimpleInversionMutation class.
     */
    public SimpleInversionMutation() {}

    @Override
    public IChromosome Compute(IChromosome chromossome) {
        
        Random rand = new Random();
        
        IChromosome c = chromossome.Clone();
        
        int lenght = c.getLength();
        
        int size = rand.nextInt(lenght - 1) + 2;
        int pos = rand.nextInt(lenght-size);
        
        for (int i = 0; i < size; i++)
            c.setGene(pos+i,chromossome.getGene(pos+size-i-1));
        
        return c;
        
    }
    
}