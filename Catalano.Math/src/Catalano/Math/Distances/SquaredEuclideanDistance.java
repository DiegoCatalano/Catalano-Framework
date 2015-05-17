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
public class SquaredEuclideanDistance implements IDistance{

    public SquaredEuclideanDistance() {}

    @Override
    public double Compute(double[] x, double[] y) {
        double d = 0.0, u;

        for (int i = 0; i < x.length; i++)
        {
            u = x[i] - y[i];
            d += u * u;
        }

        return d;
    }
}