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
public class ManhattanDistance implements IDistance{

    public ManhattanDistance() {}

    @Override
    public double Compute(double[] x, double[] y) {
        double sum = 0;
        for (int i = 0; i < x.length; i++) {
            sum += Math.abs(x[i] - y[i]);
        }
        return sum;
    }
}