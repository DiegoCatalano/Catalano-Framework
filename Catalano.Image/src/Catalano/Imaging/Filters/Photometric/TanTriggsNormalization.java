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
import Catalano.Imaging.Filters.GammaCorrection;
import Catalano.Math.Matrix;

/**
 *
 * @author Diego
 */
public class TanTriggsNormalization implements IPhotometricFilter{
    
    private double gamma;

    public TanTriggsNormalization() {
        this(0.2);
    }

    public TanTriggsNormalization(double gamma) {
        this.gamma = gamma;
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        if(!fastBitmap.isGrayscale())
            throw new IllegalArgumentException("Tan Triggs only works in grayscale images.");
        
        //Gamma correction
        GammaCorrection gc = new GammaCorrection(gamma);
        gc.applyInPlace(fastBitmap);
        
        //Transform the image into matrix
        double[][] image = new double[fastBitmap.getHeight()][fastBitmap.getWidth()];
        fastBitmap.toArrayGray(image);
        
        //Dog
        DifferenceOfGaussian dog = new DifferenceOfGaussian(0.5, 2);
        image = dog.Process(image, false);
        
        //Robust post-processor
        RobustPostprocessor rp = new RobustPostprocessor();
        image = rp.Apply(image);
        
        //Normalize the image
        double[] coef = Matrix.MinMax(image);
        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[0].length; j++) {
                fastBitmap.setGray(i, j, (int)Catalano.Math.Tools.Scale(coef[0], coef[1], 0, 255, image[i][j]));
            }
        }
    }
}