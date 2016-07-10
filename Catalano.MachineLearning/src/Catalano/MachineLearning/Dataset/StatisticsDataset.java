// Catalano Machine Learning Library
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

package Catalano.MachineLearning.Dataset;

/**
 * Descriptive statistics for dataset.
 * @author Diego Catalano
 */
public class StatisticsDataset {
    
    private final String name;
    
    private final double  mean;
    private final double  stdDev ;
    private final double  kurtosis;
    private final double  skewness;
    private final double  median ;
    private final double  min;
    private final double  max;
    private final boolean isMissingValues;

    /**
     * Get the name associated attribute.
     * @return Name of the attribute.
     */
    public String getName() {
        return name;
    }

    /**
     * Get mean.
     * @return Mean.
     */
    public double getMean() {
        return mean;
    }

    /**
     * Get standart deviation.
     * @return Standart deviation.
     */
    public double getStdDev() {
        return stdDev;
    }

    /**
     * Get kurtosis.
     * @return Kurtosis.
     */
    public double getKurtosis() {
        return kurtosis;
    }

    /**
     * Get skewness.
     * @return Skewness.
     */
    public double getSkewness() {
        return skewness;
    }

    /**
     * Get median.
     * @return Median.
     */
    public double getMedian() {
        return median;
    }

    /**
     * Get minimum.
     * @return Minimum.
     */
    public double getMin() {
        return min;
    }

    /**
     * Get maximum.
     * @return Maximum.
     */
    public double getMax() {
        return max;
    }

    /**
     * Check if the attribute is missing values.
     * @return True if is missing values, otherwise return false.
     */
    public boolean isMissingValues() {
        return isMissingValues;
    }

    /**
     * Initializes a new instance of the DatasetStatistics class.
     * @param name Name of the attribute.
     * @param mean Mean.
     * @param median Median.
     * @param min Minimum.
     * @param max Maximum.
     * @param std Standart deviation.
     * @param skewness Skewness.
     * @param kurtosis Kurtosis.
     */
    public StatisticsDataset(String name, double mean, double median, double min, double max, double std, double skewness, double kurtosis, boolean isMissingValues) {
        this.name = name;
        this.mean = mean;
        this.median = median;
        this.min = min;
        this.max = max;
        this.stdDev = std;
        this.kurtosis = kurtosis;
        this.skewness = skewness;
        this.isMissingValues = isMissingValues;
    }
}