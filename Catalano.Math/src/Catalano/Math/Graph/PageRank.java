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
public class PageRank {
    
    private double epsilon = 0.001;
    private double factor = 0.85;
    private int it = 0;

    public double getEpsilon() {
        return epsilon;
    }

    public void setEpsilon(double epsilon) {
        this.epsilon = epsilon;
    }

    public double getFactor() {
        return factor;
    }

    public void setFactor(double factor) {
        this.factor = factor;
    }

    public int getNumberOfIterations() {
        return it;
    }

    public PageRank() {}
    
    public PageRank(double factor){
        setFactor(factor);
    }
    
    public double[] Compute(AdjacencyMatrix matrix){
        
        double[][] m = matrix.getData();
        double[] ranks = new double[m.length];
        double[] outs = new double[m.length];
        
        //Calculate the number of outs for each node
        for (int i = 0; i < m.length; i++) {
            double sum = 0;
            for (int j = 0; j < m[0].length; j++) {
                if(m[i][j] != 0)
                    sum++;
            }
            outs[i] = sum;
        }
        
        //Inital value to all nodes
        double t = 1 / (double)m.length;
        for (int i = 0; i < ranks.length; i++) {
            ranks[i] = t;
        }
        
        //Common calculation for all nodes
        double part1 = (1 - factor) / (double)m.length;
        
        double maxDiff = Double.MAX_VALUE;
        double[] oldRanks = ranks.clone();
        while(maxDiff > epsilon){
            //Calculate page rank
            for (int j = 0; j < m[0].length; j++) {
                double part2 = 0;
                for (int i = 0; i < m.length; i++) {
                    if(m[i][j] != 0){
                        part2 += ranks[i] / outs[i];
                    }
                }
                ranks[j] = part1 + factor * part2;
            }
            
            maxDiff = Convergency(oldRanks, ranks);
            it++;
        }
        return ranks;
    }
    
    private double Convergency(double[] oldRanks, double[] ranks){
        
        double maxDiff = Double.MAX_VALUE;
        for (int i = 0; i < ranks.length; i++) {
            maxDiff = Math.min(maxDiff, oldRanks[i] - ranks[i]);
        }
        return maxDiff;
        
    }
    
    
    
}
