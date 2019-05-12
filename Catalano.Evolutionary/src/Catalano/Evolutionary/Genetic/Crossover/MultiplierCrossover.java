/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Catalano.Evolutionary.Genetic.Crossover;

import Catalano.Evolutionary.Genetic.Chromosome.DoubleChromosome;
import Catalano.Evolutionary.Genetic.Chromosome.IChromosome;
import Catalano.Math.Tools;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Diego
 */
public class MultiplierCrossover implements ICrossover<DoubleChromosome>{
    
    private double f;

    public MultiplierCrossover() {
        this(0.5);
    }

    public MultiplierCrossover(double f) {
        this.f = f;
    }

    @Override
    public List<DoubleChromosome> Compute(DoubleChromosome chromosome1, DoubleChromosome chromosome2) {
        
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
        
        List<DoubleChromosome> lst = new ArrayList<DoubleChromosome>();
        lst.add(c1);
        lst.add(c2);
        
        return lst;
        
    }
    
}
