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

package Catalano.Imaging.Filters;

import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.IApplyInPlace;

/**
 * MidPoint filter.
 * Set (maximum - minimum) / 2
 * @author Diego Catalano
 */
public class MidPoint implements IApplyInPlace{
    
    private int radius = 1;

    /**
     * Initialize a new instance of the MidPoint class.
     */
    public MidPoint() {}
    
    /**
     * Initialize a new instance of the MidPoint class.
     * @param radius Radius.
     */
    public MidPoint(int radius){
        radius = radius < 1 ? 1 : radius;
        this.radius = radius;
    }
    
    @Override
    public void applyInPlace(FastBitmap fastBitmap){
        
        int width = fastBitmap.getWidth();
        int height = fastBitmap.getHeight();
            
        FastBitmap copy = new FastBitmap(fastBitmap);
        int Xline,Yline;
        int lines = CalcLines(radius);

        if (fastBitmap.isGrayscale()) {
            int max,min;
            int gray;
            for (int x = 0; x < height; x++) {
                for (int y = 0; y < width; y++) {
                    max = 0;
                    min = 255;
                    for (int i = 0; i < lines; i++) {
                        Xline = x + (i-radius);
                        for (int j = 0; j < lines; j++) {
                            Yline = y + (j-radius);
                            if ((Xline >= 0) && (Xline < height) && (Yline >=0) && (Yline < width)) {
                                max = Math.max(max,copy.getGray(Xline, Yline));
                                min = Math.min(min,copy.getGray(Xline, Yline));
                            }
                        }
                    }
                    gray = (max + min) / 2;
                    fastBitmap.setGray(x, y, gray);
                }
            }
        }
        else{
            int maxR, minR;
            int maxG, minG;
            int maxB, minB;
            int r,g,b;

            for (int x = 0; x < height; x++) {
                for (int y = 0; y < width; y++) {
                    maxR = maxG = maxB = 0;
                    minR = minG = minB = 255;
                    for (int i = 0; i < lines; i++) {
                        Xline = x + (i-radius);
                        for (int j = 0; j < lines; j++) {
                            Yline = y + (j-radius);
                            if ((Xline >= 0) && (Xline < height) && (Yline >=0) && (Yline < width)) {
                                maxR = Math.max(maxR,copy.getRed(Xline, Yline));
                                maxG = Math.max(maxG,copy.getGreen(Xline, Yline));
                                maxB = Math.max(maxB,copy.getBlue(Xline, Yline));

                                minR = Math.min(minR,copy.getRed(Xline, Yline));
                                minG = Math.min(minG,copy.getGreen(Xline, Yline));
                                minB = Math.min(minB,copy.getBlue(Xline, Yline));
                            }
                        }
                    }
                    r = (maxR + minR) / 2;
                    g = (maxG + minG) / 2;
                    b = (maxB + minB) / 2;
                    fastBitmap.setRGB(x, y, r, g, b);
                }
            }
        }
        }
    
    private int CalcLines(int radius){
        return radius * 2 + 1;
    }
}