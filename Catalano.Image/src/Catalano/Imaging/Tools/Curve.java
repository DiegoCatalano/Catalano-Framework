// Catalano Imaging Library
// The Catalano Framework
//
// Copyright 2015 Diego Catalano
// diego.catalano at live.com
//
// Copyright 2015 Jerry Huxtable
// jerry at jhlabs.com
//
// Based on JH Labs
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package Catalano.Imaging.Tools;

import Catalano.Core.FloatPoint;
import java.io.Serializable;
import java.util.List;

/**
 * Curve representation.
 * @author Diego Catalano
 */
public class Curve implements Serializable{
    
    public float[] x;
    public float[] y;

    /**
     * Get value of the x axis coordinates.
     * @return X axis coordinates.
     */
    public float[] getX() {
        return x;
    }
    
    /**
     * Set values of the x axis coordinates.
     * @param x X axis coordinates.
     */
    public void setX(float[] x) {
        this.x = x;
    }

    /**
     * Get value of the y axis coordinates.
     * @return Y axis coordinates.
     */
    public float[] getY() {
        return y;
    }

    /**
     * Set value of the y axis coordinates.
     * @param y Y axis coordinates.
     */
    public void setY(float[] y) {
        this.y = y;
    }
    
    /**
     * Set the values.
     * @param x X axis coordinates.
     * @param y Y axis coordinates.
     */
    public void setXY(float[] x, float[] y){
        this.x = x;
        this.y = y;
    }
    
    /**
     * Initialize a new instance of the Curve class.
     */
    public Curve() {
        x = new float[0];
        y = new float[0];
    }
    
    /**
     * Initialize a new instance of the Curve class.
     * @param curve Curve.
     */
    public Curve( Curve curve ) {
        x = (float[])curve.x.clone();
        y = (float[])curve.y.clone();
    }
    
    /**
     *  Initialize a new instance of the Curve class.
     * @param x X points.
     * @param y Y points.
     */
    public Curve(float[] x, float[] y){
        this.x = x;
        this.y = y;
    }
    
    /**
     * Add collection of points.
     * @param points Points.
     */
    public void addPoint(List<FloatPoint> points){
        for (FloatPoint p : points) {
            addPoint(p);
        }
    }
    
    /**
     * Add point in the curve.
     * @param point Point.
     */
    public void addPoint(FloatPoint point){
        addPoint(point.x, point.y);
    }
    
    /**
     * Add point in the curve.
     * @param kx X axis coordinate.
     * @param ky Y axis coordinate.
     */
    public void addPoint( float kx, float ky ) {
        int pos = -1;
        int numKnots = x.length;
        float[] nx = new float[numKnots+1];
        float[] ny = new float[numKnots+1];
        int j = 0;
        for ( int i = 0; i < numKnots; i++ ) {
                if ( pos == -1 && x[i] > kx ) {
                        pos = j;
                        nx[j] = kx;
                        ny[j] = ky;
                        j++;
                }
                nx[j] = x[i];
                ny[j] = y[i];
                j++;
        }
        if ( pos == -1 ) {
                //pos = j;
                nx[j] = kx;
                ny[j] = ky;
        }
        x = nx;
        y = ny;
        //return pos;
    }
    
    /**
     * Remove point
     * @param index Index.
     */
    public void removePoint(int index) {
        int numKnots = x.length;
        if ( numKnots <= 2 )
                return;
        float[] nx = new float[numKnots-1];
        float[] ny = new float[numKnots-1];
        int j = 0;
        for ( int i = 0; i < numKnots-1; i++ ) {
                if ( i == index )
                        j++;
                nx[i] = x[j];
                ny[i] = y[j];
                j++;
        }
        x = nx;
        y = ny;
    }
    
    /**
     * Clear all the points.
     */
    public void clear(){
        x = y = null;
    }
    
    /**
     * Create LUT.
     * @return LUT.
     */
    public int[] makeLut() {
        int numKnots = x.length;
        float[] nx = new float[numKnots+2];
        float[] ny = new float[numKnots+2];
        System.arraycopy(x, 0, nx, 1, numKnots);
        System.arraycopy(y, 0, ny, 1, numKnots);
        nx[0] = nx[1];
        ny[0] = ny[1];
        nx[numKnots+1] = nx[numKnots];
        ny[numKnots+1] = ny[numKnots];

        int[] table = new int[256];
        for (int i = 0; i < 1024; i++) {
                float f = i/1024.0f;
                int x = (int)(255 * Curve.Spline( f, nx.length, nx ) + 0.5f);
                int y = (int)(255 * Curve.Spline( f, nx.length, ny ) + 0.5f);
                x = x > 255 ? 255 : x;
                x = x < 0 ? 0 : x;
                y = y > 255 ? 255 : y;
                y = y < 0 ? 0 : y;
                table[x] = y;
        }
        return table;
    }
    
    /**
     * Compute a Catmull-Rom spline.
     * @param x the input parameter
     * @param numKnots the number of knots in the spline
     * @param knots the array of knots
     * @return the spline value
     */
    public static float Spline(float x, int numKnots, float[] knots) {
        int span;
        int numSpans = numKnots - 3;
        float k0, k1, k2, k3;
        float c0, c1, c2, c3;

        if (numSpans < 1)
                throw new IllegalArgumentException("Too few knots in spline");

        x = x > 1 ? 1 : x;
        x = x < 0 ? 0 : x;
        x *= numSpans;
        span = (int)x;
        if (span > numKnots-4)
                span = numKnots-4;
        x -= span;

        k0 = knots[span];
        k1 = knots[span+1];
        k2 = knots[span+2];
        k3 = knots[span+3];

        c3 = -0.5f*k0 + 1.5f*k1 + -1.5f*k2 + 0.5f*k3;
        c2 = 1f*k0 + -2.5f*k1 + 2f*k2 + -0.5f*k3;
        c1 = -0.5f*k0 + 0f*k1 + 0.5f*k2 + 0f*k3;
        c0 = 0f*k0 + 1f*k1 + 0f*k2 + 0f*k3;

        return ((c3*x + c2)*x + c1)*x + c0;
    }
    
    /**
     * Compute a Catmull-Rom spline, but with variable knot spacing.
     * @param x the input parameter
     * @param numKnots the number of knots in the spline
     * @param xknots the array of knot x values
     * @param yknots the array of knot y values
     * @return the spline value
     */
    public static float Spline(float x, int numKnots, int[] xknots, int[] yknots) {
        int span;
        int numSpans = numKnots - 3;
        float k0, k1, k2, k3;
        float c0, c1, c2, c3;

        if (numSpans < 1)
                throw new IllegalArgumentException("Too few knots in spline");

        for (span = 0; span < numSpans; span++)
                if (xknots[span+1] > x)
                        break;
        if (span > numKnots-3)
                span = numKnots-3;
        float t = (float)(x-xknots[span]) / (xknots[span+1]-xknots[span]);
        span--;
        if (span < 0) {
                span = 0;
                t = 0;
        }

        k0 = yknots[span];
        k1 = yknots[span+1];
        k2 = yknots[span+2];
        k3 = yknots[span+3];

        c3 = -0.5f*k0 + 1.5f*k1 + -1.5f*k2 + 0.5f*k3;
        c2 = 1f*k0 + -2.5f*k1 + 2f*k2 + -0.5f*k3;
        c1 = -0.5f*k0 + 0f*k1 + 0.5f*k2 + 0f*k3;
        c0 = 0f*k0 + 1f*k1 + 0f*k2 + 0f*k3;

        return ((c3*t + c2)*t + c1)*t + c0;
    }
}