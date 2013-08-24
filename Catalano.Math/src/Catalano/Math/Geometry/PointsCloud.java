/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Catalano.Math.Geometry;

import Catalano.Core.FloatPoint;
import Catalano.Core.IntPoint;
import java.util.ArrayList;

/**
 *
 * @author Diego
 */
public final class PointsCloud {

    private PointsCloud() {}
    
    /**
     * Shift cloud by adding specified value to all points in the collection.
     * @param cloud Collection of points to shift their coordinates.
     * @param shift Point to shift by.
     */
    public static void Shift( ArrayList<IntPoint> cloud, IntPoint shift ){
        for ( int i = 0, n = cloud.size(); i < n; i++ ){
            IntPoint p = cloud.get(i);
            p.Add(shift);
            cloud.set(i, p);
        }
    }
    
    /**
     * Get bounding rectangle of the specified list of points.
     * @param cloud Collection of points to get bounding rectangle for.
     * @return Bounding rectangle.
     */
    public static ArrayList<IntPoint> GetBoundingRectangle( ArrayList<IntPoint> cloud){
        
        ArrayList<IntPoint> bound = new ArrayList<IntPoint>();
        
        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;

        for ( IntPoint pt : cloud )
        {
            int x = pt.x;
            int y = pt.y;

            // check X coordinate
            if ( x < minX )
                minX = x;
            if ( x > maxX )
                maxX = x;

            // check Y coordinate
            if ( y < minY )
                minY = y;
            if ( y > maxY )
                maxY = y;
        }

        if ( minX > maxX ) // if no point appeared to set either minX or maxX
            throw new IllegalArgumentException( "List of points can not be empty." );

        IntPoint min = new IntPoint( minX, minY );
        IntPoint max = new IntPoint( maxX, maxY );
        
        bound.add(min);
        bound.add(max);
        
        return bound;
    }
    
    public static FloatPoint GetCenterOfGravity( ArrayList<IntPoint> cloud ){
        int numberOfPoints = 0;
        float xSum = 0, ySum = 0;

        for ( IntPoint pt : cloud )
        {
            xSum += pt.x;
            ySum += pt.y;
            numberOfPoints++;
        }

        xSum /= numberOfPoints;
        ySum /= numberOfPoints;

        return new FloatPoint( xSum, ySum );
    }
    
    public static FurthestPoint GetFurthestPoint( ArrayList<IntPoint> cloud, IntPoint referencePoint ){
        FurthestPoint furthestPoint = new FurthestPoint();
        float maxDistance = -1;

        int rx = referencePoint.x;
        int ry = referencePoint.y;

        for ( IntPoint point : cloud )
        {
            int dx = rx - point.x;
            int dy = ry - point.y;
            // we are not calculating square root for finding "real" distance,
            // since it is really not important for finding furthest point
            float distance = dx * dx + dy * dy;

            if ( distance > maxDistance )
            {
                maxDistance = distance;
                furthestPoint.x = point.x;
                furthestPoint.y = point.y;
                furthestPoint.distance = maxDistance;
            }
        }

        return furthestPoint;
    }
    
    public static FurthestPoint[] GetFurthestPointsFromLine( ArrayList<IntPoint> cloud, IntPoint linePoint1, IntPoint linePoint2 ){
        
        FurthestPoint[] furthest = new FurthestPoint[2];
        furthest[0] = new FurthestPoint(linePoint1);
        furthest[1] = new FurthestPoint(linePoint2);
        
        double distance1 = 0;
        double distance2 = 0;

        if ( linePoint2.x != linePoint1.x ){
            // line's equation y(x) = k * x + b
            float k = (float) ( linePoint2.y - linePoint1.y ) / ( linePoint2.x - linePoint1.x );
            float b = linePoint1.y - k * linePoint1.x;

            float div = (float) Math.sqrt( k * k + 1 );
            double distance = 0;

            for ( IntPoint point : cloud ){
                distance = ( k * (double)point.x + b - (double)point.y ) / div;

                if ( distance > distance1 ){
                    distance1 = distance;
                    furthest[0] = new FurthestPoint(point, (float)distance);
                }
                if ( distance < distance2 ){
                    distance2 = distance;
                    furthest[1] = new FurthestPoint(point, (float)distance);
                }
            }
        }
        else{
            int lineX = linePoint1.x;
            float distance = 0;

            for ( IntPoint point : cloud ){
                distance = lineX - point.x;

                if ( distance > distance1 ){
                    distance1 = distance;
                    furthest[0] = new FurthestPoint(point, distance);
                }
                if ( distance < distance2 ){
                    distance2 = distance;
                    furthest[1] = new FurthestPoint(point, distance);
                }
            }
        }

        //distance2 = -distance2;
        furthest[1].distance = (float)-distance2;
        
        return furthest;
    }
    
