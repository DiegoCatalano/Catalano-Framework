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
import Catalano.Math.PaddingMatrix;

/**
 * Weber faces.
 * @author Diego Catalano
 */
public class WeberFaces implements IPhotometricFilter{
    
    private double sigma;
    private double alpha;
    private int size;
    
    private double[][] vectors;

    /**
     * Get standard deviation of the Gaussian filter.
     * @return sigma value.
     */
    public double getSigma() {
        return sigma;
    }

    /**
     * Set standard deviation of the Gaussian filter.
     * @param sigma Sigma value.
     */
    public void setSigma(double sigma) {
        Gaussian g = new Gaussian(sigma);
        double[][] kernel = g.Kernel2D(2*(int)Math.ceil(3*sigma)+1);
        
        this.sigma = sigma;
        this.vectors = Kernel.Decompose(kernel);
    }

    /**
     * Get parameter balancing the range of the input values of the atan function.
     * @return Parameter balancing.
     */
    public double getAlpha() {
        return alpha;
    }

    /**
     * Set parameter balancing the range of the input values of the atan function.
     * @param alpha Parameter balancing.
     */
    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    /**
     * Get the size of the neighborhood.
     * @return Size of the neighborhood.
     */
    public int getSize() {
        return size;
    }

    /**
     * Set the size of the neighborhood.
     * @param size Size of the neighborhood.
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * Initializes a new instance of the WeberFaces class.
     */
    public WeberFaces() {
        this(1,2,9);
    }

    /**
     * Initializes a new instance of the WeberFaces class.
     * @param sigma Smoothing value.
     * @param alpha Parameter balancing.
     * @param size Size of the neighborhood.
     */
    public WeberFaces(double sigma, double alpha, int size) {
        setSigma(sigma);
        this.alpha = alpha;
        this.size = size;
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        if(!fastBitmap.isGrayscale())
            throw new IllegalArgumentException("Weber Face only works in grayscale images.");
        
        //Convert the image in double
        double[][] image = fastBitmap.toMatrixGrayAsDouble();
        
        //Process the filter
        double[][] result = Process(image,true);
        
        //Back to the image
        fastBitmap.matrixToImage(result);
    }
    
    /**
     * Process the weberface filter.
     * @param image Image as matrix.
     * @param normalize Normalization.
     * @return Result of the filter.
     */
    public double[][] Process(double[][] image, boolean normalize){
        
        //Normalize the image
        ImageUtils.Normalize(image);
        
        //Gaussian smoothing
        double[][] r = ImageUtils.Convolution(image, vectors[0], vectors[1]);
        
        int pad = (int)((Math.sqrt(size)-1) / 2);
        
        PaddingMatrix pm = new PaddingMatrix(pad, pad, true);
        r = pm.Create(r);
        
        //Weberface calculation
        double[][] result = new double[image.length][image[0].length];
        for (int i = pad; i < image.length + pad; i++) {
            for (int j = pad; j < image[0].length + pad; j++) {
                
                double sum = 0;
                for (int k = i - pad; k <= i + pad; k++) {
                    for (int l = j - pad; l <= j + pad; l++) {
                        sum += r[i][j] - r[k][l];
                    }
                }
                
                result[i-pad][j-pad] = Math.atan(alpha * sum / (r[i][j] + 0.01));
            }
        }
        
        if(normalize)
            ImageUtils.Normalize(result);
        
        return result;
    }
}