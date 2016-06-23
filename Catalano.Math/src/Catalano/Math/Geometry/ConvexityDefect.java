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

package Catalano.Math.Geometry;

import Catalano.Core.IntPoint;

/**
 * Convexity defect.
 * @author Diego Catalano
 */
public class ConvexityDefect {
    private int start, end;
    private IntPoint point;
    private double depth;

    /**
     * Initializes a new instance of the ConvexityDefect class.
     * @param point IntPoint.
     * @param start Start.
     * @param end End.
     * @param depth Depth.
     */
    public ConvexityDefect(IntPoint point, int start, int end, double depth) {
        this.point = point;
        this.start = start;
        this.end = end;
    }

    /**
     * Gets the depth of the defect (highest distance from the hull to any of the points in the contour). 
     * @return Depth.
     */
    public double getDepth() {
        return depth;
    }

    /**
     * Sets the depth of the defect (highest distance from the hull to any of the points in the contour). 
     * @param depth Depth.
     */
    public void setDepth(double depth) {
        this.depth = depth;
    }

    /**
     * Gets the ending index of the defect in the contour.
     * @return End.
     */
    public int getEnd() {
        return end;
    }

    /**
     * Sets the ending index of the defect in the contour.
     * @param end End.
     */
    public void setEnd(int end) {
        this.end = end;
    }

    /**
     * Gets the most distant point from the hull characterizing the defect. 
     * @return IntPoint.
     */
    public IntPoint getPoint() {
        return point;
    }

    /**
     * Sets the most distant point from the hull characterizing the defect. 
     * @param point IntPoint.
     */
    public void setPoint(IntPoint point) {
        this.point = point;
    }

    /**
     * Gets the starting index of the defect in the contour. 
     * @return Start.
     */
    public int getStart() {
        return start;
    }

    /**
     * Sets the starting index of the defect in the contour. 
     * @param start Start.
     */
    public void setStart(int start) {
        this.start = start;
    }
}