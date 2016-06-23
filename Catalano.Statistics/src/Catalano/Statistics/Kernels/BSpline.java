// Catalano Statistics Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
// Copyright © César Souza, 2009-2013
// cesarsouza at gmail.com
//
//    This library is free software; you can redistribute it and/or
//    modify it under the terms of the GNU Lesser General Public
//    License as published by the Free Software Foundation; either
//    version 2.1 of the License, or (at your option) any later version.
//
//    This library is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
//    Lesser General Public License for more details.
//
//    You should have received a copy of the GNU Lesser General Public
//    License along with this library; if not, write to the Free Software
//    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
//

package Catalano.Statistics.Kernels;

import Catalano.Math.Special;

/**
 * B-Spline Kernel.
 * <para>The B-Spline kernel is defined only in the interval [−1, 1]. It is 
 * also a member of the Radial Basis Functions family of kernels.</para>
 * @author Diego Catalano
 */
public class BSpline implements IMercerKernel<double[]>{
    
    private int order;

    /**
     * Get B-Spline order.
     * @return Order.
     */
    public int getOrder() {
        return order;
    }

    /**
     * Set B-Spline order.
     * @param order Order.
     */
    public void setOrder(int order) {
        this.order = order;
    }

    /**
     * Constructs a new B-Spline Kernel.
     * @param order Order.
     */
    public BSpline(int order) {
        this.order = order;
    }

    @Override
    public double Function(double[] x, double[] y) {
        double k = 1.0;
        int n = 2 * order + 1;

        for (int p = 0; p < x.length; p++)
            k *= Special.BSpline(n, x[p] - y[p]);

        return k;
    }
}