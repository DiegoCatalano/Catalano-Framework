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
import java.util.ArrayList;

/**
 * Convex Hull Defects Extractor.
 * @author Diego Catalano
 */
public class ConvexHullDefects {
    private double minimumDepth;

    /**
     * Initializes a new instance of the ConvexHullDefects class.
     * @param minimumDepth The minimum depth which characterizes a convexity defect.
     */
    public ConvexHullDefects(double minimumDepth) {
        this.minimumDepth = minimumDepth;
    }

    /**
     * Gets the minimum depth which characterizes a convexity defect.
     * @return Depth.
     */
    public double getMinimumDepth() {
        return minimumDepth;
    }

    /**
     * Sets the minimum depth which characterizes a convexity defect.
     * @param minimumDepth Minimum depth.
     */
    public void setMinimumDepth(double minimumDepth) {
        this.minimumDepth = minimumDepth;
    }
    
    /**
     * Finds the convexity defects in a contour given a convex hull.
     * @param contour The contour.
     * @param convexHull The convex hull of the contour.
     * @return A list of ConvexityDefects containing each of the defects found considering the convex hull of the contour.
     */
    public ArrayList<ConvexityDefect> FindDefects(ArrayList<IntPoint> contour, ArrayList<IntPoint> convexHull){
        try {
            if (contour.size() < 4) throw new Exception("Point sequence size should have at least 4 points.");
            if (convexHull.size() < 3) throw new Exception("Convex hull must have at least 3 points.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Find all convex hull points in the contour
        int[] indexes = new int[convexHull.size()];
        for (int i = 0, j = 0; i < contour.size(); i++){
            if (convexHull.contains(contour.get(i))){
                indexes[j++] = i;
            }
        }
        
        ArrayList<ConvexityDefect> defects = new ArrayList<ConvexityDefect>();
        
        // For each two consecutive points in the convex hull
        for (int i = 0; i < indexes.length - 1; i++){
            ConvexityDefect current = ExtractDefect(contour, indexes[i], indexes[i + 1]);

            if (current.getDepth() > minimumDepth)
            {
                defects.add(current);
            }
        }
        return defects;
    }
    
    private ConvexityDefect ExtractDefect(ArrayList<IntPoint> contour, int startIndex, int endIndex){
        
        IntPoint start = contour.get(startIndex);
        IntPoint end = contour.get(endIndex);
        
        Line line = Line.FromPoints(start, end);
        
        double maxDepth = 0;
        int maxIndex = 0;
        
        for (int i = startIndex; i < endIndex; i++){
            double d = line.DistanceToPoint(contour.get(i));

            if (d > maxDepth){
                maxDepth = d;
                maxIndex = i;
            }
        }
        return new ConvexityDefect(contour.get(maxIndex), startIndex, endIndex, maxDepth);
    }
}