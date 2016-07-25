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

package Catalano.Imaging.Texture.BinaryPattern;

import Catalano.Core.ArraysUtil;
import Catalano.Imaging.Tools.*;
import Catalano.Imaging.FastBitmap;

/**
 * Local Ternary Pattern (LTP) is a type of feature used for classification in computer vision.
 * 
 * References: Tan, Xiaoyang, and Bill Triggs. "Enhanced local texture feature sets for face recognition under difficult lighting conditions."
 * Image Processing, IEEE Transactions on 19.6 (2010): 1635-1650.
 * 
 * @author Diego Catalano
 */
public class LocalTernaryPattern implements IBinaryPattern{
    
    private int threshold = 5;
    private ImageHistogram upperHistogram;
    private ImageHistogram lowerHistogram;

    /**
     * Get threshold value.
     * @return Threshold value.
     */
    public int getThreshold() {
        return threshold;
    }

    /**
     * Set threshold value.
     * @param threshold Threshold value.
     */
    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    /**
     * Get the Upper histogram.
     * @return Histogram.
     */
    public ImageHistogram getUpperHistogram() {
        return upperHistogram;
    }

    /**
     * Get the Lower histogram.
     * @return Histogram.
     */
    public ImageHistogram getLowerHistogram() {
        return lowerHistogram;
    }

    /**
     * Initialize a new instance of the LocalTernaryPattern class.
     */
    public LocalTernaryPattern() {}
    
    /**
     * Initialize a new instance of the LocalTernaryPattern class.
     * @param threshold Threshold.
     */
    public LocalTernaryPattern(int threshold){
        this.threshold = threshold;
    }
    
    /**
     * Process the image.
     * @param fastBitmap Image to be processed.
     */
    @Override
    public ImageHistogram ComputeFeatures(FastBitmap fastBitmap){
        if(!fastBitmap.isGrayscale())
            throw new IllegalArgumentException("Local Ternary Pattern only works in grayscale images.");
        
        
        int[] upper = new int[256];
        int[] lower = new int[256];
        
        int sumU;
        int sumL;
        
        int width = fastBitmap.getWidth();
        int height = fastBitmap.getHeight();
        for (int i = 1; i < height - 1; i++) {
            for (int j = 1; j < width - 1; j++) {
                
                sumU = sumL = 0;
                
                //Compute ternary
                int[][] ternary = TernaryMatrix(fastBitmap, i, j);
                
                //Split in upper and lower binary patterns.
                int bin = 128;
                for (int k = 0; k < 3; k++) {
                    if(ternary[0][k] == 1) sumU += bin;
                    if(ternary[0][k] == -1) sumL += bin;
                    bin /= 2;
                }
                
                if(ternary[1][2] == 1) sumU += bin;
                if(ternary[1][2] == -1) sumL += bin;
                bin /= 2;
                
                for (int k = 0; k < 3; k++) {
                    if(ternary[2][2-k] == 1) sumU += bin;
                    if(ternary[2][2-k] == -1) sumL += bin;
                    bin /= 2;
                }
                
                upper[sumU]++;
                lower[sumL]++;
                
            }
        }
        
        this.upperHistogram = new ImageHistogram(upper);
        this.lowerHistogram = new ImageHistogram(lower);
        
        //Concatenate the histograms.
        int[] all = ArraysUtil.Concatenate(upper, lower);
        
        return new ImageHistogram(all);
        
    }
    
    private int[][] TernaryMatrix(FastBitmap fastBitmap, int i, int j){
        
        int[][] ternary = new int[3][3];
        int x = 0, y;
        
        int c = fastBitmap.getGray(i, j);
        for (int k = i - 1; k <= i + 1; k++) {
            y = 0;
            for (int l = j - 1; l <= j + 1; l++) {
                if(fastBitmap.getGray(k, l) > c + threshold)
                    ternary[x][y] = 1;
                if(fastBitmap.getGray(k, l) > c - threshold && fastBitmap.getGray(k, l) < c + threshold)
                    ternary[x][y] = 0;
                if(fastBitmap.getGray(k, l) < c - threshold)
                    ternary[x][y] = -1;
                y++;
            }
            x++;
        }
        
        return ternary;
    }
}