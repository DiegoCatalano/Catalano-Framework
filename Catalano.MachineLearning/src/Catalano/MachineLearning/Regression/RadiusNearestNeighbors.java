// Catalano Machine Learning Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2018
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

import Catalano.Core.ArraysUtil;
import Catalano.MachineLearning.Dataset.DatasetRegression;
import Catalano.Math.Distances.IDivergence;
import Catalano.Math.Distances.SquaredEuclideanDistance;
import Catalano.Math.Tools;
import Catalano.Statistics.Kernels.IMercerKernel;
import java.io.Serializable;

/**
 * Radius Nearest Neighbors for regression.
 * @author Diego Catalano
 */
public class RadiusNearestNeighbors implements IRegression, Serializable{
    
    private double radius;
    private double[][] input;
    private double[] output;
    private IDivergence divergence;
    private IMercerKernel kernel;

    /**
     * Get radius.
     * @return Radius.
     */
    public double getRadius() {
        return radius;
    }

    /**
     * Set radius.
     * @param radius Radius.
     */
    public void setRadius(double radius) {
        this.radius = radius;
    }

    /**
     * Initializes a new instance of the RadiusNearestNeighbors class.
     */
    public RadiusNearestNeighbors() {
        this(0.2);
    }
    
    /**
     * Initializes a new instance of the RadiusNearestNeighbors class.
     * @param radius Radius.
     */
    public RadiusNearestNeighbors(double radius) {
        this(0.2, new SquaredEuclideanDistance());
    }
    
    /**
     * Initializes a new instance of the RadiusNearestNeighbors class.
     * @param radius Radius.
     * @param divergence Divergence.
     */
    public RadiusNearestNeighbors(double radius, IDivergence divergence) {
        this.radius = radius;
        this.divergence = divergence;
    }
    
    /**
     * Initializes a new instance of the RadiusNearestNeighbors class.
     * @param radius Radius.
     * @param kernel Kernel.
     */
    public RadiusNearestNeighbors(double radius, IMercerKernel kernel) {
        this.radius = radius;
        this.kernel = kernel;
    }
    
    @Override
    public void Learn(DatasetRegression dataset) {
        Learn(dataset.getInput(), dataset.getOutput());
    }

    @Override
    public void Learn(double[][] input, double[] output) {
        this.input = input;
        this.output = output;
    }
    
    @Override
    public double Predict(double[] feature){
        double[] dist = new double[input.length];
        double max = -Double.MAX_VALUE;
        double min = Double.MAX_VALUE;
        if(kernel == null)
            for (int i = 0; i < input.length; i++){
                double[] temp = input[i];
                //temp = Matrix.RemoveColumn(temp, temp.length - 1);
                dist[i] = divergence.Compute(temp, feature);
                max = Math.max(max, dist[i]);
                min = Math.min(min, dist[i]);
            }
        else
            for (int i = 0; i < input.length; i++){
                double[] temp = input[i];
                //temp = Matrix.RemoveColumn(temp, temp.length - 1);
                dist[i] = kernel.Function(temp, feature);
                max = Math.max(max, dist[i]);
                min = Math.min(min, dist[i]);
            }
        
        //Normalize the data ?
        for (int i = 0; i < dist.length; i++) {
            dist[i] = Tools.Scale(min, max, 0, 1, dist[i]);
        }
        
        //Sort indexes based on score
        int[] indexes = ArraysUtil.Argsort(dist, true);
        int k = Min(dist, indexes);
        
        double result = 0;
        for (int i = 0; i < k; i++) {
            result += output[indexes[i]];
        }
        
        return result / (double)k;
    }
    
    private int Min(double[] dist, int[] indexes){
        
        int e = 0;
        for (int i = 0; i < indexes.length; i++) {
            if(dist[indexes[i]] > radius) return e;
            e++;
        }
        return e;
        
    }
    
    @Override
    public IRegression clone() {
        try {
            return (IRegression)super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new IllegalArgumentException("Clone not supported: " + ex.getMessage());
        }
    }
}