// Catalano Math Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
// Copyright © Andrew Kirillov, 2005-2009
// andrew.kirillov@aforgenet.com
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

package Catalano.Math.Geometry;

import Catalano.Core.FloatPoint;
import Catalano.Core.IntPoint;

/**
 *
 * @author Diego Catalano
 */
public class Line {
    private float k,b;

    public Line(IntPoint start, IntPoint end) {
        if (start.equals(end)) {
            try {
                throw new Exception("Start point of the line cannot be the same as its end point.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        float d = end.x - start.x;
        d = d == 0 ? Float.POSITIVE_INFINITY : d;
        
        k = ( end.y - start.y ) / d;
        b = Float.isInfinite(k) ? start.x : start.y - k * start.x;
    }

    public Line(float slope, float intercept) {
        this.k = slope;
        this.b = intercept;
    }
    
    private Line( float radius, float theta, boolean unused ){

        theta *= (float) ( Math.PI / 180 );

        float sine = (float) Math.sin( theta ), cosine = (float) Math.cos( theta );
        FloatPoint pt1 = new FloatPoint( radius * cosine, radius * sine );

        // -1/tan, to get the slope of the line, and not the slope of the normal
        k = -cosine / sine;

        if ( !Float.isInfinite( k ) )
        {
            b = pt1.y - k * pt1.x;
        }
        else
        {
            b = Math.abs( radius );
        }
    }
    
    private Line( IntPoint point, float theta ){
        theta *= (float) ( Math.PI / 180 );

        k = (float) ( -1.0f / Math.tan( theta ) );

        if ( !Float.isInfinite( k ) )
        {
            b = point.y - k * point.x;
        }
        else
        {
            b = point.x;
        }
    }
    
    public boolean isVertical(){
        return Float.isInfinite(k);
    }
    
    public boolean isHorizontal(){
        return k == 0;
    }
    
    public float getSlope(){
        return k;
    }
    
    public float getIntercept(){
        return b;
    }
    
    public static Line FromPoints(IntPoint p1, IntPoint p2){
        return new Line(p1, p2);
    }
    
    public static Line FromSlopeIntercept(float slope, float intercept){
        return new Line(slope, intercept);
    }
    
    public static Line FromRTheta(float radius, float theta){
        return new Line(radius, theta, false);
    }
    
    public static Line FromPointTheta(IntPoint p, float theta){
        return new Line(p, theta);
    }
    
    public float GetAngleBetweenLines( Line secondLine ){
        float k2 = secondLine.k;

        boolean isVertical1 = isVertical();
        boolean isVertical2 = secondLine.isVertical();

        // check if lines are parallel
        if ( ( k == k2 ) || ( isVertical1 && isVertical2 ) )
            return 0;

        float angle = 0;

        if ( ( !isVertical1 ) && ( !isVertical2 ) ){
            float tanPhi = ( ( k2 > k ) ? ( k2 - k ) : ( k - k2 ) ) / ( 1 + k * k2 );
            angle = (float) Math.atan( tanPhi );
        }
        else{
            // one of the lines is parallel to Y axis

            if ( isVertical1 ){
                angle = (float) ( Math.PI / 2 - Math.atan( k2 ) * Math.signum( k2 ) );
            }
            else{
                angle = (float) ( Math.PI / 2 - Math.atan( k ) * Math.signum( k ) );
            }
        }

        // convert radians to degrees
        angle *= (float) ( 180.0 / Math.PI );

        if ( angle < 0 ){
            angle = -angle;
        }
        return angle;
    }
    
    public float DistanceToPoint( IntPoint point ){
        float distance;

        if ( !isVertical() )
        {
            float div = (float) Math.sqrt( k * k + 1 );
            distance = Math.abs( ( k * point.x + b - point.y ) / div );
        }
        else
        {
            distance = Math.abs( b - point.x );
        }

        return distance;
    }
}
