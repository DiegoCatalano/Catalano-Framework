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
import Catalano.Imaging.Filters.GaussianNoise;
import Catalano.Imaging.IApplyInPlace;

/**
 * Film Grain filter.
 * @author Diego Catalano
 */
public class FilmGrain implements IApplyInPlace{
    
    private int grain = 127;
    private double stdDev = 8;

    /**
     * Get grain value.
     * @return Grain value.
     */
    public int getGrain() {
        return grain;
    }

    /**
     * Set grain value.
     * @param grain Grain value.
     */
    public void setGrain(int grain) {
        this.grain = grain;
    }

    /**
     * Get standart deviation.
     * @return Standart deviation.
     */
    public double getStdDev() {
        return stdDev;
    }

    /**
     * Set standart deviation.
     * @param stdDev Standart deviation.
     */
    public void setStdDev(double stdDev) {
        this.stdDev = stdDev;
    }

    /**
     * Initializes a new instance of the FilmGrain class.
     */
    public FilmGrain() {}

    /**
     * Initializes a new instance of the FilmGrain class.
     * @param grain Grain value [0..255].
     */
    public FilmGrain(int grain) {
        this.grain = grain;
    }
    
    /**
     * Initializes a new instance of the FilmGrain class.
     * @param grain Grain value [0..255].
     * @param stdDev Standart deviation.
     */
    public FilmGrain(int grain, double stdDev) {
        this.grain = grain;
        this.stdDev = stdDev;
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        if (fastBitmap.isRGB()){
            
            int width = fastBitmap.getWidth();
            int height = fastBitmap.getHeight();
            FastBitmap layerA = new FastBitmap(width, height, FastBitmap.ColorSpace.RGB);
            
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    layerA.setRGB(i, j, grain, grain, grain);
                }
            }
            
            GaussianNoise gn = new GaussianNoise(stdDev);
            gn.applyInPlace(layerA);
            
            Blend b = new Blend(layerA, Blend.Algorithm.Overlay);
            b.applyInPlace(fastBitmap);
            
        }
    }
}