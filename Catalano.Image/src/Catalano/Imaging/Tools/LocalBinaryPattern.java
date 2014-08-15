// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2014
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

package Catalano.Imaging.Tools;

import Catalano.Imaging.FastBitmap;
import Catalano.Statistics.Histogram;

/**
 * Local binary patterns (LBP) is a type of feature used for classification in computer vision.
 * LBP was first described in 1994. It has since been found to be a powerful feature for texture classification.
 * @author Diego Catalano
 */
public class LocalBinaryPattern {

    /**
     * Initialize a new instance of the LocalBinaryPattern class.
     */
    public LocalBinaryPattern() {}
    
    /**
     * Process image.
     * @param fastBitmap Imae to be processed.
     * @return LBP Histogram.
     */
    public Histogram ProcessImage(FastBitmap fastBitmap){
        if (!fastBitmap.isGrayscale()) {
            try {
                throw new Exception("LBP works only with grayscale images.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        int width = fastBitmap.getWidth() - 1;
        int height = fastBitmap.getHeight() - 1;
        
        int sum;
        int[] g = new int[256];
        int gray;
        for (int x = 1; x < height; x++) {
            for (int y = 1; y < width; y++) {
                gray = fastBitmap.getGray(x, y);
                sum = 0;
                if (gray < fastBitmap.getGray(x - 1, y - 1))    sum += 128;
                if (gray < fastBitmap.getGray(x - 1, y))        sum += 64;
                if (gray < fastBitmap.getGray(x - 1, y + 1))    sum += 32;
                if (gray < fastBitmap.getGray(x, y + 1))        sum += 16;
                if (gray < fastBitmap.getGray(x + 1, y + 1))    sum += 8;
                if (gray < fastBitmap.getGray(x + 1, y))        sum += 4;
                if (gray < fastBitmap.getGray(x + 1, y - 1))    sum += 2;
                if (gray < fastBitmap.getGray(x, y - 1))        sum += 1;
                g[sum]++;
            }
        }
        return new Histogram(g);
    }
}