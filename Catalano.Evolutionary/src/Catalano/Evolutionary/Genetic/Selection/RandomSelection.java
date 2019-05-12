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

import Catalano.Evolutionary.Genetic.Chromosome.IChromosome;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Diego Catalano
 */
public class RandomSelection implements ISelection, IRealCodedSelection{
    
    private List<ISelection> selections;

    /**
     * Initializes a new instance of the ElitismSelection class.
     * @param selection List of selection operators.
     */
    public RandomSelection(List<ISelection> selection) {
        this.selections = selection;
    }

    @Override
    public int[] Compute(double[] fitness) {
        
        Random rand = new Random();
        
        int[] index = new int[2];
        index[0] = rand.nextInt(fitness.length);
        index[1] = rand.nextInt(fitness.length);
        
        return index;
        
    }

    @Override
    public int[] Compute(List<IChromosome> chromosomes) {
        
        Random rand = new Random();
        
        return selections.get(rand.nextInt(selections.size())).Compute(chromosomes);
    }
}