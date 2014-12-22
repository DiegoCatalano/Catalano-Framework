/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Catalano.Math.Graph;

import Catalano.Math.Matrix;

/**
 *
 * @author Diego
 */
public class Hits {
    
    private double epsilon = 0.001;
    private double[] authority;
    private double[] hubs;
    private int it = 0;

    public double getEpsilon() {
        return epsilon;
    }

    public void setEpsilon(double epsilon) {
        this.epsilon = epsilon;
    }

    public int getNumberOfIterations() {
        return it;
    }

    public double[] getAuthority() {
        return authority;
    }

    public double[] getHubs() {
        return hubs;
    }

    public Hits(AdjacencyMatrix matrix) {
        Compute(matrix);
    }
    
    public Hits(AdjacencyMatrix matrix, double epsilon){
        setEpsilon(epsilon);
        Compute(matrix);
    }
    
    public void Compute(AdjacencyMatrix matrix){
        
        //Initialize the matrix authority.
        double[][] mA = Matrix.Multiply(Matrix.Transpose(matrix.getData()), matrix.getData());
        
        //Initialize the matrix hubs.
        double[][] mH = Matrix.Multiply(matrix.getData(), Matrix.Transpose(matrix.getData()));
        
        //Create Authority and Hubs weights.
        authority = Matrix.CreateMatrix1D(mA.length, 1D);
        hubs = Matrix.CreateMatrix1D(mH.length, 1D);
        
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