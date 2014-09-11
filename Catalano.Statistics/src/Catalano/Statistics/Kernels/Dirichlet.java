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
public class Dirichlet implements IKernel{
    
    private int dimension;

    public int getDimension() {
        return dimension;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    public Dirichlet(int dimension) {
        this.dimension = dimension;
    }

    @Override
    public double Function(double[] x, double[] y) {
        // Optimization in case x and y are
        // exactly the same object reference.

        double prod = 1;
        for (int i = 0; i < x.length; i++)
        {
            double delta = x[i] - y[i];
            double num = Math.sin((dimension + 0.5) * (delta));
            double den = 2.0 * Math.sin(delta / 2.0);
            prod *= num / den;
        }

        return prod;
    }
}