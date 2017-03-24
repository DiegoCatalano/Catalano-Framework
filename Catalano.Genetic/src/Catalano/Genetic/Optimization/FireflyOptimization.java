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

package Catalano.Genetic.Optimization;

import Catalano.Core.DoubleRange;
import Catalano.Math.Matrix;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * Firefly Optimization
 * @author Diego Catalano
 */
public class FireflyOptimization implements IOptimization{
    
    private int population;
    private int generations;
    
    private double alpha = 0.2;
    private double beta0 = 2;
    private double gamma = 1;
    private double alphaDamp = 0.98;
    private double delta = 0.05;
    private double dmax;
    
    private double[] best;
    private double minError;
    
    @Override
    public double getError() {
        return minError;
    }

    public FireflyOptimization(){
        this(25, 1000);
    }
    
    public FireflyOptimization(int population, int generations){
        this.population = population;
        this.generations = generations;
    }

    @Override
    public double[] Compute(IObjectiveFunction function, List<DoubleRange> boundConstraint) {
        
        minError = Double.MAX_VALUE;
        
        Random rand = new Random();
        
        //Create initial population
        List<Firefly> pop = new ArrayList<Firefly>(population);
        
        for (int i = 0; i < population; i++) {
            
            double[] loc = Matrix.UniformRandom(boundConstraint);
            double fit = function.Compute(loc);
            pop.add(new Firefly(loc, fit));
            
            if(fit < minError){
                minError = fit;
                best = Arrays.copyOf(pop.get(i).location, boundConstraint.size());
            }
        }
        
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
            List<Firefly> newPop = new ArrayList<Firefly>(population);
            for (int i = 0; i < population; i++) {
                newPop.add(new Firefly(null, Double.MAX_VALUE));
            }
            
            //Compute
            for (int i = 0; i < population; i++) {
                for (int j = 0; j < population; j++) {
                    double rij = Matrix.Norm2(Matrix.Subtract(pop.get(i).location, pop.get(j).location)) / dmax;
                    double beta = beta0 * Math.exp(-gamma * (rij*rij));
                    double[] e = Matrix.Multiply(Matrix.UniformRandom(-1, 1, boundConstraint.size()), delta);

                    //New solution
                    double[] newsol = new double[boundConstraint.size()];
                    for (int k = 0; k < newsol.length; k++) {
                        Firefly a = pop.get(i);
                        Firefly b = pop.get(j);
                        newsol[k] = a.location[k] + beta * rand.nextDouble() * (b.location[k] - a.location[k]) + alpha * e[k];
                    }

                    double newfit = function.Compute(newsol);
                    if(newfit <= newPop.get(i).fitness){
                        newPop.set(i, new Firefly(newsol, newfit));
                        if(newfit < minError){
                            minError = newfit;
                            best = Arrays.copyOf(newPop.get(i).location, boundConstraint.size());
                        }
                    }
                }
            }
            
            //Merge population
            pop.addAll(newPop);
            
            //Sort
            pop.sort(new Comparator<Firefly>() {

                @Override
                public int compare(Firefly o1, Firefly o2) {
                    return Double.compare(o1.fitness, o2.fitness);
                }
            });
            
            //Truncate
            pop = pop.subList(0, population);
        }
        alpha = alpha*alphaDamp;
        
        return best;
        
    }
    
    class Firefly{
        
        public double[] location;
        public double fitness;
        
        public Firefly(double[] location){
            this(location, Double.MAX_VALUE);
        }

        public Firefly(double[] location, double fitness) {
            this.location = location;
            this.fitness = fitness;
        }
        
    }
    
}
