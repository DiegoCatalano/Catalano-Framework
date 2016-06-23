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

/**
 * Chi-Square Kernel.
 * The Chi-Square kernel comes from the Chi-Square distribution.
 * @author Diego Catalano
 */
public class ChiSquare implements IMercerKernel<double[]> {

    /**
     * Constructs a new Chi-Square kernel.
     */
    public ChiSquare() {}

    @Override
    public double Function(double[] x, double[] y) {
        double sum = 0.0;
        for (int i = 0; i < x.length; i++)
        {
            double num = x[i] - y[i];
            double den = 0.5 * (x[i] + y[i]);

            if (den != 0)
                sum += (num * num) / den;
        }

        return 1.0 - sum;
    }
    
}