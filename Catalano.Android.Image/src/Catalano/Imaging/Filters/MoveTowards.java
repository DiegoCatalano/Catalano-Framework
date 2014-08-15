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
 * Move towards filter.
 * <br /> The result of this filter is an image, which is based on source image, but updated in the way to decrease diffirence with overlay image - source image is moved towards overlay image. The update equation is defined in the next way: <b>res = src + Min( Abs( ovr - src ), step ) * Sign( ovr - src )</b>.
 * @author Diego Catalano
 */
public class MoveTowards implements IBaseInPlace{
    
    private FastBitmap overlayImage;
    private int stepSize = 1;

    /**
     * Initialize a new instance of the MoveTowards class.
     */
    public MoveTowards() {
        
    }
    
    /**
     * Initialize a new instance of the MoveTowards class.
     * @param overlayImage Overlay image.
     * @param stepSize Step size, [0, 255].
     */
    public MoveTowards(FastBitmap overlayImage,int stepSize) {
        this.overlayImage = overlayImage;
        setStepSize(stepSize);
    }

    /**
     * Defines the maximum amount of changes per pixel in the source image.
     * @return Step size, [0, 255].
     */
    public int getStepSize() {
        return stepSize;
    }

    /**
     * Defines the maximum amount of changes per pixel in the source image.
     * @param stepSize Step size, [0, 255].
     */
    public void setStepSize(int stepSize) {
        stepSize = stepSize < 0 ? 1 : stepSize;
        this.stepSize = stepSize;
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
                int l,v;
                for (int x = 0; x < height; x++) {
                    for (int y = 0; y < width; y++) {
                        v = overlayImage.getGray(x, y) - sourceImage.getGray(x, y);
                        l = sourceImage.getGray(x, y);
                        if (v > 0) {
                            l +=  stepSize < v ? stepSize : v;
                        }
                        else if(v < 0){
                            v = -v;
                            l -=  stepSize < v ? stepSize : v;
                        }
                        sourceImage.setGray(x, y, l);
                    }
                }
            }
        }
        else if ((sourceImage.isRGB()) && (overlayImage.isRGB())){
            if (sizeOrigin == sizeDestination) {
                int r,g,b,vR,vG,vB;
                for (int x = 0; x < height; x++) {
                    for (int y = 0; y < width; y++) {
                        r = overlayImage.getRed(x, y) - sourceImage.getRed(x, y);
                        g = overlayImage.getGreen(x, y) - sourceImage.getGreen(x, y);
                        b = overlayImage.getBlue(x, y) - sourceImage.getBlue(x, y);
                        vR = sourceImage.getRed(x, y);
                        vG = sourceImage.getGreen(x, y);
                        vB = sourceImage.getBlue(x, y);
                        
                        if (r > 0) {
                            vR +=  stepSize < r ? stepSize : r;
                        }
                        if(g > 0){
                            vG +=  stepSize < g ? stepSize : g;
                        }
                        if(b > 0){
                            vB +=  stepSize < b ? stepSize : b;
                        }
                        if(r < 0){
                            r = -r;
                            vR -=  stepSize < r ? stepSize : r;
                        }
                        if(g < 0){
                            g = -g;
                            vG -=  stepSize < g ? stepSize : g;
                        }
                        if(b < 0){
                            b = -b;
                            vB -=  stepSize < b ? stepSize : b;
                        }
                        sourceImage.setRGB(x, y, vR, vG, vB);
                    }
                }
            }
        }
    }
}
