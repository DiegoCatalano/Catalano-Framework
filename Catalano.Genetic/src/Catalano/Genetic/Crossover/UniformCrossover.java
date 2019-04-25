/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Catalano.Genetic.Crossover;

import Catalano.Genetic.IChromosome;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Diego
 */
public class UniformCrossover implements ICrossover<IChromosome>{

    public UniformCrossover() {}

    @Override
    public List<IChromosome> Compute(IChromosome chromosome1, IChromosome chromosome2) {
        
        IChromosome c1 = chromosome1.Clone();
        IChromosome c2 = chromosome2.Clone();
        
        int size = chromosome1.getLength();
        for (int i = 0; i < size; i++) {
            if(Math.random() <= 0.5){
                c1.setGene(i, chromosome1.getGene(i));
                c2.setGene(i, chromosome2.getGene(i));
            }
            else{
                c1.setGene(i, chromosome2.getGene(i));                
                c2.setGene(i, chromosome1.getGene(i));
            }
        }
        
        List<IChromosome> lst = new ArrayList<IChromosome>(2);
        lst.add(c1);
        lst.add(c2);
        
        return lst;
        
    }
    
}
