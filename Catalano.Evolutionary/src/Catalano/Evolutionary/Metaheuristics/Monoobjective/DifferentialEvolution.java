// Catalano Genetic Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
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

package Catalano.Evolutionary.Metaheuristics.Monoobjective;

import Catalano.Core.ArraysUtil;
import Catalano.Core.DoubleRange;
import Catalano.Math.Matrix;
import Catalano.Math.Random.Random;
import java.util.Arrays;
import java.util.List;

/**
 * Differential Evolution (DE).
 * 
 * Differential evolution (DE) is a method that optimizes a problem by iteratively trying to
 * improve a candidate solution with regard to a given measure of quality.
 * 
 * 
 * @author Diego Catalano
 */
public class DifferentialEvolution extends BaseEvolutionaryOptimization{    
    
    /**
     * Strategy of algorihtm.
     */
    public static enum Strategy{
        
        /**
         * vi = xr1 + f1 * (xr2 - xr3)
         */
        RAND_1_BIN, 
        
        /**
         * vi = xr1 + f1 * (xr2 - xr3 + xr4 - xr5)
         */
        RAND_2_BIN, 
        
        /**
         * RAND/1/Exp
         */
        RAND_1_EXP, 
        
        /**
         * RAND/2/Exp
         */
        RAND_2_EXP, 
        
        /**
         * vi = xb1 + f1 * (xr2 - xr3)
         */
        BEST_1_BIN, 
        
        /**
         * vi = xb1 + f1 * (xr2 - xr3 + xr4 - xr5)
         */
        BEST_2_BIN, 
        
        /**
         * vi = xb1 + f1 * (xr2 - xr3)
         */
        BEST_1_EXP, 
        
        /**
         * vi = xb1 + f1 * (xr2 - xr3 + xr4 - xr5)
         */
        BEST_2_EXP, 
        
        /**
         * vi = xr1 + f1 * (xr2 - xr3) + f2 * (xb - xr1)
         */
        RAND_TO_BEST_BIN, 
        
        /**
         * vi = xi + f1 * (xr2 - xr3) + f2 * (xb - xi)
         */
        CURRENT_TO_BEST_BIN,
    
        /**
         * vi = xi + f1 * (xr2 - xr3) + f2 * (xr1 - xi)
         */
        CURRENT_TO_RAND_BIN};
    
    private double f;
    private double f2;
    private float prob;
    
    private Strategy strategy;

    /**
     * Get mutation factor.
     * @return Mutation factor.
     */
    public double getF() {
        return f;
    }

    /**
     * Set mutation factor.
     * @param f Mutation factor.
     */
    public void setF(double f) {
        this.f = f;
    }
    
    /**
     * Get crossover probability.
     * @return Crossover probability.
     */
    public float getCrossoverProbability(){
        return prob;
    }
    
    /**
     * Set crossover probability.
     * @param prob Probability.
     */
    public void setCrossoverProbability(float prob){
        this.prob = prob;
    }

    /**
     * Get strategy.
     * @return Strategy.
     */
    public Strategy getStrategy() {
        return strategy;
    }

