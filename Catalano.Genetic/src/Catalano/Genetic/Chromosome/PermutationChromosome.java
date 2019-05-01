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

package Catalano.Genetic.Chromosome;

import Catalano.Core.ArraysUtil;
import Catalano.Math.Matrix;
import java.util.Arrays;

/**
 * Permutation Chromosome.
 * @author Diego Catalano
 */
public class PermutationChromosome extends ChromosomeBase {
    
    private int[] data;
    
    @Override
    public int getLength() {
        return data.length;
    }

    @Override
    public Object getGene(int index) {
        return data[index];
    }

    @Override
    public void setGene(int index, Object gene) {
        this.data[index] = (Integer)gene;
    }

    /**
     * Get data.
     * @return Data.
     */
    public int[] getData() {
        return data;
    }

    /**
     * Initializes a new instance of the PermutationChromosome class.
     * @param lenght Length of the chromosome.
     */
    public PermutationChromosome(int lenght) {
        this.data = Matrix.Indices(0, lenght);
        ArraysUtil.Shuffle(data);
    }
    
    /**
     * Initializes a new instance of the PermutationChromosome class.
     * @param data Data.
     */
    public PermutationChromosome(int[] data){
        this.data = data;
    }

    @Override
    public void Generate() {
        
        data = Matrix.Indices(0, data.length);
        ArraysUtil.Shuffle(data);
        
    }

    @Override
    public IChromosome CreateNew() {
        return new PermutationChromosome(data.length);
    }

    @Override
    public IChromosome Clone() {
        return new PermutationChromosome(Arrays.copyOf(data, data.length));
    }

    @Override
    public String toString() {
        
        String str = "";
        for (int i = 0; i < data.length; i++) {
            str += String.valueOf(data[i]);
        }
        
        return str;
    }
}