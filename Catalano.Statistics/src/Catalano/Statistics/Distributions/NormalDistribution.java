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
import Catalano.Math.Functions.Normal;
import Catalano.Math.Special;

/**
 * Normal (Gaussian) distribution.
 * @author Diego Catalano
 */
public class NormalDistribution implements IDistribution{
    
    // Distribution parameters
    private double mean = 0;  // mean
    private double stdDev = 1; // standard deviation

    // Distribution measures
    private Double entropy;

    // Derived measures
    private double variance = 1;
    private double lnconstant; // log(1/sqrt(2*pi*variance))
    
    // 97.5 percentile of standard normal distribution
    private final double p95 = 1.95996398454005423552;

    public NormalDistribution() {
        init(mean, stdDev, stdDev * stdDev);
    }
    
    public NormalDistribution(double mean){
        init(mean, stdDev, stdDev * stdDev);
    }
    
    public NormalDistribution(double mean, double stdDev)
    {
        if (stdDev <= 0){
            try {
                throw new IllegalArgumentException("Standard deviation must be positive.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        init(mean, stdDev, stdDev * stdDev);
    }

    private void init(double mu, double dev, double var){
        this.mean = mu;
        this.stdDev = dev;
        this.variance = var;

        // Compute derived values
        this.lnconstant = -Math.log(Constants.Sqrt2PI * dev);
    }

    @Override
    public double Mean() {
        return mean;
    }

    @Override
    public double Variance() {
        return variance;
    }
    
    public double StandartDeviation(){
        return stdDev;
    }

    @Override
    public double Entropy() {
        if (entropy == null){
            entropy = 0.5 * (Math.log(2.0 * Math.PI * variance) + 1);
        }

        return entropy.doubleValue();
    }

    @Override
    public double DistributionFunction(double x) {
        double z = (x - mean) / stdDev;
        return Special.Erfc(-z / Constants.Sqrt2) * 0.5;

        /*
            // For a normal distribution with zero variance, the cdf is the Heaviside
            // step function (Wipedia, http://en.wikipedia.org/wiki/Normal_distribution)
            return (x >= mean) ? 1.0 : 0.0;
        */
    }
    
    public double InverseDistributionFunction(double p){
        return mean + stdDev * Normal.Inverse(p);
    }

    @Override
    public double ProbabilityDensityFunction(double x) {
        double z = (x - mean) / stdDev;
        double lnp = lnconstant - z * z * 0.5;

        return Math.exp(lnp);

        /*
                // In the case the variance is zero, return the weak limit function 
                // of the sequence of Gaussian functions with variance towards zero.

                // In this case, the pdf can be seen as a Dirac delta function
                // (Wikipedia, http://en.wikipedia.org/wiki/Dirac_delta_function).

                return (x == mean) ? Double.PositiveInfinity : 0.0;
            */
    }

    @Override
    public double LogProbabilityDensityFunction(double x) {
        double z = (x - mean) / stdDev;
        double lnp = lnconstant - z * z * 0.5;

        return lnp;
    }
    
    /**
     * Gets the Z-Score for a given value.
     * @param x Value.
     * @return Z-Score.
     */
    public double ZScore(double x){
        return (x - mean) / stdDev;
    }
    
}