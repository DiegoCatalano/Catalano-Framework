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

/**
 * Shape optimizer, which removes obtuse angles (close to flat) from a shape.
 * @author Diego Catalano
 */
public class FlatAnglesOptimizer implements IShapeOptimizer{
    private float maxAngleToKeep = 160;

    /**
     * Initializes a new instance of the <see cref="FlatAnglesOptimizer"/> class.
     * <para>Default value is set to <b>160</b>.
     */
    public FlatAnglesOptimizer() {
        
    }
    
    /**
     * Initializes a new instance of the <see cref="FlatAnglesOptimizer"/> class.
     * <para>Default value is set to <b>160</b>.
     * @param maxAngleToKeep Maximum acceptable angle between two edges of a shape (see <see cref="MaxAngleToKeep"/>).
     */
    public FlatAnglesOptimizer(float maxAngleToKeep) {
        this.maxAngleToKeep = maxAngleToKeep;
    }

    /**
     * Maximum angle between adjacent edges to keep in a shape, [140, 180].
     * @return Angle.
     */
    public float getMaxAngleToKeep() {
        return maxAngleToKeep;
    }

    /**
     * Maximum angle between adjacent edges to keep in a shape, [140, 180].
     * @param maxAngleToKeep
     */
    public void setMaxAngleToKeep(float maxAngleToKeep) {
        this.maxAngleToKeep = Math.min(180, Math.max(140, maxAngleToKeep));
    }

    @Override
    public ArrayList<IntPoint> OptimizeShape(ArrayList<IntPoint> shape) {
            // optimized shape
            ArrayList<IntPoint> optimizedShape = new ArrayList<IntPoint>( );

            if ( shape.size() <= 3 )
            {
                // do nothing if shape has 3 points or less
                optimizedShape.addAll(shape);
            }
            else
            {
                float angle = 0;

                // add first 2 points to the new shape
                optimizedShape.add( shape.get(0) );
                optimizedShape.add( shape.get(1) );
                int pointsInOptimizedHull = 2;

                for ( int i = 2, n = shape.size(); i < n; i++ ){
                    // add new point
                    optimizedShape.add(shape.get(i));
                    pointsInOptimizedHull++;

                    // get angle between 2 vectors, which start from the next to last point
                    angle = GeometryTools.GetAngleBetweenVectors( optimizedShape.get(pointsInOptimizedHull - 2),
                        optimizedShape.get(pointsInOptimizedHull - 3), optimizedShape.get(pointsInOptimizedHull - 1) );

                    if ( ( angle > maxAngleToKeep ) &&
                         ( ( pointsInOptimizedHull > 3 ) || ( i < n - 1 ) ) )
                    {
                        // remove the next to last point
                        optimizedShape.remove(pointsInOptimizedHull - 2);
                        pointsInOptimizedHull--;
                    }
                }

                if ( pointsInOptimizedHull > 3 ){
                    // check the last point
                    angle = GeometryTools.GetAngleBetweenVectors( optimizedShape.get(pointsInOptimizedHull - 1),
                        optimizedShape.get(pointsInOptimizedHull - 2), optimizedShape.get(0) );

                    if ( angle > maxAngleToKeep )
                    {
                        optimizedShape.remove( pointsInOptimizedHull - 1 );
                        pointsInOptimizedHull--;
                    }

                    if ( pointsInOptimizedHull > 3 )
                    {
                        // check the first point
                        angle = GeometryTools.GetAngleBetweenVectors( optimizedShape.get(0),
                            optimizedShape.get(pointsInOptimizedHull - 1), optimizedShape.get(1) );

                        if ( angle > maxAngleToKeep )
                        {
                            optimizedShape.remove( 0 );
                        }
                    }
                }
            }
            return optimizedShape;
    }
}