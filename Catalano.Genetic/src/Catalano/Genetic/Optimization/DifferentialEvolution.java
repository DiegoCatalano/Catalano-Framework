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

package Catalano.Genetic.Optimization;

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
 * @author Diego Catalano
 */
public class DifferentialEvolution implements IOptimization{
    
    /**
     * Strategy of algorihtm.
     */
    public static enum Strategy{
        
        /**
         * vi = xr1 + f1 * (xr2 - xr3)
         */
        RAND_1, 
        
        /**
         * vi = xr1 + f1 * (xr2 - xr3 + xr4 - xr5)
         */
        RAND_2, 
        
        /**
         * vi = xb1 + f1 * (xr2 - xr3)
         */
        BEST_1, 
        
        /**
         * vi = xb1 + f1 * (xr2 - xr3 + xr4 - xr5)
         */
        BEST_2, 
        
        /**
         * vi = xr1 + f1 * (xr2 - xr3) + f2 * (xb - xr1)
         */
        RAND_TO_BEST, 
        
        /**
         * vi = xi + f1 * (xr2 - xr3) + f2 * (xb - xi)
         */
        CURRENT_TO_BEST,
    
        /**
         * vi = xi + f1 * (xr2 - xr3) + f2 * (xr1 - xi)
         */
        CURRENT_TO_RAND};
    
    private int population;
    private int generations;
    private int nEval;
    
    private double f;
    private double f2;
    private float prob;
    
    private double minError;
    private Strategy strategy;
    
    @Override
    public double getError(){
        return minError;
    }

    @Override
    public int getNumberOfEvaluations() {
        return nEval;
    }

    /**
     * Get number of population.
     * @return Population.
     */
    public int getPopulation() {
        return population;
    }

    /**
     * Set number of population.
     * @param population Population.
     */
    public void setPopulation(int population) {
        this.population = population;
    }

    /**
     * Get number of generations.
     * @return Generations.
     */
    public int getGenerations() {
        return generations;
    }

