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
   
public class Gaussian implements IKernel{
    private double sigma;
    private double gamma;

    /**
        * Constructs a new Gaussian Kernel.
        */
    public Gaussian(){
        sigma = 1;
    }

    /**
        * Constructs a new Gaussian Kernel.
        * @param sigma The standard deviation for the Gaussian distribution.
        */
    public Gaussian(double sigma){
        this.sigma = sigma;
    }

    /**
        * Gets the sigma value for the kernel.
        * When setting sigma, gamma gets updated accordingly (gamma = 0.5/sigma^2).
        * @return Sigma value.
        */
    public double getSigma(){
        return sigma;
    }

    /**
        * Sets the sigma value for the kernel.
        * When setting sigma, gamma gets updated accordingly (gamma = 0.5/sigma^2).
        * @param value Sigma value.
        */
    public void setSigma(double value){
            sigma = value;
            gamma = 1.0 / (2.0 * sigma * sigma);
    }

    /**
        * Gets the sigma squared value for the kernel.
        * When setting sigma, gamma gets updated accordingly (gamma = 0.5/sigma).
        * @return Sigma squared.
        */
    public double getSigmaSquared(){
        return sigma * sigma;
    }

    /**
        * Sets the sigma squared value for the kernel.
        * When setting sigma, gamma gets updated accordingly (gamma = 0.5/sigma).
        * @param value Sigma squared.
        */
    public void setSigmaSquared(double value){
            sigma = Math.sqrt(value);
            gamma = 1.0 / (2.0 * value);
    }

    /**
        * Gets the gamma value for the kernel.
        * When setting gamma, sigma gets updated accordingly (gamma = 0.5/sigma^2).
        * @return Gamma value.
        */
    public double getGamma(){
        return gamma;
    }

    /**
        * Sets the gamma value for the kernel.
        * When setting gamma, sigma gets updated accordingly (gamma = 0.5/sigma^2).
        * @return Gamma value.
        */
    public void setGamma(double value){
            gamma = value;
            sigma = Math.sqrt(1.0 / (gamma * 2.0));
    }

    /**
        * Gaussian Kernel function.
        * @param x Vector <c>x</c> in input space.
        * @param y Vector <c>y</c> in input space.
        * @return Dot product in feature (kernel) space.
        */
    public double Function(double[] x, double[] y){
        // Optimization in case x and y are
        // exactly the same object reference.
        if (x == y) return 1.0;

        double norm = 0.0, d;
        for (int i = 0; i < x.length; i++)
        {
            d = x[i] - y[i];
            norm += d * d;
        }

        return Math.exp(-gamma * norm);
    }
}