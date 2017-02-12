/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Catalano.Genetic;

import Catalano.Genetic.Crossover.ICrossover;
import Catalano.Genetic.Fitness.IFitness;
import Catalano.Genetic.Mutation.IMutation;
import Catalano.Genetic.Selection.ISelection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Diego
 */
public class Population {
    
    private int population;
    private IFitness function;
    private float crossoverRate;
    private float mutationRate;
    private List<IChromosome> list;
    
    private ISelection selection;
    private ICrossover crossover;
    private IMutation mutation;
    private boolean autoShuffle = false;
    
    private IChromosome best;
    private double minError;

    public IChromosome getBest() {
        return best;
    }

    public boolean isAutoShuffle() {
        return autoShuffle;
    }

    public void setAutoShuffle(boolean autoShuffle) {
        this.autoShuffle = autoShuffle;
    }

    public Population(IChromosome base, int population, IFitness function, float crossoverRate, float mutationRate) {
        this.population = population;
        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;
        this.function = function;
        Generate(base);
    }
    
    public void setOperators(ISelection selection, ICrossover crossover, IMutation mutation){
        this.selection = selection;
        this.crossover = crossover;
        this.mutation = mutation;
    }
    
    private void Generate(IChromosome chromossome){
        list = new ArrayList<IChromosome>(population);
        
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