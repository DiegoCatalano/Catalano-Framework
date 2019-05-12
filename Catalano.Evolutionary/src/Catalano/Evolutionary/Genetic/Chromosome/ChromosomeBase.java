// Catalano Genetic Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2019
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

package Catalano.Evolutionary.Genetic.Chromosome;

import Catalano.Evolutionary.Genetic.IFitness;

/**
 * Base class for the chromosomes.
 * @author Diego Catalano
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
