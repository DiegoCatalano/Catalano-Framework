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
import java.util.ArrayList;

/**
 * Histogram equalization filter.
 * <br /> The filter does histogram equalization increasing local contrast in images. The effect of histogram equalization can be better seen on images, where pixel values have close contrast values. Through this adjustment, pixels intensities can be better distributed on the histogram. This allows for areas of lower local contrast to gain a higher contrast without affecting the global contrast.
 * @author Diego Catalano
 */
public class HistogramEqualization implements IApplyInPlace{
    
    /**
     * Initialize a new instance of the HistogramEqualization class.
     */
    public HistogramEqualization() {}
    
    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
 
        // Get the Lookup table for histogram equalization
        ArrayList<int[]> histLUT = histogramEqualizationLUT(fastBitmap);

        if (fastBitmap.isGrayscale()) {
            int size = fastBitmap.getSize();
            int gray;
            for (int i = 0; i < size; i++) {
                //Get pixel gray
                gray = fastBitmap.getGray(i);

                //Set new pixel values using the histogram lookup table
                gray = histLUT.get(0)[gray];

                //Write pixels into image
                fastBitmap.setGray(i, gray);
            }
        }
        else if (fastBitmap.isRGB()){
            int size = fastBitmap.getSize();
            int red;
            int green;
            int blue;
            for (int i = 0; i < size; i++) {
                // Get pixels by R, G, B
                red = fastBitmap.getRed(i);
                green = fastBitmap.getGreen(i);
                blue = fastBitmap.getBlue(i);

                // Set new pixel values using the histogram lookup table
                red = histLUT.get(0)[red];
                green = histLUT.get(1)[green];
                blue = histLUT.get(2)[blue];

                // Write pixels into image
                fastBitmap.setRGB(i, red, green, blue);
            }
        }
    }
    
    /**
     * Create LUT for Histogram Equalization
     * @param fastBitmap Image to be processed.
     * @return LUT.
     */
    private ArrayList<int[]> histogramEqualizationLUT(FastBitmap fastBitmap) {
 
        ImageStatistics stat = new ImageStatistics(fastBitmap);
        ArrayList<int[]> imageHist = new ArrayList<int[]>();
        ArrayList<int[]> imageLUT;

        // Calculate the scale factor
        float scale_factor = (float) (255.0 / (fastBitmap.getWidth() * fastBitmap.getHeight()));

        if (fastBitmap.isGrayscale()) {
            imageHist.add(stat.getHistogramGray().getValues());
            // Lookup table
            imageLUT = new ArrayList<int[]>();

            int[] grayhistogram = new int[256];
            for (int i = 0; i < 256; i++) grayhistogram[i] = 0;

            long sumgray = 0;

            for (int i = 0; i < 256; i++) {
                sumgray += imageHist.get(0)[i];
                int valgray = (int)(sumgray * scale_factor);
                if (valgray > 255) {
                    grayhistogram[i] = 255;
                }
                else{
                    grayhistogram[i] = valgray;
                }
            }
            imageLUT.add(grayhistogram);
            return imageLUT;

        }
        else{
            imageHist.add(stat.getHistogramRed().getValues());
            imageHist.add(stat.getHistogramGreen().getValues());
            imageHist.add(stat.getHistogramBlue().getValues());
            // Lookup table
            imageLUT = new ArrayList<int[]>();

            //fill lookup table
            int[] rhistogram = new int[256];
            int[] ghistogram = new int[256];
            int[] bhistogram = new int[256];
            for(int i=0; i<256; i++){ 
                rhistogram[i] = 0;
                ghistogram[i] = 0;
                bhistogram[i] = 0;
            }
            long sumr = 0;
            long sumg = 0;
            long sumb = 0;

            for(int i=0; i<256; i++) {
                sumr += imageHist.get(0)[i];
                int valr = (int) (sumr * scale_factor);
                if(valr > 255) {
                    rhistogram[i] = 255;
                }
                else rhistogram[i] = valr;

                sumg += imageHist.get(1)[i];
                int valg = (int) (sumg * scale_factor);
                if(valg > 255) {
                    ghistogram[i] = 255;
                }
                else ghistogram[i] = valg;

                sumb += imageHist.get(2)[i];
                int valb = (int) (sumb * scale_factor);
                if(valb > 255) {
                    bhistogram[i] = 255;
                }
                else bhistogram[i] = valb;
            }

            imageLUT.add(rhistogram);
            imageLUT.add(ghistogram);
            imageLUT.add(bhistogram);

            return imageLUT;
        }
    }
}