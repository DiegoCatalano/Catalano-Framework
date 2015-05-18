// Catalano Statistics Library
// The Catalano Framework
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
 * Thin Plate Spline Kernel.
 * Thin plate splines (TPS) are a spline-based technique for data interpolation and smoothing.
 * 
 * @author Diego Catalano
 */
public class ThinPlateSpline implements IKernel{
    
    private double sigma = 1;

    /**
     * Get sigma.
     * @return Sigma.
     */
    public double getSigma() {
        return sigma;
    }

    /**
     * Set sigma.
     * @param sigma Sigma.
     */
    public void setSigma(double sigma) {
        this.sigma = Math.max(1, sigma);
    }

    /**
     * Constructs a new ThinPlateSpline Kernel.
     */
    public ThinPlateSpline() {}

    /**
     * Constructs a new ThinPlateSpline Kernel.
     * @param sigma Sigma.
     */
    public ThinPlateSpline(double sigma) {
        setSigma(sigma);
    }

    @Override
    public double Function(double[] x, double[] y) {
        
        double r = 0.0;
        for (int i = 0; i < x.length; i++) {
            double dxy = x[i] - y[i];
            r += dxy * dxy;
        }
            
        return r/(sigma*sigma) * Math.log(Math.sqrt(r)/sigma);
    }
}