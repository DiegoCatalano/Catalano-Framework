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
 * Multiply filter - multiply pixel values of one or two images.
 * @author Diego Catalano
 */
public class Multiply implements IApplyInPlace{
    
    private FastBitmap overlayImage;
    private double red = 0,green = 0,blue = 0, gray = 0;
    private boolean isOverlay = false;

    /**
     * Initialize a new instance of the Add class.
     */
    public Multiply() {
        
    }
    
    /**
     * Initialize a new instance of the Add class.
     * @param gray Gray value.
     */
    public Multiply(double gray){
        this.gray = gray;
    }
    
    /**
     * Initialize a new instance of the Add class.
     * @param r Red value.
     * @param g Green value.
     * @param b Blue value.
     */
    public Multiply(double r, double g, double b) {
        this.red = Math.abs(r);
        this.green = Math.abs(g);
        this.blue = Math.abs(b);
    }

    /**
     * Initialize a new instance of the Add class.
     * @param overlayImage Overlay image.
     */
    public Multiply(FastBitmap overlayImage) {
        this.overlayImage = overlayImage;
        this.isOverlay = true;
    }

    /**
     * Sets an overlay image, which will be used as the second image required to process source image.
     * @param overlayImage Overlay image.
     */
    public void setOverlayImage(FastBitmap overlayImage) {
        this.overlayImage = overlayImage;
        this.isOverlay = true;
    }
    
    @Override
    public void applyInPlace(FastBitmap sourceImage){
        if (isOverlay) {
            ApplyInPlaceImage(sourceImage);
        }
        else{
            ApplyInPlaceValues(sourceImage);
        }
    }
    
    private void ApplyInPlaceValues(FastBitmap sourceImage){
        
        int width = sourceImage.getWidth();
        int height = sourceImage.getHeight();
        if (sourceImage.isGrayscale()){
                int l;
                for (int x = 0; x < height; x++) {
                    for (int y = 0; y < width; y++) {
                        l = (int)((double)sourceImage.getGray(x, y) * gray);
                        l = l > 255 ? 255 : l;
                        sourceImage.setGray(x, y, l);
                    }
                }
            }
        else if (sourceImage.isRGB()){
                
                int r,g,b;
                for (int x = 0; x < height; x++) {
                    for (int y = 0; y < width; y++) {
                        r = (int)((double)sourceImage.getRed(x, y) * red);
                        g = (int)((double)sourceImage.getGreen(x, y) * green);
                        b = (int)((double)sourceImage.getBlue(x, y) * blue);
                        
                        r = r > 255 ? 255 : r;
                        g = g > 255 ? 255 : g;
                        b = b > 255 ? 255 : b;
                        sourceImage.setRGB(x, y, r, g, b);
                    }
                }
            }
    }
    
    private void ApplyInPlaceImage(FastBitmap sourceImage){
        
        int width = sourceImage.getWidth();
        int height = sourceImage.getHeight();
        int sizeOrigin = width * height;
        int sizeDestination = overlayImage.getWidth() * overlayImage.getHeight();
        if ((sourceImage.isGrayscale()) && (overlayImage.isGrayscale())) {
            if (sizeOrigin == sizeDestination) {
                int l;
                for (int x = 0; x < height; x++) {
                    for (int y = 0; y < width; y++) {
                        l = sourceImage.getGray(x, y) * overlayImage.getGray(x, y);
                        l = l > 255 ? 255 : l;
                        sourceImage.setGray(x, y, l);
                    }
                }
            }
        }
        else if ((sourceImage.isRGB()) && (overlayImage.isRGB())){
            if (sizeOrigin == sizeDestination) {
                
                int r,g,b;
                for (int x = 0; x < height; x++) {
                    for (int y = 0; y < width; y++) {
                        r = sourceImage.getRed(x, y) * overlayImage.getRed(x, y);
                        g = sourceImage.getGreen(x, y) * overlayImage.getGreen(x, y);
                        b = sourceImage.getBlue(x, y) * overlayImage.getBlue(x, y);
                        
                        r = r > 255 ? 255 : r;
                        g = g > 255 ? 255 : g;
                        b = b > 255 ? 255 : b;
                        sourceImage.setRGB(x, y, r, g, b);
                    }
                }
            }
        }
    }
}
