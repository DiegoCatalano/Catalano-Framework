// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2014
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
import Catalano.Imaging.IBaseInPlace;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The filter does image binarization using specified threshold value. All pixels with intensities equal or higher than threshold value are converted to white pixels. All other pixels with intensities below threshold value are converted to black pixels.
 * @author Diego Catalano
 */
public class Threshold implements IBaseInPlace{

    private int value = 128;
    private boolean invert = false;
    
    /**
     * Initialize a new instance of the Threshold class.
     */
    public Threshold() {}
    
    /**
     * Initialize a new instance of the Threshold class.
     * @param value Threshold value.
     */
    public Threshold(int value){
        this.value = value;
    }
    
    /**
     * Initialize a new instance of the Threshold class.
     * @param value Threshold value.
     * @param invert All pixels with intensities equal or higher than threshold value are converted to black pixels. All other pixels with intensities below threshold value are converted to white pixels.
     */
    public Threshold(int value, boolean invert){
        this.value = value;
        this.invert = invert;
    }

    /**
     * Threshold value.
     * @return Threshold value.
     */
    public int getValue() {
        return value;
    }

    /**
     * Threshold value.
     * @param value Threshold value.
     */
    public void setValue(int value) {
        this.value = value;
    }
    
    @Override
    public void applyInPlace(FastBitmap fastBitmap){
        
        if (!fastBitmap.isGrayscale()){
            try {
                throw new Exception("Binarization works only with TYPE_BYTE_GRAY");
            } catch (Exception ex) {
                Logger.getLogger(Threshold.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        int width = fastBitmap.getWidth();
        int height = fastBitmap.getHeight();
        int l;
        
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                l = fastBitmap.getGray(x, y);
                if(invert == false){
                    if(l >= value){
                        fastBitmap.setGray(x, y, 255);
                    }
                    else{
                        fastBitmap.setGray(x, y, 0);
                    }
                }
                else{
                    if(l < value){
                        fastBitmap.setGray(x, y, 0);
                    }
                    else{
                        fastBitmap.setGray(x, y, 255);
                    }
                }
            }
        }
    }
}