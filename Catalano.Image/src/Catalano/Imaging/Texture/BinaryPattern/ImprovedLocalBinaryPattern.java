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
 * Improved Local binary patterns (ILBP) is a type of feature used for classification in computer vision.
 * Different of LBP, ILBP use the mean in 3x3 region and the center of pixel in the encoding.
 * ILBP was first described in 2004. It has since been found to be a powerful feature for texture classification.
 * 
 * @author Diego Catalano
 */
public class ImprovedLocalBinaryPattern implements IBinaryPattern{

    /**
     * Initialize a new instance of the ImprovedLocalBinaryPattern class.
     */
    public ImprovedLocalBinaryPattern() {}
    
    @Override
    public ImageHistogram ComputeFeatures(FastBitmap fastBitmap){
        if (!fastBitmap.isGrayscale())
            throw new IllegalArgumentException("ILBP works only with grayscale images.");
        
        int width = fastBitmap.getWidth() - 1;
        int height = fastBitmap.getHeight() - 1;
        
        int sum;
        int[] g = new int[511];
        int mean;
        for (int x = 1; x < height; x++) {
            mean = 0;
            for (int y = 1; y < width; y++) {
                mean += fastBitmap.getGray(x-1, y-1);
                mean += fastBitmap.getGray(x-1, y);
                mean += fastBitmap.getGray(x-1, y+1);
                mean += fastBitmap.getGray(x, y-1);
                mean += fastBitmap.getGray(x, y);
                mean += fastBitmap.getGray(x, y+1);
                mean += fastBitmap.getGray(x+1, y-1);
                mean += fastBitmap.getGray(x+1, y);
                mean += fastBitmap.getGray(x+1, y+1);
                mean /= 9;
                
                sum = 0;
                if (fastBitmap.getGray(x - 1, y - 1) - mean >= 0)    sum += 128;
                if (fastBitmap.getGray(x - 1, y) - mean >= 0)        sum += 64;
                if (fastBitmap.getGray(x - 1, y + 1) - mean >= 0)    sum += 32;
                if (fastBitmap.getGray(x, y + 1) - mean >= 0)        sum += 16;
                if (fastBitmap.getGray(x + 1, y + 1) - mean >= 0)    sum += 8;
                if (fastBitmap.getGray(x + 1, y) - mean >= 0)        sum += 4;
                if (fastBitmap.getGray(x + 1, y - 1) - mean >= 0)    sum += 2;
                if (fastBitmap.getGray(x, y - 1) - mean >= 0)        sum += 1;
                
                //2^9
                if(fastBitmap.getGray(x, y) - mean >= 0)             sum += 256;
                
                //all zeros and all ones are the same
                if(sum == 511) sum = 0;
                
                g[sum]++;
            }
        }
        return new ImageHistogram(g);
    }
}