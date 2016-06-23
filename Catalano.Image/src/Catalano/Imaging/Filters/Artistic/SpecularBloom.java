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

package Catalano.Imaging.Filters.Artistic;

import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.Filters.GaussianBoxBlur;
import Catalano.Imaging.Filters.OtsuThreshold;
import Catalano.Imaging.Filters.RosinThreshold;
import Catalano.Imaging.Filters.SISThreshold;
import Catalano.Imaging.Filters.Threshold;
import Catalano.Imaging.IApplyInPlace;

/**
 * Specular Bloom effect.
 * @author Diego Catalano
 */
public class SpecularBloom implements IApplyInPlace{
    
    public static enum AdaptiveThreshold {Otsu, Rosin, Sis};
    private AdaptiveThreshold adaptive = AdaptiveThreshold.Rosin;
    private int threshold = 180;
    private int radius = 20;
    private boolean auto = true;

    /**
     * Get Threshold
     * @return Threshold.
     */
    public int getThreshold() {
        return threshold;
    }

    /**
     * Set Threshold.
     * @param threshold Threshold.
     */
    public void setThreshold(int threshold) {
        this.threshold = Math.max(1, Math.min(255, threshold));
    }

    /**
     * Get Radius.
     * @return Radius.
     */
    public double getRadius() {
        return radius;
    }

    /**
     * Set Radius.
     * @param radius Radius.
     */
    public void setRadius(int radius) {
        this.radius = Math.max(1, radius);
    }

    /**
     * Initialize a new instance of the SpecularBloom class.
     */
    public SpecularBloom() {}
    
    /**
     * Initialize a new instance of the SpecularBloom class.
     * @param threshold Threshold.
     * @param radius Radius.
     */
    public SpecularBloom(int threshold, int radius){
        this.threshold = threshold;
        this.radius = radius;
        this.auto = false;
    }
    
    /**
     * Initialize a new instance of the SpecularBloom class.
     * @param threshold Adaptive Threshold.
     * @param radius Radius.
     */
    public SpecularBloom(AdaptiveThreshold threshold, int radius){
        this.adaptive = threshold;
        this.radius = radius;
        this.auto = true;
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        FastBitmap layerA = new FastBitmap(fastBitmap);
        layerA.toGrayscale();
        
        switch(adaptive){
            case Otsu:
                OtsuThreshold o = new OtsuThreshold();
                o.applyInPlace(layerA);
            break;
            case Rosin:
                RosinThreshold r = new RosinThreshold();
                r.applyInPlace(layerA);
            break;
            case Sis:
                SISThreshold s = new SISThreshold();
                s.applyInPlace(layerA);
            break;
        }
        
        if (!auto){
            Threshold t = new Threshold(threshold);
            t.applyInPlace(layerA);
        }
        
        layerA.toRGB();
        
        GaussianBoxBlur fgb = new GaussianBoxBlur(radius);
        fgb.applyInPlace(layerA);
        
        Blend b = new Blend(layerA, Blend.Algorithm.Screen);
        b.applyInPlace(fastBitmap);
        
    }
}