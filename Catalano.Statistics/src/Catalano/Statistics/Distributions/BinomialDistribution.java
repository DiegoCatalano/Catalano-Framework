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

import Catalano.Math.Functions.Beta;
import Catalano.Math.Special;

/**
 * Binomial probability distribution.
 * <para>
 * The binomial distribution is the discrete probability distribution of the number of
 * successes in a sequence of <c>>n</c> independent yes/no experiments, each of which 
 * yields success with probability <c>p</c>. Such a success/failure experiment is also
 * called a Bernoulli experiment or Bernoulli trial; when <c>n = 1</c>, the binomial 
 * distribution is a <see cref="BernoulliDistribution">Bernoulli distribution</see>.</para>
 * @author Diego Catalano
 */
public class BinomialDistribution implements IDiscreteDistribution{
    
    private int numberOfTrials; // number of trials
    private double probability; // success probability in each trial

    /**
     * Gets the number of trials <c>n</c> for the distribution.
     * @return Number of trials.
     */
    public int getNumberOfTrials() {
        return numberOfTrials;
    }

    /**
     * Gets the success probability <c>p</c> for the distribution.
     * @return Success probability.
     */
    public double getProbability() {
        return probability;
    }

    /**
     * Initializes a new instance of the BinomialDistribution class.
     * @param trials The number of trials <c>n</c>.
     */
    public BinomialDistribution(int trials) {
        this(trials, 0);
    }

    /**
     * Initializes a new instance of the BinomialDistribution class.
     * @param trials The number of trials <c>n</c>.
     * @param probability The success probability <c>p</c> in each trial.
     */
    public BinomialDistribution(int trials, double probability) {
        
        if (trials <= 0)
            throw new IllegalArgumentException("The number of trials should be greater than zero.");

        if (probability < 0 || probability > 1)
            throw new IllegalArgumentException("A probability must be between 0 and 1.");
        
        this.numberOfTrials = trials;
        this.probability = probability;
    }

    @Override
    public double Mean() {
        return numberOfTrials * probability;
    }

    @Override
    public double Variance() {
        return numberOfTrials * probability * (1 - probability);
    }

    @Override
    public double Entropy() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public double DistributionFunction(int k) {
        return Beta.Incomplete(numberOfTrials - k, k + 1, 1 - probability);
    }

    @Override
    public double ProbabilityMassFunction(int k) {
        if (k < 0 || k > numberOfTrials) return 0;
        return Special.Binomial(numberOfTrials, k) * Math.pow(probability, k) * Math.pow(1 - probability, numberOfTrials - k);
    }

    @Override
    public double LogProbabilityMassFunction(int k) {
        if (k < 0 || k > numberOfTrials) return Double.NEGATIVE_INFINITY;
        return Special.LogBinomial(numberOfTrials, k) + k * Math.log(probability) + (numberOfTrials - k) * Math.log(1 - probability);
    }
}