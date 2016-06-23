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
 * Color filtering in YCbCr color space.
 * 
 * <para>The filter operates in <b>YCbCr</b> color space and filters pixels, which color is inside/outside of the specified YCbCr range -
 * it keeps pixels with colors inside/outside of the specified range and fills the rest with FillColor specified color.</para>
 * 
 * @author Diego Catalano
 */
public class YCbCrFiltering implements IApplyInPlace{
    
    private FloatRange yRange = new FloatRange(0, 1);
    private FloatRange cbRange = new FloatRange(-0.5f, 0.5f);
    private FloatRange crRange = new FloatRange(-0.5f, 0.5f);
    
    private float fillY = 0;
    private float fillCb = 0.0f;
    private float fillCr = 0.0f;
    private boolean  fillOutsideRange = true;

    private boolean updateY = true;
    private boolean updateCb = true;
    private boolean updateCr = true;

    /**
     * Get Range of Y component, [0, 1].
     * @return Y component.
     */
    public FloatRange getY() {
        return yRange;
    }

    /**
     * Set Range of Y component, [0, 1].
     * @param yRange Y component.
     */
    public void setHue(FloatRange y) {
        this.yRange = y;
    }

    /**
     * Get Range of cb component, [-0.5, 0.5].
     * @return Cb component.
     */
    public FloatRange getCb() {
        return cbRange;
    }

    /**
     * Set Range of cb component, [-0.5, 0.5].
     * @param cb Cb component.
     */
    public void setCb(FloatRange cb) {
        this.cbRange = cb;
    }

    /**
     * Get Range of cr component, [-0.5, 0.5].
     * @return Cr component.
     */
    public FloatRange getCr() {
        return crRange;
    }

    /**
     * Set Range of cr component, [-0.5, 0.5].
     * @param cr Cr component.
     */
    public void setCr(FloatRange cr) {
        this.crRange = cr;
    }
    
    /**
     * Get fill color used to fill filtered pixels.
     * @return YCbCr color.
     */
    public float[] getFillColor(){
        return new float[] {fillY, fillCb, fillCr};
    }
    
    /**
     * Set fill color used to fill filtered pixels.
     * @param y Y, [0, 1].
     * @param cb Cb, [-0.5, 0.5].
     * @param cr Cr, [-0.5, 0.5].
     */
    public void setFillColor(float y, float cb, float cr){
        this.fillY = y;
        this.fillCb = cb;
        this.fillCr = cr;
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
     * Verify if y value of filtered pixels should be updated.
     * @return True if should be updated, otherwise false.
     */
    public boolean isUpdatedY(){
        return updateY;
    }
    
    /**
     * Determines, if y value of filtered pixels should be updated.
     * @param update True if should be updated, otherwise false.
     */
    public void setUpdateY(boolean update){
        this.updateY = update;
    }
    
    /**
     * Verify if cb value of filtered pixels should be updated.
     * @return True if should be updated, otherwise false.
     */
    public boolean isUpdatedCb(){
        return updateCb;
    }
    
    /**
     * Determines, if cb value of filtered pixels should be updated.
     * @param update True if should be updated, otherwise false.
     */
    public void setUpdateCb(boolean update){
        this.updateCb = update;
    }
    
    /**
     * Verify if cr value of filtered pixels should be updated.
     * @return True if should be updated, otherwise false.
     */
    public boolean isUpdatedCr(){
        return updateCr;
    }
    
    /**
     * Determines, if cr value of filtered pixels should be updated.
     * @param update True if should be updated, otherwise false.
     */
    public void setUpdateCr(boolean update){
        this.updateCr = update;
    }

    /**
     * Initializes a new instance of the YCbCrFiltering class.
     */
    public YCbCrFiltering() {}
    
    /**
     * Initializes a new instance of the YCbCrFiltering class.
     * @param y Range of y component.
     * @param cb Range of cb component.
     * @param cr Range of cr component.
     */
    public YCbCrFiltering(FloatRange y, FloatRange cb, FloatRange cr){
        this.yRange = y;
        this.cbRange = cb;
        this.crRange = cr;
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        if (fastBitmap.isRGB()){
            
            int size = fastBitmap.getSize();
            
            boolean updated;
            for (int i = 0; i < size; i++) {
                    
                updated = false;
                int r = fastBitmap.getRed(i);
                int g = fastBitmap.getGreen(i);
                int b = fastBitmap.getBlue(i);

                // convert to YCbCr
                double[] ycbcr = ColorConverter.RGBtoYCbCr(r, g, b, ColorConverter.YCbCrColorSpace.ITU_BT_601);

                // check YCbCr values
                if (
                    ( ycbcr[0] >= yRange.getMin() )   && ( ycbcr[0] <= yRange.getMax() ) &&
                    ( ycbcr[1] >= cbRange.getMin() ) && ( ycbcr[1] <= cbRange.getMax() ) &&
                    ( ycbcr[2] >= crRange.getMin() ) && ( ycbcr[2] <= crRange.getMax() )
                    )
                {
                    if ( !fillOutsideRange )
                    {
                        if ( updateY ) ycbcr[0]   = fillY;
                        if ( updateCb ) ycbcr[1] = fillCb;
                        if ( updateCr ) ycbcr[2] = fillCr;

                        updated = true;
                    }
                }
                else
                {
                    if ( fillOutsideRange )
                    {
                        if ( updateY ) ycbcr[0]   = fillY;
                        if ( updateCb ) ycbcr[1] = fillCb;
                        if ( updateCr ) ycbcr[2] = fillCr;

                        updated = true;
                    }
                }

                if ( updated )
                {
                    // convert back to RGB
                    int[] rgb = ColorConverter.YCbCrtoRGB(ycbcr[0], ycbcr[1], ycbcr[2], ColorConverter.YCbCrColorSpace.ITU_BT_601);
                    fastBitmap.setRGB(i, rgb);
                }
            }
        }
        else{
            throw new IllegalArgumentException("HSL Filtering only works in RGB images.");
        }
    }
}