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

import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.Tools.ImageHistogram;

/**
 * Local Gradient Coding based on horizontal and diagonal prior principle (LGC-HD) is a type of feature used for classification in computer vision.
 * LGC-HD was first described in 2014. It has since been found to be a powerful feature for texture classification.
 * 
 * References:
 * 
 * Tong, Ying, Rui Chen, and Yong Cheng.
 * "Facial expression recognition algorithm using LGC based on horizontal and diagonal prior principle."
 * Optik-International Journal for Light and Electron Optics 125.16 (2014): 4186-4189.
 * 
 * @author Diego Catalano
 */
public class LocalGradientCodingHD implements IBinaryPattern{

    /**
     * Initialize a new instance of the LocalGradientCodingHD class.
     */
    public LocalGradientCodingHD() {}

    @Override
    public ImageHistogram ComputeFeatures(FastBitmap fastBitmap) {
        if (!fastBitmap.isGrayscale())
            throw new IllegalArgumentException("LGC-HD works only with grayscale images.");
        
        int width = fastBitmap.getWidth() - 1;
        int height = fastBitmap.getHeight() - 1;
        
        int sum;
        int[] g = new int[32];
        for (int x = 1; x < height; x++) {
            for (int y = 1; y < width; y++) {
                sum = 0;
                if (fastBitmap.getGray(x - 1, y - 1) - fastBitmap.getGray(x - 1, y + 1) >= 0)        sum += 16;
                if (fastBitmap.getGray(x, y - 1) - fastBitmap.getGray(x, y + 1) >= 0)                sum += 8;
                if (fastBitmap.getGray(x + 1, y - 1) - fastBitmap.getGray(x + 1, y + 1) >= 0)        sum += 4;
                if (fastBitmap.getGray(x - 1, y - 1) - fastBitmap.getGray(x + 1, y + 1) >= 0)        sum += 2;
                if (fastBitmap.getGray(x - 1, y + 1) - fastBitmap.getGray(x + 1, y - 1) >= 0)        sum += 1;
                g[sum]++;
            }
        }
        return new ImageHistogram(g);
    }
}