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
 * Replace RGB Channel.
 * @author Diego Catalano
 */
public class ReplaceRGBChannel implements IApplyInPlace{
    
    public enum RGB{R, G, B};
    private FastBitmap band;
    private RGB rgb;
    
    public FastBitmap getBand() {
        return band;
    }

    public void setBand(FastBitmap band) {
        this.band = band;
    }

    public RGB getRGB() {
        return rgb;
    }

    public void setRGB(RGB rgb) {
        this.rgb = rgb;
    }
    
    /**
     * Initialize a new instance of the ReplaceRGBChannel class.
     * @param fastBitmap Band image.
     * @param rgb RGB.
     */
    public ReplaceRGBChannel(FastBitmap fastBitmap, RGB rgb) {
        this.band = fastBitmap;
        this.rgb = rgb;
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        if ((!band.isGrayscale()) || (!fastBitmap.isRGB())) {
            try {
                throw new Exception("ReplaceRGBChannel needs one image grayscale and another RGB image");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        if ((band.getWidth() != fastBitmap.getWidth()) || (band.getHeight() != fastBitmap.getHeight())) {
            try {
                throw new Exception("The image must be the same dimension");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        int width = fastBitmap.getWidth();
        int height = fastBitmap.getHeight();
        switch(rgb){
            case R:
                for (int x = 0; x < height; x++) {
                    for (int y = 0; y < width; y++) {
                        fastBitmap.setRed(x, y, band.getGray(x, y));
                    }
                }
            break;
                
            case G:
                for (int x = 0; x < height; x++) {
                    for (int y = 0; y < width; y++) {
                        fastBitmap.setGreen(x, y, band.getGray(x, y));
                    }
                }
            break;
                
            case B:
                for (int x = 0; x < height; x++) {
                    for (int y = 0; y < width; y++) {
                        fastBitmap.setBlue(x, y, band.getGray(x, y));
                    }
                }
            break;
        }
    }
}