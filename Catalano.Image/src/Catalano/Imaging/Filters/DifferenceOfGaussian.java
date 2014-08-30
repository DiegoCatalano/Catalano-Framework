// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2014
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

package Catalano.Imaging.Filters;

import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.IBaseInPlace;

/**
 * Difference of Gaussians is a feature enhancement algorithm that involves the subtraction of one blurred version of an original image from another.
 * @author Diego Catalano
 */
public class DifferenceOfGaussian implements IBaseInPlace{
    
    private int windowSize1 = 3, windowSize2 = 5;
    private double sigma1 = 1.4D, sigma2 = 1.4D;
    
    /**
     * Get first sigma value.
     * @return Sigma value.
     */
    public double getSigma1() {
        return sigma1;
    }

    /**
     * Set first sigma value.
     * @param sigma Sigma value.
     */
    public void setSigma1(double sigma) {
        this.sigma1 = Math.max( 0.5, Math.min( 5.0, sigma ) );
    }

    /**
     * Get second sigma value.
     * @return Sigma value.
     */
    public double getSigma2() {
        return sigma2;
    }

    /**
     * Set second sigma value.
     * @param sigma Sigma value.
     */
    public void setSigma2(double sigma) {
        this.sigma2 = Math.max( 0.5, Math.min( 5.0, sigma ) );
    }
    
    /**
     * Get first window size.
     * @return Window size value.
     */
    public int getWindowSize1() {
        return windowSize1;
    }

    /**
     * Set first window size.
     * @param size Window size value.
     */
    public void setWindowSize1(int size) {
        this.windowSize1 = Math.max( 3, Math.min( 21, size | 1 ) );
    }
    
    /**
     * Get second window size.
     * @return Window size value.
     */
    public int getWindowSize2() {
        return windowSize2;
    }

    /**
     * Set second window size.
     * @param size Window size value.
     */
    public void setWindowSize2(int size) {
        this.windowSize2 = Math.max( 3, Math.min( 21, size | 1 ) );
    }

    /**
     * Initialize a new instance of the DifferenceOfGaussian class.
     */
    public DifferenceOfGaussian() {}

    /**
     * Initialize a new instance of the DifferenceOfGaussian class.
     * @param windowSize1 First window size.
     * @param windowSize2 Second window size.
     */
    public DifferenceOfGaussian(int windowSize1, int windowSize2) {
        this.windowSize1 = windowSize1;
        this.windowSize2 = windowSize2;
    }

    /**
     * Initialize a new instance of the DifferenceOfGaussian class.
     * @param windowSize1 First window size.
     * @param windowSize2 Second window size.
     * @param sigma Sigma value for both images.
     */
    public DifferenceOfGaussian(int windowSize1, int windowSize2, double sigma) {
        this.windowSize1 = windowSize1;
        this.windowSize2 = windowSize2;
        this.sigma1 = Math.max( 0.5, Math.min( 5.0, sigma ) );
    }
    
    /**
     * Initialize a new instance of the DifferenceOfGaussian class.
     * @param windowSize1 First window size.
     * @param windowSize2 Second window size.
     * @param sigma First sigma value.
     * @param sigma2 Second sigma value.
     */
    public DifferenceOfGaussian(int windowSize1, int windowSize2, double sigma, double sigma2) {
        this.windowSize1 = windowSize1;
        this.windowSize2 = windowSize2;
        this.sigma1 = Math.max( 0.5, Math.min( 5.0, sigma ) );
        this.sigma2 = Math.max( 0.5, Math.min( 5.0, sigma2 ) );
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        FastBitmap b = new FastBitmap(fastBitmap);
        
        GaussianBlur gauss = new GaussianBlur(sigma1, windowSize1);
        gauss.applyInPlace(b);
        
        gauss.setSize(windowSize2);
        gauss.setSigma(sigma2);
        gauss.applyInPlace(fastBitmap);
        
        Subtract sub = new Subtract(b);
        sub.applyInPlace(fastBitmap);
    }
}
