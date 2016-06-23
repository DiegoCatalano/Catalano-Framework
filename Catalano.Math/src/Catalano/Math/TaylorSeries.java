// Catalano Math Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
// Copyright © César Souza, 2009-2013
// cesarsouza at gmail.com
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

package Catalano.Math;

/**
 * Compute functions using Taylor series expansion.
 * @author Diego Catalano
 */
public final class TaylorSeries {

    /**
     * Don't let anyone initialize this class.
     */
    private TaylorSeries() {}
    
    /**
     * Compute Sin using Taylor Series.
     * @param x An angle, in radians.
     * @param nTerms Number of terms.
     * @return Result.
     */
    public static double Sin(double x, int nTerms){
        if (nTerms < 2) return x;
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
        if (nTerms < 2) return 1;
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
        if (nTerms < 2) return x;
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
        if (nTerms < 2) return x;
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
        if (nTerms < 2) return 1 + x;
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