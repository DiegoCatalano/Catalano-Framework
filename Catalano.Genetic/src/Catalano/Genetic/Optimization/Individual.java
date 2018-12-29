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
package Catalano.Genetic.Optimization;

import Catalano.Core.DoubleRange;
import Catalano.Math.Matrix;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents individual in the population.
 * @author Diego Catalano
 */
public class Individual implements Comparable<Individual>, Cloneable {
    
    private double[] location;
    private double fitness;
    
    public static List<Individual> CreatePopulation(int populationSize, List<DoubleRange> boundConstraints, ISingleObjectiveFunction function){
        
        List<Individual> population = new ArrayList<Individual>(populationSize);
        for (int i = 0; i < populationSize; i++) {
            double[] location = Matrix.UniformRandom(boundConstraints);
            double fitness = function.Compute(location);
            population.add(new Individual(location, fitness));
        }
        
        return population;
        
    }
    
    public static List<Individual> CreatePopulation(double[][] population, double[] fitness){
        
        List<Individual> pop = new ArrayList<Individual>(population.length);
        for (int i = 0; i < population.length; i++) {
            pop.add(new Individual(population[i], fitness[i]));
        }
        
        return pop;
        
    }

    /**
     * Get location in the space.
     * @return Location.
     */
    public double[] getLocation() {
        return location;
    }

    /**
     * Set location in the space.
     * @param location Location.
     */
    public void setLocation(double[] location) {
        this.location = location;
    }

    /**
     * Get fitness.
     * @return Fitness.
     */
    public double getFitness() {
        return fitness;
    }

    /**
     * Set fitness.
     * @param fitness Fitness.
     */
    public void setFitness(double fitness) {
        this.fitness = fitness;
    }
    
    /**
     * Initialize a new instance of the Individual class.
     * @param location Location.
     */
    public Individual(double[] location){
        this(location, Double.NaN);
    }

    /**
     * Initialize a new instance of the Individual class.
     * @param location Location.
     * @param fitness Fitness.
     */
    public Individual(double[] location, double fitness) {
        this.location = location;
        this.fitness = fitness;
    }

    @Override
    public int compareTo(Individual o) {
        return Double.compare(fitness, o.getFitness());
    }
    
    public Individual getClone(){
        try {
            // call clone in Object.
            return (Individual) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
}