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
public abstract class ChromosomeBase implements IChromosome, Comparable, Cloneable {
    
    protected double fitness = 0;

    @Override
    public double getFitness() {
        return fitness;
    }

    @Override
    public abstract Object getGene(int index);

    @Override
    public abstract void setGene(int index, Object gene);

    @Override
    public abstract void Generate();
    
    @Override
    public abstract IChromosome CreateNew( );
    
    @Override
    public abstract IChromosome Clone( );
    
    @Override
    public void Evaluate( IFitness function )
    {
        fitness = function.Evaluate( this );
    }

    @Override
    public int compareTo(Object o) {
        ChromosomeBase c = (ChromosomeBase)o;
        return Double.compare(c.getFitness(), fitness);
    }
    
}
