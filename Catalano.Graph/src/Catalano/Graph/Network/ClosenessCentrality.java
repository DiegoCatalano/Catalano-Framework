/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Catalano.Graph.Network;

import Catalano.Graph.AdjacencyMatrix;

/**
 *
 * @author Diego
 */
public class ClosenessCentrality {

    public ClosenessCentrality() {}
    
    public double[] Compute(AdjacencyMatrix matrix){
        
        double[] dc = new double[matrix.getData().length];
        
        double[][] data = matrix.getData();
        for (int i = 0; i < data.length; i++) {
            double v = 0;
            for (int j = 0; j < data[0].length; j++) {
                if(data[i][j] != 0 && data[i][j] != Double.POSITIVE_INFINITY){
                    v += data[i][j];
                }
            }
            dc[i] = v / ((double)dc.length - 1);
        }
        
        return dc;
    }
    
}
