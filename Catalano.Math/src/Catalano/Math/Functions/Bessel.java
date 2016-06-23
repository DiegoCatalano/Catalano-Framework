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

// Contains functions from the Cephes Math Library Release 2.8:
// June, 2000 Copyright 1984, 1987, 1988, 2000 by Stephen L. Moshier
//
// Original license is listed below:
//
//   Some software in this archive may be from the book _Methods and
// Programs for Mathematical Functions_ (Prentice-Hall or Simon & Schuster
// International, 1989) or from the Cephes Mathematical Library, a
// commercial product. In either event, it is copyrighted by the author.
// What you see here may be used freely but it comes with no support or
// guarantee.
//
//   The two known misprints in the book are repaired here in the
// source listings for the gamma function and the incomplete beta
// integral.
//
//
//   Stephen L. Moshier
//   moshier@na-net.ornl.gov
//

package Catalano.Math.Functions;

/**
 * Bessel functions.
 * <para> Bessel functions, first defined by the mathematician Daniel Bernoulli and generalized by Friedrich Bessel,
 * are the canonical solutions y(x) of Bessel's differential equation.</para>
 * @author Diego Catalano
 */
public final class Bessel {

    private Bessel() {}
    
    /**
     * Bessel function of order 0.
     * @param x Value.
     * @return J0 value.
     */
    public static double J0(double x){
        double ax;
        
        if ((ax = Math.abs(x)) < 8.0){
            double y = x * x;
            double ans1 = 57568490574.0 + y * (-13362590354.0 + y * (651619640.7
                + y * (-11214424.18 + y * (77392.33017 + y * (-184.9052456)))));
            double ans2 = 57568490411.0 + y * (1029532985.0 + y * (9494680.718
                + y * (59272.64853 + y * (267.8532712 + y * 1.0))));

            return ans1 / ans2;
        }
        else{
            double z = 8.0 / ax;
            double y = z * z;
            double xx = ax - 0.785398164;
            double ans1 = 1.0 + y * (-0.1098628627e-2 + y * (0.2734510407e-4
                + y * (-0.2073370639e-5 + y * 0.2093887211e-6)));
            double ans2 = -0.1562499995e-1 + y * (0.1430488765e-3
                + y * (-0.6911147651e-5 + y * (0.7621095161e-6
                - y * 0.934935152e-7)));

            return Math.sqrt(0.636619772 / ax) *
                (Math.cos(xx) * ans1 - z * Math.sin(xx) * ans2);
        }
    }
    
    /**
     * Bessel function of order 1.
     * @param x Value.
     * @return J value.
     */
    public static double J(double x){
        double ax;
        double y;
        double ans1, ans2;

        if ((ax = Math.abs(x)) < 8.0){
            y = x * x;
            ans1 = x * (72362614232.0 + y * (-7895059235.0 + y * (242396853.1
                + y * (-2972611.439 + y * (15704.48260 + y * (-30.16036606))))));
            ans2 = 144725228442.0 + y * (2300535178.0 + y * (18583304.74
                + y * (99447.43394 + y * (376.9991397 + y * 1.0))));
            return ans1 / ans2;
        }
        else{
            double z = 8.0 / ax;
            double xx = ax - 2.356194491;
            y = z * z;

            ans1 = 1.0 + y * (0.183105e-2 + y * (-0.3516396496e-4
                + y * (0.2457520174e-5 + y * (-0.240337019e-6))));
            ans2 = 0.04687499995 + y * (-0.2002690873e-3
                + y * (0.8449199096e-5 + y * (-0.88228987e-6
                + y * 0.105787412e-6)));
            double ans = Math.sqrt(0.636619772 / ax) *
                (Math.cos(xx) * ans1 - z * Math.sin(xx) * ans2);
            if (x < 0.0) ans = -ans;
            return ans;
        }
    }
    
