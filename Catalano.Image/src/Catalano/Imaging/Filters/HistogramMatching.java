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

import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.IApplyInPlace;
import Catalano.Imaging.Tools.ImageStatistics;
import Catalano.Statistics.Histogram;

/**
 * Histogram Matching.
 * 
 * <para>It is possible to use histogram matching to balance detector responses as a relative detector calibration technique.
 * It can be used to normalize two images, when the images were acquired at the same local
 * illumination (such as shadows) over the same location, but by different sensors,
 * atmospheric conditions or global illumination.</para>
 * 
 * References: http://en.wikipedia.org/wiki/Histogram_matching
 * 
 * @author Diego Catalano
 */
public class HistogramMatching implements IApplyInPlace{
    
    private int[] gray;
    private int[] red;
    private int[] green;
    private int[] blue;

    /**
     * Initialize a new instance of the HistogramMatching class.
     * @param reference Image reference.
     */
    public HistogramMatching(FastBitmap reference) {
        Init(reference);
    }
    
    private void Init(FastBitmap reference){
        ImageStatistics stat = new ImageStatistics(reference);
        
        if (reference.isGrayscale()){
            this.gray = stat.getHistogramGray().getValues();
        }
        if (reference.isRGB()){
            this.red = stat.getHistogramRed().getValues();
            this.green = stat.getHistogramGreen().getValues();
            this.blue = stat.getHistogramBlue().getValues();
        }
    }
    
    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        ImageStatistics stat = new ImageStatistics(fastBitmap);
        
        if (fastBitmap.isGrayscale()){
            
            //Get histogram.
            int[] fGray = stat.getHistogramGray().getValues();
            
            //Compute original and reference histogram.
            int[] mh = Histogram.MatchHistograms(fGray, gray);
            
            int size = fastBitmap.getSize();
            
            for (int i = 0; i < size; i++) {
                int g = fastBitmap.getGray(i);
                g = mh[g];
                fastBitmap.setGray(i, g);
            }
        }
        
        else if (fastBitmap.isRGB()){
            
            //Gets histograms.
            int[] fRed = stat.getHistogramRed().getValues();
            int[] fGreen = stat.getHistogramGreen().getValues();
            int[] fBlue = stat.getHistogramBlue().getValues();
            
            //Compute Match histogram.
            int[] mhR = Histogram.MatchHistograms(fRed, red);
            int[] mhG = Histogram.MatchHistograms(fGreen, green);
            int[] mhB = Histogram.MatchHistograms(fBlue, blue);
            
            int size = fastBitmap.getSize();
            
            for (int i = 0; i < size; i++) {
                    
                int r = fastBitmap.getRed(i);
                int g = fastBitmap.getGreen(i);
                int b = fastBitmap.getBlue(i);

                r = mhR[r];
                g = mhG[g];
                b = mhB[b];

                fastBitmap.setRGB(i, r, g, b);
                    
            }
        }
        else{
            throw new IllegalArgumentException("Histogram Matching only works with Grayscale and RGB images.");
        }
    }
}