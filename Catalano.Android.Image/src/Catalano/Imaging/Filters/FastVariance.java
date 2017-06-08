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
 * Fast Variance filter.
 * @author Diego Catalano
 */
public class FastVariance implements IApplyInPlace{
    
    private int radius = 2;

    /**
     * Get radius.
     * @return Radius.
     */
    public int getRadius() {
        return radius;
    }

    /**
     * Set radius.
     * @param radius Radius.
     */
    public void setRadius(int radius) {
        this.radius = Math.max(1, radius);
    }

    /**
     * Initialize a new instance of the FastVariance class.
     */
    public FastVariance() {}

    /**
     * Initialize a new instance of the FastVariance class.
     * @param radius Radius.
     */
    public FastVariance(int radius) {
        setRadius(radius);
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        int width = fastBitmap.getWidth();
        int height = fastBitmap.getHeight();
        FastBitmap copy = new FastBitmap(fastBitmap);
        if (fastBitmap.isGrayscale()){
            
            for (int x = 0; x < height; x++) {
                for (int y = 0; y < width; y++) {
                    int n = 0;
                    double sum = 0;
                    double sum_sq = 0;
                    for (int i = x - radius; i <= x + radius; i++) {
                        for (int j = y - radius; j <= y + radius; j++) {
                            if (i >= 0 && i < height && j >= 0 && j < width){
                                n++;
                                double val = copy.getGray(i, j);
                                sum += val;
                                sum_sq += val*val;
                                
                            }
                        }
                    }
                    double mean = sum / n;
                    double var = sum_sq / n - mean * mean;
                    if (var < 0) var = 0;
                    if (var > 255) var = 255;
                    fastBitmap.setGray(x, y, (int)var);
                }
            }
            
        }
        if (fastBitmap.isRGB()){
            
            for (int x = 0; x < height; x++) {
                for (int y = 0; y < width; y++) {
                    int n = 0;
                    double sumR = 0, sumG = 0, sumB = 0;
                    double sq_sumR = 0, sq_sumG = 0, sq_sumB = 0;
                    for (int i = x - radius; i <= x + radius; i++) {
                        for (int j = y - radius; j <= y + radius; j++) {
                            if (i >= 0 && i < height && j >= 0 && j < width){
                                n++;
                                double r = copy.getRed(i, j);
                                double g = copy.getGreen(i, j);
                                double b = copy.getBlue(i, j);
                                
                                sumR += r;
                                sumG += g;
                                sumB += b;
                                
                                sq_sumR += r*r;
                                sq_sumG += g*g;
                                sq_sumB += b*b;
                            }
                        }
                    }
                    
                    double meanR = sumR / n;
                    double meanG = sumG / n;
                    double meanB = sumB / n;
                    
                    double varR = sq_sumR / n - meanR * meanR;
                    double varG = sq_sumG / n - meanG * meanG;
                    double varB = sq_sumB / n - meanB * meanB;
                    
                    if (varR < 0) varR = 0;
                    if (varG < 0) varG = 0;
                    if (varB < 0) varB = 0;
                    
                    if (varR > 255) varR = 255;
                    if (varG > 255) varG = 255;
                    if (varB > 255) varB = 255;
                    
                    fastBitmap.setRGB(x, y, (int)varR, (int)varG, (int)varB);
                }
            }
        }
    }
}