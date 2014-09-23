/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Catalano.Math;

/**
 * Reference: http://www.haverford.edu/physics/MathAppendices/Taylor_Series.pdf
 * @author Diego Catalano
 */
public final class TaylorSeries {

    private TaylorSeries() {}
    
    /**
     * Compute Sin using Taylor Series.
     * @param x An angle, in radians.
     * @param nTerms Number of terms.
     * @return Result.
     */
    public static double Sin(double x, int nTerms){
        if (nTerms == 1) return x;
        if (nTerms == 2){
            return x - (x*x*x) / 6D;
        }
        else{
            
            double mult = x*x*x;
            double fact = 6;
            double sign = 1;
            int factS = 5;
            double result = x - mult/fact;
            for (int i = 3; i <= nTerms; i++) {
                mult *= x*x;
                fact *= factS * (factS - 1);
                factS += 2;
                result += sign * (mult/fact);
                sign *= -1;
            }
            
            return result;
        }
    }
    
    /**
     * Compute Cos using Taylor Series.
     * @param x An angle, in radians.
     * @param nTerms Number of terms.
     * @return Result.
     */
    public static double Cos(double x, int nTerms){
        if (nTerms == 1) return 1;
        if (nTerms == 2){
            return 1 - (x*x) / 2D;
        }
        else{
            
            double mult = x*x;
            double fact = 2;
            double sign = 1;
            int factS = 4;
            double result = 1 - mult/fact;
            for (int i = 3; i <= nTerms; i++) {
                mult *= x*x;
                fact *= factS * (factS - 1);
                factS += 2;
                result += sign * (mult/fact);
                sign *= -1;
            }
            
            return result;
        }
    }
    
    /**
     * Compute Sinh using Taylor Series.
     * @param x An angle, in radians.
     * @param nTerms Number of terms.
     * @return Result.
     */
    public static double Sinh(double x, int nTerms){
        if (nTerms == 1) return x;
        if (nTerms == 2){
            return x + (x*x*x) / 6D;
        }
        else{
            
            double mult = x*x*x;
            double fact = 6;
            int factS = 5;
            double result = x + mult/fact;
            for (int i = 3; i <= nTerms; i++) {
                mult *= x*x;
                fact *= factS * (factS - 1);
                factS += 2;
                result += mult/fact;
            }
            
            return result;
        }
    }
    
    /**
     * Compute Cosh using Taylor Series.
     * @param x An angle, in radians.
     * @param nTerms Number of terms.
     * @return Result.
     */
    public static double Cosh(double x, int nTerms){
        if (nTerms == 1) return x;
        if (nTerms == 2){
            return 1 + (x*x) / 2D;
        }
        else{
            
            double mult = x*x;
            double fact = 2;
            int factS = 4;
            double result = 1 + mult/fact;
            for (int i = 3; i <= nTerms; i++) {
                mult *= x*x;
                fact *= factS * (factS - 1);
                factS += 2;
                result += mult/fact;
            }
            
            return result;
        }
    }
    
   /**
     * Compute Exp using Taylor Series.
     * @param x An angle, in radians.
     * @param nTerms Number of terms.
     * @return Result.
     */
    public static double Exp(double x, int nTerms){
        if (nTerms == 1) return 1 + x;
        if (nTerms == 2){
            return 1 + x + (x*x) / 2;
        }
        else{
            
            double mult = x*x;
            double fact = 2;
            double result = 1 + x + mult/fact;
            for (int i = 3; i <= nTerms; i++) {
                mult *= x;
                fact *= i;
                result += mult/fact;
            }
            
            return result;
        }
    }
    
}
