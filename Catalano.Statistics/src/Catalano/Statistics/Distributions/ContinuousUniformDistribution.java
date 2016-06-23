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
 * Continuous Uniform Distribution.
 * @author Diego Catalano
 */
public class ContinuousUniformDistribution implements IDistribution{
    
    private double a;
    private double b;

    /**
     * Creates new ContinuousUniformDistribution.
     */
    public ContinuousUniformDistribution() {
        this(0, 1);
    }

    /**
     * Creates new ContinuousUniformDistribution.
     * @param a Minimum.
     * @param b Maximum.
     */
    public ContinuousUniformDistribution(double a, double b) {
        
        if (a > b){
            try {
                throw new IllegalArgumentException("The starting number a must be lower than b.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        this.a = a;
        this.b = b;
    }
    
    public double Length(){
        return b - a;
    }

    @Override
    public double Mean() {
        return (a + b) / 2;
    }
    
    public double Minimum(){
        return a;
    }
    
    public double Maximum(){
        return b;
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
    public double DistributionFunction(double x) {
        if (x < a)
            return 0;
        if (x >= b)
            return 1;
        return (x - a) / (b - a);
    }

    @Override
    public double ProbabilityDensityFunction(double x) {
        if (x >= a && x <= b)
            return 1.0 / (b - a);
        else return 0;
    }

    @Override
    public double LogProbabilityDensityFunction(double x) {
        if (x >= a && x <= b)
            return -Math.log(b - a);
        else return Double.NEGATIVE_INFINITY;
    }
}