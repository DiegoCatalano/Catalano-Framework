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
 * Weighted Median filter.
 * The Weighted median filter is like median filter, but the weights means the "number of votes" of the position.
 * 
 * @author Diego Catalano
 */
public class WeightedMedian implements IApplyInPlace{
    
    private int[][] weight = new int[][] {
        {1,2,1},
        {2,3,2},
        {1,2,1}
    };

    /**
     * Get Weight.
     * @return Weight.
     */
    public int[][] getWeight() {
        return weight;
    }

    /**
     * Set Weight.
     * @param weight Weight.
     */
    public void setWeight(int[][] weight) {
        this.weight = weight;
    }

    /**
     * Initializes a new instance of the WeightedMedian class.
     */
    public WeightedMedian() {}

    /**
     * Initializes a new instance of the WeightedMedian class.
     * @param weight Weight.
     */
    public WeightedMedian(int[][] weight) {
        setWeight(weight);
    }
    
    @Override
    public void applyInPlace(FastBitmap fastBitmap){
        
        int width = fastBitmap.getWidth();
        int height = fastBitmap.getHeight();
        int Xline,Yline;
        int radiusI = (weight.length - 1) / 2;
        int radiusJ = (weight[0].length - 1) / 2;
        int maxArray = calcMax(weight);
        int c;
        
        FastBitmap copy = new FastBitmap(fastBitmap);
        
        if (fastBitmap.isGrayscale()) {
            int[] avgL = new int [maxArray];
            int median;
            for (int x = 0; x < height; x++) {
                for (int y = 0; y < width; y++) {
                    c = 0;
                    for (int i = 0; i < weight.length; i++) {
                        Xline = x + (i-radiusI);
                        for (int j = 0; j < weight[0].length; j++) {
                            Yline = y + (j-radiusJ);
                            if ((Xline >= 0) && (Xline < height) && (Yline >=0) && (Yline < width)) {
                                if(weight[i][j] > 0){
                                    for (int k = 0; k < weight[i][j]; k++) {
                                        avgL[c] = copy.getGray(Xline, Yline);
                                        c++;
                                    }
                                }
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
                    for (int i = 0; i < weight.length; i++) {
                        Xline = x + (i-radiusI);
                        for (int j = 0; j < weight[0].length; j++) {
                            Yline = y + (j-radiusJ);
                            if ((Xline >= 0) && (Xline < height) && (Yline >=0) && (Yline < width)) {
                                
                                if(weight[i][j] > 0){
                                    for (int k = 0; k < weight[i][j]; k++) {
                                    avgR[c] = copy.getRed(Xline, Yline);
                                    avgG[c] = copy.getGreen(Xline, Yline);
                                    avgB[c] = copy.getBlue(Xline, Yline);
                                    c++;
                                    }
                                }
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
    
    private int calcMax(int[][] weight){
        int s = 0;
        for (int i = 0; i < weight.length; i++) {
            for (int j = 0; j < weight[0].length; j++) {
                if(weight[i][j] > 0)
                    s += weight[i][j];
            }
        }
        return s;
    }
}