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
 * Exponential distribution.
 * @author Diego Catalano
 */
public class ExponentialDistribution implements IDistribution{
    
    private double lambda;
    
    private double lnlambda;

    /**
     * Creates a new Exponential distribution
     * @param rate Rate.
     */
    public ExponentialDistribution(double rate) {
        this.lambda = rate;
        this.lnlambda = Math.log(rate);
    }

    @Override
    public double Mean() {
        return 1 / lambda;
    }

    @Override
    public double Variance() {
        return 1 / (lambda * lambda);
    }
    
    public double Median(){
        return Math.log(2) / lambda;
    }
    
    public double Mode(){
        return 0;
    }

    @Override
    public double Entropy() {
        return 1 - Math.log(lambda);
    }

    @Override
    public double DistributionFunction(double x) {
        return 1.0 - Math.exp(-lambda * x);
    }

    @Override
    public double ProbabilityDensityFunction(double x) {
        return lambda * Math.exp(-lambda * x);
    }

    @Override
    public double LogProbabilityDensityFunction(double x) {
        return lnlambda - lambda * x;
    }
}