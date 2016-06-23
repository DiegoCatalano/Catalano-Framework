// Catalano Math Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
// Copyright © César Souza, 2009-2012
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

package Catalano.Math.Distances;

import Catalano.Core.IntPoint;
import Catalano.Math.Constants;
import Catalano.Math.Matrix;

/**
 * Defines a set of extension methods defining distance measures.
 * References: http://www.ajmaa.org/RGMIA/papers/v7n4/Gsdmi_RGMIA.pdf
 * http://arabic-icr.googlecode.com/git/Papers/Comprehensive%20Survey%20on%20Distance-Similarity.pdf
 * @author Diego Catalano
 */
public final class Distance {
    
    /**
     * Don't let anyone initialize this class.
     */
    private Distance () {};
    
    public static double ArithmeticGeometricDivergence(double[] p, double[] q){
        double r = 0;
        for (int i = 0; i < p.length; i++) {
            double den = p[i] * q[i];
            if(den != 0){
                double num = p[i] + q[i];
                r += (num / 2) * Math.log(num / (2 * Math.sqrt(den)));
            }
        }
        return r;
    }
    
    /**
     * Bhattacharyya distance between two normalized histograms.
     * @param histogram1 Normalized histogram.
     * @param histogram2 Normalized histogram.
     * @return The Bhattacharyya distance between the two histograms.
     */
    public static double Bhattacharyya(double[] histogram1, double[] histogram2){
        int bins = histogram1.length; // histogram bins
        double b = 0; // Bhattacharyya's coefficient

        for (int i = 0; i < bins; i++)
            b += Math.sqrt(histogram1[i]) * Math.sqrt(histogram2[i]);

        // Bhattacharyya distance between the two distributions
        return Math.sqrt(1.0 - b);
    }
    
    /**
     * Gets the Bray Curtis distance between two points.
     * @param p A point in space.
     * @param q A point in space.
     * @return The Bray Curtis distance between x and y.
     */
    public static double BrayCurtis(double[] p, double[] q){
        double sumP, sumN;
        sumP = sumN = 0;
        
        for (int i = 0; i < p.length; i++) {
            sumN += Math.abs(p[i] - q[i]);
            sumP += Math.abs(p[i] + q[i]);
        }
        
        return sumN/sumP;
    }
    
    /**
     * Gets the Bray Curtis distance between two points.
     * @param x1 X1 axis coordinate.
     * @param y1 Y1 axis coordinate.
     * @param x2 X2 axis coordinate.
     * @param y2 X2 axis coordinate.
     * @return The Bray Curtis distance between x and y.
     */
    public static double BrayCurtis(double x1, double y1, double x2, double y2){
        double sumN = Math.abs(x1 - x2) + Math.abs(y1 - y2);
        double sumP = Math.abs(x1 + x2) + Math.abs(y1 + y2);
        
        return sumN/sumP;
    }
    
    /**
     * Gets the Bray Curtis distance between two points.
     * @param p IntPoint with X and Y axis coordinates.
     * @param q IntPoint with X and Y axis coordinates.
     * @return The Bray Curtis distance between x and y.
     */
    public static double BrayCurtis(IntPoint p, IntPoint q){
        return BrayCurtis(p.x,p.y,q.x,q.y);
    }
    
    /**
     * Gets the Canberra distance between two points.
     * @param p A point in space.
     * @param q A point in space.
     * @return The Canberra distance between x and y.
     */
    public static double Canberra(double[] p, double[] q){
        double distance = 0;
        
        for (int i = 0; i < p.length; i++) {
            distance += Math.abs(p[i] - q[i]) / (Math.abs(p[i]) + Math.abs(q[i]));
        }
        
        return distance;
    }
    
    /**
     * Gets the Canberra distance between two points.
     * @param x1 X1 axis coordinate.
     * @param y1 Y1 axis coordinate.
     * @param x2 X2 axis coordinate.
     * @param y2 Y2 axis coordinate.
     * @return The Canberra distance between x and y.
     */
    public static double Canberra(double x1, double y1, double x2, double y2){
        double distance;
        
        distance = Math.abs(x1 - x2) / (Math.abs(x1) + Math.abs(x2));
        distance += Math.abs(y1 - y2) / (Math.abs(y1) + Math.abs(y2));
        
        return distance;
    }
    
