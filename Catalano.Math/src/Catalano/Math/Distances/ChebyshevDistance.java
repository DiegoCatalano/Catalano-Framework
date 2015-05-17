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
public class ChebyshevDistance implements IDistance{

    public ChebyshevDistance() {}

    @Override
    public double Compute(double[] x, double[] y) {
        double max = Math.abs(x[0] - y[0]);
        
        for (int i = 1; i < x.length; i++){
            double abs = Math.abs(x[i] - y[i]);
            if (abs > max) max = abs;
        }
        
        return max;
    }
}