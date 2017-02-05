/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Catalano.Genetic.Selection;

import Catalano.Core.ArraysUtil;
import Catalano.Genetic.IChromosome;
import Catalano.Math.Matrix;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Diego
 */
public class TournamentSelection implements ISelection{

    private int k;
    
    public TournamentSelection() {
        this(5);
    }

    public TournamentSelection(int k) {
        this.k = k;
    }

    @Override
    public int[] Compute(List<IChromosome> chromosomes) {
        
        int[] index = Matrix.Indices(0, chromosomes.size());
        ArraysUtil.Shuffle(index);
        
        //Choose k elements from the tournament
        double[] fit = new double[k];
        for (int i = 0; i < k; i++) {
            fit[i] = chromosomes.get(index[i]).getFitness();
        }
        
        //order
        int[] order = ArraysUtil.Argsort(fit, false);
        
        return Arrays.copyOf(order, 2);
        
    }
    
}
