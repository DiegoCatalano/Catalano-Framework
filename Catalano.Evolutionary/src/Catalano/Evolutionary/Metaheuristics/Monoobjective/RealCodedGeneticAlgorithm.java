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

package Catalano.Evolutionary.Metaheuristics.Monoobjective;

import Catalano.Core.ArraysUtil;
import Catalano.Core.DoubleRange;
import Catalano.Math.Matrix;
import Catalano.Math.Tools;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Real Coded Genetic Algorithm.
 * @author Diego Catalano
 */
public class RealCodedGeneticAlgorithm extends BaseEvolutionaryOptimization {
    
    public static enum Selection {Random, RoulleteWheelSelection, Elite};
    private Selection selection;
    
    private float crossoverPercentage;
    private float mutationPercentage;
    private double mutationRate = 0.1;
    private double beta = 8;
    
    private double maxError; //Used only in Roulette Wheel Selection

    /**
     * Initializes a new instance of the RealCodedGeneticAlgorithm class.
     */
    public RealCodedGeneticAlgorithm() {
        this(100,1000);
    }
    
    /**
     * Initializes a new instance of the RealCodedGeneticAlgorithm class.
     * @param population Initial population.
     * @param generations Generations.
     */
    public RealCodedGeneticAlgorithm(int population, int generations){
        this(population,generations,0.7f,0.3f);
    }

    /**
     * Initializes a new instance of the RealCodedGeneticAlgorithm class.
     * @param population Initial population.
     * @param generations Generations.
     * @param crossoverPercentage Crossover percentage.
     * @param mutationPercentage Mutation percentage.
     */
    public RealCodedGeneticAlgorithm(int population, int generations, float crossoverPercentage, float mutationPercentage) {
        this(population, generations, crossoverPercentage, mutationPercentage, Selection.Random);
    }
    
/**
     * Initializes a new instance of the RealCodedGeneticAlgorithm class.
     * @param population Initial population.
     * @param generations Generations.
     * @param crossoverPercentage Crossover percentage.
     * @param mutationPercentage Mutation percentage.
     * @param selection Selection method.
     */
    public RealCodedGeneticAlgorithm(int population, int generations, float crossoverPercentage, float mutationPercentage, Selection selection) {
        this.populationSize = population;
        this.generations = generations;
        this.crossoverPercentage = crossoverPercentage;
        this.mutationPercentage = mutationPercentage;
        this.selection = selection;
    }

    @Override
    public void Compute(IObjectiveFunction function, List<DoubleRange> boundConstraint) {
        
         Random rand = new Random();
         
         int popCO = 2*(int)(populationSize * crossoverPercentage)/2;
         int popMU = (int)(populationSize * mutationPercentage);
        
        //Generate the population
        int pSize = populationSize + popCO * 2 + popMU;
        List<Individual> population = new ArrayList<Individual>(pSize);
        for (int i = 0; i < pSize; i++) {
            double[] values = new double[boundConstraint.size()];
            for (int j = 0; j < values.length; j++) {
                DoubleRange range = boundConstraint.get(j);
                values[j] = range.getMin() + rand.nextDouble() * (range.getMax() - range.getMin());
            }

            Individual c = new Individual(values, function.Compute(values));
            population.add(c);
        }
        
        
        //Sort
        Collections.sort(population);
        
        //Best of the all solution
        best = Arrays.copyOf(population.get(0).getLocation(), boundConstraint.size());
        minError = population.get(0).getFitness();
        maxError = population.get(population.size()-1).getFitness();
        
        //For each generation
        for (int g = 0; g < generations; g++) {
            
            //Crossover
            List<Individual> news = new ArrayList<Individual>(popCO + popMU);
            for (int i = 0; i < popCO/2; i++) {
                
                //Selection
                int[] index = null;
                switch(selection){
                    case Random:
                        index = RandomSelection(population);
                    break;
                    case RoulleteWheelSelection:
                        index = RoulleteWheelSelection(population, beta, maxError);
                    break;
                    case Elite:
                        index = EliteSelection();
                    break;
                }

                Individual c1 = population.get(index[0]);
                Individual c2 = population.get(index[1]);
                
                double[][] elem = Crossover(c1.getLocation(), c2.getLocation(), 0.4, boundConstraint);

                c1 = new Individual(elem[0], function.Compute(elem[0]));
                c2 = new Individual(elem[1], function.Compute(elem[1]));
                news.add(c1);
                news.add(c2);
                
                nEvals += 2;
            }
            
            for (int i = 0; i < popMU; i++) {
                double[] elem = Mutation(population.get(rand.nextInt(population.size())).getLocation(), mutationRate, boundConstraint);
                news.add(new Individual(elem, function.Compute(elem)));
                nEvals++;
            }
            
            population.addAll(news);
            Collections.sort(population);
            
            best = Arrays.copyOf(population.get(0).getLocation(), boundConstraint.size());
            minError = population.get(0).getFitness();
            
            population = population.subList(0, populationSize);
            
            if(listener != null)
                listener.onIteration(g+1, minError);
            
        }
        
    }
    
