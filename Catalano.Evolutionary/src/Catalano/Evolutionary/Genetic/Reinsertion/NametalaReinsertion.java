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
 * Nametala Elitism Reinsertion.
 * 
 * The best individuals from original population is put in the worst
 * individuals plus additional random indivuduals.
 * 
 * @author Diego Catalano
 */
public class NametalaReinsertion implements IReinsertion{
    
    private float pBest;
    private float pRandom;

    /**
     * Get percentage.
     * @return Percentage.
     */
    public float getPercentageBest() {
        return pBest;
    }

    /**
     * Set percentage.
     * @param percentageBest Percentage.
     */
    public void setPercentageBest(float percentageBest) {
        this.pBest = percentageBest;
    }

    public float getPercentageRandom() {
        return pRandom;
    }

    public void setPercentageRandom(float percentageRandom) {
        this.pRandom = percentageRandom;
    }

    /**
     * Initialize a new instance of the NametalaReinsertion class.
     * @param percentage Percentage.
     */
    public NametalaReinsertion(float percentage) {
        this.pBest = percentage;
    }
    
    /**
     * Initialize a new instance of the NametalaReinsertion class.
     */
    public NametalaReinsertion() {
        this(0.15f, 0.3f);
    }
    
    /**
     * Initialize a new instance of the NametalaReinsertion class.
     * @param pBest PercentageBest.
     * @param pRandom PercentageRandom.
     */
    public NametalaReinsertion(float pBest, float pRandom) {
        this.pBest = pRandom;
        this.pBest = pRandom;
    }

    @Override
    public List<IChromosome> Compute(Population pop, List<IChromosome> oldPop, List<IChromosome> newPop) {
        
        Sort(oldPop);
        Sort(newPop);
        
        List<IChromosome> p = new ArrayList<>();
        
        //Copy the best individuals from the new population
        int n = (int)(newPop.size() * pBest);
        p.addAll(newPop.subList(0, n));
        
        //Generate new individuals
        IChromosome base = newPop.get(0);
        n = (int)(pop.getPopulationSize() * pRandom);
        for (int i = 0; i < n; i++) {
            p.add(base.CreateNew());
        }
        
        //Copy the best from old population
        int diff = pop.getPopulationSize() - p.size();
        p.addAll(oldPop.subList(0, diff));
        
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