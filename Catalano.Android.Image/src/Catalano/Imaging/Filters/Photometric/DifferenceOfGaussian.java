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
    
    private double sigma1 = 1.4D, sigma2 = 1.4D;
    private boolean normalize = true;

    /**
     * Initialize a new instance of the DifferenceOfGaussian class.
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
        this(sigma1,sigma2,true);
    }
    
    public DifferenceOfGaussian(double sigma1, double sigma2, boolean normalize){
        this.sigma1 = sigma1;
        this.sigma2 = sigma2;
        this.normalize = normalize;
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        int size1 = 2 * (int)Math.ceil(3*sigma1) + 1;
        Gaussian ga = new Gaussian(sigma1);
        double[][] g1 = ga.Kernel2D(size1);

        int size2 = 2 * (int)Math.ceil(3*sigma2) + 1;
        ga.setSigma(sigma2);
        double[][] g2 = ga.Kernel2D(size2);
        
        if(fastBitmap.isGrayscale()){
            double[][] im1 = operateGray(fastBitmap, g1);
            double[][] im2 = operateGray(fastBitmap, g2);

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
            
            if(normalize){
                HistogramAdjust ha = new HistogramAdjust();
                ha.applyInPlace(fastBitmap);
            }
        }
        else if(fastBitmap.isRGB()){
            double[][][] im1 = operateRGB(fastBitmap, g1);
            double[][][] im2 = operateRGB(fastBitmap, g2);

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
            
            if(normalize){
                HistogramAdjust ha = new HistogramAdjust();
                ha.applyInPlace(fastBitmap);
            }
        }
    }
    
    private double[][] operateGray(FastBitmap fastBitmap, double[][] kernel){
        //Perform the convolution
        int width = fastBitmap.getWidth();
        int height = fastBitmap.getHeight();
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
                            gray += kernel[i][j] * fastBitmap.getGray(Xline, Yline);
                        }
                        else {

                            int r = x + i - lines;
                            int c = y + j - lines;

                            if (r < 0) r = 0;
                            if (r >= height) r = height - 1;

                            if (c < 0) c = 0;
                            if (c >= width) c = width - 1;

                            gray += kernel[i][j] * fastBitmap.getGray(r, c);
                        }
                    }
                }
                response[x][y] = gray;
            }
        }
        
        return response;
    }
    
    private double[][][] operateRGB(FastBitmap fastBitmap, double[][] kernel){
        //Perform the convolution
        int width = fastBitmap.getWidth();
        int height = fastBitmap.getHeight();
        double[][][] response = new double[height][width][3];
        
        int Xline,Yline;
        int lines = (kernel.length - 1)/2;
        double red,green,blue;
        
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                red = green = blue = 0;
                for (int i = 0; i < kernel.length; i++) {
                    Xline = x + (i-lines);
                    for (int j = 0; j < kernel[0].length; j++) {
                        Yline = y + (j-lines);
                        if ((Xline >= 0) && (Xline < height) && (Yline >=0) && (Yline < width)) {
                            red += kernel[i][j] * fastBitmap.getRed(Xline, Yline);
                            green += kernel[i][j] * fastBitmap.getGreen(Xline, Yline);
                            blue += kernel[i][j] * fastBitmap.getBlue(Xline, Yline);
                        }
                        else {

                            int r = x + i - lines;
                            int c = y + j - lines;

                            if (r < 0) r = 0;
                            if (r >= height) r = height - 1;

                            if (c < 0) c = 0;
                            if (c >= width) c = width - 1;

                            red += kernel[i][j] * fastBitmap.getRed(r, c);
                            green += kernel[i][j] * fastBitmap.getGreen(r, c);
                            blue += kernel[i][j] * fastBitmap.getBlue(r, c);
                        }
                    }
                }
                response[x][y][0] = red;
                response[x][y][1] = green;
                response[x][y][2] = blue;
            }
        }
        
        return response;
    }
}
