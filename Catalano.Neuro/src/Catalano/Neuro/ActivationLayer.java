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
 * Activation layer.
 * @author Diego Catalano
 */
public class ActivationLayer extends Layer{

    /**
     * Initializes a new instance of the ActivationLayer class.
     * @param neuronsCount Layer's neurons count.
     * @param inputsCount Layer's inputs count.
     * @param function Activation function of neurons of the layer.
     */
    public ActivationLayer(int neuronsCount, int inputsCount, IActivationFunction function) {
        super(neuronsCount, inputsCount);
        for ( int i = 0; i < neurons.length; i++ )
            neurons[i] = new ActivationNeuron(inputsCount, function);
    }
    
    /**
     * Set new activation function for all neurons of the layer.
     * @param function Activation function to set.
     */
    public void setActivationFunction(IActivationFunction function){
        for ( int i = 0; i < neurons.length; i++ )
        {
            ( (ActivationNeuron) neurons[i] ).setActivationFunction(function);
        }
    }
}