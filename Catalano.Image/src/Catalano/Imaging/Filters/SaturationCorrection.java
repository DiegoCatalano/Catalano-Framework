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

import Catalano.Core.FloatRange;
import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.IApplyInPlace;

/**
 * Saturation adjusting in HSL color space.
 * 
 * The filter operates in HSL color space and adjusts pixels' saturation value, increasing it or decreasing by specified percentage.
 * The filters is based on HSLLinear filter, passing work to it after recalculating saturation adjust value to input/output ranges of the HSLLinear filter.
 * 
 * @author Diego Catalano
 */
public class SaturationCorrection implements IApplyInPlace{
    
    private HSLLinear baseFilter = new HSLLinear( );
    private float adjustValue;	// [-1, 1]
    
    /**
     * Get Saturation adjust value.
     * @return Saturation adjust value.
     */
    public float getAdjustValue(){
        return adjustValue;
    }
    
    /**
     * Set Saturation adjust value.
     * @param value Saturation adjust value.
     */
    public void setAdjustValue(float value){
        adjustValue = Math.max( -1.0f, Math.min( 1.0f, value ) );
        // create saturation filter
        if ( adjustValue > 0 )
        {
            baseFilter.setInLuminance(new FloatRange( 0.0f, 1.0f - adjustValue ));
            baseFilter.setOutSaturation(new FloatRange( adjustValue, 1.0f ));
        }
        else
        {
            baseFilter.setInSaturation(new FloatRange( -adjustValue, 1.0f ));
            baseFilter.setOutSaturation(new FloatRange( 0.0f, 1.0f + adjustValue ));
        }
    }

    /**
     * Initialize a new instance of the SaturationCorrection class.
     */
    public SaturationCorrection() {
        setAdjustValue(0.1f);
    }
    
    /**
     * Initialize a new instance of the SaturationCorrection class.
     * @param value Saturation adjust value.
     */
    public SaturationCorrection(float value){
        setAdjustValue(value);
        
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        baseFilter.applyInPlace(fastBitmap);
    }
}