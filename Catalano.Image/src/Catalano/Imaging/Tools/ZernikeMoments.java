// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
// Copyright © Øivind Due Trier, 2013
// oivind.due.trier at nr.no
// Dept. Informatics, Univ. Oslo
//
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

package Catalano.Imaging.Tools;

import Catalano.Math.ComplexNumber;
import java.util.ArrayList;

/**
 * Compute Zernike moments.
 * <para>Zernike polynomials are widely used as basis functions of image moments.
 * Since Zernike polynomials are orthogonal to each other, Zernike moments can represent properties of an image with no redundancy 
 * or overlap of information between the moments. Although Zernike moments are significantly dependent on the scaling and the translation 
 * of the object in an ROI, their magnitudes are independent of the rotation angle of the object.
 * Thus, they can be utilized to extract features from images that describe the shape characteristics of an object.
 * For instance, Zernike moments are utilized as shape descriptors to classify benign and malignant breast masses. </para>
 * @author Diego Catalano
 */
public final class ZernikeMoments {

    /**
     * Don't let anyone instantiate this class.
     */
    private ZernikeMoments() {}
    
    /**
     * Computes the Zernike radial polynomial, Rnm(p), in the definition of V(n,m,x,y).
     * @param n Moment order.
     * @param m_in Moment order.
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     * @return Value of Rnm(p).
     */
    public static double RadialPolynomial(int n, int m_in, double x, double y){
        
        int a; // (n-s)! 
        int b; //   s!   
        int c; // [(n+|m|)/2-s]! 
        int d; // [(n-|m|)/2-s]! 
        int sign;

        int m = Math.abs(m_in);

        if ((n - m) % 2 != 0){
            return 0;
        }

        double res = 0;
        if ((x * x + y * y) <= 1.0) {
            
            sign = 1;
            a = Factorial(n);
            b=1;
            c = Factorial((n + m) / 2);
            d = Factorial((n - m) / 2);
            
            // Before the loop is entered, all the integer variables
            // (sign, a, b, c, d) have their correct values for the
            // s=0 case.
            for(int s = 0; s <= (n - m) / 2; s++){
                res += sign*(a * 1.0 / (b * c * d)) * Math.pow((x * x + y * y),(n / 2.0) -s);
                // Now update the integer variables before the next
                // iteration of the loop.
                if (s < (n - m) / 2){
                    sign = -sign;
                    a /= (n - s);
                    b *= (s + 1);
                    c /= ((n + m) / 2 - s);
                    d /= ((n - m) / 2 - s);
                }
            }
        }
        return res;
    }
    
    /**
     * Computes the Zernike basis function V(n,m,x,y).
     * @param n Moment order.
     * @param m Moment order.
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     * @return Complex number for V(n,m,x,y).
     */
    public static ComplexNumber ZernikeBasisFunction(int n, int m, double x, double y) {
        if ((x * x + y * y) > 1.0) {
            return new ComplexNumber(0.0, 0.0);
        }
        else {
            double r = RadialPolynomial(n,m,x,y);
            double arg = m * Math.atan2(y,x);
            double real = r * Math.cos(arg);
            double imag = r * Math.sin(arg);
            return new ComplexNumber(real,imag);
        }
    }
    
    /**
     * Compute zernike moments at n and m moments order.
     * Also compute the width, height, and centroid of the shape.
     * @param x X axis coordinates.
     * @param y Y axis coordinates.
     * @param nPoints Indicates the total number of points in the digitized shape.
     * @param n Moment order.
     * @param m Moment order.
     * @return Zernike moment.
     */
    public static ComplexNumber ZernikeMoments(double[] x, double[] y, int nPoints, int n, int m){
        int diff = n-Math.abs(m);
        if ((n<0) || (Math.abs(m) > n) || (diff%2!=0)){
            throw new IllegalArgumentException("zer_mom: n="+n+", m="+m+", n-|m|="+diff);    
        }
        double xmin = Double.MAX_VALUE;
        double ymin = Double.MAX_VALUE;
        double xmax = Double.MIN_VALUE;
        double ymax = Double.MIN_VALUE;
        
        for(int i = 0; i < nPoints; i++){
            xmin=Math.min(xmin,x[i]);
            xmax=Math.max(xmax,x[i]);
            ymin=Math.min(ymin,y[i]);
            ymax=Math.max(ymax,y[i]);
        }
        
        double w  = xmax-xmin;//width
        double h = ymax-ymin;//height
        double cx = xmin+w/2;
        double cy = ymin+h/2;
        return ZernikeMoments(x, y, nPoints, w, h, cx, cy, n, m);
    }
    
