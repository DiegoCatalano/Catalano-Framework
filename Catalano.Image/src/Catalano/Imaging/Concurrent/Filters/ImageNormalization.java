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
import Catalano.Imaging.Tools.ImageStatistics;

/**
 * Image Normalization.
 * 
 * References: http://www.ccis2k.org/iajit/PDF/vol.4,no.4/12-Qader.pdf
 * @author Diego Catalano
 */
public class ImageNormalization implements IApplyInPlace{
    
    private float mean = 160;
    private float variance = 150;
    
    private float globalMean;
    private float globalVariance;

    /**
     * Get Mean.
     * @return Mean.
     */
    public float getMean() {
        return mean;
    }

    /**
     * Set Mean.
     * @param mean Mean.
     */
    public void setMean(float mean) {
        this.mean = Math.max(0, Math.min(255, mean));
    }

    /**
     * Get Variance.
     * @return Variance.
     */
    public float getVariance() {
        return variance;
    }

    /**
     * Set Variance.
     * @param variance Variance.
     */
    public void setVariance(float variance) {
        this.variance = Math.max(0, Math.min(255, variance));
    }

    /**
     * Initialize a new instance of the ImageNormalization class.
     */
    public ImageNormalization() {}

    /**
     * Initialize a new instance of the ImageNormalization class.
     * @param mean Desired mean.
     * @param variance Desired variance.
     */
    public ImageNormalization(float mean, float variance) {
        setMean(mean);
        setVariance(variance);
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        if (fastBitmap.isGrayscale())
            Parallel(fastBitmap);
        else
            throw new IllegalArgumentException("ImageNormalization only works in grayscale images.");
    }

    private void Parallel(FastBitmap fastBitmap){
        
        globalMean = ImageStatistics.Mean(fastBitmap);
        globalVariance = ImageStatistics.Variance(fastBitmap);
        
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
                    
                    int g = share.fastBitmap.getGray(i, j);
                    float common = (float)Math.sqrt((variance * (float)Math.pow(g - globalMean, 2)) / globalVariance);
                    int n = 0;
                    if (g > globalMean){
                        n = (int)(mean + common);
                    }
                    else{
                        n = (int)(mean - common);
                    }
                    
                    n = n > 255 ? 255 : n;
                    n = n < 0 ? 0 : n;
                    
                    share.fastBitmap.setGray(i, j, n);
                }
            }
        }
    }
}