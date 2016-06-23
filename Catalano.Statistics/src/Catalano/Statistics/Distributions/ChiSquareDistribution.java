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

import Catalano.Math.Functions.Gamma;

/**
 * Chi-Square (χ²) probability distribution.
 * <para>In probability theory and statistics, the chi-square distribution (also chi-squared
 * or χ²-distribution) with k degrees of freedom is the distribution of a sum of the 
 * squares of k independent standard normal random variables. It is one of the most 
 * widely used probability distributions in inferential statistics, e.g. in hypothesis 
 * testing, or in construction of confidence intervals.</para>
 * @author Diego Catalano
 */
public class ChiSquareDistribution implements IDistribution{
    
    private int degreesOfFreedom;
    
    private double entropy;

    public ChiSquareDistribution(int degreesOfFreedom) {
        this.degreesOfFreedom = degreesOfFreedom;
    }

    public int getDegreesOfFreedom() {
        return degreesOfFreedom;
    }

    @Override
    public double Mean() {
        return degreesOfFreedom;
    }

    @Override
    public double Variance() {
        return 2.0 * degreesOfFreedom;
    }

    @Override
    public double Entropy() {
        double kd2 = degreesOfFreedom / 2.0;
        double m1 = Math.log(2.0 * Gamma.Function(kd2));
        double m2 = (1.0 - kd2) * Gamma.Digamma(kd2);
        entropy = kd2 + m1 + m2;

        return entropy;
    }
    
    public double ComplementaryDistributionFunction(double x){
            return Gamma.ComplementedIncomplete(degreesOfFreedom / 2.0, x / 2.0);
    }

    @Override
    public double DistributionFunction(double x) {
        return Gamma.Incomplete(degreesOfFreedom / 2.0, x / 2.0);
    }

    @Override
    public double ProbabilityDensityFunction(double x) {
        double v = degreesOfFreedom;
        double m1 = Math.pow(x, (v - 2.0) / 2.0);
        double m2 = Math.exp(-x / 2.0);
        double m3 = Math.pow(2, v / 2.0) * Gamma.Function(v / 2.0);
        return (m1 * m2) / m3;
    }

    @Override
    public double LogProbabilityDensityFunction(double x) {
        double v = degreesOfFreedom;
        double m1 = ((v - 2.0) / 2.0) * Math.log(x);
        double m2 = (-x / 2.0);
        double m3 = (v / 2.0) * Math.log(2) + Gamma.Log(v / 2.0);
        return (m1 + m2) - m3;
    }
}