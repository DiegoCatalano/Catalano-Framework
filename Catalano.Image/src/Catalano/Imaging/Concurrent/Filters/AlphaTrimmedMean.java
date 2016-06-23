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
import java.util.Arrays;

/**
 * Median Filter.
 * The median filter is normally used to reduce noise in an image, somewhat like the mean filter. However, it often does a better job than the mean filter of preserving useful detail in the image.
 * @author Diego Catalano
 */
public class AlphaTrimmedMean implements IApplyInPlace{
    
    private int radius = 1;
    private int trim = 1;
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
     * Get trimmed value.
     * @return Trimmed value.
     */
    public int getT() {
        return trim;
    }

    /**
     * Set trimmed value.
     * @param t Trimmed value.
     */
    public void setT(int t) {
        this.trim = Math.max((radius*2+1)*(radius*2+1)/2, Math.min(0, t));
    }

    /**
     * Initializes a new instance of the AlphaTrimmedMean class.
     */
    public AlphaTrimmedMean() {}

    /**
     * Initializes a new instance of the AlphaTrimmedMean class.
     * @param radius Radius.
     */
    public AlphaTrimmedMean(int radius) {
        setRadius(radius);
    }
    
    /**
     * Initializes a new instance of the AlphaTrimmedMean class.
     * @param radius Radius.
     * @param t Trimmed value.
     */
    public AlphaTrimmedMean(int radius, int t){
        setRadius(radius);
        setT(t);
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
            t[i] = new Thread(new Run(new Share(fb, startX, startX += part, lastThread)));
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
    
    private class Run implements Runnable {

        private Share share;
        
        public Run(Share obj) {
            this.share = obj;
        }

        @Override
        public void run() {

            int Xline,Yline;
            int lines = CalcLines(radius);
            int maxArray = lines*lines;
            int c;

            int safe = radius;

            if (share.lastThread){
                safe = 0;
                share.endHeight = share.fastBitmap.getHeight();
            }

            if (share.fastBitmap.isGrayscale()){
                int[] avgL = new int [maxArray];
                for (int x = share.startX; x < share.endHeight; x++) {
                    for (int y = 0; y < share.fastBitmap.getWidth(); y++) {
                        c = 0;
                        for (int i = 0; i < lines; i++) {
                            Xline = x + (i-radius);
                            for (int j = 0; j < lines; j++) {
                                Yline = y + (j-radius);
                                if ((Xline >= 0) && (Xline < share.endHeight + safe) && (Yline >=0) && (Yline < share.fastBitmap.getWidth())) {
                                    avgL[c] = copy.getGray(Xline, Yline);
                                }
                                else{
                                    avgL[c] = copy.getGray(x, y);
                                }
                                c++;
                            }
                        }
                        Arrays.sort(avgL,0,c);
                        
                        //alpha trimmed mean
                        double mean = 0;
                        for (int i = trim; i < c - trim; i++) {
                            mean += avgL[i];
                        }
                        share.fastBitmap.setGray(x, y, (int)(mean / (avgL.length - 2*trim)));
                    }
                }
            }
            else{
                int[] avgR = new int[maxArray];
                int[] avgG = new int[maxArray];
                int[] avgB = new int[maxArray];

                for (int x = share.startX; x < share.endHeight; x++) {
                    for (int y = 0; y < share.fastBitmap.getWidth(); y++) {
                        c = 0;
                        for (int i = 0; i < lines; i++) {
                            Xline = x + (i-radius);
                            for (int j = 0; j < lines; j++) {
                                Yline = y + (j-radius);
                                if ((Xline >= 0) && (Xline < share.endHeight + safe) && (Yline >=0) && (Yline < share.fastBitmap.getWidth())) {
                                    avgR[c] = copy.getRed(Xline, Yline);
                                    avgG[c] = copy.getGreen(Xline, Yline);
                                    avgB[c] = copy.getBlue(Xline, Yline);
                                }
                                else{
                                    avgR[c] = copy.getRed(x, y);
                                    avgG[c] = copy.getGreen(x, y);
                                    avgB[c] = copy.getBlue(x, y);
                                }
                                c++;
                            }
                        }
                        
                        Arrays.sort(avgR,0,c);
                        Arrays.sort(avgG,0,c);
                        Arrays.sort(avgB,0,c);
                        
                        //alpha trimmed mean
                        double meanR = 0, meanG = 0, meanB = 0;
                        for (int i = trim; i < c - trim; i++) {
                            meanR += avgR[i];
                            meanG += avgG[i];
                            meanB += avgB[i];
                        }

                        meanR /= (avgR.length - 2*trim);
                        meanG /= (avgG.length - 2*trim);
                        meanB /= (avgB.length - 2*trim);
                        
                        share.fastBitmap.setRGB(x, y, (int)meanR, (int)meanG, (int)meanB);
                    }
                }
            }
        }

        private int CalcLines(int radius){
            return radius * 2 + 1;
        }
    }
}