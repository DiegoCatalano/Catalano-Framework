// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
// Copyright © Andrew Kirillov, 2007-2008
// andrew.kirillov@gmail.com
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
import Catalano.Math.ComplexNumber;

/**
 * Filtering of frequencies outside of specified range in complex Fourier transformed image.
 * @author Diego Catalano
 */
public class FrequencyFilter {
    private IntRange freq = new IntRange(0, 1024);

    /**
     * Initializes a new instance of the FrequencyFilter class.
     */
    public FrequencyFilter() {}
    
    /**
     * Initializes a new instance of the FrequencyFilter class.
     * @param min Minimum value for to keep.
     * @param max Maximum value for to keep.
     */
    public FrequencyFilter(int min, int max){
        this.freq = new IntRange(min, max);
    }

    /**
     * Initializes a new instance of the FrequencyFilter class.S
     * @param range IntRange.
     */
    public FrequencyFilter(IntRange range) {
        this.freq = range;
    }

    /**
     * Get range of frequencies to keep.
     * @return IntRange.
     */
    public IntRange getFrequencyRange() {
        return freq;
    }

    /**
     * Set range of frequencies to keep. 
     * @param freq IntRange.
     */
    public void setFrequencyRange(IntRange freq) {
        this.freq = freq;
    }
    
    /**
     * Apply filter to an fourierTransform.
     * @param fourierTransform Fourier transformed.
     */
    public void ApplyInPlace(FourierTransform fourierTransform){
        if (!fourierTransform.isFourierTransformed()) {
            try {
                throw new Exception("the image should be fourier transformed.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        int width = fourierTransform.getWidth();
        int height = fourierTransform.getHeight();
        
        int halfWidth = width / 2;
        int halfHeight = height / 2;
        
        int min = freq.getMin();
        int max = freq.getMax();
        
        ComplexNumber[][] c = fourierTransform.getData();
        
        for ( int i = 0; i < height; i++ ){
            int y = i - halfHeight;

            for ( int j = 0; j < width; j++ ){
                int x = j - halfWidth;
                int d = (int) Math.sqrt( x * x + y * y );

                // filter values outside the range
                if ( ( d > max ) || ( d < min ) ){
                    c[i][j].real = 0;
                    c[i][j].imaginary = 0;
                }
            }
        }
        
    }
}
