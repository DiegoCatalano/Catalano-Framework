// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
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

import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.IApplyInPlace;

/**
 * Resize image using interpolation algorithms.
 * @author Diego Catalano
 */
public class Resize implements IApplyInPlace{
    
    
    /**
     * Interpolation algorithm.
     */
    public static enum Algorithm{
        /**
         * Bilinear.
         */
        BILINEAR,
        /**
         * Bicubic.
         */
        BICUBIC,
        /**
         * Nearest Neighbor.
         */
        NEAREST_NEIGHBOR};
    
    private Algorithm algorithm;
    private int newWidth, newHeight;

    /**
     * Initialize a new instance of the Resize class.
     * @param newWidth Width of the new resized image.
     * @param newHeight Height of the new resized image.
     */
    public Resize(int newWidth, int newHeight) {
        this.newWidth = newWidth;
        this.newHeight = newHeight;
        this.algorithm = Algorithm.NEAREST_NEIGHBOR;
    }
    
    /**
     * Initialize a new instance of the Resize class.
     * @param newWidth Width of the new resized image.
     * @param newHeight Height of the new resized image.
     * @param algorithm Interpolation algorithm.
     */
    public Resize(int newWidth, int newHeight, Algorithm algorithm) {
        this.newWidth = newWidth;
        this.newHeight = newHeight;
        this.algorithm = algorithm;
    }

     /**
     * Get Height of the new resized image.
     * @return New height.
     */
    public int getNewHeight() {
        return newHeight;
    }

     /**
     * Set Height of the new resized image.
     * @param newHeight New height.
     */
    public void setNewHeight(int newHeight) {
        this.newHeight = newHeight;
    }

     /**
     * Get Width of the new resized image.
     * @return New width.
     */
    public int getNewWidth() {
        return newWidth;
    }

     /**
     * Set Width of the new resized image.
     * @param newWidth New width.
     */
    public void setNewWidth(int newWidth) {
        this.newWidth = newWidth;
    }
    
    /**
     * Set Size of the new resized image.
     * @param newWidth Width of the new resized image.
     * @param newHeight Height of the new resized image.
     */
    public void setNewSize(int newWidth, int newHeight){
        this.newWidth = newWidth;
        this.newHeight = newHeight;
    }
    
    @Override
    public void applyInPlace(FastBitmap fastBitmap){
        
        switch(algorithm){
            case BILINEAR:
                ResizeBilinear rBilinear = new ResizeBilinear(newWidth, newHeight);
                rBilinear.applyInPlace(fastBitmap);
                break;
            case BICUBIC:
                ResizeBicubic rBicubic = new ResizeBicubic(newWidth, newHeight);
                rBicubic.applyInPlace(fastBitmap);
                break;
            case NEAREST_NEIGHBOR:
                ResizeNearestNeighbor rNearest = new ResizeNearestNeighbor(newWidth, newHeight);
                rNearest.applyInPlace(fastBitmap);
                break;
        }
        
    }
}