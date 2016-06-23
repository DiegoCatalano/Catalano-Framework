// Catalano Statistics Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
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

package Catalano.Statistics.Regression;

import Catalano.Math.Matrix;
import Catalano.Statistics.Correlations;
import Catalano.Statistics.Tools;

/**
 * Logarithmic Regression.
 * Equation: y = a*ln(x) + b
 * @author Diego Catalano
 */
public class LogarithmicRegression implements ISimpleRegression, ILinear{
    
    private double[] x;
    private double[] y;
    private double inclination;
    private double interception;

    /**
     * Initializes a new instance of the LogarithmicRegression class.
     * @param x Data.
     * @param y Data.
     */
    public LogarithmicRegression(double[] x, double[] y) {
        this.x = x;
        this.y = y;
        double[] xx = Matrix.Log(x);
        this.inclination = Tools.Inclination(xx, y);
        this.interception = Tools.Interception(xx, y);
    }
    
    @Override
    public double getInclination() {
        return inclination;
    }

    @Override
    public void setInclination(double inclination) {
        this.inclination = inclination;
    }

    @Override
    public double getInterception() {
        return interception;
    }

    @Override
    public void setInterception(double interception) {
        this.interception = interception;
    }

    @Override
    public double Regression(double x) {
        return inclination * Math.log(x) + interception;
    }

    @Override
    public double[] Regression(double[] x) {
        double[] r = new double[x.length];
        for (int i = 0; i < x.length; i++) {
            r[i] = inclination * Math.log(x[i]) + interception;
        }
        return r;
    }
    
    @Override
    public double CoefficientOfDetermination() {
        double[] r = Regression(x);
        return Math.pow(Correlations.PearsonCorrelation(r, y), 2);
    }

    @Override
    public String toString() {
        if(interception > 0)
            return "y = " + String.format("%.4f", inclination) + "ln(x) + " + interception;
        return "y = " + inclination + "ln(x) " + interception;
    }
}