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
import Catalano.Statistics.Histogram;

/**
 * Horizontal intensity statistics.
 * @author Diego Catalano
 */
public class HorizontalIntensityStatistics {
    
    private Histogram red, green, blue, gray;

    /**
     * Initializes a new instance of the HorizontalIntensityStatistics class.
     * @param fastBitmap Image to be processed.
     */
    public HorizontalIntensityStatistics(FastBitmap fastBitmap) {
        ProcessImage(fastBitmap);
    }

    /**
     * Get gray histogram.
     * @return Histogram.
     */
    public Histogram getGray() {
        return gray;
    }

    /**
     * Get red histogram.
     * @return Histogram.
     */
    public Histogram getRed() {
        return red;
    }

    /**
     * Get green histogram.
     * @return Histogram.
     */
    public Histogram getGreen() {
        return green;
    }

    /**
     * Get blue histogram.
     * @return Histogram.
     */
    public Histogram getBlue() {
        return blue;
    }
    
    /**
     * Computes histograms.
     * @param fastBitmap Image.
     */
    private void ProcessImage(FastBitmap fastBitmap){
        
        int width = fastBitmap.getWidth();
        int height = fastBitmap.getHeight();
        
        if (fastBitmap.isGrayscale()) {
            int[] g = new int[width];
            for (int x = 0; x < height; x++) {
                for (int y = 0; y < width; y++) {
                    g[y] += fastBitmap.getGray(x, y); 
                }
            }
            gray = new Histogram(g);
        }
        if (fastBitmap.isRGB()) {
            int[] r = new int[width];
            int[] g = new int[width];
            int[] b = new int[width];
            for (int x = 0; x < height; x++) {
                for (int y = 0; y < width; y++) {
                    r[y] += fastBitmap.getRed(x, y); 
                    g[y] += fastBitmap.getGreen(x, y); 
                    b[y] += fastBitmap.getBlue(x, y); 
                }
            }
            red = new Histogram(r);
            green = new Histogram(g);
            blue = new Histogram(b);
        }
    }
}