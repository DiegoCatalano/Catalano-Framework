// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2014
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
import Catalano.Imaging.IBaseInPlace;

/**
 * Subtract filter - subtract pixel values of one or two images.
 * @author Diego Catalano
 */
public class Subtract implements IBaseInPlace{
    
    private FastBitmap overlayImage;
    private int red = 0, green = 0, blue = 0, gray = 0;
    private boolean isOverlay = false;

    /**
     * Initialize a new instance of the Add class.
     */
    public Subtract() {
        
    }
    
    /**
     * Initialize a new instance of the Add class.
     * @param gray Gray value.
     */
    public Subtract(int gray){
        this.gray = Math.abs(gray);
    }
    
    /**
     * Initialize a new instance of the Add class.
     * @param r Red value.
     * @param g Green value.
     * @param b Blue value.
     */
    public Subtract(int r, int g, int b){
        this.red = Math.abs(r);
        this.green = Math.abs(g);
        this.blue = Math.abs(b);
    }

    /**
     * Initialize a new instance of the Add class.
     * @param overlayImage Overlay image.
     */
    public Subtract(FastBitmap overlayImage) {
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
                        l = sourceImage.getGray(x, y) - gray;
                        l = l < 0 ? 0 : l;
                        sourceImage.setGray(x, y, l);
                    }
                }
        }
        else if (sourceImage.isRGB()){
                int r,g,b;
                for (int x = 0; x < height; x++) {
                    for (int y = 0; y < width; y++) {
                        r = sourceImage.getRed(x, y) - red;
                        g = sourceImage.getGreen(x, y) - green;
                        b = sourceImage.getBlue(x, y) - blue;
                        
                        r = r < 0 ? 0 : r;
                        g = g < 0 ? 0 : g;
                        b = b < 0 ? 0 : b;
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
                        l = sourceImage.getGray(x, y) - overlayImage.getGray(x, y);
                        l = l < 0 ? 0 : l;
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
                        r = sourceImage.getRed(x, y) - overlayImage.getRed(x, y);
                        g = sourceImage.getGreen(x, y) - overlayImage.getGreen(x, y);
                        b = sourceImage.getBlue(x, y) - overlayImage.getBlue(x, y);
                        
                        r = r < 0 ? 0 : r;
                        g = g < 0 ? 0 : g;
                        b = b < 0 ? 0 : b;
                        sourceImage.setRGB(x, y, r, g, b);
                    }
                }
            }
        }
    }
}
