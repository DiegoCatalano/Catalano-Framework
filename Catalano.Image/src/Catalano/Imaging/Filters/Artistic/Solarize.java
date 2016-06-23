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
import Catalano.Imaging.Filters.Invert;
import Catalano.Imaging.IApplyInPlace;

/**
 * Solarize filter.
 * @author Diego Catalano
 */
public class Solarize implements IApplyInPlace{
    
    private double p;
    
    /**
     * Get percentage.
     * @return Percentage.
     */
    public double getPercentage() {
        return p;
    }

    /**
     * Set percentage.
     * @param p Percentage [0..1].
     */
    public void setPercentage(double p) {
        this.p = Math.max(0, Math.min(1, p));
    }

    /**
     * Initialize a new instance of the Solarize class.
     */
    public Solarize() {}

    /**
     * Initialize a new instance of the Solarize class.
     * @param percentage Percentage [0..1].
     */
    public Solarize(double percentage) {
        setPercentage(percentage);
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        FastBitmap layerA = new FastBitmap(fastBitmap);
        FastBitmap layerB = new FastBitmap(fastBitmap);
        
        Invert i = new Invert();
        i.applyInPlace(layerA);
        
        Blend b = new Blend(layerA, Blend.Algorithm.Difference);
        b.applyInPlace(layerB);
        
        Opacity op = new Opacity(layerB);
        op.setPercentage(p);
        op.applyInPlace(fastBitmap);
        
    }
}