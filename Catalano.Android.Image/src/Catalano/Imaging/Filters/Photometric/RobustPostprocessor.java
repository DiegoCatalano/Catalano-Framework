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

/**
 * Robust PostProcessing step under the Contrast Equalization.
 * @author Diego Catalano
 */
public class RobustPostprocessor {
    
    private double alfa;
    private double tao;

    /**
     * Get alfa value, controls the postprocessing procedure.
     * @return Alfa value.
     */
    public double getAlfa() {
        return alfa;
    }

    /**
     * Set alfa value, controls the postprocessing procedure.
     * @param alfa Alfa value.
     */
    public void setAlfa(double alfa) {
        this.alfa = alfa;
    }

    /**
     * Get tao value, controls the postprocessing procedure.
     * @return Tao value,
     */
    public double getTao() {
        return tao;
    }

    /**
     * Set tao value, controls the postprocessing procedure.
     * @param tao Tao value.
     */
    public void setTao(double tao) {
        this.tao = tao;
    }

    /**
     * Initialize a new instance of the RobustPostprocessor class.
     */
    public RobustPostprocessor() {
        this(0.1,10);
    }

    /**
     * Initialize a new instance of the RobustPostprocessor class.
     * @param alfa Alfa value.
     * @param tao Tao value.
     */
    public RobustPostprocessor(double alfa, double tao) {
        this.alfa = alfa;
        this.tao = tao;
    }
    
    /**
     * Perform the process.
     * @param image Image.
     * @return Robust postprocessing of the image.
     */
    public double[][] Process(double[][] image){
        
        //First stage normalization
        double mean = 0;
        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[0].length; j++) {
                mean += Math.pow(Math.abs(image[i][j]), alfa);
            }
        }
        
        mean /= (double)(image.length * image[0].length);
        mean = Math.pow(mean, 1.0 / alfa);
        
        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[0].length; j++) {
                image[i][j] /= mean;
            }
        }
        
        //Second stage normalization
        mean = 0;
        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[0].length; j++) {
                mean += Math.pow(Math.min(Math.abs(image[i][j]), tao), alfa);
            }
        }
        
        mean /= (double)(image.length * image[0].length);
        mean = Math.pow(mean, 1.0 / alfa);
        
        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[0].length; j++) {
                image[i][j] /= mean;
            }
        }
        
        //Nonlinear mapping
        double[][] result = new double[image.length][image[0].length];
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[0].length; j++) {
                result[i][j] = tao * Math.tanh(image[i][j] / tao);
            }
        }
        
        return result;
    }
}