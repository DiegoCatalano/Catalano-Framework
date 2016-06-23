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
 * Dilatation operator from Mathematical Morphology.
 * The filter assigns maximum value of surrounding pixels to each pixel of the result image. Surrounding pixels, which should be processed, are specified by structuring element: 1 - to process the neighbor, 0 - to skip it.
 * The filter especially useful for binary image processing, where it allows to grow separate objects or join objects.
 * 
 * The filter accepts 8, 24 bpp images for processing.
 * 
 * @author Diego Catalano
 */
public class Dilatation implements IApplyInPlace{
    
    private int radius = 0;
    private int[][] kernel;
    private FastBitmap copy;

    /**
     * Initialize a new instance of the Dilatation class.
     */
    public Dilatation() {
        this.radius = 1;
    }

    /**
     * Initialize a new instance of the Dilatation class.
     * @param radius Radius.
     */
    public Dilatation(int radius) {
        this.radius = Math.max(radius,1);
    }

    /**
     * Initialize a new instance of the Dilatation class.
     * @param kernel Kernel.
     */
    public Dilatation(int[][] kernel) {
        this.kernel = kernel;
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        copy = new FastBitmap(fastBitmap);
        
        if (kernel == null)
            createKernel(radius);
        
        Parallel(fastBitmap);
    }
    
    private void Parallel(FastBitmap fastBitmap){
        
        int cores = Runtime.getRuntime().availableProcessors();
        
        Thread[] t = new Thread[cores];
        int part = fastBitmap.getHeight() / cores;
        int last = cores - 1;
        boolean lastThread = false;
        
        int startX = 0;
        for (int i = 0; i < cores; i++) {
            if (i == last) lastThread = true;
            t[i] = new Thread(new Run(new Share(fastBitmap, startX, startX + part, lastThread)));
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
            
            int safe = radius;
            if (share.lastThread)
                safe = 0;
            
            
            if (share.fastBitmap.isGrayscale()){

                for (int i = share.startX; i < share.endHeight; i++) {
                    for (int j = 0; j < share.fastBitmap.getWidth(); j++) {

                        int X = 0,Y;
                        int max = 0;
                        for (int x = i - radius; x < i + radius + 1; x++) {
                            Y = 0;
                            for (int y = j - radius; y < j + radius + 1; y++) {

                                if (x >= 0 && x < share.endHeight + safe && y >= 0 && y < share.fastBitmap.getWidth()){
                                    int val = copy.getGray(x, y) + kernel[X][Y];

                                    if (val > max)
                                        max = val;

                                }
                                Y++;
                            }
                            X++;
                        }

                        max = max > 255 ? 255 : max;
                        share.fastBitmap.setGray(i, j, max);
                    }
                }
            }
            if (share.fastBitmap.isRGB()){

                for (int i = share.startX; i < share.endHeight; i++) {
                    for (int j = 0; j < share.fastBitmap.getWidth(); j++) {

                        int X = 0,Y;
                        int maxR = 0, maxG = 0, maxB = 0;
                        for (int x = i - radius; x < i + radius + 1; x++) {
                            Y = 0;
                            for (int y = j - radius; y < j + radius + 1; y++) {

                                if (x >= 0 && x < share.endHeight + safe && y >= 0 && y < share.fastBitmap.getWidth()){
                                    int valR = copy.getRed(x, y) + kernel[X][Y];
                                    int valG = copy.getGreen(x, y) + kernel[X][Y];
                                    int valB = copy.getBlue(x, y) + kernel[X][Y];

                                    if (valR > maxR)
                                        maxR = valR;

                                    if (valG > maxG)
                                        maxG = valG;

                                    if (valB > maxB)
                                        maxB = valB;

                                }
                                Y++;
                            }
                            X++;
                        }

                        maxR = maxR >  255 ? 255 : maxR;
                        maxG = maxG >  255 ? 255 : maxG;
                        maxB = maxB >  255 ? 255 : maxB;
                        share.fastBitmap.setRGB(i, j, maxR, maxG, maxB);
                    }
                }
            }
        }
    }
    
    private void createKernel(int radius){
        int size = radius * 2 + 1;
        this.kernel = new int[size][size];
        for (int i = 0; i < kernel.length; i++) {
            for (int j = 0; j < kernel[0].length; j++) {
                kernel[i][j] = 1;
            }
        }
    }
}