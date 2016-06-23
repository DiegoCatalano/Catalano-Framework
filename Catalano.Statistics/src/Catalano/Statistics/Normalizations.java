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

/**
 * Data preprocessing describes any type of processing performed on raw data to prepare it for another processing procedure.
 * @ref Computer Imaging - Scott E Umbaugh. Chapter 6 p.283-285.
 * @author Diego Catalano
 */
public class Normalizations {

    /**
     * Don't let anyone instantiate this class.
     */
    private Normalizations() {}
    
    /**
     * Decimal scaling.
     * @param data Data.
     * @return Scaled data.
     */
    public static double[] DecimalScaling(double[] data){
        
        double max = 0;
        for (int i = 0; i < data.length; i++) {
            max = Math.abs(data[i]);
        }
        
        int k = 10;
        while(max / k > 1){
            k *= 10;
        }
        
        double[] result = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            result[i] = data[i] / k;
        }
        
        return result;
        
    }
    
    /**
     * Converts the value x (which is measured in the scale 'from') to another value measured in the scale 'to'.
     * @param data Data.
     * @param fromMin Range min from.
     * @param fromMax Range max from.
     * @param toMin Range min from.
     * @param toMax Range max from.
     * @return Normalized data.
     */
    public static double[] RangeNormalization(double[] data, double fromMin, double fromMax, double toMin, double toMax){
        
        double[] norm = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            norm[i] = (toMax - toMin) * (norm[i] - fromMin) / (fromMax - fromMin) + toMin;
            if (fromMax - fromMin == 0) norm[i] = 0;
        }
        return norm;
        
    }
    
    /**
     * Will modify the feature vectors so that they all have a magnitude of 1. If this is one we will
     * retain only directional information about the vector, which preserves relationships between the features, but loses magnitudes.
     * @param data Data.
     * @return Normalized data.
     */
    public static double[] UnitVectorNormalization(double[] data){
        double[] norm = new double[data.length];
        
        // Compute Magnitude
        double magnitude = 0;
        for (int i = 0; i < data.length; i++) {
            magnitude += data[i] * data[i];
        }
        
        magnitude = Math.sqrt(magnitude);
        
        for (int i = 0; i < data.length; i++) {
            norm[i] = data[i] / magnitude;
        }
        
        return norm;
    }
    
    /**
     * A commonly used statistical-based method to normalize these measures is to take each vector component and subtract the mean and divide
     * by the standard deviation.
     * @param data Data.
     * @return Normalized data.
     */
    public static double[] StandartNormalDensity(double[] data){
        double[] norm = new double[data.length];
        double mean = Catalano.Statistics.DescriptiveStatistics.Mean(data);
        double stdDev = Catalano.Statistics.DescriptiveStatistics.StandartDeviation(data);
        
        for (int i = 0; i < data.length; i++) {
            norm[i] = (data[i] - mean) / stdDev;
        }
        
        return norm;
    }
    
    /**
     * Map the data to a specified range, Smin to Smax, but still retain the relationship between the values.
     * @param data Data.
     * @param min Range min.
     * @param max Range max.
     * @return Normalized data.
     */
    public static double[] MinMaxNormalization(double[] data, double min, double max){
        
        double[] norm = new double[data.length];
        double fMin = Catalano.Statistics.DescriptiveStatistics.Minimum(data);
        double fMax = Catalano.Statistics.DescriptiveStatistics.Maximum(data);
        
        for (int i = 0; i < data.length; i++) {
            norm[i] = ((data[i] - fMin) / (fMax - fMin)) * (max - min) + min;
        }
        
        return norm;
    }
    
    /**
     * Nonlinear method may be desired if the data distribution is skewed.
     * This is essentially a method that compresses the data into the range 0 to 1.
     * @param data Data.
     * @param r Determines the range of values for the feature, that will fall into the linear range.
     * @return Normalized data.
     */
    public static double[] SoftmaxScaling(double[] data, double r){
        
        double[] norm = new double[data.length];
        double mean = Catalano.Statistics.DescriptiveStatistics.Mean(data);
        double stdDev = Catalano.Statistics.DescriptiveStatistics.StandartDeviation(data);
        
        r *= stdDev;
        
        for (int i = 0; i < data.length; i++) {
            double y = (data[i] - mean) / r;
            norm[i] = 1.0 / (1 + Math.pow(Math.E, -y));
        }
        
        return norm;
    }
    
}