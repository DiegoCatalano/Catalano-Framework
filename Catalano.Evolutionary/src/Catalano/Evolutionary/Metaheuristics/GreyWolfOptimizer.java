// Catalano Genetic Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
// Copyright © Seyedali Mirjalili, 2018
// ali.mirjalili at gmail.com
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

package Catalano.Evolutionary.Metaheuristics;

import Catalano.Core.DoubleRange;
import Catalano.Math.Matrix;
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
public class GreyWolfOptimizer extends BaseEvolutionaryOptimization {
    
    private double[] alpha;
    private double[] beta;
    private double[] delta;
    
    private double alphaScore;
    private double betaScore;
    private double deltaScore;

    /**
     * Initializes a new instance of the GreyWolfOptimizer class.
     */
    public GreyWolfOptimizer() {
        this(25, 1000);
    }

    /**
     * Initializes a new instance of the GreyWolfOptimizer class.
     * @param population Number of population.
     * @param generations Number of generations.
     */
    public GreyWolfOptimizer(int population, int generations) {
        this.populationSize = population;
        this.generations = generations;
    }

    @Override
    public void Compute(IObjectiveFunction function, List<DoubleRange> boundConstraint) {
        
        Random rand = new Random();
        
        int iter = 0;

        //Reset error
        minError = Double.MAX_VALUE;
        nEvals = 0;
        
        //Initialize the variables;
        alpha = new double[boundConstraint.size()];
        beta = new double[boundConstraint.size()];
        delta = new double[boundConstraint.size()];
        
        alphaScore = betaScore = deltaScore = Double.MAX_VALUE;
        
        //Initialize the population
        double[][] pop = InitializePopulation(populationSize, boundConstraint);
        
        
        //GWO start here
        for (int g = 0; g < generations; g++) {
            
            //Update alpha, beta and delta
            for (int i = 0; i < pop.length; i++) {
                
                double fitness = function.Compute(pop[i]);
                nEvals++;
                
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
            
            if(listener != null)
                listener.onIteration(g+1, minError);
            
        }
        
    }
    
    private double[][] InitializePopulation(int population, List<DoubleRange> boundConstraint){
        
        double[][] pop = new double[population][boundConstraint.size()];
        
        for (int i = 0; i < pop.length; i++) {
            pop[i] = Matrix.UniformRandom(boundConstraint);
        }
        
        return pop;
        
    }
}