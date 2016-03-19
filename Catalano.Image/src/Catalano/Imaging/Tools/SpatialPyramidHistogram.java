/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Catalano.Imaging.Tools;

import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.Texture.BinaryPattern.IBinaryPattern;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Diego
 */
public class SpatialPyramidHistogram {
    
    private int level;

    public SpatialPyramidHistogram() {
        this(3);
    }

    public SpatialPyramidHistogram(int level) {
        this.level = level;
    }
    
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
}
