// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2014
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

package Catalano.Imaging.Filters;

import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.IBaseInPlace;

/**
 * Fill holes in objects in binary image.
 * @author Diego Catalano
 */
public class FillHoles implements IBaseInPlace{
    private int minArea = 0; private int maxArea = 0;

    /**
     * Initialize a new instance of the FillHoles class.
     */
    public FillHoles() {
        
    }
    
    /**
     * Initialize a new instance of the FillHoles class.
     * @param minArea Minimum area of a hole to fill.
     * @param maxArea Maximum area of a hole to fill.
     */
    public FillHoles(int minArea, int maxArea) {
        this.maxArea = Math.max(0, maxArea);
        this.minArea = Math.max(0, minArea);
    }

    /**
     * Minimum area of a hole.
     * @return Area.
     */
    public int getMinArea() {
        return minArea;
    }

    /**
     * Minimum area of a hole.
     * @param minArea Area.
     */
    public void setMinArea(int minArea) {
        this.minArea = minArea;
    }

    /**
     * Maximum area of a hole.
     * @return Area.
     */
    public int getMaxArea() {
        return maxArea;
    }

    /**
     * Maximum area of a hole.
     * @param maxArea Area.
     */
    public void setMaxArea(int maxArea) {
        this.maxArea = maxArea;
    }
    
    @Override
    public void applyInPlace(FastBitmap fastBitmap){
        
        Invert inv = new Invert();
        inv.applyInPlace(fastBitmap);
        
        BlobsFiltering bf = new BlobsFiltering();
        bf.setMinArea(minArea);
        bf.setMaxArea(maxArea);
        bf.applyInPlace(fastBitmap);
        inv.applyInPlace(fastBitmap);
        
    }
}