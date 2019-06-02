// Catalano Genetic Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2019
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

import Catalano.Core.ArraysUtil;
import Catalano.Core.DoubleRange;
import Catalano.Math.Matrix;
import Catalano.Math.Tools;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Butterfly Optimization Algorithm (BOA).
 * 
 * 
 * 
 * @author Diego Catalano
 */
public class ButterflyOptimizationAlgorithm extends BaseEvolutionaryOptimization{
    
    private float pSwith;
    private double pExpoent;
    private double sensoryModality;

    /**
     * Get probability switch.
     * @return Probability switch.
     */
    public float getProbabilitySwith() {
        return pSwith;
    }

    /**
     * Set probability switch.
     * @param pSwith Probability switch.
     */
    public void setProbabilitySwith(float pSwith) {
        this.pSwith = pSwith;
    }

    /**
     * Get power expoent.
     * @return Power expoent.
     */
    public double getPowerExpoent() {
        return pExpoent;
    }

    /**
     * Set power expoent.
     * @param pExpoent Power expoent.
     */
    public void setPowerExpoent(double pExpoent) {
        this.pExpoent = pExpoent;
    }

    /**
     * Get sensory modality.
     * @return Sensory modality.
     */
    public double getSensoryModality() {
        return sensoryModality;
    }

    /**
     * Set sensory modality.
     * @param sensoryModality Sensory modality.
     */
    public void setSensoryModality(double sensoryModality) {
        this.sensoryModality = sensoryModality;
    }

    /**
     * Initializes a new instance of the ButterflyOptimizationAlgorithm class.
     */
    public ButterflyOptimizationAlgorithm() {}
    
    /**
     * Initializes a new instance of the ButterflyOptimizationAlgorithm class.
     * @param populationSize Population size.
     * @param generations Generations.
     */
    public ButterflyOptimizationAlgorithm(int populationSize, int generations){
        this(populationSize, generations, 0.8f, 0.1, 0.01);
    }
    
    /**
     * Initializes a new instance of the ButterflyOptimizationAlgorithm class.
     * @param populationSize Population size.
     * @param generations Generations.
     * @param probabilitySwitch Probability switch.
     * @param powerExpoent Power expoent.
     * @param sensoryModality Sensory modality.
     */
    public ButterflyOptimizationAlgorithm(int populationSize, int generations, float probabilitySwitch, double powerExpoent, double sensoryModality){
        this.populationSize = populationSize;
        this.generations = generations;
        this.pSwith = probabilitySwitch;
        this.pExpoent = powerExpoent;
        this.sensoryModality = sensoryModality;
    }

    @Override
    public void Compute(IObjectiveFunction function, List<DoubleRange> bounds) {
        
        Random rand = new Random();
        
        minError = Double.MAX_VALUE;
        nEvals = 0;
        
        //Initialize the population
        double[][] pop = new double[populationSize][bounds.size()];
        double[] fitness = new double[populationSize];
        for (int i = 0; i < pop.length; i++){
            pop[i] = Matrix.UniformRandom(bounds);
            fitness[i] = function.Compute(pop[i]);
            if(fitness[i] < minError){
                minError = fitness[i];
                best = Arrays.copyOf(pop[i], pop[0].length);
            }
        }
        
        int[] index = Matrix.Indices(0,populationSize);
        for (int g = 0; g < generations; g++) {
            
            for (int i = 0; i < pop.length; i++) {
                
                //Calculate fragrance of each butterfly
                double fp = sensoryModality * Math.pow(fitness[i], pExpoent);
                
                double[] s = new double[pop[0].length]; 
                if(rand.nextFloat() <= pSwith){
                    
                    double r = rand.nextDouble();
                    for (int j = 0; j < pop[0].length; j++) {
                        s[j] = pop[i][j] + (r*r*best[j] - pop[i][j]) * fp;
                    }
                    
                }
                else{
                    double r = rand.nextDouble();
                    ArraysUtil.Shuffle(index);
                    
                    for (int j = 0; j < pop[0].length; j++) {
                        s[j] = pop[i][j] + (r*r*pop[index[0]][j] - pop[index[1]][j]) * fp;
                    }
                }
                
                //Update the population if the solution is better
                Tools.Clamp(s, bounds);
                double f = function.Compute(s);
                if(f < fitness[i]){
                    pop[i] = s;
                    fitness[i] = f;
                }
                
                //Update global error
                if(f < minError){
                    minError = f;
                    best = Arrays.copyOf(pop[i], pop[0].length);
                }
                                
            }
            
            //Update listener
            listener.onIteration(g+1, minError);
            
            sensoryModality = NewSensoryModality(sensoryModality, (g+1));
            
        }
        
        
    }
    
    private double NewSensoryModality(double x, int gen){
        return x + (0.025 / (x*gen));
    }
    
}