// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
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
import Catalano.Imaging.IApplyInPlace;

/**
 * Modified White Patch algorithm.
 * @author Diego Catalano
 */
public class ModifiedWhitePatch implements IApplyInPlace{
    
    private int redThreshold = 128;
    private int greenThreshold = 128;
    private int blueThreshold = 128;
    
    public void setThreshold(int threshold){
        this.redThreshold = threshold;
        this.greenThreshold = threshold;
        this.blueThreshold = threshold;
    }

    public int getRedThreshold() {
        return redThreshold;
    }

    public void setRedThreshold(int redThreshold) {
        this.redThreshold = redThreshold;
    }

    public int getGreenThreshold() {
        return greenThreshold;
    }

    public void setGreenThreshold(int greenThreshold) {
        this.greenThreshold = greenThreshold;
    }

    public int getBlueThreshold() {
        return blueThreshold;
    }

    public void setBlueThreshold(int blueThreshold) {
        this.blueThreshold = blueThreshold;
    }

    /**
     * Initialize a new instance of the ModifiedWhitePatch class.
     */
    public ModifiedWhitePatch() {
        this(128);
    }

    /**
     * Initialize a new instance of the ModifiedWhitePatch class.
     * @param threshold Threshold.
     */
    public ModifiedWhitePatch(int threshold) {
        this(threshold, threshold, threshold);
    }
    
    /**
     * Initialize a new instance of the ModifiedWhitePatch class.
     * @param redThreshold Red Threshold.
     * @param greenThreshold Green Threshold.
     * @param blueThreshold Blue Threshold.
     */
    public ModifiedWhitePatch(int redThreshold, int greenThreshold, int blueThreshold){
        this.redThreshold = redThreshold;
        this.greenThreshold = greenThreshold;
        this.blueThreshold = blueThreshold;
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        if(fastBitmap.isRGB()){
            
            int size = fastBitmap.getSize();
            
            double kr = 0, kg = 0, kb = 0;
            int tr = 0, tg = 0, tb = 0;
            for (int i = 0; i < size; i++) {
                if (fastBitmap.getRed(i) > redThreshold){
                    kr += fastBitmap.getRed(i);
                    tr++;
                }

                if (fastBitmap.getGreen(i) > greenThreshold){
                    kg += fastBitmap.getGreen(i);
                    tg++;
                }

                if (fastBitmap.getBlue(i) > blueThreshold){
                    kb += fastBitmap.getBlue(i);
                    tb++;
                }
            }
            
            kr = 255 / (kr/tr);
            kg = 255 / (kg/tg);
            kb = 255 / (kb/tb);
            
            for (int i = 0; i < size; i++) {
                double r = kr * fastBitmap.getRed(i);
                double g = kg * fastBitmap.getGreen(i);
                double b = kb * fastBitmap.getBlue(i);

                if (r > 255) r = 255;
                if (g > 255) g = 255;
                if (b > 255) b = 255;

                fastBitmap.setRGB(i, (int)r, (int)g, (int)b);
            }
        }
        else{
            throw new IllegalArgumentException("Modified White Patch only works in RGB images.");
        }
    }
}