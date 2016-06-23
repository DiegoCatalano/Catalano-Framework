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
import Catalano.Math.Special;

/**
 * The Inverse Gaussian distribution.
 * <para>The Inverse Gaussian distribution is a two-parameter family of continuous probability
 * distributions with support on (0,∞). As λ tends to infinity, the inverse Gaussian distribution
 * becomes more like a normal (Gaussian) distribution. The inverse Gaussian distribution has
 * several properties analogous to a Gaussian distribution. The name can be misleading: it is
 * an "inverse" only in that, while the Gaussian describes a Brownian Motion's level at a fixed
 * time, the inverse Gaussian describes the distribution of the time a Brownian Motion with positive
 * drift takes to reach a fixed positive level.</para>
 * @author Diego
 */
public class InverseGammaDistribution implements IDistribution{
    
    // Distribution parameters
    private double mean;
    private double lambda;

    /**
     * Constructs a new Inverse Gaussian distribution.
     * @param mean The mean parameter mu.
     * @param shape The shape parameter lambda.
     */
    public InverseGammaDistribution(double mean, double shape) {
        
        if ((mean <= 0) || (shape <= 0)){
            try {
                throw new IllegalArgumentException("Mean or shape must be greater than 0");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        this.mean = mean;
        this.lambda = shape;
    }

    public double getShape() {
        return lambda;
    }

    @Override
    public double Variance() {
        return (mean * mean * mean) / lambda;
    }

    @Override
    public double DistributionFunction(double x) {
        double sqrt = Math.sqrt(lambda / x);

        double a = 0.5 * Special.Erfc(sqrt * (mean - x) / (Constants.Sqrt2 * mean));
        double b = 0.5 * Special.Erfc(sqrt * (mean + x) / (Constants.Sqrt2 * mean));
        double c = Math.exp((2.0 * lambda) / mean);

        return a + b * c;
    }

    @Override
    public double ProbabilityDensityFunction(double x) {
        double a = Math.sqrt(lambda / (2.0 * Math.PI * x * x * x));
        double b = -lambda * ((x - mean) * (x - mean)) / (2.0 * mean * mean * x);

        return a * Math.exp(b);
    }

    @Override
    public double LogProbabilityDensityFunction(double x) {
        double a = Math.sqrt(lambda / (2.0 * Math.PI * x * x * x));
        double b = -lambda * ((x - mean) * (x - mean)) / (2.0 * mean * mean * x);

        return Math.log(a) + b;
    }

    @Override
    public double Mean() {
        return mean;
    }

    @Override
    public double Entropy() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}