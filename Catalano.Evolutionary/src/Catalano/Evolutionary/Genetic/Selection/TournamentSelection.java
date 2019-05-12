// Catalano Genetic Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2019
// diego.catalano at live.com
//
//
//    This library is free software; you can redistribute it and/or
//    modify it under the terms of the GNU Lesser General Public
//    License as published by the Free Software Foundation; either
//    version 2.1 of the License, or (at your option) any later version.
//
//    This library is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
//    Lesser General Public License for more details.
//
//    You should have received a copy of the GNU Lesser General Public
//    License along with this library; if not, write to the Free Software
//    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
//

package Catalano.Evolutionary.Genetic.Selection;

import Catalano.Core.ArraysUtil;
import Catalano.Evolutionary.Genetic.Chromosome.IChromosome;
import Catalano.Math.Matrix;
import java.util.List;

/**
 * Tournament Selection.
 * @author Diego Catalano
 */
public class TournamentSelection implements ISelection{

    private int k;

    /**
     * Get Number of individuals.
     * @return Number of indviduals.
     */
    public int getK() {
        return k;
    }

    /**
     * Set Number of individuals.
     * @param k Number of individuals.
     */
    public void setK(int k) {
        this.k = Math.max(k, 2);
    }
    
    /**
     * Initializes a new instance of the TournamentSelection class.
     */
    public TournamentSelection() {
        this(5);
    }

    /**
     * Initializes a new instance of the TournamentSelection class.
     * @param k Number of individuals
     */
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