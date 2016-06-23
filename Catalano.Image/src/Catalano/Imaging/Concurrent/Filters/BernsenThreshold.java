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
 * Bernsen Threshold.
 * 
 * <para>The method uses a user-provided contrast threshold.
 * If the local contrast (max-min) is above or equal to the contrast threshold, the threshold is set
 * at the local midgrey value (the mean of the minimum and maximum grey values in the local window).
 * If the local contrast is below the contrast threshold the neighbourhood is considered to consist only of one class
 * and the pixel is set to object or background depending on the value of the midgrey.</para>
 * 
 * @author Diego Catalano
 */
public class BernsenThreshold implements IApplyInPlace{
    
    private int radius = 15;
    private double c = 15;
    private FastBitmap max;
    private FastBitmap min;

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
        if (fastBitmap.isGrayscale())
            Parallel(fastBitmap);
        else
            throw new IllegalArgumentException("Bernsen Threshold only work in grayscale images.");
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
                    
                    double localContrast = max.getGray(i, j) - min.getGray(i, j);
                    double midG = (max.getGray(i, j) + min.getGray(i, j)) / 2;
                    
                    int g = share.fastBitmap.getGray(i, j);
                    if (localContrast < c)
                        g = (midG >= 128) ? 255 : 0;
                    else
                        g = (g >= midG) ? 255 : 0;
                    
                    share.fastBitmap.setGray(i, j, g);
                }
            }
        }
    }

    private void Parallel(FastBitmap fastBitmap) {
        
        max = new FastBitmap(fastBitmap);
        min = new FastBitmap(fastBitmap);
        
        Maximum m = new Maximum(radius);
        m.applyInPlace(max);
        
        Minimum mm = new Minimum(radius);
        mm.applyInPlace(min);
        
        int cores = Runtime.getRuntime().availableProcessors();
        
        Thread[] t = new Thread[cores];
        int part = fastBitmap.getHeight() / cores;
        
        int startX = 0;
        for (int i = 0; i < cores; i++) {
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
}