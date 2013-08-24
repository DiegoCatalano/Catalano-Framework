// Catalano Neuro Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2013
// diego.catalano at live.com
//
// Copyright © Andrew Kirillov, 2007-2008
// andrew.kirillov at gmail.com
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

package Catalano.Neuro;

import Catalano.Core.FloatRange;
import java.util.Random;

/**
 * Base neuron class.
 * @author Diego Catalano
 */
public abstract class Neuron {
    
    /**
     * Neuron's inputs count.
     */
    protected int inputsCount = 0;
    
    /**
     * Neuron's weights.
     */
    protected double[] weights = null;
    
    /**
     * Neuron's output value.
     */
    protected double output = 0;
    
    /**
     * Random number generator.
     */
    protected Random r = new Random();
    
    /**
     * Random generator range.
     * Sets the range of random generator. Affects initial values of neuron's weight. Default value is [0, 1].
     */
    protected static FloatRange range = new FloatRange(0.0f, 1.0f);

    /**
     * 
     * @return
     */
    public static FloatRange getRange() {
        return range;
    }

    /**
     * 
     * @param range
     */
    public static void setRange(FloatRange range) {
        Neuron.range = range;
    }
    
    /**
     * Get Neuron's inputs count.
     * @return Neuron's inputs count.
     */
    public int getInputCount(){
        return inputsCount;
    }
    
    /**
     * Get Neuron's output value.
     * The calculation way of neuron's output value is determined by inherited class.
     * @return Neuron's output value.
     */
    public double getOutput(){
        return output;
    }
    
    /**
     * Get Neuron's weights accessor.
     * @param id Weight index.
     * @return Weights.
     */
    public double getWeight(int id){
        return weights[id];
    }
    
    /**
     * Set Neuron's weights accessor.
     * @param id Weight index.
     * @param weight Weights.
     */
    public void setWeight(int id, double weight){
        this.weights[id] = weight;
    }
    
    /**
     * Get Neuron's weights accessor.
     * @return Weights.
     */
    public double[] getWeights(){
        return weights;
    }
    
    /**
     * Initializes a new instance of the Neuron class.
     * @param inputs Neuron's inputs count.
     */
    protected Neuron(int inputs){
        inputsCount = Math.max( 1, inputs );
        weights = new double[inputsCount];
        Randomize();
    }
    
    /**
     * Randomize neuron.
     * Initialize neuron's weights with random values within the range specified by Range.
     */
    public void Randomize(){
        double d = range.length();
        
        for ( int i = 0; i < inputsCount; i++ )
            weights[i] = r.nextDouble( ) * d + range.getMin();
        
    }
    
    /**
     * Computes output value of neuron.
     * @param input Input vector.
     * @return Returns neuron's output value.
     */
    public abstract double Compute( double[] input );
}