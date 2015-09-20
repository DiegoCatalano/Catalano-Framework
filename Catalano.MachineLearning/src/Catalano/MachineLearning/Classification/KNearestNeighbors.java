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

package Catalano.MachineLearning.Classification;

import Catalano.Math.Distances.EuclideanDistance;
import Catalano.Math.Distances.IDistance;
import Catalano.Math.Distances.SquaredEuclideanDistance;
import Catalano.Math.Matrix;
import Catalano.Statistics.Kernels.IMercerKernel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * K Nearest Neighbour classifier.
 * @author Diego Catalano
 */
public class KNearestNeighbors implements IClassifier, Serializable {
    
    private int k;
    private double[][] input;
    private int[] output;
    private IDistance distance = new EuclideanDistance();
    private IMercerKernel kernel;
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
        this(3);
    }
    
    /**
     * Initializes a new instance of the KNearestNeighbors class.
     * @param k Number of neighbours.
     */
    public KNearestNeighbors(int k){
        this(3, new SquaredEuclideanDistance());
    }
    
    /**
     * Initializes a new instance of the KNearestNeighbors class.
     * @param k Number of neighbours.
     * @param distance Distance.
     */
    public KNearestNeighbors(int k, IDistance distance){
        this.k = k;
        this.distance = distance;
    }
    
    /**
     * Initializes a new instance of the KNearestNeighbors class.
     * @param k Number of neighbours.
     * @param kernel Kernel.
     */
    public KNearestNeighbors(int k, IMercerKernel kernel){
        this.k = k;
        this.kernel = kernel;
        this.useKernel = true;
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
        
        //Compute distance.
        if(useKernel){
            for (int i = 0; i < sizeF; i++)
                dist[i] = this.kernel.Function(feature, input[i]);
        }else{
            for (int i = 0; i < sizeF; i++)
                dist[i] = this.distance.Compute(feature, input[i]);
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
        int min = output[lst.get(0).index];
        
        //Compute vote majority
        HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
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
        
        for(Map.Entry<Integer,Integer> entry : map.entrySet()) {
          if(entry.getValue() == max)
              return entry.getKey();

        }
        
        return min;
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