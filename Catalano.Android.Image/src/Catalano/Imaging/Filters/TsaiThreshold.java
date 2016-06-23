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
import Catalano.Imaging.Tools.ImageStatistics;

/**
 * Tsai Threshold.
 * @author Diego Catalano
 */
public class TsaiThreshold implements IApplyInPlace{
    
    private boolean invert;

    /**
     * Check if the threshold is inverted.
     * @return True if the threshold is inverted, otherwise false.
     */
    public boolean isInvert() {
        return invert;
    }

    /**
     * Invert threshold.
     * @param invert True if need to invert the threshold.
     */
    public void setInvert(boolean invert) {
        this.invert = invert;
    }

    /**
     * Initializes a new instance of the TsaiThreshold class.
     */
    public TsaiThreshold() {
        this(false);
    }

    /**
     * Initializes a new instance of the TsaiThreshold class.
     * @param invert True if need to invert the threshold.
     */
    public TsaiThreshold(boolean invert) {
        this.invert = invert;
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        if(fastBitmap.isGrayscale()){
            int value = CalculateThreshold(fastBitmap);
            Threshold t = new Threshold(value, invert);
            t.applyInPlace(fastBitmap);
        }
        else{
            throw new IllegalArgumentException("Tsai threshold only works in grayscale images.");
        }
    }
    
    /**
     * Calculate the bilevel threshold value.
     * @param fastBitmap Grayscale image.
     * @return Tsai threshold value.
     */
    public int CalculateThreshold(FastBitmap fastBitmap){
        ImageStatistics stat = new ImageStatistics(fastBitmap);
        double[] hist = stat.getHistogramGray().Normalize();
        
        //First three moments
        double m1 = 0;
        double m2 = 0;
        double m3 = 0;
        for (int i = 0; i < hist.length; i++) {
            m1 = m1 + i * hist[i];
            m2 = m2 + i * i * hist[i];
            m3 = m3 + i * i * i * hist[i];
        }
        
        double cd = m2 - m1 * m1;
        double c0 = (-m2 * m2 + m1 * m3) / cd;
        double c1 = (-m3 + m2 * m1) / cd;
        double z0 = 0.5 * (-c1 - Math.sqrt(c1 * c1 - 4.0 * c0));
        double z1 = 0.5 * (-c1 + Math.sqrt(c1 * c1 - 4.0 * c0));

        double p0 = (z1 - m1) / (z1 - z0);
        
        double sum = 0;
        for (int i = 0; i < hist.length; i++) {
            sum += hist[i];
            if(sum > p0)
                return i;
        }
        
        return 255;
        
    }
    
}