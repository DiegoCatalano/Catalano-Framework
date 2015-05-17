/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Catalano.Math.Distances;

/**
 *
 * @author Diego
 */
public class CosineDistance implements IDistance{

    public CosineDistance() {}

    @Override
    public double Compute(double[] x, double[] y) {
        double sumProduct = 0;
        double sumP = 0, sumQ = 0;
        
        for (int i = 0; i < x.length; i++) {
            sumProduct += x[i] * y[i];
            sumP += Math.pow(Math.abs(x[i]),2);
            sumQ += Math.pow(Math.abs(y[i]),2);
        }
        
        sumP = Math.sqrt(sumP);
        sumQ = Math.sqrt(sumQ);
        
        double result = 1 - (sumProduct/(sumP*sumQ));
        
        return result;
    }
}