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

package Catalano.Evolutionary.Metaheuristics;

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
public class JayaOptimization extends BaseEvolutionaryOptimization {
    
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
        this.populationSize = population;
        this.generations = iterations;
    }

    @Override
    public void Compute(IObjectiveFunction function, List<DoubleRange> boundConstraint) {
        
        minError = Double.MAX_VALUE;
        nEvals = 0;
        
        Random rand = new Random();
        
        //Generate the individuals
        double[][] pop = new double[populationSize][boundConstraint.size()];
        for (int i = 0; i < pop.length; i++) {
            pop[i] = Matrix.UniformRandom(boundConstraint);
        }

        //Compute fitness
        double[] fitness = new double[pop.length];
        for (int i = 0; i < fitness.length; i++){
            fitness[i] = function.Compute(pop[i]);
            if(fitness[i] < minError){
                minError = fitness[i];
                best = Arrays.copyOf(pop[i], pop[0].length);
            }
        }
        
        
        //Jaya algorithm
        for (int it = 0; it < generations; it++) {
            
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
                nEvals++;
                
                if(f < fitness[i]){
                    pop[i] = newSolution;
                    fitness[i] = f;
                }
                if(f < minError){
                    minError = f;
                    best = Arrays.copyOf(newSolution, pop[0].length);
                }
            }
            
            if(listener != null)
                listener.onIteration(it+1, minError);
            
        }
        
    }

    @Override
    public double getError() {
        return minError;
    }
    
}