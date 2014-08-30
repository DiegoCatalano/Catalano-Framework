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
 * Distance network.
 * @author Diego Catalano
 */
public class DistanceNetwork extends Network{
    
    /**
     * Initializes a new instance of the DistanceNetwork class.
     * @param inputsCount Network's inputs count.
     * @param neuronsCount Network's neurons count.
     */
    public DistanceNetwork(int inputsCount, int neuronsCount) {
        super(inputsCount, 1);
        layers[0] = new DistanceLayer( neuronsCount, inputsCount );
    }
    
    /**
     * Get winner neuron.
     * @return Index of the winner neuron.
     */
    public int GetWinner(){
        // find the MIN value
        double min = output[0];
        int    minIndex = 0;

        for ( int i = 1; i < output.length; i++ )
        {
            if ( output[i] < min )
            {
                // found new MIN value
                min = output[i];
                minIndex = i;
            }
        }

        return minIndex;
    }
}