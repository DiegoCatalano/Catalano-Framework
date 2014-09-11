/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Catalano.Statistics.Kernels;

/**
 *
 * @author Diego
 */
public class Circular implements IKernel{
    
    private final double c2dPI = 2.0 / Math.PI;
    private double sigma;

    public double getSigma() {
        return sigma;
    }

    public void setSigma(double sigma) {
        this.sigma = sigma;
    }

    public Circular(double sigma) {
        this.sigma = sigma;
    }

    @Override
    public double Function(double[] x, double[] y) {
            double norm = 0.0, a, b, c;
            for (int i = 0; i < x.length; i++)
            {
                double d = x[i] - y[i];
                norm += d * d;
            }

            norm = Math.sqrt(norm);

            if (norm >= sigma)
            {
                return 0;
            }
            else
            {
                norm = norm / sigma;
                a = c2dPI * Math.acos(-norm);
                b = c2dPI * norm;
                c = 1.0 - norm * norm;

                return a - b * Math.sqrt(c);
            }
    }
}