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

/**
 * Homogenity edge detector.
 * 
 * <para>The filter finds objects' edges by calculating maximum difference
 * of processing pixel with neighboring pixels in 8 direction.</para>
 * 
 * @author Diego Catalano
 */
public class HomogenityEdgeDetector implements IApplyInPlace{

    /**
     * Initializes a new instance of the HomogenityEdgeDetector class.
     */
    public HomogenityEdgeDetector() {}

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        if (fastBitmap.isGrayscale()){
            int width = fastBitmap.getWidth() - 2;
            int height = fastBitmap.getHeight() - 2;
            
            int stride = fastBitmap.getWidth();
            int offset = stride + 1;
            
            FastBitmap copy = new FastBitmap(fastBitmap);
            
            int d, max, v;
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    
                    max = 0;
                    v = copy.getGray(offset);
                    
                    d = v - copy.getGray(offset - stride - 1);
                    if ( d < 0 ) d = -d;
                    if ( d > max ) max = d;
                    
                    d = v - copy.getGray(offset - stride);
                    if ( d < 0 ) d = -d;
                    if ( d > max ) max = d;
                    
                    d = v - copy.getGray(offset - stride + 1);
                    if ( d < 0 ) d = -d;
                    if ( d > max ) max = d;
                    
                    d = v - copy.getGray(offset - 1);
                    if ( d < 0 ) d = -d;
                    if ( d > max ) max = d;
                    
                    d = v - copy.getGray(offset + 1);
                    if ( d < 0 ) d = -d;
                    if ( d > max ) max = d;
                    
                    d = v - copy.getGray(offset + stride - 1);
                    if ( d < 0 ) d = -d;
                    if ( d > max ) max = d;
                    
                    d = v - copy.getGray(offset + stride);
                    if ( d < 0 ) d = -d;
                    if ( d > max ) max = d;
                    
                    d = v - copy.getGray(offset + stride + 1);
                    if ( d < 0 ) d = -d;
                    if ( d > max ) max = d;
                    
                    fastBitmap.setGray(offset, max);
                    offset++;
                }
                offset += 2;
            }
        }
        else{
            throw new IllegalArgumentException("Homogenity Edge Detector only works with grayscale images.");
        }
    }
}