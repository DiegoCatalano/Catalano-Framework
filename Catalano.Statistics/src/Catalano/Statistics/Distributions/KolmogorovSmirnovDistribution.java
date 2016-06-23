// Catalano Statistics Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
// Copyright © César Souza, 2009-2013
// cesarsouza at gmail.com
//
// Copyright © Richard Simard, 2010
// Copyright © Pierre L'Ecuyer, 2010
// This source code is based on the original work by Simar and Ecuyer.
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

package Catalano.Statistics.Distributions;

import Catalano.Math.Constants;
import Catalano.Math.Matrix;
import Catalano.Math.Special;

/**
 * Kolmogorov-Smirnov distribution.
 * @author Diego Catalano
 */
public class KolmogorovSmirnovDistribution implements IDistribution{
    
    private static int eV;
    
    private int numberOfSamples;

    public int getNumberOfSamples() {
        return numberOfSamples;
    }

    public void setNumberOfSamples(int numberOfSamples) {
        this.numberOfSamples = numberOfSamples;
    }

    public KolmogorovSmirnovDistribution(int samples) {
        this.numberOfSamples = samples;
    }

    @Override
    public double Mean() {
        return Constants.SqrtHalfPI * Constants.Log2 / Math.sqrt(numberOfSamples);
    }

    @Override
    public double Variance() {
        return ((Math.PI * Math.PI) / 12 - (Mean() * Mean())) / numberOfSamples;
    }

    @Override
    public double Entropy() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public double DistributionFunction(double x) {
        return CumulativeFunction(numberOfSamples, x);
    }

    @Override
    public double ProbabilityDensityFunction(double x) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public double LogProbabilityDensityFunction(double x) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    public double ComplementaryDistributionFunction(double x){
        return ComplementaryDistributionFunction(numberOfSamples, x);
    }
    
    /**
     * Computes the Upper Tail of the P[Dn &gt;= x] distribution.
     * @param x Value.
     * @return Result.
     */
    public double OneSideDistributionFunction(double x){
        return OneSideUpperTail(numberOfSamples, x);
    }
    
    public static double CumulativeFunction(int n, double x){
        double nxx = n * x * x; // nx²

        // First of all, check if the given values do not represent
        // a special case. There are some combination of values for
        // which the distribution has a known, exact solution.

        // Ruben-Gambino
        if (x >= 1.0 || nxx >= 18.0)
            return 1.0;

        if (x <= 0.5 / n)
            return 0.0;

        if (n == 1)
            return 2.0 * x - 1.0;

        if (x <= 1.0 / n)
            return (n <= 20) ? Special.Factorial(n) * Math.pow(2.0 * x - 1.0 / n, n)
                : Math.exp(Special.LogFactorial(n) + n * Math.log(2.0 * x - 1.0 / n));

        if (x >= 1.0 - 1.0 / n)
            return 1.0 - 2.0 * Math.pow(1.0 - x, n);

        // This is not a special case. Continue processing to
        //  select the most adequade method for the given inputs

        if (n <= 140)
        {
            // This is the first case (i) as referred in Simard's
            // paper. Use exact algorithms depending on nx² with
            // at least 13 to 15 decimal digits of precision.

            // Durbin
            if (nxx < 0.754693)
                return Durbin(n, x);

            // Pomeranz
            if (nxx < 4.0)
                return Pomeranz(n, x);

            // Complementary CDF
            return 1.0 - ComplementaryDistributionFunction(n, x);
        }
        else
        {
            if (n <= 100000)
            {
                // This is the second case (ii) referred in Simard's
                // paper. Use either the Durbin approximation or the
                // Pelz-Good asymptotic series depending on nx^(3/2).

                // Obs:
                //
                //   x^(3/2) = x^(1 + 1/2) = x*x^(1/2) = x*sqrt(x)
                //
                //          (n*x)      * sqrt(x) <= 1.40
                //   sqrt((n*x)*(n*x)) * sqrt(x) <= 1.40
                //   sqrt((n*x)*(n*x)  *      x) <= 1.40
                //        (n*x)*(n*x)  *      x  <= 1.96
                //       
                //    n*n*x*x*x <= 1.96
                //

                if (n * nxx * x <= 1.96)
                    return Durbin(n, x);
                else return PelzGood(n, x);
            }
            else
            {
                // This is the third case (iii) as referred in Simard's
                // paper. Use only the Pelz-Good asymptotic series.
                return PelzGood(n, x);
            }
        }
    }
    
