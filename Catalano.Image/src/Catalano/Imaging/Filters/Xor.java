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
 * Xor filter - Performs logical operator "xor" between two images.
 * <br />Logical operators are generally derived from <i>Boolean algebra</i>.
 * <br /><br />Truth-tables for XOR: <br /><br />
 * A    B  |    Q <br />
 * --------- <br />
 * 0    0  |    0 <br />
 * 0    1  |    1 <br />
 * 1    0  |    1 <br />
 * 1    1  |    0 <br />
 * 
 * @author Diego Catalano
 */
public class Xor implements IApplyInPlace{
    FastBitmap overlayImage;

    /**
     * Initialize a new instance of the Xor class.
     */
    public Xor() {
        
    }

    /**
     * Initialize a new instance of the Xor class with defined an overlay image.
     * @param overlayImage Overlay image.
     */
    public Xor(FastBitmap overlayImage) {
        this.overlayImage = overlayImage;
    }

    /**
     * Set Overlay image.
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
                int grayS,grayO;
                for (int x = 0; x < height; x++) {
                    for (int y = 0; y < width; y++) {
                        grayS = sourceImage.getGray(x, y);
                        grayO = overlayImage.getGray(x, y);
                        if (((grayS == 0) && (grayO == 0)) || ((grayS == 255) && (grayO == 255))) {
                            sourceImage.setGray(x, y, 0);
                        }
                        else{
                            sourceImage.setGray(x, y, 255);
                        }
                    }
                }
            }
        }
    }
}