    public static IntPoint GetFurthestPointFromLine( ArrayList<IntPoint> cloud, IntPoint linePoint1, IntPoint linePoint2){
        IntPoint furthestPoint = linePoint1;
        float distance = 0;

        if ( linePoint2.x != linePoint1.x ){
            // line's equation y(x) = k * x + b
            float k = (float) ( linePoint2.y - linePoint1.y ) / ( linePoint2.x - linePoint1.x );
            float b = linePoint1.y - k * linePoint1.x;

            float div = (float) Math.sqrt( k * k + 1 );
            float pointDistance = 0;

            for ( IntPoint point : cloud ){
                pointDistance = Math.abs( ( k * point.x + b - point.y ) / div );

                if ( pointDistance > distance ){
                    distance = pointDistance;
                    furthestPoint = point;
                }
            }
        }
        else{
            int lineX = linePoint1.x;
            float pointDistance = 0;

            for ( IntPoint point : cloud ){
                distance = Math.abs( lineX - point.x );

                if ( pointDistance > distance ){
                    distance = pointDistance;
                    furthestPoint = point;
                }
            }
        }
        return furthestPoint;
    }
    
    private static float quadrilateralRelativeDistortionLimit = 0.1f;
    
    public static float getQuadrilateralRelativeDistortionLimit(){
        return quadrilateralRelativeDistortionLimit;
    }
    
    public static void setQuadrilateralRelativeDistortionLimit(float value){
        quadrilateralRelativeDistortionLimit = Math.max( 0.0f, Math.min( 0.25f, value ) );
    }
    
