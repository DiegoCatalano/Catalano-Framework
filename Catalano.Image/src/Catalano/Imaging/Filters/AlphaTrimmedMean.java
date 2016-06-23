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
 * Alpha Trimmed Mean filter.
 * <p>The alpha trimmed mean filter is normally used to reduce noise in an image, somewhat like the mean and median filter. However, it often does a better job than the mean filter of preserving useful detail in the image.</p>
 * 
 * <p><b>Properties:</b>
 * <li>Supported types: Grayscale, RGB.
 * <br><li>Coordinate System: Matrix.</p>
 * 
 * @author Diego Catalano
 */
public class AlphaTrimmedMean implements IApplyInPlace{
    
    private int radius = 1;
    private int t = 1;

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
        return t;
    }

    /**
     * Set trimmed value.
     * @param t Trimmed value.
     */
    public void setT(int t) {
        this.t = Math.min((radius*2+1)*(radius*2+1)/2, Math.max(0, t));
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
            for (int x = 0; x < height; x++) {
                for (int y = 0; y < width; y++) {
                    c = 0;
                    for (int i = 0; i < lines; i++) {
                        Xline = x + (i-radius);
                        for (int j = 0; j < lines; j++) {
                            Yline = y + (j-radius);
                            if ((Xline >= 0) && (Xline < height) && (Yline >=0) && (Yline < width)) {
                                avgL[c] = copy.getGray(Xline, Yline);
                            }
                            else{
                                avgL[c] = copy.getGray(x, y);
                            }
                            c++;
                        }
                    }
                    
                    Arrays.sort(avgL);
                    
                    //alpha trimmed mean
                    double mean = 0;
                    for (int i = t; i < c - t; i++) {
                        mean += avgL[i];
                    }
                    
                    fastBitmap.setGray(x, y, (int)(mean / (avgL.length - 2*t)));
                }
            }
        }
        else if(fastBitmap.isRGB()){
            int[] avgR = new int[maxArray];
            int[] avgG = new int[maxArray];
            int[] avgB = new int[maxArray];

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
                            }
                            else{
                                avgR[c] = copy.getRed(x, y);
                                avgG[c] = copy.getGreen(x, y);
                                avgB[c] = copy.getBlue(x, y);
                            }
                            c++;
                        }
                    }
                    
                    Arrays.sort(avgR);
                    Arrays.sort(avgG);
                    Arrays.sort(avgB);
                    
                    //alpha trimmed mean
                    double meanR = 0, meanG = 0, meanB = 0;
                    for (int i = t; i < c - t; i++) {
                        meanR += avgR[i];
                        meanG += avgG[i];
                        meanB += avgB[i];
                    }
                    
                    meanR /= (avgR.length - 2*t);
                    meanG /= (avgG.length - 2*t);
                    meanB /= (avgB.length - 2*t);
                    
                    fastBitmap.setRGB(x, y, (int)meanR, (int)meanG, (int)meanB);
                }
            }
        }
    }
    
    private int CalcLines(int radius){
        return radius * 2 + 1;
    }
}