    /**
     * Bessel function of order n.
     * @param n Order.
     * @param x Value.
     * @return J value.
     */
    public static double J(int n, double x){
        int j, m;
        double ax, bj, bjm, bjp, sum, tox, ans;
        boolean jsum;

        double ACC = 40.0;
        double BIGNO = 1.0e+10;
        double BIGNI = 1.0e-10;

        if (n == 0) return J0(x);
        if (n == 1) return J(x);

        ax = Math.abs(x);
        if (ax == 0.0) return 0.0;
        else if (ax > (double)n)
        {
            tox = 2.0 / ax;
            bjm = J0(ax);
            bj = J(ax);
            for (j = 1; j < n; j++)
            {
                bjp = j * tox * bj - bjm;
                bjm = bj;
                bj = bjp;
            }
            ans = bj;
        }
        else
        {
            tox = 2.0 / ax;
            m = 2 * ((n + (int)Math.sqrt(ACC * n)) / 2);
            jsum = false;
            bjp = ans = sum = 0.0;
            bj = 1.0;
            for (j = m; j > 0; j--)
            {
                bjm = j * tox * bj - bjp;
                bjp = bj;
                bj = bjm;
                if (Math.abs(bj) > BIGNO)
                {
                    bj *= BIGNI;
                    bjp *= BIGNI;
                    ans *= BIGNI;
                    sum *= BIGNI;
                }
                if (jsum) sum += bj;
                jsum = !jsum;
                if (j == n) ans = bjp;
            }
            sum = 2.0 * sum - bj;
            ans /= sum;
        }

        return x < 0.0 && n % 2 == 1 ? -ans : ans;
    }
    
    /**
     * Bessel function of the second kind, of order 0.
     * @param x Value.
     * @return Y0 value.
     */
    public static double Y0(double x){
        if (x < 8.0){
            double y = x * x;

            double ans1 = -2957821389.0 + y * (7062834065.0 + y * (-512359803.6
                + y * (10879881.29 + y * (-86327.92757 + y * 228.4622733))));
            double ans2 = 40076544269.0 + y * (745249964.8 + y * (7189466.438
                + y * (47447.26470 + y * (226.1030244 + y * 1.0))));

            return (ans1 / ans2) + 0.636619772 * J0(x) * Math.log(x);
        }
        else{
            double z = 8.0 / x;
            double y = z * z;
            double xx = x - 0.785398164;

            double ans1 = 1.0 + y * (-0.1098628627e-2 + y * (0.2734510407e-4
                + y * (-0.2073370639e-5 + y * 0.2093887211e-6)));
            double ans2 = -0.1562499995e-1 + y * (0.1430488765e-3
                + y * (-0.6911147651e-5 + y * (0.7621095161e-6
                + y * (-0.934945152e-7))));
            return Math.sqrt(0.636619772 / x) *
                (Math.sin(xx) * ans1 + z * Math.cos(xx) * ans2);
        }
    }
    
    /**
     * Bessel function of the second kind, of order 1.
     * @param x Value.
     * @return Y value.
     */
    public static double Y(double x){
        if (x < 8.0)
        {
            double y = x * x;
            double ans1 = x * (-0.4900604943e13 + y * (0.1275274390e13
                + y * (-0.5153438139e11 + y * (0.7349264551e9
                + y * (-0.4237922726e7 + y * 0.8511937935e4)))));
            double ans2 = 0.2499580570e14 + y * (0.4244419664e12
                + y * (0.3733650367e10 + y * (0.2245904002e8
                + y * (0.1020426050e6 + y * (0.3549632885e3 + y)))));
            return (ans1 / ans2) + 0.636619772 * (J(x) * Math.log(x) - 1.0 / x);
        }
        else
        {
            double z = 8.0 / x;
            double y = z * z;
            double xx = x - 2.356194491;
            double ans1 = 1.0 + y * (0.183105e-2 + y * (-0.3516396496e-4
                + y * (0.2457520174e-5 + y * (-0.240337019e-6))));
            double ans2 = 0.04687499995 + y * (-0.2002690873e-3
                + y * (0.8449199096e-5 + y * (-0.88228987e-6
                + y * 0.105787412e-6)));
            return Math.sqrt(0.636619772 / x) *
                (Math.sin(xx) * ans1 + z * Math.cos(xx) * ans2);
        }
    }
    
