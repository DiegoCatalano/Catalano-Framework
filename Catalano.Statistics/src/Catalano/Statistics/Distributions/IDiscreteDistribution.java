// Catalano Statistics Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
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

/**
 * Common interface for discrete distributions.
 * @author Diego Catalano
 */
public interface IDiscreteDistribution {
    /**
     * Gets the mean for this distribution.
     * @return Mean.
     */
    public double Mean();
    /**
     * Gets the variance for this distribution.
     * @return Variance.
     */
    public double Variance();
    /**
     * Gets the entropy for this distribution.
     * @return Entropy.
     */
    public double Entropy();
    /**
     * Gets the cumulative distribution function (cdf) for this distribution evaluated at point <c>k</c>.
     * <para>The Cumulative Distribution Function (CDF) describes the cumulative
     * probability that a given value or any value smaller than it will occur.</para>
     * @param k A single point in the distribution range.
     * @return Result.
     */
    public double DistributionFunction(int k);
    /**
     * Gets the probability mass function (pmf) for this distribution evaluated at point <c>x</c>.
     * <para>The Probability Mass Function (PMF) describes the
     * probability that a given value <c>k</c> will occur.</para>
     * @param k A single point in the distribution range.
     * @return The probability of <c>k</c> occurring in the current distribution.
     */
    public double ProbabilityMassFunction(int k);
    /**
     * Gets the log-probability mass function (pmf) for this distribution evaluated at point <c>x</c>.
     * <para>The Probability Mass Function (PMF) describes the
     * probability that a given value <c>k</c> will occur.</para>
     * @param k A single point in the distribution range.
     * @return The logarithm of the probability of <c>k</c> occurring in the current distribution.
     */
    public double LogProbabilityMassFunction(int k);
}
