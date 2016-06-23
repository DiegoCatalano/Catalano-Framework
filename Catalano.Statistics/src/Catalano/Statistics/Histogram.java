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

package Catalano.Statistics;

/**
 * Histogram for discrete random values.
 * @author Diego Catalano
 */
public class Histogram {
    
    private int[]   values;
    private double  mean = 0;
    private double  stdDev = 0;
    private double  entropy = 0;
    private int     median = 0;
    private int     mode;
    private int     min;
    private int     max;
    private long    total;
    private int     bins = 10;
    
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
    
    public static int[] MatchHistograms(Histogram histA, Histogram histB){
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
            P[i] = (double) c / (double)n;
        }
        
        return P;
    }
    
    public static double[] CDF(Histogram hist){
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
    public Histogram(int[] values) {
        this.values = values;
        update();
    }
    
    /**
     * Initializes a new instance of the Histogram class.
     * @param values Values.
     * @param bins Number of bins.
     */
    public Histogram(int[] values, int bins){
        this.values = values;
        this.bins = bins;
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
     * Get number of bins.
     * @return Number of bins.
     */
    public int getBins() {
        return bins;
    }
    
    /**
     * Update histogram.
     */
    private void update(){
        
        int i, n = values.length;

        max = 0;
        min = n;
        total = 0;
        
        int maxVal = -Integer.MAX_VALUE;
        int minVal = Integer.MAX_VALUE;

        // calculate min and max
        for ( i = 0; i < n; i++ )
        {
            if ( values[i] != 0 )
            {
                // max
                if ( i > max )
                    max = i;
                // min
                if ( i < min )
                    min = i;

                maxVal = Math.max(maxVal, values[i]);
                minVal = Math.min(minVal, values[i]);
                total += values[i];
            }
        }
        
        double k = (maxVal - minVal) / (double)bins;
        int[] h = new int[bins];
        
        for (int j = 0; j < values.length; j++) {
            double _min = minVal;
            double _max = _min + k;
            
            //First interval.
            if(values[j] >= _min && values[j] <= _max)
                h[0]++;
            
            _min += k;
            _max += k;
            
            //Others interval.
            for (int l = 1; l < bins; l++) {
                if(values[j] > _min && values[j] <= _max)
                    h[l]++;
                
                _min += k;
                _max += k;
            }
        }
        
        this.values = h;

        mean   = HistogramStatistics.Mean( values );
        stdDev = HistogramStatistics.StdDev( values, mean );
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