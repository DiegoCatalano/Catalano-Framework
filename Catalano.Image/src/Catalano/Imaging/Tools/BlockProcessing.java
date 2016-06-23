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

package Catalano.Imaging.Tools;

import Catalano.Core.ArraysUtil;
import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.Filters.Crop;
import java.util.ArrayList;
import java.util.List;

/**
 * Block processing.
 * Process specifics filters and compute function for every block in the image.
 * This process doesn't overlap the blocks.
 * 
 * @author Diego Catalano
 */
public class BlockProcessing {
    
    private int wBlock;
    private int hBlock;

    /**
     * Initializes a new instance of the BlockProcessing class.
     */
    public BlockProcessing() {
        this(6,6);
    }

    /**
     * Initializes a new instance of the BlockProcessing class.
     * @param wBlock Number of width blocks.
     * @param hBlock Number of height blocks.
     */
    public BlockProcessing(int wBlock, int hBlock) {
        this.wBlock = wBlock;
        this.hBlock = hBlock;
    }
    
    /**
     * Compute the block processing.
     * @param fastBitmap Image to be processed.
     * @param filters List of filters to be used.
     * @param function Function to compute the descriptors.
     * @return Concatenate descriptors.
     */
    public double[] Compute(FastBitmap fastBitmap, FiltersSequence filters, IAggregateVectors function){
        int width = fastBitmap.getWidth();
        int height = fastBitmap.getHeight();
        
        int wDiv = (int)Math.round((double)width / (double)wBlock) - 1;
        int hDiv = (int)Math.round((double)height / (double)hBlock) - 1;
        
        List<double[]> vectors = new ArrayList<double[]>();
        
        for (int i = 0; i < hBlock; i++) {
            for (int j = 0; j < wBlock; j++) {
                FastBitmap copy = getSubimage(fastBitmap, i*hDiv, j*wDiv, wDiv, hDiv);
                filters.applyInPlace(copy);
                vectors.add(function.Compute(copy));
            }
        }
        
        //Concatenate all the histograms
        return ArraysUtil.ConcatenateDouble(vectors);
    }

    /**
     * Compute the block processing.
     * @param fastBitmap Image to be processed.
     * @param function Function to compute the descriptors.
     * @return Concatenate descriptors.
     */
    public double[] Compute(FastBitmap fastBitmap, IAggregateVectors function) {
        int width = fastBitmap.getWidth();
        int height = fastBitmap.getHeight();
        
        int wDiv = (int)Math.round((double)width / (double)wBlock) - 1;
        int hDiv = (int)Math.round((double)height / (double)hBlock) - 1;
        
        List<double[]> vectors = new ArrayList<double[]>();
        
        for (int i = 0; i < hBlock; i++) {
            for (int j = 0; j < wBlock; j++) {
                FastBitmap copy = getSubimage(fastBitmap, i*hDiv, j*wDiv, wDiv, hDiv);
                vectors.add(function.Compute(copy));
            }
        }
        
        //Concatenate all the histograms
        return ArraysUtil.ConcatenateDouble(vectors);
    }
    
    private FastBitmap getSubimage(FastBitmap fastBitmap, int x, int y, int width, int height){
        FastBitmap copy = new FastBitmap(fastBitmap);
        
        Crop crop = new Crop(x, y, width, height);
        crop.ApplyInPlace(copy);
        
        return copy;
    }
    
}
