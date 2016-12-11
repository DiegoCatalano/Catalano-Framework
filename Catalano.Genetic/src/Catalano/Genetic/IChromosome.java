/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Catalano.Genetic;

import Catalano.Genetic.Fitness.IFitness;

/**
 *
 * @author Diego
 */
public interface IChromosome<T> {
    
    //Basic functions
    public void Generate();
    public IChromosome CreateNew();
    public IChromosome Clone();
    public double getFitness();
    public int getLength();
    
    public T getGene(int index);
    public void setGene(int index, T gene);
    
    //Operators
    public void Evaluate(IFitness function);
    
}
