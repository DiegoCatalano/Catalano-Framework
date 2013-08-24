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
 * Activation network.
 * @author Diego Catalano
 */
public class ActivationNetwork extends Network{

    /**
     * Initializes a new instance of the ActivationNetwork class.
     * @param function Activation function of neurons of the network.
     * @param inputsCount Network's inputs count.
     * @param neuronsCount Array, which specifies the amount of neurons in each layer of the neural network.
     */
    public ActivationNetwork(IActivationFunction function, int inputsCount, int[]... neuronsCount) {
        super(inputsCount, neuronsCount.length);
        
        for ( int i = 0; i < layers.length; i++ ){
            layers[i] = new ActivationLayer(
                // neurons count in the layer
                neuronsCount[0][0],
                // inputs count of the layer
                ( i == 0 ) ? inputsCount : neuronsCount[0][i - 1],
                // activation function of the layer
                function );
        }
    }
}