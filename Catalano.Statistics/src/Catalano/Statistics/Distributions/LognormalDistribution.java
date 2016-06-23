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
 * Log-Normal (Galton) distribution.
 * <para> The log-normal distribution is a probability distribution of a random variable whose logarithm is normally distributed.</para>
 * @author Diego Catalano
 */
public class LognormalDistribution implements IDistribution{
    
    // Distribution parameters
    private double location = 0; // mean of the variable's natural logarithm
    private double shape = 1;    // std. dev. of the variable's natural logarithm

    // Distribution measures
    private Double mean;
    private Double variance;
    private Double entropy;

    // Derived measures
    private double shape2; // variance of the variable's natural logarithm
    private double constant; // 1/sqrt(2*pi*shape²)

    public LognormalDistribution() {
        init(location, shape, shape * shape);
    }
    
    public LognormalDistribution(double location) {
        init(location, shape, shape * shape);
    }
    
    public LognormalDistribution(double location, double shape) {
        init(location, shape, shape * shape);
    }
    
    private void init(double mu, double dev, double var){
        this.location = mu;
        this.shape = dev;
        this.shape2 = var;

        // Compute derived values
        this.constant = 1.0 / (Constants.Sqrt2PI * dev);
    }

    public double getShape() {
        return shape;
    }

    public double getLocation() {
        return location;
    }

    @Override
    public double Mean() {
        if (mean == null)
            mean = Math.exp(location + shape2 / 2.0);
        return mean.doubleValue();
    }

    @Override
    public double Variance() {
        if (variance == null)
            variance = (Math.exp(shape2) - 1.0) * Math.exp(2 * location + shape2);
        return variance.doubleValue();
    }

    @Override
    public double Entropy() {
        if (entropy == null)
            entropy = 0.5 + 0.5 * Math.log(2.0 * Math.PI * shape2) + location;
        return entropy.doubleValue();
    }

    @Override
    public double DistributionFunction(double x) {
        double z = (Math.log(x) - location) / shape;
        return 0.5 * Special.Erfc(-z / Constants.Sqrt2);
    }

    @Override
    public double ProbabilityDensityFunction(double x) {
        double z = (Math.log(x) - location) / shape;
        return constant * Math.exp((-z * z) * 0.5) / x;
    }

    @Override
    public double LogProbabilityDensityFunction(double x) {
        double z = (Math.log(x) - location) / shape;
        return Math.log(constant) + (-z * z) * 0.5 - Math.log(x);
    }
}