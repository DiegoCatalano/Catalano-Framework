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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Partial Elitism Reinsertion.
 * 
 * The best individuals from original population is put in the worst
 * individuals from de new population.
 * 
 * @author Diego Catalano
 */
public class PartialElitismReinsertion implements IReinsertion{
    
    private float percentage;

    /**
     * Get percentage.
     * @return Percentage.
     */
    public float getPercentage() {
        return percentage;
    }

    /**
     * Set percentage.
     * @param percentage Percentage.
     */
    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    /**
     * Initialize a new instance of the PartialElitismReinsertion class.
     */
    public PartialElitismReinsertion() {
        this(0.15f);
    }

    /**
     * Initialize a new instance of the PartialElitismReinsertion class.
     * @param percentage Percentage.
     */
    public PartialElitismReinsertion(float percentage) {
        this.percentage = percentage;
    }

    @Override
    public List<IChromosome> Compute(Population pop, List<IChromosome> oldPop, List<IChromosome> newPop) {
        
        Sort(oldPop);
        Sort(newPop);
        
        List<IChromosome> p = new ArrayList<>();
        
        //Copy some individuals from the old population
        int n = (int)(oldPop.size() * percentage);
        p.addAll(oldPop.subList(0, n));
        
        //Copy the best from new population
        int diff = pop.getPopulationSize() - p.size();
        p.addAll(newPop.subList(0, diff));
        
        return p;
    }
    
    private void Sort(List<IChromosome> list){
        
        Collections.sort(list, new Comparator<IChromosome>() {
            @Override
            public int compare(IChromosome o1, IChromosome o2) {
                return Double.compare(o2.getFitness(), o1.getFitness());
            }
        });
    }
    
}