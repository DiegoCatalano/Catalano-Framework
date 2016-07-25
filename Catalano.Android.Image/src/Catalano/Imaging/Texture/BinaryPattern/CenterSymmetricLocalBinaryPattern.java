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
 * Center symmetric local binary patterns (CS-LBP) is a type of feature used for classification in computer vision.
 * @author Diego Catalano
 */
public class CenterSymmetricLocalBinaryPattern implements IBinaryPattern{
    
    private double threshold;

    /**
     * Get threshold.
     * @return Threshold value.
     */
    public double getThreshold() {
        return threshold;
    }

    /**
     * Set threshold.
     * @param threshold Threshold value.
     */
    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    /**
     * Initializes a new instance of the CenterSymmetricLocalBinaryPattern class.
     */
    public CenterSymmetricLocalBinaryPattern() {
        this(0.1);
    }
    
    /**
     * Initializes a new instance of the CenterSymmetricLocalBinaryPattern class.
     * @param threshold Threshold value.
     */
    public CenterSymmetricLocalBinaryPattern(double threshold){
        this.threshold = threshold;
    }

    @Override
    public ImageHistogram ComputeFeatures(FastBitmap fastBitmap) {
        
        if(!fastBitmap.isGrayscale())
            throw new IllegalArgumentException("CS-LBP only works in grayscale images.");
        
        int width = fastBitmap.getWidth() - 1;
        int height = fastBitmap.getHeight() - 1;
        
        int sum;
        int[] g = new int[16];
        for (int x = 1; x < height; x++) {
            for (int y = 1; y < width; y++) {
                sum = 0;
                threshold = fastBitmap.getGray(x, y);
                if (Math.abs(fastBitmap.getGray(x - 1, y - 1) - fastBitmap.getGray(x + 1, y + 1)) >= threshold)    sum += 8;
                if (Math.abs(fastBitmap.getGray(x - 1, y) - fastBitmap.getGray(x + 1, y)) >= threshold)            sum += 4;
                if (Math.abs(fastBitmap.getGray(x - 1, y + 1) - fastBitmap.getGray(x + 1, y - 1)) >= threshold)    sum += 2;
                if (Math.abs(fastBitmap.getGray(x, y + 1) - fastBitmap.getGray(x, y - 1)) >= threshold)            sum += 1;
                g[sum]++;
            }
        }
        return new ImageHistogram(g);
    }
}