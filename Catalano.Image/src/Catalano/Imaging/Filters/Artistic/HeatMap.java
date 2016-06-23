// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
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
import Catalano.Imaging.Filters.Invert;
import Catalano.Imaging.IApplyInPlace;
import Catalano.Imaging.Tools.ImageStatistics;

/**
 * Heat Map.
 * @author Diego Catalano
 */
public class HeatMap implements IApplyInPlace{
    
    private boolean invert = false;

    /**
     * Verify if heat map is inverted.
     * @return True if is inverted, otherwise false.
     */
    public boolean isInvert() {
        return invert;
    }

    /**
     * Invert heat map.
     * @param invert Invert.
     */
    public void setInvert(boolean invert) {
        this.invert = invert;
    }

    /**
     * Initializes a new instance of the HeatMap class.
     */
    public HeatMap() {}
    
    /**
     * Initializes a new instance of the HeatMap class.
     * @param invert Invert heat map.
     */
    public HeatMap(boolean invert){
        this.invert = invert;
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        if (fastBitmap.isRGB()){
            fastBitmap.toGrayscale();
        }
        
        if (invert) new Invert().applyInPlace(fastBitmap);
        
        int size = fastBitmap.getWidth() * fastBitmap.getHeight();
        
        int min = ImageStatistics.Minimum(fastBitmap);
        int max = ImageStatistics.Maximum(fastBitmap);
        
        fastBitmap.toRGB();
        for (int i = 0; i < size; i++) {
            int[] rgb = GrayscaleToHeatMap(fastBitmap.getRed(i), min, max);
            fastBitmap.setRGB(i, rgb);
        }
    }
    
    /**
     * Convert Grayscale to Heat Map.
     * @param val Normalized gray value [0..1].
     * @param min Normalized minimum value [0..1].
     * @param max Normalized maximum value [0..1].
     * @return Heat map [0..255].
     */
    private int[] GrayscaleToHeatMap(double gray, double min, double max) {
        
        int r = 0;
        int g = 0;
        int b = 0;
        
        gray = (gray - min) / (max - min);
        
        if(gray <= 0.2) {
            b = (int)((gray / 0.2) * 255);
        } else if(gray >  0.2 &&  gray <= 0.7) {
            b = (int)((1.0 - ((gray - 0.2) / 0.5)) * 255);
        }
        
        if(gray >= 0.2 &&  gray <= 0.6) {
            g = (int)(((gray - 0.2) / 0.4) * 255);
        } else if(gray >  0.6 &&  gray <= 0.9) {
            g = (int)((1.0 - ((gray - 0.6) / 0.3)) * 255);
        }
        
        if(gray >= 0.5) {
            r = (int)(((gray - 0.5) / 0.5) * 255);
        }
        
        return new int[]{r, g, b};
    }
}