// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2014
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
import Catalano.Imaging.IBaseInPlace;

/**
 * Wolf Threshold.
 * References: http://liris.cnrs.fr/christian.wolf/papers/icpr2002v.pdf
 * @author Diego Catalano
 */
public class WolfJoulionThreshold implements IBaseInPlace{
    
    private Mean.Arithmetic arithmetic = Mean.Arithmetic.Mean;
    private int radius = 15;
    private double k = 0.5D;
    private double r = 128;

    /**
     * Get Mean arithmetic.
     * @return Mean arithmetic.
     */
    public Mean.Arithmetic getArithmetic() {
        return arithmetic;
    }

    /**
     * Set Mean arithmetic.
     * @param arithmetic Mean arithmetic.
     */
    public void setArithmetic(Mean.Arithmetic arithmetic) {
        this.arithmetic = arithmetic;
    }

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
     * Get parameter R.
     * @return R value.
     */
    public double getR() {
        return r;
    }

    /**
     * Set parameter R.
     * @param r R value.
     */
    public void setR(double r) {
        this.r = r;
    }

    /**
     * Initialize a new instance of the WolfThreshold class.
     */
    public WolfJoulionThreshold() {}
    
    /**
     * Initialize a new instance of the WolfThreshold class.
     * @param radius Radius.
     */
    public WolfJoulionThreshold(int radius){
        this.radius = radius;
    }

    /**
     * Initialize a new instance of the WolfThreshold class.
     * @param k Parameter K.
     * @param r Parameter R.
     */
    public WolfJoulionThreshold(double k, double r) {
        this.k = k;
        this.r = r;
    }

    /**
     * Initialize a new instance of the WolfThreshold class.
     * @param radius Radius.
     * @param k Parameter K.
     * @param r Parameter R.
     */
    public WolfJoulionThreshold(int radius, double k, double r) {
        this.radius = radius;
        this.k = k;
        this.r = r;
    }
    
    /**
     * Initialize a new instance of the WolfThreshold class.
     * @param radius Radius.
     * @param k Parameter K.
     * @param r Parameter R.
     * @param arithmetic Mean arithmetic.
     */
    public WolfJoulionThreshold(int radius, double k, double r, Mean.Arithmetic arithmetic) {
        this.radius = radius;
        this.k = k;
        this.r = r;
        this.arithmetic = arithmetic;
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        if (fastBitmap.isGrayscale()){
            
            int width = fastBitmap.getWidth();
            int height = fastBitmap.getHeight();
            
            FastBitmap mean = new FastBitmap(fastBitmap);
            FastBitmap var = new FastBitmap(fastBitmap);
            
            Mean m = new Mean(radius, arithmetic);
            m.applyInPlace(mean);
            
            Variance v = new Variance(radius);
            v.applyInPlace(var);
            
            int maxV = 0;
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    int g = var.getGray(i, j);
                    if (g > maxV) maxV = g;
                }
            }
            
            int minG = 255;
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    int g = fastBitmap.getGray(i, j);
                    if (g < minG) minG = g;
                }
            }
            
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    double P = fastBitmap.getGray(i, j);
                    double mP = mean.getGray(i, j);
                    double vP = var.getGray(i, j);
                    
                    int g = (P > (mP + k * ((Math.sqrt(vP) / (double)maxV - 1.0) * (mP - (double)minG)))) ? 255 : 0;
                    
                    fastBitmap.setGray(i, j, g);
                }
            }
            
        }
        else{
            throw new IllegalArgumentException("Wolf Threshold only works in grayscale images.");
        }
    }
}