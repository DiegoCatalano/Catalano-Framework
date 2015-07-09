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

import Catalano.Math.Distances.EuclideanDistance;
import Catalano.Math.Distances.IDistance;
import Catalano.Math.Matrix;
import Catalano.Statistics.Kernels.IKernel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * K Nearest Neighbour classifier.
 * @author Diego Catalano
 * @param <T> Object.
 */
public class KNearestNeighbors<T> {
    
    private int k;
    private List<double[]> input;
    private T output[];
    private IDistance distance = new EuclideanDistance();
    private IKernel kernel;
    private boolean useKernel = false;

    /**
     * Get number of neighbours.
     * @return Number of neighbours.
     */
    public int getK() {
        return k;
    }

    /**
     * Set number of neighbours.
     * @param k Number of neighbours.
     */
    public void setK(int k) {
        this.k = Math.max(1, k);
    }

    /**
     * Get the input features.
     * @return List of features.
     */
    public List<double[]> getInput() {
        return input;
    }

    /**
     * Set the input features.
     * @param input Input features.
     */
    public void setInput(List<double[]> input) {
        this.input = input;
    }

    /**
     * Get the output.
     * @return Output.
     */
    public T[] getOutput() {
        return output;
    }

    /**
     * Set the output.
     * @param output Output.
     */
    public void setOutput(T[] output) {
        this.output = output;
    }

    /**
     * Get the kernel metric.
     * @return Kernel.
     */
    public IKernel getKernel() {
        return kernel;
    }

    /**
     * Set the kernel metric.
     * @param kernel Kernel.
     */
    public void setKernel(IKernel kernel) {
        this.kernel = kernel;
        this.useKernel = true;
    }

    /**
     * Get the distance metric.
     * @return Distance.
     */
    public IDistance getDistance() {
        return distance;
    }

    /**
     * Set the distance metric.
     * @param distance Distance.
     */
    public void setDistance(IDistance distance) {
        this.distance = distance;
        this.useKernel = false;
    }

    /**
     * Initializes a new instance of the KNearestNeighbors class.
     */
    public KNearestNeighbors(){
        this.k = 3;
    }
    
    /**
     * Initializes a new instance of the KNearestNeighbors class.
     * @param input List of fearures.
     * @param output Array of output.
     */
    public KNearestNeighbors(List<double[]> input, T[] output){
        this.k = 3;
        this.input = input;
        this.output = output;
    }
    
    /**
     * Initializes a new instance of the KNearestNeighbors class.
     * @param input List of fearures.
     * @param output Array of output.
     * @param k Number of neighbours.
     */
    public KNearestNeighbors(List<double[]> input, T[] output, int k){
        this.input = input;
        this.output = output;
        this.k = k;
    }
    
    /**
     * Initializes a new instance of the KNearestNeighbors class.
     * @param input List of fearures.
     * @param output Array of output.
     * @param k Number of neighbours.
     * @param distance Distance.
     */
    public KNearestNeighbors(List<double[]> input, T[] output, int k, IDistance distance){
        this.input = input;
        this.output = output;
        this.k = k;
        this.distance = distance;
    }
    
    /**
     * Initializes a new instance of the KNearestNeighbors class.
     * @param input List of fearures.
     * @param output Array of output.
     * @param k Number of neighbours.
     * @param kernel Kernel.
     */
    public KNearestNeighbors(List<double[]> input, T[] output, int k, IKernel kernel){
        this.input = input;
        this.output = output;
        this.k = k;
        this.kernel = kernel;
    }
    
    /**
     * Compute.
     * @param feature Feature to compute.
     * @return Object.
     */
    public T Compute(double[] feature){
        
        int sizeF = input.size();
        double[] dist = new double[sizeF];
        
        //Compute distance.
        if(useKernel){
            for (int i = 0; i < sizeF; i++)
                dist[i] = this.kernel.Function(feature, input.get(i));
        }else{
            for (int i = 0; i < sizeF; i++)
                dist[i] = this.distance.Compute(feature, input.get(i));
        }
        
        //If k is 1, we can retrive the object quickly.
        if(k == 1) return output[Matrix.MinIndex(dist)];
        
        //Sort indexes based on score
        int[] indexes = Matrix.Indices(0, dist.length);
        List<Score> lst = new ArrayList<Score>(dist.length);
        for (int i = 0; i < dist.length; i++) {
            lst.add(new Score(dist[i], indexes[i]));
        }
        
        Collections.sort(lst);
        
        //Compute vote majority
        HashMap<T, Integer> map = new HashMap<T, Integer>();
        int max = 0;
        for (int i = 0; i < k; i++) {
            int index = lst.get(i).index;
            if(!map.containsKey(output[index])){
                map.put(output[index], 1);
            }
            else{
                int x = map.get(output[index]) + 1;
                map.put(output[index], x);
                if(x > max) max = x;
            }
        }
        
        for(Map.Entry<T,Integer> entry : map.entrySet()) {
          if(entry.getValue() == max)
              return entry.getKey();

        }
        
        return null;
        
    }
    
    private class Score implements Comparable<Score> {
        double score;
        int index;

        public Score(double score, int index) {
            this.score = score;
            this.index = index;
        }

        @Override
        public int compareTo(Score o) {
            return score < o.score ? -1 : score > o.score ? 1 : 0;
        }
    }
    
}