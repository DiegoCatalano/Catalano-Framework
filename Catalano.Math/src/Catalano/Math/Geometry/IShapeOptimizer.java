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
import java.util.ArrayList;

/**
 * Interface for shape optimizing algorithms.
 * 
 * <para>The shape optimizing algorithms can be useful in conjunction with such algorithms
 * like convex hull searching, which usually may provide many hull points, where some
 * of them are insignificant and could be removed.</para>
 * @author Diego Catalano
 */
public interface IShapeOptimizer {
    /**
     * Optimize specified shape.
     * @param shape Shape to be optimized.
     * @return Returns final optimized shape, which may have reduced amount of points.
     */
    ArrayList<IntPoint> OptimizeShape( ArrayList<IntPoint> shape );
}
