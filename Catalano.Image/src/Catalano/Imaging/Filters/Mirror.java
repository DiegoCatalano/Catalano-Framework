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
 * Mirroring filter.
 * @author Diego Catalano
 */
public class Mirror implements IApplyInPlace{
    private boolean mirrorX,mirrorY;

    /**
     * Initialize a new instance of the Mirror class.
     * @param MirrorX Specifies if mirroring should be done for X axis (horizontal mirroring).
     * @param MirrorY Specifies if mirroring should be done for Y axis (vertical mirroring).
     */
    public Mirror(boolean MirrorX, boolean MirrorY) {
        this.mirrorX = MirrorX;
        this.mirrorY = MirrorY;
    }

    /**
     * Specifies if mirroring should be done for X axis (horizontal mirroring).
     */
    public void setMirrorX(boolean mirrorX) {
        this.mirrorX = mirrorX;
    }

    /**
     * Specifies if mirroring should be done for Y axis (vertical mirroring).
     */
    public void setMirrorY(boolean mirrorY) {
        this.mirrorY = mirrorY;
    }
    
    @Override
    public void applyInPlace(FastBitmap fastBitmap){
        
        int width = fastBitmap.getWidth();
        int height = fastBitmap.getHeight();
        int halfWidth = width / 2;
        int halfHeight = height / 2;
        
        if (fastBitmap.isRGB()) {
            if (mirrorX) {
                for (int x = 0; x < height; x++) {
                    int w = width - 1;
                    for (int y = 0; y < halfWidth; y++) {
                        int tRed = fastBitmap.getRed(x, w);
                        int tGreen = fastBitmap.getGreen(x, w);
                        int tBlue = fastBitmap.getBlue(x, w);
                        
                        int r = fastBitmap.getRed(x, y);
                        int g = fastBitmap.getGreen(x, y);
                        int b = fastBitmap.getBlue(x, y);
                        
                        fastBitmap.setRGB(x, y, tRed, tGreen, tBlue);
                        fastBitmap.setRGB(x, w, r, g, b);
                        w--;
                    }
                }
            }
            if (mirrorY) {
                for (int x = 0; x < halfHeight; x++) {
                    int h = height - x - 1;
                    for (int y = 0; y < width; y++) {
                        int tRed = fastBitmap.getRed(h, y);
                        int tGreen = fastBitmap.getGreen(h, y);
                        int tBlue = fastBitmap.getBlue(h, y);
                        
                        int r = fastBitmap.getRed(x, y);
                        int g = fastBitmap.getGreen(x, y);
                        int b = fastBitmap.getBlue(x, y);
                        
                        fastBitmap.setRGB(x, y, tRed, tGreen, tBlue);
                        fastBitmap.setRGB(h, y, r, g, b);
                    }
                }
            }
        }
        else if(fastBitmap.isGrayscale()){
            if (mirrorX) {
                for (int x = 0; x < height; x++) {
                    int w = width - 1;
                    for (int y = 0; y < halfWidth; y++) {
                        
                        int tG = fastBitmap.getGray(x, w);
                        int g = fastBitmap.getGray(x, y);
                        
                        fastBitmap.setGray(x, y, tG);
                        fastBitmap.setGray(x, w, g);
                        
                        w--;
                    }
                }
            }
            if (mirrorY) {
                for (int x = 0; x < halfHeight; x++) {
                    int h = height - x - 1;
                    for (int y = 0; y < width; y++) {

                        int tG = fastBitmap.getGray(h, y);
                        int g = fastBitmap.getGray(x, y);
                        
                        fastBitmap.setGray(x, y, tG);
                        fastBitmap.setGray(h, y, g);
                    }
                }
            }
        }
    }
}