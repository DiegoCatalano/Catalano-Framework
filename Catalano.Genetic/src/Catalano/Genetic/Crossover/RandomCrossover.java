/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Catalano.Genetic.Crossover;

import Catalano.Genetic.IChromosome;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Diego
 */
public class RandomCrossover implements ICrossover<IChromosome>{
    
    private List<ICrossover> operators;

    public RandomCrossover(List<ICrossover> operators) {
        this.operators = operators;
    }

    @Override
    public List<IChromosome> Compute(IChromosome chromosome1, IChromosome chromosome2) {
        
        Random rand = new Random();
        int index = rand.nextInt(operators.size());
        return operators.get(index).Compute(chromosome1, chromosome2);
        
    }
    
}
