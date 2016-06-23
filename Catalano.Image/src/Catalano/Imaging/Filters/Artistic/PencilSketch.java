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
import Catalano.Imaging.Filters.GammaCorrection;
import Catalano.Imaging.Filters.GaussianBlur;
import Catalano.Imaging.Filters.Invert;
import Catalano.Imaging.Filters.SaturationCorrection;
import Catalano.Imaging.IApplyInPlace;

/**
 * Pencil Sketch.
 * @author Diego Catalano
 */
public class PencilSketch implements IApplyInPlace{
    
    private int saturationCorrection = - 70;
    private double sigma = 1.4;
    private int size = 5;
    private double gamma = -5;

    /**
     * Get Saturation correction.
     * @return Saturation correction.
     */
    public int getSaturationCorrection() {
        return saturationCorrection;
    }

    /**
     * Set Saturation correction.
     * @param saturationCorrection Saturation correction.
     */
    public void setSaturationCorrection(int saturationCorrection) {
        this.saturationCorrection = saturationCorrection;
    }

    /**
     * Get sigma used in Gaussian Blur.
     * @return Sigma value.
     */
    public double getSigma() {
        return sigma;
    }

    /**
     * Set sigma used in Gaussian Blur.
     * @param sigma Sigma value.
     */
    public void setSigma(double sigma) {
        this.sigma = sigma;
    }

    /**
     * Get size of kernel used in Gaussian Blur.
     * @return Size of kernel.
     */
    public int getSize() {
        return size;
    }

    /**
     * Set size of kernel used in Gaussian Blur.
     * @param size Size of kernel.
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * Get gamma correction value.
     * @return Gamma value.
     */
    public double getGamma() {
        return gamma;
    }

    /**
     * Set gamma correction.
     * @param gamma Gamma value.
     */
    public void setGamma(double gamma) {
        this.gamma = gamma;
    }

    /**
     * Initialize a new instance of the PencilSketch class.
     */
    public PencilSketch() {}
    
    /**
     * Initialize a new instance of the PencilSketch class.
     * @param saturationCorrection Saturation correction.
     * @param sigma Sigma used in Gaussian Blur.
     * @param size Size of kernel used in Gaussian Blur.
     * @param gamma Gamma correction.
     */
    public PencilSketch(int saturationCorrection, double sigma, int size, double gamma){
        this.saturationCorrection = saturationCorrection;
        this.sigma = sigma;
        this.size = size;
        this.gamma = gamma;
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        if(fastBitmap.isRGB()){
            FastBitmap layerA = new FastBitmap(fastBitmap);
            FastBitmap layerB = new FastBitmap(fastBitmap);

            SaturationCorrection sc = new SaturationCorrection(saturationCorrection);
            sc.applyInPlace(layerA);

            layerB.setImage(layerA);
            Invert i = new Invert();
            i.applyInPlace(layerB);

            GaussianBlur gb = new GaussianBlur(sigma, size);
            gb.applyInPlace(layerB);

            Blend b = new Blend(layerB, Blend.Algorithm.ColorDodge);
            b.applyInPlace(layerA);

            GammaCorrection gc = new GammaCorrection(gamma);
            gc.applyInPlace(layerA);

            b.setAlgorithm(Blend.Algorithm.Overlay);
            b.setOverlay(layerA);
            b.applyInPlace(fastBitmap);
        }
    }
}