    /**
     * Set number of generations.
     * @param generations Generations.
     */
    public void setGenerations(int generations) {
        this.generations = generations;
    }

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
        this(100,100);
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
        this(population, generations, f, prob, Strategy.RAND_1);
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
        this.population = population;
        this.generations = generations;
        this.f = f;
        this.prob = prob;
        this.strategy = strategy;
        this.f2 = f2;
    }
    
    @Override
    public double[] Compute(ISingleObjectiveFunction function, List<DoubleRange> boundConstraint){
        
        nEval = 0;
        
        if(strategy == Strategy.RAND_1 || strategy == Strategy.RAND_2){
            return Rand(function, boundConstraint, strategy);
        }
        else{
            return Best(function, boundConstraint, strategy);
        }
        
    }
    
    private double[] Rand(ISingleObjectiveFunction function, List<DoubleRange> boundConstraint, Strategy strategy){
        
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
        
        //Best of the all solution
        double[] best = new double[pop[0].length];
        minError = fitness[0];
        
        for (int g = 0; g < generations; g++) {
            for (int p = 0; p < pop.length; p++) {
                
                int var = rand.nextInt(pop[0].length + 1);
                
                double[] trial = new double[pop[0].length];

                int[] idx;
                switch(strategy){
                    case RAND_1:
                        idx = Matrix.Indices(0, pop.length);
                        ArraysUtil.Shuffle(idx);
                        idx = Arrays.copyOf(idx, 3);
                        for (int i = 0; i < trial.length; i++) {
                            if(rand.nextDouble() <= prob || i == var){
                                trial[i] = pop[idx[0]][i] + f * (pop[idx[1]][i] - pop[idx[2]][i]);
                            }
                            else{
                                trial[i] = pop[p][i];
                            }
                        }
                    break;
                    case RAND_2:{
                        idx = Matrix.Indices(0, pop.length);
                        ArraysUtil.Shuffle(idx);
                        idx = Arrays.copyOf(idx, 5);
                        for (int i = 0; i < trial.length; i++) {
                            if(rand.nextDouble() <= prob || i == var){
                                trial[i] = pop[idx[0]][i] + f * (pop[idx[1]][i] - pop[idx[2]][i] + pop[idx[3]][i] - pop[idx[4]][i]);
                            }
                            else{
                                trial[i] = pop[p][i];
                            }
                        }
                    }
                }
                
                //Fix constraint
                for (int i = 0; i < trial.length; i++) {
                    trial[i] = trial[i] < boundConstraint.get(i).getMin() ? boundConstraint.get(i).getMin() : trial[i];
                    trial[i] = trial[i] > boundConstraint.get(i).getMax() ? boundConstraint.get(i).getMax() : trial[i];
                }

                double fTrial = function.Compute(trial);
                nEval++;
                if(fTrial < fitness[p]){
                    pop[p] = trial;
                    fitness[p] = fTrial;
                    if(fTrial < minError) {
                        best = trial;
                        minError = fTrial;
                    }
                }
            }
        }
        return best;
    }
    
    private double[] Best(ISingleObjectiveFunction function, List<DoubleRange> boundConstraint, Strategy strategy){
        Random rand = new Random();
        
        //Generate the population
        double[][] pop = new double[population][boundConstraint.size()];
        for (int i = 0; i < pop.length; i++) {
            for (int j = 0; j < pop[0].length; j++) {
                DoubleRange range = boundConstraint.get(j);
                pop[i][j] = range.getMin() + rand.nextDouble() * (range.getMax() - range.getMin());
            }
        }
        
        double[] best = new double[pop[0].length];
        minError = Double.MAX_VALUE;
        
        //Compute fitness
        double[] fitness = new double[pop.length];
        for (int i = 0; i < fitness.length; i++) {
            fitness[i] = function.Compute(pop[i]);
            if(fitness[i] < minError){
                minError = fitness[i];
                best = pop[i];
            }
        }
        
        for (int g = 0; g < generations; g++) {
            for (int p = 0; p < pop.length; p++) {
                
                int var = rand.nextInt(pop[0].length + 1);

                double[] trial = new double[pop[0].length];

                int[] idx;
                switch(strategy){
                    case BEST_1:
                        idx = Matrix.Indices(0, pop.length);
                        ArraysUtil.Shuffle(idx);
                        idx = Arrays.copyOf(idx, 2);
                        for (int i = 0; i < trial.length; i++) {
                            if(rand.nextDouble() <= prob || i == var){
                                trial[i] = best[i] + f * (pop[idx[0]][i] - pop[idx[1]][i]);
                            }
                            else{
                                trial[i] = pop[p][i];
                            }
                        }
                    break;
                    case BEST_2:{
                        idx = Matrix.Indices(0, pop.length);
                        ArraysUtil.Shuffle(idx);
                        idx = Arrays.copyOf(idx, 4);
                        for (int i = 0; i < trial.length; i++) {
                            if(rand.nextDouble() <= prob || i == var){
                                trial[i] = best[i] + f * (pop[idx[0]][i] - pop[idx[1]][i] + pop[idx[2]][i] - pop[idx[3]][i]);
                            }
                            else{
                                trial[i] = pop[p][i];
                            }
                        }
                    }
                    case RAND_TO_BEST:{
                        idx = Matrix.Indices(0, pop.length);
                        ArraysUtil.Shuffle(idx);
                        idx = Arrays.copyOf(idx, 4);
                        for (int i = 0; i < trial.length; i++) {
                            if(rand.nextDouble() <= prob || i == var){
                                trial[i] = pop[idx[1]][i] + f * (pop[idx[2]][i] - pop[idx[3]][i]) + f2 * (best[i] - pop[idx[0]][i]);
                            }
                            else{
                                trial[i] = pop[p][i];
                            }
                        }
                    }
                    case CURRENT_TO_BEST:{
                        idx = Matrix.Indices(0, pop.length);
                        ArraysUtil.Shuffle(idx);
                        idx = Arrays.copyOf(idx, 2);
                        for (int i = 0; i < trial.length; i++) {
                            if(rand.nextDouble() <= prob || i == var){
                                trial[i] = pop[p][i] + f * (pop[idx[0]][i] - pop[idx[1]][i]) + f2 * (best[i] - pop[p][i]);
                            }
                            else{
                                trial[i] = pop[p][i];
                            }
                        }
                    }
                    case CURRENT_TO_RAND:{
                        idx = Matrix.Indices(0, pop.length);
                        ArraysUtil.Shuffle(idx);
                        idx = Arrays.copyOf(idx, 3);
                        for (int i = 0; i < trial.length; i++) {
                            if(rand.nextDouble() <= prob || i == var){
                                trial[i] = pop[p][i] + f * (pop[idx[1]][i] - pop[idx[2]][i]) + f2 * (pop[idx[0]][i] - pop[p][i]);
                            }
                            else{
                                trial[i] = pop[p][i];
                            }
                        }
                    }
                }
                
                //Fix constraint
                for (int i = 0; i < trial.length; i++) {
                    trial[i] = trial[i] < boundConstraint.get(i).getMin() ? boundConstraint.get(i).getMin() : trial[i];
                    trial[i] = trial[i] > boundConstraint.get(i).getMax() ? boundConstraint.get(i).getMax() : trial[i];
                }

                double fTrial = function.Compute(trial);
                nEval++;
                if(fTrial < fitness[p]){
                    pop[p] = trial;
                    fitness[p] = fTrial;
                    if(fTrial < minError){
                        best = trial;
                        minError = fTrial;
                    }
                }
            }
        }
        return best;
    }
}