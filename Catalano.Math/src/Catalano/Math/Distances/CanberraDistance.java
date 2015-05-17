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
public class CanberraDistance implements IDistance{

    public CanberraDistance() {}

    @Override
    public double Compute(double[] x, double[] y) {
        double distance = 0;
        
        for (int i = 0; i < x.length; i++) {
            distance += Math.abs(x[i] - y[i]) / (Math.abs(x[i]) + Math.abs(y[i]));
        }
        
        return distance;
    }
}