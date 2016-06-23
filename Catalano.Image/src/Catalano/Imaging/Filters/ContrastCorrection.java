// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
// Copyright © Andrew Kirillov, 2007-2008
// andrew.kirillov at gmail.com
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

import Catalano.Core.IntRange;
import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.IApplyInPlace;

/**
 * Contrast adjusting in RGB color space.
 * 
 * <p><li>Supported types: Grayscale, RGB.
 * <br><li>Coordinate System: Matrix.
 * 
 * @author Diego Catalano
 */
public class ContrastCorrection implements IApplyInPlace{
    
    private LevelsLinear baseFilter = new LevelsLinear();
    private int factor = 10;

    /**
     * Get Contrast adjusting factor, [-127, 127].
     * @return Contrast factor.
     */
    public int getFactor() {
        return factor;
    }

    /**
     * Set Contrast adjusting factor, [-127, 127].
     * @param factor Contrast factor.
     */
    public void setFactor(int factor) {
        this.factor = factor = Math.max( -127, Math.min( 127, factor ) );
        
        if ( factor > 1 )
        {
            baseFilter.setInRed(new IntRange(factor, 255 - factor));
            baseFilter.setInGreen(new IntRange(factor, 255 - factor));
            baseFilter.setInBlue(new IntRange(factor, 255 - factor));
            baseFilter.setInGray(new IntRange(factor, 255 - factor));

            baseFilter.setOutRed(new IntRange(0,255));
            baseFilter.setOutGreen(new IntRange(0,255));
            baseFilter.setOutBlue(new IntRange(0,255));
            baseFilter.setOutGray(new IntRange(0,255));
        }
        else
        {
            baseFilter.setInRed(new IntRange(-factor, 255 + factor));
            baseFilter.setInGreen(new IntRange(-factor, 255 + factor));
            baseFilter.setInBlue(new IntRange(-factor, 255 + factor));
            baseFilter.setInGray(new IntRange(-factor, 255 + factor));

            baseFilter.setOutRed(new IntRange(0,255));
            baseFilter.setOutGreen(new IntRange(0,255));
            baseFilter.setOutBlue(new IntRange(0,255));
            baseFilter.setOutGray(new IntRange(0,255));
        }
    }
    
    /**
     * Initialize a new instance of the ContrastCorrection class.
     */
    public ContrastCorrection() {}

    /**
     * Initialize a new instance of the ContrastCorrection class.
     * @param factor Contrast factor.
     */
    public ContrastCorrection(int factor) {
        setFactor(factor);
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        baseFilter.applyInPlace(fastBitmap);
    }
    
}
