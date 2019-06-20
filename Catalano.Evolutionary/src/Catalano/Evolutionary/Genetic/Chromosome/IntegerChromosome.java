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

package Catalano.Evolutionary.Genetic.Chromosome;

import java.util.Arrays;
import java.util.Random;

/**
 * Integer Chromosome.
 * @author Diego Catalano
 */
public class IntegerChromosome extends ChromosomeBase{
    
    private final int size;
    private final int maxValue;
    
    private int[] values;

    /**
     * Get values.
     * @return Values.
     */
    public int[] getData() {
        return values;
    }

    /**
     * Get maximum value of the chromosome.
     * @return Maximum value of the chromosome.
     */
    public int getMaxValue() {
        return maxValue;
    }
    
    /**
     * Initializes a new instance of the IntegerChromosome class.
     * @param values Values.
     * @param maxValue Maximum value.
     */
    public IntegerChromosome(int[] values, int maxValue){
        this.size = values.length;
        this.values = values;
        this.maxValue = maxValue;
    }

    /**
     * Initializes a new instance of the PermutationChromosome class.
     * @param size Size of the chromosome.
     * @param maxValue Maximum value.
     */
    public IntegerChromosome(int size, int maxValue) {
        this.size = size;
        this.maxValue = maxValue;
        Generate();
    }

    @Override
    public Object getGene(int index) {
        return values[index];
    }

    @Override
    public void setGene(int index, Object gene) {
        values[index] = (Integer)gene;
    }

    @Override
    public void Generate() {
        
        Random rand = new Random();
        
        values = new int[size];
        for (int i = 0; i < size; i++) {
            values[i] = rand.nextInt(maxValue);
        }
        
    }

    @Override
    public IChromosome CreateNew() {
        return new IntegerChromosome(size, maxValue);
    }

    @Override
    public IChromosome Clone() {
        IntegerChromosome c = new IntegerChromosome(Arrays.copyOf(values, values.length), maxValue);
        c.fitness = this.fitness;
        return c;
    }

    @Override
    public int getLength() {
        return size;
    }

    @Override
    public String toString() {
        
        String str = "";
        for (int i = 0; i < size; i++) {
            str += String.valueOf(values[i]);
        }
        
        return str;
        
    }
    
}