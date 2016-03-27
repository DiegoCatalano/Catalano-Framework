// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2015
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
import Catalano.Math.Functions.Gaussian;
import Catalano.Math.Matrix;

/**
 *
 * @author Diego
 */
public class RetinaModel implements IPhotometricFilter{
    
    private double sigma1;
    private double sigma2;
    private double dogSigma1;
    private double dogSigma2;
    private double threshold;

    public RetinaModel() {
        this(1,3,0.5,4,5);
    }

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
        double[][] image = new double[fastBitmap.getHeight()][fastBitmap.getWidth()];
        fastBitmap.toArrayGray(image);
        
        //Normalize the image
        Normalize(image);
        
        //Create kernels
        int size1 = 2 * (int)Math.ceil(3*sigma1) + 1;
        Gaussian ga = new Gaussian(sigma1);
        double[][] g1 = ga.Kernel2D(size1);
        
        int size2 = 2 * (int)Math.ceil(3*sigma2) + 1;
        ga.setSigma(sigma2);
        double[][] g2 = ga.Kernel2D(size2);
        
        //First non-linearity
        double[][] f = NonLinearity(image, g1);
        Normalize(f);
        
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
        double[][] c = Convolution(image, kernel);
        
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
    
    private void Normalize(double[][] image){
        double min = Double.MAX_VALUE;
        double max = -Double.MAX_VALUE;
        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[0].length; j++) {
                min = Math.min(min, image[i][j]);
                max = Math.max(max, image[i][j]);
            }
        }

        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[0].length; j++) {
                image[i][j] = (int)(Catalano.Math.Tools.Scale(min, max, 0, 255, image[i][j]));
            }
        }
    }
    
    private double[][] Convolution(double[][] image, double[][] kernel){
        int width = image[0].length;
        int height = image.length;
        double[][] response = new double[height][width];
        
        int Xline,Yline;
        int lines = (kernel.length - 1)/2;
        double gray;
        
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                gray = 0;
                for (int i = 0; i < kernel.length; i++) {
                    Xline = x + (i-lines);
                    for (int j = 0; j < kernel[0].length; j++) {
                        Yline = y + (j-lines);
                        if ((Xline >= 0) && (Xline < height) && (Yline >=0) && (Yline < width)) {
                            gray += kernel[i][j] * image[Xline][Yline];
                        }
                        else {

                            int r = x + i - lines;
                            int c = y + j - lines;

                            if (r < 0) r = 0;
                            if (r >= height) r = height - 1;

                            if (c < 0) c = 0;
                            if (c >= width) c = width - 1;

                            gray += kernel[i][j] * image[r][c];
                        }
                    }
                }
                response[x][y] = gray;
            }
        }
        
        return response;
    }
    
}
