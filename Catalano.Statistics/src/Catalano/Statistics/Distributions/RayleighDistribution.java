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

/**
 * Rayleigh distribution.
 * <para>
 * In probability theory and statistics, the Rayleigh distribution is a continuous 
 * probability distribution. A Rayleigh distribution is often observed when the overall
 * magnitude of a vector is related to its directional components. </para>
 * 
 * <para>One example where the Rayleigh distribution naturally arises is when wind speed
 * is analyzed into its orthogonal 2-dimensional vector components. Assuming that the 
 * magnitude of each component is uncorrelated and normally distributed with equal variance,
 * then the overall wind speed (vector magnitude) will be characterized by a Rayleigh 
 * distribution.</para>
 * @author Diego Catalano
 */
public class RayleighDistribution implements IDistribution{
    
    private double sigma;

    /**
     * Initializes a new instance of the RayleighDistribution class.
     * @param sigma The Rayleight distribution's sigma.
     */
    public RayleighDistribution(double sigma) {
        this.sigma = sigma;
    }

    @Override
    public double Mean() {
        return sigma * Math.sqrt(Math.PI / 2.0);
    }

    @Override
    public double Variance() {
        return (4.0 - Math.PI) / 2.0 * sigma * sigma;
    }

    @Override
    public double Entropy() {
        return 1 + Math.log(sigma / Constants.Sqrt2) + Constants.EulerGamma / 2.0;
    }

    @Override
    public double DistributionFunction(double x) {
        return 1.0 - Math.exp(-x * x / (2 * sigma * sigma));
    }

    @Override
    public double ProbabilityDensityFunction(double x) {
        return x / (sigma * sigma) * Math.exp(-x * x / (2 * sigma * sigma));
    }

    @Override
    public double LogProbabilityDensityFunction(double x) {
        return Math.log(x / (sigma * sigma)) + (-x * x / (2 * sigma * sigma));
    }
}