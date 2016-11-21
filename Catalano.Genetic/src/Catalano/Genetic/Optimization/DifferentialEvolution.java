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
    
    private double f;
    private double f2;
    private float prob;
    
    private double minError;
    private Strategy strategy;
    
    public double getError(){
        return minError;
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }
    
    public DifferentialEvolution(){
        this(100,100);
    }
    
    public DifferentialEvolution(int population, int generations){
        this(population, generations, 1.5);
    }
    
    public DifferentialEvolution(int population, int generations, double f){
        this(population, generations, f, 0.85f);
    }

    public DifferentialEvolution(int population, int generations, double f, float prob) {
        this(population, generations, f, prob, Strategy.RAND_1);
    }
    
    public DifferentialEvolution(int population, int generations, double f, float prob, Strategy strategy) {
        this.population = population;
        this.generations = generations;
        this.f = f;
        this.prob = prob;
        this.strategy = strategy;
    }
    
    @Override
    public double[] Compute(IObjectiveFunction function, List<DoubleRange> boundConstraint){
        
        switch(strategy){
            case RAND_1:
                return Rand(function, boundConstraint, strategy);
            case RAND_2:
                return Rand(function, boundConstraint, strategy);
            case BEST_1:
                return Best(function, boundConstraint, strategy);
            case BEST_2:
                return Best(function, boundConstraint, strategy);
        }
        
        return null;
        
    }
    
    private double[] Rand(IObjectiveFunction function, List<DoubleRange> boundConstraint, Strategy strategy){
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
        
        //Solution
        double[] trial = new double[pop[0].length];
        
        for (int g = 0; g < generations; g++) {
            for (int p = 0; p < pop.length; p++) {
                
                int var = rand.nextInt(pop[0].length + 1);

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
                if(fTrial < fitness[p]){
                    pop[p] = trial;
                    if(fTrial < minError) best = trial;
                    minError = Math.min(minError, fTrial);
                }
            }
        }
        
        return best;
    }
    
    private double[] Best(IObjectiveFunction function, List<DoubleRange> boundConstraint, Strategy strategy){
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
                if(fTrial < fitness[p]){
                    pop[p] = trial;
                    if(fTrial < minError) best = trial;
                    minError = Math.min(minError, fTrial);
                }
            }
        }
        return best;
    }
}