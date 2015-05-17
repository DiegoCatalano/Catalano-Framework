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
public class HammingDistance implements IDistance{

    public HammingDistance() {}

    @Override
    public double Compute(double[] x, double[] y) {
        double distance = 0;
        
        for (int i = 0; i < x.length; i++) {
            if (x[i] != y[i]) distance++;
        }
        
        return distance;
    }
}