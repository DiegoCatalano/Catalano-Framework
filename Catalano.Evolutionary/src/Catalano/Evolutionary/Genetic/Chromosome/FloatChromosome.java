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

import Catalano.Math.Tools;
import java.util.Arrays;
import java.util.Random;

/**
 * Float Chromosome.
 * @author Diego Catalano
 */
public class FloatChromosome extends ChromosomeBase{
    
    private final int size;
    private float minValue;
    private float maxValue;
    
    private float[] values;

    /**
     * Get minimum value.
     * @return Minimum value.
     */
    public float getMinValue() {
        return minValue;
    }

    /**
     * Get maximum value.
     * @return Maximum value.
     */
    public float getMaxValue() {
        return maxValue;
    }

    /**
     * Get values.
     * @return Values.
     */
    public float[] getValues() {
        return values;
    }
    
    /**
     * Initializes a new instance of the DoubleChromosome class.
     * @param values Values.
     * @param minValue Minimum value.
     * @param maxValue Maximum value.
     */
    public FloatChromosome(float[] values, float minValue, float maxValue){
        this.size = values.length;
        this.values = values;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    /**
     * Initializes a new instance of the DoubleChromosome class.
     * @param size Size of the chromosome.
     * @param minValue Minimum value.
     * @param maxValue Maximum value.
     */
    public FloatChromosome(int size, float minValue, float maxValue) {
        this.size = size;
        this.minValue = minValue;
        this.maxValue = maxValue;
        Generate();
    }

    @Override
    public Object getGene(int index) {
        return values[index];
    }

    @Override
    public void setGene(int index, Object gene) {
        values[index] = (Float)gene;
    }

    @Override
    public void Generate() {
        
        Random rand = new Random();
        
        values = new float[size];
        for (int i = 0; i < size; i++) {
            values[i] = Tools.Scale(0f, 1f, minValue, maxValue, rand.nextFloat());
        }
        
    }

    @Override
    public IChromosome CreateNew() {
        return new FloatChromosome(size, minValue, maxValue);
    }

    @Override
    public IChromosome Clone() {
        FloatChromosome c = new FloatChromosome(Arrays.copyOf(values, values.length), minValue, maxValue);
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