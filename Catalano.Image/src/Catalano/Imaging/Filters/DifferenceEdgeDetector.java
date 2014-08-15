// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2014
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

import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.IBaseInPlace;

/**
 * Difference edge detector.
 * 
 * <para>The filter finds objects' edges by calculating maximum difference
 * between pixels in 4 directions around the processing pixel.</para>
 * 
 * <para>Suppose 3x3 square element of the source image (x - is currently processed pixel):
 * <code lang="none">
 * P1 P2 P3
 * P8  x P4
 * P7 P6 P5
 * </code>
 * The corresponding pixel of the result image equals to:
 * <code lang="none">
 * max( |P1-P5|, |P2-P6|, |P3-P7|, |P4-P8| )
 * </code>
 * </para>
 * 
 * @author Diego Catalano
 */
public class DifferenceEdgeDetector implements IBaseInPlace{

    /**
     * Initializes a new instance of the DifferenceEdgeDetector class.
     */
    public DifferenceEdgeDetector() {}

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        if (fastBitmap.isGrayscale()){
            
            FastBitmap copy = new FastBitmap(fastBitmap);
            int width = copy.getWidth();
            int height = copy.getHeight();
            
            int max, diff;
            for (int x = 1; x < height - 1; x++) {
                for (int y = 1; y < width - 1; y++) {
                    
                    max = 0;
                    
                    diff = copy.getGray(x - 1, y - 1) - copy.getGray(x + 1, y + 1);
                    if (diff < 0) diff = -diff;
                    if (diff > max) max = diff;
                    
                    diff = copy.getGray(x - 1, y) - copy.getGray(x + 1, y);
                    if (diff < 0) diff = -diff;
                    if (diff > max) max = diff;
                    
                    diff = copy.getGray(x - 1, y + 1) - copy.getGray(x + 1, y - 1);
                    if (diff < 0) diff = -diff;
                    if (diff > max) max = diff;
                    
                    diff = copy.getGray(x, y + 1) - copy.getGray(x, y - 1);
                    if (diff < 0) diff = -diff;
                    if (diff > max) max = diff;
                    
                    fastBitmap.setGray(x, y, max);
                }
            }
            
        }
        else{
            throw new IllegalArgumentException("DifferenceEdgeDetector only works in grayscale images.");
        }
    }
}