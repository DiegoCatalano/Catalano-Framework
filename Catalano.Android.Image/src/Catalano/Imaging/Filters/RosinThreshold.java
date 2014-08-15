// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2014
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

import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.IBaseInPlace;
import Catalano.Imaging.Tools.ImageStatistics;
import Catalano.Math.Matrix;
import Catalano.Statistics.Histogram;

/**
 * Rosin Threshold.
 * 
 * <para> Unimodal thresholding is an algorithm for automatic image threshold selection in image processing.
 * Most threshold selection algorithms assume that the intensity histogram is multi-modal; 
 * typically bimodal. However, some types of images are essentially unimodal since a much
 * larger proportion of just one class of pixels (e.g. the background) is present in the image,
 * and dominates the histogram. <para>
 * 
 * <para> Rosin Threshod uses Maximum deviation algorithm: a straight line is drawn
 * from the histogram peak to the end of the tail, and the
 * threshold is selected at the point of the histogram furthest from the straight line </para>
 * 
 * @author Diego Catalano
 */
public class RosinThreshold implements IBaseInPlace{

    /**
     * Initialize a new instance of the RosinThreshold class.
     */
    public RosinThreshold() {}

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        if(fastBitmap.isGrayscale()){
            int value = CalculateThreshold(fastBitmap);
            
            Threshold t = new Threshold(value);
            t.applyInPlace(fastBitmap);
            
        }
        else{
            throw new IllegalArgumentException("Rosin Threshold only works in grayscale images.");
        }
    }
    
    /**
     * Calculate binarization threshold for the given image.
     * @param fastBitmap FastBitmap.
     * @return Threshold value.
     */
    public int CalculateThreshold(FastBitmap fastBitmap){
        
        if(fastBitmap.isGrayscale()) {
            
            ImageStatistics stat = new ImageStatistics(fastBitmap);
            Histogram hist = stat.getHistogramGray();
            
            int[] values = hist.getValues();
            
            int maxIndex = Matrix.MaxIndex(values);
            int maxValue = values[maxIndex];

            int lastIndex = maxIndex;
            for (int i = lastIndex; i < values.length; i++) {
                if (values[i] > 0){
                    lastIndex = i;
                }
            }

            int lastValue = values[lastIndex];

            double d = Catalano.Math.Distance.Euclidean(maxIndex, maxValue, lastIndex, lastValue);

            int threshold = lastIndex;
            if (d != 0){
                
                double t = -1;
                
                for (int i = maxIndex; i < lastIndex; i++) {
                    
                    int tempIndex = i;
                    int tempVal = values[i];
                    
                    double p = ((lastIndex - maxIndex) * (maxValue - tempVal) - (maxIndex - tempIndex) * (lastValue - maxValue));
                    p /= d;
                    
                    if ((p > t) && (values[i] > 0)){
                        t = (int)p;
                        threshold = i;
                    }
                }
            }
            return threshold;
        }
        else{
            throw new IllegalArgumentException("Rosin Threshold only works in grayscale images.");
        }
    }
}