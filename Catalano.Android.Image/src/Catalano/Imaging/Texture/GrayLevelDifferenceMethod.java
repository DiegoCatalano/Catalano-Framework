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

package Catalano.Imaging.Texture;

import Catalano.Imaging.Tools.*;
import Catalano.Imaging.FastBitmap;
import Catalano.Statistics.Histogram;

/**
 * Gray Level Difference Method (GLDM).
 * @author Diego Catalano
 */
public class GrayLevelDifferenceMethod {
    
    /**
     * Degree to perform the Run length.
     */
    public static enum Degree{

        /**
         * 0 Degree.
         */
        Degree_0,

        /**
         * 45 Degree.
         */
        Degree_45,

        /**
         * 90 Degree.
         */
        Degree_90,

        /**
         * 135 Degree.
         */
        Degree_135 };
    
    private Degree degree;
    private boolean autoGray = true;

    /**
     * Verify Automatic gray.
     * @return True if need to find maximum gray in current image, otherwise is set 255.
     */
    public boolean isAutoGray() {
        return autoGray;
    }

    /**
     * Set Automatic gray.
     * @param autoGray True if need to find maximum gray in current image, otherwise is set 255.
     */
    public void setAutoGray(boolean autoGray) {
        this.autoGray = autoGray;
    }

    /**
     * Get Degree.
     * @return Degree.
     */
    public Degree getDegree() {
        return degree;
    }

    /**
     * Set Degree.
     * @param degree Degree.
     */
    public void setDegree(Degree degree) {
        this.degree = degree;
    }

    /**
     * Initialize a new instance of the GrayLevelDifferenceMethod class.
     * @param degree Degree.
     */
    public GrayLevelDifferenceMethod(Degree degree) {
        this.degree = degree;
    }
    
    /**
     * Initialize a new instance of the GrayLevelDifferenceMethod class.
     * @param degree Degree.
     * @param autoGray Automatic gray.
     */
    public GrayLevelDifferenceMethod(Degree degree, boolean autoGray) {
        this.degree = degree;
        this.autoGray = autoGray;
    }
    
    /**
     * Compute GLDM.
     * @param fastBitmap Image to be processed.
     * @return GLDM histogram.
     */
    public Histogram Compute(FastBitmap fastBitmap){
        
        int maxGray = 255;
        if (autoGray) maxGray = ImageStatistics.Maximum(fastBitmap);
        
        int height = fastBitmap.getHeight();
        int width = fastBitmap.getWidth();
        
        int[] hist = new int[maxGray + 1];
        
        switch(degree){
            case Degree_0:
                for (int i = 0; i < height; i++) {
                    for (int j = 1; j < width; j++) {
                        int g1 = fastBitmap.getGray(i, j - 1);
                        int g2 = fastBitmap.getGray(i, j);
                        hist[Math.abs(g1 - g2)]++;
                    }
                }
            break;
            
            case Degree_45:
                for (int x = 1; x < height; x++) {
                    for (int y = 0; y < width - 1; y++) {
                        int g1 = fastBitmap.getGray(x, y);
                        int g2 = fastBitmap.getGray(x - 1, y + 1);
                        hist[Math.abs(g1 - g2)]++;
                    }
                }
            break;
                
            case Degree_90:
                for (int i = 1; i < height; i++) {
                    for (int j = 0; j < width; j++) {
                        int g1 = fastBitmap.getGray(i - 1, j);
                        int g2 = fastBitmap.getGray(i, j);
                        hist[Math.abs(g1 - g2)]++;
                    }
                }
            break;
            
            case Degree_135:
                for (int x = 1; x < height; x++) {
                    int steps = width - 1;
                    for (int y = 0; y < width - 1; y++) {
                        int g1 = fastBitmap.getGray(x, steps - y);
                        int g2 = fastBitmap.getGray(x - 1, steps -1 - y);
                        hist[Math.abs(g1 - g2)]++;
                    }
                }
            break;
        }
        return new Histogram(hist);
    }
}