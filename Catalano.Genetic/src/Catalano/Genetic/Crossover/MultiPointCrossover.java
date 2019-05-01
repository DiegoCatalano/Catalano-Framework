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

package Catalano.Genetic.Crossover;

import Catalano.Genetic.Chromosome.IChromosome;
import Catalano.Genetic.Chromosome.PermutationChromosome;
import Catalano.Math.Matrix;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Multi Point Crossover.
 * 
 * Support: Binary/Integer/Double Chromosome.
 * 
 * @author Diego Catalano
 */
public class MultiPointCrossover implements ICrossover<IChromosome> {
    
    private int n;

    /**
     * Get Number of Points.
     * @return Number of points.
     */
    public int getNumberOfPoints() {
        return n;
    }
    
    /**
     * Set Number of Points.
     * @param n Number of Points.
     */
    public void setNumberOfPoints(int n){
        this.n = Math.max(1, n);
    }

    /**
     * Initializes a new instance of the MultiPointCrossover class.
     */
    public MultiPointCrossover() {
        this(3);
    }

    /**
     * Initializes a new instance of the MultiPointCrossover class.
     * @param n Number of points.
     */
    public MultiPointCrossover(int n) {
        this.n = n;
    }

    @Override
    public List<IChromosome> Compute(IChromosome chromosome1, IChromosome chromosome2) {
        
        if(chromosome1 instanceof PermutationChromosome){
            throw new IllegalArgumentException("Permutation Chromosome is not supported.");
        }
        
        int length = chromosome1.getLength();
        
        //Cut points
        int[] indexes = Matrix.RandomPermutation(length);
        int[] cuts = Arrays.copyOf(indexes, n);
        Arrays.sort(cuts, 0, cuts.length);
        
        IChromosome c1 = chromosome1.Clone();
        IChromosome c2 = chromosome2.Clone();
        
        //Operations of change
        int start = 0;
        for (int c = 0; c < n; c++) {
            if(c % 2 == 0){
                for (int i = start; i < cuts[c]; i++) {
                    c1.setGene(i, chromosome1.getGene(i));
                    c2.setGene(i, chromosome2.getGene(i));
                }
            } else{
                for (int i = start; i < cuts[c]; i++) {
                    c1.setGene(i, chromosome2.getGene(i));
                    c2.setGene(i, chromosome1.getGene(i));
                }
            }
            
            start = cuts[c];
        }
        
        List<IChromosome> lst = new ArrayList<IChromosome>(2);
        lst.add(c1);
        lst.add(c2);
        
        return lst;
        
    }
    
}