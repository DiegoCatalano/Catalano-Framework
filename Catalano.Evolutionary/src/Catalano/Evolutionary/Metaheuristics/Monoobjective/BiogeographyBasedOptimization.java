// Catalano Genetic Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2019
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

package Catalano.Evolutionary.Metaheuristics.Monoobjective;

import Catalano.Core.DoubleRange;
import Catalano.Math.Matrix;
import Catalano.Statistics.Tools;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Biogeography Based Optimization (BBO).
 * @author Diego Catalano
 */
public class BiogeographyBasedOptimization extends BaseEvolutionaryOptimization{
    
    private float keepRate;
    private double alpha = 0.9;
    private float pMutation = 0.1f;
    private double sigma = 0.02;

    /**
     * Get keep rate.
     * @return Keep rate.
     */
    public float getKeepRate() {
        return keepRate;
    }

    /**
     * Set keep rate.
     * @param keepRate Keep rate.
     */
    public void setKeepRate(float keepRate) {
        this.keepRate = Math.max(0, Math.min(1, keepRate));
    }

    /**
     * Get probability mutation.
     * @return Probability mutation.
     */
    public float getProbabilityMutation() {
        return pMutation;
    }

    /**
     * Set probability mutation.
     * @param pMutation Probability mutation.
     */
    public void setProbabilityMutation(float pMutation) {
        this.pMutation = pMutation;
    }

    /**
     * Initializes a new instance of the BiogeographyBasedOptimization class.
     */
    public BiogeographyBasedOptimization() {
        this(50,1000,0.2f);
    }
    
    /**
     * Initializes a new instance of the BiogeographyBasedOptimization class.
     * @param populationSize Population size.
     * @param generations Generations.
     */
    public BiogeographyBasedOptimization(int populationSize, int generations){
        this(populationSize, generations, 0.2f);
    }

    /**
     * Initializes a new instance of the BiogeographyBasedOptimization class.
     * @param populationSize Size of the population.
     * @param generations Generations.
     * @param keepRate Keep some individuals. [0..1]
     */
    public BiogeographyBasedOptimization(int populationSize, int generations, float keepRate) {
        this.populationSize = populationSize;
        this.generations = generations;
        this.keepRate = keepRate;
    }
    
    /**
     * Initializes a new instance of the BiogeographyBasedOptimization class.
     * @param populationSize Size of the population.
     * @param generations Generations.
     * @param keepRate Keep some individuals. [0..1]
     * @param pMutation Probability of mutation [0..1]
     */
    public BiogeographyBasedOptimization(int populationSize, int generations, float keepRate, float pMutation) {
        this.populationSize = populationSize;
        this.generations = generations;
        this.keepRate = keepRate;
        this.pMutation = pMutation;
    }

    @Override
    public void Compute(IObjectiveFunction function, List<DoubleRange> bounds) {
        
        Random rand = new Random();
        
        nEvals = 0;
        minError = Double.MAX_VALUE;
        
        //Initialize the population
        List<Individual> population = Individual.CreatePopulation(populationSize, bounds, function);
        nEvals += populationSize;
        
        //Sort the population
        Collections.sort(population);
        
        best = Arrays.copyOf(population.get(0).getLocation(), bounds.size());
        minError = population.get(0).getFitness();
        
        int nKeep = (int)(populationSize * keepRate);
        int nNews = populationSize - nKeep;
        
        //Migration rate
        double[] mu = Matrix.Linspace(1,0,populationSize);
        
        //Calculate sigma
        double[] sigmas = new double[bounds.size()];
        for (int i = 0; i < sigmas.length; i++) {
            DoubleRange range = bounds.get(i);
            sigmas[i] = sigma * (range.getMax() - range.getMin());
        }
        
        //Main algorithm
        for (int g = 0; g < generations; g++) {
            
            List<Individual> newpop = new ArrayList<>(populationSize);
            for (int i = 0; i < populationSize; i++) {
                newpop.add(population.get(i).getClone());
            }
            
            for (int i = 0; i < populationSize; i++) {
                
                double[] newIndividual = newpop.get(i).getLocation();
                for (int j = 0; j < bounds.size(); j++) {
                    
                    //Migration
                    if(rand.nextDouble() <= (1 - mu[i])){
                        
                        //Emmigration Probabilities
                        double[] ep = Arrays.copyOf(mu, mu.length);
                        ep[i] = 0;
                        double sum = Tools.Sum(ep);
                        for (int k = 0; k < ep.length; k++) {
                            ep[k] /= sum;
                        }
                        
                        //Roulette Whell Selection
                        int index = RouletteWheelSelection(ep, rand);
                        
                        //Migration
                        newIndividual[j] = population.get(i).getLocation(j) + alpha * (population.get(index).getLocation(j) - population.get(i).getLocation(j));
                        
                    }
                    
                    //Mutation
                    if(rand.nextFloat()<= pMutation){
                        newIndividual[j] = newIndividual[j] + sigmas[j]*rand.nextGaussian();
                    }
                    
                }
                
                Catalano.Math.Tools.Clamp(newIndividual, bounds);
                double f = function.Compute(newIndividual);
                newpop.get(i).setFitness(f);
                nEvals++;
                
            }
            
            //Sort the new population
            Collections.sort(newpop);

            //Get the best of the two population
            population = population.subList(0, nKeep);
            population.addAll(newpop.subList(0, nNews));

            //Update best and minError
            Collections.sort(population);
            best = Arrays.copyOf(population.get(0).getLocation(), bounds.size());
            minError = population.get(0).getFitness();
            
            //Update listener
            listener.onIteration(g+1, minError);   
        }
        
    }
    
    private int RouletteWheelSelection(double[] values, Random rand){
        
        double r = rand.nextDouble();
        double cumsum = 0;
        for (int i = 0; i < values.length; i++) {
            cumsum += values[i];
            if(cumsum >= r)
                return i;
        }
        
        return values.length - 1;
    }
    
}