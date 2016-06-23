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
import Catalano.Imaging.Tools.ColorConverter;

/**
 * Luminance and saturation linear correction.
 * 
 * The filter operates in HSL color space and provides with the facility of luminance and saturation
 * linear correction - mapping specified channels' input ranges to specified output ranges.
 * 
 * @author Diego Catalano
 */
public class HSLLinear implements IApplyInPlace{
    
    private FloatRange inLuminance   = new FloatRange( 0.0f, 1.0f );
    private FloatRange inSaturation  = new FloatRange( 0.0f, 1.0f );
    private FloatRange outLuminance  = new FloatRange( 0.0f, 1.0f );
    private FloatRange outSaturation = new FloatRange( 0.0f, 1.0f );

    /**
     * Get Luminance input range.
     * @return Luminance input range.
     */
    public FloatRange getInLuminance() {
        return inLuminance;
    }
    
    /**
     * Set Luminance input range.
     * @param inLuminance Luminance input range.
     */
    public void setInLuminance(FloatRange inLuminance) {
        this.inLuminance = inLuminance;
    }
    
    /**
     * Get Saturation input range.
     * @return Saturation input range.
     */
    public FloatRange getInSaturation() {
        return inSaturation;
    }

    /**
     * Set Saturation input range.
     * @param inSaturation Saturation input range.
     */
    public void setInSaturation(FloatRange inSaturation) {
        this.inSaturation = inSaturation;
    }

    /**
     * Get Luminance output range.
     * @return Luminance output range.
     */
    public FloatRange getOutLuminance() {
        return outLuminance;
    }

    /**
     * Set Luminance output range.
     * @param outLuminance Luminance output range.
     */
    public void setOutLuminance(FloatRange outLuminance) {
        this.outLuminance = outLuminance;
    }

    /**
     * Get Saturation output range.
     * @return Saturation output range.
     */
    public FloatRange getOutSaturation() {
        return outSaturation;
    }

    /**
     * Set Saturation output range.
     * @param outSaturation Saturation output range.
     */
    public void setOutSaturation(FloatRange outSaturation) {
        this.outSaturation = outSaturation;
    }

    /**
     * Initialize a new instance of the HSLLinear class.
     */
    public HSLLinear() {}

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        if (fastBitmap.isRGB()){
            
            float kl = 0, bl = 0;
            float ks = 0, bs = 0;
            
            // luminance line parameters
            if ( inLuminance.getMax() != inLuminance.getMin() )
            {
                kl = ( outLuminance.getMax() - outLuminance.getMin() ) / ( inLuminance.getMax() - inLuminance.getMin() );
                bl = outLuminance.getMin() - kl * inLuminance.getMin();
            }
            // saturation line parameters
            if ( inSaturation.getMax() != inSaturation.getMin() )
            {
                ks = ( outSaturation.getMax() - outSaturation.getMin() ) / ( inSaturation.getMax() - inSaturation.getMin() );
                bs = outSaturation.getMin() - ks * inSaturation.getMin();
            }
            
            int size = fastBitmap.getSize();
            for (int i = 0; i < size; i++) {
                int r = fastBitmap.getRed(i);
                int g = fastBitmap.getGreen(i);
                int b = fastBitmap.getBlue(i);

                double[] hsl = ColorConverter.RGBtoHSL(r, g, b);

                // do luminance correction
                if ( hsl[2] >= inLuminance.getMax() )
                    hsl[2] = outLuminance.getMax();
                else if ( hsl[2] <= inLuminance.getMin() )
                    hsl[2] = outLuminance.getMin();
                else
                    hsl[2] = kl * hsl[2] + bl;

                // do saturation correct correction
                if ( hsl[1] >= inSaturation.getMax() )
                    hsl[1] = outSaturation.getMax();
                else if ( hsl[1] <= inSaturation.getMin() )
                    hsl[1] = outSaturation.getMin();
                else
                    hsl[1] = ks * hsl[1] + bs;

                int[] rgb = ColorConverter.HSLtoRGB(hsl[0], hsl[1], hsl[2]);
                
                rgb[0] = fastBitmap.clampValues(rgb[0], 0, 255);
                rgb[1] = fastBitmap.clampValues(rgb[1], 0, 255);
                rgb[2] = fastBitmap.clampValues(rgb[2], 0, 255);
                
                int t1 = rgb[0];
                int t2 = rgb[1];
                int t3 = rgb[2];
                if(t1 > 255 || t1 < 0){
                    int stop = 0;
                }
                if(t2 > 255 || t2 < 0){
                    int stop = 0;
                }
                if(t3 > 255 || t3 < 0){
                    int stop = 0;
                }

                fastBitmap.setRGB(i, rgb);
            }
        }
    }
}