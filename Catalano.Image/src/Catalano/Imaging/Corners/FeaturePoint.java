// Catalano Imaging Library
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
package Catalano.Imaging.Corners;

import Catalano.Core.IntPoint;

/**
 * Feature Point class.
 * @author Diego Catalano
 */
public class FeaturePoint implements Comparable<FeaturePoint>{

    /**
     * X axis coordinate.
     */
    public int x;
    
    /**
     * Y axis coordinate.
     */
    public int y;
    
    /**
     * Score.
     */
    public int score;

    /**
     * Initializes a new instance of the FeaturePoint class.
     */
    public FeaturePoint() {}

    /**
     * Initializes a new instance of the FeaturePoint class.
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     */
    public FeaturePoint(int x, int y){
        this.x = x;
        this.y = y;
    }

    /**
     * Initializes a new instance of the FeaturePoint class.
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     * @param score Score.
     */
    public FeaturePoint(int x, int y, int score) {
        this.x = x;
        this.y = y;
        this.score = score;
    }

    /**
     * Convert to IntPoint.
     * @return IntPoint.
     */
    public IntPoint toIntPoint(){
        return new IntPoint(x, y);
    }

    @Override
    public int compareTo(FeaturePoint o) {
        if (o.score < this.score) return 1;
        else if (o.score == this.score) return 0;
        else return -1;
    }
}