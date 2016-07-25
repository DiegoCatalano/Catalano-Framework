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

package Catalano.Imaging.Texture.BinaryPattern;

import Catalano.Imaging.Tools.*;
import Catalano.Imaging.FastBitmap;

/**
 * Local binary patterns (LBP) is a type of feature used for classification in computer vision.
 * LBP was first described in 1994. It has since been found to be a powerful feature for texture classification.
 * 
 * @author Diego Catalano
 */
public class LocalBinaryPattern implements IBinaryPattern{

    /**
     * Initialize a new instance of the LocalBinaryPattern class.
     */
    public LocalBinaryPattern() {}
    
    @Override
    public ImageHistogram ComputeFeatures(FastBitmap fastBitmap){
        if (!fastBitmap.isGrayscale())
            throw new IllegalArgumentException("LBP works only with grayscale images.");
        
        int width = fastBitmap.getWidth() - 1;
        int height = fastBitmap.getHeight() - 1;
        
        int sum;
        int[] g = new int[256];
        int gray;
        for (int x = 1; x < height; x++) {
            for (int y = 1; y < width; y++) {
                gray = fastBitmap.getGray(x, y);
                sum = 0;
                if (fastBitmap.getGray(x - 1, y - 1) - gray >= 0)    sum += 128;
                if (fastBitmap.getGray(x - 1, y) - gray >= 0)        sum += 64;
                if (fastBitmap.getGray(x - 1, y + 1) - gray >= 0)    sum += 32;
                if (fastBitmap.getGray(x, y + 1) - gray >= 0)        sum += 16;
                if (fastBitmap.getGray(x + 1, y + 1) - gray >= 0)    sum += 8;
                if (fastBitmap.getGray(x + 1, y) - gray >= 0)        sum += 4;
                if (fastBitmap.getGray(x + 1, y - 1) - gray >= 0)    sum += 2;
                if (fastBitmap.getGray(x, y - 1) - gray >= 0)        sum += 1;
                g[sum]++;
            }
        }
        return new ImageHistogram(g);
    }
    
    /**
     * Convert the image in binary pattern representation.
     * @param fastBitmap Image.
     * @return Binary pattern image.
     */
    public FastBitmap toFastBitmap(FastBitmap fastBitmap){
        
        if (!fastBitmap.isGrayscale())
            throw new IllegalArgumentException("LBP works only with grayscale images.");
        
        FastBitmap fb = new FastBitmap(fastBitmap.getWidth(), fastBitmap.getHeight(), FastBitmap.ColorSpace.Grayscale);
        
        int width = fastBitmap.getWidth() - 1;
        int height = fastBitmap.getHeight() - 1;
        
        int sum;
        int gray;
        for (int x = 1; x < height; x++) {
            for (int y = 1; y < width; y++) {
                gray = fastBitmap.getGray(x, y);
                sum = 0;
                if (fastBitmap.getGray(x - 1, y - 1) - gray >= 0)    sum += 128;
                if (fastBitmap.getGray(x - 1, y) - gray >= 0)        sum += 64;
                if (fastBitmap.getGray(x - 1, y + 1) - gray >= 0)    sum += 32;
                if (fastBitmap.getGray(x, y + 1) - gray >= 0)        sum += 16;
                if (fastBitmap.getGray(x + 1, y + 1) - gray >= 0)    sum += 8;
                if (fastBitmap.getGray(x + 1, y) - gray >= 0)        sum += 4;
                if (fastBitmap.getGray(x + 1, y - 1) - gray >= 0)    sum += 2;
                if (fastBitmap.getGray(x, y - 1) - gray >= 0)        sum += 1;
                fb.setGray(x, y, sum);
            }
        }
        return fb;
    }
}