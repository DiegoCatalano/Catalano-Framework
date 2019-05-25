// Catalano Genetic Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
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

package Catalano.Evolutionary.Genetic.Mutation;

import Catalano.Evolutionary.Genetic.Chromosome.DoubleChromosome;
import Catalano.Evolutionary.Genetic.Chromosome.FloatChromosome;
import Catalano.Evolutionary.Genetic.Chromosome.IChromosome;
import Catalano.Math.Tools;
import java.util.Random;

/**
 * Multiplier Mutation.
 * 
 * Support: Double/Float chromosome.
 * 
 * @author Diego Catalano
 */
public class MultiplierMutation implements IMutation<IChromosome>{
    
    private float balancer;

    /**
     * Get balancer value.
     * @return Balancer value.
     */
    public double getBalancer() {
        return balancer;
    }

    /**
     * Set balancer value.
     * @param balancer Balancer value.
     */
    public void setBalancer(float balancer) {
        this.balancer = balancer;
    }

    /**
     * Initializes a new instance of the MultiplierMutation class.
     */
    public MultiplierMutation() {
        this(0.5f);
    }

    /**
     * Initializes a new instance of the MultiplierMutation class.
     * @param balancer Balancer value.
     */
    public MultiplierMutation(float balancer) {
        this.balancer = balancer;
    }

    @Override
    public IChromosome Compute(IChromosome chromossome) {
        
        if(chromossome instanceof FloatChromosome){
            return Compute((FloatChromosome)chromossome);
        } else if (chromossome instanceof DoubleChromosome){
            return Compute((DoubleChromosome)chromossome);
        } else{
            throw new IllegalArgumentException("Multiplier mutation only works with Double/Float chromosomes.");
        }
    }
    
    private IChromosome Compute(DoubleChromosome chromossome){
        Random rand = new Random();
        
        DoubleChromosome c = (DoubleChromosome)chromossome.Clone();
        
        int gene = rand.nextInt(c.getLength());
        if(rand.nextDouble() < balancer){
            double value = (Double)chromossome.getGene(gene) * rand.nextDouble();
            value = Tools.Clamp(value, chromossome.getMinValue(), chromossome.getMaxValue());

            c.setGene(gene, value);
        } else{
            double value = (Double)chromossome.getGene(gene) + rand.nextDouble();
            value = Tools.Clamp(value, chromossome.getMinValue(), chromossome.getMaxValue());

            c.setGene(gene, value);
        }

        return c;
    }
    
    private IChromosome Compute(FloatChromosome chromossome){
        Random rand = new Random();
        
        FloatChromosome c = (FloatChromosome)chromossome.Clone();
        
        int gene = rand.nextInt(c.getLength());
        if(rand.nextDouble() < balancer){
            float value = (Float)chromossome.getGene(gene) * rand.nextFloat();
            value = Tools.Clamp(value, chromossome.getMinValue(), chromossome.getMaxValue());

            c.setGene(gene, value);
        } else{
            float value = (Float)chromossome.getGene(gene) + rand.nextFloat();
            value = Tools.Clamp(value, chromossome.getMinValue(), chromossome.getMaxValue());

            c.setGene(gene, value);
        }

        return c;
    }
    
}