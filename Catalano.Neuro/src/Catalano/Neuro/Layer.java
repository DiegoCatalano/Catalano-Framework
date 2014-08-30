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

/**
 * Base neural layer class.
 * @author Diego Catalano
 */
public abstract class Layer {
    
    /**
     * Layer's inputs count.
     */
    protected int inputsCount = 0;
    /**
     * Layer's neurons count.
     */
    protected int neuronsCount = 0;
    /**
     * Layer's neurons.
     */
    protected Neuron[] neurons;
    /**
     * Layer's output vector.
     */
    protected double[] output;

    /**
     * Get Layer's inputs count.
     * @return Layer's inputs count.
     */
    public int getInputsCount() {
        return inputsCount;
    }
    
    /**
     * Get Layer's neurons count.
     * @return Layer's neurons count.
     */
    public int getNeuronsCount(){
        return neuronsCount;
    }
    
    /**
     * Get Neurons from layer.
     * @return Neurons.
     */
    public Neuron[] getNeurons(){
        return neurons;
    }
    
    /*
     * Get specified neuron from layer.
     */
    public Neuron getNeuron(int id){
        return neurons[id];
    }

    /**
     * Get Layer's output vector.
     * @return Layer's output vector.
     */
    public double[] getOutput() {
        return output;
    }

    /**
     * Initializes a new instance of the Layer class.
     * @param neuronsCount Layer's neurons count.
     * @param inputsCount Layer's inputs count.
     */
    protected Layer(int neuronsCount, int inputsCount){
        this.inputsCount = Math.max( 1, inputsCount );
        this.neuronsCount = Math.max( 1, neuronsCount );
        neurons = new Neuron[this.neuronsCount];
    }
    
    /**
     * Compute output vector of the layer.
     * @param input Input vector.
     * @return Returns layer's output vector.
     */
    public double[] Compute(double[] input){
        // local variable to avoid mutlithread conflicts
        double[] output = new double[neuronsCount];

        // compute each neuron
        for ( int i = 0; i < neurons.length; i++ )
            output[i] = neurons[i].Compute( input );

        // assign output property as well (works correctly for single threaded usage)
        this.output = output;

        return output;
    }
    
    /**
     * Randomize neurons of the layer.
     */
    public void Randomize(){
        for (Neuron neuron : neurons) {
            neuron.Randomize();
        }
    }
}