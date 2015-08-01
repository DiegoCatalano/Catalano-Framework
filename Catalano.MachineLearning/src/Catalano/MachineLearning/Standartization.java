// Catalano Machine Learning Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2015
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

package Catalano.MachineLearning;

import Catalano.Core.DoubleRange;
import Catalano.Math.Matrix;
import java.util.ArrayList;
import java.util.List;

/**
 * Standartize matrix.
 * Standartize each column in the matrix.
 * x = (x - u) / s;
 * 
 * @author Diego Catalano
 */
public class Standartization {
    
    private double mean;
    private double std;
    private List<DoubleRange> range;
    
    boolean find;

    public double getMean() {
        return mean;
    }

    public void setMean(double mean) {
        this.mean = mean;
    }

    public double getStd() {
        return std;
    }

    public void setStd(double std) {
        this.std = std;
    }

    /**
     * Get range normalization.
     * @return Range normalization.
     */
    public List<DoubleRange> getRangeNormalization() {
        return range;
    }

    /**
     * Set range normalization.
     * @param range Range normalization.
     */
    public void setRangeNormalization(List<DoubleRange> range) {
        this.range = range;
    }

    /**
     * Initializes a new instance of the Standartization class.
     */
    public Standartization() {
        this.find = true;
    }

    public Standartization(double mean, double standardDeviation){
        this.mean = mean;
        this.std = standardDeviation;
        this.find = false;
    }
    
    /**
     * Normalize matrix.
     * @param data Matrix.
     * @return Normalized matrix.
     */
    public double[][] Normalize(double[][] data){
        
        range = new ArrayList<DoubleRange>();
        double[][] matrix = new double[data.length][data[0].length];
        
        if(find == true){
            for (int i = 0; i < data[0].length; i++) {
                double[] temp = Matrix.getColumn(data, i);
                double _mean = Catalano.Statistics.Tools.Mean(temp);
                double _std = Catalano.Statistics.Tools.StandartDeviation(temp, mean);
                if(range != null) range.add(new DoubleRange(_mean,_std));
                for (int j = 0; j < temp.length; j++) {
                    matrix[j][i] = (data[j][i] - _mean) / _std;
                }
            }
        }
        else{
            for (int i = 0; i < data[0].length; i++) {
                double[] temp = Matrix.getColumn(data, i);
                if(range != null) range.add(new DoubleRange(mean,std));
                for (int j = 0; j < temp.length; j++) {
                    matrix[j][i] = (data[j][i] - mean) / std;
                }
            }
        }
        
        return matrix;
    }
    
    /**
     * Apply range normalization in the specified feature.
     * @param feature Feature.
     * @return Normalized feature.
     */
    public double[] ApplyRangeNormalization(double[] feature){
        
        if(range == null)
            throw new IllegalArgumentException("The matrix must be standartized.");
        
        double[] norm = new double[feature.length];
        for (int i = 0; i < norm.length; i++)
            norm[i] = (feature[i] - range.get(i).getMin()) / range.get(i).getMax();
        
        
        return norm;
    }
    
    /**
     * Apply range normalization in the given instances.
     * @param matrix Matrix.
     * @return Normalized instances.
     */
    public double[][] ApplyRangeNormalization(double[][] matrix){
        
        if(range == null)
            throw new IllegalArgumentException("The matrix must be stardartized.");
        
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[0].length; j++)
                matrix[i][j] =  (matrix[i][j] - range.get(j).getMin()) / range.get(j).getMax();
        
        return matrix;
    }
}