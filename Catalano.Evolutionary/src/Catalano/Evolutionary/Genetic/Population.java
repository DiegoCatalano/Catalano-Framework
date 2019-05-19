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

package Catalano.Evolutionary.Genetic;

import Catalano.Evolutionary.Genetic.Chromosome.IChromosome;
import Catalano.Evolutionary.Genetic.Crossover.ICrossover;
import Catalano.Evolutionary.Genetic.Mutation.IMutation;
import Catalano.Evolutionary.Genetic.Selection.ISelection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * Population of chromosomes.
 * @author Diego Catalano
 */
public class Population {
    
    private int population;
    private float crossoverRate;
    private float mutationRate;
    
    private IFitness function;
    private List<IChromosome> list;
    
    private ISelection selection;
    private ICrossover crossover;
    private IMutation mutation;
    private boolean autoShuffle = false;
    
    private IChromosome best;
    private double minError;

    /**
     * Get best chromosome.
     * @return Best chromosome.
     */
    public IChromosome getBest() {
        return best;
    }

    public boolean isAutoShuffle() {
        return autoShuffle;
    }

    public void setAutoShuffle(boolean autoShuffle) {
        this.autoShuffle = autoShuffle;
    }
    
    /**
     * Set selection method.
     * @param selection Selection method.
     */
    public void setSelection(ISelection selection){
        this.selection = selection;
    }
    
    /**
     * Set crossover method.
     * @param crossover Crossover method.
     */
    public void setCrossover(ICrossover crossover){
        this.crossover = crossover;
    }
    
    /**
     * Set mutation method.
     * @param mutation Mutation method.
     */
    public void setMutation(IMutation mutation){
        this.mutation = mutation;
    }

    /**
     * Initializes a new instance of the Population class.
     * @param base Chromosome base.
     * @param population Size of population.
     * @param function Function to be optimized.
     * @param crossoverRate Crossover rate.
     * @param mutationRate Mutation rate.
     */
    public Population(IChromosome base, int population, IFitness function, float crossoverRate, float mutationRate) {
        this.population = population;
        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;
        this.function = function;
        Generate(base);
    }
    
    /**
     * Set all operators in the population.
     * @param selection Selection method.
     * @param crossover Crossover method.
     * @param mutation Mutation method.
     */
    public void setOperators(ISelection selection, ICrossover crossover, IMutation mutation){
        this.selection = selection;
        this.crossover = crossover;
        this.mutation = mutation;
    }
    
    private void Generate(IChromosome chromossome){
        list = new ArrayList<>(population);
        
        chromossome.Evaluate(function);
        minError = chromossome.getFitness();
        best = chromossome.Clone();
        
        list.add(chromossome);
        for (int i = 1; i < population; i++) {
            IChromosome c = chromossome.CreateNew();
            c.Evaluate(function);
            list.add(c);
            
            if(c.getFitness() < minError){
                minError = c.getFitness();
                best = c.Clone();
            }
        }
    }
    
    /**
     * Run a epoch (Generation).
     */
    public void RunEpoch(){
        
        Random rand = new Random();
            
        //Crossover
        for (int i = 1; i < population; i+=2) {
            if(rand.nextFloat() < crossoverRate){
                
                //Selection
                int[] index = selection.Compute(list);
                
                List<IChromosome> elem = crossover.Compute(list.get(index[0]), list.get(index[1]));
                elem.get(0).Evaluate(function);
                elem.get(1).Evaluate(function);
                list.addAll(elem);
            }
        }
        
        //Mutation
        for (int i = 0; i < list.size(); i++) {
            if(rand.nextFloat() < mutationRate){
                IChromosome c = (IChromosome)mutation.Compute(list.get(i));
                c.Evaluate(function);
                list.add(c);
            }
        }
        
        //Find best chromossome
        IChromosome bTemp = FindBestChromossome(list);
        if(bTemp.getFitness() > minError){
            minError = bTemp.getFitness();
            best = bTemp.Clone();
        }
        
        //Sort the chromossomes
        if(autoShuffle)
            Collections.shuffle(list);
        else
            Sort();
        
        //Get the new population
        list = list.subList(0, population);
        
    }
    
    private IChromosome FindBestChromossome(List<IChromosome> list){
        
        IChromosome b = null;
        double f = -Double.MAX_VALUE;
        for (IChromosome c : list) {
            if(c.getFitness() > f){
                f = c.getFitness();
                b = c.Clone();
            }
        }
        
        return b;
        
    }
    
    private void Sort(){
        list.sort(new Comparator<IChromosome>() {

            @Override
            public int compare(IChromosome o1, IChromosome o2) {
                return Double.compare(o2.getFitness(), o1.getFitness());
            }
        });
    }
    
}