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

package Catalano.Imaging.Tools;

import Catalano.Statistics.HistogramStatistics;

/**
 * Image Histogram for random values.
 * @author Diego Catalano
 */
public class ImageHistogram {
    
    private int[]   values;
    
    private double  mean = 0;
    private double  stdDev = 0;
    private double  entropy = 0;
    private double  kurtosis = 0;
    private double  skewness = 0;
    private int     median = 0;
    private int     mode;
    private int     min;
    private int     max;
    private long    total;
    
    public static int[] MatchHistograms(int[] histA, int[] histB){
        int length = histA.length;
        double[] PA = CDF(histA);
        double[] PB = CDF(histB);
        int[] F = new int[length];
        
        for (int a = 0; a < length; a++) {
            int j = length - 1;
            do {
                F[a] = j;
                j--;
            } while (j >= 0 && PA[a] <= PB[j]);
        }
        
        return F;
    }
    
    public static int[] MatchHistograms(ImageHistogram histA, ImageHistogram histB){
        return MatchHistograms(histA.values, histB.values);
    }
    
    public static double[] CDF(int[] values){
        int length = values.length;
        int n = 0;
        
        for (int i = 0; i < length; i++) {
            n += values[i];
        }
        
        double[] P = new double[length];
        int c = values[0];
        P[0] = (double) c / n;
        for (int i = 1; i < length; i++) {
            c += values[i];
            P[i] = (double) c / n;
        }
        
        return P;
    }
    
    public static double[] CDF(ImageHistogram hist){
        return CDF(hist.values);
    }
    
    /**
     * Normalize histogram.
     * @param values Values.
     * @return Normalized histogram.
     */
    public static double[] Normalize(int[] values){
        int sum = 0;
        for (int i = 0; i < values.length; i++) {
            sum += values[i];
        }
        
        double[] norm = new double[values.length];
        for (int i = 0; i < norm.length; i++) {
            norm[i] = values[i] / (double)sum;
        }
        
        return norm;
    }

    /**
     * Initializes a new instance of the Histogram class.
     * @param values Values.
     */
    public ImageHistogram(int[] values) {
        this.values = values;
        update();
    }

    /**
     * Get values of the histogram.
     * @return Values.
     */
    public int[] getValues() {
        return values;
    }

    /**
     * Get mean value.
     * @return Mean.
     */
    public double getMean() {
        return mean;
    }

    /**
     * Get standart deviation value.
     * @return Standart deviation.
     */
    public double getStdDev() {
        return stdDev;
    }
    
    /**
     * Get entropy value.
     * @return Entropy.
     */
    public double getEntropy(){
        return entropy;
    }

    /**
     * Get kurtosis value.
     * @return Kurtosis.
     */
    public double getKurtosis() {
        return kurtosis;
    }

    /**
     * Get skewness value.
     * @return Skewness.
     */
    public double getSkewness() {
        return skewness;
    }

    /**
     * Get median value.
     * @return Median.
     */
    public int getMedian() {
        return median;
    }
    
    /**
     * Get mode value.
     * @return Mode.
     */
    public int getMode(){
        return mode;
    }

    /**
     * Get minimum value.
     * @return Minimum.
     */
    public int getMin() {
        return min;
    }

    /**
     * Get maximum value.
     * @return Maximum.
     */
    public int getMax() {
        return max;
    }

    /**
     * Get the sum of pixels.
     * @return 
     */
    public long getTotal() {
        return total;
    }
    
    /**
     * Update histogram.
     */
    private void update(){
        
        total = 0;
        for (int i = 0; i < values.length; i++) {
            total += values[i];
        }

        mean   = HistogramStatistics.Mean( values );
        stdDev = HistogramStatistics.StdDev( values, mean );
        kurtosis = HistogramStatistics.Kurtosis(values, mean, stdDev);
        skewness = HistogramStatistics.Skewness(values, mean, stdDev);
        median = HistogramStatistics.Median( values );
        mode = HistogramStatistics.Mode(values);
        entropy = HistogramStatistics.Entropy(values);
    }
    
    /**
     * Normalize histogram.
     * @return Normalized histogram.
     */
    public double[] Normalize(){
        double[] h = new double[values.length];
        for (int i = 0; i < h.length; i++) {
            h[i] = values[i] / (double)total;
        }
        return h;
    }
}