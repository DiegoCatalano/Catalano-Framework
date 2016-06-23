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
 * Pareto's Distribution.
 * <para>
 * The Pareto distribution, named after the Italian economist Vilfredo Pareto, is a power law
 * probability distribution that coincides with social, scientific, geophysical, actuarial, 
 * and many other types of observable phenomena. Outside the field of economics it is sometimes
 * referred to as the Bradford distribution.
 * </para>
 * @author Diego Catalano
 */
public class ParetoDistribution implements IDistribution{
    
    private double xm;
    private double a;

    /**
    * Creates new Pareto distribution.
    * @param scale The scale parameter x.
    * @param shape The shape parameter alpha.
    */
    public ParetoDistribution(double scale, double shape) {
        this.xm = scale;
        this.a = shape;
    }
    
    @Override
    public double Mean(){
        return (a * xm) / (a - 1);
    }
        
    @Override
    public double Variance(){
        return (xm * xm * a) / ((a - 1) * (a - 1) * (a - 2));
    }
    
    @Override
    public double Entropy(){
        return Math.log(xm / a) + 1.0 / a + 1.0;
    }
    
    /**
     * Gets the mode for this distribution.
     * @return The distribution's mode value.
     */
    public double Mode(){
        return xm;
    }
    
    /**
     * Gets the median for this distribution.
     * @return The distribution's median value.
     */
    public double Median(){
        return xm * Math.pow(2, 1.0 / a);
    }
    
    @Override
    public double DistributionFunction(double x){
        if (x >= xm)
            return 1 - Math.pow(xm / x, a);
        return 0;
    }
    
    @Override
    public double ProbabilityDensityFunction(double x){
        if (x >= xm)
            return (a * Math.pow(xm, a)) / Math.pow(x, a + 1);
        return 0;
    }
    
    @Override
    public double LogProbabilityDensityFunction(double x){
        if (x >= xm)
            return Math.log(a) + a * Math.log(xm) - (a + 1) * Math.log(x);
        return 0;
    }
}