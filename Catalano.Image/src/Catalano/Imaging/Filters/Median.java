// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
// Copyright © Andrew Kirillov, 2007-2008
// andrew.kirillov at gmail.com
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
 * Median filter.
 * The median filter is normally used to reduce noise in an image, somewhat like the mean filter. However, it often does a better job than the mean filter of preserving useful detail in the image.
 * @author Diego Catalano
 */
public class Median implements IApplyInPlace{
    
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
     * Initializes a new instance of the Median class.
     */
    public Median() {}

    /**
     * Initializes a new instance of the Median class.
     * @param radius Radius.
     */
    public Median(int radius) {
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
            int median;
            for (int x = 0; x < height; x++) {
                for (int y = 0; y < width; y++) {
                    c = 0;
                    for (int i = 0; i < lines; i++) {
                        Xline = x + (i-radius);
                        for (int j = 0; j < lines; j++) {
                            Yline = y + (j-radius);
                            if ((Xline >= 0) && (Xline < height) && (Yline >=0) && (Yline < width)) {
                                avgL[c] = copy.getGray(Xline, Yline);
                                c++;
                            }
                        }
                    }
                    Arrays.sort(avgL,0,c);
                    //median
                    median = c / 2;
                    fastBitmap.setGray(x, y, avgL[median]);
                }
            }
        }
        else if(fastBitmap.isRGB()){
            int[] avgR = new int[maxArray];
            int[] avgG = new int[maxArray];
            int[] avgB = new int[maxArray];
            int median;

            for (int x = 0; x < height; x++) {
                for (int y = 0; y < width; y++) {
                    c = 0;
                    for (int i = 0; i < lines; i++) {
                        Xline = x + (i-radius);
                        for (int j = 0; j < lines; j++) {
                            Yline = y + (j-radius);
                            if ((Xline >= 0) && (Xline < height) && (Yline >=0) && (Yline < width)) {
                                avgR[c] = copy.getRed(Xline, Yline);
                                avgG[c] = copy.getGreen(Xline, Yline);
                                avgB[c] = copy.getBlue(Xline, Yline);
                                c++;
                            }
                        }
                    }
                    Arrays.sort(avgR,0,c);
                    Arrays.sort(avgG,0,c);
                    Arrays.sort(avgB,0,c);
                    //median
                    median = c / 2;
                    fastBitmap.setRGB(x, y, avgR[median], avgG[median], avgB[median]);
                }
            }
        }
    }
    
    private int CalcLines(int radius){
        return radius * 2 + 1;
    }
}