    /**
     * Computes the Complementary Cumulative Distribution Function (1-CDF) for the Kolmogorov-Smirnov statistic's distribution.
     * @param n The sample size.
     * @param x The Kolmogorov-Smirnov statistic.
     * @return Under a sample size n.
     */
    public static double ComplementaryDistributionFunction(int n, double x){
        double nxx = n * x * x; // nx²

        // First of all, check if the given values do not represent
        // a special case. There are some combination of values for
        // which the distribution has a known, exact solution.

        // Ruben-Gambino's Complement
        if (x >= 1.0 || nxx >= 370.0)
            return 0.0;

        if (x <= 0.5 / n || nxx <= 0.0274)
            return 1.0;

        if (n == 1)
            return 2.0 - 2.0 * x;

        if (x <= 1.0 / n)
            return (n <= 20) ? 1.0 - Special.Factorial(n) * Math.pow(2.0 * x - 1.0 / n, n)
                : 1.0 - Math.exp(Special.LogFactorial(n) + n * Math.log(2.0 * x - 1.0 / n));

        if (x >= 1.0 - 1.0 / n)
            return 2.0 * Math.pow(1.0 - x, n);

        // This is not a special case. Continue processing to
        //  select the most adequade method for the given inputs

        if (n <= 140)
        {
            // This is the first region (i) of the complementary
            // CDF as detailed in Simard's paper. It is further
            // divided into two sub-regions.
            if (nxx >= 4.0)
            {
                // For x close to one, Simard's advocates the use of the one-
                // sided Kolmogorov-Smirnov statistic as given by Miller (1956).
                return 2 * OneSideUpperTail(n, x);
            }
            else
            {
                // For higher values of x, the direct cumulative 
                // distribution will be giving enough precision.
                return 1.0 - CumulativeFunction(n, x);
            }
        }
        else
        {
            // This is the second region (ii) of the complementary
            // CDF discussed in Simard's paper. It is again divided
            // into two sub-regions depending on the value of nx².
            if (nxx >= 2.2)
            {
                // In this region, the Miller approximation returns
                // at least 6 digits of precision (Simard, 2010).
                return 2 * OneSideUpperTail(n, x);
            }
            else
            {
                // In this region, the direct cumulative
                // distribution will give enough precision.
                return 1.0 - CumulativeFunction(n, x);
            }
        }
    }
    
