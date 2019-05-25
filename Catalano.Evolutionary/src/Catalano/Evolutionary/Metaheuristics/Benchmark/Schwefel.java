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
public class Schwefel implements IObjectiveFunction{

    public Schwefel() {}

    @Override
    public double Compute(double[] values) {
        
        double sum = 0;
        for (int i = 0; i < values.length; i++) {
            sum += values[i]*Math.sin(Math.sqrt(Math.abs(values[i])));
        }
        
        return 418.9829*values.length - sum;
        
    }
    
}
