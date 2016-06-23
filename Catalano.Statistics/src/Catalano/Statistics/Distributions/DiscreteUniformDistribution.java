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
 * Discrete uniform distribution.
 * @author Diego Catalano
 */
public class DiscreteUniformDistribution implements IDiscreteDistribution{
    
    // Distribution parameters
    private int a;
    private int b;

    // Derived measures
    private int n;
    
    /**
     * Gets the minimum value of the distribution (a).
     * @return Minimum value.
     */
    public double getMinimum(){
        return a;
    }
    
    /**
     * Gets the maximum value of the distribution (b).
     * @return Maximum value.
     */
    public double getMaximum(){
        return a;
    }
    
    /**
     * Gets the length of the distribution (b - a + 1).
     * @return Length.
     */
    public double getLength(){
        return n;
    }

    /**
     * Initializes a new instance of the DiscreteUniformDistribution class.
     * @param a The starting (minimum) value a.
     * @param b The ending (maximum) value b.
     */
    public DiscreteUniformDistribution(int a, int b) {
        if (a > b)
            throw new IllegalArgumentException("The starting number a must be lower than b.");

        this.a = a;
        this.b = b;
        this.n = b - a + 1;
    }
    
    @Override
    public double Mean() {
        return (a + b) / 2.0;
    }

    @Override
    public double Variance() {
        return ((b - a) * (b - a)) / 12.0;
    }

    @Override
    public double Entropy() {
        return Math.log(b - a);
    }

    @Override
    public double DistributionFunction(int k) {
        if (k < a)
            return 0;
        if (k >= b)
            return 1;
        return (k - a + 1.0) / n;
    }

    @Override
    public double ProbabilityMassFunction(int k) {
        if (k >= a && k <= b)
            return 1.0 / n;
        else return 0;
    }

    @Override
    public double LogProbabilityMassFunction(int k) {
        if (k >= a && k <= b)
            return -Math.log(n);
        else return Double.NEGATIVE_INFINITY;
    }
}