// Catalano Statistics Library
// The Catalano Framework
//
// Copyright 2015 Haifeng Li
// haifeng.hli at gmail.com
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package Catalano.Statistics.Kernels;

/**
 * Thin Plate Spline Kernel.
 * Thin plate splines (TPS) are a spline-based technique for data interpolation and smoothing.
 * 
 * @author Diego Catalano
 */
public class ThinPlateSpline implements IMercerKernel<double[]>{
    
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