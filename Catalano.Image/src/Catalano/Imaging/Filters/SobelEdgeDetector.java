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

import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.IApplyInPlace;

/**
 * Sobel edge detector.
 * <para> The filter searches for objects' edges by applying Sobel operator.
 * Each pixel of the result image is calculated as approximated absolute gradient magnitude for corresponding pixel of the source image:
 * |G| = |Gx| + |Gy] ,
 * where Gx and Gy are calculate utilizing Sobel convolution kernels.
 * Using the above kernel the approximated magnitude for pixel x is calculate using the next equation:
 * |G| = |P1 + 2P2 + P3 - P7 - 2P6 - P5| + |P3 + 2P4 + P5 - P1 - 2P8 - P7|</para>
 * @author Diego Catalano
 */
public class SobelEdgeDetector implements IApplyInPlace{
    
    private boolean scaleIntensity = true;

    /**
     * Is scale intensity.
     * 
     * <para>Check if edges' pixels intensities of the result image
     * should be scaled in the range of the lowest and the highest possible intensity
     * values.</para>
     * 
     * @return Scale intensity value.
     */
    public boolean isScaleIntensity() {
        return scaleIntensity;
    }

    /**
     * Set scale intensity.
     * 
     * <para>Check if edges' pixels intensities of the result image
     * should be scaled in the range of the lowest and the highest possible intensity
     * values.</para>
     * 
     * @param scaleIntensity Scale intensity value.
     */
    public void setScaleIntensity(boolean scaleIntensity) {
        this.scaleIntensity = scaleIntensity;
    }

    /**
     * Initializes a new instance of the SobelEdgeDetector class.
     */
    public SobelEdgeDetector() {}

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        if (fastBitmap.isGrayscale()){
            int width = fastBitmap.getWidth() - 2;
            int height = fastBitmap.getHeight() - 2;
            
            int stride = width;
            int offset = stride + 1;
            
            FastBitmap copy = new FastBitmap(fastBitmap);
            double g, max = 0;
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    int p1 = copy.getGray(offset - stride - 1);
                    int p2 = copy.getGray(offset - stride);
                    int p3 = copy.getGray(offset - stride + 1);
                    int p4 = copy.getGray(offset + 1);
                    int p5 = copy.getGray(offset + stride);
                    int p6 = copy.getGray(offset + stride + 1);
                    int p7 = copy.getGray(offset + stride - 1);
                    int p8 = copy.getGray(offset - 1);

                    g = Math.min(255, Math.abs(p1 + p3 - p7 - p2 + 2 * (p2 - p5)) + Math.abs(p2 + p6 - p1 - p7 + 2 * (p4 - p8)));
                    if (g > max) max = g;
                    fastBitmap.setGray(offset, (int)g);
                    offset++;
                }
                offset += 2;
            }
            
            int size = fastBitmap.getSize();
            if (scaleIntensity && max != 255){
                double factor = 255.0 / (double) max;
                
                for (int i = 0; i < size; i++) {
                    fastBitmap.setGray(i, (int)(fastBitmap.getGray(i) * factor));
                }
            }
            
        }
        else{
            throw new IllegalArgumentException("SobelEdgeDetector only works in grayscale images.");
        }
    }
}