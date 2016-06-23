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
 * Threshold.
 * The filter does image binarization using specified threshold value. All pixels with intensities equal or higher than threshold value are converted to white pixels. All other pixels with intensities below threshold value are converted to black pixels.
 * @author Diego Catalano
 */
public class Threshold implements IApplyInPlace{
    
    private int threshold = 128;

    /**
     * Get Threshold value.
     * @return Value.
     */
    public int getValue() {
        return threshold;
    }

    /**
     * Set Threshold value.
     * @param threshold Value.
     */
    public void setValue(int threshold) {
        this.threshold = threshold;
    }
    
    /**
     * Initialize a new instance of the Threshold class.
     */
    public Threshold() {}

    /**
     * Initialize a new instance of the Threshold class.
     * @param threshold Value.
     */
    public Threshold(int threshold) {
        this.threshold = threshold;
    }
    
    @Override
    public void applyInPlace(FastBitmap fb){
        
        if(fb.isGrayscale()){
            Parallel(fb);
        }
        else{
            throw new IllegalArgumentException("Threshold only works in grayscale images.");
        }
    }
    
    private void Parallel(FastBitmap fastBitmap){
        
        int processors = Runtime.getRuntime().availableProcessors();
        
        Thread[] t = new Thread[processors];
        int part = fastBitmap.getHeight() / processors;

        int startX = 0;
        for (int i = 0; i < processors; i++) {
            t[i] = new Thread(new Run(new Share(fastBitmap, startX, startX + part)));
            t[i].start();
            startX += part;
        }

        try {
            for (int i = 0; i < processors; i++) {
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
                    
                    int x = share.fastBitmap.getGray(i, j);
                    if (x < threshold){
                        share.fastBitmap.setGray(i, j, 0);
                    }
                    else{
                        share.fastBitmap.setGray(i, j, 255);
                    }
                }
            }
        }
    }
}