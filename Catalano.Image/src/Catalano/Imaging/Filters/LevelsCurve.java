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

package Catalano.Imaging.Filters;

import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.IApplyInPlace;
import Catalano.Imaging.Tools.Curve;

/**
 * Curve correction of RGB channels.
 * <p>The filter performs curve correction of RGB channels by mapping specified channels' input ranges to output ranges.</p>
 * 
 * @author Diego Catalano
 */
public class LevelsCurve implements IApplyInPlace{
    
    private Curve curveRed;
    private Curve curveGreen;
    private Curve curveBlue;
    private Curve curveGray;

    /**
     * Get Red curve.
     * @return Red curve.
     */
    public Curve getCurveRed() {
        return curveRed;
    }

    /**
     * Set Red curve.
     * @param curveRed Red curve.
     */
    public void setCurveRed(Curve curveRed) {
        this.curveRed = curveRed;
    }

    /**
     * Get Green curve.
     * @return Green curve.
     */
    public Curve getCurveGreen() {
        return curveGreen;
    }

    /**
     * Set Green curve.
     * @param curveGreen Green curve.
     */
    public void setCurveGreen(Curve curveGreen) {
        this.curveGreen = curveGreen;
    }

    /**
     * Get Blue curve.
     * @return Blue curve.
     */
    public Curve getCurveBlue() {
        return curveBlue;
    }

    /**
     * Set Blue curve.
     * @param curveBlue Blue curve.
     */
    public void setCurveBlue(Curve curveBlue) {
        this.curveBlue = curveBlue;
    }
    
    /**
     * Set curves.
     * @param curveRed Red curve.
     * @param curveGreen Green curve.
     * @param curveBlue Blue curve.
     */
    public void setCurve(Curve curveRed, Curve curveGreen, Curve curveBlue){
        this.curveRed = curveRed;
        this.curveGreen = curveGreen;
        this.curveBlue = curveBlue;
    }

    /**
     * Get Gray curve.
     * @return Gray curve.
     */
    public Curve getCurveGray() {
        return curveGray;
    }

    /**
     * Set Gray curve.
     * @param curveGray Gray curve.
     */
    public void setCurveGray(Curve curveGray) {
        this.curveGray = curveGray;
    }

    /**
     * Initialize a new instance of the LevelsCurve class.
     */
    public LevelsCurve() {
        curveRed = new Curve();
        curveGreen = new Curve();
        curveBlue = new Curve();
        curveGray = new Curve();
    }
    
    /**
     * Initialize a new instance of the LevelsCurve class.
     * @param curveGray Gray's channel.
     */
    public LevelsCurve(Curve curveGray){
        this.curveGray = curveGray;
    }

    /**
     * Initialize a new instance of the LevelsCurve class.
     * @param curveRed Red's channel.
     * @param curveGreen Green's channel
     * @param curveBlue Blue's channel.
     */
    public LevelsCurve(Curve curveRed, Curve curveGreen, Curve curveBlue) {
        this.curveRed = curveRed;
        this.curveGreen = curveGreen;
        this.curveBlue = curveBlue;
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        if(fastBitmap.isGrayscale()){
            
            int[] g = curveGray.makeLut();
            
            int size = fastBitmap.getSize();
            for (int i = 0; i < size; i++) {
                fastBitmap.setGray(i, g[fastBitmap.getGreen(i)]);
            }
            
        }
        else if(fastBitmap.isRGB()){
            
            int[] r = curveRed.makeLut();
            int[] g = curveGreen.makeLut();
            int[] b = curveBlue.makeLut();
            
            int size = fastBitmap.getSize();
            for (int i = 0; i < size; i++) {
                fastBitmap.setRGB(i, r[fastBitmap.getRed(i)], g[fastBitmap.getGreen(i)], b[fastBitmap.getBlue(i)]);
            }
            
        }
        else{
            throw new IllegalArgumentException("Levels Curve only supports grayscale and rgb images.");
        }
    }
}