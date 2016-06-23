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
import Catalano.Imaging.IApplyInPlace;

/**
 * Separable Convolution filter.
 * @author Diego Catalano
 */
public class SeparableConvolution implements IApplyInPlace{
    
    private int width,height;
    private double[] kernelX;
    private double[] kernelY;
    private boolean replicate = false;

    /**
     * Verify if needs replicate pixels when is out of border.
     * @return Replicate.
     */
    public boolean isReplicate() {
        return replicate;
    }

    /**
     * Replicate pixels out of border.
     * @param replicate Replicate.
     */
    public void setReplicate(boolean replicate) {
        this.replicate = replicate;
    }

    /**
     * Initialize a new instance of the SeparableConvolution class.
     */
    public SeparableConvolution() {}

    /**
     * Initialize a new instance of the SeparableConvolution class.
     * @param kernelX X - Structuring element.
     * @param kernelY Y - Structuring element.
     */
    public SeparableConvolution(double[] kernelX, double[] kernelY) {
        this.kernelX = kernelX;
        this.kernelY = kernelY;
    }
    
    /**
     * Initialize a new instance of the SeparableConvolution class.
     * @param kernelX X - Structuring element.
     * @param kernelY Y - Structuring element.
     * @param replicate Replicate pixels out of border.
     */
    public SeparableConvolution(double[] kernelX, double[] kernelY, boolean replicate) {
        this.kernelX = kernelX;
        this.kernelY = kernelY;
        this.replicate = replicate;
    }
    
    @Override
    public void applyInPlace(FastBitmap fastBitmap){
        
        this.width = fastBitmap.getWidth();
        this.height = fastBitmap.getHeight();
        
        int Xline,Yline;
        int lines = (kernelX.length - 1) / 2;
        
        if (fastBitmap.isGrayscale()) {
            double[][] copy = new double[height][width];
            double gray;
            
            //Horizontal orientation
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    gray = 0;
                    for (int k = 0; k < kernelX.length; k++) {
                        Yline = j - lines + k;
                        if ((Yline >=0) && (Yline < width)) {
                            gray += kernelX[k] * fastBitmap.getGray(i, Yline);
                        }
                        else if (replicate){
                             
                            int c = j + k - lines;

                            if (c < 0) c = 0;
                            if (c >= width) c = width - 1;
                             
                            gray += kernelX[kernelX.length - k - 1] * fastBitmap.getGray(i, c);
                         }
                    }
                    
                    copy[i][j] = gray;
                    
                }
            }
            
            //Vertical orientation
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    gray = 0;
                    for (int k = 0; k < kernelX.length; k++) {
                        Xline = i - lines + k;
                        if ((Xline >=0) && (Xline < height)) {
                            gray += kernelY[k] * copy[Xline][j];
                        }
                         else if (replicate){
                             
                            int r = i + k - lines;

                            if (r < 0) r = 0;
                            if (r >= height) r = height - 1;
                             
                            gray += kernelY[k] * copy[r][j];
                         }
                    }
                    
                    gray = gray < 0 ? 0 : gray;
                    gray = gray > 255 ? 255 : gray;
                    
                    fastBitmap.setGray(i, j, (int)gray);
                }
            }
            
        }
        else{
            double[][][] copy = new double[height][width][3];
            double r,g,b;
            
            //Horizontal orientation
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    r = g = b = 0;
                    for (int k = 0; k < kernelX.length; k++) {
                        Yline = j - lines + k;
                        if ((Yline >=0) && (Yline < width)) {
                            r += kernelX[k] * fastBitmap.getRed(i, Yline);
                            g += kernelX[k] * fastBitmap.getGreen(i, Yline);
                            b += kernelX[k] * fastBitmap.getBlue(i, Yline);
                        }
                        else if (replicate){
                             
                            int c = j + k - lines;

                            if (c < 0) c = 0;
                            if (c >= width) c = width - 1;
                             
                            r += kernelX[kernelX.length - k - 1] * fastBitmap.getRed(i, c);
                            g += kernelX[kernelX.length - k - 1] * fastBitmap.getGreen(i, c);
                            b += kernelX[kernelX.length - k - 1] * fastBitmap.getBlue(i, c);
                        }
                    }
                    copy[i][j][0] = r;
                    copy[i][j][1] = g;
                    copy[i][j][2] = b;
                }
            }
            
            //Vertical orientation
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    r = g = b = 0;
                    for (int k = 0; k < kernelY.length; k++) {
                        Xline = i - lines + k;
                        if ((Xline >=0) && (Xline < height)) {
                            r += kernelY[k] * copy[Xline][j][0];
                            g += kernelY[k] * copy[Xline][j][1];
                            b += kernelY[k] * copy[Xline][j][2];
                        }
                         else if (replicate){
                             
                            int rr = i + k - lines;

                            if (rr < 0) rr = 0;
                            if (rr >= height) rr = height - 1;
                             
                            r += kernelY[k] * copy[rr][j][0];
                            g += kernelY[k] * copy[rr][j][1];
                            b += kernelY[k] * copy[rr][j][2];
                         }
                    }
                    
                    r = r < 0 ? 0 : r;
                    r = r > 255 ? 255 : r;
                    
                    g = g < 0 ? 0 : g;
                    g = g > 255 ? 255 : g;
                    
                    b = b < 0 ? 0 : b;
                    b = b > 255 ? 255 : b;
                    
                    fastBitmap.setRGB(i, j, (int)r, (int)g, (int)b);
                }
            }
        }
    }
    
    private double SumKernel(double[] h, double[] v){
        double sum = 0;
        for (int i = 0; i < h.length; i++) {
            for (int j = 0; j < v.length; j++) {
                sum += h[i] * v[j];
            }
        }
        return sum;
    }
}