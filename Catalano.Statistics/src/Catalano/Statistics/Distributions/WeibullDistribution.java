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

package Catalano.Statistics.Distributions;

import Catalano.Math.Constants;
import Catalano.Math.Functions.Gamma;

/**
 * Weibull distribution.
 * @author Diego Catalano
 */
public class WeibullDistribution implements IDistribution{
    
    // Distribution parameters
    private double a;
    private double b;

    /**
     * Initializes a new instance of the WeibullDistribution class.
     * @param shape The shape parameter k.
     * @param scale The scale parameter lambda.
     */
    public WeibullDistribution(double shape, double scale) {
        this.a = shape;
        this.b = scale;
    }

    @Override
    public double Mean() {
        return b * Gamma.Function(1 + 1 / a);
    }

    @Override
    public double Variance() {
        return b * b * Gamma.Function(1 + 2 / a) - Mean() * Mean();
    }

    @Override
    public double Entropy() {
        return Constants.EulerGamma * (1 - 1 / a) + Math.log(b / a) + 1;
    }

    @Override
    public double DistributionFunction(double x) {
        if (x > 0)
            return 1.0 - Math.exp(-Math.pow(x / b, a));
        if (x == 0)
            return Double.POSITIVE_INFINITY;
        else return 0;
    }

    @Override
    public double ProbabilityDensityFunction(double x) {
        if (x > 0)
            return (a / b) * Math.pow(x / b, a - 1) * Math.exp(-Math.pow(x / b, a));
        else return 0;
    }

    @Override
    public double LogProbabilityDensityFunction(double x) {
        if (x >= 0)
            return Math.log(a / b) + (a - 1) * Math.log(x / b) - Math.pow(x / b, a);
        else return Double.NEGATIVE_INFINITY;
    }
}