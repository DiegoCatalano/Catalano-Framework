// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
// Copyright © Wayne Rasband, 2010
// wsr at nih.gov
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
 * Kuwahara filter is able to apply smoothing on the image while preserving the edges.
 * @author Diego Catalano
 */
public class Kuwahara implements IApplyInPlace{
    
    private int windowSize = 5;

    /**
     * Initialize a new instance of the Kuwahara class.
     * Default window size is 5x5;
     */
    public Kuwahara() {}

    /**
     * Initialize a new instance of the Kuwahara class.
     * @param windowSize Window size.
     */
    public Kuwahara(int windowSize) {
        this.windowSize = Math.max(windowSize, 5);
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        int width = fastBitmap.getWidth();
        int height = fastBitmap.getHeight();
        int size2 = (windowSize+1)/2;
        int offset = (windowSize-1)/2;
        
        FastBitmap copy = new FastBitmap(fastBitmap);
        
        if (fastBitmap.isRGB()) {
            int width2 = width+offset;
            int height2 = height+offset;
            float[][][] mean = new float[width2][height2][3];
            float[][][] variance = new float[width2][height2][3];
            double sumR, sum2R;
            double sumG, sum2G;
            double sumB, sum2B;
            int n, r,g,b, xbase, ybase;
            for (int y1=0-offset; y1<0+height; y1++) {
                for (int x1=0-offset; x1<0+width; x1++) {
                    sumR=sumG=sumB=0;
                    sum2R=sum2G=sum2B=0;
                    n=0;
                    for (int x2=x1; x2<x1+size2; x2++) {
                        for (int y2=y1; y2<y1+size2; y2++) {
                            if(x2 > 0 && x2 < width && y2 > 0 && y2 < height){
                                r = copy.getRed(y2, x2);
                                g = copy.getGreen(y2, x2);
                                b = copy.getBlue(y2, x2);
                                
                                sumR += r;
                                sum2R += r*r;
                                
                                sumG += g;
                                sum2G += g*g;
                                
                                sumB += b;
                                sum2B += b*b;
                                
                                n++;
                            }
                            else{
                                n++;
                            }
                        }
                    }
                    mean[x1+offset][y1+offset][0] = (float)(sumR/n);
                    mean[x1+offset][y1+offset][1] = (float)(sumG/n);
                    mean[x1+offset][y1+offset][2] = (float)(sumB/n);
                    
                    variance[x1+offset][y1+offset][0] = (float)(sum2R-sumR*sumR/n);
                    variance[x1+offset][y1+offset][1] = (float)(sum2G-sumG*sumG/n);
                    variance[x1+offset][y1+offset][2] = (float)(sum2B-sumB*sumB/n);
                }
            }
            
            int xbase2=0, ybase2=0;
            float var, min;
            for (int y1=0; y1<0+height; y1++) {
                for (int x1=0; x1<0+width; x1++) {
                    
                    //Red channel
                    min = Float.MAX_VALUE;
                    xbase = x1; ybase=y1;
                    var = variance[xbase][ybase][0];
                    if (var<min) {min= var; xbase2=xbase; ybase2=ybase;}
                    xbase = x1+offset;
                    var = variance[xbase][ybase][0];
                    if (var<min) {min= var; xbase2=xbase; ybase2=ybase;}
                    ybase = y1+offset;
                    var = variance[xbase][ybase][0];
                    if (var<min) {min= var; xbase2=xbase; ybase2=ybase;}
                    xbase = x1; 
                    var = variance[xbase][ybase][0];
                    if (var<min) {min= var; xbase2=xbase; ybase2=ybase;}
                    
                    r = (int)(mean[xbase2][ybase2][0]+0.5);
                    
                    //Green channel
                    min = Float.MAX_VALUE;
                    xbase = x1; ybase=y1;
                    var = variance[xbase][ybase][1];
                    if (var<min) {min= var; xbase2=xbase; ybase2=ybase;}
                    xbase = x1+offset;
                    var = variance[xbase][ybase][1];
                    if (var<min) {min= var; xbase2=xbase; ybase2=ybase;}
                    ybase = y1+offset;
                    var = variance[xbase][ybase][1];
                    if (var<min) {min= var; xbase2=xbase; ybase2=ybase;}
                    xbase = x1; 
                    var = variance[xbase][ybase][1];
                    if (var<min) {min= var; xbase2=xbase; ybase2=ybase;}
                    
                    g = (int)(mean[xbase2][ybase2][1]+0.5);
                    
                    //Blue channel
                    min = Float.MAX_VALUE;
                    xbase = x1; ybase=y1;
                    var = variance[xbase][ybase][2];
                    if (var<min) {min= var; xbase2=xbase; ybase2=ybase;}
                    xbase = x1+offset;
                    var = variance[xbase][ybase][2];
                    if (var<min) {min= var; xbase2=xbase; ybase2=ybase;}
                    ybase = y1+offset;
                    var = variance[xbase][ybase][2];
                    if (var<min) {min= var; xbase2=xbase; ybase2=ybase;}
                    xbase = x1; 
                    var = variance[xbase][ybase][2];
                    if (var<min) {min= var; xbase2=xbase; ybase2=ybase;}
                    
                    b = (int)(mean[xbase2][ybase2][2]+0.5);

                    fastBitmap.setRGB(y1, x1, r, g, b);
                }
            }
        }
        else if(fastBitmap.isGrayscale()){
            int width2 = width+offset;
            int height2 = height+offset;
            float[][] mean = new float[width2][height2];
            float[][] variance = new float[width2][height2];
            double sum, sum2;
            int n, v=0, xbase, ybase;
            for (int y1=0-offset; y1<0+height; y1++) {
                for (int x1=0-offset; x1<0+width; x1++) {
                    sum=0; sum2=0; n=0;
                    for (int x2=x1; x2<x1+size2; x2++) {
                        for (int y2=y1; y2<y1+size2; y2++) {
                            if(x2 > 0 && x2 < width && y2 > 0 && y2 < height){
                                v = copy.getGray(y2, x2);
                                sum += v;
                                sum2 += v*v;
                                n++;
                            }
                            else{
                                v = 0;
                                sum += v;
                                sum2 += v*v;
                                n++;
                            }
                        }
                    }
                    mean[x1+offset][y1+offset] = (float)(sum/n);
                    variance[x1+offset][y1+offset] = (float)(sum2-sum*sum/n);
                }
            }
            
            int xbase2=0, ybase2=0;
            float var, min;
            for (int y1=0; y1<0+height; y1++) {
                for (int x1=0; x1<0+width; x1++) {
                    min = Float.MAX_VALUE;
                    xbase = x1; ybase=y1;
                    var = variance[xbase][ybase];
                    if (var<min) {min= var; xbase2=xbase; ybase2=ybase;}
                    xbase = x1+offset;
                    var = variance[xbase][ybase];
                    if (var<min) {min= var; xbase2=xbase; ybase2=ybase;}
                    ybase = y1+offset;
                    var = variance[xbase][ybase];
                    if (var<min) {min= var; xbase2=xbase; ybase2=ybase;}
                    xbase = x1; 
                    var = variance[xbase][ybase];
                    if (var<min) {min= var; xbase2=xbase; ybase2=ybase;}

                    fastBitmap.setGray(y1, x1, (int)(mean[xbase2][ybase2]+0.5));
                }
            }
        }
    }
}