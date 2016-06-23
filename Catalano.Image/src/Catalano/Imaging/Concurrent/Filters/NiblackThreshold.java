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

package Catalano.Imaging.Concurrent.Filters;

import Catalano.Imaging.Concurrent.Share;
import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.IApplyInPlace;

/**
 * Niblack Threshold.
 * @author Diego Catalano
 */
public class NiblackThreshold implements IApplyInPlace{
    
    private Mean.Arithmetic arithmetic = Mean.Arithmetic.Mean;
    private int radius = 15;
    private double k = 0.2D;
    private double c = 0;
    
    private FastBitmap mean;
    private FastBitmap var;

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
    
    /**
     * Initialize a new instance of the NiblackThreshold class.
     * @param radius Radius.
     * @param k Parameter K.
     * @param c Parameter C.
     * @param arithmetic Mean arithmetic.
     */
    public NiblackThreshold(int radius, double k, double c, Mean.Arithmetic arithmetic) {
        this.radius = radius;
        this.k = k;
        this.c = c;
        this.arithmetic = arithmetic;
    }
    
    @Override
    public void applyInPlace(FastBitmap fastBitmap){
        
        if (fastBitmap.isGrayscale()){
            mean = new FastBitmap(fastBitmap);
            var = new FastBitmap(fastBitmap);

            Mean m = new Mean(radius, arithmetic);
            m.applyInPlace(mean);

            FastVariance v = new FastVariance(radius);
            v.applyInPlace(var);
            
            Parallel(fastBitmap);
        }
        else{
            throw new IllegalArgumentException("Niblack threshold only works in grayscale images.");
        }
        
    }
    
    private void Parallel(FastBitmap fastBitmap){
        int cores = Runtime.getRuntime().availableProcessors();
        
        Thread[] t = new Thread[cores];
        int part = fastBitmap.getHeight() / cores;
        int lastC = cores - 1;
        
        int startX = 0;
        for (int i = 0; i < cores; i++) {
            if (lastC == i) part = fastBitmap.getHeight() - startX;
            t[i] = new Thread(new Run(new Share(fastBitmap, startX, startX + part)));
            t[i].start();
            startX += part;
        }
        
        try {
            
            for (int i = 0; i < cores; i++) {
                t[i].join();
            }
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    private class Run implements Runnable {

        private Share share;
        
        public Run(Share obj) {
            this.share = obj;
        }

        @Override
        public void run() {
            for (int i = share.startX; i < share.endHeight; i++) {
                for (int j = 0; j < share.fastBitmap.getWidth(); j++) {
                    float P = share.fastBitmap.getGray(i, j);
                    float mP = mean.getGray(i, j);
                    float vP = var.getGray(i, j);
                    int g = (P > (mP + k * Math.sqrt(vP) - c)) ? 255 : 0;
                    
                    share.fastBitmap.setGray(i, j, g);
                }
            }
        }
    }
}