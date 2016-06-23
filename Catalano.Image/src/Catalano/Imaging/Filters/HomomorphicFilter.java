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

package Catalano.Imaging.Filters;

import Catalano.Core.IntRange;
import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.IApplyInPlace;
import Catalano.Math.ComplexNumber;

/**
 * Is a frequency domain filtering process that compresses the brightness, while enhancing the contrast.
 * Homomorphic filtering process:
 * Image -> A natural log transform (base e) -> Fourier Transform -> Frequency filter -> Inverse Fourier Transform -> Inverse log function (the exponencial).
 * @author Diego Catalano
 */
public class HomomorphicFilter implements IApplyInPlace{
    
    private IntRange range;

    /**
     * Initialize a new instance of the HomomorphicFilter class.
     */
    public HomomorphicFilter() {}
    
    /**
     * Initialize a new instance of the HomomorphicFilter class.
     * @param min Minimum value of frequencies to keep.
     * @param max Maximum value of frequencies to keep.
     */
    public HomomorphicFilter(int min, int max){
        this.range = new IntRange(min, max);
    }

    /**
     * Initialize a new instance of the HomomorphicFilter class.
     * @param range Range of frequencies to keep.
     */
    public HomomorphicFilter(IntRange range) {
        this.range = range;
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        
        // Convert image to complex image.
        FourierTransform ft = new FourierTransform(fastBitmap);
        ComplexNumber[][] complex = ft.getData();
        int width = fastBitmap.getWidth();
        int height = fastBitmap.getHeight();
        
        // Compute log transform
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                complex[x][y].real = Math.log(complex[x][y].real + 1);
            }
        }
        
        // Forward Fast Fourier Transform
        ft.setData(complex);
        ft.Forward();
        
        // Frequency filter
        FrequencyFilter freq = new FrequencyFilter(range);
        freq.ApplyInPlace(ft);
        
        // Backward Fourier Transform
        ft.Backward();
        
        // Inverse log transform (exponencial)
        complex = ft.getData();
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                complex[x][y].real = Math.exp(complex[x][y].real - 1);
            }
        }
        ft.setData(complex);
        
        fastBitmap.setImage(ft.toFastBitmap());
    }
}