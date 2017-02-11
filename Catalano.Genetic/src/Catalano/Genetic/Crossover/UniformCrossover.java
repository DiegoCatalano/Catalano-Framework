/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Catalano.Genetic.Crossover;

import Catalano.Genetic.BinaryChromosome;
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
        
        List<Object> c1 = new ArrayList<Object>(chromosome1.getLength());
        List<Object> c2 = new ArrayList<Object>(chromosome1.getLength());
        
        int size = chromosome1.getLength();
        for (int i = 0; i < size; i++) {
            if(Math.random() <= 0.5){
                c1.add(chromosome1.getGene(i));
                c2.add(chromosome2.getGene(i));
            }
            else{
                c1.add(chromosome2.getGene(i));                
                c2.add(chromosome1.getGene(i));
            }
        }
        
        IChromosome cc1 = chromosome1.Clone();
        IChromosome cc2 = chromosome2.Clone();
        
        for (int i = 0; i < cc1.getLength(); i++) {
            cc1.setGene(i, c1.get(i));
            cc2.setGene(i, c2.get(i));
        }
        
        List<IChromosome> lst = new ArrayList<IChromosome>(2);
        lst.add(cc1);
        lst.add(cc2);
        
        return lst;
        
    }
    
}
