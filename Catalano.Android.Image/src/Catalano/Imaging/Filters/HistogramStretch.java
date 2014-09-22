// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2014
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
import Catalano.Imaging.IBaseInPlace;

/**
 * Stretch histogram across the entire gray level range, which has the effect of increasing the contrast of a low contrast image.
 * <br /> If a stretch is desired over a smaller range,  different MAX and MIN values can be specified.
 * @see Computer Imaging - Scott E. Umbaugh - Chapter 8 - p. 353
 * @author Diego Catalano
 */
public class HistogramStretch implements IBaseInPlace{
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
        
        if (fastBitmap.isGrayscale()) {
            float grayMax = getMaxGray(fastBitmap);
            float grayMin = getMinGray(fastBitmap);

            float gray; 
            float stretch;
            for (int x = 0; x < fastBitmap.getHeight(); x++) {
                for (int y = 0; y < fastBitmap.getWidth(); y++) {
                    gray = fastBitmap.getGray(x, y);
                    stretch = (((gray - grayMin)/(grayMax - grayMin)) * (max - min)) + min;
                    fastBitmap.setGray(x, y, (int)stretch);
                }
            }
        }
        else{
            float[] colorMax = getMaxRGB(fastBitmap);
            float[] colorMin = getMinRGB(fastBitmap);

            float r,g,b; 
            float stretchRed,stretchGreen,stretchBlue;
            for (int x = 0; x < fastBitmap.getHeight(); x++) {
                for (int y = 0; y < fastBitmap.getWidth(); y++) {
                    r = fastBitmap.getRed(x, y);
                    g = fastBitmap.getGreen(x, y);
                    b = fastBitmap.getBlue(x, y);
                    
                    stretchRed = (((r - colorMin[0])/(colorMax[0] - colorMin[0])) * (max - min)) + min;
                    stretchGreen = (((g - colorMin[1])/(colorMax[1] - colorMin[1])) * (max - min)) + min;
                    stretchBlue = (((b - colorMin[2])/(colorMax[2] - colorMin[2])) * (max - min)) + min;
                    
                    fastBitmap.setRGB(x, y, (int)stretchRed, (int)stretchGreen, (int)stretchBlue);
                }
            }
        }
    }
    
    private int getMaxGray(FastBitmap fb){
        int width = fb.getWidth();
        int height = fb.getHeight();
        
        
        int max = 0;
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                if (fb.getGray(i, j) > max) max = fb.getGray(i, j);
        
        return max;
    }
    
    private int getMinGray(FastBitmap fb){
        int width = fb.getWidth();
        int height = fb.getHeight();
        
        
        int min = 255;
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                if (fb.getGray(i, j) < min) min = fb.getGray(i, j);
        
        return min;
    }
    
    private float[] getMaxRGB(FastBitmap fb){
        float[] color = new float[3];
        int width = fb.getWidth();
        int height = fb.getHeight();
        
        int maxR = 0, maxG = 0, maxB = 0;
        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                if (fb.getRed(i, j) > maxR) maxR = fb.getRed(i, j);
                if (fb.getGreen(i, j) > maxG) maxG = fb.getGreen(i, j);
                if (fb.getBlue(i, j) > maxB) maxB = fb.getBlue(i, j);
            }
        }
        color[0] = (float)maxR;
        color[1] = (float)maxG;
        color[2] = (float)maxB;
        
        return color;
    }
    
    private float[] getMinRGB(FastBitmap fb){
        float[] color = new float[3];
        int width = fb.getWidth();
        int height = fb.getHeight();
        
        int minR = 255, minG = 255, minB = 255;
        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                if (fb.getRed(i, j) < minR) minR = fb.getRed(i, j);
                if (fb.getGreen(i, j) < minG) minG = fb.getGreen(i, j);
                if (fb.getBlue(i, j) < minB) minB = fb.getBlue(i, j);
            }
        }
        color[0] = (float)minR;
        color[1] = (float)minG;
        color[2] = (float)minB;
        
        return color;
    }
}