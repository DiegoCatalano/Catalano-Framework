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
public class RationalQuadratic implements IMercerKernel<double[]>{
    
    private double constant;

    public double getConstant() {
        return constant;
    }

    public void setConstant(double constant) {
        this.constant = constant;
    }

    public RationalQuadratic() {
        this(1);
    }

    public RationalQuadratic(double constant) {
        this.constant = constant;
    }

    @Override
    public double Function(double[] x, double[] y) {
        double norm = 0.0;
        for (int i = 0; i < x.length; i++)
        {
            double d = x[i] - y[i];
            norm += d * d;
        }

        return 1.0 - (norm / (norm - constant));
    }
}