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

package Catalano.Math.Functions;

import Catalano.Math.Constants;
import Catalano.Math.Special;

/**
 * Normal distribution functions.
 * @author Diego
 */
public class Normal {

    /**
     * Don't let anyone instantiate this class.
     */
    private Normal() {}
    
    /**
     * Normal cumulative distribution function.
     * @param value Value.
     * @return Result.
     */
    public static double Function(double value){
        return 0.5 * Special.Erfc(-value / Constants.Sqrt2);
    }
    
    /**
     * Normal (Gaussian) inverse cumulative distribution function.
     * @param y0 Value.
     * @return Result.
     */
    public static double Inverse(double y0){
        if (y0 <= 0.0)
        {
            if (y0 == 0) return Double.NEGATIVE_INFINITY;
            try {
                throw new IllegalArgumentException("y0");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (y0 >= 1.0)
        {
            if (y0 == 1) return Double.POSITIVE_INFINITY;
            try {
                throw new IllegalArgumentException("y0");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        double s2pi = Math.sqrt(2.0 * Math.PI);
        int code = 1;
        double y = y0;
        double x;

        double[] P0 =
        {
            -59.963350101410789,
            98.001075418599967,
            -56.676285746907027,
            13.931260938727968,
            -1.2391658386738125
        };

        double[] Q0 = 
        {
            1.9544885833814176,
            4.6762791289888153, 
            86.360242139089053,
            -225.46268785411937,
            200.26021238006066,
            -82.037225616833339,
            15.90562251262117,
            -1.1833162112133
        };

        double[] P1 = 
        {
            4.0554489230596245,
            31.525109459989388,
            57.162819224642128,
            44.080507389320083,
            14.684956192885803,
            2.1866330685079025,
            -0.14025607917135449,
            -0.035042462682784818,
            -0.00085745678515468545
        };

        double[] Q1 = 
        {
            15.779988325646675,
            45.390763512887922, 
            41.317203825467203, 
            15.04253856929075, 
            2.5046494620830941, 
            -0.14218292285478779, 
            -0.038080640769157827,
            -0.00093325948089545744
        };

        double[] P2 = 
        {
            3.2377489177694603, 
            6.9152288906898418, 
            3.9388102529247444,
            1.3330346081580755, 
            0.20148538954917908, 
            0.012371663481782003,
            0.00030158155350823543, 
            2.6580697468673755E-06,
            6.2397453918498331E-09
        };

        double[] Q2 = 
        {
            6.02427039364742,
            3.6798356385616087,
            1.3770209948908132,
            0.21623699359449663, 
            0.013420400608854318, 
            0.00032801446468212774,
            2.8924786474538068E-06,
            6.7901940800998127E-09
        };

        if (y > 0.8646647167633873)
        {
            y = 1.0 - y;
            code = 0;
        }

        if (y > 0.1353352832366127)
        {
            y -= 0.5;
            double y2 = y * y;
            x = y + y * ((y2 * Special.Polevl(y2, P0, 4)) / Special.P1evl(y2, Q0, 8));
            x *= s2pi;
            return x;
        }

        x = Math.sqrt(-2.0 * Math.log(y));
        double x0 = x - Math.log(x) / x;
        double z = 1.0 / x;
        double x1;

        if (x < 8.0)
        {
            x1 = (z * Special.Polevl(z, P1, 8)) / Special.P1evl(z, Q1, 8);
        }
        else
        {
            x1 = (z * Special.Polevl(z, P2, 8)) / Special.P1evl(z, Q2, 8);
        }

        x = x0 - x1;

        if (code != 0)
            x = -x;

        return x;
    }
    
    /**
     * High-accuracy Normal cumulative distribution function.
     * @param x Value.
     * @return Result.
     */
    public static double HighAccuracyFunction(double x){
        if (x < -8 || x > 8)
            return 0;

        double sum = x;
        double term = 0;

        double nextTerm = x;
        double pwr = x * x;
        double i = 1;

        // Iterate until adding next terms doesn't produce
        // any change within the current numerical accuracy.

        while (sum != term)
        {
            term = sum;

            // Next term
            nextTerm *= pwr / (i += 2);

            sum += nextTerm;
        }

        return 0.5 + sum * Math.exp(-0.5 * pwr - 0.5 * Constants.Log2PI);
    }
    
    /**
     * High-accuracy Complementary normal distribution function.
     * @param x Value.
     * @return Result.
     */
    public static double HighAccuracyComplemented(double x){
        double[] R = 
        {
            1.25331413731550025,   0.421369229288054473,  0.236652382913560671,
            0.162377660896867462,  0.123131963257932296,  0.0990285964717319214,
            0.0827662865013691773, 0.0710695805388521071, 0.0622586659950261958
        };

        int j = (int)(0.5 * (Math.abs(x) + 1));

        double a = R[j];
        double z = 2 * j;
        double b = a * z - 1;

        double h = Math.abs(x) - z;
        double q = h * h;
        double pwr = 1;

        double sum = a + h * b;
        double term = a;


        for (int i = 2; sum != term; i += 2)
        {
            term = sum;

            a = (a + z * b) / (i);
            b = (b + z * a) / (i + 1);
            pwr *= q;

            sum = term + pwr * (a + h * b);
        }

        sum *= Math.exp(-0.5 * (x * x) - 0.5 * Constants.Log2PI);

        return (x >= 0) ? sum : (1.0 - sum);
    }
    
}