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
import Catalano.Imaging.Texture.BinaryPattern.IBinaryPattern;
import java.util.ArrayList;
import java.util.List;

/**
 * Spatial Histogram.
 * @author Diego Catalano
 */
public class SpatialHistogram {
    
    private int wBlock;
    private int hBlock;

    /**
     * Initializes a new instance of the SpatialHistogram class.
     */
    public SpatialHistogram() {
        this(6,6);
    }

    /**
     * Initializes a new instance of the SpatialHistogram class.
     * @param wBlock Number of width blocks.
     * @param hBlock Number of height blocks.
     */
    public SpatialHistogram(int wBlock, int hBlock) {
        this.wBlock = wBlock;
        this.hBlock = hBlock;
    }
    
    /**
     * Compute the spatial histogram.
     * @param fastBitmap Image to be processed.
     * @param pattern Binary pattern.
     * @return Spatial Histogram.
     */
    public int[] Compute(FastBitmap fastBitmap, IBinaryPattern pattern){
        
        int width = fastBitmap.getWidth();
        int height = fastBitmap.getHeight();
        
        int wDiv = (int)Math.round((double)width / (double)wBlock) - 1;
        int hDiv = (int)Math.round((double)height / (double)hBlock) - 1;
        
        ImageHistogram[] hist = new ImageHistogram[wBlock * hBlock];
        int idx = 0;
        
        for (int i = 0; i < hBlock; i++) {
            for (int j = 0; j < wBlock; j++) {
                FastBitmap copy = getSubimage(fastBitmap, i*hDiv, j*wDiv, wDiv, hDiv);
                hist[idx++] = pattern.ComputeFeatures(copy);
            }
        }
        
        //Concatenate all the histograms
        int[] all = new int[hist.length * hist[0].getValues().length];
        
        idx = 0;
        for (int i = 0; i < hist.length; i++) {
            int[] values = hist[i].getValues();
            for (int j = 0; j < values.length; j++) {
                all[idx++] = values[j];
            }
        }
        
        return all;
        
    }
    
    /**
     * Compute the spatial histogram.
     * @param fastBitmap Image to be processed.
     * @param features Aggregate vectors.
     * @return Spatial features.
     */
    public double[] Compute(FastBitmap fastBitmap, IAggregateVectors features){
        
        int width = fastBitmap.getWidth();
        int height = fastBitmap.getHeight();
        
        int wDiv = (int)Math.round((double)width / (double)wBlock) - 1;
        int hDiv = (int)Math.round((double)height / (double)hBlock) - 1;
        
        List<double[]> lst = new ArrayList<double[]>();
        
        for (int i = 0; i < hBlock; i++) {
            for (int j = 0; j < wBlock; j++) {
                FastBitmap copy = getSubimage(fastBitmap, i*hDiv, j*wDiv, wDiv, hDiv);
                lst.add(features.Compute(copy));
            }
        }
        
        //Concatenate all the histograms
        return ArraysUtil.ConcatenateDouble(lst);
        
    }
    
    private FastBitmap getSubimage(FastBitmap fastBitmap, int x, int y, int width, int height){
        FastBitmap copy = new FastBitmap(fastBitmap);
        
        Crop crop = new Crop(x, y, width, height);
        crop.ApplyInPlace(copy);
        
        return copy;
    }
    
}