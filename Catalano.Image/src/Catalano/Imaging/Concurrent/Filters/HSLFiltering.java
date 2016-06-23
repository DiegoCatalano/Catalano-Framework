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

package Catalano.Imaging.Concurrent.Filters;

import Catalano.Core.FloatRange;
import Catalano.Core.IntRange;
import Catalano.Imaging.Concurrent.Share;
import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.IApplyInPlace;
import Catalano.Imaging.Tools.ColorConverter;

/**
 * Color filtering in HSL color space.
 * 
 * <para>The filter operates in <b>HSL</b> color space and filters pixels, which color is inside/outside of the specified HSL range -
 * it keeps pixels with colors inside/outside of the specified range and fills the rest with FillColor specified color.</para>
 * 
 * @author Diego Catalano
 */
public class HSLFiltering implements IApplyInPlace{
    
    private IntRange hue = new IntRange(0, 359);
    private FloatRange saturation = new FloatRange(0, 1);
    private FloatRange luminance = new FloatRange(0, 1);
    
    private int   fillH = 0;
    private float fillS = 0.0f;
    private float fillL = 0.0f;
    private boolean  fillOutsideRange = true;

    private boolean updateH = true;
    private boolean updateS = true;
    private boolean updateL = true;

    /**
     * Get Range of hue component, [0, 359].
     * @return Hue component.
     */
    public IntRange getHue() {
        return hue;
    }

    /**
     * Set Range of hue component, [0, 359].
     * @param hue Hue component.
     */
    public void setHue(IntRange hue) {
        this.hue = hue;
    }

    /**
     * Get Range of saturation component, [0, 1].
     * @return Saturation component.
     */
    public FloatRange getSaturation() {
        return saturation;
    }

    /**
     * Set Range of saturation component, [0, 1].
     * @param saturation Saturation component.
     */
    public void setSaturation(FloatRange saturation) {
        this.saturation = saturation;
    }

    /**
     * Get Range of luminance component, [0, 1].
     * @return Luminance component.
     */
    public FloatRange getLuminance() {
        return luminance;
    }

    /**
     * Set Range of luminance component, [0, 1].
     * @param luminance Luminance component.
     */
    public void setLuminance(FloatRange luminance) {
        this.luminance = luminance;
    }
    
    /**
     * Get fill color used to fill filtered pixels.
     * @return Hue color.
     */
    public float[] getFillColor(){
        return new float[] {fillH, fillS, fillL};
    }
    
    /**
     * Set fill color used to fill filtered pixels.
     * @param hue Hue, [0, 359].
     * @param saturation Saturation, [0, 1].
     * @param luminance Luminance, [0, 1].
     */
    public void setFillColor(int hue, float saturation, float luminance){
        this.fillH = hue;
        this.fillS = saturation;
        this.fillL = luminance;
    }

    /**
     * Verify if pixels should be filled inside or outside specified color range.
     * @return True if fill outside the range, otherwise false.
     */
    public boolean isFillOutsideRange() {
        return fillOutsideRange;
    }

    /**
     * Determines, if pixels should be filled inside or outside specified.
     * @param fillOutsideRange True if fill outside the range, otherwise false.
     */
    public void setFillOutsideRange(boolean fillOutsideRange) {
        this.fillOutsideRange = fillOutsideRange;
    }
    
    /**
     * Verify if hue value of filtered pixels should be updated.
     * @return True if should be updated, otherwise false.
     */
    public boolean isUpdatedHue(){
        return updateH;
    }
    
    /**
     * Determines, if hue value of filtered pixels should be updated.
     * @param update True if should be updated, otherwise false.
     */
    public void setUpdateHue(boolean update){
        this.updateH = update;
    }
    
    /**
     * Verify if saturation value of filtered pixels should be updated.
     * @return True if should be updated, otherwise false.
     */
    public boolean isUpdatedSaturation(){
        return updateS;
    }
    
