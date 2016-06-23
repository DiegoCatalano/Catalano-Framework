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
 * Move towards filter.
 * <br /> The result of this filter is an image, which is based on source image, but updated in the way to decrease diffirence with overlay image - source image is moved towards overlay image. The update equation is defined in the next way: <b>res = src + Min( Abs( ovr - src ), step ) * Sign( ovr - src )</b>.
 * @author Diego Catalano
 */
public class MoveTowards implements IApplyInPlace{
    
    private FastBitmap overlayImage;
    private int stepSize = 1;

    /**
     * Initialize a new instance of the MoveTowards class.
     */
    public MoveTowards() {}
    
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
        
        int size = sourceImage.getSize();
        int sizeDestination = overlayImage.getWidth() * overlayImage.getHeight();
        if ((sourceImage.isGrayscale()) && (overlayImage.isGrayscale())) {
            if (size == sizeDestination) {
                int l,v;
                for (int i = 0; i < size; i++) {
                    v = overlayImage.getGray(i) - sourceImage.getGray(i);
                    l = sourceImage.getGray(i);
                    if (v > 0) {
                        l +=  stepSize < v ? stepSize : v;
                    }
                    else if(v < 0){
                        v = -v;
                        l -=  stepSize < v ? stepSize : v;
                    }
                    sourceImage.setGray(i, l);
                }
            }
        }
        else if ((sourceImage.isRGB()) && (overlayImage.isRGB())){
            if (size == sizeDestination) {
                int r,g,b,vR,vG,vB;
                for (int i = 0; i < size; i++) {
                    r = overlayImage.getRed(i) - sourceImage.getRed(i);
                    g = overlayImage.getGreen(i) - sourceImage.getGreen(i);
                    b = overlayImage.getBlue(i) - sourceImage.getBlue(i);
                    vR = sourceImage.getRed(i);
                    vG = sourceImage.getGreen(i);
                    vB = sourceImage.getBlue(i);

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
                    sourceImage.setRGB(i, vR, vG, vB);
                }
            }
        }
    }
}