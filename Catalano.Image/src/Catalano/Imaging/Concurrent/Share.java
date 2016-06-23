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

package Catalano.Imaging.Concurrent;

import Catalano.Imaging.FastBitmap;

/**
 * Share data.
 * Share data among the threads for run the filters.
 * @author Diego Catalano
 */
public class Share {
    
    /**
     * Shared fast bitmap.
     */
    public FastBitmap fastBitmap;
    
    /**
     * Initial X axis coordinate position.
     */
    public int startX;
    
    /**
     * Initial Y axis coordinate position.
     */
    public int startY;
    
    /**
     * End of height.
     */
    public int endHeight;
    
    /**
     * End of width;
     */
    public int endWidth;
    
    /**
     * Specifies if is the last thread for to run.
     */
    public boolean lastThread = false;
    
    /**
     * Initializes a new instance of the Share class.
     * @param fastBitmap Image to be shared.
     * @param startX Initial X axis coordinate position.
     * @param endHeight End of height.
     */
    public Share(FastBitmap fastBitmap, int startX, int endHeight) {
        this.fastBitmap = fastBitmap;
        this.startX = startX;
        this.endHeight = endHeight;
    }
    
    /**
     * Initializes a new instance of the Share class.
     * @param fastBitmap Image to be shared.
     * @param startX Initial X axis coordinate position.
     * @param endHeight End of height.
     * @param lastThread Specifies if is the last thread for to run.
     */
    public Share(FastBitmap fastBitmap, int startX, int endHeight, boolean lastThread){
        this.fastBitmap = fastBitmap;
        this.startX = startX;
        this.endHeight = endHeight;
        this.lastThread = lastThread;
    }
    
    /**
     * Initializes a new instance of the Share class.
     * @param fastBitmap Image to be shared.
     * @param startX Initial X axis coordinate position.
     * @param endHeight End of height.
     * @param lastThead Specifies if is the last thread for to run.
     */
    public Share(FastBitmap fastBitmap, int startX, int startY, int endWidth, int endHeight){
        this.fastBitmap = fastBitmap;
        this.startX = startX;
        this.startY = startY;
        this.endHeight = endHeight;
        this.endWidth = endWidth;
    }
    
    /**
     * Initializes a new instance of the Share class.
     * @param fastBitmap Image to be shared.
     * @param startX Initial X axis coordinate position.
     * @param endHeight End of height.
     * @param lastThead Specifies if is the last thread for to run.
     */
    public Share(FastBitmap fastBitmap, int startX, int startY, int endWidth, int endHeight, boolean lastThread){
        this.fastBitmap = fastBitmap;
        this.startX = startX;
        this.startY = startY;
        this.endHeight = endHeight;
        this.endWidth = endWidth;
        this.lastThread = lastThread;
    }
    
}