/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Catalano.Vision.SpatioTemporal;

import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.Filters.Mean;
import Catalano.Vision.ITemporal;
import java.util.List;

/**
 *
 * @author Diego
 */
public class MeanBackgroundDetector implements ITemporal{
    
    private int radius;

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = Math.max(1, radius);
    }

    public MeanBackgroundDetector() {}

    public MeanBackgroundDetector(int radius) {
        setRadius(radius);
    }

    @Override
    public FastBitmap Process(List<FastBitmap> sequenceImage) {
        
        Mean m = new Mean(radius);
        for (FastBitmap fb : sequenceImage) {
            m.applyInPlace(fb);
        }
        
        Catalano.Vision.Temporal.MeanBackgroundDetector mbd = new Catalano.Vision.Temporal.MeanBackgroundDetector();
        
        return mbd.Process(sequenceImage);
    }
    
    
    
}
