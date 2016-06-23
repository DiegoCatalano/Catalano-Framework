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

package Catalano.Imaging.Concurrent.Filters;

import Catalano.Imaging.Concurrent.Share;
import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.IApplyInPlace;

/**
 * Desaturation.
 * @author Diego Catalano
 */
public class Desaturation implements IApplyInPlace{
    
    private double saturationFactor = 0.2D;

    /**
     * Get Saturation factor.
     * @return Saturation factor.
     */
    public double getSaturationFactor() {
        return saturationFactor;
    }

    /**
     * Set Saturation factor.
     * @param saturationFactor Saturation factor.
     */
    public void setSaturationFactor(double saturationFactor) {
        this.saturationFactor = Math.min(1, Math.max(0, saturationFactor));
    }

    /**
     * Initialize a new instance of the Desaturation class.
     */
    public Desaturation() {}

    /**
     * Initialize a new instance of the Desaturation class.
     * @param saturationFactor Saturation factor.
     */
    public Desaturation(double saturationFactor) {
        setSaturationFactor(saturationFactor);
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        if (fastBitmap.isRGB()){
            Parallel(fastBitmap);
        }
        else{
            throw new IllegalArgumentException("Desaturation only works in RGB space color.");
        }
    }
    
    private void Parallel(FastBitmap fastBitmap){
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
    
    private class Run implements Runnable {

        private Share share;
        
        public Run(Share obj) {
            this.share = obj;
        }

        @Override
        public void run() {
            
            for (int i = share.startX; i < share.endHeight; i++) {
                for (int j = 0; j < share.fastBitmap.getWidth(); j++) {
                    double r = share.fastBitmap.getRed(i, j);
                    double g = share.fastBitmap.getGreen(i, j);
                    double b = share.fastBitmap.getBlue(i, j);
                    
                    double luminance = 0.2125D * r + 0.7154 * g + 0.0721 * b;
                    
                    double nr = (luminance + saturationFactor * (r - luminance));
                    double ng = (luminance + saturationFactor * (g - luminance));
                    double nb = (luminance + saturationFactor * (b - luminance));
                    
                    share.fastBitmap.setRGB(i, j, (int)nr, (int)ng, (int)nb);
                }
            }
        }
    }
}