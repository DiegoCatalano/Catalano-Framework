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
 * Normalization matrix.
 * Normalize each column in the matrix of given matrix.
 * 
 * @author Diego Catalano
 */
public class Normalization {
    
    private double min = 0;
    private double max = 1;
    private List<DoubleRange> range;

    /**
     * Get minimum value.
     * @return Minimum value.
     */
    public double getMin() {
        return min;
    }

    /**
     * Set minimum value.
     * @param min Minimum value.
     */
    public void setMin(double min) {
        this.min = min;
    }

    /**
     * Get maximum value.
     * @return Maximum value.
     */
    public double getMax() {
        return max;
    }

    /**
     * Set maximum value.
     * @param max Maximum value.
     */
    public void setMax(double max) {
        this.max = max;
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
     * Initializes a new instance of the Normalization class.
     */
    public Normalization() {}
    
    /**
     * Initializes a new instance of the Normalization class.
     * @param min Minimum value.
     * @param max Maximum value.
     */
    public Normalization(double min, double max){
        this.min = min;
        this.max = max;
    }
    
    /**
     * Normalize matrix.
     * @param data Matrix.
     * @return Normalized matrix.
     */
    public double[][] Normalize(double[][] data){
        
        range = new ArrayList<DoubleRange>();
        double[][] matrix = new double[data.length][data[0].length];
        
        for (int i = 0; i < data[0].length; i++) {
            double[] temp = Matrix.getColumn(data, i);
            double _min = Catalano.Statistics.Tools.Min(temp);
            double _max = Catalano.Statistics.Tools.Max(temp);
            if(range != null) range.add(new DoubleRange(_min,_max));
            for (int j = 0; j < temp.length; j++) {
                matrix[j][i] = Catalano.Math.Tools.Scale(_min, _max, min, max, temp[j]);
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
            throw new IllegalArgumentException("The matrix must be normalized.");
        
        double[] norm = new double[feature.length];
        for (int i = 0; i < norm.length; i++) {
            norm[i] = Catalano.Math.Tools.Scale(range.get(i), new DoubleRange(min, max), feature[i]);
        }
        
        return norm;
    }
    
    /**
     * Apply range normalization in the given instances.
     * @param matrix Matrix.
     * @return Normalized instances.
     */
    public double[][] ApplyRangeNormalization(double[][] matrix){
        
        if(range == null)
            throw new IllegalArgumentException("The matrix must be normalized.");
        
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                matrix[i][j] =  Catalano.Math.Tools.Scale(range.get(j), new DoubleRange(min, max), matrix[i][j]);
            }
        }
        
        return matrix;
    }
}