    /**
     * Pelz-Good algorithm for computing lower-tail areas of the Kolmogorov-Smirnov distribution.
     */
    public static double PelzGood(int n, double x){
        final int maxTerms = 20;
        final double eps = 1.0e-10;

        final double PI2 = Math.PI * Math.PI;
        final double PI4 = PI2 * PI2;

        double sqrtN = Math.sqrt(n);

        double z = sqrtN * x;
        double z2 = z * z;
        double z3 = z2 * z;
        double z4 = z2 * z2;
        double z5 = z4 * z;
        double z6 = z4 * z2;
        double z7 = z4 * z3;
        double z8 = z4 * z4;
        double z10 = z8 * z2;

        double pz = -PI2 / (2 * z2);
        double term;


        double k0 = 0; // Evaluate K0(z)
        for (int k = 0; k <= maxTerms; k++)
        {
            double h = (k + 0.5);
            k0 += term = Math.exp(h * h * pz);
            if (term <= eps * k0) break;
        }


        double k1 = 0; // Evaluate K1(z)
        for (int k = 0; k <= maxTerms; k++)
        {
            double hh = (k + 0.5) * (k + 0.5);
            k1 += term = (PI2 * hh - z2) * Math.exp(hh * pz);
            if (Math.abs(term) <= eps * Math.abs(k1)) break;
        }


        double k2a = 0; // Evaluate 1st part of K2(z)
        for (int k = 0; k <= maxTerms; k++)
        {
            double hh = (k + 0.5) * (k + 0.5);
            k2a += term = (6 * z6 + 2 * z4
                        + PI2 * (2 * z4 - 5 * z2) * hh
                        + PI4 * (1 - 2 * z2) * hh * hh) * Math.exp(hh * pz);
            if (Math.abs(term) <= eps * Math.abs(k2a)) break;
        }

        double k2b = 0; // Evaluate 2nd part of K2(z)
        for (int k = 1; k <= maxTerms; k++)
        {
            double kk = k * k;
            k2b += term = PI2 * kk * Math.exp(kk * pz);
            if (term <= eps * k2b) break;
        }


        double k3a = 0; // Evaluate 1st part of K3(z)
        for (int k = 0; k <= maxTerms; k++)
        {
            double hh = (k + 0.5) * (k + 0.5);
            k3a += term = (-30 * z6 - 90 * z8
                + PI2 * (135 * z4 - 96 * z6) * hh
                + PI4 * (212 * z4 - 60 * z2) * hh * hh
                + PI2 * PI4 * hh * hh * hh * (5 - 30 * z2)) * Math.exp(hh * pz);
            if (Math.abs(term) <= eps * Math.abs(k3a)) break;
        }

        double k3b = 0; // Evaluate 2nd part of K3(z)
        for (int k = 1; k <= maxTerms; k++)
        {
            double kk = k * k;
            k3b += term = (3 * PI2 * kk * z2 - PI4 * kk * kk) * Math.exp(kk * pz);
            if (Math.abs(term) <= eps * Math.abs(k3b)) break;
        }


        // Evaluate the P[sqrt(N) * Dn <= z | H0]
        double sum = k0 * (Constants.Sqrt2PI / z)
                    + k1 * (Constants.SqrtHalfPI / (sqrtN * 3.0 * z4))
                    + k2a * (Constants.SqrtHalfPI / (n * 36.0 * z7))
                    - k2b * (Constants.SqrtHalfPI / (n * 18.0 * z3))
                    + k3a * (Constants.SqrtHalfPI / (n * sqrtN * 3240.0 * z10))
                    + k3b * (Constants.SqrtHalfPI / (n * sqrtN * 108.0 * z6));

        return sum;
    }
    
    /**
     * Computes the Upper Tail of the P[Dn &gt;= x] distribution.
     */
    public static double OneSideUpperTail(int n, double x) {
        if (n > 200000)
        {
            // Use an asymptotic formula for n too high
            double t = (6 * n * x + 1.0);
            double z = t * t / (18 * n);
            double v = (1.0 - (2 * z * z - 4 * z - 1.0) / (18 * n)) * Math.exp(-z);

            if (v <= 0.0) return 0.0;
            if (v >= 1.0) return 1.0;
            else return 1.0 * v;
        }
        else
        {
            // Use Smirnov's stable formula for computing Pn+, the upper tail of
            // the one-sided Kolmogorov's statistic Dn+. This upper tail of the
            // one-sided statistic can then be used to approximate the upper tail
            // Pn of the Kolmogorov statistic Dn with Pn ~ 2*Pn+.

            int jmax = (int)(n * (1.0 - x));
            if ((1.0 - x - (double)jmax / n) <= 0.0)
                jmax--;

            // Initialize
            int jdiv = (n > 3000) ? 2 : 3;
            int jstart = jmax / jdiv + 1;

            double logBinomial = Special.LogBinomial(n, jstart);
            double LOGJMAX = logBinomial;
            double EPSILON = 1.0E-12;


            // Start computing the series
            double sum = 0;

            for (int j = jstart; j <= jmax; j++)
            {
                double q = (double)j / n + x;
                double term = logBinomial + (j - 1) * Math.log(q) + (n - j) * Special.Log1p(-q);
                double t = Math.exp(term);

                sum += t;
                logBinomial += Math.log((double)(n - j) / (j + 1));

                if (t <= sum * EPSILON)
                    break;
            }

            jstart = jmax / jdiv;
            logBinomial = LOGJMAX + Math.log((double)(jstart + 1) / (n - jstart));

            for (int j = jstart; j > 0; j--)
            {
                double q = (double)j / n + x;
                double term = logBinomial + (j - 1) * Math.log(q) + (n - j) * Special.Log1p(-q);
                double t = Math.exp(term);

                sum += t;
                logBinomial += Math.log((double)j / (n - j + 1));

                if (t <= sum * EPSILON)
                    break;
            }


            return sum * x + Math.exp(n * Special.Log1p(-x));
        }
    }
    
