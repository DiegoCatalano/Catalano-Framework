/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Catalano.Math.Distances;

import Catalano.Core.IntPoint;

/**
 *
 * @author Diego Catalano
 */
public class EuclideanDistance implements IDistance{

    public EuclideanDistance() {}

    @Override
    public double Compute(double[] x, double[] y) {
        double d = 0.0, u;

        for (int i = 0; i < x.length; i++)
        {
            u = x[i] - y[i];
            d += u * u;
        }

        return Math.sqrt(d);
    }
}