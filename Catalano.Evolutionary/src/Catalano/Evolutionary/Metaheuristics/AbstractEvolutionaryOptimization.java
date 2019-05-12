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

package Catalano.Evolutionary.Metaheuristics;

/**
 * Abstract class for evolutionary algorithms.
 * @author Diego Catalano
 */
public abstract class AbstractEvolutionaryOptimization implements IOptimization{
    
    protected int populationSize;
    protected int generations;
    protected long nEvals;
    
    /**
     * Get population size.
     * @return Population size.
     */
    public int getPopulationSize(){
        return populationSize;
    }
    
    /**
     * Set population size.
     * @param populationSize Population size.
     */
    public void setPopulationSize(int populationSize){
        this.populationSize = populationSize;
    }
    
    /**
     * Get number of generations.
     * @return Number of generations.
     */
    public int getGenerations(){
        return generations;
    }
    
    /**
     * Set number of generations
     * @param generations Generations.
     */
    public void setGenerations(int generations){
        this.generations = generations;
    }

    /**
     * Get number of evaluations.
     * @return Number of evaluations.
     */
    public long getNumberOfEvaluations() {
        return nEvals;
    }
    
}