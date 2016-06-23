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

/**
 * Fisher-Snedecor Distribution.
 * @author Diego Catalano
 */
public class FisherDistribution implements IDistribution{
    
    // Distribution parameters
    private int d1;
    private int d2;

    // Derived values
    private double b;

    private Double mean;
    private Double variance;

    /**
     * Constructs new FisherDistribution.
     * @param degrees1 The first degree of freedom.
     * @param degrees2 The second degree of freedom.
     */
    public FisherDistribution(int degrees1, int degrees2) {
        if (degrees1 <= 0){
            try {
                throw new IllegalArgumentException("Degrees of freedom must be positive.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        if (degrees2 <= 0){
            try {
                throw new IllegalArgumentException("Degrees of freedom must be positive.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        this.d1 = degrees1;
        this.d2 = degrees2;

        this.b = Beta.Function(degrees1 * 0.5, degrees2 * 0.5);
    }

    public int getDegreesOfFreedom1() {
        return d1;
    }

    public int getDegreesOfFreedom2() {
        return d2;
    }

    @Override
    public double Mean() {
        if (mean == null)
            {
                if (d2 <= 2)
                {
                    mean = Double.NaN;
                }
                else
                {
                    mean = d2 / (d2 - 2.0);
                }
            }
        return mean.doubleValue();
    }

    @Override
    public double Variance() {
        if (variance == null){
            if (d2 <= 4)
            {
                variance = Double.NaN;
            }
            else
            {
                variance = (2.0 * d2 * d2 * (d1 + d2 - 2)) /
                    (d1 * (d2 - 2) * (d2 - 2) * (d2 - 4));
            }
        }

        return variance.doubleValue();
    }

    @Override
    public double Entropy() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public double DistributionFunction(double x) {
        double u = (d1 * x) / (d1 * x + d2);
        return Beta.Incomplete(d1 * 0.5, d2 * 0.5, u);
    }
    
    public double ComplementaryDistributionFunction(double x){
        double u = d1 / (d1 * x + d2);
        return Beta.Incomplete(d2 * 0.5, d1 * 0.5, u);
    }

    @Override
    public double ProbabilityDensityFunction(double x) {
        double u = Math.pow(d1 * x, d1) * Math.pow(d2, d2) /
            Math.pow(d1 * x + d2, d1 + d2);
        return Math.sqrt(u) / (x * b);
    }

    @Override
    public double LogProbabilityDensityFunction(double x) {
        double lnu = d1 * Math.log(d1 * x) + d2 * Math.log(d2) -
            (d1 + d2) * Math.log(d1 * x + d2);
        return 0.5 * lnu - Math.log(x * b);
    }
}