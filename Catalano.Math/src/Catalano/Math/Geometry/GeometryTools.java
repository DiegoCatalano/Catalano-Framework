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

import Catalano.Core.IntPoint;

/**
 *
 * @author Diego Catalano
 */
public class GeometryTools {
    public static float GetAngleBetweenVectors( IntPoint startPoint, IntPoint vector1end, IntPoint vector2end ){
        float x1 = vector1end.x - startPoint.x;
        float y1 = vector1end.y - startPoint.y;

        float x2 = vector2end.x - startPoint.x;
        float y2 = vector2end.y - startPoint.y;

        return (float) ( Math.acos( ( x1 * x2 + y1 * y2 ) / ( Math.sqrt( x1 * x1 + y1 * y1 ) * Math.sqrt( x2 * x2 + y2 * y2 ) ) ) * 180.0 / Math.PI );
    }
}
