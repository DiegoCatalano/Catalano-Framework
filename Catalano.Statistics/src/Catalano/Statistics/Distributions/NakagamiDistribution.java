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
 * Nakagami distribution.
 * <para> The Nakagami distribution has been used in the modelling of wireless signal attenuation while traversing multiple paths. </para>
 * @author Diego Catalano
 */
public class NakagamiDistribution implements IDistribution{
    
    // distribution parameters
    private double mu;
    private double omega;

    // derived values
    private Double mean;
    private Double variance;

    private double constant;
    private double nratio;
    private double twoMu1;

    /**
     *  Initializes a new instance of the NakagamiDistribution class.
     * @param shape The shape parameter μ.
     * @param spread The spread parameter ω.
     */
    public NakagamiDistribution(double shape, double spread) {
        if (shape < 0.5){
            try {
                throw new IllegalArgumentException("Shape parameter (mu) should be greater than or equal to 0.5.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        if (spread <= 0){
            try {
                throw new IllegalArgumentException("Spread parameter (omega) should be greater than 0.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        this.mu = shape;
        this.omega = spread;

        init(shape, spread);
    }
    
    private void init(double shape, double spread){
        double twoMuMu = 2.0 * Math.pow(shape, shape);
        double gammaMu = Gamma.Function(shape);
        double spreadMu = Math.pow(spread, shape);
        nratio = -shape / spread;
        twoMu1 = 2.0 * shape - 1.0;

        constant = twoMuMu / (gammaMu * spreadMu);

        mean = null;
        variance = null;
    }
    
    public double getShape(){
        return mu;
    }
    
    public double getSpread(){
        return omega;
    }

    @Override
    public double Mean() {
        if (mean == null)
            mean = (Gamma.Function(mu + 0.5) / Gamma.Function(mu)) * Math.sqrt(omega / mu);
        return mean.doubleValue();
    }

    @Override
    public double Variance() {
        if (variance == null)
        {
            double a = Gamma.Function(mu + 0.5) / Gamma.Function(mu);
            variance = omega * (1.0 - (1.0 / mu) * (a * a));
        }
        return variance.doubleValue();
    }

    @Override
    public double Entropy() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public double DistributionFunction(double x) {
        return Gamma.LowerIncomplete(mu, (mu / omega) * (x * x));
    }

    @Override
    public double ProbabilityDensityFunction(double x) {
        return constant * Math.pow(x, twoMu1) * Math.exp(nratio * x * x);
    }

    @Override
    public double LogProbabilityDensityFunction(double x) {
        return Math.log(constant) + twoMu1 * Math.log(x) + nratio * x * x;
    }
}