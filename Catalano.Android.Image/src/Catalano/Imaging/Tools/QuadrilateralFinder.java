// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2018
// diego.catalano at live.com
//
// Copyright © Andrew Kirillov, 2007-2013
// andrew.kirillov@gmail.com
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

package Catalano.Imaging.Tools;

import Catalano.Core.IntPoint;
import Catalano.Imaging.FastBitmap;
import Catalano.Math.Geometry.PointsCloud;
import java.util.ArrayList;
import java.util.List;

/**
 * Searching of quadrilateral/triangle corners.
 * @author Diego Catalano
 */
public class QuadrilateralFinder {

    /**
     * Initialize a new instance of the QuadrilateralFinder class.
     */
    public QuadrilateralFinder() {}
    
    /**
     * Find corners of quadrilateral/triangular area in the specified image.
     * @param fastBitmap Image to be processed.
     * @return List of points.
     */
    public List<IntPoint> ProcessImage(FastBitmap fastBitmap){
        
        if(fastBitmap.isGrayscale()){
            
            List<IntPoint> points = new ArrayList<IntPoint>();
            
            int width = fastBitmap.getWidth();
            int height = fastBitmap.getHeight();
            
            boolean lineIsEmpty;
            
            // for each row
            for (int y = 0; y < height; y++)
            {
                lineIsEmpty = true;

                // scan from left to right
                for (int x = 0; x < width; x++)
                {
                    if (fastBitmap.getGray(y, x) != 0)
                    {
                        points.add(new IntPoint(x, y));
                        lineIsEmpty = false;
                        break;
                    }
                }
                if (!lineIsEmpty)
                {
                    // scan from right to left
                    for (int x = width - 1; x >= 0; x--)
                    {
                        if (fastBitmap.getGray(y, x) != 0)
                        {
                            points.add(new IntPoint(x, y));
                            break;
                        }
                    }
                }
            }
            
            points = PointsCloud.FindQuadrilateralCorners(points);
            
            if(fastBitmap.getCoordinateSystem() == FastBitmap.CoordinateSystem.Matrix){
                for (IntPoint p : points) {
                    p.Swap();
                }
            }
            
            return points;
            
        }
        else{
            throw new IllegalArgumentException("Only works in grayscale images.");
        }
    }
}
