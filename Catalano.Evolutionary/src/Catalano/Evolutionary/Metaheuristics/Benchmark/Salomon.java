/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Catalano.Evolutionary.Metaheuristics.Benchmark;

import Catalano.Evolutionary.Metaheuristics.Monoobjective.IObjectiveFunction;

/**
 *
 * @author Diego
 */
public class Salomon implements IObjectiveFunction{

    public Salomon() {}

    @Override
    public double Compute(double[] values) {
        
        double sum = 0;
        for (int i = 0; i < values.length; i++) {
            sum += values[i] * values[i];
        }
        sum = Math.sqrt(sum);
        
        return 1 - Math.cos(2*Math.PI*sum) + (0.1 * sum);
        
    }
    
}
