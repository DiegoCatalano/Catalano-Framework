/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Catalano.Genetic.Crossover;

import Catalano.Genetic.Chromosome.DoubleChromosome;
import Catalano.Math.Random.Random;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Diego
 */
public class BlendedCrossover implements ICrossover<DoubleChromosome>{
    
    private double alpha;

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public BlendedCrossover() {
        this(0.5);
    }

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