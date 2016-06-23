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

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Laplace's Distribution.
 *  <para>
 *    In probability theory and statistics, the Laplace distribution is a continuous
 *    probability distribution named after Pierre-Simon Laplace. It is also sometimes called
 *    the double exponential distribution.</para>
 *  
 *  <para>
 *    The difference between two independent identically distributed exponential random
 *    variables is governed by a Laplace distribution, as is a Brownian motion evaluated at an
 *    exponentially distributed random time. Increments of Laplace motion or a variance gamma 
 *    process evaluated over the time scale also have a Laplace distribution.</para>
 *  
 *  <para>
 *     The probability density function of the Laplace distribution is also reminiscent of the
 *     normal distribution; however, whereas the normal distribution is expressed in terms of 
 *     the squared difference from the mean μ, the Laplace density is expressed in terms of the 
 *     absolute difference from the mean. Consequently the Laplace distribution has fatter tails
 *     than the normal distribution.</para>
 * 
 * @author Diego Catalano
 */
public class LaplaceDistribution implements IDistribution{
    private double u;
    private double b;
    private double constant;

    public LaplaceDistribution(double location, double scale) {
            if (scale <= 0) try {
                throw new Exception("Scale must be non-negative.");
            } catch (Exception ex) {
                Logger.getLogger(LaplaceDistribution.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.u = location;
            this.b = scale;

            this.constant = 1.0 / (2.0 * b);
    }
    
    @Override
    public double Mean(){
        return u;
    }
    
    @Override
    public double Variance(){
        return 2 * b * b;
    }
    
    @Override
    public double Entropy(){
        return Math.log(2 * Math.E * b);
    }
    
    @Override
    public double DistributionFunction(double x){
        return 0.5 * (1 + Math.signum(x - u) * (1 - Math.exp(-Math.abs(x - u) / b)));
    }
    
    @Override
    public double ProbabilityDensityFunction(double x){
        return constant * Math.exp(-Math.abs(x - u) / b);
    }
    
    @Override
    public double LogProbabilityDensityFunction(double x){
        return Math.log(constant) - Math.abs(x - u) / b;
    }
}
