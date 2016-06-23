// Catalano Math Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
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

package Catalano.Math.Geometry;

import Catalano.Core.IntPoint;
import java.util.ArrayList;
import java.util.Collections;


/**
 * Graham scan algorithm for finding convex hull.
 * @author Diego Catalano
 */
public class GrahamConvexHull {

    /**
     * Initializes a new instance of the <see cref="GrahamConvexHull"/> class.
     */
    public GrahamConvexHull() {
        
    }
    
    /**
     * Find convex hull for the given set of points.
     * @param points Set of points to search convex hull for.
     * @return Set of points, which form a convex hull for the given points.
     */
    public ArrayList<IntPoint> FindFull(ArrayList<IntPoint> points){
        
        ArrayList<PointToProcess> pointsToProcess = new ArrayList<PointToProcess>();
        
        for (IntPoint p : points) {
            pointsToProcess.add(new PointToProcess(p));
        }
        
        int firstCornerIndex = 0;
        PointToProcess firstCorner = pointsToProcess.get(0);
        
        for ( int i = 1, n = pointsToProcess.size(); i < n; i++ ){
            if ( ( pointsToProcess.get(i).x < firstCorner.x ) || ( ( pointsToProcess.get(i).x == firstCorner.x ) && ( pointsToProcess.get(i).y < firstCorner.y ) ) ){
                firstCorner = pointsToProcess.get(i);
                firstCornerIndex = i;
            }
        }
        
        // remove the just found point
        pointsToProcess.remove(firstCornerIndex);

        // find K (tangent of line's angle) and distance to the first corner
        for ( int i = 0, n = pointsToProcess.size(); i < n; i++ ){
            int dx = pointsToProcess.get(i).x - firstCorner.x;
            int dy = pointsToProcess.get(i).y - firstCorner.y;

            // don't need square root, since it is not important in our case
            pointsToProcess.get(i).distance = dx * dx + dy * dy;
            // tangent of lines angle
            pointsToProcess.get(i).k = ( dx == 0 ) ? Float.POSITIVE_INFINITY : (float) dy / dx;
        }
        
        // sort points by angle and distance
        Collections.sort(pointsToProcess);
        
        ArrayList<PointToProcess> convexHullTemp = new ArrayList<PointToProcess>();
        
        // add first corner, which is always on the hull
        convexHullTemp.add(firstCorner);
        // add another point, which forms a line with lowest slope
        convexHullTemp.add(pointsToProcess.get(0));
        pointsToProcess.remove(0);

        PointToProcess lastPoint = convexHullTemp.get(1);
        PointToProcess prevPoint = convexHullTemp.get(0);
        
        while (!pointsToProcess.isEmpty()){
            PointToProcess newPoint = pointsToProcess.get(0);

            // skip any point, which has the same slope as the last one or
            // has 0 distance to the first point
            if ( ( newPoint.k == lastPoint.k ) || ( newPoint.distance == 0 ) ){
                pointsToProcess.remove( 0 );
                continue;
            }

            // check if current point is on the left side from two last points
            if ( ( newPoint.x - prevPoint.x ) * ( lastPoint.y - newPoint.y ) - ( lastPoint.x - newPoint.x ) * ( newPoint.y - prevPoint.y ) < 0 ){
                // add the point to the hull
                convexHullTemp.add( newPoint );
                // and remove it from the list of points to process
                pointsToProcess.remove( 0 );

                prevPoint = lastPoint;
                lastPoint = newPoint;
            }
            else{
                // remove the last point from the hull
                convexHullTemp.remove( convexHullTemp.size() - 1 );

                lastPoint = prevPoint;
                prevPoint = convexHullTemp.get(convexHullTemp.size() - 2);
            }
        }
        
        // convert points back
        ArrayList<IntPoint> convexHull = new ArrayList<IntPoint>();
        
        for (PointToProcess p : convexHullTemp) {
            convexHull.add(p.toPoint());
        }

        return convexHull;
        
    }
    
}
class PointToProcess implements Comparable<PointToProcess> {
    public int x;
    public int y;
    public float k;
    public float distance;

    public PointToProcess(IntPoint p) {
        this.x = p.x;
        this.y = p.y;
        k = 0;
        distance = 0;
    }

    @Override
    public int compareTo(PointToProcess o) {
        if (this.k == o.k) {
            return 0;
        }
        else if ((this.k) > o.k) {
            return 1;
        }
        else {
            return -1;
        }
    }
    
    public IntPoint toPoint(){
        return new IntPoint(x,y);
    }
    
}