    /**
     * Bessel function of the second kind, of order n.
     * @param n Order.
     * @param x Value.
     * @return Y value.
     */
    public static double Y(int n, double x){
        double by, bym, byp, tox;

        if (n == 0) return Y0(x);
        if (n == 1) return Y(x);

        tox = 2.0 / x;
        by = Y(x);
        bym = Y0(x);
        for (int j = 1; j < n; j++){
            byp = j * tox * by - bym;
            bym = by;
            by = byp;
        }
        return by;
    }
    
    /**
     * Bessel function of the first kind, of order 0.
     * @param x Value.
     * @return I0 value.
     */
    public static double I0(double x){
        double ans;
        double ax = Math.abs(x);

        if (ax < 3.75){
            double y = x / 3.75;
            y = y * y;
            ans = 1.0 + y * (3.5156229 + y * (3.0899424 + y * (1.2067492
               + y * (0.2659732 + y * (0.360768e-1 + y * 0.45813e-2)))));
        }
        else{
            double y = 3.75 / ax;
            ans = (Math.exp(ax) / Math.sqrt(ax)) * (0.39894228 + y * (0.1328592e-1
               + y * (0.225319e-2 + y * (-0.157565e-2 + y * (0.916281e-2
               + y * (-0.2057706e-1 + y * (0.2635537e-1 + y * (-0.1647633e-1
               + y * 0.392377e-2))))))));
        }

        return ans;
    }
    
    /**
     * Bessel function of the first kind, of order 1.
     * @param x Value.
     * @return I value.
     */
    public static double I(double x){
        double ans;

        double ax = Math.abs(x);

        if (ax < 3.75){
            double y = x / 3.75;
            y = y * y;
            ans = ax * (0.5 + y * (0.87890594 + y * (0.51498869 + y * (0.15084934
               + y * (0.2658733e-1 + y * (0.301532e-2 + y * 0.32411e-3))))));
        }
        else{
            double y = 3.75 / ax;
            ans = 0.2282967e-1 + y * (-0.2895312e-1 + y * (0.1787654e-1 - y * 0.420059e-2));
            ans = 0.39894228 + y * (-0.3988024e-1 + y * (-0.362018e-2 + y * (0.163801e-2 + y * (-0.1031555e-1 + y * ans))));
            ans *= Math.exp(ax) / Math.sqrt(ax);
        }
        return x < 0.0 ? -ans : ans;
    }
    
    /**
     * Bessel function of the first kind, of order n.
     * @param n Order.
     * @param x Value.
     * @return I value.
     */
    public static double I(int n, double x){
        if (n < 0)
            throw new IllegalArgumentException("the variable n out of range.");
        else if (n == 0)
            return I0(x);
        else if (n == 1)
            return I(x);

        if (x == 0.0)
            return 0.0;

        double ACC = 40.0;
        double BIGNO = 1.0e+10;
        double BIGNI = 1.0e-10;

        double tox = 2.0 / Math.abs(x);
        double bip = 0, ans = 0.0;
        double bi = 1.0;

        for (int j = 2 * (n + (int)Math.sqrt(ACC * n)); j > 0; j--)
        {
            double bim = bip + j * tox * bi;
            bip = bi;
            bi = bim;

            if (Math.abs(bi) > BIGNO)
            {
                ans *= BIGNI;
                bi *= BIGNI;
                bip *= BIGNI;
            }

            if (j == n)
                ans = bip;
        }

        ans *= I0(x) / bi;
        return x < 0.0 && n % 2 == 1 ? -ans : ans;
    }
}
