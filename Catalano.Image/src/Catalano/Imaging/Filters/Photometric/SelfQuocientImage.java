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
import Catalano.Math.Functions.Gaussian;
import Catalano.Math.Matrix;
import Catalano.Math.PaddingMatrix;

/**
 * Self Quocient Image.
 * @author Diego Catalano
 */
public class SelfQuocientImage implements IPhotometricFilter{
    
    private int size;
    private double sigma;
    private double[][] kernel;

    /**
     * Get bandwidth of the gaussian filter.
     * @return Bandwidth of the gaussian filter.
     */
    public double getSigma() {
        return sigma;
    }

    /**
     * Set bandwidth of the gaussian filter.
     * @param sigma Sigma value.
     */
    public void setSigma(double sigma) {
        this.sigma = sigma;
        BuildKernel();
    }

    /**
     * Get the size of the kernel.
     * @return Size of the kernel.
     */
    public int getSize() {
        return size;
    }

    /**
     * Set the size of the kernel.
     * @param size Size of the kernel.
     */
    public void setSize(int size) {
        this.size = size;
        BuildKernel();
    }

    /**
     * Initializes a new instance of the SelfQuocientImage class.
     */
    public SelfQuocientImage() {
        this(5);
    }
    
    /**
     * Initializes a new instance of the SelfQuocientImage class.
     * @param size Size of the kernel.
     */
    public SelfQuocientImage(int size){
        this(size,1);
    }

    /**
     * Initializes a new instance of the SelfQuocientImage class.
     * @param size Size of the kernel.
     * @param sigma Sigma value.
     */
    public SelfQuocientImage(int size, double sigma) {
        this.size = size;
        this.sigma = sigma;
        BuildKernel();
    }
    
    private void BuildKernel(){
        Gaussian g = new Gaussian(sigma);
        this.kernel = g.Kernel2D(size);
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        if(!fastBitmap.isGrayscale())
            throw new IllegalArgumentException("Self Quocient Image only works in grayscale images.");
        
        double[][] image = fastBitmap.toMatrixGrayAsDouble();
        
        double[][] result = Process(image, true);
        fastBitmap.matrixToImage(result);
    }
    
    /**
     * Process the filter.
     * @param image Image as matrix.
     * @param normalize Normalize the image.
     * @return Result of the filter.
     */
    public double[][] Process(double[][] image, boolean normalize){
        
        int width = image[0].length;
        int height = image.length;
        
        //Normalize the image
        ImageUtils.Normalize(image);
        
        //Image padding
        int padSize = (int)Math.floor(size/2.0);
        PaddingMatrix pad = new PaddingMatrix(padSize, padSize, true);
        double[][] padX = pad.Create(image);
        
        //Process the image
        double[][] Z = new double[height][width];
        for (int i = padSize; i < height + padSize; i++) {
            for (int j = padSize; j < width + padSize; j++) {
                double[][] region = Matrix.Submatrix(padX, i-padSize, i+padSize, j-padSize, j+padSize);
                double[][] m = returnStep(region);
                double[][] filt1 = Matrix.DotProduct(kernel,m);
                
                double sum = 0;
                for (int k = 0; k < size; k++) {
                    for (int l = 0; l < size; l++) {
                        sum += filt1[k][l];
                    }
                }
                
                Matrix.Divide(filt1, sum);
                Z[i-padSize][j-padSize] = Matrix.Sum(Matrix.DotProduct(filt1, region)) / (size*size);
            }
        }
        
        //Compute self quotient image and correct singularities
        for (int i = 0; i < Z.length; i++) {
            for (int j = 0; j < Z[0].length; j++) {
                Z[i][j] = image[i][j] / (Z[i][j] + 0.01);
            }
        }
        
        if(normalize)
            ImageUtils.Normalize(Z);
        
        return Z;
    }
    
    private double[][] returnStep(double[][] region){
        
        double[][] m = new double[region.length][region[0].length];
        double mean = Matrix.Mean(region);
        for (int i = 0; i < region.length; i++) {
            for (int j = 0; j < region[0].length; j++) {
                if(region[i][j] >= mean)
                    m[i][j] = 1;
                else
                    m[i][j] = 0;
            }
        }
        
        return m;
    }
}