/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Catalano.Genetic.Optimization;

import Catalano.Core.ArraysUtil;
import Catalano.Core.DoubleRange;
import Catalano.Math.Matrix;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Diego Catalano
 */
public class DifferentialEvolution {
    
    private int population;
    private int generations;
    
    private double f;
    private double prob;
    
    private double minError;
    
    public double getError(){
        return minError;
    }
    
    public DifferentialEvolution(){
        this(100,100,0.2,0.3);
    }

    public DifferentialEvolution(int population, int generations, double f, double prob) {
        this.population = population;
        this.generations = generations;
        this.f = f;
        this.prob = prob;
    }
    
    public double[] Compute(IObjectiveFunction function, List<DoubleRange> boundConstraint){
        
        Random rand = new Random();
        
        //Generate the population
        double[][] pop = new double[population][boundConstraint.size()];
        for (int i = 0; i < pop.length; i++) {
            for (int j = 0; j < pop[0].length; j++) {
                DoubleRange range = boundConstraint.get(j);
                pop[i][j] = range.getMin() + rand.nextDouble() * (range.getMax() - range.getMin());
            }
        }
        
        //Compute fitness
        double[] fitness = new double[pop.length];
        for (int i = 0; i < fitness.length; i++) {
            fitness[i] = function.Compute(pop[i]);
        }
        
        double[] best = new double[pop[0].length];
        
        for (int g = 0; g < generations; g++) {
            for (int p = 0; p < pop.length; p++) {

                //Select 3 random individual
                int[] idx = Matrix.CreateMatrix1D(0, fitness.length);
                ArraysUtil.Shuffle(idx);
                idx = Arrays.copyOf(idx, 3);

                //Select a random feature
                int var = rand.nextInt(pop[0].length + 1);

                double[] trial = new double[pop[0].length];
                for (int i = 0; i < trial.length; i++) {
                    if(rand.nextDouble() <= prob || i == var){
                        trial[i] = pop[idx[0]][i] + f * (pop[idx[1]][i] - pop[idx[2]][i]);
                    }
                    else{
                        trial[i] = pop[p][i];
                    }
                }

                double fTrial = function.Compute(trial);
                if(fTrial < fitness[p]){
                    pop[p] = trial;
                    best = trial;
                    minError = fTrial;
                }
            }
        }
        
        return pop[Matrix.MinIndex(fitness)];
    }
}