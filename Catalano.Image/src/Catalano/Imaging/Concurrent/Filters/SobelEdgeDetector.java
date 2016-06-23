// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
// Copyright © Andrew Kirillov, 2007-2008
// andrew.kirillov@gmail.com
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
 * Sobel Edge Detector.
 * <para> The filter searches for objects' edges by applying Sobel operator.
 * Each pixel of the result image is calculated as approximated absolute gradient magnitude for corresponding pixel of the source image:
 * |G| = |Gx| + |Gy] ,
 * where Gx and Gy are calculate utilizing Sobel convolution kernels.
 * Using the above kernel the approximated magnitude for pixel x is calculate using the next equation:
 * |G| = |P1 + 2P2 + P3 - P7 - 2P6 - P5| + |P3 + 2P4 + P5 - P1 - 2P8 - P7|
 * @author Diego Catalano
 */
public class SobelEdgeDetector implements IApplyInPlace{
    
    private FastBitmap copy;
    private int max = 0;
    private boolean scaleIntensity = true;
    
    /**
     * Is scale intensity.
     * 
     * <para>Check if edges' pixels intensities of the result image
     * should be scaled in the range of the lowest and the highest possible intensity
     * values.</para>
     * 
     * @return Scale intensity value.
     */
    public boolean isScaleIntensity() {
        return scaleIntensity;
    }

    /**
     * Set scale intensity.
     * 
     * <para>Check if edges' pixels intensities of the result image
     * should be scaled in the range of the lowest and the highest possible intensity
     * values.</para>
     * 
     * @param scaleIntensity Scale intensity value.
     */
    public void setScaleIntensity(boolean scaleIntensity) {
        this.scaleIntensity = scaleIntensity;
    }

    /**
     * Initialize a new instance of the SobelEdgeDetector class.
     */
    public SobelEdgeDetector() {}
    
    @Override
    public void applyInPlace(FastBitmap fb){
        int cores = Runtime.getRuntime().availableProcessors();
        this.copy = new FastBitmap(fb);
        
        Thread[] t = new Thread[cores];
        int part = fb.getHeight() / cores;
        int lastT = cores - 1;
        int endWidth = fb.getWidth() - 1;
        
        int startX = 1;
        for (int i = 0; i < cores; i++) {
            if(i == lastT) part = fb.getHeight() - startX - 1;
            t[i] = new Thread(new Run(new Share(fb, startX, 1, endWidth, startX + part)));
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
                for (int j = share.startY; j < share.endWidth; j++) {
                    
                    int p1 = copy.getGray(i - 1, j - 1);
                    int p2 = copy.getGray(i - 1, j);
                    int p3 = copy.getGray(i - 1, j + 1);
                    int p4 = copy.getGray(i, j + 1);
                    int p5 = copy.getGray(i + 1, j);
                    int p6 = copy.getGray(i + 1, j + 1);
                    int p7 = copy.getGray(i + 1, j - 1);
                    int p8 = copy.getGray(i, j - 1);

                    int g = Math.min(255, Math.abs(p1 + p3 - p7 - p2 + 2 * (p2 - p5)) + Math.abs(p2 + p6 - p1 - p7 + 2 * (p4 - p8)));
                    if (g > max) max = g;
                    share.fastBitmap.setGray(i, j, g);
                }
            }
            
            if (scaleIntensity && max != 255){
                double factor = 255.0 / (double) max;
                
                for (int i = share.startX; i < share.endHeight; i++) {
                    for (int j = share.startY; j < share.endWidth; j++) {
                        share.fastBitmap.setGray(i, j, (int)(share.fastBitmap.getGray(i, j) * factor));
                    }
                }
            }
            
        }
    }
}