    /**
     * Gets the Canberra distance between two points.
     * @param p IntPoint with X and Y axis coordinates.
     * @param q IntPoint with X and Y axis coordinates.
     * @return The Canberra distance between x and y.
     */
    public static double Canberra(IntPoint p, IntPoint q){
        return Canberra(p.x,p.y,q.x,q.y);
    }

    /**
     * Gets the Chebyshev distance between two points.
     * @param p A point in space.
     * @param q A point in space.
     * @return The Chebyshev distance between x and y.
     */
    public static double Chebyshev(double[] p, double[] q){
        double max = Math.abs(p[0] - q[0]);
        
        for (int i = 1; i < p.length; i++){
            double abs = Math.abs(p[i] - q[i]);
            if (abs > max) max = abs;
        }
        
        return max;
    }
    
    /**
     * Gets the Chebyshev distance between two points.
     * @param x1 X1 axis coordinate.
     * @param y1 Y1 axis coordinate.
     * @param x2 X2 axis coordinate.
     * @param y2 Y2 axis coordinate.
     * @return The Chebyshev distance between x and y.
     */
    public static double Chebyshev(double x1, double y1, double x2, double y2){
        double max = Math.max(Math.abs(x1 - x2),Math.abs(y1 - y2));
        return max;
    }
    
    /**
     * Gets the Chebyshev distance between two points.
     * @param p IntPoint with X and Y axis coordinates.
     * @param q IntPoint with X and Y axis coordinates.
     * @return The Chebyshev distance between x and y.
     */
    public static double Chebyshev(IntPoint p, IntPoint q){
        return Chebyshev(p.x, p.y,q.x,q.y);
    }
    
    /**
     * Gets the Chessboard distance between two points.
     * @param x A point in space.
     * @param y A point in space.
     * @return The Chessboard distance between x and y.
     */
    public static double Chessboard(double[] x, double[] y){
        
        double d = 0;
        for (int i = 0; i < x.length; i++) {
            d = Math.max(d, x[i] - y[i]);
        }
        
        return d;
    }
    
    /**
     * Gets the Chessboard distance between two points.
     * @param x1 X1 axis coordinate.
     * @param y1 Y1 axis coordinate.
     * @param x2 X2 axis coordinate.
     * @param y2 Y2 axis coordinate.
     * @return The Chessboard distance between x and y.
     */
    public static double Chessboard(double x1, double y1, double x2, double y2){
        double dx = Math.abs(x1 - x2);
        double dy = Math.abs(y1 - y2);
        
        return Math.max(dx, dy);
    }
    
    /**
     * Gets the Chessboard distance between two points.
     * @param p IntPoint with X and Y axis coordinates.
     * @param q IntPoint with X and Y axis coordinates.
     * @return The Chessboard distance between x and y.
     */
    public static double Chessboard(IntPoint p, IntPoint q){
        return Chessboard(p.x,p.y,q.x,q.y);
    }
    
    /**
     * Gets the Chi Square distance between two normalized histograms.
     * @param histogram1 Histogram.
     * @param histogram2 Histogram.
     * @return The Chi Square distance between x and y.
     */
    public static double ChiSquare(double[] histogram1, double[] histogram2){
        double r = 0;
        for (int i = 0; i < histogram1.length; i++) {
            double t = histogram1[i] + histogram2[i];
            if(t != 0)
                r += Math.pow(histogram1[i] - histogram2[i], 2) / t;
        }
        
        return 0.5 * r;
    }
    
    /**
     * Gets the Correlation distance between two points.
     * @param p A point in space.
     * @param q A point in space.
     * @return The Correlation distance between x and y.
     */
    public static double Correlation(double[] p, double[] q){
        
        double x = 0;
        double y = 0;
        
        for (int i = 0; i < p.length; i++) {
            x += -p[i];
            y += -q[i];
        }
        
        x /= p.length;
        y /= q.length;
        
        double num = 0;
        double den1 = 0;
        double den2 = 0;
        for (int i = 0; i < p.length; i++)
        {
            num += (p[i] + x) * (q[i] + y);

            den1 += Math.abs(Math.pow(p[i] + x, 2));
            den2 += Math.abs(Math.pow(q[i] + x, 2));
        }

        return 1 - (num / (Math.sqrt(den1) * Math.sqrt(den2)));
        
    }
    
