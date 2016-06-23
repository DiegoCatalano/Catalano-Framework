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

/**
 * Bernoulli probability distribution.
 * <para>
 * The Bernoulli distribution is a distribution for a single
 * binary variable x E {0,1}, representing, for example, the
 * flipping of a coin. It is governed by a single continuous
 * parameter representing the probability of an observation
 * to be equal to 1.</para>
 * @author Diego Catalano
 */
public class BernoulliDistribution implements IDiscreteDistribution{
    
    // Distribution parameters
    private double probability;

    // Derived parameter values
    private double complement;

    // Distribution measures
    private Double entropy;

    /**
     * Initializes a new instance of the BernoulliDistribution class.
     * @param mean The probability of an observation being equal to 1.
     */
    public BernoulliDistribution(double mean) {
        this.probability = mean;
        this.complement = 1.0 - mean;

        this.entropy = null;
    }
        
    @Override
    public double Mean(){
        return probability;
    }
    
    @Override
    public double Variance(){
        return probability * complement;
    }
    
    @Override
    public double Entropy(){
        if (entropy == null){
            entropy = -probability * Math.log(probability) -
                        complement * Math.log(complement);
        }

        return entropy.doubleValue();
    }
    
    @Override
    public double DistributionFunction(int k){
        if (k < 0) return 0;
        if (k >= 1) return 1;
        return complement;
    }
    
    @Override
    public double ProbabilityMassFunction(int k){
        if (k == 1) return probability;
        if (k == 0) return complement;
        return 0;
    }
    
    @Override
    public double LogProbabilityMassFunction(int k){
        if (k == 1) return Math.log(probability);
        if (k == 0) return Math.log(complement);
        return Double.NEGATIVE_INFINITY;
    }
}