/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Catalano.Genetic;

import Catalano.Genetic.Crossover.ICrossover;
import Catalano.Genetic.Crossover.SinglePointCrossover;
import Catalano.Genetic.Fitness.IFitness;
import Catalano.Genetic.Mutation.BitFlipMutation;
import Catalano.Genetic.Mutation.IMutation;
import Catalano.Genetic.Selection.ISelection;
import Catalano.Genetic.Selection.RouletteWheelSelection;
import java.util.ArrayList;
import java.util.List;

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
    
    private int popCO;
    private int popMU;
    
    private IChromosome best;
    private double minError;

    public Population(IChromosome base, int population, IFitness function, float crossoverRate, float mutationRate) {
        this.population = population;
        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;
        Generate(base);
        
        popCO = (int)((crossoverRate * population) / 2);
        popMU = (int)(mutationRate * population);
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
    
    public void RunEpoch(IFitness function){
            
        //Crossover
        ICrossover crossover = new SinglePointCrossover();
        for (int i = 0; i < popCO; i++) {
            List<IChromosome> elem = crossover.Compute(list.get(0), list.get(1));
            list.addAll(elem);
        }
        
        //Mutation
        IMutation mutation = new BitFlipMutation();
        for (int i = 0; i < popMU; i++) {
            list.add((IChromosome)mutation.Compute(list.get(i)));
        }

        //Selection
        ISelection selection = new RouletteWheelSelection(population);
        int[] index = selection.Compute(list);
        
        
        
    }
    
}