    /**
     * Elite selection algorithm.
     * @param lst List of chromosomes.
     * @return Index of the selected chromosome.
     */
    private int[] EliteSelection(){
        
        return new int[] {0,1};
        
    }
    
    /**
     * Random selection algorithm.
     * @param lst List of chromosomes.
     * @return Index of the selected chromosome.
     */
    private int[] RandomSelection(List<Individual> lst){
        
        Random rand = new Random();
        
        int[] index = new int[2];
        index[0] = rand.nextInt(lst.size());
        index[1] = rand.nextInt(lst.size());
        
        return index;
        
    }
    
    /**
     * Roullete Wheel Selection.
     * @param lst List of the chromosome.
     * @param worstError Worst error.
     * @return Index of the selected chromosome.
     */
    private int[] RoulleteWheelSelection(List<Individual> lst, double beta, double worstError){
        
        Random rand = new Random();
        double[] fitness = new double[lst.size()];
        
        double sum = 0;
        for (int i = 0; i < fitness.length; i++) {
            double v = Math.exp(-beta * lst.get(i).getFitness() / worstError);
            fitness[i] = v;
            sum += v;
        }
        
        for (int i = 0; i < fitness.length; i++) {
            fitness[i] = fitness[i] / sum;
        }
        
        sum = fitness[0];
        for (int i = 1; i < fitness.length; i++) {
            sum += fitness[i];
            fitness[i] = sum;
        }
        
        int[] index = new int[2];
        for (int i = 0; i < index.length; i++) {
            double v = rand.nextDouble();
            for (int j = 0; j < fitness.length; j++) {
                if(fitness[j] < v){
                    index[i] = j;
                }
            }
        }
        return index;
    }
    
    private double[][] Crossover(double[] a, double[] b, double gamma, List<DoubleRange> boundConstraint){
        
        Random rand = new Random();
        
        double min = -gamma;
        double max = 1+gamma;
        
        double[] alpha = new double[a.length];
        for (int i = 0; i < alpha.length; i++) {
            alpha[i] = min + rand.nextDouble() * (max - min);
        }
        
        double[][] y = new double[2][a.length];
        for (int i = 0; i < y[0].length; i++) {
            double v1 = alpha[i]*a[i] + (1 - alpha[i]) * b[i];
            double v2 = alpha[i]*b[i] + (1 - alpha[i]) * a[i];
            
            v1 = Tools.Clamp(v1, boundConstraint.get(i));
            v2 = Tools.Clamp(v2, boundConstraint.get(i));
            y[0][i] = v1;
            y[1][i] = v2;
        }
        
        return y;
        
    }
    
    private double[] Mutation(double[] a, double mu, List<DoubleRange> boundConstraint){
        
        Random rand = new Random();
        
        int[] j = Matrix.Indices(0, a.length);
        ArraysUtil.Shuffle(j);
        
        int n = (int)Math.ceil(mu*a.length);
        j = Arrays.copyOf(j, n);
        
        double[] sigma = new double[a.length];
        for (int i = 0; i < sigma.length; i++) {
            DoubleRange range = boundConstraint.get(i);
            sigma[i] = 0.1 * (range.getMax() - range.getMin());
        }
        
        double[] y = Arrays.copyOf(a, a.length);
        for (int i = 0; i < j.length; i++) {
            DoubleRange range = boundConstraint.get(j[i]);
            double v = a[j[i]] + sigma[j[i]] * rand.nextGaussian();
            v = Tools.Clamp(v, range);
            y[j[i]] = v;
        }
        
        return y;
        
    }   
}