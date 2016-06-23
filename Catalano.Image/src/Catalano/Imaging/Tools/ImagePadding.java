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

package Catalano.Imaging.Tools;

import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.IApplyInPlace;

/**
 * Image padding.
 * Fill the image with zero. Zero padding technique.
 * @author Diego Catalano
 */
public class ImagePadding implements IApplyInPlace{
    
    private int w = 1;
    private int h = 1;

    /**
     * Get Width of expand value.
     * @return Width.
     */
    public int getWidth() {
        return w;
    }

    /**
     * Set Width of expand value.
     * @param width Width.
     */
    public void setWidth(int width) {
        this.w = width;
    }

    /**
     * Get Height of expand value.
     * @return Height.
     */
    public int getHeight() {
        return h;
    }

    /**
     * Set Height of expand value.
     * @param height Height.
     */
    public void setHeight(int height) {
        this.h = height;
    }
    
    /**
     * Set Width and Height values.
     * @param size Size.
     */
    public void setSize(int size){
        this.w = this.h = size;
    }
    
    /**
     * Initialize a new instance of the ImagePadding class.
     */
    public ImagePadding() {}

    /**
     * Initialize a new instance of the ImagePadding class.
     * @param size Size.
     */
    public ImagePadding(int size) {
        this.w = size;
        this.h = size;
    }
    
    /**
     * Initialize a new instance of the ImagePadding class.
     * @param width Expand width value.
     * @param height Expand height value.
     */
    public ImagePadding(int width, int height){
        this.w = width;
        this.h = height;
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        int width = fastBitmap.getWidth();
        int height = fastBitmap.getHeight();
        
        int newWidth = width + w;
        int newHeight = height + h;
        
        int startI = (newHeight - height) / 2;
        int startJ = (newWidth - width) / 2;
        
        int moveI = 0;
        int moveJ = 0;
        
        if (h % 2 != 0){
            moveI = 1;
        }
        if (w % 2 != 0){
            moveJ = 1;
        }
        
        FastBitmap fb = new FastBitmap(newWidth, newHeight, fastBitmap.getColorSpace());
        
        if (fb.isGrayscale()){
            for (int i = startI; i < newHeight - startI - moveI; i++) {
                for (int j = startJ; j < newWidth - startJ - moveJ; j++) {
                    fb.setGray(i, j, fastBitmap.getGray(i - startI, j - startJ));
                }
            }
        }
        if (fb.isRGB()){
            for (int i = startI; i < newHeight - startI - moveI; i++) {
                for (int j = startJ; j < newWidth - startJ - moveJ; j++) {
                    fb.setRGB(i, j, fastBitmap.getRed(i - startI, j - startJ), fastBitmap.getGreen(i - startI, j - startJ), fastBitmap.getBlue(i - startI, j - startJ));
                }
            }
        }
        
        fastBitmap.setImage(fb);
    }
}