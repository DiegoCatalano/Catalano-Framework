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
import java.util.Arrays;

/**
 * Median binary patterns (MBP) is a type of feature used for classification in computer vision.
 * Different of ILBP, MBP use the median in 3x3 region and the center of pixel in the encoding.
 * MBP was first described in 2007. It has since been found to be a powerful feature for texture classification.
 * 
 * @author Diego Catalano
 */
public class MedianBinaryPattern implements IBinaryPattern{

    /**
     * Initialize a new instance of the MedianBinaryPattern class.
     */
    public MedianBinaryPattern() {}
    
    @Override
    public ImageHistogram ComputeFeatures(FastBitmap fastBitmap){
        if (!fastBitmap.isGrayscale())
            throw new IllegalArgumentException("MBP only works in grayscale images.");
        
        int width = fastBitmap.getWidth() - 1;
        int height = fastBitmap.getHeight() - 1;
        
        int sum;
        int[] g = new int[511];
        int[] values = new int[9];
        int median;
        for (int x = 1; x < height; x++) {
            for (int y = 1; y < width; y++) {
                values[0] = fastBitmap.getGray(x-1, y-1);
                values[1] = fastBitmap.getGray(x-1, y);
                values[2] = fastBitmap.getGray(x-1, y+1);
                values[3] = fastBitmap.getGray(x, y-1);
                values[4] = fastBitmap.getGray(x, y);
                values[5] = fastBitmap.getGray(x, y+1);
                values[6] = fastBitmap.getGray(x+1, y-1);
                values[7] = fastBitmap.getGray(x+1, y);
                values[8] = fastBitmap.getGray(x+1, y+1);
                
                Arrays.sort(values);
                median = values[4];
                
                sum = 0;
                if (fastBitmap.getGray(x - 1, y - 1) - median >= 0)    sum += 128;
                if (fastBitmap.getGray(x - 1, y) - median >= 0)        sum += 64;
                if (fastBitmap.getGray(x - 1, y + 1) - median >= 0)    sum += 32;
                if (fastBitmap.getGray(x, y + 1) - median >= 0)        sum += 16;
                if (fastBitmap.getGray(x + 1, y + 1) - median >= 0)    sum += 8;
                if (fastBitmap.getGray(x + 1, y) - median >= 0)        sum += 4;
                if (fastBitmap.getGray(x + 1, y - 1) - median >= 0)    sum += 2;
                if (fastBitmap.getGray(x, y - 1) - median >= 0)        sum += 1;
                
                //2^9
                if(fastBitmap.getGray(x, y) - median >= 0)             sum += 256;
                
                //all zeros and all ones are the same
                if(sum == 511) sum = 0;
                
                g[sum]++;
            }
        }
        return new ImageHistogram(g);
    }
}