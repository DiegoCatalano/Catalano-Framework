/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Catalano.Math;

/**
 *
 * @author Diego
 */
public class FastMath {
    
    /**
     * Calculate Pow.
     * The pow is calculate following the formula:
     * Exp(y * Log(x))
     * @param x The base.
     * @param y The exponent.
     * @return 
     */
    public static double Highprecision_Pow(double x, double y){
        return Math.exp(y * Math.log(x));
    }
    
    /**
     * Calculate Pow.
     * The pow is calculate following the formula:
     * Highprecision_Exp(y * Log(x))
     * @param x The base.
     * @param y The exponent.
     * @return 
     */
    public static double Lowprecision_Pow(double x, double y){
        return Highprecision_Exp(y * Math.log(x));
    }
    
    /**
     * Calculate Exp.
     * Exp by Taylor series (9).
     * @param x Value.
     * @return Exponential.
     */
    public static double Highprecision_Exp(double x) {
        return (362880+x*(362880+x*(181440+x*(60480+x*(15120+x*(3024+x*(504+x*(72+x*(9+x)))))))))*2.75573192e-6;
    }
    
    /**
     * Calculate Pow.
     * The exp is calculate following the formula:
     * 6*(x-1) / (x + 1 + 4*(Sqrt(x)));
     * @param x Value.
     * @return 
     */
    public static double Lowprecision_Log(double x){
        return 6*(x-1) / (x + 1 + 4*(Math.sqrt(x)));
    }
    
}
