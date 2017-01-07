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
import Catalano.Math.Matrix;

/**
 * Multi scale retinex.
 * @author Diego Catalano
 */
public class MultiscaleRetinex implements IPhotometricFilter{
    
    private int[] scales;

    /**
     * Get scales.
     * @return scales.
     */
    public int[] getScales() {
        return scales;
    }

    /**
     * Set scales.
     * @param scales scales.
     */
    public void setScales(int[] scales) {
        this.scales = scales;
    }

    /**
     * Initializes a new instance of the MultiScaleRetinex class.
     * <br>
     * <br> Default:
     * <br> Scales = 7, 15, 21
     */
    public MultiscaleRetinex() {
        this(new int[] {7,15,21});
    }

    /**
     * Initializes a new instance of the MultiScaleRetinex class.
     * @param scales Scales.
     */
    public MultiscaleRetinex(int[] scales) {
        this.scales = scales;
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        //Convert to matrix
        double[][] image = fastBitmap.toMatrixGrayAsDouble();
        
        //Process the filter
        double[][] r = Process(image,true);
        
        //Convert to the image.
        fastBitmap.matrixToImage(r);
        
    }
    
    /**
     * Process the image.
     * @param image Image to be processed.
     * @param normalize Normalize the image [0..255].
     * @return Response of the filter.
     */
    public double[][] Process(double[][] image, boolean normalize){
        
        SingleScaleRetinex ssr =  new SingleScaleRetinex(scales[0]);
        double[][] r = ssr.Process(image, normalize);
        
        for (int i = 1; i < scales.length; i++) {
            ssr.setScale(scales[i]);
            Matrix.Add(r, ssr.Process(image, normalize));
        }
        
        if(normalize)
            ImageUtils.Normalize(r);
        
        return r;
    }
}
