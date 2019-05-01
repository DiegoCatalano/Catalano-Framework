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

package Catalano.Genetic.Selection;

import Catalano.Core.ArraysUtil;
import Catalano.Genetic.Chromosome.IChromosome;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Rank Selection.
 * @author Diego Catalano
 */
public class RankSelection implements ISelection{
    
    private long seed;

    /**
     * Initializes a new instance of the RankSelection class.
     */
    public RankSelection() {}

    @Override
    public int[] Compute(List<IChromosome> chromosomes) {
        
        
        //Scale fitness based on the rank
        int[] index = new int[2];
        double[] fitness = new double[chromosomes.size()];
        
        for (int i = 0; i < fitness.length; i++) {
            fitness[i] = chromosomes.get(i).getFitness();
        }
        
        int[] indexes = ArraysUtil.Argsort(fitness, true);
        for (int i = 0; i < fitness.length; i++) {
            fitness[i] = indexes[i] / (double)indexes.length;
        }
        
        //Sort the fitness values
        Arrays.sort(fitness);
        
        //Select the chromossomes
        Random r = new Random();
        if(seed != 0) r.setSeed(seed);
        
        for (int i = 0; i < index.length; i++) {
            double v = r.nextDouble();
            for (int j = 0; j < fitness.length; j++) {
                if(v >= fitness[j]){
                    index[i] = indexes[j];
                }
            }
        }
        
        return index;
        
        
    }
    
}