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

package Catalano.MachineLearning.Regression;

import Catalano.Statistics.Correlations;
import Catalano.Statistics.Tools;

/**
 * Regression Measure.
 * @author Diego Catalano
 */
public class RegressionMeasure {
    
    private final double mae;
    private final double mse;
    private final double rmse;
    private final double r2;
    
    public static double MeanAbsoluteError(double[] actual, double[] predicted){
        if(actual.length != predicted.length)
            throw new IllegalArgumentException("The lenght of the vectors must be the same.");
        
        //Mean absolute error
        double sum = 0;
        for (int i = 0; i < actual.length; i++) {
            sum += Math.abs(actual[i] - predicted[i]);
        }
        sum /= actual.length;
        
        return sum;
    }
    
    public static double MeanSquaredError(double[] actual, double[] predicted){
        if(actual.length != predicted.length)
            throw new IllegalArgumentException("The lenght of the vectors must be the same.");
        
        //Mean squared error
        double sum = 0;
        for (int i = 0; i < actual.length; i++) {
            sum += Math.pow(actual[i] - predicted[i],2);
        }
        sum /= actual.length;
        
        return sum;
    }
    
    public static double NormalizedMeanSquareError(double[] actual, double[] predicted){
        double m = Tools.Mean(actual)*Tools.Mean(predicted);
        
        double sum = 0;
        for (int i = 0; i < actual.length; i++) {
            sum += Math.pow(actual[i] - predicted[i], 2) / m;
        }
        
        return sum / (double)actual.length;
    }
    
    public static double RootMeanSquaredError(double[] actual, double[] predicted){
        if(actual.length != predicted.length)
            throw new IllegalArgumentException("The lenght of the vectors must be the same.");
        
        //Mean squared error
        double sum = 0;
        for (int i = 0; i < actual.length; i++) {
            sum += Math.pow(actual[i] - predicted[i],2);
        }
        sum /= actual.length;
        
        return Math.sqrt(sum);
    }
    
    public static double CoefficientOfDetermination(double[] actual, double[] predicted){
        return Math.pow(Correlations.PearsonCorrelation(actual, predicted), 2);
    }
    
    /**
     * Get the mean absolute error.
     * @return Mean absolute error.
     */
    public double getMeanAbsoluteError() {
        return mae;
    }
    
    /**
     * Get the mean squared error.
     * @return Mean squared error.
     */
    public double getMeanSquaredError(){
        return mse;
    }
    
    /**
     * Get the root mean squared error.
     * @return Root mean squared error.
     */
    public double getRootMeanSquaredError(){
        return rmse;
    }
    
    /**
     * Get the coefficient of determination (R2).
     * @return Coefficient of determination.
     */
    public double getCoefficientOfDetermination(){
        return r2;
    }
    
    /**
     * Initializes a new instance of the RegressionMeasure class.
     * @param mae
     * @param mse
     * @param rmse
     * @param r2 
     */
    public RegressionMeasure(double mae, double mse, double rmse, double r2){
        this.mae = mae;
        this.mse = mse;
        this.rmse = rmse;
        this.r2 = r2;
    }
}