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
public class Sphere implements IObjectiveFunction{

    public Sphere() {}

    @Override
    public double Compute(double[] values) {
        
        double sum = 0;
        for (int i = 0; i < values.length; i++) {
            sum += values[i]*values[i];
        }
        
        return sum;
        
    }
    
}
