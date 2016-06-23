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

import Catalano.Imaging.Filters.*;
import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.Tools.ImageUtils;
import Catalano.Imaging.Tools.Kernel;
import Catalano.Math.Functions.Gaussian;
import Catalano.Math.Matrix;

/**
 * Difference of Gaussians is a feature enhancement algorithm that involves the subtraction of one blurred version of an original image from another.
 * 
 * <p><li>Supported types: Grayscale, RGB.
 * <br><li>Coordinate System: Matrix.
 * 
 * @author Diego Catalano
 */
public class DifferenceOfGaussian implements IPhotometricFilter{
    
    private double sigma1;
    private double sigma2;
    private double[][] gv1;
    private double[][] gv2;

    /**
     * Get sigma 1.
     * @return Sigma value.
     */
    public double getSigma1() {
        return sigma1;
    }

    /**
     * Set sigma 1.
     * @param sigma1 Sigma value.
     */
    public void setSigma1(double sigma1) {
        this.sigma1 = sigma1;
        BuildKernels();
    }

    /**
     * Get sigma 2.
     * @return Sigma value.
     */
    public double getSigma2() {
        return sigma2;
    }

    /**
     * Set sigma 2.
     * @param sigma2 Sigma value.
     */
    public void setSigma2(double sigma2) {
        this.sigma2 = sigma2;
        BuildKernels();
    }

    /**
     * Initialize a new instance of the DifferenceOfGaussian class.
     * <br> 
     * <br> Default:
     * <br> Sigma 1: 1
     * <br> Sigma 2: 2
     */
    public DifferenceOfGaussian() {
        this(1,2);
    }
    
    /**
     * Initialize a new instance of the DifferenceOfGaussian class.
     * @param windowSize1 First window size.
     * @param windowSize2 Second window size.
     * @param sigma1 First sigma value.
     * @param sigma2 Second sigma value.
     */
    public DifferenceOfGaussian(double sigma1, double sigma2) {
        this.sigma1 = sigma1;
        this.sigma2 = sigma2;
        BuildKernels();
    }
    
    private void BuildKernels(){
        int size1 = 2 * (int)Math.ceil(3*sigma1) + 1;
        Gaussian ga = new Gaussian(sigma1);
        double[][] g1 = ga.Kernel2D(size1);

        int size2 = 2 * (int)Math.ceil(3*sigma2) + 1;
        ga.setSigma(sigma2);
        double[][] g2 = ga.Kernel2D(size2);
        
        //Decompose kernels
        gv1 = Kernel.Decompose(g1);
        gv2 = Kernel.Decompose(g2);
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        if(fastBitmap.isGrayscale()){
            
            double[][] image = fastBitmap.toMatrixGrayAsDouble();
            ImageUtils.Normalize(image);
            
            double[][] im1 = ImageUtils.Convolution(image, gv1[0], gv1[1], true);
            double[][] im2 = ImageUtils.Convolution(image, gv2[0], gv2[1], true);

            im1 = Matrix.Subtract(im1, im2);

            //Normalization
            double min = Double.MAX_VALUE;
            double max = -Double.MAX_VALUE;
            for (int i = 0; i < im1.length; i++) {
                for (int j = 0; j < im1[0].length; j++) {
                    min = Math.min(min, im1[i][j]);
                    max = Math.max(max, im1[i][j]);
                }
            }

            for (int i = 0; i < im1.length; i++) {
                for (int j = 0; j < im1[0].length; j++) {
                    fastBitmap.setGray(i, j, (int)Catalano.Math.Tools.Scale(min, max, 0, 255, im1[i][j]));
                }
            }
                
        }
        else if(fastBitmap.isRGB()){
            
            double[][][] image = fastBitmap.toMatrixRGBAsDouble();
            
            double[][][] im1 = ImageUtils.Convolution(image, gv1[0], gv1[1], true);
            double[][][] im2 = ImageUtils.Convolution(image, gv2[0], gv2[1], true);

            //Subtract operation
            for (int i = 0; i < im1.length; i++) {
                for (int j = 0; j < im1[0].length; j++) {
                    im1[i][j][0] = im1[i][j][0] - im2[i][j][0];
                    im1[i][j][1] = im1[i][j][1] - im2[i][j][1];
                    im1[i][j][2] = im1[i][j][2] - im2[i][j][2];
                }
            }

            //Normalization
            double minR,minG,minB;
            double maxR,maxG,maxB;
            minR = minG = minB = Double.MAX_VALUE;
            maxR = maxG = maxB = -Double.MAX_VALUE;
            
            for (int i = 0; i < im1.length; i++) {
                for (int j = 0; j < im1[0].length; j++) {
                    minR = Math.min(minR, im1[i][j][0]);
                    minG = Math.min(minG, im1[i][j][1]);
                    minB = Math.min(minB, im1[i][j][2]);
                    
                    maxR = Math.max(maxR, im1[i][j][0]);
                    maxG = Math.max(maxG, im1[i][j][1]);
                    maxB = Math.max(maxB, im1[i][j][2]);
                }
            }

            for (int i = 0; i < im1.length; i++) {
                for (int j = 0; j < im1[0].length; j++) {
                    int r = (int)Catalano.Math.Tools.Scale(minR, maxR, 0, 255, im1[i][j][0]);
                    int g = (int)Catalano.Math.Tools.Scale(minG, maxG, 0, 255, im1[i][j][1]);
                    int b = (int)Catalano.Math.Tools.Scale(minB, maxB, 0, 255, im1[i][j][2]);
                    
                    fastBitmap.setRGB(i, j, r,g,b);
                }
            }
            
            HistogramAdjust ha = new HistogramAdjust();
            ha.applyInPlace(fastBitmap);

        }
    }
    
    /**
     * Process the image as matrix.
     * @param image Image.
     * @param normalize True if the image needs to be normalized.
     * @return DoG of the image.
     */
    public double[][] Process(double[][] image, boolean normalize){
        
        double[][] copy = Matrix.Copy(image);
        
        ImageUtils.Normalize(copy);
        
        double[][] im1 = ImageUtils.Convolution(copy, gv1[0], gv1[1]);
        double[][] im2 = ImageUtils.Convolution(copy, gv2[0], gv2[1]);

        im1 = Matrix.Subtract(im1, im2);
        
        if(normalize){
            ImageUtils.Normalize(im1);
        }
        
        return im1;
    }
}