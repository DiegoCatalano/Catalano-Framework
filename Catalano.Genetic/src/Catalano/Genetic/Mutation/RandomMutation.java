/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Catalano.Genetic.Mutation;

import Catalano.Genetic.IChromosome;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Diego
 */
public class RandomMutation implements IMutation<IChromosome> {
    
    private List<IMutation> operators;

    public RandomMutation(List<IMutation> operators) {
        this.operators = operators;
    }

    @Override
    public IChromosome Compute(IChromosome chromossome) {
        
        Random rand = new Random();
        
        IChromosome c = chromossome.Clone();
        int index = rand.nextInt(operators.size());
        return (IChromosome)operators.get(index).Compute(c);

    }
    
}
