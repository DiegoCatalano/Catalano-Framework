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
import Catalano.Math.Matrix;

/**
 * Single scale retinex.
 * @author Diego Catalano
 */
public class SingleScaleRetinex implements IPhotometricFilter{
    
    private double scale;

    /**
     * Get scale.
     * @return Scale.
     */
    public double getScale() {
        return scale;
    }

    /**
     * Set scale.
     * @param scale Scale.
     */
    public void setScale(double scale) {
        this.scale = scale;
    }

    /**
     * Initializes a new instance of the SingleScaleRetinex class.
     * <br>
     * <br> Default:
     * <br> Scale = 15
     */
    public SingleScaleRetinex() {
        this(15);
    }

    /**
     * Initializes a new instance of the SingleScaleRetinex class.
     * @param scale Scale.
     */
    public SingleScaleRetinex(double scale) {
        this.scale = scale;
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        if(!fastBitmap.isGrayscale())
            throw new IllegalArgumentException("Single scale retinex only works in grayscale images.");
        
        //Transform the image in matrix.
        double[][] image = fastBitmap.toMatrixGrayAsDouble();
        
        //Perform the filter
        image = Process(image,true);
        
        //Convert back to the image
        fastBitmap.matrixToImage(image);
    }
    
    /**
     * Process the image.
     * @param image Image in matrix.
     * @param normalize Normalize the image[0..255].
     * @return The response of the filter.
     */
    public double[][] Process(double[][] image, boolean normalize){
        
        //Build filter
        double[][] g = new double[image.length][image[0].length];
        double c = Math.ceil(image.length / 2);
        
        double sum = 0;
        for (int i = 0; i < g.length; i++) {
            for (int j = 0; j < g[0].length; j++) {
                double radius = ((c-i)*(c-i)+(c-j)*(c-j));
                g[i][j] = Math.exp(-(radius/(scale*scale)));
                sum += g[i][j];
            }
        }
        Matrix.Divide(g, sum);
        
        //Decompose the kernel for to use separable convolution
        double[][] vec = Kernel.Decompose(g);
        
        //Perform the convolution
        double[][] r = ImageUtils.Convolution(image, vec[0], vec[1]);
        for (int i = 0; i < r.length; i++) {
            for (int j = 0; j < r[0].length; j++) {
                r[i][j] = Math.ceil(r[i][j]);
                if(r[i][j] == 0) r[i][j] = 0.01;
            }
        }
        
        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[0].length; j++) {
                r[i][j] = Math.log(image[i][j] + 0.01) - Math.log(r[i][j]);
            }
        }
        
        if(normalize)
            ImageUtils.Normalize(r);
        
        return r;
        
    }
}