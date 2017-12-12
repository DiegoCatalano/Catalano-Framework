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

import Catalano.Core.DoubleRange;
import java.util.List;
import java.util.Random;

/**
 * Grey Wolf Optimization (GWO).
 * 
 * The GWO algorithm mimics the leadership hierarchy and hunting mechanism of grey wolves in nature.
 * 
 * References:
 * Mirjalili, Seyedali, Seyed Mohammad Mirjalili, and Andrew Lewis.
 * "Grey wolf optimizer." Advances in Engineering Software 69 (2014): 46-61.
 * 
 * @author Diego Catalano
 */
public class GreyWolfOptimizer implements IOptimization{
    
    private int population;
    private int generations;
    private int eval;
    
    private double[] alpha;
    private double[] beta;
    private double[] delta;
    
    private double alphaScore;
    private double betaScore;
    private double deltaScore;
    
    private double minError;
    
    @Override
    public int getNumberOfEvaluations() {
        return eval;
    }
    
    @Override
    public double getError() {
        return minError;
    }

    /**
     * Initializes a new instance of the GreyWolfOptimizer class.
     */
    public GreyWolfOptimizer() {}

    /**
     * Initializes a new instance of the GreyWolfOptimizer class.
     * @param population Number of population.
     * @param generations Number of generations.
     */
    public GreyWolfOptimizer(int population, int generations) {
        this.population = population;
        this.generations = generations;
    }

    @Override
    public double[] Compute(ISingleObjectiveFunction function, List<DoubleRange> boundConstraint) {
        
        Random rand = new Random();
        
        int iter = 0;

        //Reset error
        minError = Double.MAX_VALUE;
        eval = 0;
        
        //Initialize the variables;
        alpha = new double[boundConstraint.size()];
        beta = new double[boundConstraint.size()];
        delta = new double[boundConstraint.size()];
        
        alphaScore = betaScore = deltaScore = Double.MAX_VALUE;
        
        //Initialize the population
        double[][] pop = InitializePopulation(population, boundConstraint);
        
        //Best solution
        double[] best = new double[pop[0].length];
        
        //GWO start here
        for (int g = 0; g < generations; g++) {
            
            //Update alpha, beta and delta
            for (int i = 0; i < pop.length; i++) {
                
                double fitness = function.Compute(pop[i]);
                eval++;
                
                if(fitness < alphaScore){
                    alphaScore = fitness;
                    alpha = pop[i];
                }
                
                if(fitness > alphaScore && fitness < betaScore){
                    betaScore = fitness;
                    beta = pop[i];
                }
                
                if(fitness > alphaScore && fitness > betaScore && fitness < deltaScore){
                    deltaScore = fitness;
                    delta = pop[i];
                }
                
                if(fitness < minError){
                    minError = fitness;
                    best = pop[i];
                }
                
            }
            
            //Linear decrease
            double a = 2D - (double)iter *(2D/(double)generations);
            
            //Update the population
            for (int i = 0; i < pop.length; i++) {
                for (int j = 0; j < pop[0].length; j++) {
                    
                    double a1 = 2 * a * rand.nextDouble() - a;
                    double c1 = 2 * rand.nextDouble();
                    
                    double d_alpha = Math.abs(c1 * alpha[j] - pop[i][j]);
                    double x1 = alpha[j] - a1 * d_alpha;
                    
                    double a2 = 2 * a * rand.nextDouble() - a;
                    double c2 = 2 * rand.nextDouble();

                    double d_beta = Math.abs(c2 * beta[j]-pop[i][j]);
                    double x2 = beta[j] - a2 * d_beta;

                    double a3 = 2 * a * rand.nextDouble() - a;
                    double c3 = 2 * rand.nextDouble();

                    double d_delta = Math.abs(c3 * delta[j] - pop[i][j]);
                    double x3 = delta[j] - a3 * d_delta;
                    
                    double val = (x1+x2+x3)/3;
                    
                    DoubleRange bounds = boundConstraint.get(j);
                    
                    //Clamp values
                    if(val > bounds.getMax())
                        val = bounds.getMax();
                    if(val < bounds.getMin())
                        val = bounds.getMin();
                    
                    pop[i][j] = val;
                    
                }
            }
            
            iter++;
            
        }
        
        return best;
        
    }
    
    private double[][] InitializePopulation(int population, List<DoubleRange> boundConstraint){
        
        double[][] pop = new double[population][boundConstraint.size()];
        
        Random rand = new Random();
        
        for (int i = 0; i < pop.length; i++) {
            for (int j = 0; j < pop[0].length; j++) {
                DoubleRange range = boundConstraint.get(j);
                pop[i][j] = range.getMin() + rand.nextDouble() * (range.getMax() - range.getMin());
            }
        }
        
        return pop;
        
    }
}