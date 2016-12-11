/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Catalano.Genetic.Selection;

import java.util.Random;

/**
 *
 * @author Diego
 */
public class RandomSelection implements IRealCodedSelection{

    public RandomSelection() {}

    @Override
    public int[] Compute(double[] fitness) {
        
        Random rand = new Random();
        
        int[] index = new int[2];
        index[0] = rand.nextInt(fitness.length);
        index[1] = rand.nextInt(fitness.length);
        
        return index;
        
    }
}