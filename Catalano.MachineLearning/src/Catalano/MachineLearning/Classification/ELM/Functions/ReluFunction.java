/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Catalano.MachineLearning.Classification.ELM.Functions;

import java.util.Random;

/**
 *
 * @author Diego
 */
public class ReluFunction implements IActivationFunction{
    
    private double alpha = 1;
    Random r = new Random();

    public ReluFunction() {
        this(1);
    }
    
    public ReluFunction(double alpha){
        this.alpha = alpha;
    }

    @Override
    public double Compute(double x) {
        if(x < 0)
            return alpha*x;
        return x;
    }
    
}