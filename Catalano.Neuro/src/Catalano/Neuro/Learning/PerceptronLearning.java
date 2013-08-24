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

package Catalano.Neuro.Learning;

import Catalano.Neuro.ActivationNetwork;
import Catalano.Neuro.ActivationNeuron;
import Catalano.Neuro.Layer;

/**
 * Perceptron learning algorithm.
 * @author Diego Catalano
 */
public class PerceptronLearning implements ISupervisedLearning{
    
    // network to teach
    private ActivationNetwork network;
    // learning rate
    private double learningRate = 0.1;

    /**
     * Get Learning rate. Range[0, 1].
     * @return Learning rate.
     */
    public double getLearningRate() {
        return learningRate;
    }

    /**
     * Set Learning rate. Range[0, 1].
     * @param learningRate Learning rate.
     */
    public void setLearningRate(double learningRate) {
        this.learningRate = Math.max( 0.0, Math.min( 1.0, learningRate ) );
    }

    /**
     * Initializes a new instance of the PerceptronLearning class.
     * @param network Network to teach.
     */
    public PerceptronLearning(ActivationNetwork network) {
        // check layers count
        if ( network.layers.length != 1)
        {
            throw new IllegalArgumentException( "Invalid nuaral network. It should have one layer only." );
        }

        this.network = network;
    }

    @Override
    public double Run(double[] input, double[] output) {
        // compute output of network
        double[] networkOutput = network.Compute( input );

        // get the only layer of the network
        Layer layer = network.layers[0];

        // summary network absolute error
        double error = 0.0;
            // check output of each neuron and update weights
            for ( int j = 0; j < layer.getNeurons().length; j++ )
            {
                double e = output[j] - networkOutput[j];

                if ( e != 0 )
                {
                    ActivationNeuron perceptron = (ActivationNeuron)layer.getNeuron(j);

                    // update weights
                    for ( int i = 0; i < perceptron.getWeights().length; i++ )
                    {
                        perceptron.setWeight(i, perceptron.getWeight(i) + learningRate * e * input[i]);
                        //perceptron.Weights[i] += learningRate * e * input[i];
                    }

                    // update threshold value
                    perceptron.setThreshold(perceptron.getThreshold() + learningRate * e );
                    //perceptron.Threshold += learningRate * e;

                    // make error to be absolute
                    error += Math.abs( e );
                }
            }

            return error;
    }

    @Override
    public double RunEpoch(double[][] input, double[][] output) {
        double error = 0.0;

        // run learning procedure for all samples
        for ( int i = 0, n = input.length; i < n; i++ )
        {
            error += Run( input[i], output[i] );
        }

        // return summary error
        return error;
    }
}