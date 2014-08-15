// Catalano Math Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2014
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
import java.util.Random;

/**
 * Set of mathematical tools.
 * @author Diego Catalano
 */
public final class Tools {
    
    private static Random random = new Random();
    
    public static Random Random(){
        return random;
    }
    
    public static void SetupGenerator(int seed){
        random = new Random(seed);
    }

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
    
    /**
     * Hypotenuse calculus without overflow/underflow.
     * @param a First value.
     * @param b Second value.
     * @return The hypotenuse Sqrt(a^2 + b^2).
     */
    public static double Hypotenuse(double a, double b){
        double r = 0.0;
        double absA = Math.abs(a);
        double absB = Math.abs(b);

        if (absA > absB)
        {
            r = b / a;
            r = absA * Math.sqrt(1 + r * r);
        }
        else if (b != 0)
        {
            r = a / b;
            r = absB * Math.sqrt(1 + r * r);
        }

        return r;
    }
    
    public static int Log2( int x ){
        if ( x <= 65536 )
        {
            if ( x <= 256 )
            {
                if ( x <= 16 )
                {
                    if ( x <= 4 )
                    {
                        if ( x <= 2 )
                        {
                            if ( x <= 1 )
                                return 0;
                            return 1;
                        }
                        return 2;
                    }
                    if ( x <= 8 )
                        return 3;
                    return 4;
                }
                if ( x <= 64 )
                {
                    if ( x <= 32 )
                        return 5;
                    return 6;
                }
                if ( x <= 128 )
                    return 7;
                return 8;
            }
            if ( x <= 4096 )
            {
                if ( x <= 1024 )
                {
                    if ( x <= 512 )
                        return 9;
                    return 10;
                }
                if ( x <= 2048 )
                    return 11;
                return 12;
            }
            if ( x <= 16384 )
            {
                if ( x <= 8192 )
                    return 13;
                return 14;
            }
            if ( x <= 32768 )
                return 15;
            return 16;
        }

        if ( x <= 16777216 )
        {
            if ( x <= 1048576 )
            {
                if ( x <= 262144 )
                {
                    if ( x <= 131072 )
                        return 17;
                    return 18;
                }
                if ( x <= 524288 )
                    return 19;
                return 20;
            }
            if ( x <= 4194304 )
            {
                if ( x <= 2097152 )
                    return 21;
                return 22;
            }
            if ( x <= 8388608 )
                return 23;
            return 24;
        }
        if ( x <= 268435456 )
        {
            if ( x <= 67108864 )
            {
                if ( x <= 33554432 )
                    return 25;
                return 26;
            }
            if ( x <= 134217728 )
                return 27;
            return 28;
        }
        if ( x <= 1073741824 )
        {
            if ( x <= 536870912 )
                return 29;
            return 30;
        }
        return 31;
    }
    
    public static int Pow2( int power ){
        return ( ( power >= 0 ) && ( power <= 30 ) ) ? ( 1 << power ) : 0;
    }
    
}