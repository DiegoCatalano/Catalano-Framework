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
public class ExponentialLinearFunction implements IActivationFunction{
    
    private double alpha;

    public ExponentialLinearFunction() {
        this(1);
    }

    public ExponentialLinearFunction(double alpha) {
        this.alpha = alpha;
    }

    @Override
    public double Compute(double x) {
        if(x < 0)
            return alpha * (Math.exp(x) - 1);
        return x;
    }
    
}
