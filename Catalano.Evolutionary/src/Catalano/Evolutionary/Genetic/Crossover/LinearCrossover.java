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
import java.util.ArrayList;
import java.util.List;

/**
 * Linear Crossover.
 * 
 * Support: Double/Float Chromosome.
 * 
 * @author Diego Catalano
 */
public class LinearCrossover implements ICrossover<IChromosome>{

    /**
     * Initializes a new Instance of the LinearCrossover class.
     */
    public LinearCrossover() {}

    @Override
    public List<IChromosome> Compute(IChromosome chromosome1, IChromosome chromosome2) {
        
        if(chromosome1 instanceof FloatChromosome){
            return Compute((FloatChromosome)chromosome1, (FloatChromosome)chromosome2);
        } else if (chromosome1 instanceof DoubleChromosome){
            return Compute((DoubleChromosome)chromosome1, (DoubleChromosome)chromosome2);
        } else{
            throw new IllegalArgumentException("Linear crossover only works with Double/Float chromosomes.");
        }
        
    }
    
    private List<IChromosome> Compute(DoubleChromosome chromosome1, DoubleChromosome chromosome2) {
        
        double[] c1 = new double[chromosome1.getLength()];
        double[] c2 = new double[chromosome1.getLength()];
        double[] c3 = new double[chromosome1.getLength()];
        
        for (int i = 0; i < c1.length; i++) {
            c1[i] = 0.5 * (Double)chromosome1.getGene(i) + 0.5 * (Double)chromosome2.getGene(i);
            c2[i] = 1.5 * (Double)chromosome1.getGene(i) - 0.5 * (Double)chromosome2.getGene(i);
            c3[i] = (-0.5 * (Double)chromosome1.getGene(i)) + 1.5 * (Double)chromosome2.getGene(i);
        }
        
        List<IChromosome> list = new ArrayList<>(3);
        list.add(new DoubleChromosome(c1, chromosome1.getMinValue(), chromosome1.getMaxValue()));
        list.add(new DoubleChromosome(c2, chromosome1.getMinValue(), chromosome1.getMaxValue()));
        list.add(new DoubleChromosome(c3, chromosome1.getMinValue(), chromosome1.getMaxValue()));
        return list;
        
    }
    
    private List<IChromosome> Compute(FloatChromosome chromosome1, FloatChromosome chromosome2) {
        
        float[] c1 = new float[chromosome1.getLength()];
        float[] c2 = new float[chromosome1.getLength()];
        float[] c3 = new float[chromosome1.getLength()];
        
        for (int i = 0; i < c1.length; i++) {
            c1[i] = 0.5f * (Float)chromosome1.getGene(i) + 0.5f * (Float)chromosome2.getGene(i);
            c2[i] = 1.5f * (Float)chromosome1.getGene(i) - 0.5f * (Float)chromosome2.getGene(i);
            c3[i] = (-0.5f * (Float)chromosome1.getGene(i)) + 1.5f * (Float)chromosome2.getGene(i);
        }
        
        List<IChromosome> list = new ArrayList<>(3);
        list.add(new FloatChromosome(c1, chromosome1.getMinValue(), chromosome1.getMaxValue()));
        list.add(new FloatChromosome(c2, chromosome1.getMinValue(), chromosome1.getMaxValue()));
        list.add(new FloatChromosome(c3, chromosome1.getMinValue(), chromosome1.getMaxValue()));
        return list;
        
    }
    
}