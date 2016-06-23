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
 * Set of special mathematic functions.
 * @author Diego Catalano
 */
public final class Gamma {
    
    /**
     * Maximum gamma on the machine.
     */
    public final static double GammaMax = 171.624376956302725;

    /**
     * Don't let anyone instantiate this class.
     */
    private Gamma() {}
    
    /**
     * Gamma function of the specified value.
     * @param x Value.
     * @return Result.
     */
    public static double Function(double x){
        double[] P =
        {
            1.60119522476751861407E-4,
            1.19135147006586384913E-3,
            1.04213797561761569935E-2,
            4.76367800457137231464E-2,
            2.07448227648435975150E-1,
            4.94214826801497100753E-1,
            9.99999999999999996796E-1
                    };
        double[] Q =
        {
            -2.31581873324120129819E-5,
            5.39605580493303397842E-4,
            -4.45641913851797240494E-3,
            1.18139785222060435552E-2,
            3.58236398605498653373E-2,
            -2.34591795718243348568E-1,
            7.14304917030273074085E-2,
            1.00000000000000000320E0
            };

        double p, z;

        double q = Math.abs(x);

        if (q > 33.0)
        {
            if (x < 0.0)
            {
                p = Math.floor(q);

                if (p == q){
                    try {
                        throw new ArithmeticException("Overflow");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                z = q - p;
                if (z > 0.5)
                {
                    p += 1.0;
                    z = q - p;
                }
                z = q * Math.sin(Math.PI * z);

                if (z == 0.0){
                    try {
                        throw new ArithmeticException("Overflow");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                z = Math.abs(z);
                z = Math.PI / (z * Stirling(q));

                return -z;
            }
            else
            {
                return Stirling(x);
            }
        }

        z = 1.0;
        while (x >= 3.0)
        {
            x -= 1.0;
            z *= x;
        }

        while (x < 0.0)
        {
            if (x == 0.0)
            {
                throw new ArithmeticException();
            }
            else if (x > -1.0E-9)
            {
                return (z / ((1.0 + 0.5772156649015329 * x) * x));
            }
            z /= x;
            x += 1.0;
        }

        while (x < 2.0)
        {
            if (x == 0.0)
            {
                throw new ArithmeticException();
            }
            else if (x < 1.0E-9)
            {
                return (z / ((1.0 + 0.5772156649015329 * x) * x));
            }

            z /= x;
            x += 1.0;
        }

        if ((x == 2.0) || (x == 3.0)) return z;

        x -= 2.0;
        p = Special.Polevl(x, P, 6);
        q = Special.Polevl(x, Q, 7);
        return z * p / q;

    }
    
    public static double LowerIncomplete(double a, double x){
        final double eps = 0.000000000000001;
        final double big = 4503599627370496.0;
        final double biginv = 2.22044604925031308085e-16;

        if (a < 0){
            try {
                throw new IllegalArgumentException("Out of Range: a");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (x < 0){
            try {
                throw new IllegalArgumentException("Out of Range: x");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (a == 0.0)
        {
            if (x == 0.0)
                return Double.NaN;
            return 1d;
        }

        if (x == 0.0) return 0d;

        double ax = (a * Math.log(x)) - x - Gamma.Log(a);

        if (ax < -709.78271289338399)
            return 1.0;

        if (x <= 1 || x <= a)
        {
            double r2 = a;
            double c2 = 1;
            double ans2 = 1;

            do
            {
                r2 = r2 + 1;
                c2 = c2 * x / r2;
                ans2 += c2;
            }
            while ((c2 / ans2) > eps);

            return Math.exp(ax) * ans2 / a;
        }

        int c = 0;
        double y = 1 - a;
        double z = x + y + 1;

        double p3 = 1;
        double q3 = x;
        double p2 = x + 1;
        double q2 = z * x;
        double ans = p2 / q2;

        double error = 0;

        do
        {
            c++;
            y += 1;
            z += 2;
            double yc = y * c;

            double p = (p2 * z) - (p3 * yc);
            double q = (q2 * z) - (q3 * yc);

            if (q != 0)
            {
                double nextans = p / q;
                error = Math.abs((ans - nextans) / nextans);
                ans = nextans;
            }
            else
            {
                // zero div, skip
                error = 1;
            }

            // shift
            p3 = p2;
            p2 = p;
            q3 = q2;
            q2 = q;

            // normalize fraction when the numerator becomes large
            if (Math.abs(p) > big)
            {
                p3 *= biginv;
                p2 *= biginv;
                q3 *= biginv;
                q2 *= biginv;
            }
        }
        while (error > eps);

        return 1.0 - (Math.exp(ax) * ans);
    }
    
    /**
     * Gamma function as computed by Stirling's formula.
     * @param x Value.
     * @return Result.
     */
    public static double Stirling(double x){
        double[] STIR ={
            7.87311395793093628397E-4,
           -2.29549961613378126380E-4,
           -2.68132617805781232825E-3,
            3.47222221605458667310E-3,
            8.33333333333482257126E-2,
        };

        double MAXSTIR = 143.01608;

        double w = 1.0 / x;
        double y = Math.exp(x);

        w = 1.0 + w * Special.Polevl(w, STIR, 4);

        if (x > MAXSTIR)
        {
            double v = Math.pow(x, 0.5 * x - 0.25);
            y = v * (v / y);
        }
        else
        {
            y = Math.pow(x, x - 0.5) / y;
        }

        y = 2.50662827463100050242E0 * y * w;
        return y;
    }
    
    /**
     * Digamma function.
     * @param x Value.
     * @return Result.
     */
    public static double Digamma(double x){
        double s = 0;
        double w = 0;
        double y = 0;
        double z = 0;
        double nz = 0;

        boolean negative = false;

        if (x <= 0.0)
        {
            negative = true;
            double q = x;
            double p = (int)Math.floor(q);

            if (p == q){
                try {
                    throw new ArithmeticException("Function computation resulted in arithmetic overflow.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            nz = q - p;

            if (nz != 0.5)
            {
                if (nz > 0.5)
                {
                    p = p + 1.0;
                    nz = q - p;
                }
                nz = Math.PI / Math.tan(Math.PI * nz);
            }
            else
            {
                nz = 0.0;
            }

            x = 1.0 - x;
        }

        if (x <= 10.0 && x == Math.floor(x))
        {
            y = 0.0;
            int n = (int)Math.floor(x);
            for (int i = 1; i <= n - 1; i++)
            {
                w = i;
                y = y + 1.0 / w;
            }
            y = y - 0.57721566490153286061;
        }
        else
        {
            s = x;
            w = 0.0;

            while (s < 10.0)
            {
                w = w + 1.0 / s;
                s = s + 1.0;
            }

            if (s < 1.0E17)
            {
                z = 1.0 / (s * s);

                double polv = 8.33333333333333333333E-2;
                polv = polv * z - 2.10927960927960927961E-2;
                polv = polv * z + 7.57575757575757575758E-3;
                polv = polv * z - 4.16666666666666666667E-3;
                polv = polv * z + 3.96825396825396825397E-3;
                polv = polv * z - 8.33333333333333333333E-3;
                polv = polv * z + 8.33333333333333333333E-2;
                y = z * polv;
            }
            else
            {
                y = 0.0;
            }
            y = Math.log(s) - 0.5 / s - y - w;
        }

        if (negative == true)
        {
            y = y - nz;
        }

        return y;
    }
    
    /**
     * Complemented incomplete gamma function.
     * @param a Value.
     * @param x Value.
     * @return Result.
     */
    public static double ComplementedIncomplete(double a, double x){
        final double big = 4.503599627370496e15;
        final double biginv = 2.22044604925031308085e-16;
        double ans, ax, c, yc, r, t, y, z;
        double pk, pkm1, pkm2, qk, qkm1, qkm2;

        if (x <= 0 || a <= 0) return 1.0;

        if (x < 1.0 || x < a) return 1.0 - Incomplete(a, x);

        ax = a * Math.log(x) - x - Log(a);
        if (ax < -Constants.LogMax) return 0.0;

        ax = Math.exp(ax);

        // continued fraction
        y = 1.0 - a;
        z = x + y + 1.0;
        c = 0.0;
        pkm2 = 1.0;
        qkm2 = x;
        pkm1 = x + 1.0;
        qkm1 = z * x;
        ans = pkm1 / qkm1;

        do
        {
            c += 1.0;
            y += 1.0;
            z += 2.0;
            yc = y * c;
            pk = pkm1 * z - pkm2 * yc;
            qk = qkm1 * z - qkm2 * yc;
            if (qk != 0)
            {
                r = pk / qk;
                t = Math.abs((ans - r) / r);
                ans = r;
            }
            else
                t = 1.0;

            pkm2 = pkm1;
            pkm1 = pk;
            qkm2 = qkm1;
            qkm1 = qk;
            if (Math.abs(pk) > big)
            {
                pkm2 *= biginv;
                pkm1 *= biginv;
                qkm2 *= biginv;
                qkm1 *= biginv;
            }
        } while (t > Constants.DoubleEpsilon);

        return ans * ax;
    }
    
    /**
     * Incomplete gamma function.
     * @param a Value.
     * @param x Value.
     * @return Result.
     */
    public static double Incomplete(double a, double x){
        double ans, ax, c, r;

        if (x <= 0 || a <= 0) return 0.0;

        if (x > 1.0 && x > a) return 1.0 - ComplementedIncomplete(a, x);

        ax = a * Math.log(x) - x - Log(a);
        if (ax < -Constants.LogMax) return (0.0);

        ax = Math.exp(ax);

        r = a;
        c = 1.0;
        ans = 1.0;

        do
        {
            r += 1.0;
            c *= x / r;
            ans += c;
        } while (c / ans > Constants.DoubleEpsilon);

        return (ans * ax / a);
    }
    
    /**
     * Natural logarithm of gamma function.
     * @param x Value.
     * @return Result.
     */
    public static double Log(double x){
        double p, q, w, z;

        double[] A =
        {
                8.11614167470508450300E-4,
            -5.95061904284301438324E-4,
                7.93650340457716943945E-4,
            -2.77777777730099687205E-3,
                8.33333333333331927722E-2
                    };

        double[] B =
        {
            -1.37825152569120859100E3,
            -3.88016315134637840924E4,
            -3.31612992738871184744E5,
            -1.16237097492762307383E6,
            -1.72173700820839662146E6,
            -8.53555664245765465627E5
                };

        double[] C =
        {
            -3.51815701436523470549E2,
            -1.70642106651881159223E4,
            -2.20528590553854454839E5,
            -1.13933444367982507207E6,
            -2.53252307177582951285E6,
            -2.01889141433532773231E6
                    };

        if (x < -34.0)
        {
            q = -x;
            w = Log(q);
            p = Math.floor(q);

            if (p == q){
                try {
                    throw new ArithmeticException("Overflow.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            z = q - p;
            if (z > 0.5)
            {
                p += 1.0;
                z = p - q;
            }
            z = q * Math.sin(Math.PI * z);

            if (z == 0.0){
                try {
                    throw new ArithmeticException("Overflow.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            z = Constants.LogPI - Math.log(z) - w;
            return z;
        }

        if (x < 13.0)
        {
            z = 1.0;
            while (x >= 3.0)
            {
                x -= 1.0;
                z *= x;
            }
            while (x < 2.0)
            {
                if (x == 0.0){
                    try {
                        throw new ArithmeticException("Overflow.");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                z /= x;
                x += 1.0;
            }
            if (z < 0.0) z = -z;
            if (x == 2.0) return Math.log(z);
            x -= 2.0;
            p = x * Special.Polevl(x, B, 5) / Special.P1evl(x, C, 6);
            return (Math.log(z) + p);
        }

        if (x > 2.556348e305){
            try {
                throw new ArithmeticException("Overflow.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        q = (x - 0.5) * Math.log(x) - x + 0.91893853320467274178;
        if (x > 1.0e8) return (q);

        p = 1.0 / (x * x);
        if (x >= 1000.0)
        {
            q += ((7.9365079365079365079365e-4 * p
                - 2.7777777777777777777778e-3) * p
                + 0.0833333333333333333333) / x;
        }
        else
        {
            q += Special.Polevl(p, A, 4) / x;
        }

        return q;
    }
}