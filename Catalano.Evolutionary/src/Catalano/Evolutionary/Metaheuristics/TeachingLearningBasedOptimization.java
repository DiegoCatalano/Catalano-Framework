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
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Teaching-Learning-Based Optimization (TLBO).
 * @author Diego Catalano
 */
public class TeachingLearningBasedOptimization extends AbstractEvolutionaryOptimization implements IOptimization{
    
    private double minError;
    private double[] best;
    
    @Override
    public double getError() {
        return minError;
    }

    /**
     * Initializes a new instance of the TeachingLearningBasedOptimization class.
     */
    public TeachingLearningBasedOptimization() {
        this(25,1000);
    }

    /**
     * Initializes a new instance of the TeachingLearningBasedOptimization class.
     * @param population Initial population.
     * @param iterations Iterations.
     */
    public TeachingLearningBasedOptimization(int population, int iterations) {
        this.populationSize = population;
        this.generations = iterations;
    }

    @Override
    public double[] Compute(ISingleObjectiveFunction function, List<DoubleRange> boundConstraint) {
        
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
        for (int i = 0; i < fitness.length; i++)
            fitness[i] = function.Compute(pop[i]);
        
        //TLBO algorithm
        for (int it = 0; it < generations; it++) {
            
            //Calculate mean
            double[] mean = Catalano.Statistics.Tools.Mean(pop);
            
            //Select teacher
            double[] teacher = Arrays.copyOf(pop[Matrix.MinIndex(fitness)], pop[0].length);
            
            //Teacher phase
            for (int i = 0; i < populationSize; i++) {
                double tf = rand.nextInt(2)+1;
                
                double[] newsol = new double[pop[0].length];
                for (int j = 0; j < newsol.length; j++) {
                    newsol[j] = pop[i][j] + rand.nextDouble() * (teacher[j] - tf * mean[j]);
                    newsol[j] = Tools.Clamp(newsol[j], boundConstraint.get(j));
                }
                
                double f = function.Compute(newsol);
                nEvals++;
                
                if(f < fitness[i]){
                    pop[i] = newsol;
                    fitness[i] = f;
                    if(f < minError){
                        minError = f;
                        best = Arrays.copyOf(newsol, newsol.length);
                    }
                }
            }
            
            //Learner phase
            for (int i = 0; i < populationSize; i++) {
                
                int[] a = Matrix.Indices(0, populationSize);
                a = Matrix.RemoveColumn(a, i);
                int pos = a[rand.nextInt(populationSize - 1)];
                
                double[] step = Matrix.Subtract(pop[i], pop[pos]);
                if(fitness[pos] < fitness[i]){
                    step = Matrix.Multiply(step, -1);
                }
                
                double[] newsol = new double[pop[0].length];
                for (int j = 0; j < newsol.length; j++) {
                    newsol[j] = pop[i][j] + rand.nextDouble() * step[j];
                    newsol[j] = Tools.Clamp(newsol[j], boundConstraint.get(j));
                }
                
                double f = function.Compute(newsol);
                nEvals++;
                
                if(f < fitness[i]){
                    pop[i] = newsol;
                    fitness[i] = f;
                    if(f < minError){
                        minError = f;
                        best = Arrays.copyOf(pop[i], pop[i].length);
                    }
                }
            }
        }
        
        return best;
    }
}