    /**
     * Pomeranz algorithm.
     */
    public static double Pomeranz(int n, double x){
        // The Pomeranz algorithm to compute the KS distribution
        double EPS = 1.0e-15;
        int ENO = 350;
        double RENO = Math.pow(2, ENO); // for renormalization of V
        int renormalizations;
        double t = n * x;
        double w, sum, minsum;
        int k, s;
        int r1, r2; // Indices i and i-1 for V[i][]
        int jlow, jup, klow, kup, kup0;

        double[] A = new double[2 * n + 3];
        double[] floors = new double[2 * n + 3];
        double[] ceilings = new double[2 * n + 3];

        double[][] V = new double[2][];
        for (int j = 0; j < V.length; j++)
            V[j] = new double[n + 2];

        double[][] H = new double[4][];     // = pow(w, j) / Factorial(j)
        for (int j = 0; j < H.length; j++)
            H[j] = new double[n + 2];

        double z = computeLimits(t, floors, ceilings);

        computeA(n, A, z);
        computeH(n, A, H);

        V[1][1] = RENO;
        renormalizations = 1;

        r1 = 0;
        r2 = 1;
        for (int i = 2; i <= 2 * n + 2; i++)
        {
            jlow = (int)(2 + floors[i]);
            if (jlow < 1)
                jlow = 1;
            jup = (int)(ceilings[i]);
            if (jup > n + 1)
                jup = n + 1;

            klow = (int)(2 + floors[i - 1]);
            if (klow < 1)
                klow = 1;
            kup0 = (int)(ceilings[i - 1]);

            // Find to which case it corresponds
            w = (A[i] - A[i - 1]) / n;
            s = -1;
            for (int j = 0; j < 4; j++)
            {
                if (Math.abs(w - H[j][1]) <= EPS)
                {
                    s = j;
                    break;
                }
            }

            minsum = RENO;
            r1 = (r1 + 1) & 1;          // i - 1
            r2 = (r2 + 1) & 1;          // i

            for (int j = jlow; j <= jup; j++)
            {
                kup = kup0;
                if (kup > j)
                    kup = j;
                sum = 0;
                for (k = kup; k >= klow; k--)
                    sum += V[r1][k] * H[s][j - k];
                V[r2][j] = sum;
                if (sum < minsum)
                    minsum = sum;
            }

            if (minsum < 1.0e-280)
            {
                // V is too small: renormalize to avoid underflow of probabilities
                for (int j = jlow; j <= jup; j++)
                    V[r2][j] *= RENO;
                renormalizations++;              // keep track of log of RENO
            }
        }

        sum = V[r2][n + 1];
        w = Special.LogFactorial(n) - renormalizations * ENO * Constants.Log2 + Math.log(sum);
        if (w >= 0)
            return 1;
        return Math.exp(w);
    }
    
    /**
     * Durbin's algorithm for computing P[Dn &lt; d]
     */
    public static double Durbin(int n, double d) {
        double s;

        int k = (int)(n * d) + 1;
        int m = 2 * k - 1;
        double h = k - n * d;
        double[][] H = new double[m][m];
        double[][] Q = new double[m][m];
        double[][] B = new double[m][m];


        for (int i = 0; i < m; i++)
            for (int j = 0; j < m; j++)
                if (i - j + 1 >= 0)
                    H[i][j] = 1;

        for (int i = 0; i < m; i++)
        {
            H[i][0] -= Math.pow(h, i + 1);
            H[m - 1][i] -= Math.pow(h, m - i);
        }

        H[m - 1][0] += (2 * h - 1 > 0 ? Math.pow(2 * h - 1, m) : 0);

        for (int i = 0; i < m; i++)
            for (int j = 0; j < m; j++)
                if (i - j + 1 > 0)
                    for (int g = 1; g <= i - j + 1; g++)
                        H[i][j] /= g;

        int pQ = 0;
        matrixPower(H, 0, Q, m, n, B);

        s = Q[k - 1][k - 1];

        for (int i = 1; i <= n; i++)
        {
            s *= (double)i / n;
            if (s < 1.0e-140)
            {
                s *= 1.0e140;
                pQ -= 140;
            }
        }

        return s * Math.pow(10.0, pQ);
    }
    
