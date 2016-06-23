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
 * Variance Filter.
 * @author Diego Catalano
 */
public class Variance implements IApplyInPlace{
    
    private int radius = 2;
    private FastBitmap copy;

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
        this.radius = Math.max(1, radius);
    }

    /**
     * Initializes a new instance of the Median class.
     */
    public Variance() {}

    /**
     * Initializes a new instance of the Median class.
     * @param radius Radius.
     */
    public Variance(int radius) {
        setRadius(radius);
    }
    
    @Override
    public void applyInPlace(FastBitmap fb){
        this.copy = new FastBitmap(fb);
        int cores = Runtime.getRuntime().availableProcessors();
        
        Thread[] t = new Thread[cores];
        int part = fb.getHeight() / cores;
        int last = cores - 1;
        boolean lastThread = false;
        
        int startX = 0;
        for (int i = 0; i < cores; i++) {
            if (i == last) lastThread = true;
            t[i] = new Thread(new CThread(new Share(fb, startX, startX += part, lastThread)));
            t[i].start();
        }
        
        try {
            
            for (int i = 0; i < cores; i++) {
                t[i].join();
            }
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    private class CThread implements Runnable {

        private Share share;
        public CThread(Share obj) {
            this.share = obj;
        }

    @Override
    public void run() {
        
        int safe = radius;
        
        if (share.lastThread){
            safe = 0;
            share.endHeight = share.fastBitmap.getHeight();
        }

        if (share.fastBitmap.isGrayscale()){
            for (int x = share.startX; x < share.endHeight; x++) {
                for (int y = 0; y < share.fastBitmap.getWidth(); y++) {
                    double mean = 0;
                    double var = 0;
                    int total = 0;
                    for (int i = x - radius; i <= x + radius; i++) {
                        for (int j = y - radius; j <= y + radius; j++) {
                            if (i >= 0 && i < share.endHeight + safe && j >= 0 && j < share.fastBitmap.getWidth()){
                                mean += copy.getGray(i, j);
                                total++;
                            }
                        }
                    }
                    mean /= total;
                    for (int i = x - radius; i <= x + radius; i++) {
                        for (int j = y - radius; j <= y + radius; j++) {
                            if (i >= 0 && i < share.endHeight + safe && j >= 0 && j < share.fastBitmap.getWidth())
                                var += Math.pow(copy.getGray(i, j) - mean, 2);
                        }
                    }
                    var /= total - 1;
                    if (var < 0) var = 0;
                    if (var > 255) var = 255;
                    share.fastBitmap.setGray(x, y, (int)var);
                }
            }
        }
        else{
            for (int x = share.startX; x < share.endHeight; x++) {
                for (int y = 0; y < share.fastBitmap.getWidth(); y++) {
                    double meanR = 0, meanG = 0, meanB = 0;
                    double varR = 0, varG = 0, varB = 0;
                    int total = 0;
                    for (int i = x - radius; i <= x + radius; i++) {
                        for (int j = y - radius; j <= y + radius; j++) {
                            if (i >= 0 && i < share.endHeight + safe && j >= 0 && j < share.fastBitmap.getWidth()){
                                meanR += copy.getRed(i, j);
                                meanG += copy.getGreen(i, j);
                                meanB += copy.getBlue(i, j);
                                total++;
                            }
                        }
                    }
                    meanR /= total;
                    meanG /= total;
                    meanB /= total;
                    for (int i = x - radius; i <= x + radius; i++) {
                        for (int j = y - radius; j <= y + radius; j++) {
                            if (i >= 0 && i < share.endHeight + safe && j >= 0 && j < share.fastBitmap.getWidth()){
                                varR += Math.pow(copy.getRed(i, j) - meanR, 2);
                                varG += Math.pow(copy.getGreen(i, j) - meanG, 2);
                                varB += Math.pow(copy.getBlue(i, j) - meanB, 2);
                            }
                        }
                    }
                    varR /= total - 1;
                    varG /= total - 1;
                    varB /= total - 1;
                    
                    if (varR < 0) varR = 0;
                    if (varG < 0) varG = 0;
                    if (varB < 0) varB = 0;
                    
                    if (varR > 255) varR = 255;
                    if (varG > 255) varG = 255;
                    if (varB > 255) varB = 255;
                    
                    share.fastBitmap.setRGB(x, y, (int)varR, (int)varG, (int)varB);
                }
            }
        }
    }
        
    private int CalcLines(int radius){
        return radius * 2 + 1;
    }
    }
}