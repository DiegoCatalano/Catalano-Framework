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
import java.util.List;

/**
 * Curve representation.
 * @author Diego Catalano
 */
public class Curve {
    
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
     * 
     * @return 
     */
    public float[] getY() {
        return y;
    }
    
    /**
     * Initialize a new instance of the Curve class.
     */
    public Curve() {
        x = new float[] { 0, 1 };
        y = new float[] { 0, 1 };
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
     * Add colection of points.
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
        x = new float[] { 0, 1 };
        y = new float[] { 0, 1 };
    }

    /**
     * Sort points.
     */
    public void sortPoints() {
        int numKnots = x.length;
        for (int i = 1; i < numKnots-1; i++) {
            for (int j = 1; j < i; j++) {
                if (x[i] < x[j]) {
                        float t = x[i];
                        x[i] = x[j];
                        x[j] = t;
                        t = y[i];
                        y[i] = y[j];
                        y[j] = t;
                }
            }
        }
    }
}