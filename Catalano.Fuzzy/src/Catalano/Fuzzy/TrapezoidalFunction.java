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

/**
 * Membership function in the shape of a trapezoid. Can be a half trapzoid if the left or the right side is missing.
 * @author Diego Catalano
 */
public class TrapezoidalFunction extends PiecewiseLinearFunction{
    
    /**
     * Enumeration used to create trapezoidal membership functions with half trapezoids.
     */
    public enum EdgeType {

        /**
         * The fuzzy side of the trapezoid is at the left side.
         */
        Left,
        /**
         * The fuzzy side of the trapezoid is at the right side.
         */
        Right
    };

    /**
     * A private constructor used only to reuse code inside of this default constructor.
     * @param size Size of points vector to create. This size depends of the shape of the trapezoid.
     */
    private TrapezoidalFunction(int size) {
        points = new FloatPoint[size];
    }

    /**
     * Initializes a new instance of the TrapezoidalFunction class.
     * @param m1 X value where the degree of membership starts to raise.
     * @param m2 X value where the degree of membership reaches the maximum value.
     * @param m3 X value where the degree of membership starts to fall.
     * @param m4 X value where the degree of membership reaches the minimum value.
     * @param max The maximum value that the membership will reach, [0, 1].
     * @param min The minimum value that the membership will reach, [0, 1].
     */
    public TrapezoidalFunction(float m1, float m2, float m3, float m4, float max, float min) {
        this(4);
        points[0] = new FloatPoint(m1, min);
        points[1] = new FloatPoint(m2, max);
        points[2] = new FloatPoint(m3, max);
        points[3] = new FloatPoint(m4, min);
    }
    
    /**
     * Initializes a new instance of the TrapezoidalFunction class.
     * @param m1 X value where the degree of membership starts to raise.
     * @param m2 X value where the degree of membership reaches the maximum value.
     * @param m3 X value where the degree of membership starts to fall.
     * @param m4 X value where the degree of membership reaches the minimum value.
     */
    public TrapezoidalFunction(float m1, float m2, float m3, float m4){
        this(m1, m2, m3, m4, 1.0f, 0.0f);
    }
    
    /**
     * Initializes a new instance of the TrapezoidalFunction class.
     * <br > With three points the shape is known as triangular fuzzy number or just fuzzy number (/\).
     * @param m1 X value where the degree of membership starts to raise.
     * @param m2 X value where the degree of membership reaches the maximum value and starts to fall.
     * @param m3 X value where the degree of membership reaches the minimum value.
     * @param max The maximum value that the membership will reach, [0, 1].
     * @param min The minimum value that the membership will reach, [0, 1].
     */
    public TrapezoidalFunction(float m1, float m2, float m3, float max, float min){
        this(3);
        points[0] = new FloatPoint(m1, min);
        points[1] = new FloatPoint(m2, max);
        points[2] = new FloatPoint(m3, min);
    }
    
    /**
     * Initializes a new instance of the TrapezoidalFunction class.
     * <br /> With three points the shape is known as triangular fuzzy number or just fuzzy number (/\).
     * @param m1 X value where the degree of membership starts to raise.
     * @param m2 X value where the degree of membership reaches the maximum value and starts to fall.
     * @param m3 X value where the degree of membership reaches the minimum value.
     * <para>
     * Maximum membership value is set to <b>1.0</b> and the minimum is set to <b>0.0</b>.
     * </para>
     */
    public TrapezoidalFunction(float m1, float m2, float m3){
        this(m1, m2, m3, 1.0f, 0.0f);
    }
    
    /**
     * Initializes a new instance of the TrapezoidalFunction class.
     * <br /> With two points and an edge this shape can be a left fuzzy number (/) or a right fuzzy number (\).
     * @param m1 Edge = Left: X value where the degree of membership starts to raise. <br /> Edge = Right: X value where the function starts, with maximum degree of membership.
     * @param m2 Edge = Left: X value where the degree of membership reaches the maximum. <br /> Edge = Right: X value where the degree of membership reaches minimum value.
     * @param max The maximum value that the membership will reach, [0, 1].
     * @param min The minimum value that the membership will reach, [0, 1].
     * @param edge Trapezoid's EdgeType.
     */
    public TrapezoidalFunction(float m1, float m2, float max, float min, EdgeType edge){
        this(2);
        if ( edge == EdgeType.Left )
        {
            points[0] = new FloatPoint(m1, min);
            points[1] = new FloatPoint(m2, max);
        }
        else
        {
            points[0] = new FloatPoint(m1, max);
            points[1] = new FloatPoint(m2, min);
        }
    }
    
    /**
     * Initializes a new instance of the TrapezoidalFunction class.
     * 
     * <br /> With three points and an edge this shape can be a left fuzzy number (/--) or a right fuzzy number (--\).
     * @param m1 Edge = Left: X value where the degree of membership starts to raise. <br /> Edge = Right: X value where the function starts, with maximum degree of membership.
     * @param m2 Edge = Left: X value where the degree of membership reaches the maximum. <br /> Edge = Right: X value where the degree of membership reaches minimum value.
     * @param edge Trapezoid's EdgeType.
     */
    public TrapezoidalFunction(float m1, float m2, EdgeType edge){
        this(m1, m2, 1.0f, 0.0f, edge);
    }
}