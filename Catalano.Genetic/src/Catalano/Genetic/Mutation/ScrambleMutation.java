/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Catalano.Genetic.Mutation;

import Catalano.Core.ArraysUtil;
import Catalano.Genetic.IChromosome;
import java.util.Random;

/**
 *
 * @author Diego
 */
public class ScrambleMutation implements IMutation<IChromosome>{

    public ScrambleMutation() {}

    @Override
    public IChromosome Compute(IChromosome chromossome) {
        Random rand = new Random();
        
        IChromosome c = chromossome.Clone();
        
        int lenght = c.getLength();
        
        int a = rand.nextInt(lenght/2);
        int b = rand.nextInt(lenght/2) + lenght/2;
        
        int size = Math.abs(a-b) + 1;
        
        Object[] elem = new Object[size];
        for (int i = 0; i < size; i++) {
            elem[i] = c.getGene(i+a);
        }
        
        ArraysUtil.Shuffle(elem);
        
        for (int i = 0; i < size; i++) {
            c.setGene(i+a, elem[elem.length - i - 1]);
        }
        
        return c;
    }
    
}
