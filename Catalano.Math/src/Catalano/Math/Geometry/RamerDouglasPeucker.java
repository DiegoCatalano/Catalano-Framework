// Catalano Math Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2018
// diego.catalano at live.com
//
// Copyright © Pier Lorenzo Paracchini, 2007-2008
// https://pparacch.github.io/
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
import Catalano.Math.Distances.Distance;
import java.util.ArrayList;
import java.util.List;

/**
 * Ramer-Douglas-Peucker.
 * 
 * Douglas–Peucker algorithm and iterative end-point fit algorithm, is an algorithm
 * that decimates a curve composed of line segments to a similar curve with fewer points.
 * 
 * @author Diego Catalano
 */
public class RamerDouglasPeucker implements IShapeOptimizer {
    
    private double distanceThreshold;

    /**
     * Get distance threshold.
     * @return Distance threshold.
     */
    public double getDistanceThreshold() {
        return distanceThreshold;
    }

    /**
     * Distance threshold.
     * @param distanceThreshold Distance threshold.
     */
    public void setDistanceThreshold(double distanceThreshold) {
        this.distanceThreshold = distanceThreshold;
    }

    /**
     * Initializes a new instance of the RamerDouglasPeucker class.
     */
    public RamerDouglasPeucker() {
        this(0.5);
    }

    /**
     * Initializes a new instance of the RamerDouglasPeucker class.
     * @param distanceThreshold Distance threshold.
     */
    public RamerDouglasPeucker(double distanceThreshold) {
        this.distanceThreshold = distanceThreshold;
    }

    @Override
    public List<IntPoint> OptimizeShape(List<IntPoint> shape) {
        List<IntPoint> simplifiedVertices = new ArrayList<IntPoint>();

        Double maxDistance = null;
        int maxDistancePointIdx = 0;

        int lastPointIdx = shape.size()-1;

        int currentIdx = 0;
        for (IntPoint aVertex : shape) {

            if (currentIdx != 0 && currentIdx != lastPointIdx) {

                Double distance = shortestDistanceToSegment(aVertex, shape.get(0), shape.get(lastPointIdx));

                if (maxDistance == null || distance > maxDistance) {
                    maxDistancePointIdx = currentIdx;
                    maxDistance = distance;
                }

            }
            currentIdx++;
        }

        if (maxDistance != null) {
            if (maxDistance > distanceThreshold) {
                List<IntPoint> sub = OptimizeShape(shape.subList(0, maxDistancePointIdx+1));
                List<IntPoint> sup = OptimizeShape(shape.subList(maxDistancePointIdx, lastPointIdx+1));

                simplifiedVertices.addAll(sub);
                simplifiedVertices.addAll(sup);

            } else {
                simplifiedVertices.add(shape.get(0));
                simplifiedVertices.add(shape.get(lastPointIdx));
            }
        }
        return simplifiedVertices;
    }
    
    private double shortestDistanceToSegment(IntPoint thePoint, IntPoint segmentPoint_A, IntPoint segmentPoint_B) {
        double area = calculateTriangleAreaGivenVertices(thePoint, segmentPoint_A, segmentPoint_B);
        double lengthSegment = Distance.Euclidean(segmentPoint_A, segmentPoint_B);
        return (2 * area) / lengthSegment;
    }

    private double calculateTriangleAreaGivenVertices(IntPoint a, IntPoint b, IntPoint c) {
        // http://www.mathopenref.com/coordtrianglearea.html
        double area = Math.abs(((a.x * (b.y - c.y)) + (b.x * (c.y - a.y)) + (c.x * (a.y - b.y))) / 2);
        return area;
    }
}