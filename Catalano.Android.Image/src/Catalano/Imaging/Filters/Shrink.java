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
 * Shrink filter.
 * @author Diego Catalano
 */
public class Shrink implements IApplyInPlace{
    
    private boolean isBlackObject = false;

    /**
     * Initializes a new instance of the Shrink class.
     */
    public Shrink() {}
    
    /**
     * Initializes a new instance of the Shrink class.
     * Only works in binary images.
     * @param isBlackObject Object is black color.
     */
    public Shrink(boolean isBlackObject){
        this.isBlackObject = isBlackObject;
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        if(fastBitmap.isGrayscale()){
            
            int gray = 255;
            if(isBlackObject) gray = 0;
            
            int width = fastBitmap.getWidth();
            int height = fastBitmap.getHeight();
            
            int minHeight = height;
            int maxHeight = 0;
            int minWidth = width;
            int maxWidth = 0;
            
            int index = 0;
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if(fastBitmap.getGray(index++) == gray){
                        if(i < minHeight)
                            minHeight = i;
                        if(i > maxHeight)
                            maxHeight = i;
                        if(j < minWidth)
                            minWidth = j;
                        if(j > maxWidth)
                            maxWidth = j;
                    }
                }
            }
            
            // check
            if ( ( minHeight == height ) && ( minWidth == width ) && ( maxHeight == 0 ) && ( maxWidth == 0 ) )
            {
                minHeight = minWidth = 0;
            }
            
            Crop crop = new Crop(minHeight, minWidth, maxWidth-minWidth+1, maxHeight-minHeight+1);
            crop.ApplyInPlace(fastBitmap);
        }
        else if(fastBitmap.isRGB()){
            
            int width = fastBitmap.getWidth();
            int height = fastBitmap.getHeight();
            
            int minHeight = height;
            int maxHeight = 0;
            int minWidth = width;
            int maxWidth = 0;
            
            int index = 0;
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if(fastBitmap.getRed(index) != 0 && fastBitmap.getGreen(index) != 0 && fastBitmap.getBlue(index) != 0){
                        if(i < minHeight)
                            minHeight = i;
                        if(i > maxHeight)
                            maxHeight = i;
                        if(j < minWidth)
                            minWidth = j;
                        if(j > maxWidth)
                            maxWidth = j;
                    }
                    index++;
                }
            }
            
            Crop crop = new Crop(minHeight, minWidth, maxWidth-minWidth+1, maxHeight-minHeight+1);
            crop.ApplyInPlace(fastBitmap);
        }
    }
}