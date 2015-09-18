/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Catalano.Statistics.Kernels;

import java.io.Serializable;

/**
 *
 * @author Diego
 */
public class Tensor implements IMercerKernel<double[]>, Serializable{
    
    private IMercerKernel<double[]>[] kernels;

    public Tensor(IMercerKernel<double[]>[] kernels) {
        this.kernels = kernels;
    }

    @Override
    public double Function(double[] x, double[] y) {
        double product = 1.0;

        for (int i = 0; i < kernels.length; i++)
        {
            product *= kernels[i].Function(x, y);
        }

        return product;
    }
}