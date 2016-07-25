// Catalano Android Imaging Library
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
 * Gradient-based local binary patterns (GLBP) is a type of feature used for classification in computer vision.
 * GLBP was first described in 2011. It has since been found to be a powerful feature for texture classification.
 * @author Diego Catalano
 */
public class GradientLocalBinaryPattern implements IBinaryPattern{

    /**
     * Initialize a new instance of the GradientLocalBinaryPattern class.
     */
    public GradientLocalBinaryPattern() {}
    
    @Override
    public ImageHistogram ComputeFeatures(FastBitmap fastBitmap){
        if (!fastBitmap.isGrayscale()) {
            try {
                throw new Exception("GLBP works only with grayscale images.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        int width = fastBitmap.getWidth() - 1;
        int height = fastBitmap.getHeight() - 1;
        
        int sum;
        int[] g = new int[256];
        int grad;
        for (int x = 1; x < height; x++) {
            for (int y = 1; y < width; y++) {
                int cp = fastBitmap.getGray(x, y);
                grad = (int)(0.5 * (Math.abs(fastBitmap.getGray(x - 1, y - 1) - fastBitmap.getGray(x, y)) + Math.abs(fastBitmap.getGray(x + 1, y) - fastBitmap.getGray(x - 1, y))));
                
                sum = 0;
                if (grad - Math.abs(fastBitmap.getGray(x - 1, y - 1) - cp) >= 0)    sum += 128;
                if (grad - Math.abs(fastBitmap.getGray(x - 1, y) - cp) >= 0)        sum += 64;
                if (grad - Math.abs(fastBitmap.getGray(x - 1, y + 1) - cp) >= 0)    sum += 32;
                if (grad - Math.abs(fastBitmap.getGray(x, y + 1) - cp) >= 0)        sum += 16;
                if (grad - Math.abs(fastBitmap.getGray(x + 1, y + 1) - cp) >= 0)    sum += 8;
                if (grad - Math.abs(fastBitmap.getGray(x + 1, y) - cp) >= 0)        sum += 4;
                if (grad - Math.abs(fastBitmap.getGray(x + 1, y - 1) - cp) >= 0)    sum += 2;
                if (grad - Math.abs(fastBitmap.getGray(x, y - 1) - cp) >= 0)        sum += 1;
                g[sum]++;
            }
        }
        return new ImageHistogram(g);
    }
}