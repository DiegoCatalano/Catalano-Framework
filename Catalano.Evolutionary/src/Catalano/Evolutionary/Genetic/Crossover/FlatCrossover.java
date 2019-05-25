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

package Catalano.Evolutionary.Genetic.Crossover;

import Catalano.Evolutionary.Genetic.Chromosome.DoubleChromosome;
import Catalano.Evolutionary.Genetic.Chromosome.FloatChromosome;
import Catalano.Evolutionary.Genetic.Chromosome.IChromosome;
import Catalano.Math.Random.Random;
import java.util.ArrayList;
import java.util.List;

/**
 * Flat Crossover.
 * 
 * Support: Double/Float Chromosome.
 * 
 * @author Diego Catalano
 */
public class FlatCrossover implements ICrossover<IChromosome>{

    /**
     * Initializes a new instance of the FlatCrossover class.
     */
    public FlatCrossover() {}

    @Override
    public List<IChromosome> Compute(IChromosome chromosome1, IChromosome chromosome2) {
        
        if(chromosome1 instanceof FloatChromosome){
            return Compute((FloatChromosome)chromosome1, (FloatChromosome)chromosome2);
        } else if (chromosome1 instanceof DoubleChromosome){
            return Compute((DoubleChromosome)chromosome1, (DoubleChromosome)chromosome2);
        } else{
            throw new IllegalArgumentException("Flat crossover only works with Double/Float chromosomes.");
        }
        
    }
    
    private List<IChromosome> Compute(DoubleChromosome chromosome1, DoubleChromosome chromosome2){
        Random rand = new Random();
        
        DoubleChromosome c1 = (DoubleChromosome)chromosome1.Clone();
        DoubleChromosome c2 = (DoubleChromosome)chromosome2.Clone();
        
        int length = c1.getLength();
        for (int i = 0; i < length; i++) {
            double min = Math.min((Double)c1.getGene(i), (Double)c2.getGene(i));
            double max = Math.max((Double)c1.getGene(i), (Double)c2.getGene(i));
            
            c1.setGene(i, rand.nextDouble(min, max));
            c2.setGene(i, rand.nextDouble(min, max));
        }
        
        List<IChromosome> lst = new ArrayList<>(2);
        lst.add(c1);
        lst.add(c2);
        
        return lst;
    }
    
    private List<IChromosome> Compute(FloatChromosome chromosome1, FloatChromosome chromosome2){
        Random rand = new Random();
        
        FloatChromosome c1 = (FloatChromosome)chromosome1.Clone();
        FloatChromosome c2 = (FloatChromosome)chromosome2.Clone();
        
        int length = c1.getLength();
        for (int i = 0; i < length; i++) {
            float min = Math.min((Float)c1.getGene(i), (Float)c2.getGene(i));
            float max = Math.max((Float)c1.getGene(i), (Float)c2.getGene(i));
            
            c1.setGene(i, (float)rand.nextDouble(min, max));
            c2.setGene(i, (float)rand.nextDouble(min, max));
        }
        
        List<IChromosome> lst = new ArrayList<>(2);
        lst.add(c1);
        lst.add(c2);
        
        return lst;
    }
    
}