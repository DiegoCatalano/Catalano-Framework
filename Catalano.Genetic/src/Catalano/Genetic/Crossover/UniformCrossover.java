/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Catalano.Genetic.Crossover;

import Catalano.Genetic.BinaryChromosome;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Diego
 */
public class UniformCrossover implements ICrossover<BinaryChromosome>{

    public UniformCrossover() {}

    @Override
    public List<BinaryChromosome> Compute(BinaryChromosome chromosome1, BinaryChromosome chromosome2) {
        
        String c1 = "";
        String c2 = "";
        int size = chromosome1.getLength();
        for (int i = 0; i < size; i++) {
            if(Math.random() <= 0.5){
                c1 = chromosome1.getGene(i).toString();
                c2 = chromosome2.getGene(i).toString();
            }
            else{
                c1 = chromosome2.getGene(i).toString();
                c2 = chromosome1.getGene(i).toString();
            }
        }
        
        List<BinaryChromosome> lst = new ArrayList<BinaryChromosome>(2);
        lst.add(new BinaryChromosome(size, c1));
        lst.add(new BinaryChromosome(size, c2));
        
        return lst;
        
    }
    
}
