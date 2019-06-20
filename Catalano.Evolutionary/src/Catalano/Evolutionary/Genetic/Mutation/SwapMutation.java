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
 * Swap Mutation.
 * 
 * Support: Binary/Permutation/Integer/Double/Float Chromosome.
 * 
 * @author Diego Catalano
 */
public class SwapMutation implements IMutation<IChromosome>{
    
    private float probability;

    public float getProbability() {
        return probability;
    }

    public void setProbability(float probability) {
        this.probability = probability;
    }

    /**
     * Initializes a new instance of the SwapMutation class.
     */
    public SwapMutation() {}

    public SwapMutation(float probability) {
        this.probability = probability;
    }

    @Override
    public IChromosome Compute(IChromosome chromossome) {
        
        Random rand = new Random();
        
        IChromosome c = chromossome.Clone();
        
        if(probability == 0){
            int indexA = rand.nextInt(c.getLength());
            int indexB = rand.nextInt(c.getLength());

            Object t1 = c.getGene(indexA);
            Object t2 = c.getGene(indexB);

            c.setGene(indexA, t2);
            c.setGene(indexB, t1);
        }
        else{
            for (int i = 0; i < c.getLength(); i++) {
                if(rand.nextFloat() <= probability){
                    int indexA = rand.nextInt(c.getLength());
                    int indexB = rand.nextInt(c.getLength());

                    Object t1 = c.getGene(indexA);
                    Object t2 = c.getGene(indexB);

                    c.setGene(indexA, t2);
                    c.setGene(indexB, t1);
                }
            }
        }
        
        return c;
        
    }
    
}