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

/**
 * Beta functions.
 * @author Diego Catalano
 */
public final class Beta {

    /**
     * Don't let anyone instantiate this class.
     */
    private Beta() {}
    
    /**
     * Beta function as gamma(a) * gamma(b) / gamma(a+b).
     * @param a Value.
     * @param b Value.
     * @return Result.
     */
    public static double Function(double a, double b){
        return Math.exp(Log(a, b));
    }
    
    /**
     * Natural logarithm of the Beta function.
     * @param a Value.
     * @param b Value.
     * @return Result.
     */
    public static double Log(double a, double b){
        return Gamma.Log(a) + Gamma.Log(b) - Gamma.Log(a + b);
    }
    
    /**
     * Incomplete (regularized) Beta function Ix(a, b).
     * @param a Value.
     * @param b Value.
     * @param x Value.
     * @return Result.
     */
    public static double Incomplete(double a, double b, double x){
        double aa, bb, t, xx, xc, w, y;
        boolean flag;

        if (a <= 0.0){
            try {
                throw new IllegalArgumentException(" 'a' Lower limit must be greater than zero.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (b <= 0.0){
            try {
                throw new IllegalArgumentException(" 'b' Upper limit must be greater than zero.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if ((x <= 0.0) || (x >= 1.0))
        {
            if (x == 0.0) return 0.0;
            if (x == 1.0) return 1.0;
            try {
                throw new IllegalArgumentException(" 'x' Value must be between 0 and 1.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        flag = false;
        if ((b * x) <= 1.0 && x <= 0.95)
        {
            t = PowerSeries(a, b, x);
            return t;
        }

        w = 1.0 - x;

        if (x > (a / (a + b)))
        {
            flag = true;
            aa = b;
            bb = a;
            xc = x;
            xx = w;
        }
        else
        {
            aa = a;
            bb = b;
            xc = w;
            xx = x;
        }

        if (flag && (bb * xx) <= 1.0 && xx <= 0.95)
        {
            t = PowerSeries(aa, bb, xx);
            if (t <= Constants.DoubleEpsilon) t = 1.0 - Constants.DoubleEpsilon;
            else t = 1.0 - t;
            return t;
        }

        y = xx * (aa + bb - 2.0) - (aa - 1.0);
        if (y < 0.0)
            w = Incbcf(aa, bb, xx);
        else
            w = Incbd(aa, bb, xx) / xc;


        y = aa * Math.log(xx);
        t = bb * Math.log(xc);
        if ((aa + bb) < Gamma.GammaMax && Math.abs(y) < Constants.LogMax && Math.abs(t) < Constants.LogMax)
        {
            t = Math.pow(xc, bb);
            t *= Math.pow(xx, aa);
            t /= aa;
            t *= w;
            t *= Gamma.Function(aa + bb) / (Gamma.Function(aa) * Gamma.Function(bb));
            if (flag)
            {
                if (t <= Constants.DoubleEpsilon) t = 1.0 - Constants.DoubleEpsilon;
                else t = 1.0 - t;
            }
            return t;
        }

        y += t + Gamma.Log(aa + bb) - Gamma.Log(aa) - Gamma.Log(bb);
        y += Math.log(w / aa);
        if (y < Constants.LogMin)
            t = 0.0;
        else
            t = Math.exp(y);

        if (flag)
        {
            if (t <= Constants.DoubleEpsilon) t = 1.0 - Constants.DoubleEpsilon;
            else t = 1.0 - t;
        }
        return t;
    }
    
    /**
     * Continued fraction expansion #1 for incomplete beta integral.
     * @param a Value.
     * @param b Value.
     * @param x Value.
     * @return Result.
     */
    public static double Incbcf(double a, double b, double x){
        double xk, pk, pkm1, pkm2, qk, qkm1, qkm2;
        double k1, k2, k3, k4, k5, k6, k7, k8;
        double r, t, ans, thresh;
        int n;
        double big = 4.503599627370496e15;
        double biginv = 2.22044604925031308085e-16;

        k1 = a;
        k2 = a + b;
        k3 = a;
        k4 = a + 1.0;
        k5 = 1.0;
        k6 = b - 1.0;
        k7 = k4;
        k8 = a + 2.0;

        pkm2 = 0.0;
        qkm2 = 1.0;
        pkm1 = 1.0;
        qkm1 = 1.0;
        ans = 1.0;
        r = 1.0;
        n = 0;
        thresh = 3.0 * Constants.DoubleEpsilon;

        do
        {
            xk = -(x * k1 * k2) / (k3 * k4);
            pk = pkm1 + pkm2 * xk;
            qk = qkm1 + qkm2 * xk;
            pkm2 = pkm1;
            pkm1 = pk;
            qkm2 = qkm1;
            qkm1 = qk;

            xk = (x * k5 * k6) / (k7 * k8);
            pk = pkm1 + pkm2 * xk;
            qk = qkm1 + qkm2 * xk;
            pkm2 = pkm1;
            pkm1 = pk;
            qkm2 = qkm1;
            qkm1 = qk;

            if (qk != 0) r = pk / qk;
            if (r != 0)
            {
                t = Math.abs((ans - r) / r);
                ans = r;
            }
            else
                t = 1.0;

            if (t < thresh) return ans;

            k1 += 1.0;
            k2 += 1.0;
            k3 += 2.0;
            k4 += 2.0;
            k5 += 1.0;
            k6 -= 1.0;
            k7 += 2.0;
            k8 += 2.0;

            if ((Math.abs(qk) + Math.abs(pk)) > big)
            {
                pkm2 *= biginv;
                pkm1 *= biginv;
                qkm2 *= biginv;
                qkm1 *= biginv;
            }
            if ((Math.abs(qk) < biginv) || (Math.abs(pk) < biginv))
            {
                pkm2 *= big;
                pkm1 *= big;
                qkm2 *= big;
                qkm1 *= big;
            }
        } while (++n < 300);

        return ans;
    }
    
    /**
     * Continued fraction expansion #2 for incomplete beta integral.
     * @param a Value.
     * @param b Value.
     * @param x Value.
     * @return Result.
     */
    public static double Incbd(double a, double b, double x){
        double xk, pk, pkm1, pkm2, qk, qkm1, qkm2;
        double k1, k2, k3, k4, k5, k6, k7, k8;
        double r, t, ans, z, thresh;
        int n;
        double big = 4.503599627370496e15;
        double biginv = 2.22044604925031308085e-16;

        k1 = a;
        k2 = b - 1.0;
        k3 = a;
        k4 = a + 1.0;
        k5 = 1.0;
        k6 = a + b;
        k7 = a + 1.0;
        k8 = a + 2.0;

        pkm2 = 0.0;
        qkm2 = 1.0;
        pkm1 = 1.0;
        qkm1 = 1.0;
        z = x / (1.0 - x);
        ans = 1.0;
        r = 1.0;
        n = 0;
        thresh = 3.0 * Constants.DoubleEpsilon;
        do
        {
            xk = -(z * k1 * k2) / (k3 * k4);
            pk = pkm1 + pkm2 * xk;
            qk = qkm1 + qkm2 * xk;
            pkm2 = pkm1;
            pkm1 = pk;
            qkm2 = qkm1;
            qkm1 = qk;

            xk = (z * k5 * k6) / (k7 * k8);
            pk = pkm1 + pkm2 * xk;
            qk = qkm1 + qkm2 * xk;
            pkm2 = pkm1;
            pkm1 = pk;
            qkm2 = qkm1;
            qkm1 = qk;

            if (qk != 0) r = pk / qk;
            if (r != 0)
            {
                t = Math.abs((ans - r) / r);
                ans = r;
            }
            else
                t = 1.0;

            if (t < thresh) return ans;

            k1 += 1.0;
            k2 -= 1.0;
            k3 += 2.0;
            k4 += 2.0;
            k5 += 1.0;
            k6 += 1.0;
            k7 += 2.0;
            k8 += 2.0;

            if ((Math.abs(qk) + Math.abs(pk)) > big)
            {
                pkm2 *= biginv;
                pkm1 *= biginv;
                qkm2 *= biginv;
                qkm1 *= biginv;
            }
            if ((Math.abs(qk) < biginv) || (Math.abs(pk) < biginv))
            {
                pkm2 *= big;
                pkm1 *= big;
                qkm2 *= big;
                qkm1 *= big;
            }
        } while (++n < 300);

        return ans;
    }
    
    /**
     * Power series for incomplete beta integral. Use when b*x is small and x not too close to 1.
     * @param a Value.
     * @param b Value.
     * @param x Value.
     * @return Result.
     */
    public static double PowerSeries(double a, double b, double x){
        double s, t, u, v, n, t1, z, ai;

        ai = 1.0 / a;
        u = (1.0 - b) * x;
        v = u / (a + 1.0);
        t1 = v;
        t = u;
        n = 2.0;
        s = 0.0;
        z = Constants.DoubleEpsilon * ai;
        while (Math.abs(v) > z)
        {
            u = (n - b) * x / n;
            t *= u;
            v = t / (a + n);
            s += v;
            n += 1.0;
        }
        s += t1;
        s += ai;

        u = a * Math.log(x);
        if ((a + b) < Gamma.GammaMax && Math.abs(u) < Constants.LogMax)
        {
            t = Gamma.Function(a + b) / (Gamma.Function(a) * Gamma.Function(b));
            s = s * t * Math.pow(x, a);
        }
        else
        {
            t = Gamma.Log(a + b) - Gamma.Log(a) - Gamma.Log(b) + u + Math.log(s);
            if (t < Constants.LogMin) s = 0.0;
            else s = Math.exp(t);
        }
        return s;
    }
}