    public static ArrayList<IntPoint> FindQuadrilateralCorners( ArrayList<IntPoint> cloud ) {
        // quadrilateral's corners
        ArrayList<IntPoint> corners = new ArrayList<IntPoint>( );

        // get bounding rectangle of the points list
        IntPoint minXY, maxXY;
        ArrayList<IntPoint> bounds = PointsCloud.GetBoundingRectangle( cloud );
        minXY = bounds.get(0);
        maxXY = bounds.get(1);
        // get cloud's size
        IntPoint cloudSize = IntPoint.Subtract(maxXY, minXY);
        // calculate center point
        cloudSize.Divide(2);
        minXY.Add(cloudSize);
        IntPoint center = minXY;
        // acceptable deviation limit
        float distortionLimit = quadrilateralRelativeDistortionLimit * ( cloudSize.x + cloudSize.y ) / 2;

        // get the furthest point from (0,0)
        IntPoint point1 = PointsCloud.GetFurthestPoint( cloud, center ).toIntPoint();
        // get the furthest point from the first point
        IntPoint point2 = PointsCloud.GetFurthestPoint( cloud, point1 ).toIntPoint();

        corners.add( point1 );
        corners.add( point2 );

        // get two furthest points from line
        IntPoint point3, point4;
        float distance3, distance4;

        FurthestPoint[] fur = PointsCloud.GetFurthestPointsFromLine(cloud, point1, point2 );
        
        point3 = fur[0].toIntPoint();
        distance3 = fur[0].distance;
        
        point4 = fur[1].toIntPoint();
        distance4 = fur[1].distance;

        // ideally points 1 and 2 form a diagonal of the
        // quadrilateral area, and points 3 and 4 form another diagonal

        // but if one of the points (3 or 4) is very close to the line
        // connecting points 1 and 2, then it is one the same line ...
        // which means corner was not found.
        // in this case we deal with a trapezoid or triangle, where
        // (1-2) line is one of it sides.

        // another interesting case is when both points (3) and (4) are
        // very close the (1-2) line. in this case we may have just a flat
        // quadrilateral.

        if (
             ( ( distance3 >= distortionLimit ) && ( distance4 >= distortionLimit ) ) ||

             ( ( distance3 < distortionLimit ) && ( distance3 != 0 ) &&
               ( distance4 < distortionLimit ) && ( distance4 != 0 ) ) )
        {
            // don't add one of the corners, if the point is already in the corners list
            // (this may happen when both #3 and #4 points are very close to the line
            // connecting #1 and #2)
            if ( !corners.contains( point3 ) )
            {
                corners.add( point3 );
            }
            if ( !corners.contains( point4 ) )
            {
                corners.add( point4 );
            }
        }
        else
        {
            // it seems that we deal with kind of trapezoid,
            // where point 1 and 2 are on the same edge

            IntPoint tempPoint = ( distance3 > distance4 ) ? point3 : point4;

            // try to find 3rd point
            fur = PointsCloud.GetFurthestPointsFromLine( cloud, point1, tempPoint );
            
            point3 = fur[0].toIntPoint();
            distance3 = fur[0].distance;
            point4 = fur[1].toIntPoint();
            distance4 = fur[1].distance;

            boolean thirdPointIsFound = false;

            if ( ( distance3 >= distortionLimit ) && ( distance4 >= distortionLimit ) )
            {
                if ( point4.DistanceTo( point2 ) > point3.DistanceTo( point2 ) )
                    point3 = point4;

                thirdPointIsFound = true;
            }
            else
            {
                fur = PointsCloud.GetFurthestPointsFromLine( cloud, point2, tempPoint );
                
                point3 = fur[0].toIntPoint();
                distance3 = fur[0].distance;
                point4 = fur[1].toIntPoint();
                distance4 = fur[1].distance;

                if ( ( distance3 >= distortionLimit ) && ( distance4 >= distortionLimit ) )
                {
                    if ( point4.DistanceTo( point1 ) > point3.DistanceTo( point1 ) )
                        point3 = point4;

                    thirdPointIsFound = true;
                }
            }

            if ( !thirdPointIsFound )
            {
                // failed to find 3rd edge point, which is away enough from the temp point.
                // this means that the clound looks more like triangle
                corners.add( tempPoint );
            }
            else
            {
                corners.add( point3 );

                // try to find 4th point
                float tempDistance;

                fur = PointsCloud.GetFurthestPointsFromLine( cloud, point1, point3 );
                
                tempPoint = fur[0].toIntPoint();
                tempDistance = fur[0].distance;
                point4 = fur[1].toIntPoint();
                distance4 = fur[1].distance;

                if ( ( distance4 >= distortionLimit ) && ( tempDistance >= distortionLimit ) )
                {
                    if ( tempPoint.DistanceTo( point2 ) > point4.DistanceTo( point2 ) )
                        point4 = tempPoint;
                }
                else
                {
                    fur = PointsCloud.GetFurthestPointsFromLine( cloud, point2, point3 );
                        
                    tempPoint = fur[0].toIntPoint();
                    tempDistance = fur[0].distance;
                    point4 = fur[1].toIntPoint();
                    distance4 = fur[1].distance;

                    if ( ( tempPoint.DistanceTo( point1 ) > point4.DistanceTo( point1 ) ) &&
                         ( tempPoint != point2 ) && ( tempPoint != point3 ) )
                    {
                        point4 = tempPoint;
                    }
                }

                if ( ( point4 != point1 ) && ( point4 != point2 ) && ( point4 != point3 ) )
                    corners.add( point4 );
            }
        }

        // put the point with lowest X as the first
        for ( int i = 1, n = corners.size(); i < n; i++ )
        {
            if ( ( corners.get(i).x < corners.get(0).x ) ||
                 ( ( corners.get(i).x == corners.get(0).x ) && ( corners.get(i).y < corners.get(0).y ) ) )
            {
                IntPoint temp = corners.get(i);
                corners.set(i, corners.get(0));
                corners.set(0, temp);
            }
        }


        // sort other points in counter clockwise order
        float k1 = ( corners.get(1).x != corners.get(0).x ) ?
            ( (float) ( corners.get(1).y - corners.get(0).y ) / ( corners.get(1).x - corners.get(0).x ) ) :
            ( ( corners.get(1).y > corners.get(0).y ) ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY );

        float k2 = ( corners.get(2).x != corners.get(0).x ) ?
            ( (float) ( corners.get(2).y - corners.get(0).y ) / ( corners.get(2).x - corners.get(0).x ) ) :
            ( ( corners.get(2).y > corners.get(0).y ) ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY );

        if ( k2 < k1 )
        {
            IntPoint temp = corners.get(1);
            corners.set(1, corners.get(2));
            corners.set(2, temp);

            float tk = k1;
            k1 = k2;
            k2 = tk;
        }

        if ( corners.size() == 4 )
        {
            float k3 = ( corners.get(3).x != corners.get(0).x ) ?
                ( (float) ( corners.get(3).y - corners.get(0).y ) / ( corners.get(3).x - corners.get(0).x ) ) :
                ( ( corners.get(3).y > corners.get(0).y ) ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY );

            if ( k3 < k1 )
            {
                IntPoint temp = corners.get(1);
                corners.set(1, corners.get(3));
                corners.set(3, temp);

                float tk = k1;
                k1 = k3;
                k3 = tk;
            }
            if ( k3 < k2 )
            {
                IntPoint temp = corners.get(2);
                corners.set(2, corners.get(3));
                corners.set(3, temp);

                float tk = k2;
                k2 = k3;
                k3 = tk;
            }
        }

        return corners;
    }
        
    public static class FurthestPoint{
        public int x;
        public int y;
        public float distance;

        public FurthestPoint() {}

        public FurthestPoint(int x, int y, float distance) {
            this.x = x;
            this.y = y;
            this.distance = distance;
        }
        
        public FurthestPoint(IntPoint p){
            this.x = p.x;
            this.y = p.y;
        }
        
        public FurthestPoint(IntPoint p, float distance){
            this.x = p.x;
            this.y = p.y;
            this.distance = distance;
        }
        
        public IntPoint toIntPoint(){
            return new IntPoint(x,y);
        }
    }
}