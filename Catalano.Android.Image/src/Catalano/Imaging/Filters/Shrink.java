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

    /**
     * Initializes a new instance of the Shrink class.
     */
    public Shrink() {}

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        if(fastBitmap.isGrayscale()){
            int width = fastBitmap.getWidth();
            int height = fastBitmap.getHeight();
            
            int minHeight = 0;
            int maxHeight = 0;
            int minWidth = 0;
            int maxWidth = 0;
            
            boolean found = false;
            
            //Minimum height
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if(fastBitmap.getGray(i*width+j) != 0){
                        minHeight = i;
                        found = true;
                        break;
                    }
                }
                if (found) break;
            }
            
            found = false;
            
            //Maximum height
            for (int i = height - 1; i >= 0; i--) {
                for (int j = 0; j < width; j++) {
                    if(fastBitmap.getGray(i*width+j) != 0){
                        maxHeight = i;
                        found = true;
                        break;
                    }
                }
                if (found) break;
            }
            
            found = false;
            
            //Minimum width
            for (int j = 0; j < height; j++) {
                for (int i = 0; i < width; i++) {
                    if(fastBitmap.getGray(i*width+j) != 0){
                        minWidth = j;
                        found = true;
                        break;
                    }
                }
                if (found) break;
            }
            
            found = false;
            
            //Maximum width
            for (int j = width - 1; j >= 0; j--) {
                for (int i = 0; i < width; i++) {
                    if(fastBitmap.getGray(i*width+j) != 0){
                        maxWidth = j;
                        found = true;
                        break;
                    }
                }
                if (found) break;
            }
            
            Crop crop = new Crop(minHeight, minWidth, maxWidth-minWidth+1, maxHeight-minHeight+1);
            crop.ApplyInPlace(fastBitmap);
        }
        else if(fastBitmap.isRGB()){
            
            int width = fastBitmap.getWidth();
            int height = fastBitmap.getHeight();
            
            int minHeight = 0;
            int maxHeight = 0;
            int minWidth = 0;
            int maxWidth = 0;
            
            boolean found = false;
            
            //Minimum height
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if(fastBitmap.getRed(i*width+j) != 0 && fastBitmap.getGreen(i*width+j) != 0 && fastBitmap.getBlue(i*width+j) != 0){
                        minHeight = i;
                        found = true;
                        break;
                    }
                }
                if (found) break;
            }
            
            found = false;
            
            //Maximum height
            for (int i = height - 1; i >= 0; i--) {
                for (int j = 0; j < width; j++) {
                    if(fastBitmap.getRed(i*width+j) != 0 && fastBitmap.getGreen(i*width+j) != 0 && fastBitmap.getBlue(i*width+j) != 0){
                        maxHeight = i;
                        found = true;
                        break;
                    }
                }
                if (found) break;
            }
            
            found = false;
            
            //Minimum width
            for (int j = 0; j < height; j++) {
                for (int i = 0; i < width; i++) {
                    if(fastBitmap.getRed(i*width+j) != 0 && fastBitmap.getGreen(i*width+j) != 0 && fastBitmap.getBlue(i*width+j) != 0){
                        minWidth = j;
                        found = true;
                        break;
                    }
                }
                if (found) break;
            }
            
            found = false;
            
            //Maximum width
            for (int j = width - 1; j >= 0; j--) {
                for (int i = 0; i < width; i++) {
                    if(fastBitmap.getRed(i*width+j) != 0 && fastBitmap.getGreen(i*width+j) != 0 && fastBitmap.getBlue(i*width+j) != 0){
                        maxWidth = j;
                        found = true;
                        break;
                    }
                }
                if (found) break;
            }
            
            Crop crop = new Crop(minHeight, minWidth, maxWidth-minWidth+1, maxHeight-minHeight+1);
            crop.ApplyInPlace(fastBitmap);
        }
    }
}