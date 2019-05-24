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
import java.util.ArrayList;
import java.util.List;

/**
 * Linear Crossover.
 * 
 * Support: Double Chromosome.
 * 
 * @author Diego Catalano
 */
public class LinearCrossover implements ICrossover<DoubleChromosome>{

    /**
     * Initializes a new Instance of the LinearCrossover class.
     */
    public LinearCrossover() {}

    @Override
    public List<DoubleChromosome> Compute(DoubleChromosome chromosome1, DoubleChromosome chromosome2) {
        
        double[] c1 = new double[chromosome1.getLength()];
        double[] c2 = new double[chromosome1.getLength()];
        double[] c3 = new double[chromosome1.getLength()];
        
        for (int i = 0; i < c1.length; i++) {
            c1[i] = 0.5 * (Double)chromosome1.getGene(i) + 0.5 * (Double)chromosome2.getGene(i);
            c2[i] = 1.5 * (Double)chromosome1.getGene(i) - 0.5 * (Double)chromosome2.getGene(i);
            c3[i] = (-0.5 * (Double)chromosome1.getGene(i)) + 1.5 * (Double)chromosome2.getGene(i);
        }
        
        List<DoubleChromosome> list = new ArrayList<>(3);
        list.add(new DoubleChromosome(c1, chromosome1.getMinValue(), chromosome1.getMaxValue()));
        list.add(new DoubleChromosome(c2, chromosome1.getMinValue(), chromosome1.getMaxValue()));
        list.add(new DoubleChromosome(c3, chromosome1.getMinValue(), chromosome1.getMaxValue()));
        return list;
        
    }
    
}