    /**
     * Gets the Cosine distance between two points.
     * @param p A point in space.
     * @param q A point in space.
     * @return The Cosine distance between x and y.
     */
    public static double Cosine(double[] p, double[] q){
        double sumProduct = 0;
        double sumP = 0, sumQ = 0;
        
        for (int i = 0; i < p.length; i++) {
            sumProduct += p[i] * q[i];
            sumP += Math.pow(Math.abs(p[i]),2);
            sumQ += Math.pow(Math.abs(q[i]),2);
        }
        
        sumP = Math.sqrt(sumP);
        sumQ = Math.sqrt(sumQ);
        
        double result = 1 - (sumProduct/(sumP*sumQ));
        
        return result;
    }
    
    /**
     * Gets the Cosine distance between two points.
     * @param x1 X1 axis coordinate.
     * @param y1 Y1 axis coordinate.
     * @param x2 X2 axis coordinate.
     * @param y2 Y2 axis coordinate.
     * @return The Cosine distance between x and y.
     */
    public static double Cosine(double x1, double y1, double x2, double y2){
        
        double sumProduct = x1*x2 + y1*y2;
        double sumP = Math.pow(Math.abs(x1),2) + Math.pow(Math.abs(x2),2);
        double sumQ = Math.pow(Math.abs(y1),2) + Math.pow(Math.abs(y2),2);
        sumP = Math.sqrt(sumP);
        sumQ = Math.sqrt(sumQ);
        
        double result = 1 - (sumProduct/(sumP*sumQ));
        return result;
    }
    
    /**
     * Gets the Cosine distance between two points.
     * @param p IntPoint with X and Y axis coordinates.
     * @param q IntPoint with X and Y axis coordinates.
     * @return The Cosine distance between x and y.
     */
    public static double Cosine(IntPoint p, IntPoint q){
        return Cosine(p.x,p.y,q.x,q.y);
    }
    
    /**
     * Gets the Euclidean distance between two points.
     * @param p A point in space.
     * @param q A point in space.
     * @return The Euclidean distance between x and y.
     */
    public static double Euclidean(double[] p, double[] q){
        return Math.sqrt(SquaredEuclidean(p, q));
    }
    
    /**
     * Gets the Euclidean distance between two points.
     * @param x1 X1 axis coordinate.
     * @param y1 Y1 axis coordinate.
     * @param x2 X2 axis coordinate.
     * @param y2 Y2 axis coordinate.
     * @return The Euclidean distance between x and y.
     */
    public static double Euclidean(double x1, double y1, double x2, double y2){
        double dx = Math.abs(x1 - x2);
        double dy = Math.abs(y1 - y2);
        
        return Math.sqrt(dx*dx + dy*dy);
    }
    
    /**
     * Gets the Euclidean distance between two points.
     * @param p IntPoint with X and Y axis coordinates.
     * @param q IntPoint with X and Y axis coordinates.
     * @return The Euclidean distance between x and y.
     */
    public static double Euclidean(IntPoint p, IntPoint q){
        return Euclidean(p.x,p.y,q.x,q.y);
    }
    
    /**
     * Gets the Hamming distance between two strings.
     * @param first First string.
     * @param second Second string.
     * @return The Hamming distance between p and q.
     */
    public static int Hamming(String first, String second){
        
        if(first.length() != second.length())
            throw new IllegalArgumentException("The size of string must be the same.");
        
        int diff = 0;
        for (int i = 0; i < first.length(); i++) 
            if(first.charAt(i) != second.charAt(i))
                diff++;
        return diff;
    }
    
    /**
     * Gets the Jaccard distance between two points.
     * @param p A point in space.
     * @param q A point in space.
     * @return The Jaccard distance between x and y.
     */
    public static double JaccardDistance(double[] p, double[] q){
        double distance = 0;
        int intersection = 0, union = 0;

        for ( int x = 0; x < p.length; x++)
        {
            if ( ( p[x] != 0 ) || ( q[x] != 0 ) )
            {
                if ( p[x] == q[x] )
                {
                    intersection++;
                }

                union++;
            }
        }

        if ( union != 0 )
            distance = 1.0 - ( (double) intersection / (double) union );
        else
            distance = 0;

        return distance;
    }
    
