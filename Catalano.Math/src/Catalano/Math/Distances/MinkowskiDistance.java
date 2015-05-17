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
public class MinkowskiDistance implements IDistance{
    
    private double p = 1;

    public double getP() {
        return p;
    }

    public void setP(double p) {
        if(p == 0)
            throw new IllegalArgumentException("P must be different from 0.");
        this.p = p;
    }

    public MinkowskiDistance() {}
    
    public MinkowskiDistance(double p){
        setP(p);
    }

    @Override
    public double Compute(double[] x, double[] y) {
        double distance = 0;
        for (int i = 0; i < x.length; i++) {
            distance += Math.pow(Math.abs(x[i] - y[i]),p);
        }
        return Math.pow(distance,1/p);
    }
}