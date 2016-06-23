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

package Catalano.Graph;

import Catalano.Math.Matrix;

/**
 * Adjacency Matrix.
 * @author Diego Catalano
 */
public class AdjacencyMatrix {
    
    private double[][] matrix;
    
    /**
     * Initializes a new instance of the AdjacencyMatrix class.
     * @param edges Number of the edges.
     */
    public AdjacencyMatrix(int edges){
        this.matrix = new double[edges][edges];
    }

    /**
     * Initializes a new instance of the AdjacencyMatrix class.
     * @param matrix Matrix.
     */
    public AdjacencyMatrix(double[][] matrix) {
        this.matrix = matrix;
    }
    
    /**
     * Get data from the adjacency matrix.
     * @return Data.
     */
    public double[][] getData(){
        return matrix;
    }
    
    /**
     * Get number of nodes.
     * @return Number of nodes.
     */
    public int getNumberOfNodes(){
        return matrix.length;
    }
    
    /**
     * Computes the average degree of a node in a graph.
     * @return Average degree.
     */
    public double getAverageDegree(){
        return 2 * getNumberOfEdges() / getNumberOfNodes();
    }
    
    /**
     * Computes the link density of a graph.
     * @return Link density.
     */
    public double getLinkDensity(){
        int n = getNumberOfNodes();
        int e = getNumberOfEdges();
        return 2*e/(n * (n - 1));
    }
    
    /**
     * Get number of edges.
     * @return Number of edges.
     */
    public int getNumberOfEdges(){
        
        int sl = getNumberOfSelfLoops();
        boolean sym = isSymmetric();
        
        int edges = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if(matrix[i][j] != 0) edges++;
            }
        }
        
        //Undirected Simple Graph
        if(sym && sl == 0){
            return edges / 2;
        }
        else if (sym && sl > 0){
            return (edges - sl) / 2 + sl;
        }
        
        return edges;
        
    }
    
    /**
     * Get Number of self loops in the graph.
     * @return Number of self loops.
     */
    public int getNumberOfSelfLoops(){
        int loops = 0;
        for (int i = 0; i < matrix.length; i++) {
            loops += matrix[i][i];
        }
        return loops;
    }
    
    /**
     * Set data in the adjacency matrix.
     * @param data Data.
     */
    public void setData(double[][] data){
        if(data.length != data[0].length)
            throw new IllegalArgumentException("Number of rows must be the same of number of cols.");
        
        this.matrix = data;
    }
    
    /**
     * Get the maximum degree.
     * @return Maximum degree.
     */
    public int getMaxDegree(){
        
        int h = matrix.length;
        int w = matrix[0].length;
        
        int max = 0;
        for (int i = 0; i < h; i++) {
            int degree = 0;
            for (int j = 0; j < w; j++) {
                if (matrix[i][j] > 0) degree++;
            }
            if (degree > max){
                max = degree;
            }
        }
        
        return max;
        
    }
    
    /**
     * Get edge by maximum degree.
     * @return Edge.
     */
    public int getEdgeByMaxDegree(){
        int h = matrix.length;
        int w = matrix[0].length;
        
        int edge = 0;
        int max = 0;
        for (int i = 0; i < h; i++) {
            int degree = 0;
            for (int j = 0; j < w; j++) {
                if (matrix[i][j] > 0) degree++;
            }
            if (degree > max){
                max = degree;
                edge = i;
            }
        }
        
        return edge;
    }
    
    /**
     * Create stochastic matrix.
     * @param matrix Matrix.
     * @return Stochastic matrix.
     */
    public static double[][] CreateStochasticMatrix(double[][] matrix){
        
        double[][] result = new double[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            int n = 0;
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] != 0) n++;
            }
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] != 0) result[i][j] = 1/(double)n;
            }
        }
        return result;
        
    }
    
    /**
     * Create stochastic matrix.
     */
    public void setStochasticMatrix(){
        
        for (int i = 0; i < matrix.length; i++) {
            int n = 0;
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] != 0) n++;
            }
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] != 0) matrix[i][j] = 1/(double)n;
            }
        }
        
    }
    
    /**
     * Check if the adjacency matrix is symmetric.
     * @return True if is symmetric, otherwise false.
     */
    public boolean isSymmetric(){
        return Matrix.isSymmetric(matrix);
    }
}