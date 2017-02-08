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
    
    private int popCO;
    private int popMU;
    
    private IChromosome best;
    private double minError;

    public IChromosome getBest() {
        return best;
    }

    public Population(IChromosome base, int population, IFitness function, float crossoverRate, float mutationRate) {
        this.population = population;
        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;
        this.function = function;
        Generate(base);
        
        popCO = (int)((crossoverRate * (float)population) / 2f);
        popMU = (int)(mutationRate * (float)population);
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

        //Selection
        int[] index = selection.Compute(list);
            
        //Crossover
        if(rand.nextFloat() <= crossoverRate){
            List<IChromosome> elem = crossover.Compute(list.get(index[0]), list.get(index[1]));
            elem.get(0).Evaluate(function);
            elem.get(1).Evaluate(function);
            list.addAll(elem);
        }
        
        //Mutation
        if(rand.nextFloat() <= mutationRate){
            IChromosome c1 = (IChromosome)mutation.Compute(list.get(index[0]));
            IChromosome c2 = (IChromosome)mutation.Compute(list.get(index[1]));
            c1.Evaluate(function);
            c2.Evaluate(function);
            list.add(c1);
            list.add(c2);
        }
        
        //Sort the chromossomes
        Sort();
        
        //Get the new population
        list = list.subList(0, population);
        
        if(list.get(0).getFitness() > minError){
            best = list.get(0).Clone();
            minError = list.get(0).getFitness();
        }
        
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