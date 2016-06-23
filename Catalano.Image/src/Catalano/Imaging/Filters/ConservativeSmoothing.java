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

/**
 * Conservative Smoothing.
 * 
 * <p> Conservative smoothing is a noise reduction technique that derives its name from the fact that it employs a
 * simple, fast filtering algorithm that sacrifices noise suppression power in order to preserve the high
 * spatial frequency detail (e.g. sharp edges) in an image. It is explicitly designed to remove noise spikes --- i.e. isolated pixels of
 * exceptionally low or high pixel intensity (e.g. salt and pepper noise). </p>
 * 
 * <p><li>Supported types: Grayscale, RGB.
 * <br><li>Coordinate System: Matrix.
 * 
 * @author Diego Catalano
 */
public class ConservativeSmoothing implements IApplyInPlace{
    
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
        this.radius = Math.max(1, radius);
    }

    /**
     * Initialize a new instance of the ConservativeSmoothing class.
     */
    public ConservativeSmoothing() {}
    
    /**
     * Initialize a new instance of the ConservativeSmoothing class.
     * @param radius Radius.
     */
    public ConservativeSmoothing(int radius){
        setRadius(radius);
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        int width = fastBitmap.getWidth();
        int height = fastBitmap.getHeight();
        
        int Xline,Yline;
        int lines = CalcLines(radius);
        
        FastBitmap copy = new FastBitmap(fastBitmap);
        
        if (fastBitmap.isRGB()){
            int minR, minG, minB;
            int maxR, maxG, maxB;
            for (int x = 0; x < height; x++) {
                for (int y = 0; y < width; y++) {
                    minR = minG = minB = 255;
                    maxR = maxG = maxB = 0;
                    for (int i = 0; i < lines; i++) {
                        Xline = x + (i-radius);
                        for (int j = 0; j < lines; j++) {
                            Yline = y + (j-radius);
                            if (((Xline >= 0) && (Xline < height) && (Yline >=0) && (Yline < width)) && (i != j)) {
                                
                                if (copy.getRed(Xline, Yline) > maxR){
                                    maxR = copy.getRed(Xline, Yline);
                                }
                                
                                if (copy.getGreen(Xline, Yline) > maxG){
                                    maxG = copy.getGreen(Xline, Yline);
                                }
                                
                                if (copy.getBlue(Xline, Yline) > maxB){
                                    maxB = copy.getBlue(Xline, Yline);
                                }
                                
                                if (copy.getRed(Xline, Yline) < minR){
                                    minR = copy.getRed(Xline, Yline);
                                }
                                
                                if (copy.getGreen(Xline, Yline) < minG){
                                    minG = copy.getGreen(Xline, Yline);
                                }
                                
                                if (copy.getBlue(Xline, Yline) < minB){
                                    minB = copy.getBlue(Xline, Yline);
                                }
                                
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
                    
                    fastBitmap.setRGB(x, y, r, g, b);
                }
            }
        }
        else{
            int minG;
            int maxG;
            for (int x = 0; x < height; x++) {
                for (int y = 0; y < width; y++) {
                    minG = 255;
                    maxG = 0;
                    for (int i = 0; i < lines; i++) {
                        Xline = x + (i-radius);
                        for (int j = 0; j < lines; j++) {
                            Yline = y + (j-radius);
                            if (((Xline >= 0) && (Xline < height) && (Yline >=0) && (Yline < width)) && (i != j)) {
                                
                                if (copy.getGray(Xline, Yline) > maxG){
                                    maxG = copy.getGray(Xline, Yline);
                                }
                                
                                if (copy.getGray(Xline, Yline) < minG){
                                    minG = copy.getGray(Xline, Yline);
                                }
                                
                            }
                        }
                    }
                    
                    int g = copy.getGray(x, y);
                    
                    if (g > maxG) g = maxG;
                    if (g < minG) g = minG;
                    
                    fastBitmap.setGray(x, y, g);
                }
            }
        }
        
    }
    
    private int CalcLines(int radius){
        return radius * 2 + 1;
    }
    
}
