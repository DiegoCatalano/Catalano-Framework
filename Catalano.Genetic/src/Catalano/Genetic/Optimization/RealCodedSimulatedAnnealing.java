// Catalano Genetic Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2018
// diego.catalano at live.com
//
// Copyright (c) 2015, Yarpiz (www.yarpiz.com)
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are
// met:
//
//     * Redistributions of source code must retain the above copyright
//       notice, this list of conditions and the following disclaimer.
//	  
//     * Redistributions in binary form must reproduce the above copyright
//       notice, this list of conditions and the following disclaimer in
//       the documentation and/or other materials provided with the distribution
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE.
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
import Catalano.Math.Tools;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Simulated Annealing for Continuous space domain.
 * @author Diego Catalano
 */
public class RealCodedSimulatedAnnealing extends AbstractEvolutionaryOptimization implements IOptimization{
    
    private int maxSub;
    private double t0;
    private double alpha;
    private int move;
    private double mu;
    private double sigma;
    
    private long nEval;
    private double minError;
    private double[] best;

    /**
     * Get maximum sub iterations.
     * @return Maximum sub iterations.
     */
    public int getMaxSubIterations() {
        return maxSub;
    }

    /**
     * Set maximum sub iterations.
     * @param maxSub Maximum sub iterations.
     */
    public void setMaxSubIterations(int maxSub) {
        this.maxSub = maxSub;
    }

    /**
     * Get initial temperature.
     * @return Initial temperature.
     */
    public double getInitialTemperature() {
        return t0;
    }

    /**
     * Set initial temperature.
     * @param t0 Initial temperature.
     */
    public void setInitialTemperature(double t0) {
        this.t0 = t0;
    }

    /**
     * Get temperature reduction.
     * @return Temperature reduction.
     */
    public double getTemperatureReduction() {
        return alpha;
    }

    /**
     * Set temperature reduction.
     * @param alpha Temperature reduction.
     */
    public void setTemperatureReduction(double alpha) {
        this.alpha = alpha;
    }

    /**
     * Get number of neighbors.
     * @return Number of neighbors.
     */
    public int getNumberOfNeighbors() {
        return move;
    }

    /**
     * Set number of neighbors.
     * @param move Number of neighbors.
     */
    public void setNumberOfNeighbors(int move) {
        this.move = move;
    }

    /**
     * Get mutation rate.
     * @return Mutation rate.
     */
    public double getMutationRate() {
        return mu;
    }

    /**
     * Set mutation rate.
     * @param mu Mutation rate.
     */
    public void setMutationRate(double mu) {
        this.mu = mu;
    }

    /**
     * Get mutation range.
     * @return Mutation range.
     */
    public double getMutationRange() {
        return sigma;
    }

    /**
     * Set mutation range.
     * @param sigma Mutation range.
     */
    public void setMutationRange(double sigma) {
        this.sigma = sigma;
    }

    /**
     * Initialize a new instance of the RealCodedSimulatedAnnealing class.
     */
    public RealCodedSimulatedAnnealing() {
        this(10,1000);
    }
    
    /**
     * Initialize a new instance of the RealCodedSimulatedAnnealing class.
     * @param population Population
     * @param iterations Iterations.
     */
    public RealCodedSimulatedAnnealing(int population, int iterations){
        this(population, iterations, 20);
    }
    
    /**
     * Initialize a new instance of the RealCodedSimulatedAnnealing class.
     * @param population Population.
     * @param iterations Iterations.
     * @param maxSub Maximum sub iterations.
     */
    public RealCodedSimulatedAnnealing(int population, int iterations, int maxSub){
        this(population, iterations, maxSub, 0.1);
    }
    
    /**
     * Initialize a new instance of the RealCodedSimulatedAnnealing class.
     * @param population Population.
     * @param iterations Iterations.
     * @param maxSub Maximum sub iterations.
     * @param t0 Initial temperature.
     */
    public RealCodedSimulatedAnnealing(int population, int iterations, int maxSub, double t0){
        this(population, iterations, maxSub, t0, 0.99);
    }
    
    /**
     * Initialize a new instance of the RealCodedSimulatedAnnealing class.
     * @param population Population.
     * @param iterations Iterations.
     * @param maxSub Maximum sub iterations.
     * @param t0 Initial temperature.
     * @param alpha Temperature reduction rate.
     */
    public RealCodedSimulatedAnnealing(int population, int iterations, int maxSub, double t0, double alpha){
        this(population, iterations, maxSub, t0, alpha, 5);
    }
    
    /**
     * Initialize a new instance of the RealCodedSimulatedAnnealing class.
     * @param population Population.
     * @param iterations Iterations.
     * @param maxSub Maximum sub iterations.
     * @param t0 Initial temperature.
     * @param alpha Temperature reduction rate.
     * @param move Number of neighbors per individual.
     */
    public RealCodedSimulatedAnnealing(int population, int iterations, int maxSub, double t0, double alpha, int move){
        this(population, iterations, maxSub, t0, alpha, move, 0.5);
    }
    
