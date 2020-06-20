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
import Catalano.Evolutionary.Genetic.Reinsertion.GlobalElistismReinsertion;
import Catalano.Evolutionary.Genetic.Reinsertion.IReinsertion;
import Catalano.Evolutionary.Genetic.Selection.ISelection;
import java.util.ArrayList;
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
    
    private IFitness function;
    private List<IChromosome> list;
    
    private ISelection selection;
    private ICrossover crossover;
    private IMutation mutation;
    private IReinsertion reinsertion;
    
    private IChromosome best;
    private double minError;
    private long nEvals;

    /**
     * Get population size.
     * @return Population size.
     */
    public int getPopulationSize() {
        return population;
    }

    /**
     * Get crossover rate.
     * @return Crossover rate.
     */
    public float getCrossoverRate() {
        return crossoverRate;
    }

    /**
     * Set crossover rate.
     * @param crossoverRate Crossover rate.
     */
    public void setCrossoverRate(float crossoverRate) {
        this.crossoverRate = crossoverRate;
    }

    /**
     * Get best chromosome.
     * @return Best chromosome.
     */
    public IChromosome getBest() {
        return best;
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
     * Get reinsertion method.
     * @return Reinsertion method.
     */
    public IReinsertion getReinsertion() {
        return reinsertion;
    }

    /**
     * Set reinsertion method.
     * @param reinsertion Reinsertionn method.
     */
    public void setReinsertion(IReinsertion reinsertion) {
        this.reinsertion = reinsertion;
    }

    /**
     * Get number of evaluations.
     * @return Number of evaluations.
     */
    public long getNumberOfEvaluations() {
        return nEvals;
    }

    /**
     * Initializes a new instance of the Population class.
     * @param base Chromosome base.
     * @param population Size of population.
     * @param function Function to be optimized.
     * @param crossoverRate Crossover rate.
     */
    public Population(IChromosome base, int population, IFitness function, float crossoverRate) {
        this.population = population;
        this.crossoverRate = crossoverRate;
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
        this.reinsertion = new GlobalElistismReinsertion();
    }
    
    /**
     * Set all operators in the population.
     * @param selection Selection method.
     * @param crossover Crossover method.
     * @param mutation Mutation method.
     * @param reinsertion Reinsertion method.
     */
    public void setOperators(ISelection selection, ICrossover crossover, IMutation mutation, IReinsertion reinsertion){
        this.selection = selection;
        this.crossover = crossover;
        this.mutation = mutation;
        this.reinsertion = reinsertion;
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
        
        List<IChromosome> newPop = new ArrayList<>();
            
        //Crossover
        for (int i = 1; i < population; i+=2) {
            if(rand.nextFloat() < crossoverRate){
                
                //Selection
                int[] index = selection.Compute(list);
                
                List<IChromosome> elem = crossover.Compute(list.get(index[0]), list.get(index[1]));
                newPop.addAll(elem);
            }
            else{
                newPop.add(list.get(i-1).Clone());
                newPop.add(list.get(i).Clone());
            }
        }
        
        //Mutation
        int size = newPop.size();
        for (int i = 0; i < size; i++) {
            IChromosome c = (IChromosome)mutation.Compute(newPop.get(i));
            c.Evaluate(function);
            newPop.set(i, c);
            nEvals++;
        }
        
        list = reinsertion.Compute(this, list, newPop);
        
        //Find best chromossome
        IChromosome bTemp = FindBestChromossome(list);
        if(bTemp.getFitness() > minError){
            minError = bTemp.getFitness();
            best = bTemp.Clone();
        }
        
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
    
    private void Sort(List<IChromosome> list){
        list.sort(new Comparator<IChromosome>() {

            @Override
            public int compare(IChromosome o1, IChromosome o2) {
                return Double.compare(o2.getFitness(), o1.getFitness());
            }
        });
    }
    
}