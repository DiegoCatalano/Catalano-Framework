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

/**
 * PageRank algorithm.
 * PageRank is a way of measuring the importance of website pages.
 * @author Diego Catalano
 */
public class PageRank {
    
    private double epsilon = 0.001;
    private double factor = 0.85;
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
     * Get damping factor.
     * @return Damping factor.
     */
    public double getFactor() {
        return factor;
    }

    /**
     * Set damping factor.
     * @param factor Damping factor.
     */
    public void setFactor(double factor) {
        this.factor = Math.min(1, Math.max(0, factor));
    }

    /**
     * Get number of iterations executed by the algorithm.
     * @return Number of iterations.
     */
    public int getNumberOfIterations() {
        return it;
    }

    /**
     * Initialize a new instance of the PageRank class.
     */
    public PageRank() {}
    
    /**
     * Initialize a new instance of the PageRank class.
     * @param factor Damping factor.
     */
    public PageRank(double factor){
        setFactor(factor);
    }
    
    /**
     * Compute PageRank.
     * @param matrix Adjacency matrix.
     * @return Ranks.
     */
    public double[] Compute(AdjacencyMatrix matrix){
        
        //Basic weights. 1/n
        double[] w = new double[matrix.getData().length];
        double t = 1 / w.length;
        for (int i = 0; i < w.length; i++) {
            w[i] = t;
        }
        
        return Compute(matrix,w);
    }
    
    /**
     * Compute PageRank.
     * @param matrix Adjacency matrix.
     * @param weights Inital weights,
     * @return Ranks.
     */
    public double[] Compute(AdjacencyMatrix matrix, double[] weights){
        
        if(matrix.getData().length != weights.length)
            throw new IllegalArgumentException("The matrix lenght must be the same lenght of the weights.");
        
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
        
        //Inital wwights to all nodes
        ranks = weights;
        
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
    
    /**
     * Test for convergency.
     * @param oldRanks Old ranks value.
     * @param ranks New ranks value.
     * @return Convergeny.
     */
    private double Convergency(double[] oldRanks, double[] ranks){
        
        double maxDiff = Double.MAX_VALUE;
        for (int i = 0; i < ranks.length; i++) {
            maxDiff = Math.min(maxDiff, oldRanks[i] - ranks[i]);
        }
        return maxDiff;
        
    }
}