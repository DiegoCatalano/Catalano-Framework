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

package Catalano.Imaging.Filters;

import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.IApplyInPlace;
import java.util.Arrays;

/**
 * Mode filter.
 * The mode is of course very difficult to determine for small populations and theoretically does not even exist for a continuous distribution.
 * As such we are forced to estimate the mode: the truncated median filter, as introduced by Davies (1988), aims to achieve this.
 * @author Diego Catalano
 */
public class Mode implements IApplyInPlace{
    
    private int radius = 1;

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
        this.radius = radius;
    }

    /**
     * Initializes a new instance of the Mode class.
     */
    public Mode() {}

    /**
     * Initializes a new instance of the Mode class.
     * @param radius Radius.
     */
    public Mode(int radius) {
        this.radius = Math.max(1, radius);
    }
    
    @Override
    public void applyInPlace(FastBitmap fastBitmap){
        
        int width = fastBitmap.getWidth();
        int height = fastBitmap.getHeight();
        int Xline,Yline;
        int lines = CalcLines(radius);
        int maxArray = lines*lines;
        int c;
        
        FastBitmap copy = new FastBitmap(fastBitmap);
        
        if (fastBitmap.isGrayscale()) {
            int[] avgL = new int [maxArray];
            double mean;
            int median;
            int min;
            int max;
            for (int x = 0; x < height; x++) {
                for (int y = 0; y < width; y++) {
                    c = 0;
                    mean = 0;
                    min = 255;
                    max = 0;
                    for (int i = 0; i < lines; i++) {
                        Xline = x + (i-radius);
                        for (int j = 0; j < lines; j++) {
                            Yline = y + (j-radius);
                            if ((Xline >= 0) && (Xline < height) && (Yline >=0) && (Yline < width)) {
                                int g = copy.getGray(Xline, Yline);
                                avgL[c] = g;
                                mean += g;
                                c++;
                                if(g > max)
                                    max = g;
                                if(g < min)
                                    min = g;
                            }
                        }
                    }
                    Arrays.sort(avgL,0,c);
                    median = c / 2;
                    median = avgL[median];
                    
                    mean /= c;
                    
                    int upper = 2 * median - min;
                    int lower = 2 * median - max;
                    int cc = 0;
                    
                    for (int i = 0; i < lines; i++) {
                        Xline = x + (i - radius);
                        for (int j = 0; j < lines; j++) {
                            Yline = y + (j - radius);
                            if ((Xline >= 0) && (Xline < height) && (Yline >=0) && (Yline < width)) {
                                int g = copy.getGray(Xline, Yline);
                                if((g < upper) && (median < mean)){
                                    avgL[cc] = g;
                                    cc++;
                                }
                                if((g > lower) && (median > mean)){
                                    avgL[cc] = g;
                                    cc++;
                                }
                            }
                        }
                    }
                    
                    if(cc > 0){
                        Arrays.sort(avgL,0,cc);
                        median = cc / 2;
                        median = avgL[median];
                        fastBitmap.setGray(x, y, median);
                    }
                    
                    fastBitmap.setGray(x, y, median);
                }
            }
        }
        else if(fastBitmap.isRGB()){
            int[] avgR = new int [maxArray];
            int[] avgG = new int [maxArray];
            int[] avgB = new int [maxArray];
            double meanR, meanG, meanB;
            int medianR, medianG, medianB;
            int minR, minG, minB;
            int maxR, maxG, maxB;
            for (int x = 0; x < height; x++) {
                for (int y = 0; y < width; y++) {
                    c = 0;
                    meanR = meanG = meanB = 0;
                    minR = minG = minB = 255;
                    maxR = maxG = maxB = 0;
                    for (int i = 0; i < lines; i++) {
                        Xline = x + (i-radius);
                        for (int j = 0; j < lines; j++) {
                            Yline = y + (j-radius);
                            if ((Xline >= 0) && (Xline < height) && (Yline >=0) && (Yline < width)) {
                                int r = copy.getRed(Xline, Yline);
                                int g = copy.getGreen(Xline, Yline);
                                int b = copy.getBlue(Xline, Yline);
                                
                                avgR[c] = r;
                                meanR += r;
                                
                                avgG[c] = g;
                                meanG += g;
                                
                                avgB[c] = b;
                                meanB += b;
                                
                                maxR = Math.max(r, maxR);
                                maxG = Math.max(g, maxG);
                                maxB = Math.max(b, maxB);
                                
                                minR = Math.min(r, minR);
                                minG = Math.min(g, minG);
                                minB = Math.min(b, minB);
                                
                                c++;
                            }
                        }
                    }
                    Arrays.sort(avgR,0,c);
                    Arrays.sort(avgG,0,c);
                    Arrays.sort(avgB,0,c);
                    
                    medianR = c / 2;
                    medianR = avgR[medianR];
                    
                    medianG = c / 2;
                    medianG = avgG[medianG];
                    
                    medianB = c / 2;
                    medianB = avgB[medianB];
                    
                    meanR /= c;
                    meanG /= c;
                    meanB /= c;
                    
                    int upperR = 2 * medianR - minR;
                    int upperG = 2 * medianG - minG;
                    int upperB = 2 * medianB - minB;
                    
                    int lowerR = 2 * medianR - maxR;
                    int lowerG = 2 * medianG - maxG;
                    int lowerB = 2 * medianB - maxB;
                    
                    int ccR = 0;
                    int ccG = 0;
                    int ccB = 0;
                    for (int i = 0; i < lines; i++) {
                        Xline = x + (i - radius);
                        for (int j = 0; j < lines; j++) {
                            Yline = y + (j - radius);
                            if ((Xline >= 0) && (Xline < height) && (Yline >=0) && (Yline < width)) {
                                int v = copy.getRed(Xline, Yline);
                                if((v < upperR) && (medianR < meanR)){
                                    avgR[ccR] = v;
                                    ccR++;
                                }
                                if((v > lowerR) && (medianR > meanR)){
                                    avgR[ccR] = v;
                                    ccR++;
                                }
                                
                                v = copy.getGreen(Xline, Yline);
                                if((v < upperG) && (medianG < meanG)){
                                    avgG[ccG] = v;
                                    ccG++;
                                }
                                if((v > lowerG) && (medianG > meanG)){
                                    avgG[ccG] = v;
                                    ccG++;
                                }
                                
                                v = copy.getBlue(Xline, Yline);
                                if((v < upperB) && (medianB < meanB)){
                                    avgB[ccB] = v;
                                    ccB++;
                                }
                                if((v > lowerB) && (medianB > meanB)){
                                    avgB[ccB] = v;
                                    ccB++;
                                }
                            }
                        }
                    }
                    
                    if(ccR > 0){
                        Arrays.sort(avgR,0,ccR);
                        medianR = ccR / 2;
                        medianR = avgR[medianR];
                        fastBitmap.setRed(x, y, medianR);
                    }
                    else{
                        fastBitmap.setRed(x, y, medianR);
                    }
                    
                    if(ccG > 0){
                        Arrays.sort(avgG,0,ccG);
                        medianG = ccG / 2;
                        medianG = avgG[medianG];
                        fastBitmap.setGreen(x, y, medianG);
                    }
                    else{
                        fastBitmap.setGreen(x, y, medianG);
                    }
                    
                    if(ccB > 0){
                        Arrays.sort(avgB,0,ccB);
                        medianB = ccB / 2;
                        medianB = avgB[medianB];
                        fastBitmap.setBlue(x, y, medianB);
                    }
                    else{
                        fastBitmap.setBlue(x, y, medianB);
                    }
                    
                }
            }
        }
        else{
            throw new IllegalArgumentException("Mode only works in grayscale or rgb images.");
        }
    }
    
    private int CalcLines(int radius){
        return radius * 2 + 1;
    }
}