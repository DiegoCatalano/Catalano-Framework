/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Catalano.Math.Graph;

/**
 * https://cs7083.wordpress.com/2013/01/31/demystifying-the-pagerank-and-hits-algorithms/
 * @author Diego
 */
public class AdjacencyMatrix {
    
    private double[][] m;
    
    public AdjacencyMatrix(int edges){
        this.m = new double[edges][edges];
    }

    public AdjacencyMatrix(double[][] m) {
        this.m = m;
    }
    
    public double[][] getData(){
        return m;
    }
    
    public int MaxDegree(){
        
        int h = m.length;
        int w = m[0].length;
        
        int max = 0;
        for (int i = 0; i < h; i++) {
            int degree = 0;
            for (int j = 0; j < w; j++) {
                if (m[i][j] > 0) degree++;
            }
            if (degree > max){
                max = degree;
            }
        }
        
        return max;
        
    }
    
    public int getEdgeByMaxDegree(){
        int h = m.length;
        int w = m[0].length;
        
        int edge = 0;
        int max = 0;
        for (int i = 0; i < h; i++) {
            int degree = 0;
            for (int j = 0; j < w; j++) {
                if (m[i][j] > 0) degree++;
            }
            if (degree > max){
                max = degree;
                edge = i;
            }
        }
        
        return edge;
    }
}