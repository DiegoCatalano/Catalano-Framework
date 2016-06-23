// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
// Copyright (c) 2011, Vitomir Struc
// Copyright (c) 2009, Gabriel Peyre
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without 
// modification, are permitted provided that the following conditions are 
// met:
//
//    * Redistributions of source code must retain the above copyright 
//      notice, this list of conditions and the following disclaimer.
//    * Redistributions in binary form must reproduce the above copyright 
//      notice, this list of conditions and the following disclaimer in 
//      the documentation and/or other materials provided with the distribution
//      
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE 
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
// POSSIBILITY OF SUCH DAMAGE.
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

package Catalano.Imaging.Filters.Photometric;

import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.Tools.ImageUtils;
import Catalano.Imaging.Tools.Kernel;
import Catalano.Math.Functions.Gaussian;
import Catalano.Math.Matrix;

/**
 * Retina modeling normalization.
 * @author Diego Catalano
 */
public class RetinaModel implements IPhotometricFilter{
    
    private double sigma1;
    private double sigma2;
    private double dogSigma1;
    private double dogSigma2;
    private double threshold;

    /**
     * Get Sigma 1.
     * @return Sigma value.
     */
    public double getSigma1() {
        return sigma1;
    }

    /**
     * Set Sigma 1.
     * @param sigma1 Sigma value.
     */
    public void setSigma1(double sigma1) {
        this.sigma1 = sigma1;
    }

    /**
     * Get Sigma 2.
     * @return Sigma value.
     */
    public double getSigma2() {
        return sigma2;
    }

    /**
     * Set Sigma 2.
     * @param sigma1 Sigma value.
     */
    public void setSigma2(double sigma2) {
        this.sigma2 = sigma2;
    }

    /**
     * Get DoG sigma 1.
     * @return DoG sigma 1.
     */
    public double getDogSigma1() {
        return dogSigma1;
    }

    /**
     * Set DoG sigma 1.
     * @param dogSigma1 DoG sigma 1.
     */
    public void setDogSigma1(double dogSigma1) {
        this.dogSigma1 = dogSigma1;
    }

   /**
     * Get DoG sigma 2.
     * @return DoG sigma 2.
     */
    public double getDogSigma2() {
        return dogSigma2;
    }

    /**
     * Set DoG sigma 2.
     * @param dogSigma1 DoG sigma 2.
     */
    public void setDogSigma2(double dogSigma2) {
        this.dogSigma2 = dogSigma2;
    }

    /**
     * Get threshold.
     * @return Threshold value.
     */
    public double getThreshold() {
        return threshold;
    }

    /**
     * Set threshold value.
     * @param threshold Threshold value.
     */
    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    /**
     * Initialize a new instance of the RetinaModel class.
     * Default:
     * Sigma1 = 1
     * Sigma2 = 3
     * DoG Sigma1 = 0.5
     * DoG Sigma2 = 4
     * Threshold = 5
     */
    public RetinaModel() {
        this(1,3,0.5,4,5);
    }

    /**
     * Initialize a new instance of the RetinaModel class.
     * @param sigma1 Sigma 1.
     * @param sigma2 Sigma 2.
     * @param dogSigma1 DoG Sigma 1.
     * @param dogSigma2 DoG Sigma 2.
     * @param threshold Threshold.
     */
    public RetinaModel(double sigma1, double sigma2, double dogSigma1, double dogSigma2, double threshold) {
        this.sigma1 = sigma1;
        this.sigma2 = sigma2;
        this.dogSigma1 = dogSigma1;
        this.dogSigma2 = dogSigma2;
        this.threshold = threshold;
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        if(!fastBitmap.isGrayscale())
            throw new IllegalArgumentException("Retina modeling only works in grayscale images.");
        
        //Transform the image in the matrix
        double[][] image = fastBitmap.toMatrixGrayAsDouble();
        
        //Normalize the image
        ImageUtils.Normalize(image);
        
        //Create kernels
        int size1 = 2 * (int)Math.ceil(3*sigma1) + 1;
        Gaussian ga = new Gaussian(sigma1);
        double[][] g1 = ga.Kernel2D(size1);
        
        int size2 = 2 * (int)Math.ceil(3*sigma2) + 1;
        ga.setSigma(sigma2);
        double[][] g2 = ga.Kernel2D(size2);
        
        //First non-linearity
        double[][] f = NonLinearity(image, g1);
        ImageUtils.Normalize(f);
        
        //Second non-linearity
        f = NonLinearity(f, g2);
        
        //Apply Dog
        DifferenceOfGaussian dog = new DifferenceOfGaussian(dogSigma1, dogSigma2);
        f = dog.Process(f, false);
        
        //Rescaling
        double s = 0;
        for (int i = 0; i < f.length; i++) {
            for (int j = 0; j < f[0].length; j++) {
                s += f[i][j] * f[i][j];
            }
        }
        s /= (double)(f.length * f[0].length);
        s = Math.sqrt(s);
        
        Matrix.Divide(f, s);
        
        //Truncation
        double min = Double.MAX_VALUE;
        double max = -Double.MAX_VALUE;
        for (int i = 0; i < f.length; i++) {
            for (int j = 0; j < f[0].length; j++) {
                double v = f[i][j];
                v = v > threshold ? threshold : v;
                v = v < -threshold ? -threshold : v;
                f[i][j] = v;
                
                min = Math.min(min, v);
                max = Math.max(max, v);
            }
        }
        
        //Normalize
        for (int i = 0; i < f.length; i++) {
            for (int j = 0; j < f[0].length; j++) {
                fastBitmap.setGray(i, j, (int)Catalano.Math.Tools.Scale(min, max, 0, 255, f[i][j]));
            }
        }
    }
    
    private double[][] NonLinearity(double[][] image, double[][] kernel){
        
        double mean = Matrix.Mean(image) / 2;
        double max = Matrix.Max(image);
        
        //Convolution
        double[][] c = ImageUtils.Convolution(image, kernel);
        
        //Sum with the mean and clap the values
        for (int i = 0; i < c.length; i++) {
            for (int j = 0; j < c[0].length; j++) {
                double v = c[i][j] + mean;
                c[i][j] = v > 255 ? 255 : v;
            }
        }
        
        //Perform the function
        for (int i = 0; i < c.length; i++) {
            for (int j = 0; j < c[0].length; j++) {
                c[i][j] = (c[i][j] + max) * (image[i][j] / (image[i][j] + c[i][j]));
            }
        }
        
        return c;
        
    }
}