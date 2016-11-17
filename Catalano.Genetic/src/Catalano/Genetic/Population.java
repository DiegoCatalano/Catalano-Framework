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

package Catalano.Genetic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Diego
 */
public class Population {
    
    private List<BinaryChromossome> chromossomes;
    
    public static Population Generate(int nBits, int size, long seed){
        
        Random r = new Random();
        if(seed != 0) r.setSeed(seed);
        
        List<BinaryChromossome> pop = new ArrayList<BinaryChromossome>(size);
        for (int i = 0; i < size; i++) {
            pop.add(BinaryChromossome.Generate(nBits, seed));
        }
        
        return new Population(pop);
        
    }

    public List<BinaryChromossome> getChromossomes() {
        return chromossomes;
    }

    public void setChromossomes(List<BinaryChromossome> chromossomes) {
        this.chromossomes = chromossomes;
    }

    public Population(List<BinaryChromossome> chromossomes) {
        this.chromossomes = chromossomes;
    }
    
    public void Sort(){
        Sort(false);
    }
    
    public void Sort(boolean ascending){
        if(ascending)
            Collections.reverse(chromossomes);
        else
            Collections.sort(chromossomes);
    }
    
}