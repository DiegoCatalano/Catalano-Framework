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
public class Griewank implements IObjectiveFunction{

    public Griewank() {}

    @Override
    public double Compute(double[] values) {
        
        double sum = 0;
        double sumProd = 1;
        for (int i = 0; i < values.length; i++) {
            sum += (values[i]*values[i]) / 4000;
            sumProd *= Math.cos(values[i] / Math.sqrt(i+1));
        }
        
        return sum - sumProd + 1;
        
    }
    
}
