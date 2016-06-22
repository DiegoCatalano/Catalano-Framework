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
public class BentIdentityFunction implements IActivationFunction{

    public BentIdentityFunction() {}

    @Override
    public double Compute(double x) {
        return (Math.sqrt(x*x+1) - 1) / 2 + x;
    }
    
}
