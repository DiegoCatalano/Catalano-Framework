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
public class Wave implements IMercerKernel<double[]>{
    
    private double sigma;

    public double getSigma() {
        return sigma;
    }

    public void setSigma(double sigma) {
        this.sigma = sigma;
    }

    public Wave(double sigma) {
        this.sigma = sigma;
    }

    @Override
    public double Function(double[] x, double[] y) {
        double norm = 0.0;
        for (int i = 0; i < x.length; i++)
        {
            double d = x[i] - y[i];
            norm += d * d;
        }
        norm = Math.sqrt(norm);

        if (sigma == 0 || norm == 0)
            return 0;
        else
            return (sigma / norm) * Math.sin(norm / sigma);
    }
}