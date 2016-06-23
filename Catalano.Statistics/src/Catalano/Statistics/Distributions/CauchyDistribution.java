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
 * Cauchy-Lorentz distribution.
 * @author Diego Catalano
 */
public class CauchyDistribution implements IDistribution{
    
    // Distribution parameters
    private double location;
    private double scale;

    // Derived measures
    private double lnconstant;
    private double constant;

    private boolean immutable;

    public CauchyDistribution() {
        this(0, 1);
    }
    
    public CauchyDistribution(double location, double scale){
        if(scale < 0){
            try {
                throw new IllegalArgumentException("Scale must be greater than zero.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Init(location, scale);
    }
    
    private void Init(double location, double scale){
        this.location = location;
        this.scale = scale;

        this.constant = 1.0 / (Math.PI * scale);
        this.lnconstant = -Math.log(Math.PI * scale);
    }

    public double getScale() {
        return scale;
    }

    public double getLocation() {
        return location;
    }

    @Override
    public double Mean() {
        return Double.NaN;
    }
    
    public double Median(){
        return location;
    }
    
    public double Mode(){
        return location;
    }

    @Override
    public double Variance() {
        return Double.NaN;
    }

    @Override
    public double Entropy() {
        return Math.log(scale) + Math.log(4.0 * Math.PI);
    }

    @Override
    public double DistributionFunction(double x) {
        return 1.0 / Math.PI * Math.atan2(x - location, scale) + 0.5;
    }

    @Override
    public double ProbabilityDensityFunction(double x) {
        double z = (x - location) / scale;
        return constant / (1.0 + z * z);
    }

    @Override
    public double LogProbabilityDensityFunction(double x) {
        double z = (x - location) / scale;
        return lnconstant - Math.log(1.0 + z * z);
    }
}