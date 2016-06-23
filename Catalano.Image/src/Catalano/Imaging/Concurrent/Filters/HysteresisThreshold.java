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
 * Hysteresis Threshold.
 * 
 * <para>Hysteresis thresholding for edge detection using two thresholds.
 * Low thresholded edges which are connected to high thresholded edges are retained.
 * Low thresholded edges which are non connected to high thresholded edges are removed.</para>
 * @author Diego Catalano
 */
public class HysteresisThreshold implements IApplyInPlace{
    
    int lowThreshold = 20;
    int highThreshold = 100;

    /**
     * Get Low threshold.
     * @return Threshold value.
     */
    public int getLowThreshold() {
        return lowThreshold;
    }

    /**
     * Set Low threshold.
     * @param lowThreshold Threshold value.
     */
    public void setLowThreshold(int lowThreshold) {
        this.lowThreshold = lowThreshold;
    }

    /**
     * Get High threshold.
     * @return Threshold value.
     */
    public int getHighThreshold() {
        return highThreshold;
    }

    /**
     * Set High threshold.
     * @param highThreshold Threshold value.
     */
    public void setHighThreshold(int highThreshold) {
        this.highThreshold = highThreshold;
    }

    /**
     * Initialize a new instance of the HysteresisThreshold class.
     */
    public HysteresisThreshold() {}

    /**
     * Initialize a new instance of the HysteresisThreshold class.
     * @param lowThreshold Low threshold.
     * @param highThreshold High threshold.
     */
    public HysteresisThreshold(int lowThreshold, int highThreshold) {
        this.lowThreshold = lowThreshold;
        this.highThreshold = highThreshold;
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        if (fastBitmap.isGrayscale()){
            Parallel(fastBitmap);
        }
        else{
            throw new IllegalArgumentException("Hysteresis Threshold only works with grayscale images.");
        }
    }
    
    private void Parallel(FastBitmap fastBitmap){
        
        int processors = Runtime.getRuntime().availableProcessors();
        
        Thread[] t = new Thread[processors];
        int part = fastBitmap.getHeight() / processors;
        int last = processors - 1;

        int startX = 1;
        for (int i = 0; i < processors; i++){
            if (last == i) part = fastBitmap.getHeight() - startX - 1;
            t[i] = new Thread(new Run(new Share(fastBitmap, startX, 1, fastBitmap.getWidth() - 1, startX + part)));
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
                for (int j = share.startY; j < share.endWidth; j++) {
                    if ( share.fastBitmap.getGray(i, j) < highThreshold ){
                        if ( share.fastBitmap.getGray(i, j) < lowThreshold ){
                            // non edge
                            share.fastBitmap.setGray(i, j, 0);
                        }
                        else{
                            
                            // check 8 neighboring pixels
                            if (( share.fastBitmap.getGray(i, j - 1) < highThreshold ) &&
                                ( share.fastBitmap.getGray(i, j + 1) < highThreshold ) &&
                                ( share.fastBitmap.getGray(i - 1, j - 1) < highThreshold ) &&
                                ( share.fastBitmap.getGray(i - 1, j) < highThreshold ) &&
                                ( share.fastBitmap.getGray(i - 1, j + 1) < highThreshold ) &&
                                ( share.fastBitmap.getGray(i + 1, j - 1) < highThreshold ) &&
                                ( share.fastBitmap.getGray(i + 1, j) < highThreshold ) &&
                                ( share.fastBitmap.getGray(i + 1, j + 1) < highThreshold ) )
                            {
                                share.fastBitmap.setGray(i, j, 0);
                            }
                        }
                    }
                }
            }
        }
    }
}