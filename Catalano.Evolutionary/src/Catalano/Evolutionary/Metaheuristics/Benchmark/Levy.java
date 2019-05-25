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
public class Levy implements IObjectiveFunction{

    public Levy() {}

    @Override
    public double Compute(double[] values) {
        
        double[] wi = new double[values.length];
        for (int i = 0; i < wi.length; i++) {
            wi[i] = 1 + (values[i] - 1) / 4;
        }
        
        double t1 = Math.pow(Math.sin(Math.PI*wi[0]),2);
        
        double t3 = Math.pow(wi[values.length-1] - 1,2) * (1 + Math.pow(Math.sin(2*Math.PI*wi[values.length-1]),2));
        
        double sum = 0;
        for (int i = 0; i < values.length-1; i++) {
            //(wi-1)^2 * (1+10*(sin(pi*wi+1))^2);
            sum += Math.pow(wi[i]-1, 2) * (1 + 10 * Math.pow(Math.sin(Math.PI*wi[i] + 1), 2));
        }
        
        return t1 + sum + t3;
        
    }
    
}