    /**
     * Computes matrix power. Used in the Durbin algorithm.
     */
    private static void matrixPower(double[][] A, int eA, double[][] V, int m, int n, double[][] B) {
        if (n == 1)
        {
            for (int i = 0; i < m; i++)
                for (int j = 0; j < m; j++)
                    V[i][j] = A[i][j];
            eV = eA;
            return;
        }

        matrixPower(A, eA, V, m, n / 2, B);
        
        B = Matrix.Multiply(V, B);

        int eB = 2 * eV;
        if (B[m / 2][m / 2] > 1.0e140)
        {
            for (int i = 0; i < m; i++)
                for (int j = 0; j < m; j++)
                    B[i][j] *= 1.0e-140;
            eB += 140;
        }

        if (n % 2 == 0)
        {
            for (int i = 0; i < m; i++)
                for (int j = 0; j < m; j++)
                    V[i][j] = B[i][j];
            eV = eB;
        }
        else
        {
            V = Matrix.Multiply(A, B);
            eV = eA + eB;
        }

        if (V[m / 2][m / 2] > 1.0e140)
        {
            for (int i = 0; i < m; i++)
                for (int j = 0; j < m; j++)
                    V[i][j] *= 1.0e-140;
            eV += 140;
        }
    }
    
    /**
     * Initializes the Pomeranz algorithm.
     */
    private static double computeLimits(double t, double[] floors, double[] ceilings){
        double floor = Math.floor(t);
        double ceiling = Math.ceil(t);

        double z = t - floor;
        double w = ceiling - t;

        if (z > 0.5)
        {
            for (int i = 1; i < floors.length; i += 2)
                floors[i] = i / 2 - 1 - floor;
            for (int i = 2; i < floors.length; i += 2)
                floors[i] = i / 2 - 2 - floor;
            for (int i = 1; i < ceilings.length; i += 2)
                ceilings[i] = i / 2 + 1 + floor;
            for (int i = 2; i < ceilings.length; i += 2)
                ceilings[i] = i / 2 + floor;
        }
        else if (z > 0.0)
        {
            ceilings[1] = 1 + floor;
            for (int i = 1; i < floors.length; i++)
                floors[i] = i / 2 - 1 - floor;
            for (int i = 2; i < ceilings.length; i++)
                ceilings[i] = i / 2 + floor;
        }
        else // if (z == 0)
        {
            for (int i = 1; i < floors.length; i += 2)
                floors[i] = i / 2 - floor;
            for (int i = 2; i < floors.length; i += 2)
                floors[i] = i / 2 - 1 - floor;
            for (int i = 1; i < ceilings.length; i += 2)
                ceilings[i] = i / 2 + floor;
            for (int i = 2; i < ceilings.length; i += 2)
                ceilings[i] = i / 2 - 1 + floor;
        }

        if (w < z) z = w;

        return z;
    }
    /**
     * Creates matrix A of the Pomeranz algorithm.
     */
    private static void computeA(int n, double[] A, double z){
        A[0] = 0;
        A[1] = 0;
        A[2] = z;
        A[3] = 1 - z;

        for (int i = 4; i < A.length - 1; i++)
            A[i] = A[i - 2] + 1;

        A[A.length - 1] = n;
    }
    
    /**
     * Computes matrix H of the Pomeranz algorithm.
     */
    private static double computeH(int n, double[] A, double[][] H){
        // Precomputes H[][] = (A[j] - A[j-1]^k / k!

        double w;
        H[0][0] = 1;
        w = 2.0 * A[2] / n;
        for (int j = 1; j <= n + 1; j++)
            H[0][j] = w * H[0][j - 1] / j;

        H[1][0] = 1;
        w = (1.0 - 2.0 * A[2]) / n;
        for (int j = 1; j <= n + 1; j++)
            H[1][j] = w * H[1][j - 1] / j;

        H[2][0] = 1;
        w = A[2] / n;
        for (int j = 1; j <= n + 1; j++)
            H[2][j] = w * H[2][j - 1] / j;

        H[3][0] = 1;
        for (int j = 1; j <= n + 1; j++)
            H[3][j] = 0;
        return w;
    }
    
}
