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

import Catalano.Math.Functions.Gamma;

/**
 * Set of special mathematic functions.
 * @author Diego Catalano
 */
public final class Special {

    /**
     * Don't let anyone instantiate this class.
     */
    private Special() {}
    
    /**
     * Complementary error function of the specified value.
     * @param value Value.
     * @return Result.
     */
    public static double Erfc(double value) {
        double x, y, z, p, q;

        double[] P =
        {
            2.46196981473530512524E-10,
            5.64189564831068821977E-1,
            7.46321056442269912687E0,
            4.86371970985681366614E1,
            1.96520832956077098242E2,
            5.26445194995477358631E2,
            9.34528527171957607540E2,
            1.02755188689515710272E3,
            5.57535335369399327526E2
                    };
        double[] Q =
        {
            1.32281951154744992508E1,
            8.67072140885989742329E1,
            3.54937778887819891062E2,
            9.75708501743205489753E2,
            1.82390916687909736289E3,
            2.24633760818710981792E3,
            1.65666309194161350182E3,
            5.57535340817727675546E2
                    };

        double[] R =
        {
            5.64189583547755073984E-1,
            1.27536670759978104416E0,
            5.01905042251180477414E0,
            6.16021097993053585195E0,
            7.40974269950448939160E0,
            2.97886665372100240670E0
                    };
        double[] S =
        {
            2.26052863220117276590E0,
            9.39603524938001434673E0,
            1.20489539808096656605E1,
            1.70814450747565897222E1,
            9.60896809063285878198E0,
            3.36907645100081516050E0
                    };

        if (value < 0.0) x = -value;
        else x = value;

        if (x < 1.0) return 1.0 - Erf(value);

        z = -value * value;

        if (z < - Constants.LogMax)
        {
            if (value < 0) return (2.0);
            else return (0.0);
        }

        z = Math.exp(z);

        if (x < 8.0)
        {
            p = Polevl(x, P, 8);
            q = P1evl(x, Q, 8);
        }
        else
        {
            p = Polevl(x, R, 5);
            q = P1evl(x, S, 6);
        }

        y = (z * p) / q;

        if (value < 0) y = 2.0 - y;

        if (y == 0.0)
        {
            if (value < 0) return 2.0;
            else return (0.0);
        }


        return y;
    }
    
    /**
     * Error function of the specified value.
     * @param x Value.
     * @return Result.
     */
    public static double Erf(double x){
        double y, z;
        double[] T =
        {
            9.60497373987051638749E0,
            9.00260197203842689217E1,
            2.23200534594684319226E3,
            7.00332514112805075473E3,
            5.55923013010394962768E4
                };
        double[] U =
        {
            3.35617141647503099647E1,
            5.21357949780152679795E2,
            4.59432382970980127987E3,
            2.26290000613890934246E4,
            4.92673942608635921086E4
                };

        if (Math.abs(x) > 1.0)
            return (1.0 - Erfc(x));

        z = x * x;
        y = x * Polevl(z, T, 4) / P1evl(z, U, 5);

        return y;
    }
    
    /**
     * Evaluates polynomial of degree N.
     * @param x Value.
     * @param coef Coefficient.
     * @param n Degree.
     * @return Result.
     */
    public static double Polevl(double x, double[] coef, int n){
        double ans;

        ans = coef[0];

        for (int i = 1; i <= n; i++)
            ans = ans * x + coef[i];

        return ans;
    }
    
    /**
     * Evaluates polynomial of degree N with assumption that coef[N] = 1.0
     * @param x Value.
     * @param coef Coefficient.
     * @param n Degree.
     * @return Result.
     */
    public static double P1evl(double x, double[] coef, int n){
        double ans;

        ans = x + coef[0];

        for (int i = 1; i < n; i++)
            ans = ans * x + coef[i];

        return ans;
    }
    
    /**
     * Computes the Basic Spline of order <c>n</c>.
     */
    public static double BSpline(int n, double x){
        // ftp://ftp.esat.kuleuven.ac.be/pub/SISTA/hamers/PhD_bhamers.pdf
        // http://sepwww.stanford.edu/public/docs/sep105/sergey2/paper_html/node5.html

        if (n == Integer.MAX_VALUE)
            throw new IllegalArgumentException("n");


        double a = 1.0 / Special.Factorial(n);
        double c;

        boolean positive = true;
        for (int k = 0; k <= n + 1; k++)
        {
            c = Binomial(n + 1, k) * Tools.TruncatedPower(x + (n + 1.0) / 2.0 - k, n);
            a += positive ? c : -c;
            positive = !positive;
        }

        return a;
    }
    
    /**
     * Computes the binomial coefficients C(n,k).
     * @param n Coefficient.
     * @param k Coefficient.
     * @return Result.
     */
    public static double Binomial(int n, int k){
        return Math.round(Math.exp(LogFactorial(n) - LogFactorial(k) - LogFactorial(n - k)));
    }
    
    /**
     * Computes the log binomial Coefficients Log[C(n,k)].
     * @param n Coefficient.
     * @param k Coefficient.
     * @return Result.
     */
    public static double LogBinomial(int n, int k){
        return LogFactorial(n) - LogFactorial(k) - LogFactorial(n - k);
    }
    