    /**
     * Determines, if saturation value of filtered pixels should be updated.
     * @param update True if should be updated, otherwise false.
     */
    public void setUpdateSaturation(boolean update){
        this.updateS = update;
    }
    
    /**
     * Verify if luminance value of filtered pixels should be updated.
     * @return True if should be updated, otherwise false.
     */
    public boolean isUpdatedLuminance(){
        return updateL;
    }
    
    /**
     * Determines, if luminance value of filtered pixels should be updated.
     * @param update True if should be updated, otherwise false.
     */
    public void setUpdateLuminance(boolean update){
        this.updateL = update;
    }

    /**
     * Initializes a new instance of the HSLFiltering class.
     */
    public HSLFiltering() {}
    
    /**
     * Initializes a new instance of the HSLFiltering class.
     * @param hue Range of hue component.
     * @param saturation Range of saturation component.
     * @param luminance Range of luminance component.
     */
    public HSLFiltering(IntRange hue, FloatRange saturation, FloatRange luminance){
        this.hue = hue;
        this.saturation = saturation;
        this.luminance = luminance;
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        if (fastBitmap.isRGB()){
            Parallel(fastBitmap);
        }
        else{
            throw new IllegalArgumentException("HSL Filtering only works in RGB space color.");
        }
    }
    
    private void Parallel(FastBitmap fastBitmap){
        int cores = Runtime.getRuntime().availableProcessors();
        
        Thread[] t = new Thread[cores];
        int part = fastBitmap.getHeight() / cores;
        
        int startX = 0;
        for (int i = 0; i < cores; i++) {
            t[i] = new Thread(new Run(new Share(fastBitmap, startX, startX + part)));
            t[i].start();
            startX += part;
        }
        
        try {
            
            for (int i = 0; i < cores; i++) {
                t[i].join();
            }
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    private class Run implements Runnable {

        private Share share;
        
        public Run(Share obj) {
            this.share = obj;
        }

        @Override
        public void run() {
            
            boolean updated;
            for (int i = share.startX; i < share.endHeight; i++) {
                for (int j = 0; j < share.endWidth; j++) {
                    
                    updated = false;
                    int r = share.fastBitmap.getRed(i, j);
                    int g = share.fastBitmap.getGreen(i, j);
                    int b = share.fastBitmap.getBlue(i, j);
                    
                    double[] hsl = ColorConverter.RGBtoHSL(r, g, b);
                    
                    // check HSL values
                    if (
                        ( hsl[1] >= saturation.getMin() ) && ( hsl[1] <= saturation.getMax() ) &&
                        ( hsl[2] >= luminance.getMin() ) && ( hsl[2] <= luminance.getMax() ) &&
                        (
                        ( ( hue.getMin() < hue.getMax() ) && ( hsl[0] >= hue.getMin() ) && ( hsl[0] <= hue.getMax() ) ) ||
                        ( ( hue.getMin() > hue.getMax() ) && ( ( hsl[0] >= hue.getMin() ) || ( hsl[0] <= hue.getMax() ) ) )
                        )
                        )
                    {
                        if ( !fillOutsideRange )
                        {
                            if ( updateH ) hsl[0] = fillH;
                            if ( updateS ) hsl[1] = fillS;
                            if ( updateL ) hsl[2] = fillL;

                            updated = true;
                        }
                    }
                    else
                    {
                        if ( fillOutsideRange )
                        {
                            if ( updateH ) hsl[0] = fillH;
                            if ( updateS ) hsl[1] = fillS;
                            if ( updateL ) hsl[2] = fillL;

                            updated = true;
                        }
                    }

                    if ( updated )
                    {
                        // convert back to RGB
                        int[] rgb = ColorConverter.HSLtoRGB(hsl[0], hsl[1], hsl[2]);
                        share.fastBitmap.setRGB(i, j, rgb);
                    }
                }
            }
        }
    }
}