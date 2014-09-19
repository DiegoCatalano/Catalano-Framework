/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Catalano.Math.Graph;

/**
 *
 * @author Diego
 */
public class AdjacencyMatrix {
    
    private boolean[][] m;
    
    public AdjacencyMatrix(int edges){
        this.m = new boolean[edges][edges];
    }

    public AdjacencyMatrix(boolean[][] m) {
        this.m = m;
    }
    
    public void addEdge(int i, int j){
        m[i][j] = true;
    }
    
    public void removeEdge(int i, int j){
        m[i][j] = false;
    }
    
    public boolean hasEdge(int i, int j){
        return m[i][j];
    }
    
    public int MaxDegree(){
        
        int h = m.length;
        int w = m[0].length;
        
        int max = 0;
        for (int i = 0; i < h; i++) {
            int degree = 0;
            for (int j = 0; j < w; j++) {
                if (m[i][j] == true) degree++;
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
                if (m[i][j] == true) degree++;
            }
            if (degree > max){
                max = degree;
                edge = i;
            }
        }
        
        return edge;
    }
    
}
