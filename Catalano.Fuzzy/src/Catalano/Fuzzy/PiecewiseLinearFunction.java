// Catalano Fuzzy Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2013
// diego.catalano at live.com
//
// Copyright © Andrew Kirillov, 2007-2008
// andrew.kirillov at gmail.com
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

package Catalano.Fuzzy;

import Catalano.Core.FloatPoint;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Membership function composed by several connected linear functions.
 * @author Diego Catalano
 */
public class PiecewiseLinearFunction implements IMembershipFunction{
    
    /**
     * Vector of (X,Y) coordinates for end/start of each line.
     */
    protected FloatPoint[] points;

    /**
     * Initializes a new instance of the <see cref="PiecewiseLinearFunction"/> class.
     * <para> This constructor must be used only by inherited classes to create the points vector after the instantiation. </para>
     */
    public PiecewiseLinearFunction() {
        points = null;
    }

    /**
     * Initializes a new instance of the <see cref="PiecewiseLinearFunction"/> class.
     * @param points Array of (X,Y) coordinates of each start/end of the lines.
     * 
     * <exception cref="ArgumentException">Points must be in crescent order on X axis.</exception>
     * <exception cref="ArgumentException">Y value of points must be in the range of [0, 1].</exception>
     */
    public PiecewiseLinearFunction(FloatPoint[] points) {
        this.points = points;
        
        // check if X points are in a sequence and if Y values are in [0..1] range
        for ( int i = 0, n = points.length; i < n; i++ ){
            if ( ( points[i].y < 0 ) || ( points[i].y > 1 ) )
                try {
                throw new Exception( "Y value of points must be in the range of [0, 1]." );
            } catch (Exception ex) {
                Logger.getLogger(PiecewiseLinearFunction.class.getName()).log(Level.SEVERE, null, ex);
            }

            if ( i == 0 )
                continue;

            if ( points[i - 1].x > points[i].x )
                try {
                throw new Exception( "Points must be in crescent order on X axis." );
            } catch (Exception ex) {
                Logger.getLogger(PiecewiseLinearFunction.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public float GetMembership(float x) {
        // no values belong to the fuzzy set, if there are no points in the piecewise function
        if ( points.length == 0 )
            return 0.0f;

        // if X value is less than the first point, so first point's Y will be returned as membership
        if ( x < points[0].x )
            return points[0].y;

        // looking for the line that contais the X value
        for ( int i = 1, n = points.length; i < n; i++ ){
            // the line with X value starts in points[i-1].X and ends at points[i].X
            if ( x < points[i].x ){
                // points to calculate line's equation
                float y1 = points[i].y;
                float y0 = points[i - 1].y;
                float x1 = points[i].x;
                float x0 = points[i - 1].x;
                // angular coefficient
                float m = ( y1 - y0 ) / ( x1 - x0 );
                // returning the membership - the Y value for this X
                return m * ( x - x0 ) + y0;
            }
        }

        // X value is more than last point, so last point Y will be returned as membership
        return points[points.length - 1].y;
    }

    @Override
    public float LeftLimit() {
        return points[0].x;
    }

    @Override
    public float RightLimit() {
        return points[points.length - 1].x;
    }
    
}
