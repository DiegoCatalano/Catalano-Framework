/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Catalano.MachineLearning.Classification.ELM.Functions;

/**
 *
 * @author Diego
 */
public class SoftSignFunction implements IActivationFunction{

    public SoftSignFunction() {}

    @Override
    public double Compute(double x) {
        return x / (1 + Math.abs(x));
    }
    
}
