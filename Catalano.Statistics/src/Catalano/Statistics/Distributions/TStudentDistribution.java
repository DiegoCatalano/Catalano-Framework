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
import Catalano.Math.Functions.Gamma;

/**
 * Student's t-distribution.
 * @author Diego Catalano
 */
public class TStudentDistribution implements IDistribution{
    
    private double constant;
    private double degreesOfFreedom;
    
    /**
     * Gets the degrees of freedom for the distribution.
     * @return Degrees of freedom.
     */
    public double getDegreesOfFreedom() {
        return degreesOfFreedom;
    }

    /**
     * Sets the degrees of freedom for the distribution.
     * @param degreesOfFreedom Degrees of freedom.
     */
    public void setDegreesOfFreedom(double degreesOfFreedom) {
        this.degreesOfFreedom = degreesOfFreedom;
    }

    /**
     * Initializes a new instance of the TStudentDistribution class.
     * @param degreesOfFreedom Degrees of freedom.
     */
    public TStudentDistribution(double degreesOfFreedom){
        if (degreesOfFreedom < 1){
            try {
                throw new IllegalArgumentException("degreesOfFreedom");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        this.degreesOfFreedom = degreesOfFreedom;

        double v = degreesOfFreedom;

        // TODO: Use LogGamma instead.
        this.constant = Gamma.Function((v + 1) / 2.0) / (Math.sqrt(v * Math.PI) * Gamma.Function(v / 2.0));
    }

    @Override
    public double Mean() {
        return (degreesOfFreedom > 1) ? 0 : Double.NaN;
    }

    @Override
    public double Variance() {
        if (degreesOfFreedom > 2)
            return degreesOfFreedom / (degreesOfFreedom - 2);
        else if (degreesOfFreedom > 1)
            return Double.POSITIVE_INFINITY;
        return Double.NaN;
    }

    @Override
    public double Entropy() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public double DistributionFunction(double x) {
        double v = degreesOfFreedom;
        double sqrt = Math.sqrt(x * x + v);
        double u = (x + sqrt) / (2 * sqrt);
        return Beta.Incomplete(v / 2.0, v / 2.0, u);
    }

    @Override
    public double ProbabilityDensityFunction(double x) {
        double v = degreesOfFreedom;
        return constant * Math.pow(1 + (x * x) / degreesOfFreedom, -(v + 1) / 2.0);
    }

    @Override
    public double LogProbabilityDensityFunction(double x) {
        double v = degreesOfFreedom;
        return Math.log(constant) - ((v + 1) / 2.0) * Math.log(1 + (x * x) / degreesOfFreedom);
    }
}