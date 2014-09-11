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
public class HistogramIntersection implements IKernel{
    
    private double alpha = 1;
    private double beta = 1;

    public HistogramIntersection(double alpha, double beta) {
        this.alpha = alpha;
        this.beta = beta;
    }

    @Override
    public double Function(double[] x, double[] y) {
        double sum = 0.0;

        for (int i = 0; i < x.length; i++)
        {
            sum += Math.min(
                Math.pow(Math.abs(x[i]), alpha),
                Math.pow(Math.abs(y[i]), beta));
        }

        return sum;
    }
}