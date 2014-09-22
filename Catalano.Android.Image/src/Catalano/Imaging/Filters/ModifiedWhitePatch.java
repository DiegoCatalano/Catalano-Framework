// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2014
// diego.catalano at live.com
//
// Copyright © Juan Manuel Perez Rua, 2013
// juanmanpr at gmail.com
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
 * Modified White Patch algorithm.
 * @author Diego Catalano
 */
public class ModifiedWhitePatch implements IBaseInPlace{
    
    int threshold = 128;

    /**
     * Initialize a new instance of the ModifiedWhitePatch class.
     */
    public ModifiedWhitePatch() {}

    /**
     * Initialize a new instance of the ModifiedWhitePatch class.
     * @param threshold Threshold.
     */
    public ModifiedWhitePatch(int threshold) {
        this.threshold = threshold;
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        if(fastBitmap.isRGB()){
            
            int width = fastBitmap.getWidth();
            int height = fastBitmap.getHeight();
            
            double kr = 0, kg = 0, kb = 0;
            int tr = 0, tg = 0, tb = 0;
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    
                    if (fastBitmap.getRed(i, j) > threshold){
                        kr += fastBitmap.getRed(i, j);
                        tr++;
                    }
                    
                    if (fastBitmap.getGreen(i, j) > threshold){
                        kg += fastBitmap.getGreen(i, j);
                        tg++;
                    }
                    
                    if (fastBitmap.getBlue(i, j) > threshold){
                        kb += fastBitmap.getBlue(i, j);
                        tb++;
                    }
                }
            }
            
            kr = 255 / (kr/tr);
            kg = 255 / (kg/tg);
            kb = 255 / (kb/tb);
            
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    double r = kr * fastBitmap.getRed(i, j);
                    double g = kg * fastBitmap.getGreen(i, j);
                    double b = kb * fastBitmap.getBlue(i, j);
                    
                    if (r > 255) r = 255;
                    if (g > 255) g = 255;
                    if (b > 255) b = 255;
                    
                    fastBitmap.setRGB(i, j, (int)r, (int)g, (int)b);
                }
            }
        }
        else{
            throw new IllegalArgumentException("Modified White Patch only works in RGB images.");
        }
    }
}