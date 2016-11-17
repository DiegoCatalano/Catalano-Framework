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
import Catalano.Genetic.BinaryChromossome;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Diego
 */
public class ElitismSelection implements ISelection{
    
    private int size;

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void setSize(int size) {
        this.size = size;
    }

    public ElitismSelection(int size) {
        this.size = size;
    }

    @Override
    public int[] Compute(List<BinaryChromossome> chromossomes) {
        
        //Sort the chromossomes
        double[] fitness = new double[chromossomes.size()];
        for (int i = 0; i < chromossomes.size(); i++) {
            fitness[i] = chromossomes.get(i).getFitness();
        }
        
        int[] index = ArraysUtil.Argsort(fitness, false);
        
        return Arrays.copyOfRange(index, 0, size);
        
    }

    
    
}
