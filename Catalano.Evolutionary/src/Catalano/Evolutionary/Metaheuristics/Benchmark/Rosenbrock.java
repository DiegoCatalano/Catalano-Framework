/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Catalano.Evolutionary.Metaheuristics.Benchmark;

import Catalano.Evolutionary.Metaheuristics.IObjectiveFunction;

/**
 * 
 * @author Diego
 */
public class Rosenbrock implements IObjectiveFunction{

    public Rosenbrock() {}

    @Override
    public double Compute(double[] values) {
        
        double sum = 0;
        for (int i = 0; i < values.length-1; i++) {
            sum += 100 * Math.pow((values[i+1] - values[i]*values[i]),2) + Math.pow((values[i] - 1),2);
        }
        
        return sum;
        
    }
    
}
