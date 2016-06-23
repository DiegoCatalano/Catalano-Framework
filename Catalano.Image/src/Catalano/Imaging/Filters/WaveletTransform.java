// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
// Copyright © César Souza, 2009-2013
// cesarsouza at gmail.com
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

package Catalano.Imaging.Filters;

import Catalano.Imaging.FastBitmap;
import Catalano.Math.Tools;
import Catalano.Math.Wavelets.IWavelet;

/**
 * Wavelet transform filter.
 * @author Diego Catalano
 */
public class WaveletTransform {
    
    private IWavelet wavelet;
    private int width, height;
    private boolean waveletTransformed = false;
    private double[][] data;

    /**
     * Initialize a new instance of the WaveletTransform class.
     * @param wavelet A wavelet function.
     */
    public WaveletTransform(IWavelet wavelet) {
        this.wavelet = wavelet;
    }

    /**
     * Image's data.
     * @return data.
     */
    public double[][] getData() {
        return data;
    }

    /**
     * Image's data.
     * @param data data.
     */
    public void setData(double[][] data) {
        this.data = data;
    }
    
    /**
     * Status of the image - Wavelet transformed or not. 
     * @return <b>True</b>: is transformed, otherwise ,<b>False</b>.
     */
    public boolean isWaveletTransformed(){
        return waveletTransformed;
    }
    
    /**
     * Applies forward Wavelet transformation to an image.
     * @param fastBitmap Image.
     */
    public void Forward(FastBitmap fastBitmap){
        
        this.width = fastBitmap.getWidth();
        this.height = fastBitmap.getHeight();
        if (!waveletTransformed) {
            if (fastBitmap.isGrayscale()) {
                if (Tools.isPowerOf2(width) && Tools.isPowerOf2(height)) {
                    data = new double[height][width];
                    for (int i = 0; i < height; i++) {
                        for (int j = 0; j < width; j++) {
                            data[i][j] = Tools.Scale(0, 255, -1, 1, fastBitmap.getGray(i, j));
                        }
                    }
                    wavelet.Forward(data);
                    waveletTransformed = true;
                }
                else{
                    try {
                        throw new IllegalArgumentException("Image width and height should be power of 2.");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            else{
                try {
                    throw new IllegalArgumentException("Only grayscale images are supported.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * Applies backward Wavelet transformation to an image.
     */
    public void Backward(){
        if (waveletTransformed) {
            wavelet.Backward(data);
            waveletTransformed = false;
        }
    }
    
    /**
     * Convert image's data to FastBitmap.
     * @return FastBitmap.
     */
    public FastBitmap toFastBitmap(){
        FastBitmap l = new FastBitmap(width, height, FastBitmap.ColorSpace.Grayscale);
        
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                l.setGray(i, j, (int)Tools.Scale(-1, 1, 0, 255, data[i][j]));
            }
        }

        return l;
    }
}