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

import java.io.*;

/**
 * Base neural network class.
 * @author Diego Catalano
 */
public abstract class Network implements Serializable{
    
    /**
     * Network's inputs count.
     */
    public int inputsCount;
    
    /**
     * Network's layers count.
     */
    protected int layersCount;
    
    /**
     * Network's layers.
     */
    public Layer[] layers;
    
    /**
     * Network's output vector.
     */
    public double[] output;

    /**
     * Get Network's inputs count.
     * @return Network's inputs count.
     */
    public int getInputsCount() {
        return inputsCount;
    }

    /**
     * Get Network's layers count.
     * @return Network's layers count.
     */
    public Layer[] getLayers() {
        return layers;
    }

    /**
     * Get Network's output vector.
     * @return Network's output vector.
     */
    public double[] getOutput() {
        return output;
    }
    
    /**
     * Initializes a new instance of the Network class.
     * @param inputsCount Network's inputs count.
     * @param layersCount Network's layers count.
     */
    protected Network( int inputsCount, int layersCount ){
        this.inputsCount = Math.max( 1, inputsCount );
        this.layersCount = Math.max( 1, layersCount );
        
        // create collection of layers
        this.layers = new Layer[this.layersCount];
    }
    
    /**
     * Compute output vector of the network.
     * @param input Input vector.
     * @return Returns network's output vector.
     */
    public double[] Compute( double[] input ){
        // local variable to avoid mutlithread conflicts
        double[] output = input;

        // compute each layer
        for ( int i = 0; i < layers.length; i++ )
        {
            output = layers[i].Compute( output );
        }

        // assign output property as well (works correctly for single threaded usage)
        this.output = output;

        return output;
    }
    
    /**
     * Randomize layers of the network.
     */
    public void Randomize(){
        for (Layer layer : layers) {
            layer.Randomize();
        }
    }
    
    /**
     * Save network to specified file.
     * @param fileName File name to save network into.
     */
    public void Save(String fileName){
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName));
            out.writeObject(this);
            out.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    
    /**
     * Load network from specified file.
     * @param fileName File name to load network from.
     * @return Returns instance of Network class with all properties initialized from file.
     */
    public Network Load(String fileName){
        Network network = null;
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName));
            network = (Network)in.readObject();
            in.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        return network;
    }
}