/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Catalano.Statistics.Distributions;

/**
 *
 * @author Diego Catalano
 */
public interface IDistribution {
    /**
    * Gets the mean for this distribution.
    * @return The distribution's mean value.
    */
    double Mean();
    /**
     * Gets the variance for this distribution.
     * @return The distribution's variance.
     */
    double Variance();
    /**
     * Gets the entropy for this distribution.
     * @return The distribution's entropy.
     */
    double Entropy();
    /**
     * Gets the cumulative distribution function (cdf) for this distribution evaluated at point <c>x</c>.
     * <br /> The Cumulative Distribution Function (CDF) describes the cumulative probability that a given value or any value smaller than it will occur.
     * @param x A single point in the distribution range.
     * @return The probability of <c>x</c> occurring in the current distribution.
     */
    double DistributionFunction(double x);
    /**
     * Gets the probability density function (pdf) for this distribution evaluated at point <c>x</c>.
     * <br /> The Probability Density Function (PDF) describes the probability that a given value <c>x</c> will occur.
     * @param x A single point in the distribution range.
     * @return The probability of <c>x</c> occurring in the current distribution.
     */
    double ProbabilityDensityFunction(double x);
    /**
     * Gets the log-probability density function (pdf) for this distribution evaluated at point <c>x</c>.
     * <br /> The Probability Density Function (PDF) describes the probability that a given value <c>x</c> will occur.
     * @param x A single point in the distribution range.
     * @return The probability of <c>x</c> occurring in the current distribution.
     */
    double LogProbabilityDensityFunction(double x);
}
