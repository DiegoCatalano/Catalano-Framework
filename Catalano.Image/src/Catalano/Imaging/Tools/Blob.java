// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalan at yahoo.com.br
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
import Catalano.Imaging.Shapes.IntRectangle;
import java.util.ArrayList;

/**
 * Blob object.
 * @author Diego Catalano
 */
public class Blob {
    private int id;
    private int area;
    private IntPoint center;
    private ArrayList<IntPoint> points;
    private int width;
    private int height;
    private IntRectangle rectangle;

    /**
     * Initialize a new instance of the Blob class.
     */
    public Blob() {}

    /**
     * Initialize a new instance of the Blob class.
     * @param id ID blob.
     * @param area Area.
     * @param center Center.
     * @param points List of points.
     * @param rectangle Bounding box rectangle.
     */
    public Blob(int id, int area, IntPoint center,ArrayList<IntPoint> points, IntRectangle rectangle) {
        this.id = id;
        this.area = area;
        this.center = center;
        this.points = points;
        this.width = rectangle.width;
        this.height = rectangle.height;
        this.rectangle = rectangle;
    }

    /**
     * Get ID.
     * @return ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Get area.
     * @return area.
     */
    public int getArea() {
        return area;
    }

    /**
     * Get center.
     * @return Center.
     */
    public IntPoint getCenter() {
        return center;
    }

    /**
     * Get height.
     * @return Height.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Get Width.
     * @return Width.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Get Bounding box in contour of blob.
     * @return IntRectangle.
     */
    public IntRectangle getBoundingBox() {
        return rectangle;
    }

    /**
     * Get points.
     * @return List of points.
     */
    public ArrayList<IntPoint> getPoints() {
        return points;
    }
}