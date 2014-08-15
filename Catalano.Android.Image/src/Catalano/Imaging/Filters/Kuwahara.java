// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2014
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
import Catalano.Imaging.IBaseInPlace;
import Catalano.Statistics.DescriptiveStatistics;

/**
 * Kuwahara filter is able to apply smoothing on the image while preserving the edges.
 * @author Diego Catalano
 */
public class Kuwahara implements IBaseInPlace{
    
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
        
        FastBitmap copy = new FastBitmap(fastBitmap);
        
        if (fastBitmap.isRGB()) {
            int steps = calcSteps(windowSize);
            float meanR = 0; float varianceR = Float.MAX_VALUE;
            float meanG = 0; float varianceG = Float.MAX_VALUE;
            float meanB = 0; float varianceB = Float.MAX_VALUE;
            
            for (int x = 0; x < height; x++) {
                for (int y = 0; y < width; y++) {
                    
                    int index = 0;
                    int steps2 = steps * steps;
                    float tMeanR = 0; float tVarianceR = Float.MAX_VALUE;
                    float tMeanG = 0; float tVarianceG = Float.MAX_VALUE;
                    float tMeanB = 0; float tVarianceB = Float.MAX_VALUE;
                    float[] valuesR = new float[steps2];
                    float[] valuesG = new float[steps2];
                    float[] valuesB = new float[steps2];
                    
                    // 1: Region
                    for (int i = x - steps; i < x; i++) {
                        for (int j = y - steps; j < y; j++) {
                            
                            if ((i >= 0) && (i < height) && (j >=0) && (j < width)) {
                                valuesR[index] = (float)copy.getRed(i, j);
                                valuesG[index] = (float)copy.getGreen(i, j);
                                valuesB[index] = (float)copy.getBlue(i, j);
                            }
                            else{
                                valuesR[index] = valuesG[index] = valuesB[index] = 0;
                            }
                            
                            index++;
                            
                        }
                    }
                    
                    meanR = DescriptiveStatistics.Mean(valuesR);
                    meanG = DescriptiveStatistics.Mean(valuesG);
                    meanB = DescriptiveStatistics.Mean(valuesB);
                    
                    varianceR = DescriptiveStatistics.Variance(valuesR, meanR);
                    varianceG = DescriptiveStatistics.Variance(valuesG, meanG);
                    varianceB = DescriptiveStatistics.Variance(valuesB, meanB);
                    index = 0;
                    
                    // 2: Region
                    for (int i = x; i < x + steps; i++) {
                        for (int j = y + 1; j <= y + steps; j++) {
                            
                            if ((i >= 0) && (i < height) && (j >=0) && (j < width)) {
                                valuesR[index] = (float)copy.getRed(i, j);
                                valuesG[index] = (float)copy.getGreen(i, j);
                                valuesB[index] = (float)copy.getBlue(i, j);
                            }
                            else{
                                valuesR[index] = valuesG[index] = valuesB[index] = 0;
                            }
                            
                            index++;
                        }
                    }
                    
                    tMeanR = DescriptiveStatistics.Mean(valuesR);
                    tMeanG = DescriptiveStatistics.Mean(valuesG);
                    tMeanB = DescriptiveStatistics.Mean(valuesB);
                    
                    tVarianceR = DescriptiveStatistics.Variance(valuesR, tMeanR);
                    tVarianceG = DescriptiveStatistics.Variance(valuesG, tMeanG);
                    tVarianceB = DescriptiveStatistics.Variance(valuesB, tMeanB);
                    
                    if (tVarianceR < varianceR) {
                        varianceR = tVarianceR;
                        meanR = tMeanR;
                    }
                    if (tVarianceG < varianceG) {
                        varianceG = tVarianceG;
                        meanG = tMeanG;
                    }
                    if (tVarianceB < varianceB) {
                        varianceB = tVarianceB;
                        meanB = tMeanB;
                    }
                    
                    
                    index = 0;
                    
                    // 3: Region
                    for (int i = x + 1; i <= x + steps; i++) {
                        for (int j = y + 1; j <= y + steps; j++) {
                            
                            //System.err.println("i: " + i + " j: " + j);
                            
                            if ((i >= 0) && (i < height) && (j >=0) && (j < width)) {
                                valuesR[index] = (float)copy.getRed(i, j);
                                valuesG[index] = (float)copy.getGreen(i, j);
                                valuesB[index] = (float)copy.getBlue(i, j);
                            }
                            else{
                                valuesR[index] = valuesG[index] = valuesB[index] = 0;
                            }
                            
                            index++;
                        }
                    }
                    
                    tMeanR = DescriptiveStatistics.Mean(valuesR);
                    tMeanG = DescriptiveStatistics.Mean(valuesG);
                    tMeanB = DescriptiveStatistics.Mean(valuesB);
                    
                    tVarianceR = DescriptiveStatistics.Variance(valuesR, tMeanR);
                    tVarianceG = DescriptiveStatistics.Variance(valuesG, tMeanG);
                    tVarianceB = DescriptiveStatistics.Variance(valuesB, tMeanB);
                    
                    if (tVarianceR < varianceR) {
                        varianceR = tVarianceR;
                        meanR = tMeanR;
                    }
                    if (tVarianceG < varianceG) {
                        varianceG = tVarianceG;
                        meanG = tMeanG;
                    }
                    if (tVarianceB < varianceB) {
                        varianceB = tVarianceB;
                        meanB = tMeanB;
                    }
                    
                    index = 0;
                    
                    // 4: Region
                    for (int i = x + 1; i <= x + steps; i++) {
                        for (int j = y - steps; j < y; j++) {
                            
                            if ((i >= 0) && (i < height) && (j >=0) && (j < width)) {
                                valuesR[index] = (float)copy.getRed(i, j);
                                valuesG[index] = (float)copy.getGreen(i, j);
                                valuesB[index] = (float)copy.getBlue(i, j);
                            }
                            else{
                                valuesR[index] = valuesG[index] = valuesB[index] = 0;
                            }
                            
                            index++;
                        }
                    }
                    
                    tMeanR = DescriptiveStatistics.Mean(valuesR);
                    tMeanG = DescriptiveStatistics.Mean(valuesG);
                    tMeanB = DescriptiveStatistics.Mean(valuesB);
                    
                    tVarianceR = DescriptiveStatistics.Variance(valuesR, tMeanR);
                    tVarianceG = DescriptiveStatistics.Variance(valuesG, tMeanG);
                    tVarianceB = DescriptiveStatistics.Variance(valuesB, tMeanB);
                    
                    if (tVarianceR < varianceR) {
                        varianceR = tVarianceR;
                        meanR = tMeanR;
                    }
                    if (tVarianceG < varianceG) {
                        varianceG = tVarianceG;
                        meanG = tMeanG;
                    }
                    if (tVarianceB < varianceB) {
                        varianceB = tVarianceB;
                        meanB = tMeanB;
                    }
                    
                    fastBitmap.setRGB(x, y, (int)meanR, (int)meanG, (int)meanB);
                    
                }
            }
        }
        else{
            int steps = calcSteps(windowSize);
            float mean = 0; float variance = Float.MAX_VALUE;
            
            for (int x = 0; x < height; x++) {
                for (int y = 0; y < width; y++) {
                    
                    int index = 0;
                    float tMean = 0; float tVariance = Float.MAX_VALUE;
                    float[] values = new float[steps * steps];
                    
                    // 1: Region
                    for (int i = x - steps; i < x; i++) {
                        for (int j = y - steps; j < y; j++) {
                            
                            if ((i >= 0) && (i < height) && (j >=0) && (j < width)) {
                                values[index] = (float)copy.getGray(i, j);
                            }
                            else{
                                values[index] = 0;
                            }
                            
                            index++;
                            
                        }
                    }
                    
                    mean = DescriptiveStatistics.Mean(values);
                    variance = DescriptiveStatistics.Variance(values, mean);
                    index = 0;
                    
                    // 2: Region
                    for (int i = x; i < x + steps; i++) {
                        for (int j = y + 1; j <= y + steps; j++) {
                            
                            if ((i >= 0) && (i < height) && (j >=0) && (j < width)) {
                                values[index] = (float)copy.getGray(i, j);
                            }
                            else{
                                values[index] = 0;
                            }
                            
                            index++;
                        }
                    }
                    
                    tMean = DescriptiveStatistics.Mean(values);
                    tVariance = DescriptiveStatistics.Variance(values, tMean);
                    
                    if (tVariance < variance) {
                        variance = tVariance;
                        mean = tMean;
                    }
                    
                    
                    index = 0;
                    
                    // 3: Region
                    for (int i = x + 1; i <= x + steps; i++) {
                        for (int j = y + 1; j <= y + steps; j++) {
                            
                            //System.err.println("i: " + i + " j: " + j);
                            
                            if ((i >= 0) && (i < height) && (j >=0) && (j < width)) {
                                values[index] = (float)copy.getGray(i, j);
                            }
                            else{
                                values[index] = 0;
                            }
                            
                            index++;
                        }
                    }
                    
                    tMean = DescriptiveStatistics.Mean(values);
                    tVariance = DescriptiveStatistics.Variance(values, tMean);
                    
                    if (tVariance < variance) {
                        variance = tVariance;
                        mean = tMean;
                    }
                    
                    index = 0;
                    
                    // 4: Region
                    for (int i = x + 1; i <= x + steps; i++) {
                        for (int j = y - steps; j < y; j++) {
                            
                            if ((i >= 0) && (i < height) && (j >=0) && (j < width)) {
                                values[index] = (float)copy.getGray(i, j);
                            }
                            else{
                                values[index] = 0;
                            }
                            
                            index++;
                        }
                    }
                    
                    tMean = DescriptiveStatistics.Mean(values);
                    tVariance = DescriptiveStatistics.Variance(values, tMean);
                    
                    if (tVariance < variance) {
                        variance = tVariance;
                        mean = tMean;
                    }
                    
                    fastBitmap.setGray(x, y, (int)mean);
                    
                }
            }
        }
    }
    
    private int calcSteps(int windowSize){
        return (windowSize + 1) / 2 - 1;
    }
}
