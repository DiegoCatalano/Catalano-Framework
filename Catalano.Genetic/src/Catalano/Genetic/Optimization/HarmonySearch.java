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

import Catalano.Core.DoubleRange;
import Catalano.Math.Matrix;
import Catalano.Math.Tools;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Harmony Search.
 * @author Diego Catalano
 */
public class HarmonySearch implements IOptimization{
    
    //Parameters
    private int population;
    private int generations;
    private int newHarmonies;
    private double hmcr;
    private double pitch;
    private double fw;
    private double dampFactor;
    
    private double[] best;
    private double minError;
    private int eval;

    /**
     * Get number of population.
     * @return Number of population.
     */
    public int getPopulation() {
        return population;
    }

    /**
     * Set number of population.
     * @param population Number of population.
     */
    public void setPopulation(int population) {
        this.population = population;
    }

    /**
     * Get number of generations.
     * @return Number of generations.
     */
    public int getGenerations() {
        return generations;
    }

    /**
     * Set number of generations.
     * @param generations Number of generations.
     */
    public void setGenerations(int generations) {
        this.generations = generations;
    }

    /**
     * Get number of new harmonies.
     * @return Number of new harmonies.
     */
    public int getNewHarmonies() {
        return newHarmonies;
    }

    /**
     * Set number of new harmonies.
     * @param newHarmonies Number of new harmonies.
     */
    public void setNewHarmonies(int newHarmonies) {
        this.newHarmonies = newHarmonies;
    }

    /**
     * Get the Harmony memory consideration rate.
     * @return Harmony memory consideration rate.
     */
    public double getHarmonyMemoryConsiderationRate() {
        return hmcr;
    }

    /**
     * Set the Harmony memory consideration rate.
     * @param hmcr Harmony memory consideration rate.
     */
    public void setHarmonyMemoryConsiderationRate(double hmcr) {
        this.hmcr = hmcr;
    }

    /**
     * Get Pitch adjustment.
     * @return Pitch value.
     */
    public double getPitch() {
        return pitch;
    }

    /**
     * Set Pitch adjustment.
     * @param pitch Pitch value.
     */
    public void setPitch(double pitch) {
        this.pitch = pitch;
    }

    /**
     * Get Fret Width (Bandwidth);
     * @return Fret Width value.
     */
    public double getFretWidth() {
        return fw;
    }

    /**
     * Set Fret Width (Bandwidth);
     * @param fw Fret Width value.
     */
    public void setFretWidth(double fw) {
        this.fw = fw;
    }

    /**
     * Get Damping factor.
     * @return Damping factor.
     */
    public double getDampFactor() {
        return dampFactor;
    }

    /**
     * Set Damping factor.
     * @param dampFactor Damping factor.
     */
    public void setDampFactor(double dampFactor) {
        this.dampFactor = dampFactor;
    }

    /**
     * Initializes a new intance of the HarmonySearch class.
     */
    public HarmonySearch() {
        this(25,1000);
    }
    
    /**
     * Initializes a new intance of the HarmonySearch class.
     * @param population Population.
     * @param generations Generations.
     */
    public HarmonySearch(int population, int generations){
        this(population, generations, 20);
    }
    
    /**
     * Initializes a new intance of the HarmonySearch class.
     * @param population Population.
     * @param generations Generations.
     * @param newHarmonies New harmonies.
     */
    public HarmonySearch(int population, int generations, int newHarmonies){
        this(population, generations, newHarmonies, 0.9);
    }
    
    /**
     * Initializes a new intance of the HarmonySearch class.
     * @param population Population.
     * @param generations Generations.
     * @param newHarmonies New harmonies.
     * @param hmcr Harmony Memory Consideration Rate.
     */
    public HarmonySearch(int population, int generations, int newHarmonies, double hmcr){
        this(population, generations, newHarmonies, hmcr, 0.1);
    }
    
    /**
     * Initializes a new intance of the HarmonySearch class.
     * @param population Population.
     * @param generations Generations.
     * @param newHarmonies New harmonies.
     * @param hmcr Harmony Memory Consideration Rate.
     * @param pitch Pitch Adjustment Rate.
     */
    public HarmonySearch(int population, int generations, int newHarmonies, double hmcr, double pitch){
        this(population, generations, newHarmonies, hmcr, pitch, 0.02);
    }
    
