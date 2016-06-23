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
 * Bernsen Threshold.
 * 
 * <p>The method uses a user-provided contrast threshold.
 * If the local contrast (max-min) is above or equal to the contrast threshold, the threshold is set
 * at the local midgrey value (the mean of the minimum and maximum grey values in the local window).
 * If the local contrast is below the contrast threshold the neighbourhood is considered to consist only of one class
 * and the pixel is set to object or background depending on the value of the midgrey.</p>
 * 
 * <p><b>Properties:</b>
 * <li>Supported types: Grayscale.
 * <br><li>Coordinate System: Matrix.</p>
 * 
 * @author Diego Catalano
 */
public class BernsenThreshold implements IApplyInPlace{
    
    private int radius = 15;
    private double c = 15;

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
     * Get Contrast threshold.
     * @return Contrast threshold.
     */
    public double getContrastThreshold() {
        return c;
    }

    /**
     * Set Contrast threshold.
     * @param c Contrast threshold.
     */
    public void setContrastThreshold(double c) {
        this.c = Math.max(0, c);
    }

    /**
     * Initialize a new instance of the BernsenThreshold class.
     */
    public BernsenThreshold() {}
    
    /**
     * Initialize a new instance of the BernsenThreshold class.
     * @param radius Radius.
     */
    public BernsenThreshold(int radius){
        this.radius = radius;
    }

    /**
     * Initialize a new instance of the BernsenThreshold class.
     * @param radius Radius
     * @param contrastThreshold Contrast Threshold.
     */
    public BernsenThreshold(int radius, double contrastThreshold) {
        this.radius = radius;
        this.c = contrastThreshold;
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        if (fastBitmap.isGrayscale()){
            
            FastBitmap max = new FastBitmap(fastBitmap);
            FastBitmap min = new FastBitmap(fastBitmap);
            
            Maximum maximum = new Maximum(radius);
            maximum.applyInPlace(max);
            
            Minimum minimum = new Minimum(radius);
            minimum.applyInPlace(min);
            
            int size = fastBitmap.getSize();
            
            for (int i = 0; i < size; i++) {
                double localContrast = max.getGray(i) - min.getGray(i);
                double midG = (max.getGray(i) + min.getGray(i)) / 2;

                int g = fastBitmap.getGray(i);
                if (localContrast < c)
                    g = (midG >= 128) ? 255 : 0;
                else
                    g = (g >= midG) ? 255 : 0;

                fastBitmap.setGray(i, g);
            }
        }
        else{
            throw new IllegalArgumentException("Bernsen Threshold only works in grayscale images.");
        }
    }
}