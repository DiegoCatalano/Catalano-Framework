// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
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
 * Roberts Cross Edge Detector.
 * References: http://en.wikipedia.org/wiki/Roberts_cross
 * @author Diego Catalano
 */
public class RobertsCrossEdgeDetector implements IApplyInPlace{

    /**
     * Initialize a new instance of the RobertsCrossEdgeDetector class.
     */
    public RobertsCrossEdgeDetector() {}

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        if (fastBitmap.isGrayscale()){
            
            int width = fastBitmap.getWidth();
            int height = fastBitmap.getHeight();
            
            FastBitmap image = new FastBitmap(width, height, FastBitmap.ColorSpace.Grayscale);
            
            for (int i = 1; i < height - 1; i++) {
                for (int j = 1; j < width - 1; j++) {
                    
                    int p1 = fastBitmap.getGray(i - 1, j - 1);
                    int p2 = fastBitmap.getGray(i - 1, j);
                    int p3 = fastBitmap.getGray(i, j - 1);
                    int p4 = fastBitmap.getGray(i, j);
                    
                    int g = Math.abs(p1 - p4) + Math.abs(p2 - p3);
                    image.setGray(i, j, g);
                }
            }
            fastBitmap.setImage(image);
        }
        else{
            throw new IllegalArgumentException("Roberts Cross Edge Detector only works with grayscale images.");
        }
    }
}