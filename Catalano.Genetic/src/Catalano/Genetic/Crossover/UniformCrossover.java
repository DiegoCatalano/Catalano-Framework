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

package Catalano.Genetic.Crossover;

import Catalano.Genetic.Chromosome.IChromosome;
import java.util.ArrayList;
import java.util.List;

/**
 * Uniform Crossover.
 * 
 * Support: Binary/Integer/Double Chromosome.
 * 
 * @author Diego Catalano
 */
public class UniformCrossover implements ICrossover<IChromosome>{

    /**
     * Initializes a new instance of the UniformCrossover class.
     */
    public UniformCrossover() {}

    @Override
    public List<IChromosome> Compute(IChromosome chromosome1, IChromosome chromosome2) {
        
        IChromosome c1 = chromosome1.Clone();
        IChromosome c2 = chromosome2.Clone();
        
        int size = chromosome1.getLength();
        for (int i = 0; i < size; i++) {
            if(Math.random() <= 0.5){
                c1.setGene(i, chromosome1.getGene(i));
                c2.setGene(i, chromosome2.getGene(i));
            }
            else{
                c1.setGene(i, chromosome2.getGene(i));                
                c2.setGene(i, chromosome1.getGene(i));
            }
        }
        
        List<IChromosome> lst = new ArrayList<IChromosome>(2);
        lst.add(c1);
        lst.add(c2);
        
        return lst;
        
    }
    
}