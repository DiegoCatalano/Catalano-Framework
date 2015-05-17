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
public class ChessboardDistance implements IDistance{

    public ChessboardDistance() {}

    @Override
    public double Compute(double[] x, double[] y) {
        double d = 0;
        for (int i = 0; i < x.length; i++) {
            d = Math.max(d, x[i] - y[i]);
        }
        
        return d;
    }
}