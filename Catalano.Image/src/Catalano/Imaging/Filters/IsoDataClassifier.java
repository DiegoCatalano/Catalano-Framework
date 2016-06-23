// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
// Copyright © Johannes Schindelin, 2013
// Johannes.Schindelin at gmx.de
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

/**
 * IsoData Classifier.
 * Calculates a classification based on the histogram of the image by generalizing the IsoData algorithm to more than two classes.
 * @author Diego Catalano
 */
public class IsoDataClassifier implements IApplyInPlace{
    
    int n;
    int[] histogram;

    /**
     * Initialize a new instance of the IsoDataClassifier class.
     */
    public IsoDataClassifier() {
        n = 3;
    }

    /**
     * Initialize a new instance of the IsoDataClassifier class.
     * @param n Classes.
     */
    public IsoDataClassifier(int n) {
        this.n = Math.min(255, n);
        this.n = Math.max(1, n);
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        if(fastBitmap.isGrayscale()){
            histogram = Histogram(fastBitmap);
            histogram = IsoData(histogram, n);
            
            int size = fastBitmap.getSize();
            
            for (int i = 0; i < size; i++) {
                fastBitmap.setGray(i, histogram[fastBitmap.getGray(i)]);
            }
        }
        else{
            throw new IllegalArgumentException("IsoData Classifier only works with grayscale image.");
        }
    }
    
    /**
     * Compute histogram.
     * @param fastBitmap Image to be processed.
     * @return Histogram.
     */
    private int[] Histogram(FastBitmap fastBitmap){
        int size = fastBitmap.getSize();
        int[] hist = new int[256];
        
        for (int i = 0; i < size; i++) {
            hist[fastBitmap.getGray(i)]++;
        }
        
        return hist;
    }
    
    /**
     * Compute IsoData algorithm.
     * @param histogram Histogram.
     * @param classes Classes.
     * @return IsoData histogram.
     */
    private int[] IsoData(int[] histogram, int classes){
        int[] result = new int[histogram.length];
        int total = 0;
        for (int i = 0; i < histogram.length; i++)            
            total += histogram[i] * i;
        int left = 0;
        for (int j = 0, i = 0; j < classes; j++) {
            int i2 = i, previousLeft = left, count = 0;
            while (i2 < histogram.length && left * classes / total < j + 1) {
                left += histogram[i2] * i2;
                count += histogram[i2++];
            }
            int v = count > 0 ? (left - previousLeft) / count : (i2 + i) / 2;
            while (i < i2)
                result[i++] = v;
        }
        return result;
    }
}