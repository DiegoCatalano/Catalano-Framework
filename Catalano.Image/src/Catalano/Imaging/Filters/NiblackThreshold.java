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
import Catalano.Imaging.Filters.Integral.IntegralMean;
import Catalano.Imaging.Filters.Integral.IntegralVariance;
import Catalano.Imaging.IApplyInPlace;
import Catalano.Math.Matrix;

/**
 * Niblack Threshold.
 * @author Diego Catalano
 */
public class NiblackThreshold implements IApplyInPlace{
    
    private int radius = 15;
    private double k = 0.2D;
    private double c = 0;

    /**
     * Get Radius.
     * @return Radius.
     */
    public int getRadius() {
        return radius;
    }

    /**
     * Set Radius.
     * @param radius Radius.
     */
    public void setRadius(int radius) {
        this.radius = radius;
    }

    /**
     * Get parameter K.
     * @return K value.
     */
    public double getK() {
        return k;
    }

    /**
     * Set parameter K.
     * @param k K value.
     */
    public void setK(double k) {
        this.k = k;
    }

    /**
     * Get parameter C.
     * @return C value.
     */
    public double getC() {
        return c;
    }

    /**
     * Set parameter C.
     * @param c C value.
     */
    public void setC(double c) {
        this.c = c;
    }

    /**
     * Initialize a new instance of the NiblackThreshold class.
     */
    public NiblackThreshold() {}
    
    /**
     * Initialize a new instance of the NiblackThreshold class.
     * @param radius Radius.
     */
    public NiblackThreshold(int radius){
        this.radius = radius;
    }

    /**
     * Initialize a new instance of the NiblackThreshold class.
     * @param k Parameter K.
     * @param c Parameter C.
     */
    public NiblackThreshold(double k, double c) {
        this.k = k;
        this.c = c;
    }

    /**
     * Initialize a new instance of the NiblackThreshold class.
     * @param radius Radius.
     * @param k Parameter K.
     * @param c Parameter C.
     */
    public NiblackThreshold(int radius, double k, double c) {
        this.radius = radius;
        this.k = k;
        this.c = c;
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        if (fastBitmap.isGrayscale()){
            
            FastBitmap mean = new FastBitmap(fastBitmap);
            FastBitmap var = new FastBitmap(fastBitmap);
            
            IntegralMean im = new IntegralMean(radius);
            im.applyInPlace(mean);
            
            IntegralVariance iv = new IntegralVariance(radius);
            iv.applyInPlace(var);
            
            int size = fastBitmap.getSize();
            for (int i = 0; i < size; i++) {
                float P = fastBitmap.getGray(i);
                float mP = mean.getGray(i);
                float vP = var.getGray(i);
                int g = (P > (mP + k * Math.sqrt(vP) - c)) ? 255 : 0;

                fastBitmap.setGray(i, g);
            }
            
        }
        else{
            throw new IllegalArgumentException("Niblack Threshold only works in grayscale images.");
        }
    }
}