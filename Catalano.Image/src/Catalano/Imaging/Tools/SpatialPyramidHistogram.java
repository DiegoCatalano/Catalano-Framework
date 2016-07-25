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
import Catalano.Imaging.Texture.BinaryPattern.IBinaryPattern;
import java.util.ArrayList;
import java.util.List;

/**
 * Spatial Pyramid Histogram.
 * @author Diego Catalano
 */
public class SpatialPyramidHistogram {
    
    private int level;

    /**
     * Get level of the pyramid.
     * @return Level of the pyramid.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Set level of the pyramid.
     * @param level Level of the pyramid.
     */
    public void setLevel(int level) {
        this.level = Math.max(1, level);
    }

    /**
     * Initializes a new instance of the SpatialPyramidHistogram class.
     */
    public SpatialPyramidHistogram() {
        this(3);
    }

    /**
     * Initializes a new instance of the SpatialPyramidHistogram class.
     * @param level Level.
     */
    public SpatialPyramidHistogram(int level) {
        setLevel(level);
    }
    
    /**
     * Compute the Spatial Pyramid Histogram.
     * @param fastBitmap Image to be processed.
     * @param pattern Binary pattern.
     * @return Spatial Pyramid Histogram.
     */
    public int[] Compute(FastBitmap fastBitmap, IBinaryPattern pattern){
        
        List<int[]> histograms = new ArrayList<int[]>();
        int elem = 0;
        int size = 1;
        for (int i = 0; i < level; i++) {
            SpatialHistogram sh = new SpatialHistogram(size, size);
            histograms.add(sh.Compute(fastBitmap, pattern));
            elem += histograms.get(i).length;
            size *= 2;
        }
        
        //Concatenate the the histograms
        int[] all = new int[elem];
        int idx = 0;
        for (int i = 0; i < histograms.size(); i++) {
            int[] h = histograms.get(i);
            for (int j = 0; j < h.length; j++) {
                all[idx++] = h[j];
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
        
        List<double[]> lst = new ArrayList<double[]>();
        int size = 1;
        for (int i = 0; i < level; i++) {
            SpatialHistogram sh = new SpatialHistogram(size, size);
            lst.add(sh.Compute(fastBitmap, features));
            size *= 2;
        }
        
        //Concatenate the the histograms
        return ArraysUtil.ConcatenateDouble(lst);   
        
    }
}
