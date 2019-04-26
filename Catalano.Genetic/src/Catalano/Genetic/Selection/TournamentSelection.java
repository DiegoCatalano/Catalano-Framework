/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Catalano.Genetic.Selection;

import Catalano.Core.ArraysUtil;
import Catalano.Genetic.Chromosome.IChromosome;
import Catalano.Math.Matrix;
import java.util.List;

/**
 *
 * @author Diego Catalano
 */
public class TournamentSelection implements ISelection{

    private int k;

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = Math.max(k, 2);
    }
    
    public TournamentSelection() {
        this(5);
    }

    public TournamentSelection(int k) {
        this.k = Math.max(k, 2);
    }

    @Override
    public int[] Compute(List<IChromosome> chromosomes) {
        
        int[] index = Matrix.Indices(0, chromosomes.size());
        ArraysUtil.Shuffle(index);
        
        //Choose k elements from the tournament
        int[] selected = new int[k];
        double[] fit = new double[k];
        
        for (int i = 0; i < k; i++) {
            selected[i] = index[i];
            fit[i] = chromosomes.get(index[i]).getFitness();
        }
        
        //order
        int[] order = ArraysUtil.Argsort(fit, false);
        
        return new int[] {selected[order[0]], selected[order[1]]};
        
    }
    
}