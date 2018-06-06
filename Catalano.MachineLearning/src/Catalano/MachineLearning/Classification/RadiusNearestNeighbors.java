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

package Catalano.MachineLearning.Classification;

import Catalano.Core.ArraysUtil;
import Catalano.MachineLearning.Dataset.DatasetClassification;
import Catalano.Math.Distances.IDivergence;
import Catalano.Math.Distances.SquaredEuclideanDistance;
import Catalano.Math.Matrix;
import Catalano.Math.Tools;
import Catalano.Statistics.Kernels.IMercerKernel;
import java.io.Serializable;

/**
 * Radius Neighbour classifier.
 * @author Diego Catalano
 */
public class RadiusNearestNeighbors implements IClassifier, Serializable {
    
    private double radius;
    private double[][] input;
    private int[] output;
    private IDivergence divergence = new SquaredEuclideanDistance();
    private IMercerKernel kernel;
    private boolean useKernel = false;

    /**
     * Get Radius.
     * @return Radius.
     */
    public double getRadius() {
        return radius;
    }

    /**
     * Set Radius.
     * @param radius Radius.
     */
    public void setRadius(double radius) {
        this.radius = radius;
    }

    /**
     * Get the kernel metric.
     * @return Kernel.
     */
    public IMercerKernel getKernel() {
        return kernel;
    }

    /**
     * Set the kernel metric.
     * @param kernel Kernel.
     */
    public void setMercerKernel(IMercerKernel kernel) {
        this.kernel = kernel;
        this.useKernel = true;
    }

    /**
     * Get the divergence function.
     * @return Divergence.
     */
    public IDivergence getDistance() {
        return divergence;
    }

    /**
     * Set the divergence function.
     * @param divergence Divergence.
     */
    public void setDistance(IDivergence divergence) {
        this.divergence = divergence;
        this.useKernel = false;
    }
    
    /**
     * Initializes a new instance of the RadiusNearestNeighbors class.
     */
    public RadiusNearestNeighbors(){
        this(0.05);
    }
    
    /**
     * Initializes a new instance of the RadiusNearestNeighbors class.
     * @param radius Radius.
     */
    public RadiusNearestNeighbors(double radius){
        this(radius, new SquaredEuclideanDistance());
    }
    
    /**
     * Initializes a new instance of the RadiusNearestNeighbors class.
     * @param radius Radius.
     * @param divergence Distance.
     */
    public RadiusNearestNeighbors(double radius, IDivergence divergence){
        this.radius = radius;
        this.divergence = divergence;
    }
    
    /**
     * Initializes a new instance of the RadiusNearestNeighbors class.
     * @param radius Radius.
     * @param kernel Kernel.
     */
    public RadiusNearestNeighbors(double radius, IMercerKernel kernel){
        this.radius = radius;
        this.kernel = kernel;
        this.useKernel = true;
    }

    @Override
    public void Learn(DatasetClassification dataset) {
        Learn(dataset.getInput(), dataset.getOutput());
    }
    
    @Override
    public void Learn(double[][] input, int[] output){
        this.input = input;
        this.output = output;
    }
    
    /**
     * Compute.
     * @param feature Feature to compute.
     * @return Object.
     */
    @Override
    public int Predict(double[] feature){
        
        int sizeF = input.length;
        double[] dist = new double[sizeF];
        double max = -Double.MAX_VALUE;
        double min = Double.MAX_VALUE;
        
        //Compute distance.
        if(useKernel){
            for (int i = 0; i < sizeF; i++){
                dist[i] = this.kernel.Function(feature, input[i]);
                max = Math.max(max, dist[i]);
                min = Math.min(min, dist[i]);
            }
        }else{
            for (int i = 0; i < sizeF; i++){
                dist[i] = this.divergence.Compute(feature, input[i]);
                max = Math.max(max, dist[i]);
                min = Math.min(min, dist[i]);
            }
        }
        
        //Normalize the distances ?
        for (int i = 0; i < dist.length; i++) {
            dist[i] = Tools.Scale(min, max, 0, 1, dist[i]);
        }
        
        //Sort indexes based on score
        int[] indexes = ArraysUtil.Argsort(dist, true);
        int k = Min(dist, indexes);
        
        //Compute vote majority
        int classes = Matrix.Max(output) + 1;
        int[] votes = new int[classes];
        for (int i = 0; i < k; i++) {
            votes[output[indexes[i]]]++;
        }
        
        return Matrix.MaxIndex(votes);

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
    public IClassifier clone() {
        try {
            return (IClassifier)super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new IllegalArgumentException("Clone not supported: " + ex.getMessage());
        }
    }
}