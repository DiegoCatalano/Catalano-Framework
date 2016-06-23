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
 * Brightness adjusting in RGB color space.
 * 
 * <p><li>Supported types: Grayscale, RGB.
 * <br><li>Coordinate System: Matrix.
 * 
 * @author Diego Catalano
 */
public class BrightnessCorrection implements IApplyInPlace{
    
    private LevelsLinear baseFilter = new LevelsLinear( );
    private int adjustValue;

    /**
     * Get Brightness adjust value, [-255, 255].
     * @return Brightness value.
     */
    public int getAdjustValue() {
        return adjustValue;
    }

    /**
     * Set Brightness adjust value, [-255, 255].
     * @param adjustValue Brightness value.
     */
    public void setAdjustValue(int adjustValue) {
        this.adjustValue = adjustValue = Math.max( -255, Math.min( 255, adjustValue ) );
        
        if ( adjustValue > 0 ){
            
            baseFilter.setInRed(new IntRange(0, 255 - adjustValue));
            baseFilter.setInGreen(new IntRange(0, 255 - adjustValue));
            baseFilter.setInBlue(new IntRange(0, 255 - adjustValue));
            baseFilter.setInGray(new IntRange(0, 255 - adjustValue));
            
            baseFilter.setOutRed(new IntRange(adjustValue, 255));
            baseFilter.setOutGreen(new IntRange(adjustValue, 255));
            baseFilter.setOutBlue(new IntRange(adjustValue, 255));
            baseFilter.setOutGray(new IntRange(adjustValue, 255));
        }
        else{
            baseFilter.setInRed(new IntRange(-adjustValue, 255));
            baseFilter.setInGreen(new IntRange(-adjustValue, 255));
            baseFilter.setInBlue(new IntRange(-adjustValue, 255));
            baseFilter.setInGray(new IntRange(-adjustValue, 255));
            
            baseFilter.setOutRed(new IntRange(0, 255 + adjustValue));
            baseFilter.setOutGreen(new IntRange(0, 255 + adjustValue));
            baseFilter.setOutBlue(new IntRange(0, 255 + adjustValue));
            baseFilter.setOutGray(new IntRange(0, 255 + adjustValue));
        }
    }

    /**
     * Initialize a new instance of the BrightnessCorrection class.
     */
    public BrightnessCorrection() {}

    /**
     * Initialize a new instance of the BrightnessCorrection class.
     * @param adjustValue Brightness adjust value.
     */
    public BrightnessCorrection(int adjustValue) {
        setAdjustValue(adjustValue);
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        baseFilter.applyInPlace(fastBitmap);
    }
}