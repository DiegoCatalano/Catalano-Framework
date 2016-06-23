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
import Catalano.Imaging.IExtract;

/**
 * Extract RGB channel from image.
 * <br /> Extracts specified channel of color image and returns it as grayscale image.
 * @author Diego Catalano
 */
public class ExtractRGBChannel implements IExtract{
    
    private FastBitmap l;
    
    /**
     * RGB Channel to extract.
     */
    public enum Channel {

        /**
         * Red channel.
         */
        R,
        /**
         * Green channel.
         */
        G,
        /**
         * Blue channel.
         */
        B
    };
    private Channel rgb;

    /**
     * Initialize a new instance of the ExtractRGBChannel class.
     * @param rgb RGB channel.
     */
    public ExtractRGBChannel(Channel rgb) {
        this.rgb = rgb;
    }

    /**
     * RGB channel.
     * @return RGB channel.
     */
    public Channel getRGB() {
        return rgb;
    }

    /**
     * RGB channel.
     * @param rgb RGB channel.
     */
    public void setRGB(Channel rgb) {
        this.rgb = rgb;
    }
    
    @Override
    public FastBitmap Extract(FastBitmap fastBitmap) {
        if (fastBitmap.isGrayscale()) {
            try {
                throw new Exception("Extract Channel works only with RGB images");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else{
            int width = fastBitmap.getWidth();
            int height = fastBitmap.getHeight();
            
            l = new FastBitmap(width, height, FastBitmap.ColorSpace.Grayscale);
            
            switch(rgb){
                case R:
                    for (int x = 0; x < height; x++) {
                        for (int y = 0; y < width; y++) {
                            l.setGray(x, y, fastBitmap.getRed(x, y));
                        }
                    }
                break;
                case G:
                    for (int x = 0; x < height; x++) {
                        for (int y = 0; y < width; y++) {
                            l.setGray(x, y, fastBitmap.getGreen(x, y));
                        }
                    }
                break;
                case B:
                    for (int x = 0; x < height; x++) {
                        for (int y = 0; y < width; y++) {
                            l.setGray(x, y, fastBitmap.getBlue(x, y));
                        }
                    }
                break;
            }
        }
        return l;
    }
}