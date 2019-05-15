// Catalano Genetic Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
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

package Catalano.Evolutionary.Metaheuristics;

import Catalano.Core.DoubleRange;
import Catalano.Math.Matrix;
import Catalano.Math.Tools;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Firefly Optimization (FA).
 * 
 * The firefly algorithm is a metaheuristic proposed by Xin-She Yang and inspired
 * by the flashing behaviour of fireflies.
 * 
 * @author Diego Catalano
 */
public class FireflyOptimization extends BaseEvolutionaryOptimization {
    
    private double alpha = 0.2;
    private double beta0 = 2;
    private double gamma = 1;
    private double alphaDamp = 0.98;
    private double delta = 0.05;
    private double dmax;
    
    /**
     * Get mutation coefficient.
     * @return Alpha value.
     */
    public double getMutationCoefficient(){
        return alpha;
    }
    
    /**
     * Set mutation coefficient.
     * @param alpha Alpha value.
     */
    public void setMutationCoefficient(double alpha){
        this.alpha = alpha;
    }
    
    /**
     * Get attraction coefficient
     * @return Beta0 value.
     */
    public double getAttractionCoefficient(){
        return beta0;
    }
    
    /**
     * Set Attraction Coefficient.
     * @param beta0 Beta0 value.
     */
    public void setAttractionCoefficient(double beta0){
        this.beta0 = beta0;
    }
    
    /**
     * Get light absorption coefficient.
     * @return Gamma value.
     */
    public double getLightAbsorptionCoefficient(){
        return gamma;
    }
    
    /**
     * Set light absorption coefficient.
     * @param gamma Gamma value.
     */
    public void setLightAbsorptionCoefficient(double gamma){
        this.gamma = gamma;
    }
    
    /**
     * Get damping ratio
     * @return Damping value.
     */
    public double getDampingRatio(){
        return alphaDamp;
    }
    
    /**
     * Set damping ratio.
     * @param damp Damping value.
     */
    public void setDampingRatio(double damp){
        this.alphaDamp = damp;
    }
    
    /**
     * Get uniform mutation ratio.
     * @return Delta value.
     */
    public double getUniformMutationRatio(){
        return delta;
    }
    
    /**
     * Set uniform mutation ratio.
     * @param delta Delta value.
     */
    public void setUniformMutationRatio(double delta){
        this.delta = delta;
    }

    /**
     * Initializes a new instance of the FireflyOptimization class.
     */
    public FireflyOptimization(){
        this(25, 1000);
    }
    
    /**
     * Initializes a new instance of the FireflyOptimization class.
     * @param population Number of population.
     * @param generations Number of generations.
     */
    public FireflyOptimization(int population, int generations){
        this(population, generations, 0.2, 2, 1);
    }
    
    /**
     * Initializes a new instance of the FireflyOptimization class.
     * @param population Number of population.
     * @param generations Number of generations.
     * @param alpha Mutation coefficient.
     * @param beta0 Attraction Coefficient Base Value.
     * @param gamma Light Absorption Coefficient.
     */
    public FireflyOptimization(int population, int generations, double alpha, double beta0, double gamma){
        this(population, generations, alpha, beta0, gamma, 0.98, 0.05);
    }
    
    /**
     * Initializes a new instance of the FireflyOptimization class.
     * @param population Number of population.
     * @param generations Number of generations.
     * @param alpha Mutation coefficient.
     * @param beta0 Attraction Coefficient Base Value.
     * @param gamma Light Absorption Coefficient.
     * @param alphaDamp Mutation Coefficient Damping Ratio.
     * @param delta Uniform Mutation Range Base Value.
     */
    public FireflyOptimization(int population, int generations, double alpha, double beta0, double gamma, double alphaDamp, double delta){
        this.populationSize = population;
        this.generations = generations;
        this.beta0 = beta0;
        this.gamma = gamma;
        this.alphaDamp = alphaDamp;
        this.delta = delta;
    }

    @Override
    public void Compute(ISingleObjectiveFunction function, List<DoubleRange> boundConstraint) {
        
        double damp = alpha;
        minError = Double.MAX_VALUE;
        nEvals = 0;
        
        Random rand = new Random();
        
        //Generate population
        List<Individual> pop = Individual.CreatePopulation(populationSize, boundConstraint, function);
        
        //Calculate dmax
        double[] min = new double[boundConstraint.size()];
        double[] max = new double[boundConstraint.size()];
        for (int i = 0; i < min.length; i++) {
            DoubleRange range = boundConstraint.get(i);
            min[i] = range.getMin();
            max[i] = range.getMax();
        }
        dmax = Matrix.Norm2(Matrix.Subtract(min, max));
        
        //Firefly algorithm
        for (int g = 0; g < generations; g++) {
            
            //Initialize a new population
            List<Individual> newPop = new ArrayList<Individual>(populationSize);
            for (int i = 0; i < populationSize; i++) {
                newPop.add(new Individual(null, Double.MAX_VALUE));
            }
            
            //Compute
            for (int i = 0; i < populationSize; i++) {
                for (int j = 0; j < populationSize; j++) {
                    double rij = Matrix.Norm2(Matrix.Subtract(pop.get(i).getLocation(), pop.get(j).getLocation())) / dmax;
                    double beta = beta0 * Math.exp(-gamma * (rij*rij));
                    double[] e = Matrix.Multiply(Matrix.UniformRandom(-1, 1, boundConstraint.size()), delta);

                    //New solution
                    double[] newsol = new double[boundConstraint.size()];
                    for (int k = 0; k < newsol.length; k++) {
                        Individual a = pop.get(i);
                        Individual b = pop.get(j);
                        newsol[k] = a.getLocation()[k] + beta * rand.nextDouble() * (b.getLocation()[k] - a.getLocation()[k]) + damp * e[k];
                        newsol[k] = Tools.Clamp(newsol[k], boundConstraint.get(k));
                    }

                    double newfit = function.Compute(newsol);
                    nEvals++;
                    if(newfit <= newPop.get(i).getFitness()){
                        newPop.set(i, new Individual(newsol, newfit));
                        if(newfit < minError){
                            minError = newfit;
                            best = Arrays.copyOf(newPop.get(i).getLocation(), boundConstraint.size());
                        }
                    }
                }
            }
            
            //Merge population
            pop.addAll(newPop);
            
            //Sort
            Collections.sort(pop);
            
            //Truncate
            pop = pop.subList(0, populationSize);
            
            damp = damp*alphaDamp;
        }
        
    }
}