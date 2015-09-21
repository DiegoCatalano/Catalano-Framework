/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Catalano.Statistics.Regression;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Diego Catalano
 */
public class RegressionAnalysis {
    
    private int maxDegree = 2;
    private int usedDegree = 0;
    private boolean usePolynomial = true;

    public int getMaxDegree() {
        return maxDegree;
    }

    public void setMaxDegree(int maxDegree) {
        this.maxDegree = Math.max(maxDegree, 2);
    }

    public int getUsedDegree() {
        return usedDegree;
    }

    public boolean isUsePolynomial() {
        return usePolynomial;
    }

    public void setUsePolynomial(boolean usePolynomial) {
        this.usePolynomial = usePolynomial;
    }
    
    /**
     * Initializes a new instance of the RegressionAnalysis class.
     * Uses polynomial regression with max degree equal 2.
     */
    public RegressionAnalysis(){
        this(2);
    }
    
    /**
     * Initializes a new instance of the RegressionAnalysis class.
     * @param maxDegree Maximu
     */
    public RegressionAnalysis(int maxDegree) {
        this.usePolynomial = true;
        setMaxDegree(maxDegree);
    }
    
    /**
     * Initializes a new instance of the RegressionAnalysis class.
     * @param usePolynomial True if the analysis uses polynomial regression.
     */
    public RegressionAnalysis(boolean usePolynomial){
        this.usePolynomial = usePolynomial;
        setMaxDegree(2);
    }
    
    /**
     * Compute the best regression to give data.
     * @param x X Data.
     * @param y Y Data.
     * @return Best regression.
     */
    public ISimpleRegression BestFit(double[] x, double[] y){
        
        //All regressions, except polynomial
        List<ISimpleRegression> lst = new ArrayList<ISimpleRegression>();
        lst.add(new LinearRegression(x, y));
        lst.add(new LogarithmicRegression(x, y));
        lst.add(new ExponentialRegression(x, y));
        lst.add(new PowerRegression(x, y));
        
        ISimpleRegression bestRegression = null;
        double bestFit = 0;
        
        for (ISimpleRegression r : lst) {
            double f = r.CoefficientOfDetermination();
            if(f == 1) return r;
            if(f > bestFit){
                bestFit = f;
                bestRegression = r;
            }
        }
        
        //Special case: Polynomial regression
        
        if(usePolynomial){
            
            //Compute polynomial for degree 2
            ISimpleRegression r = new PolynomialRegression(x, y, 2);
            double f = r.CoefficientOfDetermination();
            if(f == 1) return r;
            if(f > bestFit){
                bestFit = f;
                bestRegression = r;
                usedDegree = 2;
            }

            //Compute polynomial for degree 3+
            for (int i = 3; i <= maxDegree; i++) {
                r = new PolynomialRegression(x, y, i);
                f = r.CoefficientOfDetermination();
                if(f == 1) return r;
                if(f > bestFit){
                    bestFit = f;
                    bestRegression = r;
                    this.usedDegree = i;
                }
            }
        }
        
        return bestRegression;
    }
    
    /**
     * Compute the best regression.
     * @param regressions Regressions.
     * @return Best regression.
     */
    public ISimpleRegression BestFit(List<ISimpleRegression> regressions){
        
        ISimpleRegression reg = null;
        double best = 0;
        for (ISimpleRegression r : regressions) {
            double c = r.CoefficientOfDetermination();
            if(c > best){
                best = c;
                reg = r;
            }
        }
        return reg;
        
    }
}