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
public class SoftExponentialFunction implements IActivationFunction{
    
    private double alpha;

    public SoftExponentialFunction() {
        this(0);
    }

    public SoftExponentialFunction(double alpha) {
        this.alpha = alpha;
    }

    @Override
    public double Compute(double x) {
        if(alpha == 0)
            return x;
        
        if(alpha < 0)
            return - (Math.log(1 - alpha * (x + alpha))) / alpha;
        else
            return ((Math.exp(alpha*x) - 1) / alpha) + alpha;
    }
    
}
