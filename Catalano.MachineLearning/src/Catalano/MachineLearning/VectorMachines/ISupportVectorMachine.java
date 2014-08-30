/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Catalano.MachineLearning.VectorMachines;

/**
 * Common interface for Support Vector Machines
 * @author Diego Catalano
 */
public interface ISupportVectorMachine {
    
    /**
     * Computes the given input to produce the corresponding output.
     * @param inputs An input vector.
     * @return The decision label for the given input.
     */
    int Compute(double[] inputs);
}
