// Catalano Math Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2014
// diego.catalano at live.com
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

import Catalano.Core.IntPoint;

/**
 * Defines a set of extension methods defining distance measures.
 * @author Diego Catalano
 */
public final class Distance {
    
    /**
     * Don't let anyone initialize this class.
     */
    private Distance () {};
    
    /**
     * Gets the Bray Curtis distance between two points.
     * @param p X axis coordinates.
     * @param q Y axis coordinates.
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
     * @param p X axis coordinates.
     * @param q Y axis coordinates.
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
     * @param p X axis coordinates.
     * @param q Y axis coordinates.
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
     * Gets the Cosine distance between two points.
     * @param p X axis coordinates.
     * @param q Y axis coordinates.
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
     * @param p X axis coordinates.
     * @param q Y axis coordinates.
     * @return The Euclidean distance between x and y.
     */
    public static double Euclidean(double[] p, double[] q){
        double d = 0.0, u;

        for (int i = 0; i < p.length; i++)
        {
            u = p[i] - q[i];
            d += u * u;
        }

        return Math.sqrt(d);
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
     * Gets the Hamming distance between two points.
     * @param p X axis coordinates.
     * @param q Y axis coordinates.
     * @return The Hamming distance between x and y.
     */
    public static double Hamming(double[] p, double[] q){
        double distance = 0;
        
        for (int i = 0; i < p.length; i++) {
            if (p[i] != q[i]) distance++;
        }
        
        return distance;
    }
    
    /**
     * Gets the Hamming distance between two points.
     * @param x1 X1 axis coordinate.
     * @param y1 Y1 axis coordinate.
     * @param x2 X2 axis coordinate.
     * @param y2 Y2 axis coordinate.
     * @return The Hamming distance between x and y.
     */
    public static double Hamming(double x1, double y1, double x2, double y2){
        double distance = 0;
        if (x1 != x2) distance++;
        if (y1 != y2) distance++;
        return distance;
    }
    
    /**
     * Gets the Hamming distance between two points.
     * @param p IntPoint with X and Y axis coordinates.
     * @param q IntPoint with X and Y axis coordinates.
     * @return The Hamming distance between x and y.
     */
    public static double Hamming(IntPoint p, IntPoint q){
        return Hamming(p.x,p.y,q.x,q.y);
    }
    
    /**
     * Gets the Jaccard distance between two points.
     * @param p X axis coordinates.
     * @param q Y axis coordinates.
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
     * Gets the Manhattan distance between two points.
     * @param p X axis coordinates.
     * @param q Y axis coordinates.
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
     * @param p X axis coordinates.
     * @param q Y axis coordinates.
     * @param r Order between two points.
     * @return The Minkowski distance between x and y.
     */
    public static double Minkowski(double[] p, double[] q, int r){
        double distance = 0;
        for (int i = 0; i < p.length; i++) {
            distance += Math.pow(Math.abs(p[i] - q[i]),r);
        }
        return Math.pow(distance,1/r);
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
}