    /**
     * Compute zernike moments at specified order.
     * @param x X axis coordinates.
     * @param y Y axis coordinates.
     * @param nPoints Indicates the total number of points in the digitized shape.
     * @param w Width of the bounding box of the shape.
     * @param h Height of the bounding box of the shape.
     * @param cx X axis of centroid point of the shape.
     * @param cy Y axis of centroid point of the shape.
     * @param n Moment order.
     * @param m Moment order.
     * @return Zernike moment.
     */
    public static ComplexNumber ZernikeMoments(double[] x, double[] y, int nPoints, double w, double h, double cx, double cy, int n, int m){
        double i_0, j_0;
        double i_scale, j_scale;
        double X,Y;
        ComplexNumber v;
        //double isize, jsize;

        int diff = n-Math.abs(m);
        if ((n<0) || (Math.abs(m) > n) || (diff%2!=0)){
            throw new IllegalArgumentException("zer_mom: n="+n+", m="+m+", n-|m|="+diff);    
        }
        //isize = ww;
        //jsize = hh;
        i_0 = cx;
        j_0 = cy;
        double radius = w/2;
        i_scale=Math.sqrt(2)*radius;
        radius=h/2;
        j_scale=Math.sqrt(2)*radius; //note we want to construct a circle to contain the rectangle
        ComplexNumber res = new ComplexNumber();
        for(int i=0; i<nPoints; i++){
            X = (x[i]-i_0)/i_scale;
            Y = (y[i]-j_0)/j_scale;
            if (((X*X + Y*Y) <= 1.0)){// we ignore (x,y) not in the unit circle
                v = ZernikeBasisFunction(n,m,X,Y);
                res.real = res.real+v.real;
                res.imaginary = res.imaginary+v.imaginary;
            }
        }
        res.real = res.real*(n+1)/Math.PI;
        res.imaginary = res.imaginary*(n+1)/Math.PI;
        return res;
    }
    
    /**
     * Compute the set of Zernike's moments up to the specified order.
     * Also compute the width, height, and centroid of the shape.
     * @param order Order.
     * @param x X axis coordinates.
     * @param y Y axis coordinates.
     * @param npoints Indicates the total number of points in the digitized shape.
     * @return Zernike`s moments.
     */
    public static ComplexNumber[] ZernikeMoments(int order, double[] x, double[] y, int npoints){
        double xmin = Double.MAX_VALUE;
        double ymin = Double.MAX_VALUE;
        double xmax = Double.MIN_VALUE;
        double ymax = Double.MIN_VALUE;
        for(int i=0; i<npoints; i++){
            xmin=Math.min(xmin,x[i]);
            xmax=Math.max(xmax,x[i]);
            ymin=Math.min(ymin,y[i]);
            ymax=Math.max(ymax,y[i]);
        }
        double ww  = xmax-xmin;//width
        double hh = ymax-ymin;//height
        double cx = xmin+ww/2;
        double cy = ymin+hh/2;
        return ZernikeMoments(order, x, y, npoints, ww, hh, cx, cy);
    }
    
    /**
     * Compute the set of Zernike's moments up to the specified order.
     * @param order Order.
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     * @param npoints Indicates the total number of points in the digitized shape.
     * @param w Width of the bounding box of the shape.
     * @param h Height of the bounding box of the shape.
     * @param cx X axis of centroid point of the shape.
     * @param cy Y axis of centroid point of the shape.
     * @return Set of Zernike's moments.
     */
    public static ComplexNumber[] ZernikeMoments(int order, double[] x, double[] y, int npoints, double w, double h, double cx, double cy){
        ArrayList list = new ArrayList(order);
        int ct=0;
        for(int n=0; n<=order; n++){
            for(int m=0; m<=n; m++){
                if((n-Math.abs(m))%2 == 0){
                    ComplexNumber v = ZernikeMoments(x,y,npoints,w,h,cx,cy,n,m);
                    list.add(ct, v);
                    list.add(v);
                    ct++;
                }
            }
        }
        ComplexNumber[] mmts = new ComplexNumber[ct];
        for(int i=0; i<ct; i++){
            mmts[i]=(ComplexNumber)list.get(i);
        }
        return mmts;
    }
    
    /**
     * Computes factorial order n.
     * @param n Order.
     * @return Result.
     */
    private static int Factorial(int n){
        int x = 1;
        for (int i = 2; i <= n; i++){
            x *= i;
        }
        return x;
    }
}