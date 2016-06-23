// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
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
 * Stretch histogram across the entire gray level range, which has the effect of increasing the contrast of a low contrast image.
 * <br /> If a stretch is desired over a smaller range,  different MAX and MIN values can be specified.
 * @see Computer Imaging - Scott E. Umbaugh - Chapter 8 - p. 353
 * @author Diego Catalano
 */
public class HistogramStretch implements IApplyInPlace{
    private int max, min;

    /**
     * Initialize a new instance of the HistogramStretch class.
     */
    public HistogramStretch() {
        this.max = 255;
        this.min = 0;
    }

    /**
     * Initialize a new instance of the HistogramStretch class.
     * @param min Correspond to the minimum gray level values possible. Range [0..255].
     * @param max Correspond to the maximum gray level values possible. Range [0..255].
     */
    public HistogramStretch(int min, int max) {
        this.min = Math.max(min, 0);
        this.max = Math.min(max, 255);
    }

    /**
     * Maximum gray level.
     * @return Maximum.
     */
    public int getMax() {
        return max;
    }

    /**
     * Maximum gray level.
     * @param max Maximum.
     */
    public void setMax(int max) {
        this.max = Math.min(max, 255);
    }

    /**
     * Minimum gray level.
     * @return Minimum.
     */
    public int getMin() {
        return min;
    }

    /**
     * Minimum gray level.
     * @param min Minimum.
     */
    public void setMin(int min) {
        this.min = Math.max(min, 0);
    }
    
    @Override
    public void applyInPlace(FastBitmap fastBitmap){
        
        int size = fastBitmap.getSize();
        
        if (fastBitmap.isGrayscale()) {
            float grayMax = getMaxGray(fastBitmap);
            float grayMin = getMinGray(fastBitmap);

            float gray; 
            float stretch;
            for (int x = 0; x < size; x++) {
                gray = fastBitmap.getGray(x);
                stretch = (((gray - grayMin)/(grayMax - grayMin)) * (max - min)) + min;
                fastBitmap.setGray(x, (int)stretch);
            }
        }
        else{
            float[] colorMax = getMaxRGB(fastBitmap);
            float[] colorMin = getMinRGB(fastBitmap);

            float r,g,b; 
            float stretchRed,stretchGreen,stretchBlue;
            for (int x = 0; x < size; x++) {
                r = fastBitmap.getRed(x);
                g = fastBitmap.getGreen(x);
                b = fastBitmap.getBlue(x);

                stretchRed = (((r - colorMin[0])/(colorMax[0] - colorMin[0])) * (max - min)) + min;
                stretchGreen = (((g - colorMin[1])/(colorMax[1] - colorMin[1])) * (max - min)) + min;
                stretchBlue = (((b - colorMin[2])/(colorMax[2] - colorMin[2])) * (max - min)) + min;

                fastBitmap.setRGB(x, (int)stretchRed, (int)stretchGreen, (int)stretchBlue);
            }
        }
    }
    
    private int getMaxGray(FastBitmap fb){
        
        int size = fb.getSize();
        
        int max = 0;
        for (int i = 0; i < size; i++)
            if (fb.getGray(i) > max) max = fb.getGray(i);
        
        return max;
    }
    
    private int getMinGray(FastBitmap fb){
        
        int size = fb.getSize();
        
        int min = 255;
        for (int i = 0; i < size; i++)
            if (fb.getGray(i) < min) min = fb.getGray(i);
        
        return min;
    }
    
    private float[] getMaxRGB(FastBitmap fb){
        float[] color = new float[3];
        int size = fb.getSize();
        
        int maxR = 0, maxG = 0, maxB = 0;
        for (int i = 0; i < size; i++){
            if (fb.getRed(i) > maxR) maxR = fb.getRed(i);
            if (fb.getGreen(i) > maxG) maxG = fb.getGreen(i);
            if (fb.getBlue(i) > maxB) maxB = fb.getBlue(i);
        }
        color[0] = (float)maxR;
        color[1] = (float)maxG;
        color[2] = (float)maxB;
        
        return color;
    }
    
    private float[] getMinRGB(FastBitmap fb){
        float[] color = new float[3];
        int size = fb.getSize();
        
        int minR = 255, minG = 255, minB = 255;
        for (int i = 0; i < size; i++){
            if (fb.getRed(i) < minR) minR = fb.getRed(i);
            if (fb.getGreen(i) < minG) minG = fb.getGreen(i);
            if (fb.getBlue(i) < minB) minB = fb.getBlue(i);
        }
        color[0] = (float)minR;
        color[1] = (float)minG;
        color[2] = (float)minB;
        
        return color;
    }
}