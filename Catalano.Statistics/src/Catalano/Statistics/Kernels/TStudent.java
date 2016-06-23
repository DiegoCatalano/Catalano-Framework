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
 * Generalized T-Student Kernel.
 * The Generalized T-Student Kernel is a Mercer Kernel and thus forms a positive semi-definite Kernel matrix (Boughorbel, 2004).
 * @author Diego Catalano
 */
public class TStudent implements IMercerKernel<double[]>{
    
    private int degree;

    /**
     * Gets the degree of this kernel.
     * @return Degree.
     */
    public int getDegree() {
        return degree;
    }

    /**
     * Set the degree of this kernel.
     * @param degree Degree.
     */
    public void setDegree(int degree) {
        this.degree = degree;
    }

    /**
     * Constructs a new TStudent kernel.
     */
    public TStudent() {}

    /**
     * Constructs a new TStudent kernel.
     * @param degree Degree.
     */
    public TStudent(int degree) {
        this.degree = degree;
    }

    @Override
    public double Function(double[] x, double[] y) {
        double norm = 0.0;
        for (int i = 0; i < x.length; i++){
            double d = x[i] - y[i];
            norm += d * d;
        }
        norm = Math.sqrt(norm);

        return 1.0 / (1.0 + Math.pow(norm, degree));
    }
}