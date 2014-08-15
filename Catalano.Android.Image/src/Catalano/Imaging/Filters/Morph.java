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
 * Morph filter.
 * <br /> The filter combines two images by taking specified percent of pixels' intensities from source image and the rest from overlay image. For example, if the source percent value is set to 0.8, then each pixel of the result image equals to <b>0.8 * source + 0.2 * overlay</b>, where <b>source</b> and <b>overlay</b> are corresponding pixels' values in source and overlay images.
 * @author Diego Catalano
 */
public class Morph implements IBaseInPlace{
    
    private FastBitmap overlayImage;
    private double sourcePercent = 0.50;

    /**
     * Initialize a new instance of the BottomHat class.
     */
    public Morph() {
        
    }

    /**
     * Initialize a new instance of the BottomHat class.
     * @param overlayImage Overlay image.
     */
    public Morph(FastBitmap overlayImage) {
        this.overlayImage = overlayImage;
    }

    /**
     * Sets an overlay image, which will be used as the second image required to process source image. See documentation of particular inherited class for information about overlay image purpose.
     * @param overlayImage Overlay image.
     */
    public void setOverlayImage(FastBitmap overlayImage) {
        this.overlayImage = overlayImage;
    }

    /**
     * Percent of source image to keep, [0, 1].
     * @return Source percent.
     */
    public double getSourcePercent() {
        return sourcePercent;
    }

    /**
     * Percent of source image to keep, [0, 1].
     * @param sourcePercent Source percent.
     */
    public void setSourcePercent(double sourcePercent) {
        sourcePercent = sourcePercent < 0 ? 0: sourcePercent;
        sourcePercent = sourcePercent > 1 ? 1: sourcePercent;
        this.sourcePercent = sourcePercent;
    }
    
    @Override
    public void applyInPlace(FastBitmap sourceImage){
        
        int width = sourceImage.getWidth();
        int height = sourceImage.getHeight();
        int sizeOrigin = width * height;
        int sizeDestination = overlayImage.getWidth() * overlayImage.getHeight();
        if ((sourceImage.isGrayscale()) && (overlayImage.isGrayscale())) {
            if (sizeOrigin == sizeDestination) {
                int lS,lO;
                //percentage of overlay image
                double q = 1 - sourcePercent;
                for (int x = 0; x < height; x++) {
                    for (int y = 0; y < width; y++) {
                        lS = sourceImage.getGray(x, y);
                        lO = overlayImage.getGray(x, y);
                        sourceImage.setGray(x, y, (int)( ( sourcePercent * ( lS ) ) + ( q * ( lO ) )));
                    }
                }
            }
        }
        else if ((sourceImage.isRGB()) && (overlayImage.isRGB())){
            if (sizeOrigin == sizeDestination) {
                int rS, gS, bS,rO, gO, bO;
                //percentage of overlay image
                double q = 1 - sourcePercent;
                for (int x = 0; x < height; x++) {
                    for (int y = 0; y < width; y++) {
                        rS = sourceImage.getRed(x, y);
                        gS = sourceImage.getGreen(x, y);
                        bS = sourceImage.getBlue(x, y);
                        
                        rO = overlayImage.getRed(x, y);
                        gO = overlayImage.getGreen(x, y);
                        bO = overlayImage.getBlue(x, y);
                        
                        int r = (int)( ( sourcePercent * ( rS ) ) + ( q * ( rO )));
                        int g = (int)( ( sourcePercent * ( gS ) ) + ( q * ( gO )));
                        int b = (int)( ( sourcePercent * ( bS ) ) + ( q * ( bO )));
                        
                        sourceImage.setRGB(x, y, r, g, b);
                    }
                }
            }
        }
    }
}
