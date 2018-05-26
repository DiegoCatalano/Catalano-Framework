// Catalano Genetic Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2018
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
import Catalano.Math.Matrix;
import Catalano.Math.Tools;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Jaya Optimization.
 * 
 * References:
 * Rao, R. "Jaya: A simple and new optimization algorithm for solving constrained and unconstrained optimization problems."
 * International Journal of Industrial Engineering Computations 7.1 (2016): 19-34.
 * 
 * @author Diego Catalano
 */
public class JayaOptimization implements IOptimization{

    private int population;
    private int iterations;
    
    private int evals;
    private double minError;
    private double[] best;
    
    /**
     * Initializes a new instance of the JayaOptimization class.
     */
    public JayaOptimization() {
        this(100,1000);
    }

    /**
     * Initializes a new instance of the JayaOptimization class.
     * @param population Number of population.
     * @param iterations Number of iterations.
     */
    public JayaOptimization(int population, int iterations) {
        this.population = population;
        this.iterations = iterations;
    }

    @Override
    public int getNumberOfEvaluations() {
        return evals;
    }

    @Override
    public double[] Compute(ISingleObjectiveFunction function, List<DoubleRange> boundConstraint) {
        
        minError = Double.MAX_VALUE;
        evals = 0;
        
        Random rand = new Random();
        
        double[][] pop = new double[population][boundConstraint.size()];
        double[] fitness = new double[population];
        
        //Initialize the solutions
        for (int i = 0; i < pop.length; i++) {
            for (int j = 0; j < pop[0].length; j++) {
                DoubleRange range = boundConstraint.get(j);
                pop[i][j] = range.getMin() + rand.nextDouble() * (range.getMax() - range.getMin());
            }
        }
        
        //Compute fitness
        for (int i = 0; i < population; i++) {
            double f = function.Compute(pop[i]);
            evals++;
            fitness[i] = f;
            if(f < minError){
                minError = f;
                best = Arrays.copyOf(pop[i], pop[0].length);
            }
        }
        
        //Jaya algorithm
        for (int it = 0; it < iterations; it++) {
            
            //Worst solution
            double[] worst = pop[Matrix.MaxIndex(fitness)];
            
            for (int i = 0; i < pop.length; i++) {
                
                double[] newSolution = new double[pop[0].length];
                for (int j = 0; j < pop[0].length; j++) {
                    
                    //Jaya equation
                    newSolution[j] = pop[i][j] + (rand.nextDouble() * (best[j] - Math.abs(pop[i][j]))) - (rand.nextDouble() * (worst[j] - Math.abs(pop[i][j])));
                    
                    //Clamp values
                    newSolution[j] = Tools.Clamp(newSolution[j], boundConstraint.get(j));
                }
                
                double f = function.Compute(newSolution);
                evals++;
                
                if(f < fitness[i]){
                    pop[i] = newSolution;
                    fitness[i] = f;
                }
                if(f < minError){
                    minError = f;
                    best = Arrays.copyOf(newSolution, pop[0].length);
                }
            }
        }
        return best;
    }

    @Override
    public double getError() {
        return minError;
    }
    
}