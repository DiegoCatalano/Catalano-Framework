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
 * Power Regression.
 * Equation: y = b*x^a
 * @author Diego Catalano
 */
public class PowerRegression implements ISimpleRegression, ILinear{
    
    private double[] x;
    private double[] y;
    private double inclination;
    private double interception;

    /**
     * Initializes a new instance of the PowerRegression class.
     * @param x Data.
     * @param y Data.
     */
    public PowerRegression(double[] x, double[] y) {
        this.x = x;
        this.y = y;
        double[] xx = Matrix.Log(x);
        double[] yy = Matrix.Log(y);
        this.inclination = Tools.Inclination(xx, yy);
        this.interception = Math.exp(Tools.Interception(xx, yy));
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
        return interception * Math.pow(x, inclination);
    }

    @Override
    public double[] Regression(double[] x) {
        double[] result = new double[x.length];
        
        for (int i = 0; i < x.length; i++) {
            result[i] = interception * Math.pow(x[i], inclination);
        }
        return result;
    }
    
    @Override
    public double CoefficientOfDetermination() {
        double[] r = Regression(x);
        return Math.pow(Correlations.PearsonCorrelation(r, y), 2);
    }

    @Override
    public String toString() {
        return "y = " + String.format("%.4f", interception) + "x^" + String.format("%.4f", inclination);
    }
}