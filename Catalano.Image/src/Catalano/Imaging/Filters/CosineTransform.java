// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
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

/**
 * Cosine transform filter.
 * @author Diego Catalano
 */
public class CosineTransform {
    
    private int width, height;
    private boolean isTransformed = false;
    private double[][] data;
    private double[][] Power;
    private double PowerMax;
    private int scaleValue = 255;

    /**
     * Initialize a new instance of the CosineTransform class.
     * @param wavelet A wavelet function.
     */
    public CosineTransform() {}

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
     * Status of the image - Cosine transformed or not. 
     * @return <b>True</b>: is transformed, otherwise ,<b>False</b>.
     */
    public boolean isWaveletTransformed(){
        return isTransformed;
    }
    
    /**
     * Applies forward Cosine transformation to an image.
     * @param fastBitmap Image.
     */
    public void Forward(FastBitmap fastBitmap){
        
        this.width = fastBitmap.getWidth();
        this.height = fastBitmap.getHeight();
        if (!isTransformed) {
            if (fastBitmap.isGrayscale()) {
                if (Tools.isPowerOf2(width) && Tools.isPowerOf2(height)) {
                    data = new double[height][width];
                    for (int i = 0; i < height; i++) {
                        for (int j = 0; j < width; j++) {
                            data[i][j] = Tools.Scale(0, 255, 0, 1, fastBitmap.getGray(i, j));
                        }
                    }
                    Catalano.Math.Transforms.DiscreteCosineTransform.Forward(data);
                    isTransformed = true;
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
     * Applies backward Cosine transformation to an image.
     */
    public void Backward(){
        if (isTransformed) {
            Catalano.Math.Transforms.DiscreteCosineTransform.Backward(data);
            isTransformed = false;
        }
    }
    
    /**
     * Convert image's data to FastBitmap.
     * @return FastBitmap.
     */
    public FastBitmap toFastBitmap(){
        FastBitmap l = new FastBitmap(width, height, FastBitmap.ColorSpace.Grayscale);
        PowerSpectrum();
        
        double max = Math.log(PowerMax+1.0);
        double scale = 1.0;
        if (scaleValue > 0) scale = scaleValue/max;
        
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                double p = Power[i][j];
                double plog = Math.log(p+1.0);
                l.setGray(i, j, (int)(plog * scale * 255));
            }
        }

        return l;
    }
    
    /**
     * Compute the Power Spectrum;
     */
    private void PowerSpectrum(){
        Power = new double[data.length][data[0].length];
        PowerMax = 0;

        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                double p = data[i][j];
                if (p < 0) p = -p;
                Power[i][j] = p;
                if (p > PowerMax) PowerMax = p;
            }
        }
    }
}