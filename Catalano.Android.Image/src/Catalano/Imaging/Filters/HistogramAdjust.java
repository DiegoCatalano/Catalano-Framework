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
import Catalano.Imaging.Tools.ImageStatistics;
import Catalano.Statistics.Histogram;

/**
 * Histogram adjust.
 * Histogram adjust based on CDF.
 * @author Diego Catalano
 */
public class HistogramAdjust implements IApplyInPlace{
    
    private double tolerance;
    private int min = 0;
    private int max = 255;

    /**
     * Get the tolerance.
     * @return Tolerance.
     */
    public double getTolerance() {
        return tolerance;
    }

    /**
     * Set the tolerance.
     * @param tolerance Tolerance.
     */
    public void setTolerance(double tolerance) {
        this.tolerance = tolerance;
    }

    /**
     * Initializes a new instance of the HistogramAdjust class.
     */
    public HistogramAdjust() {
        this(0,255);
    }
    /**
     * Initializes a new instance of the HistogramAdjust class.
     * @param tolerance Tolerance.
     */
    public HistogramAdjust(double tolerance) {
        this(0,255,tolerance);
    }
    
    /**
     * Initializes a new instance of the HistogramAdjust class.
     * @param min Minimum value.
     * @param max Maximum value.
     */
    public HistogramAdjust(int min, int max){
        this(min,max,0.01);
    }
    
    /**
     * Initializes a new instance of the HistogramAdjust class.
     * @param min Minimum value.
     * @param max Maximum value.
     * @param tolerance Tolerance.
     */
    public HistogramAdjust(int min, int max, double tolerance){
        this.min = min;
        this.max = max;
        this.tolerance = tolerance;
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        if(fastBitmap.isGrayscale()){
            ImageStatistics stat = new ImageStatistics(fastBitmap);
            int[] hist = stat.getHistogramGray().getValues();
            
            //Compute CDF
            double[] cdf = Histogram.CDF(hist);
            
            int minV = 255;
            int maxV = 0;
            
            double maxTolerance = 1 - tolerance;
            for (int i = 0; i < cdf.length; i++) {
                if(cdf[i] > tolerance){
                    minV = i;
                    break;
                }
            }
            
            for (int i = 0; i < cdf.length; i++) {
                if(cdf[i] > maxTolerance){
                    maxV = i;
                    break;
                }
            }
            
            int size = fastBitmap.getSize();
            for (int x = 0; x < size; x++) {
                int gray = fastBitmap.getGray(x);
                double stretch = (((gray - minV)/(double)(maxV - minV)) * (double)(max - min)) + min;
                fastBitmap.setGray(x, fastBitmap.clampValues((int)stretch,0,255));
            }
        }
        else if(fastBitmap.isRGB()){
            ImageStatistics stat = new ImageStatistics(fastBitmap);
            int[] histR = stat.getHistogramRed().getValues();
            int[] histG = stat.getHistogramGreen().getValues();
            int[] histB = stat.getHistogramBlue().getValues();
            
            //Compute CDF
            double[] cdfR = Histogram.CDF(histR);
            double[] cdfG = Histogram.CDF(histG);
            double[] cdfB = Histogram.CDF(histB);
            
            int minV_R = 255;
            int minV_G = 255;
            int minV_B = 255;
            
            int maxV_R = 0;
            int maxV_G = 0;
            int maxV_B = 0;
            
            double maxTolerance = 1 - tolerance;
            for (int i = 0; i < cdfR.length; i++) {
                if(cdfR[i] > tolerance){
                    minV_R = i;
                    break;
                }
            }
            for (int i = 0; i < cdfG.length; i++) {
                if(cdfG[i] > tolerance){
                    minV_G = i;
                    break;
                }
            }
            for (int i = 0; i < cdfB.length; i++) {
                if(cdfB[i] > tolerance){
                    minV_B = i;
                    break;
                }
            }
            
            //Max
            for (int i = 0; i < cdfR.length; i++) {
                if(cdfR[i] > maxTolerance){
                    maxV_R = i;
                    break;
                }
            }
            for (int i = 0; i < cdfG.length; i++) {
                if(cdfG[i] > maxTolerance){
                    maxV_G = i;
                    break;
                }
            }
            for (int i = 0; i < cdfB.length; i++) {
                if(cdfB[i] > maxTolerance){
                    maxV_B = i;
                    break;
                }
            }
            
            int size = fastBitmap.getSize();
            for (int x = 0; x < size; x++) {
                int r = fastBitmap.getRed(x);
                int g = fastBitmap.getGreen(x);
                int b = fastBitmap.getBlue(x);
                
                
                int sR = fastBitmap.clampValues((int)(((r - minV_R)/(double)(maxV_R - minV_R)) * (double)(max - min)) + min,0,255);
                int sG = fastBitmap.clampValues((int)(((g - minV_G)/(double)(maxV_G - minV_G)) * (double)(max - min)) + min,0,255);
                int sB = fastBitmap.clampValues((int)(((b - minV_B)/(double)(maxV_B - minV_B)) * (double)(max - min)) + min,0,255);
                
                fastBitmap.setRGB(x, sR, sG, sB);
            }
        }
    }
}