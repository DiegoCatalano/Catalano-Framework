// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
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
import Catalano.Imaging.IApplyInPlace;

/**
 * Intersect filter - get MIN of pixels in two images. 
 * <br /> The intersect filter takes two images (source and overlay images) of the same size and pixel format and produces an image, where each pixel equals to the minimum value of corresponding pixels from provided images.
 * @author Diego Catalano
 */
public class Intersect implements IApplyInPlace{
    
    private FastBitmap overlayImage;

    /**
     * Initialize a new instance of the Intersect class.
     */
    public Intersect() {}

    /**
     * Initialize a new instance of the Intersect class.
     * @param overlayImage Overlay image.
     */
    public Intersect(FastBitmap overlayImage) {
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
        
        int size = sourceImage.getSize();
        int sizeDestination = overlayImage.getWidth() * overlayImage.getHeight();
        if ((sourceImage.isGrayscale()) && (overlayImage.isGrayscale())) {
            if (size == sizeDestination) {
                
                int l;
                for (int i = 0; i < size; i++) {
                    l = overlayImage.getGray(i);
                    if (l < sourceImage.getGray(i)) {
                        sourceImage.setGray(i, l);
                    }
                }
            }
        }
        else if ((sourceImage.isRGB()) && (overlayImage.isRGB())){
            if (size == sizeDestination) {
                
                int r,g,b;
                for (int i = 0; i < size; i++) {
                    r = overlayImage.getRed(i);
                    g = overlayImage.getGreen(i);
                    b = overlayImage.getBlue(i);

                    if (r < sourceImage.getRed(i)) {
                        sourceImage.setRed(i, r);
                    }
                    if (g < sourceImage.getGreen(i)) {
                        sourceImage.setGreen(i, g);
                    }
                    if (b < sourceImage.getBlue(i)) {
                        sourceImage.setBlue(i, b);
                    }
                }
            }
        }
    }
}