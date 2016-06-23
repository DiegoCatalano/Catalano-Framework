// Catalano Math Library
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

package Catalano.Math;

/**
 * Calculate approximations for common math operations.
 * @author Diego Catalano
 */
public final class Approximation {

    /**
     * Don't let anyone instantiate this class.
     */
    private Approximation() {}
    
    /**
     * Calculate absolute number.
     * @param x Number.
     * @return Abs(x).
     */
    public static int abs(int x){
        final int i = x >>> 31;
        return (x ^ (~i + 1)) + i;
    }
    
    /**
     * Calculate absolute number.
     * @param x Number.
     * @return Abs(x).
     */
    public static long abs(long x){
        final long l = x >>> 63;
        return (x ^ (~l + 1)) + l;
    }
    
    /**
     * Calculate Pow.
     * The pow is calculate following the formula:
     * Formula: Exp(y * Log(x))
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
     * Formula: Highprecision_Exp(y * Log(x))
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
    
    /**
     * Calculate Sin using Quadratic curve.
     * @param x An angle, in radians.
     * @return Result.
     */
    public static double Lowprecision_Sin(double x){
        
        //always wrap input angle to -PI..PI
        if (x < -3.14159265)
            x += 6.28318531;
        else if (x >  3.14159265)
            x -= 6.28318531;

        //compute sine
        if (x < 0)
            return 1.27323954 * x + .405284735 * x * x;
        else
            return 1.27323954 * x - 0.405284735 * x * x;
    }
    
    /**
     * Calculate Sin using Quadratic curve.
     * @param x An angle, in radians.
     * @return Result.
     */
    public static double Highprecision_Sin(double x){
        
        //always wrap input angle to -PI..PI
        if (x < -3.14159265)
            x += 6.28318531;
        else if (x >  3.14159265)
            x -= 6.28318531;

        //compute sine
        if (x < 0)
        {
            double sin = 1.27323954 * x + .405284735 * x * x;

            if (sin < 0)
                return .225 * (sin *-sin - sin) + sin;
            else
                return .225 * (sin * sin - sin) + sin;
        }
        else
        {
            double sin = 1.27323954 * x - 0.405284735 * x * x;

            if (sin < 0)
                return .225 * (sin *-sin - sin) + sin;
            else
                return .225 * (sin * sin - sin) + sin;
        }
        
    }
    
    /**
     * Returns the angle theta from the conversion of rectangular coordinates (x, y) to polar coordinates (r, theta).
     * This method computes the phase theta by computing an arc tangent of y/x in the range of -pi to pi.
     * @param y Y axis coordinate.
     * @param x X axis coordinate.
     * @return The theta component of the point (r, theta) in polar coordinates that corresponds to the point (x, y) in Cartesian coordinates.
     */
    public static double atan2(double y, double x) {
        double coeff_1 = 0.78539816339744830961566084581988;//Math.PI / 4d;
        double coeff_2 = 3d * coeff_1;
        double abs_y = Math.abs(y);
        double angle;
        if (x >= 0d) {
                double r = (x - abs_y) / (x + abs_y);
                angle = coeff_1 - coeff_1 * r;
        } else {
                double r = (x + abs_y) / (abs_y - x);
                angle = coeff_2 - coeff_1 * r;
        }
        return y < 0d ? -angle : angle - 0.06;
    }
}