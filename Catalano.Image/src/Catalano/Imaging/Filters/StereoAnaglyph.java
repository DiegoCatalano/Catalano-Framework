// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
// Copyright © Andrew Kirillov, 2005-2009
// andrew.kirillov@aforgenet.com
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

/**
 * Stereo anaglyph filter.
 * <para>The image processing filter produces stereo anaglyph images which are
 * aimed to be viewed through anaglyph glasses with red filter over the left eye and
 * cyan over the right.</para>
 * @author Diego Catalano
 */
public class StereoAnaglyph implements IApplyInPlace{

    /**
     * Enumeration of algorithms for creating anaglyph images.
     */
    public static enum Algorithm {

        /**
         * Creates anaglyph image using the below calculations: 
         * <br />
         * <br /> Ra = 0.299*RI + 0.587 * GI + 0.114*BI;
         * <br /> Ga = 0;
         * <br /> Ba = 0.299 * Rr + 0.587 * Gr + 0.114 * Br.
         */
        TrueAnaglyph,
        /**
         * Creates anaglyph image using the below calculations: 
         * <br />
         * <br /> Ra = 0.299*RI + 0.587 * GI + 0.114*BI;
         * <br /> Ga = 0.299*Rr + 0.587 * Gr + 0.114*Br;
         * <br /> Ba =0.299*Rr + 0.587 * Gr + 0.114*Br;
         */
        GrayAnaglyph,
        /**
         * Creates anaglyph image using the below calculations: 
         * <br />
         * <br /> Ra = RI;
         * <br /> Ga = Gr;
         * <br /> Ba = Br;
         */
        ColorAnaglyph,
        /**
         * Creates anaglyph image using the below calculations: 
         * <br />
         * <br /> Ra = 0.299*RI + 0.587 * GI + 0.114*BI;
         * <br /> Ga = Gr;
         * <br /> Ba = Br;
         */
        HalfColorAnaglyph,
        /**
         * Creates anaglyph image using the below calculations: 
         * <br />
         * <br /> Ra = 0.7 * GI + 0.3 * BI;
         * <br /> Ga = Gr;
         * <br /> Ba = Br;
         */
        OptimizedAnaglyph
    };
    private FastBitmap overlayImage;
    private Algorithm algorithm;

    /**
     * Initializes a new instance of the StereoAnaglyph class.
     */
    public StereoAnaglyph() {
        
    }

    /**
     * Initializes a new instance of the StereoAnaglyph class.
     * @param overlayImage Overlay image.
     */
    public StereoAnaglyph(FastBitmap overlayImage) {
        this.overlayImage = overlayImage;
    }

    /**
     * Initializes a new instance of the StereoAnaglyph class.
     * @param overlayImage Overlay image.
     * @param algorithm Enumeration of algorithms for creating anaglyph images.
     */
    public StereoAnaglyph(FastBitmap overlayImage, Algorithm algorithm) {
        this.overlayImage = overlayImage;
        this.algorithm = algorithm;
    }

    /**
     * Algorithm for creating anaglyph images.
     * @return Algorithm.
     */
    public Algorithm getAlgorithm() {
        return algorithm;
    }

    /**
     * Algorithm for creating anaglyph images.
     * @param algorithm Algorithm.
     */
    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    /**
     * Overlay Image.
     * @return Overlay image.
     */
    public FastBitmap getOverlayImage() {
        return overlayImage;
    }

    /**
     * Overlay Image.
     * @param overlayImage Overlay image.
     */
    public void setOverlayImage(FastBitmap overlayImage) {
        this.overlayImage = overlayImage;
    }
    
    @Override
    public void applyInPlace(FastBitmap sourceImage){
        
        int size = sourceImage.getSize();
        int r,g,b;
        switch(algorithm){
            case TrueAnaglyph:
                for (int i = 0; i < size; i++) {
                    r = (int)(sourceImage.getRed(i) * 0.299 + sourceImage.getGreen(i) * 0.587 + sourceImage.getBlue(i) * 0.114);
                    g = 0;
                    b = (int)(overlayImage.getRed(i) * 0.299 + overlayImage.getGreen(i) * 0.587 + overlayImage.getBlue(i) * 0.114);
                    sourceImage.setRGB(i, r, g, b);
                }
            break;
            
            case GrayAnaglyph:
                for (int i = 0; i < size; i++) {
                    r = (int)(sourceImage.getRed(i) * 0.299 + sourceImage.getGreen(i) * 0.587 + sourceImage.getBlue(i) * 0.114);
                    g = (int)(overlayImage.getRed(i) * 0.299 + overlayImage.getGreen(i) * 0.587 + overlayImage.getBlue(i) * 0.114);
                    b = g;
                    sourceImage.setRGB(i, r, g, b);
                }
            break;
                
            case ColorAnaglyph:
                for (int i = 0; i < size; i++) {
                    g = overlayImage.getGreen(i);
                    b = overlayImage.getBlue(i);
                    sourceImage.setGreen(i, g);
                    sourceImage.setBlue(i, b);
                }
            break;
                
            case HalfColorAnaglyph:
                for (int i = 0; i < size; i++) {
                    r = (int)(sourceImage.getRed(i) * 0.299 + sourceImage.getGreen(i) * 0.587 + sourceImage.getBlue(i) * 0.114);
                    g = overlayImage.getGreen(i);
                    b = overlayImage.getBlue(i);
                    sourceImage.setRGB(i, r, g, b);
                }
            break;
                
            case OptimizedAnaglyph:
                for (int i = 0; i < size; i++) {
                    r = (int)(sourceImage.getGreen(i) * 0.7 + sourceImage.getBlue(i) * 0.3);
                    g = overlayImage.getGreen(i);
                    b = overlayImage.getBlue(i);
                    sourceImage.setRGB(i, r, g, b);
                }
            break;
        }
    }
}