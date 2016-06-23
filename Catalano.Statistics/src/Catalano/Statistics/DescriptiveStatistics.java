// Catalano Statistics Library
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

package Catalano.Statistics;

import java.util.Arrays;
/**
 * Descriptive statistics are used to describe the basic features of the data in a study.
 * <br /> They provide simple summaries about the sample and the measures.
 * <br /> @ref http://www.socialresearchmethods.net/kb/statdesc.php
 * @author Diego Catalano
 */
public final class DescriptiveStatistics {
    
     /**
     * Don't let anyone instantiate this class.
     */
    private DescriptiveStatistics(){};
    
    /**
     * The Mean or average is probably the most commonly used method of describing central tendency.
     * <br /> To compute the mean  is add up all the values and divide by the number of values.
     * <br /><br /> Example:
     * <code lang = "none">
     * <br /> 1, 2, 3 ,4 ,5
     * <br /> Mean: 3
     * </code>
     * @param values
     * @return Mean.
     */
    public static double Mean(double[] values){
        double mean = 0;
        for (int i = 0; i < values.length; i++) {
            mean += values[i];
        }
        return mean/values.length;
    }
    
    /**
     * The Mean or average is probably the most commonly used method of describing central tendency.
     * <br /> To compute the mean  is add up all the values and divide by the number of values.
     * <br /><br /> Example:
     * <code lang = "none">
     * <br /> 1, 2, 3 ,4 ,5
     * <br /> Mean: 3
     * </code>
     * @param values
     * @return Mean.
     */
    public static float Mean(float[] values){
        float mean = 0;
        for (int i = 0; i < values.length; i++) {
            mean += values[i];
        }
        return mean/values.length;
    }
    
    /**
     * The Mean or average is probably the most commonly used method of describing central tendency.
     * <br /> To compute the mean  is add up all the values and divide by the number of values.
     * <br /><br /> Example:
     * <code lang = "none">
     * <br /> 1, 2, 3 ,4 ,5
     * <br /> Mean: 3
     * </code>
     * @param values
     * @return Mean.
     */
    public static double Mean(int[] values){
        double mean = 0;
        for (int i = 0; i < values.length; i++) {
            mean += values[i];
        }
        return mean/(double)values.length;
    }
    
    /**
     * The Median is the score found at the exact middle of the set of values.
     * <br /><br /> Example:
     * <code lang = "none">
     * <br /> 5, 3, 8 ,4 ,0
     * <br /> 0, 3, 4, 5, 8
     * <br /> Median: 4
     * </code>
     * @param values Set of values.
     * @return Median.
     */
    public static double Median(double[] values){
        if(values.length == 1)
            return values[0];
        Arrays.sort(values);
        return values[(values.length+1) / 2];
    }
    
    /**
     * The mode is the most frequently occurring value in the set of scores.
     * <br /><br /> Example:
     * <code lang = "none">
     * <br /> 5, 5, 8 ,4 ,0
     * <br /> 0, 4, 5, 5, 8
     * <br /> Mode: 5
     * </code>
     * @param values Set of values.
     * @return Mode.
     */
    public static double Mode(double[] values){
        Arrays.sort(values);
        double v = values[0];
        int index = 0, x = 0, rep = 0;
        for (int i = 1; i < values.length; i++) {
            if (values[i] == v) {
                x++;
                if (x > rep) {
                    rep = x;
                    index = i;
                }
                v = values[i];
                x = 0;
            }
            else{
                if (x > rep) {
                    rep = x;
                    index = i;
                }
                v = values[i];
                x = 0;
            }
        }
        return values[index];
    }
    
    /**
     * The minimum is the minimum among sets of values.
     * @param values Set of values.
     * @return Minimum.
     */
    public static double Minimum(double[] values){
        double min = Double.MAX_VALUE;
        for (int i = 0; i < values.length; i++) {
            if (min > values[i]) {
                min = values[i];
            }
        }
        
        return min;
    }
    
    /**
     * The maximum is the maximum among sets of values.
     * @param values Set of values.
     * @return Maximum.
     */
    public static double Maximum(double[] values){
        double max = Double.MIN_VALUE;
        for (int i = 0; i < values.length; i++) {
            if (max < values[i]) {
                max = values[i];
            }
        }
        
        return max;
    }
    
    /**
     * The variance is a measure of how far a set of numbers is spread out.
     * @param values Set of values.
     * @param mean Mean.
     * @return Variance.
     */
    public static double Variance(double[] values, double mean){
        double sum = 0;
        for (int i = 0; i < values.length; i++) {
            sum += Math.pow((values[i] - mean), 2);
        }
        return sum / ((double)values.length - 1);
    }
    
