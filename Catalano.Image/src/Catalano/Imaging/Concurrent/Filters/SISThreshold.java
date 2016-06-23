// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
// Copyright © Andrew Kirillov, 2007-2008
// andrew.kirillov at gmail.com
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

package Catalano.Imaging.Concurrent.Filters;

import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.IApplyInPlace;

/**
 * Threshold using Simple Image Statistics (SIS).
 * @author Diego Catalano
 */
public class SISThreshold implements IApplyInPlace{

    /**
     * Initialize a new instance of the SISThreshold class.
     */
    public SISThreshold() {}

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        int val = CalculateThreshold(fastBitmap);
        
        Threshold t = new Threshold(val);
        t.applyInPlace(fastBitmap);
        
    }
    
    /**
     * Calculate binarization threshold for the given image.
     * @param fastBitmap Image to calculate binarization threshold for.
     * @return Returns binarization threshold.
     */
    public int CalculateThreshold(FastBitmap fastBitmap){
        
        if (fastBitmap.isGrayscale()){
            
            double ei, ej, weight, weightTotal = 0, total = 0;
            
            int width = fastBitmap.getWidth() - 1;
            int height = fastBitmap.getHeight() - 1;
            
            for (int i = 1; i < height; i++) {
                for (int j = 1; j < width; j++) {
                    
                    ei = Math.abs(fastBitmap.getGray(i+1, j) - fastBitmap.getGray(i-1, j));
                    ej = Math.abs(fastBitmap.getGray(i, j+1) - fastBitmap.getGray(i, j-1));
                    
                    weight = ei > ej ? ei : ej;
                    weightTotal += weight;
                    total += weight * fastBitmap.getGray(i, j);
                    
                }
            }
            
            return ( weightTotal == 0 ) ? 0 : (int)( total / weightTotal );
            
        }
        else{
            throw new IllegalArgumentException("SIS threshold only works in grayscale images.");
        }
    }
}