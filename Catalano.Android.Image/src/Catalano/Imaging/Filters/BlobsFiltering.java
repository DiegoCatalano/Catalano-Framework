// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2013
// diego.catalano at live.com
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

import Catalano.Core.IntPoint;
import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.IBaseInPlace;
import Catalano.Imaging.Tools.Blob;
import Catalano.Imaging.Tools.BlobDetection;
import java.util.ArrayList;

/**
 * Eliminates blobs with certain area.
 * @author Diego Catalano
 */
public class BlobsFiltering implements IBaseInPlace{
    private int minArea = 0,maxArea = Integer.MAX_VALUE;
    
    /**
     * Initialize a new instance of the BlobsFiltering class.
     */
    public BlobsFiltering() {}
    
    /**
     * Initialize a new instance of the BlobsFiltering class.
     * @param minArea Minimum area.
     * @param maxArea Maximum area.
     */
    public BlobsFiltering(int minArea, int maxArea) {
        this.minArea = Math.max(0, minArea);
        this.maxArea = Math.max(0, maxArea);
    }

    /**
     * Maximum area allowed for eliminate.
     * @return Maximum area.
     */
    public int getMaxArea() {
        return maxArea;
    }

    /**
     * Maximum area allowed for eliminate.
     * @param maxArea Maximum area.
     */
    public void setMaxArea(int maxArea) {
        this.maxArea = Math.max(0, maxArea);
    }

    /**
     * Minimum area allowed for eliminate.
     * @return Minimum area.
     */
    public int getMinArea() {
        return minArea;
    }

    /**
     * Minimum area allowed for eliminate.
     * @param minArea Minimum area.
     */
    public void setMinArea(int minArea) {
        this.minArea = Math.max(0, minArea);
    }
    
    @Override
    public void applyInPlace(FastBitmap fastBitmap){
        
        ArrayList<Blob> blobs = new BlobDetection().ProcessImage(fastBitmap);
        
        int area;
        for (int i = 0; i < blobs.size(); i++) {
            area = blobs.get(i).getArea();
            if ((area > minArea) && (area <= maxArea)) {
                for (IntPoint p : blobs.get(i).getPoints()) {
                    fastBitmap.setGray(p.x, p.y, 0);
                }
            }
        }
    }
}
