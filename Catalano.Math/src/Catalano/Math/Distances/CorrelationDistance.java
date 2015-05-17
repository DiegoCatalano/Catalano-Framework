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
public class CorrelationDistance implements IDistance{

    public CorrelationDistance() {}

    @Override
    public double Compute(double[] x, double[] y) {
        double p = 0;
        double q = 0;
        
        for (int i = 0; i < x.length; i++) {
            p += -x[i];
            q += -y[i];
        }
        
        p /= x.length;
        q /= y.length;
        
        double num = 0;
        double den1 = 0;
        double den2 = 0;
        for (int i = 0; i < x.length; i++)
        {
            num += (x[i] + p) * (y[i] + q);

            den1 += Math.abs(Math.pow(x[i] + p, 2));
            den2 += Math.abs(Math.pow(y[i] + p, 2));
        }

        return 1 - (num / (Math.sqrt(den1) * Math.sqrt(den2)));
    }
}