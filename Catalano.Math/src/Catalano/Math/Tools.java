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

import Catalano.Core.DoubleRange;
import Catalano.Core.FloatRange;
import Catalano.Core.IntRange;
import java.util.HashSet;
import java.util.Iterator;

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
     * Gets the square of the number.
     * @param x Number.
     * @return x*x;
     */
    public static double Square(double x){
        return x*x;
    }
    
    /**
     * Sinc function.
     * @param x Value.
     * @return Sinc of the value.
     */
    public static double Sinc(double x){
        return Math.sin(Math.PI * x) / (Math.PI * x);
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
     * Return the digital root from a number.
     * Example:
     * Number: 24365
     * Digital root: 2+4+3+6+5 = 20 = 2+0 = 2
     * @param n Number.
     * @return Digital root.
     */
    public static int DigitalRoot(int n){
        return 1 + (n - 1) % 9;
    }
    
    /**
     * Gets the greatest common divisor between two integers.
     * @param a First integer.
     * @param b Second integer.
     * @return Greatest common divisor.
     */
    public static int GreatestCommonDivisor(int a, int b){
        
        int x = a - b * (int)Math.floor(a / b);
        while(x != 0){
            a = b;
            b = x;
            x = a - b * (int)Math.floor(a / b); 
        }
        return b;
    }
    
        public static boolean isNumeric(String str) {
            if (str.length() == 0) {
                return false;
            }
            char[] chars = str.toCharArray();
            int sz = chars.length;
            boolean hasExp = false;
            boolean hasDecPoint = false;
            boolean allowSigns = false;
            boolean foundDigit = false;
            // deal with any possible sign up front
            int start = (chars[0] == '-') ? 1 : 0;
            if (sz > start + 1) {
                if (chars[start] == '0' && chars[start + 1] == 'x') {
                    int i = start + 2;
                    if (i == sz) {
                        return false; // str == "0x"
                    }
                    // checking hex (it can't be anything else)
                    for (; i < chars.length; i++) {
                        if ((chars[i] < '0' || chars[i] > '9')
                            && (chars[i] < 'a' || chars[i] > 'f')
                            && (chars[i] < 'A' || chars[i] > 'F')) {
                            return false;
                        }
                    }
                    return true;
                }
            }
            sz--; // don't want to loop to the last char, check it afterwords
                  // for type qualifiers
            int i = start;
            // loop to the next to last char or to the last char if we need another digit to
            // make a valid number (e.g. chars[0..5] = "1234E")
            while (i < sz || (i < sz + 1 && allowSigns && !foundDigit)) {
                if (chars[i] >= '0' && chars[i] <= '9') {
                    foundDigit = true;
                    allowSigns = false;
    
                } else if (chars[i] == '.') {
                    if (hasDecPoint || hasExp) {
                        // two decimal points or dec in exponent   
                        return false;
                    }
                    hasDecPoint = true;
                } else if (chars[i] == 'e' || chars[i] == 'E') {
                    // we've already taken care of hex.
                    if (hasExp) {
                        // two E's
                        return false;
                    }
                    if (!foundDigit) {
                        return false;
                    }
                    hasExp = true;
                    allowSigns = true;
                } else if (chars[i] == '+' || chars[i] == '-') {
                    if (!allowSigns) {
                        return false;
                    }
                    allowSigns = false;
                    foundDigit = false; // we need a digit after the E
                } else {
                    return false;
                }
                i++;
            }
            if (i < chars.length) {
                if (chars[i] >= '0' && chars[i] <= '9') {
                    // no type qualifier, OK
                    return true;
                }
                if (chars[i] == 'e' || chars[i] == 'E') {
                    // can't have an E at the last byte
                    return false;
                }
                if (chars[i] == '.') {
                    if (hasDecPoint || hasExp) {
                        // two decimal points or dec in exponent
                        return false;
                    }
                    // single trailing decimal point after non-exponent is ok
                    return foundDigit;
                }
                if (!allowSigns
                    && (chars[i] == 'd'
                        || chars[i] == 'D'
                        || chars[i] == 'f'
                        || chars[i] == 'F')) {
                    return foundDigit;
                }
                if (chars[i] == 'l'
                    || chars[i] == 'L') {
                    // not allowing L with an exponent
                    return foundDigit && !hasExp;
                }
                // last character is illegal
                return false;
            }
            // allowSigns is true iff the val ends in 'E'
            // found digit it to make sure weird stuff like '.' and '1E-' doesn't pass
            return !allowSigns && foundDigit;
        }
    
    /**
     * Checks if the specified integer is power of 2.
     * @param x Integer number to check.
     * @return True: if the specified number is power of 2, otherwise returns false.
     */
    public static boolean isPowerOf2( int x ){
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
     * Generates a permutation of given array. This method is properly synchronized
     * to allow correct use by more than one thread. However, if many threads
     * need to generate pseudorandom numbers at a great rate, it may reduce
     * contention for each thread to have its own pseudorandom-number generator.
     */
    public static void Permutate(int[] x) {
        random.permutate(x);
    }
    
    /**
     * Returns the previous power of 2 after the input value x.
     * @param  x value x.
     * @return Returns the previous power of 2 after the input value x.
     */
    public static int PreviousPowerOf2(int x){
        return NextPowerOf2(x + 1) / 2;
    }
    
    /**
     * Generate a random number in [0, 1). This method is properly synchronized
     * to allow correct use by more than one thread. However, if many threads
     * need to generate pseudorandom numbers at a great rate, it may reduce
     * contention for each thread to have its own pseudorandom-number generator. 
     */
    public static synchronized double RandomNextDouble() {
        return random.nextDouble();
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
    public static double Scale(DoubleRange from, DoubleRange to, double x){
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
     * Sum of the elements.
     * @param data Data.
     * @return Sum(data).
     */
    public static double Sum(double[] data){
        double sum = 0;
        for (int i = 0; i < data.length; i++) {
            sum += data[i];
        }
        return sum;
    }
    
    /**
     * Sum of the elements.
     * @param data Data.
     * @return Sum(data).
     */
    public static int Sum(int[] data){
        int sum = 0;
        for (int i = 0; i < data.length; i++) {
            sum += data[i];
        }
        return sum;
    }
    
    /**
     * Sum of the elements.
     * @param data Data.
     * @return Sum(data).
     */
    public static float Sum(float[] data){
        float sum = 0;
        for (int i = 0; i < data.length; i++) {
            sum += data[i];
        }
        return sum;
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
     * Get unique values form the array.
     * @param values Array of values.
     * @return Unique values.
     */
    public static int[] Unique(int[] values){
        HashSet<Integer> lst = new HashSet<Integer>();
        for (int i = 0; i < values.length; i++) {
            lst.add(values[i]);
        }
        
        int[] v = new int[lst.size()];
        Iterator<Integer> it = lst.iterator();
        for (int i = 0; i < v.length; i++) {
            v[i] = it.next();
        }
        
        return v;
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