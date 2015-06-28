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

package Catalano.MachineLearning.Regression;

import Catalano.Math.Distances.EuclideanDistance;
import Catalano.Math.Distances.IDistance;
import Catalano.Math.Matrix;
import Catalano.Statistics.Kernels.IKernel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * K Nearest Neighbour for regression.
 * @author Diego Catalano
 */
public class KNearestNeighbors {
    
    private int k = 3;
    private List<double[]> input;
    private IDistance distance;
    private IKernel kernel;

    public KNearestNeighbors(List<double[]> input) {
        this.input = input;
        this.distance = new EuclideanDistance();
    }
    
    public KNearestNeighbors(List<double[]> input, int k) {
        this.input = input;
        this.k = k;
        this.distance = new EuclideanDistance();
    }
    
    public KNearestNeighbors(List<double[]> input, int k, IDistance distance) {
        this.input = input;
        this.k = k;
        this.distance = distance;
    }
    
    public KNearestNeighbors(List<double[]> input, int k, IKernel kernel) {
        this.input = input;
        this.k = k;
        this.kernel = kernel;
    }
    
    public double Compute(double[] feature){
        
        double[] dist = new double[input.size()];
        if(kernel == null)
            for (int i = 0; i < input.size(); i++){
                double[] temp = input.get(i);
                temp = Matrix.RemoveColumn(temp, temp.length - 1);
                dist[i] = distance.Compute(temp, feature);
            }
        else
            for (int i = 0; i < input.size(); i++){
                double[] temp = input.get(i);
                temp = Matrix.RemoveColumn(temp, temp.length - 1);
                dist[i] = kernel.Function(temp, feature);
            }
        
        //Sort indexes based on score
        int[] indexes = Matrix.Indices(0, dist.length);
        List<Score> lst = new ArrayList<Score>(dist.length);
        for (int i = 0; i < dist.length; i++) {
            lst.add(new Score(dist[i], indexes[i]));
        }
        
        Collections.sort(lst);
        
        double result = 0;
        int lastCol = input.get(0).length - 1;
        for (int i = 0; i < k; i++) {
            result += input.get(lst.get(i).index)[lastCol];
        }
        
        return result / (double)k;
        
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