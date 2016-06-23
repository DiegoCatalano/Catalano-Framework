// Catalano Graph Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
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

package Catalano.Graph.Pathfinding;

import Catalano.Core.IntPoint;
import java.util.ArrayList;

/**
 * Interface for graph search algorithms.
 * @author Diego Catalano
 */
public interface ISearch {
    
    /**
     * Find path.
     * @param startX Start X axis coordinate.
     * @param startY Start Y axis coordinate.
     * @param endX End X axis coordinate.
     * @param endY End Y axis coordinate.
     * @return List of points that contains the path.
     */
    public ArrayList<IntPoint> FindPath(int startX, int startY, int endX, int endY);
    
    /**
     * Find path.
     * @param start Start point.
     * @param end End point.
     * @return List of points that contains the path.
     */
    public ArrayList<IntPoint> FindPath(IntPoint start, IntPoint end);
}