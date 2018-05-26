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
 * Also called Gray level reduction.
 * Image quantization is the process of reducing the image data by removing some of the detail information
 * by mapping groups of data points to a single point.
 * @author Diego Catalano
 */
public class ImageQuantization implements IApplyInPlace{
    
    private int level = 16;

    /**
     * Initialize a new instance of the ImageQuantization class.
     */
    public ImageQuantization() {}
    
    /**
     * Initialize a new instance of the ImageQuantization class.
     * @param level 
     */
    public ImageQuantization(int level){
        this.level = Math.min(level, 256);
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        if (fastBitmap.isGrayscale()) {
            
            int maxG = 0;
            int size = fastBitmap.getSize();
            for (int i = 0; i < size; i++) {
                maxG = Math.max(maxG, fastBitmap.getGray(i));
            }
            
            int div = maxG / (level - 1);
            for (int i = 0; i < size; i++) {
                
                int g = fastBitmap.getGray(i) / div * div;
                g = g > 255 ? 255 : g;
                g = g < 0 ? 0 : g;
                
                fastBitmap.setGray(i, g);
            }
            
        }
        else if(fastBitmap.isRGB()){
            
            int maxR, maxG, maxB;
            maxR = maxG = maxB = 0;
            int size = fastBitmap.getSize();
            for (int i = 0; i < size; i++) {
                maxR = Math.max(maxR, fastBitmap.getRed(i));
                maxG = Math.max(maxG, fastBitmap.getRed(i));
                maxB = Math.max(maxB, fastBitmap.getRed(i));
            }
            
            int divR = maxR / (level - 1);
            int divG = maxG / (level - 1);
            int divB = maxB / (level - 1);
            for (int i = 0; i < size; i++) {
                
                int r = fastBitmap.getRed(i) / divR * divR;
                int g = fastBitmap.getGreen(i) / divG * divG;
                int b = fastBitmap.getBlue(i) / divB * divB;
                
                r = r > 255 ? 255 : r;
                r = r < 0 ? 0 : r;
                
                g = g > 255 ? 255 : g;
                g = g < 0 ? 0 : g;
                
                
                b = b > 255 ? 255 : b;
                b = b < 0 ? 0 : b;
                
                fastBitmap.setRGB(i, r, g, b);
            }
            
        }
    }
}