    /**
     * Set strategy.
     * @param strategy Strategy.
     */
    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }
    
    /**
     * Initializes a new instance of the DifferentialEvolution class.
     */
    public DifferentialEvolution(){
        this(100,1000);
    }
    
    /**
     * Initializes a new instance of the DifferentialEvolution class.
     * @param population Population.
     * @param generations Generations.
     */
    public DifferentialEvolution(int population, int generations){
        this(population, generations, 0.5);
    }
    
    /**
     * Initializes a new instance of the DifferentialEvolution class.
     * @param population Population.
     * @param generations Generations.
     * @param f Mutation factor.
     */
    public DifferentialEvolution(int population, int generations, double f){
        this(population, generations, f, 0.85f);
    }

    /**
     * Initializes a new instance of the DifferentialEvolution class.
     * @param population Population.
     * @param generations Generations.
     * @param f Mutation factor.
     * @param prob Crossover probability.
     */
    public DifferentialEvolution(int population, int generations, double f, float prob) {
        this(population, generations, f, prob, Strategy.RAND_1_BIN);
    }
    
    /**
     * Initializes a new instance of the DifferentialEvolution class.
     * @param population Population.
     * @param generations Generations.
     * @param f Mutation factor.
     * @param prob Crossover probability.
     * @param strategy Strategy algorithm.
     */
    public DifferentialEvolution(int population, int generations, double f, float prob, Strategy strategy) {
        this(population, generations, f, prob, strategy, f);
    }
    
    /**
     * Initializes a new instance of the DifferentialEvolution class.
     * @param population Population size.
     * @param generations Generations.
     * @param f Mutation factor.
     * @param prob Crossover probability.
     * @param strategy Strategy.
     * @param f2 Mutation factor 2 (RAND_TO_BEST and CURRENT_) only.
     */
    public DifferentialEvolution(int population, int generations, double f, float prob, Strategy strategy, double f2) {
        this.populationSize = population;
        this.generations = generations;
        this.f = f;
        this.prob = prob;
        this.strategy = strategy;
        this.f2 = f2;
    }
    
    @Override
    public void Compute(IObjectiveFunction function, List<DoubleRange> boundConstraint){
        
        nEvals = 0;
        minError = Double.MAX_VALUE;
        
        if(strategy == Strategy.RAND_1_BIN || strategy == Strategy.RAND_2_BIN || strategy == Strategy.RAND_1_EXP || strategy == Strategy.RAND_2_EXP){
            Rand(function, boundConstraint, strategy);
        }
        else{
            Best(function, boundConstraint, strategy);
        }
        
    }
    
    private void Rand(IObjectiveFunction function, List<DoubleRange> boundConstraint, Strategy strategy){
        
        Random rand = new Random();
        
        //Generate the population
        double[][] pop = new double[populationSize][boundConstraint.size()];
        for (int i = 0; i < pop.length; i++) {
            pop[i] = Matrix.UniformRandom(boundConstraint);
        }

        //Compute fitness
        double[] fitness = new double[pop.length];
        for (int i = 0; i < fitness.length; i++) {
            fitness[i] = function.Compute(pop[i]);
            if(fitness[i] < minError){
                minError = fitness[i];
                best = pop[i];
            }
        }

        int[] idx = Matrix.Indices(0, pop.length);        
        for (int g = 0; g < generations; g++) {
            for (int p = 0; p < pop.length; p++) {
                
                int var = rand.nextInt(pop[0].length);
                
                double[] trial = new double[pop[0].length];

                switch(strategy){
                    case RAND_1_BIN:
                        ArraysUtil.Shuffle(idx);
                        for (int i = 0; i < trial.length; i++) {
                            if(rand.nextDouble() <= prob || i == var){
                                trial[i] = pop[idx[0]][i] + f * (pop[idx[1]][i] - pop[idx[2]][i]);
                            }
                            else{
                                trial[i] = pop[p][i];
                            }
                        }
                    break;
                    case RAND_2_BIN:{
                        ArraysUtil.Shuffle(idx);
                        for (int i = 0; i < trial.length; i++) {
                            if(rand.nextDouble() <= prob || i == var){
                                trial[i] = pop[idx[0]][i] + f * (pop[idx[1]][i] - pop[idx[2]][i] + pop[idx[3]][i] - pop[idx[4]][i]);
                            }
                            else{
                                trial[i] = pop[p][i];
                            }
                        }
                    }
                    case RAND_1_EXP:{
                        ArraysUtil.Shuffle(idx);
                        int l = 0;
                        
                        //Mutation vector
                        double[] mv = Arrays.copyOf(pop[p], pop[0].length);
                        for (int i = 0; i < trial.length; i++) {
                            mv[i] = pop[idx[0]][i] + f * (pop[idx[1]][i] - pop[idx[2]][i]);
                        }
                        
                        trial = Arrays.copyOf(pop[p], pop[0].length);
                        do {
                            trial[var] = mv[var];
                            l++;
                            var = (var + 1) % pop[0].length;
                        } while (rand.nextDouble() <= prob && l < pop[0].length);
                    }
                    case RAND_2_EXP:{
                        ArraysUtil.Shuffle(idx);
                        int l = 0;
                        
                        //Mutation vector
                        double[] mv = Arrays.copyOf(pop[p], pop[0].length);
                        for (int i = 0; i < trial.length; i++) {
                            mv[i] = pop[idx[0]][i] + f * (pop[idx[1]][i] - pop[idx[2]][i] + pop[idx[3]][i] - pop[idx[4]][i]);
                        }
                        
                        trial = Arrays.copyOf(pop[p], pop[0].length);
                        do {
                            trial[var] = mv[var];
                            l++;
                            var = (var + 1) % pop[0].length;
                        } while (rand.nextDouble() <= prob && l < pop[0].length);
                    }
                }
                
                //Fix constraint
                for (int i = 0; i < trial.length; i++) {
                    trial[i] = trial[i] < boundConstraint.get(i).getMin() ? boundConstraint.get(i).getMin() : trial[i];
                    trial[i] = trial[i] > boundConstraint.get(i).getMax() ? boundConstraint.get(i).getMax() : trial[i];
                }

                double fTrial = function.Compute(trial);
                nEvals++;
                if(fTrial < fitness[p]){
                    pop[p] = trial;
                    fitness[p] = fTrial;
                    if(fTrial < minError) {
                        best = trial;
                        minError = fTrial;
                    }
                }
            }
            
            if(listener != null)
                listener.onIteration(g+1, minError);
            
        }
    }
    
    private void Best(IObjectiveFunction function, List<DoubleRange> boundConstraint, Strategy strategy){
        
        Random rand = new Random();

        //Generate the population
        double[][] pop = new double[populationSize][boundConstraint.size()];
        for (int i = 0; i < pop.length; i++) {
            pop[i] = Matrix.UniformRandom(boundConstraint);
        }

        //Compute fitness
        double[] fitness = new double[pop.length];
        for (int i = 0; i < fitness.length; i++) {
            fitness[i] = function.Compute(pop[i]);
            if(fitness[i] < minError){
                minError = fitness[i];
                best = pop[i];
            }
        }
        
        int[] idx = Matrix.Indices(0, pop.length);
        for (int g = 0; g < generations; g++) {
            for (int p = 0; p < pop.length; p++) {
                
                int var = rand.nextInt(pop[0].length + 1);

                double[] trial = new double[pop[0].length];

                switch(strategy){
                    case BEST_1_BIN:
                        ArraysUtil.Shuffle(idx);
                        for (int i = 0; i < trial.length; i++) {
                            if(rand.nextDouble() <= prob || i == var){
                                trial[i] = best[i] + f * (pop[idx[0]][i] - pop[idx[1]][i]);
                            }
                            else{
                                trial[i] = pop[p][i];
                            }
                        }
                    break;
                    case BEST_2_BIN:{
                        ArraysUtil.Shuffle(idx);
                        for (int i = 0; i < trial.length; i++) {
                            if(rand.nextDouble() <= prob || i == var){
                                trial[i] = best[i] + f * (pop[idx[0]][i] - pop[idx[1]][i] + pop[idx[2]][i] - pop[idx[3]][i]);
                            }
                            else{
                                trial[i] = pop[p][i];
                            }
                        }
                    break;
                    }
                    case BEST_1_EXP:{
                        ArraysUtil.Shuffle(idx);
                        int l = 0;
                        
                        //Mutation vector
                        double[] mv = Arrays.copyOf(pop[p], pop[0].length);
                        for (int i = 0; i < trial.length; i++) {
                            mv[i] = best[i] + f * (pop[idx[0]][i] - pop[idx[1]][i]);
                        }
                        
                        trial = Arrays.copyOf(pop[p], pop[0].length);
                        do {
                            trial[var] = mv[var];
                            l++;
                            var = (var + 1) % pop[0].length;
                        } while (rand.nextDouble() <= prob && l < pop[0].length);
                    break;
                    }
                    case BEST_2_EXP:{
                        ArraysUtil.Shuffle(idx);
                        int l = 0;
                        
                        //Mutation vector
                        double[] mv = Arrays.copyOf(pop[p], pop[0].length);
                        for (int i = 0; i < trial.length; i++) {
                            mv[i] = best[i] + f * (pop[idx[0]][i] - pop[idx[1]][i] + pop[idx[2]][i] - pop[idx[3]][i]);
                        }
                        
                        trial = Arrays.copyOf(pop[p], pop[0].length);
                        do {
                            trial[var] = mv[var];
                            l++;
                            var = (var + 1) % pop[0].length;
                        } while (rand.nextDouble() <= prob && l < pop[0].length);
                    break;
                    }
                    case RAND_TO_BEST_BIN:{
                        ArraysUtil.Shuffle(idx);
                        for (int i = 0; i < trial.length; i++) {
                            if(rand.nextDouble() <= prob || i == var){
                                trial[i] = pop[idx[1]][i] + f * (pop[idx[2]][i] - pop[idx[3]][i]) + f2 * (best[i] - pop[idx[0]][i]);
                            }
                            else{
                                trial[i] = pop[p][i];
                            }
                        }
                    break;
                    }
                    case CURRENT_TO_BEST_BIN:{
                        ArraysUtil.Shuffle(idx);
                        for (int i = 0; i < trial.length; i++) {
                            if(rand.nextDouble() <= prob || i == var){
                                trial[i] = pop[p][i] + f * (pop[idx[0]][i] - pop[idx[1]][i]) + f2 * (best[i] - pop[p][i]);
                            }
                            else{
                                trial[i] = pop[p][i];
                            }
                        }
                    break;
                    }
                    case CURRENT_TO_RAND_BIN:{
                        ArraysUtil.Shuffle(idx);
                        for (int i = 0; i < trial.length; i++) {
                            if(rand.nextDouble() <= prob || i == var){
                                trial[i] = pop[p][i] + f * (pop[idx[1]][i] - pop[idx[2]][i]) + f2 * (pop[idx[0]][i] - pop[p][i]);
                            }
                            else{
                                trial[i] = pop[p][i];
                            }
                        }
                    break;
                    }
                }
                
                //Fix constraint
                for (int i = 0; i < trial.length; i++) {
                    trial[i] = trial[i] < boundConstraint.get(i).getMin() ? boundConstraint.get(i).getMin() : trial[i];
                    trial[i] = trial[i] > boundConstraint.get(i).getMax() ? boundConstraint.get(i).getMax() : trial[i];
                }

                double fTrial = function.Compute(trial);
                nEvals++;
                if(fTrial < fitness[p]){
                    pop[p] = trial;
                    fitness[p] = fTrial;
                    if(fTrial < minError){
                        best = trial;
                        minError = fTrial;
                    }
                }
            }
            
            if(listener != null)
                listener.onIteration(g+1, minError);
            
        }
    }
}