    /**
     * Initializes a new intance of the HarmonySearch class.
     * @param population Population.
     * @param generations Generations.
     * @param newHarmonies New harmonies.
     * @param hmcr Harmony Memory Consideration Rate.
     * @param pitch Pitch Adjustment Rate.
     * @param fw Fret Width.
     */
    public HarmonySearch(int population, int generations, int newHarmonies, double hmcr, double pitch, double fw){
        this(population, generations, newHarmonies, hmcr, pitch, fw, 0.995);
    }

    /**
     * Initializes a new intance of the HarmonySearch class.
     * @param population Population.
     * @param generations Generations.
     * @param newHarmonies New harmonies.
     * @param hmcr Harmony Memory Consideration Rate.
     * @param pitch Pitch Adjustment Rate.
     * @param fw Fret Width.
     * @param dampFactor Damp factor ratio;
     */
    public HarmonySearch(int population, int generations, int newHarmonies, double hmcr, double pitch, double fw, double dampFactor) {
        this.population = population;
        this.generations = generations;
        this.newHarmonies = newHarmonies;
        this.hmcr = hmcr;
        this.pitch = pitch;
        this.fw = fw;
        this.dampFactor = dampFactor;
    }
    

    @Override
    public int getNumberOfEvaluations() {
        return eval;
    }

    @Override
    public double[] Compute(ISingleObjectiveFunction function, List<DoubleRange> boundConstraint) {
        
        //Reset parameters
        eval = 0;
        
        Random rand = new Random();
        
        //Initialize the population
        List<Harmony> pop = new ArrayList<Harmony>(population);
        for (int i = 0; i < population; i++) {
            double[] values = Matrix.UniformRandom(boundConstraint);
            double fitness = function.Compute(values);
            
            pop.add(new Harmony(values, fitness));
            eval++;
        }
        
        //Sort population by your fitness
        Collections.sort(pop);
        
        //Calculate fret width
        double[] fretWidth = new double[boundConstraint.size()];
        for (int i = 0; i < fretWidth.length; i++) {
            DoubleRange range = boundConstraint.get(i);
            fretWidth[i] = fw * (range.getMax() - range.getMin());
        }
        
        //Initialize the algorithm
        for (int g = 0; g < generations; g++) {
            
            //Initialize new pop
            List<Harmony> newPop = new ArrayList<Harmony>(newHarmonies);
            for (int i = 0; i < newHarmonies; i++) {
                double[] values = Matrix.UniformRandom(boundConstraint);
                newPop.add(new Harmony(values));
            }
            
            for (int i = 0; i < newHarmonies; i++) {
                for (int j = 0; j < boundConstraint.size(); j++) {
                    
                    //Harmony memory
                    if(rand.nextDouble() <= hmcr){
                        int randPos = rand.nextInt(pop.size());
                        newPop.get(i).location[j] = pop.get(randPos).location[j];
                    }
                    
                    //Pitch adjustment
                    double v = newPop.get(i).location[j];
                    if(rand.nextDouble() <= pitch){
                        v += rand.nextGaussian() * fretWidth[j];
                    }
                    
                    //Clamp value
                    newPop.get(i).location[j] = Tools.Clamp(v, boundConstraint.get(j));
                }
                
                //Compute fitness
                newPop.get(i).fitness = function.Compute(newPop.get(i).location);
                eval++;
            }
            
            //Merge harmonies
            pop.addAll(newPop);
            
            //Sort
            Collections.sort(pop);
            
            //Truncate
            pop = pop.subList(0, population);
            
            //Damp fret width
            for (int i = 0; i < fretWidth.length; i++) {
                fretWidth[i] *= dampFactor;
            }
            
            //Update global values
            best = Arrays.copyOf(pop.get(0).location, boundConstraint.size());
            minError = pop.get(0).fitness;
            
        }
        
        
        return best;
        
    }

    @Override
    public double getError() {
        return minError;
    }
    
    class Harmony implements Comparable<Harmony>{
        
        public double[] location;
        public double fitness;
        
        public Harmony(double[] location){
            this(location, Double.MAX_VALUE);
        }

        public Harmony(double[] location, double fitness) {
            this.location = location;
            this.fitness = fitness;
        }

        @Override
        public int compareTo(Harmony o) {
            return Double.compare(fitness, o.fitness);
        }
        
    }
    
}
