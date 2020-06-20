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

package Catalano.Evolutionary.Genetic.Reinsertion;

import Catalano.Evolutionary.Genetic.Chromosome.IChromosome;
import Catalano.Evolutionary.Genetic.Population;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Elitism reinsertion.
 * @author Diego Catalano
 */
public class GlobalElistismReinsertion implements IReinsertion{

    /**
     * Initialize a new instance of the GlobalElistismReinsertion class.
     * 
     * Get the best individuals from the both population.
     * 
     */
    public GlobalElistismReinsertion() {}

    @Override
    public List<IChromosome> Compute(Population population, List<IChromosome> oldPopulation, List<IChromosome> newPopulation) {
        
        List<IChromosome> pop = new ArrayList<>(oldPopulation.size() + newPopulation.size());
        pop.addAll(oldPopulation);
        pop.addAll(newPopulation);
        
        pop.sort(Comp());
        
        return pop.subList(0, population.getPopulationSize());
        
    }
    
    private Comparator Comp(){
        return new Comparator<IChromosome>() {

            @Override
            public int compare(IChromosome o1, IChromosome o2) {
                return Double.compare(o2.getFitness(), o1.getFitness());
            }
        };
    }
    
}