    /**
     * Initialize a new instance of the RealCodedSimulatedAnnealing class.
     * @param population Population.
     * @param iterations Iterations.
     * @param maxSub Maximum sub iterations.
     * @param t0 Initial temperature.
     * @param alpha Temperature reduction rate.
     * @param move Number of neighbors per individual.
     * @param mu Mutation rate.
     */
    public RealCodedSimulatedAnnealing(int population, int iterations, int maxSub, double t0, double alpha, int move, double mu){
        this(population, iterations, maxSub, t0, alpha, move, mu, 0.1);
    }

    /**
     * Initialize a new instance of the RealCodedSimulatedAnnealing class.
     * @param population Population.
     * @param iterations Iterations.
     * @param maxSub Maximum sub iterations.
     * @param t0 Initial temperature.
     * @param alpha Temperature reduction rate.
     * @param move Number of neighbors per individual.
     * @param mu Mutation rate.
     * @param sigma Mutation range.
     */
    public RealCodedSimulatedAnnealing(int population, int iterations, int maxSub, double t0, double alpha, int move, double mu, double sigma) {
        this.populationSize = population;
        this.generations = iterations;
        this.maxSub = maxSub;
        this.t0 = t0;
        this.alpha = alpha;
        this.move = move;
        this.mu = mu;
        this.sigma = sigma;
    }

    @Override
    public long getNumberOfEvaluations() {
        return nEval;
    }

    @Override
    public double[] Compute(ISingleObjectiveFunction function, List<DoubleRange> boundConstraint) {
        
        minError = Double.MAX_VALUE;
        nEval = 0;
        
        Random rand = new Random();
        
        double[][] pop = new double[populationSize][boundConstraint.size()];
        double[] fitness = new double[populationSize];
        
        //Initialize the solutions
        for (int i = 0; i < pop.length; i++) {
            for (int j = 0; j < pop[0].length; j++) {
                DoubleRange range = boundConstraint.get(j);
                pop[i][j] = range.getMin() + rand.nextDouble() * (range.getMax() - range.getMin());
            }
        }
        
        //Compute fitness
        for (int i = 0; i < populationSize; i++) {
            double f = function.Compute(pop[i]);
            fitness[i] = f;
            if(f < minError){
                minError = f;
                best = Arrays.copyOf(pop[i], pop[0].length);
            }
            nEval++;
        }
        
        //Initialize temp
        double t = t0;
        for (int i = 0; i < generations; i++) {
            
            double[][] newPop = new double[pop.length * move][pop[0].length];
            double[] newFitness = new double[newPop.length];
            
            //Compute mutation range by sigma
            double[] sigmaRange = new double[boundConstraint.size()];
            for (int m = 0; m < sigmaRange.length; m++) {
                DoubleRange range = boundConstraint.get(m);
                sigmaRange[m] = sigma * (range.getMax() - range.getMin());
            }
            
            for (int j = 0; j < maxSub; j++) {
                int index = 0;
                for (int k = 0; k < pop.length; k++) {
                    for (int l = 0; l < move; l++) {
                        
                        //Mutate
                        newPop[index] = Mutate(pop[k], mu, sigmaRange, boundConstraint, rand);
                        newFitness[index] = function.Compute(newPop[index]);
                        index++;
                        nEval++;
                    }
                }
                
                //Sort
                int[] order = ArraysUtil.Argsort(newFitness, true);

                for (int p = 0; p < pop.length; p++) {
                    if(newFitness[order[p]] <= fitness[p]){
                        pop[p] = Arrays.copyOf(newPop[order[p]], newPop[0].length);
                        fitness[p] = newFitness[order[p]];
                    }
                    else{
                        double delta = (newFitness[order[p]] - fitness[p]) / fitness[p];
                        double pr = Math.exp(-delta/t);
                        if(rand.nextDouble() <= pr){
                            pop[p] = Arrays.copyOf(newPop[order[p]], newPop[0].length);
                            fitness[p] = newFitness[order[p]];
                        }
                    }

                    if(fitness[p] <= minError){
                        minError = fitness[p];
                        best = Arrays.copyOf(pop[p], pop[p].length);
                    }
                }
            }
            
            //Update Temp.
            t *= alpha;

            sigma *= 0.98;
        }
        
        return best;
        
    }
    
    private double[] Mutate(double[] solution, double mu, double[] sigmaRange, List<DoubleRange> boundConstraint, Random rand){
        
        double[] v = Matrix.UniformRandom(0, 1, solution.length);
        List<Integer> lst = new ArrayList<Integer>();
        for (int i = 0; i < v.length; i++) {
            if(v[i] <= mu) lst.add(i);
        }
        
        v = Arrays.copyOf(solution, solution.length);
        for (Integer i : lst) {
            v[i] = solution[i] + sigmaRange[i] * rand.nextGaussian();
            v[i] = Tools.Clamp(v[i], boundConstraint.get(i));
        }
        
        return v;
        
    }

    @Override
    public double getError() {
        return minError;
    }
    
}