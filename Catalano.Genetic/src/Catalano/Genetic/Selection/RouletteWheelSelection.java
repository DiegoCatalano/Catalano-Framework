// Catalano Genetic Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
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
import Catalano.Genetic.IChromosome;
import Catalano.Math.Matrix;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Diego
 */
public class RouletteWheelSelection implements ISelection{
    
    private long seed;

    public RouletteWheelSelection() {}

    public RouletteWheelSelection(long seed) {
        this.seed = seed;
    }

    @Override
    public int[] Compute(List<IChromosome> chromossomes) {
        
        //Scale fitness [0..1]
        int[] index = new int[2];
        double[] fitness = new double[chromossomes.size()];
        
        double max = 0;
        for (int i = 0; i < fitness.length; i++) {
            fitness[i] = chromossomes.get(i).getFitness();
            max = Math.max(max, fitness[i]);
        }
        
        for (int i = 0; i < fitness.length; i++) {
            fitness[i] /= max;
        }
        
        //Sort the fitness values
        int[] oriIndex = ArraysUtil.Argsort(fitness, true);
        Arrays.sort(fitness);
        
        //Select the chromossomes
        Random r = new Random();
        if(seed != 0) r.setSeed(seed);
        
        for (int i = 0; i < index.length; i++) {
            double v = r.nextDouble();
            for (int j = 0; j < fitness.length; j++) {
                if(v >= fitness[j]){
                    index[i] = oriIndex[j];
                }
            }
        }
        
        return index;
        
    }
}