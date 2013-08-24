// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2013
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
 * Sobel edge detector.
 * <para> The filter searches for objects' edges by applying Sobel operator.
 * Each pixel of the result image is calculated as approximated absolute gradient magnitude for corresponding pixel of the source image:
 * |G| = |Gx| + |Gy] ,
 * where Gx and Gy are calculate utilizing Sobel convolution kernels.
 * Using the above kernel the approximated magnitude for pixel x is calculate using the next equation:
 * |G| = |P1 + 2P2 + P3 - P7 - 2P6 - P5| + |P3 + 2P4 + P5 - P1 - 2P8 - P7|
 * @author Diego Catalano
 */
public class SobelEdgeDetector implements IBaseInPlace{

    /**
     * Initializes a new instance of the SobelEdgeDetector class.
     */
    public SobelEdgeDetector() {}

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        if (fastBitmap.isGrayscale()){
            int height = fastBitmap.getHeight();
            int width = fastBitmap.getWidth();
            FastBitmap copy = new FastBitmap(fastBitmap);
            for (int i = 1; i < height - 1; i++) {
                for (int j = 1; j < width - 1; j++) {
                    int p1 = copy.getGray(i - 1, j - 1);
                    int p2 = copy.getGray(i - 1, j);
                    int p3 = copy.getGray(i - 1, j + 1);
                    int p4 = copy.getGray(i, j + 1);
                    int p5 = copy.getGray(i + 1, j + 1);
                    int p6 = copy.getGray(i + 1, j);
                    int p7 = copy.getGray(i + 1, j - 1);
                    int p8 = copy.getGray(i, j - 1);

                    int g = Math.min(255, Math.abs(p1 + 2*p2 + p3 - p7 - 2*p6 - p5) + Math.abs(p3 + 2*p4 + p5 - p1 - 2*p8 - p7));
                    fastBitmap.setGray(i, j, g);

                }
            }
        }
        else{
            throw new IllegalArgumentException("SobelEdgeDetector only works in grayscale images.");
        }
    }
}