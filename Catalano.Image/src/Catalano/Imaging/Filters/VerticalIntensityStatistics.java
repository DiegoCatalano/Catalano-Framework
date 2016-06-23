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
 * Vertical intensity statistics.
 * @author Diego Catalano
 */
public class VerticalIntensityStatistics {
    
    private Histogram red, green, blue, gray;

    /**
     * Initializes a new instance of the VerticalIntensityStatistics class.
     * @param fastBitmap Image to be processed.
     */
    public VerticalIntensityStatistics(FastBitmap fastBitmap) {
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
        
        int size = fastBitmap.getSize();
        int height = fastBitmap.getHeight();
        
        if (fastBitmap.isGrayscale()) {
            int[] g = new int[height];
            for (int x = 0; x < size; x++) {
                g[x] += fastBitmap.getGray(x); 
            }
            gray = new Histogram(g);
        }
        if (fastBitmap.isRGB()) {
            int[] r = new int[height];
            int[] g = new int[height];
            int[] b = new int[height];
            for (int x = 0; x < height; x++) {
                r[x] += fastBitmap.getRed(x); 
                g[x] += fastBitmap.getGreen(x); 
                b[x] += fastBitmap.getBlue(x);
            }
            red = new Histogram(r);
            green = new Histogram(g);
            blue = new Histogram(b);
        }
    }
}
