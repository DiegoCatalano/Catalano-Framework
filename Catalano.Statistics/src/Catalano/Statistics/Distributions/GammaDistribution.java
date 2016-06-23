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
 * Gamma distribution.
 * @author Diego Catalano
 */
public class GammaDistribution implements IDistribution{
    
    // Distribution parameters
    private double scale;
    private double shape;

    // Derived measures
    private double constant;
    private double lnconstant;

    /**
     * Creates new Gamma Distribution.
     * @param scale The scale parameter theta.
     * @param shape The shape parameter k.
     */
    public GammaDistribution(double scale, double shape) {
        this.scale = scale;
        this.shape = shape;

        this.constant = 1.0 / (Math.pow(scale, shape) * Gamma.Function(shape));
        this.lnconstant = -(shape * Math.log(scale) + Gamma.Log(shape));
    }

    public double getScale() {
        return scale;
    }

    public double getShape() {
        return shape;
    }

    @Override
    public double Mean() {
        return shape * scale;
    }

    @Override
    public double Variance() {
        return shape * scale * scale;
    }
    
    public double Median(){
        return Double.NaN;
    }

    @Override
    public double Entropy() {
        return shape + Math.log(scale) + Gamma.Log(shape) + (1 - shape) * Gamma.Digamma(shape);
    }

    @Override
    public double DistributionFunction(double x) {
        return Gamma.LowerIncomplete(shape, x / scale);
    }

    @Override
    public double ProbabilityDensityFunction(double x) {
        return constant * Math.pow(x, shape - 1) * Math.exp(-x / scale);
    }

    @Override
    public double LogProbabilityDensityFunction(double x) {
        return lnconstant + (shape - 1) * Math.log(x) - x / scale;
    }
}