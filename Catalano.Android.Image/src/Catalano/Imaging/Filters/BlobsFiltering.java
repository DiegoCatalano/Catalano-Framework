// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2014
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
 * Eliminates blobs with certain area or size.
 * @author Diego Catalano
 */
public class BlobsFiltering implements IBaseInPlace{
    
    public static enum Filter {Area, Size};
    private Filter filter = Filter.Area;
    
    private int minArea = 0;
    private int maxArea = Integer.MAX_VALUE;
    
    private int minHeight = 0;
    private int maxHeight = Integer.MAX_VALUE;
    private int minWidth = 0;
    private int maxWidth = Integer.MAX_VALUE;

    /**
     * Get filtering type.
     * @return Filter.
     */
    public Filter getFilter() {
        return filter;
    }

    /**
     * Set filtering type.
     * @param filter Filter.
     */
    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    /**
     * Get Minimum width allowed.
     * @return Minimum width.
     */
    public int getMinWidth() {
        return minWidth;
    }

    /**
     * Set Minimum width allowed.
     * @param minWidth Minimum width.
     */
    public void setMinWidth(int minWidth) {
        this.minWidth = minWidth;
    }

    /**
     * Get Maximum width allowed.
     * @return Maximum width.
     */
    public int getMaxWidth() {
        return maxWidth;
    }

    /**
     * Set Maximum width allowed.
     * @param maxWidth Maximum width.
     */
    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }

    /**
     * Get Minimum height allowed.
     * @return Minimum height.
     */
    public int getMinHeight() {
        return minHeight;
    }

    /**
     * Set Minimum height allowed.
     * @param minHeight Minimum height.
     */
    public void setMinHeight(int minHeight) {
        this.minHeight = minHeight;
    }

    /**
     * Get Maximum height allowed.
     * @return Maximum height.
     */
    public int getMaxHeight() {
        return maxHeight;
    }

    /**
     * Set Maximum height allowed.
     * @param maxHeight Maximum height.
     */
    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
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
     * Initialize a new instance of the BlobsFiltering class.
     * @param minWidth Minimum allowed width.
     * @param maxWidth Maximum allowed width.
     * @param minHeight Minimum allowed height.
     * @param maxHeight Maximum allowed height.
     */
    public BlobsFiltering(int minWidth, int maxWidth, int minHeight, int maxHeight){
        this.minWidth = minWidth;
        this.maxWidth = maxWidth;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
        this.filter = Filter.Size;
    }
    
    @Override
    public void applyInPlace(FastBitmap fastBitmap){
        
        ArrayList<Blob> blobs = new BlobDetection().ProcessImage(fastBitmap);
        
        switch(filter){
            case Area:
                int area;
                for (int i = 0; i < blobs.size(); i++) {
                    area = blobs.get(i).getArea();
                    if ((area > minArea) && (area <= maxArea)) {
                        for (IntPoint p : blobs.get(i).getPoints()) {
                            fastBitmap.setGray(p.x, p.y, 0);
                        }
                    }
                }
            break;
            case Size:
                int blobWidth;
                int blobHeight;
                for (int i = 0; i < blobs.size(); i++) {
                    blobWidth = blobs.get(i).getWidth();
                    blobHeight = blobs.get(i).getHeight();
                    if ((blobWidth > minWidth) && (blobWidth <= maxWidth)) {
                        if ((blobHeight > minHeight) && (blobHeight <= maxHeight)){
                            for (IntPoint p : blobs.get(i).getPoints()) {
                                fastBitmap.setGray(p.x, p.y, 0);
                            }
                        }
                    }
                }
            break;
        }
    }
}