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
public class Hypersecant implements IKernel{
    
    private double gamma;

    public double getGamma() {
        return gamma;
    }

    public void setGamma(double gamma) {
        this.gamma = gamma;
    }

    public Hypersecant(double gamma) {
        this.gamma = gamma;
    }

    @Override
    public double Function(double[] x, double[] y) {
        double norm = 0.0, d;
        for (int i = 0; i < x.length; i++)
        {
            d = x[i] - y[i];
            norm += d * d;
        }

        norm = Math.sqrt(norm);

        return 2.0 / Math.exp(gamma * norm) + Math.exp(-gamma * norm);
    }
}