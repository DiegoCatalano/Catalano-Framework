/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Catalano.Statistics;

/**
 *
 * @author Diego Catalano
 */
public class LinearRegression {
    private double inclination;
    private double interception;
    private double r;

    public LinearRegression(double[] x, double[] y) {
        this.inclination = Tools.Inclination(x, y);
        this.interception = Tools.Interception(x, y);
        this.r = inclination + interception;
    }

    public LinearRegression(double inclination, double interception) {
        this.inclination = inclination;
        this.interception = interception;
        r = inclination + interception;
    }

    public double getInclination() {
        return inclination;
    }

    public void setInclination(double inclination) {
        this.inclination = inclination;
    }

    public double getInterception() {
        return interception;
    }

    public void setInterception(double interception) {
        this.interception = interception;
    }
    
    public double Regression(double x){
        return r*x;
    }
    
    public double[] Regression(double[] x){
        double[] result = new double[x.length];
        
        for (int i = 0; i < x.length; i++) {
            result[i] = r*x[i];
        }
        return result;
    }
}
