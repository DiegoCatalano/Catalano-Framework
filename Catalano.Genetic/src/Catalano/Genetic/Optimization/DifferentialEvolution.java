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
public class DifferentialEvolution {
    
    private int population;
    private int generations;
    
    private double f;
    private float prob;
    
    private double minError;
    
    public double getError(){
        return minError;
    }
    
    public DifferentialEvolution(){
        this(100,100,0.5,0.85f);
    }
    
    public DifferentialEvolution(int population, int generations){
        this(population, generations, 1.5);
    }
    
    public DifferentialEvolution(int population, int generations, double f){
        this(population, generations, f, 0.85f);
    }

    public DifferentialEvolution(int population, int generations, double f, float prob) {
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
        minError = fitness[0];
        
        for (int g = 0; g < generations; g++) {
            for (int p = 0; p < pop.length; p++) {

                //Select 3 random individual
                int[] idx = Matrix.Indices(0, fitness.length);
                ArraysUtil.Shuffle(idx);
                idx = Arrays.copyOf(idx, 3);

                //Select a random feature
                int var = rand.nextInt(pop[0].length + 1);

                //Crossover
                double[] trial = new double[pop[0].length];
                for (int i = 0; i < trial.length; i++) {
                    if(rand.nextDouble() <= prob || i == var){
                        trial[i] = pop[idx[0]][i] + f * (pop[idx[1]][i] - pop[idx[2]][i]);
                    }
                    else{
                        trial[i] = pop[p][i];
                    }
                }
                
                //Fix constraint
                for (int i = 0; i < trial.length; i++) {
                    trial[i] = trial[i] < boundConstraint.get(0).getMin() ? boundConstraint.get(0).getMin() : trial[i];
                    trial[i] = trial[i] > boundConstraint.get(0).getMax() ? boundConstraint.get(0).getMax() : trial[i];
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