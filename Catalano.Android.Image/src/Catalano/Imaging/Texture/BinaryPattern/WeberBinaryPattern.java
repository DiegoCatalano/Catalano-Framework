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
 * Weber binary patterns (WBP) is a type of feature used for classification in computer vision.
 * WBP was first described in 2015. It has since been found to be a powerful feature for texture classification.
 * 
 * @author Diego Catalano
 */
public class WeberBinaryPattern implements IBinaryPattern{
    
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
    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    /**
     * Initialize a new instance of the WeberBinaryPattern class.
     * Default threshold: -0.2
     */
    public WeberBinaryPattern() {
        this(-0.2);
    }

    /**
     * Initialize a new instance of the WeberBinaryPattern class.
     * @param threshold Threshold.
     */
    public WeberBinaryPattern(double threshold) {
        this.threshold = threshold;
    }

    @Override
    public ImageHistogram ComputeFeatures(FastBitmap fastBitmap) {
        if (!fastBitmap.isGrayscale())
            throw new IllegalArgumentException("WBP works only with grayscale images.");
        
        int width = fastBitmap.getWidth() - 1;
        int height = fastBitmap.getHeight() - 1;
        
        int sum;
        int[] g = new int[256];
        double cp;
        for (int x = 1; x < height; x++) {
            for (int y = 1; y < width; y++) {
                cp = fastBitmap.getGray(x, y);
                sum = 0;
                if (((fastBitmap.getGray(x - 1, y - 1) - cp) / cp) > threshold)    sum += 128;
                if (((fastBitmap.getGray(x - 1, y) - cp) / cp) > threshold)        sum += 64;
                if (((fastBitmap.getGray(x - 1, y + 1) - cp) / cp) > threshold)    sum += 32;
                if (((fastBitmap.getGray(x, y + 1) - cp) / cp) > threshold)        sum += 16;
                if (((fastBitmap.getGray(x + 1, y + 1) - cp) / cp) > threshold)    sum += 8;
                if (((fastBitmap.getGray(x + 1, y) - cp) / cp) > threshold)        sum += 4;
                if (((fastBitmap.getGray(x + 1, y - 1) - cp) / cp) > threshold)    sum += 2;
                if (((fastBitmap.getGray(x, y - 1) - cp) / cp) > threshold)        sum += 1;
                g[sum]++;
            }
        }
        return new ImageHistogram(g);
    }
}