    /**
     * Returns the log factorial of a number (ln(n!))
     * @param n Number.
     * @return Result.
     */
    public static double LogFactorial(int n){
        if (lnfcache == null)
            lnfcache = new double[101];

        if (n < 0)
        {
            // Factorial is not defined for negative numbers.
            try {
                throw new ArithmeticException("Argument cannot be negative.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (n <= 1)
        {
            // Factorial for n between 0 and 1 is 1, so log(factorial(n)) is 0.
            return 0.0;
        }
        if (n <= 100)
        {
            // Compute the factorial using ln(gamma(n)) approximation, using the cache
            // if the value has been previously computed.
            return (lnfcache[n] > 0) ? lnfcache[n] : (lnfcache[n] = Gamma.Log(n + 1.0));
        }
        else
        {
            // Just compute the factorial using ln(gamma(n)) approximation.
            return Gamma.Log(n + 1.0);
        }
    }
    
    // factorial function caches
    private static int ftop;
    private static double[] fcache;
    private static double[] lnfcache;
    
    /**
     * Computes the factorial of a number (n!).
     * @param n Number.
     * @return Result.
     */
    public static double Factorial(int n){
        if (fcache == null){
            // Initialize factorial cache
            fcache = new double[33];
            fcache[0] = 1; fcache[1] = 1;
            fcache[2] = 2; fcache[3] = 6;
            fcache[4] = 24; ftop = 4;
        }

        if (n < 0){
            try {
                throw new ArithmeticException("Argument cannot be negative.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (n > 32){
            // Return Gamma approximation using exp(gammaln(n+1)),
            //  which for some reason is faster than gamma(n+1).
            return Math.exp(Gamma.Log(n + 1.0));
        }
        else{
            // Compute in the standard way, but use the
            //  factorial cache to speed up computations.
            while (ftop < n){
                int j = ftop++;
                fcache[ftop] = fcache[j] * ftop;
            }
            return fcache[n];
        }
    }
    
    /**
     * Computes log(1-x) without losing precision for small values of x.
     * @param x Value.
     * @return Result.
     */
    public static double Log1m(double x){
        if (x >= 1.0)
            return Double.NaN;

        if (Math.abs(x) > 1e-4)
            return Math.log(1.0 - x);

        // Use Taylor approx. log(1 + x) = x - x^2/2 with error roughly x^3/3
        // Since |x| < 10^-4, |x|^3 < 10^-12, relative error less than 10^-8
        return -(0.5 * x + 1.0) * x;
    }
    
    /**
     * Computes log(1+x) without losing precision for small values of x.
     * @param x Value.
     * @return Result.
     */
    public static double Log1p(double x){
        if (x <= -1.0)
            return Double.NaN;

        if (Math.abs(x) > 1e-4)
            return Math.log(1.0 + x);

        // Use Taylor approx. log(1 + x) = x - x^2/2 with error roughly x^3/3
        // Since |x| < 10^-4, |x|^3 < 10^-12, relative error less than 10^-8
        return (-0.5 * x + 1.0) * x;
    }
    
    /**
     * Compute exp(x) - 1 without loss of precision for small values of x.
     * @param x Value.
     * @return Result.
     */
    public static double Expm1(double x){
        if (Math.abs(x) < 1e-5)
            return x + 0.5 * x * x;
        else
            return Math.exp(x) - 1.0;
    }
    
    /**
     * Estimates unit roundoff in quantities of size x.
     * @param x Value.
     * @return Result.
     */
    public static double Epslon(double x){
        double a, b, c, eps = 0;

        a = 1.3333333333333333;
        
        while(eps == 0.0){
            b = a - 1.0;
            c = b + b + b;
            eps = Math.abs(c - 1.0);
        }

        return eps * Math.abs(x);
    }
    
    /**
     * Returns A with the sign of B.
     * @param a Value.
     * @param b Value.
     * @return Result.
     */
    public static double Sign(double a, double b){
        double x = (a >= 0 ? a : -a);
        return (b >= 0 ? x : -x);
    }
    
    /**
     * Computes x + y without losing precision using ln(x) and ln(y).
     * @param lna Value.
     * @param lnc Value.
     * @return Result.
     */
    public static double LogDiff(double lna, double lnc){
        if (lna > lnc)
            return lna + Math.exp(1.0 - Math.exp(lnc - lna));

        return Double.NEGATIVE_INFINITY;
    }
    
    /**
     * Computes x + y without losing precision using ln(x) and ln(y).
     * @param lna Value.
     * @param lnc Value.
     * @return Result.
     */
    public static double LogSum(double lna, double lnc){
        if (lna == Double.NEGATIVE_INFINITY)
            return lnc;
        if (lnc == Double.NEGATIVE_INFINITY)
            return lna;

        if (lna > lnc)
            return lna + Special.Log1p(Math.exp(lnc - lna));

        return lnc + Special.Log1p(Math.exp(lna - lnc));
    }
    
    /**
     * Computes x + y without losing precision using ln(x) and ln(y).
     * @param lna Value.
     * @param lnc Value.
     * @return Result.
     */
    public static double LogSum(float lna, float lnc){
        if (lna == Float.NEGATIVE_INFINITY)
            return lnc;
        if (lnc == Float.NEGATIVE_INFINITY)
            return lna;

        if (lna > lnc)
            return lna + Special.Log1p(Math.exp(lnc - lna));

        return lnc + Special.Log1p(Math.exp(lna - lnc));
    }
}