    /**
     * Gets the J-Divergence.
     * @param p P vector.
     * @param q Q vector.
     * @return The J-Divergence between p and q.
     */
    public static double JDivergence(double[] p, double[] q){
        boolean intersection = false;
        double k = 0;

        for (int i = 0; i < p.length; i++) {
            if (p[i] != 0 && q[i] != 0) {
                intersection = true;
                k += (p[i] - q[i]) * Math.log(p[i] / q[i]);
            }
        }

        if (intersection)
            return k;
        else
            return Double.POSITIVE_INFINITY;
    }
    
    /**
     * Gets the Jensen difference divergence.
     * @param p P vector.
     * @param q Q vector.
     * @return The Jensen difference between p and q.
     */
    public static double JensenDifferenceDivergence(double[] p, double[] q){
        boolean intersection = false;
        double k = 0;

        for (int i = 0; i < p.length; i++) {
            if (p[i] != 0 && q[i] != 0) {
                intersection = true;
                double pq = p[i] + q[i];
                k += (p[i] * Math.log(p[i]) + q[i] * Math.log(q[i])) / 2 - (pq / 2) * Math.log(pq / 2);
            }
        }

        if (intersection)
            return k;
        else
            return Double.POSITIVE_INFINITY;
    }
    
    /**
     * Gets the Jensen Shannon divergence.
     * @param p U vector.
     * @param q V vector.
     * @return The Jensen Shannon divergence between u and v.
     */
    public static double JensenShannonDivergence(double[] p, double[] q) {
        double[] m = new double[p.length];
        for (int i = 0; i < m.length; i++) {
            m[i] = (p[i] + q[i]) / 2;
        }

        return (KullbackLeiblerDivergence(p, m) + KullbackLeiblerDivergence(q, m)) / 2;
    }
    
    /**
     * Gets the K-Divergence.
     * @param p U vector.
     * @param q V vector.
     * @return The K-Divergence between u and v.
     */
    public static double KDivergence(double[] p, double[] q){
        double r = 0;
        for (int i = 0; i < p.length; i++) {
            double den = p[i] + q[i];
            if(den != 0 && p[i] != 0)
                r += p[i] * Math.log(2 * p[i] / den);
        }
        
        return r;
    }
    
    /**
     * Gets the Kumar-Johnson divergence.
     * @param p P vector.
     * @param q Q vector.
     * @return The Kumar-Johnson divergence between p and q.
     */
    public static double KumarJohnsonDivergence(double[] p, double[] q){
        double r = 0;
        for (int i = 0; i < p.length; i++) {
            if(p[i] != 0 && q[i] != 0){
                r += Math.pow(p[i]*p[i] - q[i]*q[i], 2) / 2 * Math.pow(p[i]*q[i], 1.5);
            }
        }
        return r;
    }
    
    /**
     * Gets the Kullback Leibler divergence.
     * @param p P vector.
     * @param q Q vector.
     * @return The Kullback Leibler divergence between u and v.
     */
    public static double KullbackLeiblerDivergence(double[] p, double[] q) {
        boolean intersection = false;
        double k = 0;

        for (int i = 0; i < p.length; i++) {
            if (p[i] != 0 && q[i] != 0) {
                intersection = true;
                k += p[i] * Math.log(p[i] / q[i]);
            }
        }

        if (intersection)
            return k;
        else
            return Double.POSITIVE_INFINITY;
    }
    
    /**
     * Gets the Mahalanobis distance.
     * @param A Matrix A.
     * @param B Matrix B.
     * @return The Mahalanobis distance between A and B.
     */
    public static double Mahalanobis(double[][] A, double[][] B){
        
        if(A[0].length != B[0].length)
            throw new IllegalArgumentException("The number of columns of both matrix must be equals.");
        
        double[][] subA = new double[A.length][A[0].length];
        double[][] subB = new double[B.length][B[0].length];
        
        //Center data A
        double[] meansA = new double[A[0].length];
        for (int j = 0; j < A[0].length; j++) {
            for (int i = 0; i < A.length; i++) {
                meansA[j] += A[i][j];
            }
            meansA[j] /= (double)A.length;
            for (int i = 0; i < A.length; i++) {
                subA[i][j] = A[i][j] - meansA[j];
            }
        }
        
        //Center data B
        double [] meansB = new double[B[0].length];
        for (int j = 0; j < B[0].length; j++) {
            for (int i = 0; i < B.length; i++) {
                meansB[j] += B[i][j];
            }
            meansB[j] /= (double)B.length;
            for (int i = 0; i < B.length; i++) {
                subB[i][j] = B[i][j] - meansB[j];
            }
        }
        
        //Matrix of covariance
        double[][] covA = Covariance(subA);
        double[][] covB = Covariance(subB);
        
        //Pooled covariance
        double rows = subA.length + subB.length;
        double[][] pCov = new double[covA.length][covA[0].length];
        for (int i = 0; i < pCov.length; i++) {
            for (int j = 0; j < pCov[0].length; j++) {
                pCov[i][j] = covA[i][j]*((double)subA.length/rows) + covB[i][j]*((double)subB.length/rows);
            }
        }
        
        //Inverse of pooled covariance
        pCov = Matrix.Inverse(pCov);
        
        //Compute mean difference
        double[] diff = new double[A[0].length];
        for (int i = 0; i < diff.length; i++) {
            diff[i] = meansA[i] - meansB[i];
        }
        
        return Math.sqrt(Matrix.InnerProduct(Matrix.MultiplyByTranspose(pCov, diff),diff));
        
    }
    
