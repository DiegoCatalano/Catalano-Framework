// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2014
// diego.catalano at live.com
//
// Copyright © Andrew Kirillov, 2007-2008
// andrew.kirillov@gmail.com
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
 * Merge filter - get MAX of pixels in two images.
 * <br /> The merge filter takes two images (source and overlay images) of the same size and pixel format and produces an image, where each pixel equals to the maximum value of corresponding pixels from provided images.
 * @author Diego Catalano
 */
public class Merge implements IBaseInPlace{
    
    private FastBitmap overlayImage;

    /**
     * Initialize a new instance of the Merge class.
     */
    public Merge() {
        
    }

    /**
     * Initialize a new instance of the Merge class.
     * @param overlayImage Overlay image.
     */
    public Merge(FastBitmap overlayImage) {
        this.overlayImage = overlayImage;
    }

    /**
     * Sets an overlay image, which will be used as the second image required to process source image.
     * @param overlayImage Overlay image.
     */
    public void setOverlayImage(FastBitmap overlayImage) {
        this.overlayImage = overlayImage;
    }
    
    @Override
    public void applyInPlace(FastBitmap sourceImage){
        
        int width = sourceImage.getWidth();
        int height = sourceImage.getHeight();
        int sizeOrigin = width * height;
        int sizeDestination = overlayImage.getWidth() * overlayImage.getHeight();
        if ((sourceImage.isGrayscale()) && (overlayImage.isGrayscale())) {
            if (sizeOrigin == sizeDestination) {
                int l;
                for (int x = 0; x < height; x++) {
                    for (int y = 0; y < width; y++) {
                        l = overlayImage.getGray(x, y);
                        if (l > sourceImage.getGray(x, y)) {
                            sourceImage.setGray(x, y, l);
                        }
                    }
                }
            }
        }
        else if ((sourceImage.isRGB()) && (overlayImage.isRGB())){
            if (sizeOrigin == sizeDestination) {
                int r,g,b;
                for (int x = 0; x < height; x++) {
                    for (int y = 0; y < width; y++) {
                        r = overlayImage.getRed(x, y);
                        g = overlayImage.getGreen(x, y);
                        b = overlayImage.getBlue(x, y);
                        
                        if (r > sourceImage.getRed(x, y)) {
                            sourceImage.setRed(x, y, r);
                        }
                        if (g > sourceImage.getGreen(x, y)) {
                            sourceImage.setGreen(x, y, g);
                        }
                        if (b > sourceImage.getBlue(x, y)) {
                            sourceImage.setBlue(x, y, b);
                        }
                    }
                }
            }
        }
    }
}
