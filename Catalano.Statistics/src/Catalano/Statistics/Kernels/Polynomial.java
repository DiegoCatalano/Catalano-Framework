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

package Catalano.Statistics.Kernels;

/**
 * Polynomial Kernel.
 * @author Diego Catalano
 */
public class Polynomial implements IMercerKernel<double[]>{
    
    private int degree;
    private double constant;

    /**
     * Get the degree of the polynomial kernel.
     * @return Degree.
     */
    public int getDegree() {
        return degree;
    }

    /**
     * Set the degree of the polynomial kernel.
     * @param degree Degree.
     */
    public void setDegree(int degree) {
        this.degree = Math.max(1, degree);
    }

    /**
     * Get the polynomial constant term.
     * @return Polynomial constant term.
     */
    public double getConstant() {
        return constant;
    }

    /**
     * Set the polynomial constant term.
     * @param constant Constant term.
     */
    public void setConstant(double constant) {
        this.constant = constant;
    }
    
    /**
     * Constructs a new Polynomial Kernel.
     */
    public Polynomial() {
        this(2);
    }

    /**
     * Constructs a new Polynomial Kernel.
     * @param degree Polynomial degree.
     */
    public Polynomial(int degree) {
        this(degree, 1.0);
    }
    
    /**
     * Constructs a new Polynomial Kernel.
     * @param degree Polynomial degree.
     * @param constant Polynomial constant term.
     */
    public Polynomial(int degree, double constant){
        setDegree(degree);
        this.constant = constant;
    }

    @Override
    public double Function(double[] x, double[] y) {
        double sum = constant;
        for (int i = 0; i < x.length; i++)
            sum += x[i] * y[i];

        return Math.pow(sum, degree);
    }
    
}