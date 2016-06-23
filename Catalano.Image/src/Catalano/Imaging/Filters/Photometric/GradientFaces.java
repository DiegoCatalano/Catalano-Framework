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
import Catalano.Math.Matrix;

/**
 * Gradient faces normalization.
 * @author Diego Catalano
 */
public class GradientFaces implements IPhotometricFilter{
    
    private double sigma;
    private double[][] gx;
    private double[][] gy;
    private boolean useEquation = true;

    /**
     * Get sigma.
     * @return Sigma value.
     */
    public double getSigma() {
        return sigma;
    }

    /**
     * Set sigma value.
     * @param sigma Sigma value.
     */
    public void setSigma(double sigma) {
        this.sigma = sigma;
        BuildDerivatives(sigma);
    }

    /**
     * Use the equation.
     * @return True if needs to use the equation.
     */
    public boolean isUseEquation() {
        return useEquation;
    }

    /**
     * Use the equation.
     * @param useEquation True if needs to use the equation.
     */
    public void setUseEquation(boolean useEquation) {
        this.useEquation = useEquation;
    }

    /**
     * Initializes a new instance of the GradientFaces class.
     */
    public GradientFaces() {
        this(0.75);
    }

    /**
     * Initializes a new instance of the GradientFaces class.
     * @param sigma Smoothing step parameter.
     */
    public GradientFaces(double sigma) {
        this(sigma, false);
    }
    
    /**
     * Initializes a new instance of the GradientFaces class.
     * @param sigma Smoothing step parameter.
     * @param useEquation Use the equation (8) in the paper.
     */
    public GradientFaces(double sigma, boolean useEquation){
        setSigma(sigma);
        this.useEquation = useEquation;
    }
    
    private void BuildDerivatives(double sigma){
        
        //Construct derivatives of Gaussians in x and y directions
        int size = (int)Math.floor(3.5*sigma);
        double[][] kernelX = new double[size * 2 + 1][size * 2 + 1];
        for (int i = 0; i < kernelX.length; i++) {
            int n = -size;
            for (int j = 0; j < kernelX[0].length; j++) {
                kernelX[i][j] = n++;
            }
        }
        double[][] kernelY = Matrix.Transpose(kernelX);
        
        gx = new double[kernelX.length][kernelX[0].length];
        for (int i = 0; i < gx.length; i++) {
            for (int j = 0; j < gx[0].length; j++) {
                gx[i][j] = -2 * kernelX[i][j] * Math.exp(-(kernelX[i][j] * kernelX[i][j] + kernelY[i][j] * kernelY[i][j]) / (2*(sigma*sigma)));
            }
        }
        gy = Matrix.Transpose(gx);
        
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        //Convolution and find atan.
        int width = fastBitmap.getWidth();
        int height = fastBitmap.getHeight();
        double[][] response = new double[height][width];
        
        int Xline,Yline;
        int lines = (gx.length - 1)/2;
        double grayX, grayY;
        double min = Double.MAX_VALUE;
        double max = -Double.MAX_VALUE;
        
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                grayX = grayY = 0;
                for (int i = 0; i < gx.length; i++) {
                    Xline = x + (i-lines);
                    for (int j = 0; j < gx[0].length; j++) {
                        Yline = y + (j-lines);
                        if ((Xline >= 0) && (Xline < height) && (Yline >=0) && (Yline < width)) {
                            grayX += gx[i][j] * fastBitmap.getGray(Xline, Yline);
                            grayY += gy[i][j] * fastBitmap.getGray(Xline, Yline);
                        }
                        else {

                            int r = x + i - lines;
                            int c = y + j - lines;

                            if (r < 0) r = 0;
                            if (r >= height) r = height - 1;

                            if (c < 0) c = 0;
                            if (c >= width) c = width - 1;

                            grayX += gx[i][j] * fastBitmap.getGray(r, c);
                            grayY += gy[i][j] * fastBitmap.getGray(r, c);
                        }
                    }
                }
                
                response[x][y] = Math.atan2(grayY, grayX);
                min = Math.min(min, response[x][y]);
                max = Math.max(max, response[x][y]);
            }
        }
        
        //to get the range given in Eq. (8) of the paper
        if(useEquation){
            min = Double.MAX_VALUE;
            max = -Double.MAX_VALUE;
            double pi2 = 2 * Math.PI;
            for (int i = 0; i < response.length; i++) {
                for (int j = 0; j < response[0].length; j++) {
                    if(response[i][j] >= 0)
                        response[i][j] += pi2;
                    else
                        response[i][j] = pi2 - response[i][j];
                min = Math.min(min, response[i][j]);
                max = Math.max(max, response[i][j]);
                }
            }
        }
        
        //Normalization
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                fastBitmap.setGray(i, j, (int)Catalano.Math.Tools.Scale(min, max, 0, 255, response[i][j]));
            }
        }
    }
}