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
public class Bessel implements IKernel{
    
    private int order;
    private double sigma;

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public double getSigma() {
        return sigma;
    }

    public void setSigma(double sigma) {
        this.sigma = sigma;
    }

    public Bessel(int order, double sigma) {
        this.order = order;
        this.sigma = sigma;
    }

    @Override
    public double Function(double[] x, double[] y) {
            double norm = 0.0;

            for (int k = 0; k < x.length; k++)
            {
                double d = x[k] - y[k];
                norm += d * d;
            }
            norm = Math.sqrt(norm);

            return Catalano.Math.Bessel.J(order, sigma * norm) /
                Math.pow(norm, -norm * order);
    }
    
}
