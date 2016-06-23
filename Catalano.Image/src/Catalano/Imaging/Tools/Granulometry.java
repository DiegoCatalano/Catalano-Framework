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

package Catalano.Imaging.Tools;

import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.Filters.Opening;

/**
 * Calculate the granulometry function for a given image.
 * 
 * <para>In mathematical morphology, granulometry is an approach to compute a size distribution of grains in binary images,
 * using a series of morphological opening operations. It was introduced by Georges Matheron in the 1960s,
 * and is the basis for the characterization of the concept of size in mathematical morphology.</para>
 * 
 * References: http://en.wikipedia.org/wiki/Granulometry_%28morphology%29
 * @author Diego Catalano
 */
public class Granulometry {
    
    private int minRadius = 1;
    private int maxRadius = 10;
    private int steps = 1;

    /**
     * Get minimum radius.
     * @return Radius.
     */
    public int getMinRadius() {
        return minRadius;
    }

    /**
     * Set minimum radius.
     * @param minRadius Radius.
     */
    public void setMinRadius(int minRadius) {
        this.minRadius = minRadius;
    }

    /**
     * Get maximum radius.
     * @return Radius.
     */
    public int getMaxRadius() {
        return maxRadius;
    }

    /**
     * Set maximum radius.
     * @param maxRadius Radius.
     */
    public void setMaxRadius(int maxRadius) {
        this.maxRadius = maxRadius;
    }

    /**
     * Get number of steps.
     * @return Steps.
     */
    public int getSteps() {
        return steps;
    }

    /**
     * Set number of steps.
     * @param steps Steps.
     */
    public void setSteps(int steps) {
        this.steps = steps;
    }

    /**
     * Initialize a new instance of the Granulometry class.
     */
    public Granulometry() {}
    
    /**
     * Initialize a new instance of the Granulometry class.
     * @param maxRadius Maximum radius.
     */
    public Granulometry(int maxRadius){
        this.maxRadius = maxRadius;
    }
    
    /**
     * Initialize a new instance of the Granulometry class.
     * @param minRadius Minimum radius.
     * @param maxRadius Maximum radius.
     */
    public Granulometry(int minRadius, int maxRadius){
        this.minRadius = minRadius;
        this.maxRadius = maxRadius;
    }
    
    /**
     * Initialize a new instance of the Granulometry class.
     * @param minRadius Minimum radius.
     * @param maxRadius Maximum radius.
     * @param steps Steps per radius.
     */
    public Granulometry(int minRadius, int maxRadius, int steps){
        this.minRadius = minRadius;
        this.maxRadius = maxRadius;
        this.steps = steps;
    }
    
    /**
     * Calculate granulometry density function.
     * 
     * G(n) = N(x + 1) - N(x) where N(x) = 1 - sumP(x) / sumP(0)
     * sumP(0) is the original image.
     * 
     * @param fastBitmap Image to be processed.
     * @return Result for each radius.
     */
    public float[] ProcessImage(FastBitmap fastBitmap){
        if (fastBitmap.isGrayscale()){
            float[] density = new float[maxRadius - minRadius];

            long sumPixel = getPixelSum(fastBitmap);

            // Original sum of pixel
            long sumOriginal = sumPixel;

            int index = 0;
            for (int i = minRadius; i < maxRadius; i += steps) {
                FastBitmap copy = new FastBitmap(fastBitmap);
                Opening open = new Opening(i);
                open.applyInPlace(copy);

                long sumPixel2 = getPixelSum(copy);

                //Calculate density function
                
                density[index] = -(float)(sumPixel2 - sumPixel) / (float)sumOriginal;
                sumPixel2 = sumPixel;
                index++;
            }

            return density;
        }
        else
            throw new IllegalArgumentException("Granulometry only works in grayscale images.");
    }
    
    /**
     * Returns the sum of pixels.
     * @param fastBitmap Image to be processed.
     * @return Sum of pixels.
     */
    private long getPixelSum(FastBitmap fastBitmap){
        
        long sum = 0;
        for (int i = 0; i < fastBitmap.getHeight(); i++) {
            for (int j = 0; j < fastBitmap.getWidth(); j++) {
                sum += fastBitmap.getGray(i, j);
            }
        }
        return sum;
    }
}