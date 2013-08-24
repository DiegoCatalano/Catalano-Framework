// Catalano Math Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2013
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

import Catalano.Core.DoubleRange;
import Catalano.Core.FloatRange;
import Catalano.Core.IntRange;

/**
 * Set of mathematical tools.
 * @author Diego Catalano
 */
public final class Tools {

    /**
     * Don't let anyone instantiate this class.
     */
    private Tools() {}
    
    /**
     * Gets the angle formed by the vector [x,y].
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     * @return Angle formed by the vector.
     */
    public static float Angle(float x, float y){
        if (y >= 0){
            if (x >= 0)
                return (float)Math.atan(y / x);
            return (float)(Math.PI - Math.atan(-y / x));
        }
        else{
            if (x >= 0)
                return (float)(2 * Math.PI - Math.atan(-y / x));
            return (float)(Math.PI + Math.atan(y / x));
        }
    }
    
    /**
     * Gets the angle formed by the vector [x,y].
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     * @return Angle formed by the vector.
     */
    public static double Angle(double x, double y){
        if (y >= 0){
            if (x >= 0)
                return Math.atan(y / x);
            return (Math.PI - Math.atan(-y / x));
        }
        else{
            if (x >= 0)
                return (2 * Math.PI - Math.atan(-y / x));
            return (Math.PI + Math.atan(y / x));
        }
    }
    
    /**
     * Checks if the specified integer is power of 2.
     * @param x Integer number to check.
     * @return True: if the specified number is power of 2, otherwise returns false.
     */
    public static boolean IsPowerOf2( int x ){
        return ( x > 0 ) ? ( ( x & ( x - 1 ) ) == 0 ) : false;
    }
    
    /**
     * Gets the proper modulus operation.
     * @param x Integer.
     * @param m Modulo.
     * @return Modulus.
     */
    public static int Mod(int x, int m){
        if (m < 0) m = -m;
        int r = x % m;
        return r < 0 ? r + m : r;
    }
    
    /**
     * Returns the next power of 2 after the input value x.
     * @param x Input value x.
     * @return Returns the next power of 2 after the input value x.
     */
    public static int NextPowerOf2(int x){
        --x;
        x |= x >> 1;
        x |= x >> 2;
        x |= x >> 4;
        x |= x >> 8;
        x |= x >> 16;
        return ++x;
    }
    
    /**
     * Returns the previous power of 2 after the input value x.
     * @param  Input value x.
     * @return Returns the previous power of 2 after the input value x.
     */
    public static int PreviousPowerOf2(int x){
        return NextPowerOf2(x + 1) / 2;
    }
    
    /**
     * Converts the value x (which is measured in the scale 'from') to another value measured in the scale 'to'.
     * @param from Scale from.
     * @param to Scale to.
     * @param x Value.
     * @return Result.
     */
    public static int Scale(IntRange from, IntRange to, int x){
        if (from.length() == 0) return 0;
        return (int)((to.length()) * (x - from.getMin()) / from.length() + to.getMin());
    }
    
    /**
     * Converts the value x (which is measured in the scale 'from') to another value measured in the scale 'to'.
     * @param from Scale from.
     * @param to Scale to.
     * @param x Value.
     * @return Result.
     */
    public static double Scale(DoubleRange from, DoubleRange to, int x){
        if (from.length() == 0) return 0;
        return ((to.length()) * (x - from.getMin()) / from.length() + to.getMin());
    }
    
    /**
     * Converts the value x (which is measured in the scale 'from') to another value measured in the scale 'to'.
     * @param from Scale from.
     * @param to Scale to.
     * @param x Value.
     * @return Result.
     */
    public static float Scale(FloatRange from, FloatRange to, int x){
        if (from.length() == 0) return 0;
        return (float)((to.length()) * (x - from.getMin()) / from.length() + to.getMin());
    }
    
    /**
     * Converts the value x (which is measured in the scale 'from') to another value measured in the scale 'to'.
     * @param fromMin Scale min from.
     * @param fromMax Scale max from.
     * @param toMin Scale min to.
     * @param toMax Scale max to.
     * @param x Value.
     * @return Result.
     */
    public static double Scale(double fromMin, double fromMax, double toMin, double toMax, double x){
            if (fromMax - fromMin == 0) return 0;
            return (toMax - toMin) * (x - fromMin) / (fromMax - fromMin) + toMin;
    }
    
    /**
     * Calculates the logarithm with base determined.
     * @param a Value.
     * @param b Base.
     * @return Result.
     */
    public static double Log(double a, double b){
        return Math.log(a) / Math.log(b);
    }
    
    /**
     * Truncated power function.
     * @param value Value.
     * @param degree Degree.
     * @return Result.
     */
    public static double TruncatedPower(double value, double degree){
        double x = Math.pow(value, degree);
        return (x > 0) ? x : 0.0;
    }
}