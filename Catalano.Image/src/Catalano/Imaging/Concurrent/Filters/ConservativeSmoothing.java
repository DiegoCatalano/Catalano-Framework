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
 * Conservative Smoothing.
 * 
 * <para> Conservative smoothing is a noise reduction technique that derives its name from the fact that it employs a
 * simple, fast filtering algorithm that sacrifices noise suppression power in order to preserve the high
 * spatial frequency detail (e.g. sharp edges) in an image. It is explicitly designed to remove noise spikes --- i.e. isolated pixels of
 * exceptionally low or high pixel intensity (e.g. salt and pepper noise). </para>
 * 
 * @author Diego Catalano
 */
public class ConservativeSmoothing implements IApplyInPlace{
    
    private int radius = 1;
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
     * Initializes a new instance of the ConservativeSmoothing class.
     */
    public ConservativeSmoothing() {}

    /**
     * Initializes a new instance of the ConservativeSmoothing class.
     * @param radius Radius.
     */
    public ConservativeSmoothing(int radius) {
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

            int Xline,Yline;
            int lines = CalcLines(radius);
            
            int safe = radius;

            if (share.lastThread){
                safe = 0;
                share.endHeight = share.fastBitmap.getHeight();
            }

            if (share.fastBitmap.isGrayscale()){
                int minG;
                int maxG;
                for (int x = share.startX; x < share.endHeight; x++) {
                    for (int y = 0; y < share.fastBitmap.getWidth(); y++) {
                        minG = 255;
                        maxG = 0;
                        for (int i = 0; i < lines; i++) {
                            Xline = x + (i-radius);
                            for (int j = 0; j < lines; j++) {
                                Yline = y + (j-radius);
                                if (((Xline >= 0) && (Xline < share.endHeight + safe) && (Yline >=0) && (Yline < share.fastBitmap.getWidth())) && (i != j)) {
                                    
                                    if (copy.getGray(Xline, Yline) > maxG)
                                        maxG = copy.getGray(Xline, Yline);
                                    
                                    if (copy.getGray(Xline, Yline) < minG)
                                        minG = copy.getGray(Xline, Yline);
                                }
                            }
                        }
                        
                        int g = copy.getGray(x, y);

                        if (g > maxG) g = maxG;
                        if (g < minG) g = minG;
                        
                        share.fastBitmap.setGray(x, y, g);
                    }
                }
            }
            else{
                int minR, minG, minB;
                int maxR, maxG, maxB;
                for (int x = share.startX; x < share.endHeight; x++) {
                    for (int y = 0; y < share.fastBitmap.getWidth(); y++) {
                        minR = minG = minB = 255;
                        maxR = maxG = maxB = 0;
                        for (int i = 0; i < lines; i++) {
                            Xline = x + (i-radius);
                            for (int j = 0; j < lines; j++) {
                                Yline = y + (j-radius);
                                if (((Xline >= 0) && (Xline < share.endHeight + safe) && (Yline >=0) && (Yline < share.fastBitmap.getWidth())) && (i != j)) {
                                    
                                    if (copy.getRed(Xline, Yline) > maxR)
                                        maxR = copy.getRed(Xline, Yline);
                                    
                                    if (copy.getGreen(Xline, Yline) > maxG)
                                        maxG = copy.getGreen(Xline, Yline);
                                    
                                    if (copy.getBlue(Xline, Yline) > maxB)
                                        maxB = copy.getBlue(Xline, Yline);
                                    
                                    if (copy.getRed(Xline, Yline) < minR)
                                        minR = copy.getRed(Xline, Yline);
                                    
                                    if (copy.getGreen(Xline, Yline) < minG)
                                        minG = copy.getGreen(Xline, Yline);
                                    
                                    if (copy.getBlue(Xline, Yline) < minB)
                                        minB = copy.getBlue(Xline, Yline);
                                }
                            }
                        }
                        
                        int r = copy.getRed(x, y);
                        int g = copy.getGreen(x, y);
                        int b = copy.getBlue(x, y);

                        if (r > maxR) r = maxR;
                        if (g > maxG) g = maxG;
                        if (b > maxB) b = maxB;
                        
                        if (r < minR) r = minR;
                        if (g < minG) g = minG;
                        if (b < minB) b = minB;
                        
                        share.fastBitmap.setRGB(x, y, r, g, b);
                    }
                }
            }
        }

        private int CalcLines(int radius){
            return radius * 2 + 1;
        }
    }
}