    /**
     * The variance is a measure of how far a set of numbers is spread out.
     * @param values Set of values.
     * @param mean Mean.
     * @return Variance.
     */
    public static float Variance(float[] values, float mean){
        float sum = 0;
        for (int i = 0; i < values.length; i++) {
            sum += Math.pow((values[i] - mean), 2);
        }
        return sum / ((float)values.length - 1);
    }
    
    /**
     * The variance is a measure of how far a set of numbers is spread out.
     * @param values Set of values.
     * @return Variance.
     */
    public static double Variance(double[] values){
        double mean = Mean(values);
        return Variance(values, mean);
    }
    
    /**
     * The variance is a measure of how far a set of numbers is spread out.
     * @param values Set of values.
     * @return Variance.
     */
    public static float Variance(float[] values){
        float mean = Mean(values);
        return Variance(values, mean);
    }
    
    /**
     * The range is simply the highest value minus the lowest value.
     * <br /><br /> Example:
     * <code lang = "none">
     * <br /> 1, 8, 2 ,5 ,7
     * <br /> Range: 7
     * </code>
     * @param values Sets of values.
     * @return Range.
     */
    public static double Range(double[] values){
        double min = values[0];
        double max = values[0];
        for (int i = 1; i < values.length; i++) {
            min = Math.min(min, values[i]);
            max = Math.max(max, values[i]);
        }
        return max - min;
    }
    
    /**
     * The Standard Deviation is a more accurate and detailed estimate of dispersion.
     * <br /> The Standard Deviation shows the relation that set of scores has to the mean of the sample.
     * @param values Set of values.
     * @return Standart deviation.
     */
    public static double StandartDeviation(double[] values){
        double var = Variance(values);
        return Math.sqrt(var);
    }
    
    /**
     * The Standard Deviation is a more accurate and detailed estimate of dispersion.
     * <br /> The Standard Deviation shows the relation that set of scores has to the mean of the sample.
     * @param values Set of values.
     * @param mean Meaan.
     * @return Standart deviation.
     */
    public static double StandartDeviation(double[] values, double mean){
        double var = Variance(values, mean);
        return Math.sqrt(var);
    }
    
    /**
     * Kurtosis is a measure of whether the data are peaked or flat relative to a normal distribution.
     * @param values Set of values.
     * @param mean Mean.
     * @param stdDeviation Standart deviation.
     * @return Kurtosis.
     */
    public static double Kurtosis(double[] values, double mean, double stdDeviation){
        double n = values.length;
        
        double part1 = n * (n + 1);
        part1 /= ((n - 1) * (n - 2) * (n - 3));
        
        double part2 = 0;
        for (int i = 0; i < values.length; i++) {
            part2 += Math.pow((values[i] - mean) / stdDeviation, 4);
        }
        
        double part3 = 3 * Math.pow((n - 1), 2);
        part3 /= (n - 2) * (n - 3);
        
        return part1 * part2 - part3;
        
    }
    
    /**
     * Kurtosis is a measure of whether the data are peaked or flat relative to a normal distribution.
     * @param values Set of values.
     * @return Kurtosis.
     */
    public static double Kurtosis(double[] values){
        double mean = Mean(values);
        double std = StandartDeviation(values);
        return Kurtosis(values, mean, std);
    }
    
    /**
     * Skewness is a measure of symmetry, or more precisely, the lack of symmetry.
     * <br /> A distribution, or data set, is symmetric if it looks the same to the left and right of the center point.
     * @param values Set of values.
     * @param mean Mean.
     * @param stdDeviation Standart deviation.
     * @return Skewness.
     */
    public static double Skewness(double[] values, double mean, double stdDeviation){
        double n = values.length;
        
        double part1 = n / (n - 1) * (n - 2);
        
        double part2 = 0;
        for (int i = 0; i < values.length; i++) {
            part2 += Math.pow((values[i] - mean) / stdDeviation, 3);
        }
        
        return part1 * part2;
    }
    
    /**
     * Skewness is a measure of symmetry, or more precisely, the lack of symmetry.
     * <br /> A distribution, or data set, is symmetric if it looks the same to the left and right of the center point.
     * @param values Set of values.
     * @return Skewness.
     */
    public static double Skewness(double[] values){
        double mean = Mean(values);
        double std = StandartDeviation(values);
        return Skewness(values, mean, std);
    }
    
}