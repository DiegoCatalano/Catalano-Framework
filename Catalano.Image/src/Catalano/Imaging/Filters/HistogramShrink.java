// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
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

import Catalano.Imaging.Tools.ImageStatistics;
import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.IApplyInPlace;

/**
 * Decrease image contrast by compressing the gray values.
 * @see Computer Imaging - Scott E. Umbaugh - Chapter 8 - p. 353
 * @author Diego Catalano
 */
public class HistogramShrink implements IApplyInPlace{
    private int max = 255, min = 0;

    /**
     * Initialize a new instance of the HistogramShrink class.
     */
    public HistogramShrink() {}

    /**
     * Initialize a new instance of the HistogramShrink class.
     * @param min Correspond to the minimum desired in the compressed histogram. Range[0..255].
     * @param max Correspond to the maximum desired in the compressed histogram. Range[0..255].
     */
    public HistogramShrink(int min, int max) {
        this.min = min;
        this.max = max;
    }

    /**
     * Maximum desired in the compressed histogram.
     * @return Maximum.
     */
    public int getMax() {
        return max;
    }

    /**
     * Maximum desired in the compressed histogram.
     * @param max Maximum. Range[0..255].
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * Minimum desired in the compressed histogram.
     * @return Minimum.
     */
    public int getMin() {
        return min;
    }

    /**
     * Minimum desired in the compressed histogram.
     * @param min Minimum. Range[0..255].
     */
    public void setMin(int min) {
        this.min = min;
    }
    
    @Override
    public void applyInPlace(FastBitmap fastBitmap){
        
        ImageStatistics stat = new ImageStatistics(fastBitmap);
        
        if (fastBitmap.isGrayscale()) {
            float grayMax = stat.getHistogramGray().getMax();
            float grayMin = stat.getHistogramGray().getMin();

            float gray; 
            float shrink;
            
            int size = fastBitmap.getSize();
            for (int i = 0; i < size; i++) {
                gray = fastBitmap.getGray(i);
                shrink = (((max - min)/(grayMax - grayMin)) * (gray - grayMin)) + min;
                fastBitmap.setGray(i, (int)shrink);
            }
        }
        else{
            float redMax = stat.getHistogramRed().getMax();
            float greenMax = stat.getHistogramGreen().getMax();
            float blueMax = stat.getHistogramBlue().getMax();
            
            float redMin = stat.getHistogramRed().getMin();
            float greenMin = stat.getHistogramGreen().getMin();
            float blueMin = stat.getHistogramBlue().getMin();

            float r,g,b; 
            float shrinkRed,shrinkGreen,shrinkBlue;
            
            int size = fastBitmap.getSize();
            for (int i = 0; i < size; i++) {
                r = fastBitmap.getRed(i);
                g = fastBitmap.getGreen(i);
                b = fastBitmap.getBlue(i);

                shrinkRed = (((max - min)/(redMax - redMin)) * (r - redMin)) + min;
                shrinkGreen = (((max - min)/(greenMax - greenMin)) * (g - greenMin)) + min;
                shrinkBlue = (((max - min)/(blueMax - blueMin)) * (b - blueMin)) + min;

                fastBitmap.setRGB(i, (int)shrinkRed, (int)shrinkGreen, (int)shrinkBlue);
            }
        }
    }
}