    private static double Covariance(double[] x, double[] y, double meanX, double meanY){
        double result = 0;
        for (int i = 0; i < x.length; i++) {
            result += (x[i] - meanX) * (y[i] - meanY);
        }
        
        return result / (double)(x.length);
    }
    
    private static double[][] Covariance(double[][] matrix){
        double[] means = new double[matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                means[j] += matrix[i][j];
            }
        }
        
        for (int i = 0; i < means.length; i++) {
            means[i] /= means.length;
        }
        
        return Covariance(matrix, means);
    }

    private static double[][] Covariance(double[][] matrix, double[] means){
        double[][] cov = new double[means.length][means.length];
        
        for (int i = 0; i < cov.length; i++) {
            for (int j = 0; j < cov[0].length; j++) {
                cov[i][j] = Covariance(Matrix.getColumn(matrix, i), Matrix.getColumn(matrix, j), means[i], means[j]);
            }
        }
        
        return cov;
        
    }
    
    /**
     * Gets the Manhattan distance between two points.
     * @param p A point in space.
     * @param q A point in space.
     * @return The Manhattan distance between x and y.
     */
    public static double Manhattan(double[] p, double[] q){
        double sum = 0;
        for (int i = 0; i < p.length; i++) {
            sum += Math.abs(p[i] - q[i]);
        }
        return sum;
    }
    
    /**
     * Gets the Manhattan distance between two points.
     * @param x1 X1 axis coordinate.
     * @param y1 Y1 axis coordinate.
     * @param x2 X2 axis coordinate.
     * @param y2 Y2 axis coordinate.
     * @return The Manhattan distance between x and y.
     */
    public static double Manhattan(double x1, double y1, double x2, double y2){
        double dx = Math.abs(x1 - x2);
        double dy = Math.abs(y1 - y2);
        
        return dx + dy;
    }
    
    /**
     * Gets the Manhattan distance between two points.
     * @param p IntPoint with X and Y axis coordinates.
     * @param q IntPoint with X and Y axis coordinates.
     * @return The Manhattan distance between x and y.
     */
    public static double Manhattan(IntPoint p, IntPoint q){
        return Manhattan(p.x,p.y,q.x,q.y);
    }
    
    /**
     * Gets the Minkowski distance between two points.
     * @param x1 X1 axis coordinate.
     * @param y1 Y1 axis coordinate.
     * @param x2 X2 axis coordinate.
     * @param y2 Y2 axis coordinate.
     * @param r Order between two points.
     * @return The Minkowski distance between x and y.
     */
    public static double Minkowski(double x1, double y1, double x2, double y2, int r){
        double sum = Math.pow(Math.abs(x1 - x2),r);
        sum += Math.pow(Math.abs(y1 - y2),r);
        return Math.pow(sum,1/r);
    }
    
    /**
     * Gets the Minkowski distance between two points.
     * @param p IntPoint with X and Y axis coordinates.
     * @param q IntPoint with X and Y axis coordinates.
     * @param r Order between two points.
     * @return The Minkowski distance between x and y.
     */
    public static double Minkowski(IntPoint p, IntPoint q, int r){
        return Minkowski(p.x,p.y,q.x,q.y,r);
    }
    
    /**
     * Gets the Minkowski distance between two points.
     * @param u A point in space.
     * @param v A point in space.
     * @param p Order between two points.
     * @return The Minkowski distance between x and y.
     */
    public static double Minkowski(double[] u, double[] v, double p){
        double distance = 0;
        for (int i = 0; i < u.length; i++) {
            distance += Math.pow(Math.abs(u[i] - v[i]),p);
        }
        return Math.pow(distance,1/p);
    }
    
    /**
     * Gets the Quasi-Euclidean distance between two points.
     * @param x1 X1 axis coordinates.
     * @param y1 Y1 axis coordinates.
     * @param x2 X2 axis coordinates.
     * @param y2 Y2 axis coordinates.
     * @return The Quasi-Euclidean distance between x and y.
     */
    public static double QuasiEuclidean(double x1, double y1, double x2, double y2){
        
        if (Math.abs(x1 - x2) > Math.abs(y1 - y2)){
            return Math.abs(x1 - x2) + (Constants.Sqrt2 - 1) * Math.abs(y1 - y2);
        }
        return (Constants.Sqrt2 - 1) * Math.abs(x1 - x2) + Math.abs(y1 - y2);
        
    }
    
    /**
     * Gets the Quasi-Euclidean distance between two points.
     * @param p IntPoint with X and Y axis coordinates.
     * @param q IntPoint with X and Y axis coordinates.
     * @return The Quasi Euclidean distance between p and q.
     */
    public static double QuasiEuclidean(IntPoint p, IntPoint q){
        return QuasiEuclidean(p.x, p.y, q.x, q.y);
    }
    
    /**
     * Gets the Square Euclidean distance between two points.
     * @param x A point in space.
     * @param y A point in space.
     * @return The Square Euclidean distance between x and y.
     */
    public static double SquaredEuclidean(double[] x, double[] y){
        double d = 0.0, u;

        for (int i = 0; i < x.length; i++)
        {
            u = x[i] - y[i];
            d += u * u;
        }

        return d;
    }
    
    /**
     * Gets the Squared Euclidean distance between two points.
     * @param x1 X1 axis coordinates.
     * @param y1 Y1 axis coordinates.
     * @param x2 X2 axis coordinates.
     * @param y2 Y2 axis coordinates.
     * @return The Squared euclidean distance between x and y.
     */
    public static double SquaredEuclidean(double x1, double y1, double x2, double y2){
        
        double dx = x2 - x1;
        double dy = y2 - y1;
        return dx*dx + dy*dy;
        
    }
    
    /**
     * Gets the Squared Euclidean distance between two points.
     * @param p IntPoint with X and Y axis coordinates.
     * @param q IntPoint with X and Y axis coordinates.
     * @return The Squared euclidean distance between x and y.
     */
    public static double SquaredEuclidean(IntPoint p, IntPoint q){
        
        double dx = q.x - p.x;
        double dy = q.y - p.y;
        return dx*dx + dy*dy;
        
    }
    
    /**
     * Gets the Symmetric Chi-square divergence.
     * @param p P vector.
     * @param q Q vector.
     * @return The Symmetric chi-square divergence between p and q.
     */
    public static double SymmetricChiSquareDivergence(double[] p, double[] q){
        double r = 0;
        for (int i = 0; i < p.length; i++) {
            double den = p[i] * q[i];
            if(den != 0){
                double p1 = p[i] - q[i];
                double p2 = p[i] + q[i];
                r += (p1 * p1 * p2) / den;
            }
        }
        
        return r;
    }
    
    /**
     * Gets the Taneja divergence.
     * @param p P vector.
     * @param q Q vector.
     * @return The Taneja divergence between p and q.
     */
    public static double Taneja(double[] p, double[] q){
        double r = 0;
        for (int i = 0; i < p.length; i++) {
            if(p[i] != 0 && q[i] != 0){
                double pq = p[i] + q[i];
                r += (pq / 2) * Math.log(pq / (2 * Math.sqrt(p[i]*q[i])));
            }
        }
        return r;
    }
    
    /**
     * Gets the Topsoe divergence.
     * @param p P vector.
     * @param q Q vector.
     * @return The Topsoe divergence between p and q.
     */
    public static double TopsoeDivergence(double[] p, double[] q){
        double r = 0;
        for (int i = 0; i < p.length; i++) {
            if(p[i] != 0 && q[i] != 0){
                double den = p[i] + q[i];
                r += p[i] * Math.log(2*p[i]/den) + q[i] * Math.log(2*q[i]/den);
            }
        }
        return r;
    }
    
}