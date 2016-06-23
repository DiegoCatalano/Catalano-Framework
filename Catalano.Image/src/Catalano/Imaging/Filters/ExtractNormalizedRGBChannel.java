// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
// Copyright © Andrew Kirillov, 2007-2008
// andrew.kirillov at gmail.com
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
import Catalano.Imaging.IExtract;

/**
 * Extract normalized RGB channel from color image.
 * Extracts specified normalized RGB channel of color image and returns it as grayscale image.
 * Note: Normalized RGB color space is defined as:
 * r = R / (R + G + B ),
 * g = G / (R + G + B ),
 * b = B / (R + G + B ).
 * @author Diego Catalano
 */
public class ExtractNormalizedRGBChannel implements IExtract{
    
    /**
     * RGB Channel.
     */
    public enum Channel {

        /**
         * Red Channel.
         */
        R,
        /**
         * Green Channel.
         */
        G,
        /**
         * Blue Channel.
         */
        B
    };
    private Channel rgb;

    /**
     * Initialize a new instance of the ExtractNormalizedRGBChannel class.
     * @param rgb RGB Channel.
     */
    public ExtractNormalizedRGBChannel(Channel rgb) {
        this.rgb = rgb;
    }

    /**
     * Get RGB Channel.
     * @return RGB Channel.
     */
    public Channel getRGB() {
        return rgb;
    }

    /**
     * Set RGB Channel.
     * @param rgb RGB Channel.
     */
    public void setRGB(Channel rgb) {
        this.rgb = rgb;
    }

    @Override
    public FastBitmap Extract(FastBitmap fastBitmap) {
        
        int width = fastBitmap.getWidth();
        int height = fastBitmap.getHeight();
        
        if (fastBitmap.isGrayscale()) {
            try {
                throw new IllegalArgumentException("Extract Channel works only with RGB images");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
            
        FastBitmap l = new FastBitmap(width, height, FastBitmap.ColorSpace.Grayscale);
        int color;

        switch(rgb){
            case R:
                for (int x = 0; x < height; x++) {
                    for (int y = 0; y < width; y++) {
                        int r = fastBitmap.getRed(x, y);
                        int g = fastBitmap.getGreen(x, y);
                        int b = fastBitmap.getBlue(x, y);
                        color = r + g + b;
                        color = color == 0 ? 1: color;
                        color = 255 * r / color;
                        l.setGray(x, y, color);
                    }
                }
            return l;
                
            case G:
                for (int x = 0; x < height; x++) {
                    for (int y = 0; y < width; y++) {
                        int r = fastBitmap.getRed(x, y);
                        int g = fastBitmap.getGreen(x, y);
                        int b = fastBitmap.getBlue(x, y);
                        color = r + g + b;
                        color = color == 0 ? 1: color;
                        color = 255 * g / color;
                        l.setGray(x, y, color);
                    }
                }
            return l;
                
            case B:
                for (int x = 0; x < height; x++) {
                    for (int y = 0; y < width; y++) {
                        int r = fastBitmap.getRed(x, y);
                        int g = fastBitmap.getGreen(x, y);
                        int b = fastBitmap.getBlue(x, y);
                        color = r + g + b;
                        color = color == 0 ? 1: color;
                        color = 255 * b / color;
                        l.setGray(x, y, color);
                    }
                }
            return l;
        }
        return l;
    }
}