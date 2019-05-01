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

package Catalano.Genetic.Crossover;

import Catalano.Genetic.Chromosome.DoubleChromosome;
import Catalano.Math.Random.Random;
import java.util.ArrayList;
import java.util.List;

/**
 * Blended Crossover.
 * 
 * Support: Double Chromosome.
 * 
 * @author Diego Catalano
 */
public class BlendedCrossover implements ICrossover<DoubleChromosome>{
    
    private double alpha;

    /**
     * Get alpha.
     * @return Alpha value.
     */
    public double getAlpha() {
        return alpha;
    }

    /**
     * Set alpha value.
     * @param alpha Alpha.
     */
    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    /**
     * Initializes a new instance of the BlendedCrossover class.
     */
    public BlendedCrossover() {
        this(0.5);
    }

    /**
     * Initializes a new instance of the BlendedCrossover class.
     * @param alpha Alpha value.
     */
    public BlendedCrossover(double alpha) {
        this.alpha = alpha;
    }

    @Override
    public List<DoubleChromosome> Compute(DoubleChromosome chromosome1, DoubleChromosome chromosome2) {
        
        Random rand = new Random();
        
        DoubleChromosome c1 = (DoubleChromosome)chromosome1.Clone();
        DoubleChromosome c2 = (DoubleChromosome)chromosome2.Clone();
        
        int length = c1.getLength();
        for (int i = 0; i < length; i++) {
            
            double min = Math.min((Double)c1.getGene(i), (Double)c2.getGene(i));
            double max = Math.max((Double)c1.getGene(i), (Double)c2.getGene(i));
            double delta = max - min;
            
            c1.setGene(i, rand.nextDouble(min - alpha*delta, max + alpha*delta));
            c2.setGene(i, rand.nextDouble(min - alpha*delta, max + alpha*delta));
            
        }
        
        List<DoubleChromosome> lst = new ArrayList<DoubleChromosome>(2);
        lst.add(c1);
        lst.add(c2);
        
        return lst;
        
    }
}