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
import Catalano.Statistics.Tools;

/**
 * Empirical distribution.
 * @author Diego Catalano
 */
public class EmpiricalDistribution implements IDistribution{
    
    // Distribution parameters
    private double[] samples;
    private Double smoothing;

    // Derived measures
    private Double mean;
    private Double variance;
    private Double entropy;

    /**
     * Creates new EmpiricalDistribution.
     * @param samples Samples.
     */
    public EmpiricalDistribution(double[] samples) {
        initialize(samples, null);
    }
    
    /**
     * Creates new EmpiricalDistribution.
     * @param samples Samples.
     * @param smoothing Smoothing.
     */
    public EmpiricalDistribution(double[] samples, double smoothing){
        initialize(samples, smoothing);
    }
    
    private void initialize(double[] observations, Double smoothing){
        if (smoothing == null)
        {
            // Practical estimation of the bandwidth as suggested in Wikipedia
            //  - http://en.wikipedia.org/wiki/Kernel_density_estimation

            double sigma = Tools.StandartDeviation(observations);
            smoothing = sigma * Math.pow(4.0 / (3.0 * observations.length), -1 / 5.0);
        }

        this.samples = observations;
        this.smoothing = smoothing.doubleValue();

        this.mean = null;
        this.variance = null;
    }

    public double[] getSamples() {
        return samples;
    }

    public double getSmoothing() {
        return smoothing;
    }

    @Override
    public double Mean() {
        if (mean == null)
            mean = Tools.Mean(samples);
        return mean.doubleValue();
    }

    @Override
    public double Variance() {
        if (variance == null)
            variance = Tools.Variance(samples);
        return variance.doubleValue();
    }

    @Override
    public double Entropy() {
        if (entropy == null)
        {
            entropy = 0D;
            for (int i = 0; i < samples.length; i++)
            {
                double p = ProbabilityDensityFunction(samples[i]);

                entropy += p * Math.log(p);
            }
        }
        return entropy.doubleValue();
    }

    @Override
    public double DistributionFunction(double x) {
        int sum = 0;
        for (int i = 0; i < samples.length; i++)
            if (samples[i] <= x) sum++;
        return sum / (double)samples.length;
    }

    @Override
    public double ProbabilityDensityFunction(double x) {
        double p = 0;

        for (int i = 0; i < samples.length; i++)
        {
            double z = (x - samples[i]) / smoothing;
            p += Math.exp(-z * z * 0.5);
        }

        p *= 1.0 / (Constants.Sqrt2PI * smoothing);

        return p / samples.length;
    }

    @Override
    public double LogProbabilityDensityFunction(double x) {
        double p = 0;

        for (int i = 0; i < samples.length; i++)
        {
            double z = (x - samples[i]) / smoothing;
            p += Math.exp(-z * z * 0.5);
        }

        double logp = Math.log(p) - Math.log(Constants.Sqrt2PI * smoothing);

        return logp - Math.log(samples.length);
    }
}