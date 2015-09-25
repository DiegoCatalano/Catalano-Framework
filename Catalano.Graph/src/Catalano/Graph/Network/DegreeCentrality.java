/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Catalano.Graph.Network;

import Catalano.Graph.AdjacencyMatrix;

/**
 * 
 * @author Diego Catalano
 */
public class DegreeCentrality {
    
    private boolean normalize = false;

    public DegreeCentrality() {}
    
    public DegreeCentrality(boolean normalize){
        this.normalize = normalize;
    }
    
    public double[] Compute(AdjacencyMatrix matrix){
        
        double[] dc = new double[matrix.getData().length];
        double edges = dc.length - 1;
        
        double[][] data = matrix.getData();
        for (int i = 0; i < data.length; i++) {
            int degree = 0;
            for (int j = 0; j < data[0].length; j++) {
                if(data[i][j] != 0) degree++;
            }
            if(degree != 0) dc[i] = degree;
        }
        
        if(normalize)
            for (int i = 0; i < dc.length; i++)
                dc[i] /= edges;
        
        return dc;
    }
}