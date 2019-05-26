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

package Catalano.Evolutionary.Genetic.Mutation;

import Catalano.Evolutionary.Genetic.Chromosome.IChromosome;
import Catalano.Math.Matrix;
import java.util.Arrays;
import java.util.Random;

/**
 * Displacement Mutation.
 * 
 * Support: Binary/Permutation/Integer/Double/Float Chromosome.
 * 
 * @author Diego Catalano
 */
public class DisplacementMutation implements IMutation<IChromosome>{

    /**
     * Initializes a new instance of the DisplacementMutation class.
     */
    public DisplacementMutation() {}
    
    @Override
    public IChromosome Compute(IChromosome chromossome) {
        
        Random rand = new Random();
        
        IChromosome c = chromossome.Clone();
        
        int lenght = c.getLength();
        
        int[] order = {rand.nextInt(lenght-1), rand.nextInt(lenght-1)};
        Arrays.sort(order);
        
        int idx;
        //Copy the elements between the order
        idx = 0;
        Object[] elem = new Object[order[1] - order[0]];
        for (int i = order[0]; i < order[1]; i++) {
            elem[idx++] = c.getGene(i);
        }
        
        //Copy the elements out of order
        idx = 0;
        Object[] elem2 = new Object[lenght - (order[1] - order[0])];
        for (int i = 0; i < lenght; i++) {
            if(i < order[0] || i >= order[1]){
                elem2[idx++] = c.getGene(i);
            }
        }
        
        //Insert the elements
        idx = rand.nextInt(elem2.length);
        elem = Matrix.InsertColumns(elem2, elem, idx);
        
        for (int i = 0; i < elem.length; i++) {
            c.setGene(i, elem[i]);
        }
        
        return c;
        
    }
    
}