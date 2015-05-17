/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Catalano.Math.Distances;

/**
 *
 * @author Diego
 */
public class BrayCurtisDistance implements IDistance{

    public BrayCurtisDistance() {}

    @Override
    public double Compute(double[] x, double[] y) {
        double sumP, sumN;
        sumP = sumN = 0;
        
        for (int i = 0; i < x.length; i++) {
            sumN += Math.abs(x[i] - y[i]);
            sumP += Math.abs(x[i] + y[i]);
        }
        
        return sumN/sumP;
    }
}