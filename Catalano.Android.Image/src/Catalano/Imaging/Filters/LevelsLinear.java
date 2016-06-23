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
 * Linear correction of RGB channels.
 * <p>The filter performs linear correction of RGB channels by mapping specified channels' input ranges to output ranges.</p>
 * 
 * <p><li>Supported types: Grayscale, RGB.
 * <br><li>Coordinate System: Matrix.
 * 
 * @author Diego Catalano
 */
public class LevelsLinear implements IApplyInPlace{
    
    private IntRange inRed = new IntRange(0, 255);
    private IntRange inGreen = new IntRange(0, 255);
    private IntRange inBlue = new IntRange(0, 255);
    private IntRange inGray = new IntRange(0, 255);
    
    private IntRange outRed = new IntRange(0, 255);
    private IntRange outGreen = new IntRange(0, 255);
    private IntRange outBlue = new IntRange(0, 255);
    private IntRange outGray = new IntRange(0, 255);
    
    private int[] mapRed = new int[256];
    private int[] mapGreen = new int[256];
    private int[] mapBlue = new int[256];
    private int[] mapGray = new int[256];

    /**
     * Initialize a new instance of the LevelsLinear class.
     */
    public LevelsLinear() {}

    /**
     * Get blue component's input range.
     * @return Range.
     */
    public IntRange getInBlue() {
        return inBlue;
    }

    /**
     * Set blue component's input range.
     * @param inBlue Range.
     */
    public void setInBlue(IntRange inBlue) {
        this.inBlue = inBlue;
        CalculateMap(inBlue, outBlue, mapBlue);
    }

    /**
     * Get gray component's input range.
     * @return Range.
     */
    public IntRange getInGray() {
        return inGray;
    }

    /**
     * Set gray component's input range.
     * @param inGray Range.
     */
    public void setInGray(IntRange inGray) {
        this.inGray = inGray;
        CalculateMap(inGray, outGray, mapGray);
    }

    /**
     * Get green component's input range.
     * @return Range.
     */
    public IntRange getInGreen() {
        return inGreen;
    }

    /**
     * Set green component's input range.
     * @param inGreen Range.
     */
    public void setInGreen(IntRange inGreen) {
        this.inGreen = inGreen;
        CalculateMap(inGreen, outGreen, mapGreen);
    }

    /**
     * Get red component's input range.
     * @return Range.
     */
    public IntRange getInRed() {
        return inRed;
    }

    /**
     * Get red component's input range.
     * @param inRed Range.
     */
    public void setInRed(IntRange inRed) {
        this.inRed = inRed;
        CalculateMap(inRed, outRed, mapRed);
    }

    /**
     * Get blue component's output range.
     * @return Range.
     */
    public IntRange getOutBlue() {
        return outBlue;
    }

    /**
     * Set blue component's output range.
     * @param outBlue Range.
     */
    public void setOutBlue(IntRange outBlue) {
        this.outBlue = outBlue;
        CalculateMap(inBlue, outBlue, mapBlue);
    }

    /**
     * Get gray component's output range.
     * @return Range.
     */
    public IntRange getOutGray() {
        return outGray;
    }

    /**
     * Set gray component's output range.
     * @param outGray Range.
     */
    public void setOutGray(IntRange outGray) {
        this.outGray = outGray;
        CalculateMap(inGray, outGray, mapGray);
    }

    /**
     * Get green component's output range.
     * @return Range.
     */
    public IntRange getOutGreen() {
        return outGreen;
    }

    /**
     * Set blue component's output range.
     * @param outGreen Range.
     */
    public void setOutGreen(IntRange outGreen) {
        this.outGreen = outGreen;
        CalculateMap(inGreen, outGreen, mapGreen);
    }

    /**
     * Get red component's output range.
     * @return Range.
     */
    public IntRange getOutRed() {
        return outRed;
    }

    /**
     * Set red component's output range.
     * @param outRed Range.
     */
    public void setOutRed(IntRange outRed) {
        this.outRed = outRed;
        CalculateMap(inRed, outRed, mapRed);
    }
    
    /**
     * Set RGB input range.
     * @param inRGB Range.
     */
    public void setInRGB(IntRange inRGB){
        this.inRed = inRGB;
        this.inGreen = inRGB;
        this.inBlue = inRGB;
        
        CalculateMap(inRGB, outRed, mapRed);
        CalculateMap(inRGB, outGreen, mapGreen);
        CalculateMap(inRGB, outBlue, mapBlue);
    }
    
    /**
     * Set RGB output range.
     * @param outRGB Range.
     */
    public void setOutRGB(IntRange outRGB){
        this.outRed = outRGB;
        this.outGreen = outRGB;
        this.outBlue = outRGB;
        
        CalculateMap(inRed, outRGB, mapRed);
        CalculateMap(inGreen, outRGB, mapGreen);
        CalculateMap(inBlue, outRGB, mapBlue);
    }
    
    @Override
    public void applyInPlace(FastBitmap fastBitmap){
        
        int size = fastBitmap.getSize();
        
        if (fastBitmap.isGrayscale()) {
            
            CalculateMap( inGray, outGray, mapGray );
            
            for (int i = 0; i < size; i++) {
                fastBitmap.setGray(i, mapGray[fastBitmap.getGray(i)]);
            }
        }
        else{
            
            CalculateMap( inRed, outRed, mapRed );
            CalculateMap( inGreen, outGreen, mapGreen );
            CalculateMap( inBlue, outBlue, mapBlue );
            
            for (int i = 0; i < size; i++) {
                int r = mapRed[fastBitmap.getRed(i)];
                int g = mapGreen[fastBitmap.getGreen(i)];
                int b = mapBlue[fastBitmap.getBlue(i)];

                fastBitmap.setRGB(i, r, g, b);
            }
        }
        
    }
    
    /**
     * Calculate conversion map.
     * @param inRange Input range.
     * @param outRange Output range.
     * @param map Conversion map.
     */
    private void CalculateMap(IntRange inRange, IntRange outRange, int[] map){
        double k = 0, b = 0;

        if ( inRange.getMax() != inRange.getMin() )
        {
            k = (double) ( outRange.getMax() - outRange.getMin() ) / (double) ( inRange.getMax() - inRange.getMin() );
            b = (double) ( outRange.getMin() ) - k * inRange.getMin();
        }

        for ( int i = 0; i < 256; i++ )
        {
            int v = (int) i;

            if ( v >= inRange.getMax() )
                v = outRange.getMax();
            else if ( v <= inRange.getMin() )
                v = outRange.getMin();
            else
                v = (int) ( k * v + b );

            map[i] = v;
        }
    }
}