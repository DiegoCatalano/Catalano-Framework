// Catalano Graph Library
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

package Catalano.Graph.Network;

import Catalano.Graph.AdjacencyMatrix;
import Catalano.Math.Matrix;

/**
 * Hyperlink-Induced Topic Search.
 * Hits is a link analysis algorithm that rates Web pages.
 * 
 * @author Diego Catalano
 */
public class Hits {
    
    private double epsilon = 0.001;
    private double[] authority;
    private double[] hubs;
    private int it = 0;

    /**
     * Get epsilon value.
     * @return Epsilon value.
     */
    public double getEpsilon() {
        return epsilon;
    }

    /**
     * Set epsilon value.
     * Epsilon value, is the stop criteria.
     * @param epsilon Epsilon value.
     */
    public void setEpsilon(double epsilon) {
        if(epsilon == 0)
            throw new IllegalArgumentException("The epsilon value must be different of zero.");
        this.epsilon = epsilon;
    }

    /**
     * Get number of iterations executed by the algorithm.
     * @return Number of iterations.
     */
    public int getNumberOfIterations() {
        return it;
    }

    /**
     * Get Authority.
     * @return Authority.
     */
    public double[] getAuthority() {
        return authority;
    }

    /**
     * Get Hubs.
     * @return Hubs.
     */
    public double[] getHubs() {
        return hubs;
    }

    /**
     * Initialize a new instance of the Hits class.
     * @param matrix Adjacency matrix.
     */
    public Hits(AdjacencyMatrix matrix) {
        Compute(matrix);
    }
    
    /**
     * Initialize a new instance of the Hits class.
     * @param matrix Adjacency matrix.
     * @param epsilon Epsilon value.
     */
    public Hits(AdjacencyMatrix matrix, double epsilon){
        setEpsilon(epsilon);
        Compute(matrix);
    }
    
    /**
     * Compute Hits.
     * @param matrix Adjacency matrix.
     */
    public void Compute(AdjacencyMatrix matrix){
        //Basic weights.
        double[] weights = Matrix.CreateMatrix1D(matrix.getData().length, 1D);
        Compute(matrix, weights);
    }
    
    /**
     * Compute Hits.
     * @param matrix Adjacency matrix.
     * @param weights Normalized weights.[0..1]
     */
    public void Compute(AdjacencyMatrix matrix, double[] weights){
        
        if(matrix.getData().length != weights.length)
            throw new IllegalArgumentException("The matrix lenght must be the same lenght of the weights.");
        
        //Initialize the matrix authority. mA = A' * A
        double[][] mA = Matrix.Multiply(Matrix.Transpose(matrix.getData()), matrix.getData());
        
        //Initialize the matrix hubs. mH = A * A'
        double[][] mH = Matrix.MultiplyByTranspose(matrix.getData(), matrix.getData());
        
        //Create Authority and Hubs weights.
        authority = weights;
        hubs = weights;
        
        double maxDiff = Double.MAX_VALUE;
        while(maxDiff > epsilon){
        
            //Compute authority: a = A' * A * a
            double[] a = Iteration(mA, authority);

            //Compute hubs = h = A * A' * h
            double[] h = Iteration(mH, hubs);
            
            maxDiff = Convergency(authority, hubs, a, h);
            
            authority = a;
            hubs = h;
            it++;
        }
        
    }
    
    /**
     * Equivalent like Multiply by Transpose, but with normalizations.
     * @param m Matrix.
     * @param a Vector to be transposed.
     * @return Result.
     */
    private double[] Iteration(double[][] m, double[] a){
        
        double[] result = new double[a.length];
        
        double sum = 0;
        for (int i = 0; i < m.length; i++) {
            double r = 0;
            for (int j = 0; j < m[0].length; j++) {
                r += m[i][j] * a[j];
            }
            result[i] = r;
            sum += r;
        }
        
        //Normalize
        for (int i = 0; i < result.length; i++) {
            result[i] /= sum;
        }
        
        return result;
        
    }
    
    private double Convergency(double[] oldAuthority, double[] oldHubs, double[] authority, double[] hubs){
        
        double maxDiff = Double.MAX_VALUE;
        for (int i = 0; i < oldAuthority.length; i++) {
            maxDiff = Math.min(maxDiff, Math.abs(oldAuthority[i] - authority[i]));
            maxDiff = Math.min(maxDiff, Math.abs(oldHubs[i] - hubs[i]));
        }
        
        return maxDiff;
        
    }
}