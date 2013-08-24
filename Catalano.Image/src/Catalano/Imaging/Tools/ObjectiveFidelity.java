// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2013
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

/**
 * Objective Fidelity Criteria.
 * 
 * The Objective criteria, although widely used, are not necessarily correlated with out perception of image quality. For instance,
 * an image with a low error as determined by an objective measure may actually look much worse than an image with a high error metric.
 * 
 * @author Diego Catalano
 */
public class ObjectiveFidelity {
    private FastBitmap original, reconstructed;

    /**
     * Initialize a new instance of the ObjectiveFidelity class.
     * @param original Original image.
     * @param reconstructed Reconstructed image.
     */
    public ObjectiveFidelity(FastBitmap original, FastBitmap reconstructed) {
        this.original = original;
        this.reconstructed = reconstructed;
        if ((original.getWidth() != reconstructed.getWidth()) || (original.getHeight() != reconstructed.getHeight())) {
            try {
                throw new IllegalArgumentException("The both images must be equal dimensions");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public int getTotalError(){
        int sumError = 0;
        for (int x = 0; x < original.getHeight(); x++) {
            for (int y = 0; y < original.getWidth(); y++) {
                sumError += reconstructed.getGray(x, y) - original.getGray(x, y);
            }
        }
        return sumError;
    }
    
    public double getErrorRMS(){
        int sumError = 0;
        double squareDiff;
        for (int x = 0; x < original.getHeight(); x++) {
            for (int y = 0; y < original.getWidth(); y++) {
                squareDiff = Math.pow(reconstructed.getGray(x, y) - original.getGray(x, y), 2);
                sumError += squareDiff;
            }
        }
        int size = 1/(original.getWidth() * original.getHeight());
        return Math.sqrt(size * sumError);
    }
    
    public double getSignalToNoiseRatioRMS(){
        int squareImg = 0;
        double squareRecon = 0;
        for (int x = 0; x < original.getHeight(); x++) {
            for (int y = 0; y < original.getWidth(); y++) {
                int g = reconstructed.getGray(x, y);
                squareRecon += g*g;
                squareImg += Math.pow(g - original.getGray(x, y), 2);
            }
        }
        return Math.sqrt(squareRecon/squareImg);
    }
    
    public double getSignalToNoiseRatioPEAK(int l){
        
        double sum = 0;
        for (int x = 0; x < original.getHeight(); x++) {
            for (int y = 0; y < original.getWidth(); y++) {
                sum += Math.pow(reconstructed.getGray(x, y) - original.getGray(x, y), 2);
            }
        }
        int size = original.getWidth() * original.getHeight();
        sum = (1/size) * sum;
        sum = l*l / sum;
        sum = 10 * Math.log10(sum);
        return sum;
    }
}