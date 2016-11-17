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

package Catalano.Genetic.Crossover;

import Catalano.Genetic.BinaryChromossome;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Diego
 */
public class SinglePointCrossover implements ICrossover{
    
    private float prob;
    private long seed;

    public SinglePointCrossover() {
        this(0.8f);
    }

    public SinglePointCrossover(float prob) {
        this(prob, 0);
    }

    public SinglePointCrossover(float prob, long seed) {
        this.prob = prob;
        this.seed = seed;
    }

    @Override
    public List<BinaryChromossome> Compute(List<BinaryChromossome> chromossomes) {
        
        List<BinaryChromossome> lst = new ArrayList<>(2);
        
        Random r = new Random();
        if(seed != 0) r.setSeed(seed);
        
        //First element
        if(r.nextFloat() < prob){
            String bin1 = chromossomes.get(0).toBinary();
            String bin2 = chromossomes.get(1).toBinary();
            
            int middle = chromossomes.get(0).getNumberOfBits() / 2;
            String newBin = bin1.substring(0, middle);
            newBin += bin2.substring(middle, chromossomes.get(0).getNumberOfBits());
            
            BinaryChromossome bc = new BinaryChromossome(chromossomes.get(0));
            bc.setValue(newBin, 2);
            lst.add(bc);
            
        }
        else{
            lst.add(chromossomes.get(0).clone());
        }
        
        //Second element
        if(Math.random() < prob){
            String bin1 = chromossomes.get(1).toBinary();
            String bin2 = chromossomes.get(0).toBinary();
            
            int middle = chromossomes.get(0).getNumberOfBits() / 2;
            String newBin = bin1.substring(0, middle);
            newBin += bin2.substring(middle, chromossomes.get(0).getNumberOfBits());
            
            BinaryChromossome bc = new BinaryChromossome(chromossomes.get(0));
            bc.setValue(newBin, 2);
            lst.add(bc);
        }
        else{
            lst.add(chromossomes.get(1).clone());
        }
        
        return lst;
        
    }
    
}