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
 * Adaptive Contrast Enhancement is modification of the gray level values based on some criterion that adjusts its parameters as local image characteristics change.
 * 
 * <p><b>Filter supports:</b>
 * <li>Images: Grayscale.
 * <br><li>Coordinate System: Matrix.</p>
 * 
 * @author Diego Catalano
 */
public class AdaptiveContrastEnhancement implements IApplyInPlace {
    
    int windowSize;
    double k1, k2, maxGain, minGain;

    /**
     * Initialize a new instance of the AdaptiveContrastEnhancement class.
     * @param windowSize Size of window(should be an odd number).
     * @param k1 Local gain factor, between 0 and 1.
     * @param k2 Local mean constant, between 0 and 1.
     * @param minGain The minimum gain factor.
     * @param maxGain The maximum gain factor.
     */
    public AdaptiveContrastEnhancement(int windowSize, double k1, double k2, double minGain, double maxGain) {
        this.windowSize = windowSize;
        this.k1 = k1;
        this.k2 = k2;
        this.minGain = minGain;
        this.maxGain = maxGain;
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        int width = fastBitmap.getWidth();
        int height = fastBitmap.getHeight();
        int lines = CalcLines(windowSize);
        
        FastBitmap copy = new FastBitmap(fastBitmap);
        
        if (fastBitmap.isGrayscale()) {
            
            // the mean (average) for the entire image I(x,y);
            double mean = getMean(fastBitmap);
            
            for (int x = 0; x < height; x++) {
                for (int y = 0; y < width; y++) {
                   
                    int hits = 0;
                    int windowSize2 = windowSize * windowSize;
                    int[] values = new int[windowSize2];
                    
                    double sumMean = 0;
                    double sumVar = 0;
                    double factor;
                    
                    for (int i = x - lines; i <= x + lines; i++) {
                        for (int j = y - lines; j <= y + lines; j++) {
                            
                            if ((i >= 0) && (i < height) && (j >=0) && (j < width)) {
                                values[hits] = copy.getGray(i, j);
                                //sumGray += values[hits];
                                sumMean += values[hits];
                                sumVar += values[hits] * values[hits];
                                hits++;
                            }
                        }
                    }
                    
                    sumMean /= windowSize2;
                    sumVar /= windowSize2;
                    sumVar -= sumMean * sumMean;
                    
                    if (sumVar != 0)
                        factor = k1 * (mean / sumVar);
                    else 
                        factor = maxGain;
                    
                    if (factor > maxGain) factor = maxGain;
                    if (factor < minGain) factor = minGain;
                    
                    double gray = factor * (copy.getGray(x, y) - sumMean) + k2 * sumMean;
                    fastBitmap.setGray(x, y, (int)gray);
                    
                }
            }
        }
        else{
            try {
                throw new IllegalArgumentException("AdaptiveContrastEnhancement works only with grayscale.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    private double getMean(FastBitmap fastBitmap){
        
        int sum = 0;
        for (int i = 0; i < fastBitmap.getHeight(); i++) {
            for (int j = 0; j < fastBitmap.getWidth(); j++) {
                sum += fastBitmap.getGray(i, j);
            }
        }
        
        return sum / (fastBitmap.getWidth() * fastBitmap.getHeight());
    }
    
    private int CalcLines(int windowSize){
        return (windowSize - 1) / 2;
    }
    
}