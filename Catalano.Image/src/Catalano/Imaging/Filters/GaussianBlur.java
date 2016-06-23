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

import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.IApplyInPlace;
import Catalano.Math.Functions.Gaussian;

/**
 * Gaussian blur filter.
 * @author Diego Catalano
 */
public class GaussianBlur implements IApplyInPlace{
    private double sigma = 1.4;
    private int size = 5;

    /**
     * Initialize a new instance of the GaussianBlur class.
     */
    public GaussianBlur() {}
    
    /**
     * Initialize a new instance of the GaussianBlur class.
     * @param sigma Gaussian sigma value.[0.5, 5.0].
     */
    public GaussianBlur(double sigma) {
        this.sigma = Math.max( 0.5, Math.min( 5.0, sigma ) );
    }

    /**
     * Initialize a new instance of the GaussianBlur class.
     * @param sigma Gaussian sigma value.[0.5, 5.0].
     * @param size Kernel size. [3, 21].
     */
    public GaussianBlur(double sigma, int size) {
        setSigma(sigma);
        setSize(size);
    }
    
    /**
     * Get Gaussian sigma value.
     * @return Gaussian sigma.
     */
    public double getSigma() {
        return sigma;
    }

    /**
     * Set Gaussian sigma value.
     * @param sigma Gaussian sigma.
     */
    public void setSigma(double sigma) {
        this.sigma = Math.max( 0.5, Math.min( 5.0, sigma ) );
    }

    /**
     * Get kernel size.
     * @return Kernel size.
     */
    public int getSize() {
        return size;
    }

    /**
     * Set kernel size.
     * @param size Kernel size.
     */
    public void setSize(int size) {
        this.size = Math.max( 3, Math.min( 21, size | 1 ) );
    }
    
    @Override
    public void applyInPlace(FastBitmap fastBitmap){
        int[][] k = CreateKernel();
        Convolution c = new Convolution(k);
        c.applyInPlace(fastBitmap);
    }
    
    private int[][] CreateKernel(){
        Gaussian g = new Gaussian(sigma);
        double[][] k = g.Kernel2D(size);
        int[][] kint = new int[k.length][k[0].length];
        double min = k[0][0];
        
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                double v = k[i][j] / min;
                kint[i][j] = (int)v;
            }
        }
        return kint;
    }
}