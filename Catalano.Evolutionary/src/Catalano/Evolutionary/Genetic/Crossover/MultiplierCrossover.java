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
import Catalano.Math.Tools;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Multiplier Crossover.
 * 
 * Support: Double/Float Chromosome.
 * 
 * @author Diego Catalano
 */
public class MultiplierCrossover implements ICrossover<IChromosome>{
   
    /**
     * Initializes a new instance of the MultiplierCrossover class.
     */
    public MultiplierCrossover() {}

    @Override
    public List<IChromosome> Compute(IChromosome chromosome1, IChromosome chromosome2) {
        
        if(chromosome1 instanceof FloatChromosome){
            return Compute((FloatChromosome)chromosome1, (FloatChromosome)chromosome2);
        } else if (chromosome1 instanceof DoubleChromosome){
            return Compute((DoubleChromosome)chromosome1, (DoubleChromosome)chromosome2);
        } else{
            throw new IllegalArgumentException("Multiplier crossover only works with Double/Float chromosomes.");
        }
        
    }
    
    private List<IChromosome> Compute(DoubleChromosome chromosome1, DoubleChromosome chromosome2){
        Random rand = new Random();
        
        DoubleChromosome c1 = (DoubleChromosome)chromosome1.Clone();
        DoubleChromosome c2 = (DoubleChromosome)chromosome2.Clone();
        
        double[] v1 = c1.getValues();
        double[] v2 = c2.getValues();
        
        for (int i = 0; i < v1.length; i++) {
            v1[i] = v1[i] + rand.nextDouble() * (v1[i] - v2[i]);
            v2[i] = v2[i] + rand.nextDouble() * (v1[i] - v2[i]);
            
            v1[i] = Tools.Clamp(v1[i], c1.getMinValue(), c1.getMaxValue());
            v2[i] = Tools.Clamp(v2[i], c1.getMinValue(), c1.getMaxValue());
        }
        
        List<IChromosome> lst = new ArrayList<>();
        lst.add(c1);
        lst.add(c2);
        
        return lst;
    }
    
    private List<IChromosome> Compute(FloatChromosome chromosome1, FloatChromosome chromosome2){
        Random rand = new Random();
        
        FloatChromosome c1 = (FloatChromosome)chromosome1.Clone();
        FloatChromosome c2 = (FloatChromosome)chromosome2.Clone();
        
        float[] v1 = c1.getValues();
        float[] v2 = c2.getValues();
        
        for (int i = 0; i < v1.length; i++) {
            v1[i] = v1[i] + rand.nextFloat()* (v1[i] - v2[i]);
            v2[i] = v2[i] + rand.nextFloat()* (v1[i] - v2[i]);
            
            v1[i] = Tools.Clamp(v1[i], c1.getMinValue(), c1.getMaxValue());
            v2[i] = Tools.Clamp(v2[i], c1.getMinValue(), c1.getMaxValue());
        }
        
        List<IChromosome> lst = new ArrayList<>();
        lst.add(c1);
        lst.add(c2);
        
        return lst;
    }
    
}
