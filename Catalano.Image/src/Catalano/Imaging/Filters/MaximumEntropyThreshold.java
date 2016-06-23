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

package Catalano.Imaging.Filters;

import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.IApplyInPlace;
import Catalano.Imaging.Tools.ImageHistogram;
import Catalano.Imaging.Tools.ImageStatistics;

/**
 * Automatic threshold based on the entropy.
 * 
 * @see OtsuThreshold
 * @author Diego Catalano
 */
public class MaximumEntropyThreshold implements IApplyInPlace{
    
    private boolean invert = false;

    /**
     * Initialize a new instance of the MaximumEntropyThreshold class.
     */
    public MaximumEntropyThreshold() {}
    
    /**
     * Initialize a new instance of the MaximumEntropyThreshold class.
     * @param invert All pixels with intensities equal or higher than threshold value are converted to black pixels. All other pixels with intensities below threshold value are converted to white pixels.
     */
    public MaximumEntropyThreshold(boolean invert){
        this.invert = invert;
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        int entropy = CalculateThreshold(fastBitmap);
        
        Threshold t = new Threshold(entropy, invert);
        t.applyInPlace(fastBitmap);
        
    }
    
    /**
     * Calculate binarization threshold for the given image.
     * @param fastBitmap FastBitmap.
     * @return Threshold value.
     */
    public int CalculateThreshold(FastBitmap fastBitmap){
        
        ImageStatistics stat = new ImageStatistics(fastBitmap);
        ImageHistogram hist = stat.getHistogramGray();
        int[] histogram = hist.getValues();
        
        // Normalize histogram, that is makes the sum of all bins equal to 1.
        double sum = 0;
        for (int i = 0; i < histogram.length; ++i) {
            sum += histogram[i];
        }
        if (sum == 0) {
            // This should not normally happen, but...
            throw new IllegalArgumentException("Empty histogram: sum of all bins is zero.");
        }

        double[] normalizedHist = new double[histogram.length];
        for (int i = 0; i < histogram.length; i++) {
            normalizedHist[i] = histogram[i] / sum;
        }

        //
        double[] pT = new double[histogram.length];
        pT[0] = normalizedHist[0];
        for (int i = 1; i < histogram.length; i++) {
            pT[i] = pT[i - 1] + normalizedHist[i];
        }

        // Entropy for black and white parts of the histogram
        final double epsilon = Double.MIN_VALUE;
        double[] hB = new double[histogram.length];
        double[] hW = new double[histogram.length];
        for (int t = 0; t < histogram.length; t++) {
            // Black entropy
            if (pT[t] > epsilon) {
            double hhB = 0;
            for (int i = 0; i <= t; i++) {
                if (normalizedHist[i] > epsilon) {
                hhB -= normalizedHist[i] / pT[t] * Math.log(normalizedHist[i] / pT[t]);
                }
            }
            hB[t] = hhB;
            } else {
            hB[t] = 0;
            }

            // White  entropy
            double pTW = 1 - pT[t];
            if (pTW > epsilon) {
            double hhW = 0;
            for (int i = t + 1; i < histogram.length; ++i) {
                if (normalizedHist[i] > epsilon) {
                hhW -= normalizedHist[i] / pTW * Math.log(normalizedHist[i] / pTW);
                }
            }
            hW[t] = hhW;
            } else {
            hW[t] = 0;
            }
        }

        // Find histogram index with maximum entropy
        double jMax = hB[0] + hW[0];
        int tMax = 0;
        for (int t = 1; t < histogram.length; ++t) {
            double j = hB[t] + hW[t];
            if (j > jMax) {
            jMax = j;
            tMax = t;
            }
        }

        return tMax;
    }
}