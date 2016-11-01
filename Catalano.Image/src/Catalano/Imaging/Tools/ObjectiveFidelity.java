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

package Catalano.Imaging.Tools;

import Catalano.Imaging.FastBitmap;

/**
 * Objective Fidelity Criteria.
 * 
 * The Objective criteria, although widely used, are not necessarily correlated with out perception of image quality. For instance,
 * an image with a low error as determined by an objective measure may actually look much worse than an image with a high error metric.
 * References: http://uotechnology.edu.iq/sweit/lecture%202013n/4th%20Image%20Processing%20_Lectures/DIP_Lecture13.pdf
 * 
 * @author Diego Catalano
 */
public class ObjectiveFidelity {
    private FastBitmap original, reconstructed;

    /**
     * Get Original image.
     * @return Original image.
     */
    public FastBitmap getOriginalImage() {
        return original;
    }

    /**
     * Set Original image.
     * @param original Original image.
     */
    public void setOriginalImage(FastBitmap original) {
        this.original = original;
    }

    /**
     * Get Reconstructed image.
     * @return Reconstructed image.
     */
    public FastBitmap getReconstructedImage() {
        return reconstructed;
    }

    /**
     * Set Reconstructed Image.
     * @param reconstructed Reconstructed image.
     */
    public void setReconstructedImage(FastBitmap reconstructed) {
        this.reconstructed = reconstructed;
    }

    /**
     * Initialize a new instance of the ObjectiveFidelity class.
     * @param original Original image.
     * @param reconstructed Reconstructed image.
     */
    public ObjectiveFidelity(FastBitmap original, FastBitmap reconstructed) {
        this.original = original;
        this.reconstructed = reconstructed;
        if ((original.getWidth() != reconstructed.getWidth()) || (original.getHeight() != reconstructed.getHeight()))
            throw new IllegalArgumentException("The both images must be equal dimensions");
        
        if(original.getColorSpace() != reconstructed.getColorSpace())
            throw new IllegalArgumentException("The both images must be the same pixel format.");
    }
    
    /**
     * Calculate total error.
     * @return Total error.
     */
    public int getTotalError(){
        int sumError = 0;
        for (int x = 0; x < original.getHeight(); x++) {
            for (int y = 0; y < original.getWidth(); y++) {
                sumError += Math.abs(reconstructed.getGray(x, y) - original.getGray(x, y));
            }
        }
        return sumError;
    }
    
    /**
     * Calculate mean absolute error.
     * @return Mean absolute error.
     */
    public double getMAE(){
        double sumError = 0;
        for (int x = 0; x < original.getHeight(); x++) {
            for (int y = 0; y < original.getWidth(); y++) {
                sumError += Math.abs(reconstructed.getGray(x, y) - original.getGray(x, y));
            }
        }
        return (1/(double)(original.getWidth() * original.getHeight())) * sumError;
    }
    
    /**
     * Calculate Root mean square Error.
     * @return MSE.
     */
    public double getMSE(){
        int sumError = 0;
        double squareDiff;
        for (int x = 0; x < original.getHeight(); x++) {
            for (int y = 0; y < original.getWidth(); y++) {
                squareDiff = Math.pow(reconstructed.getGray(x, y) - original.getGray(x, y), 2);
                sumError += squareDiff;
            }
        }
        double size = 1D/(double)(original.getWidth() * original.getHeight());
        return Math.sqrt(size * sumError);
    }
    
    /**
     * Calculate Mean square signal-to-noise ratio.
     * @return SNR.
     */
    public double getSNR(){
        double squareImg = 0;
        double squareRecon = 0;
        for (int x = 0; x < original.getHeight(); x++) {
            for (int y = 0; y < original.getWidth(); y++) {
                int g = reconstructed.getGray(x, y);
                squareRecon += g*g;
                squareImg += Math.pow(g - original.getGray(x, y), 2);
            }
        }
        if(squareImg == 0) return 0;
        return Math.sqrt(squareRecon/squareImg);
    }
    
    /**
     * Derivative Signal noise ratio.
     * D-SNR consider the sentivity of the human eye to the edges.
     * Reference: A New Objective Fidelity Criterion For Image Processing: Derivative SNR - Hakki Tarkan Yalazan, and Melek D. Yucel.
     * @return D-SNR.
     */
    public double getDSNR(){
        
        int w = original.getWidth();
        int h = original.getHeight();
        
        double sumGradO = 0;
        double sumGradDiff = 0;
        for (int i = 0; i < h - 1; i++) {
            for (int j = 0; j < w - 1; j++) {
                int gradO = Math.abs(original.getGray(i, j) - original.getGray(i + 1, j)) + Math.abs(original.getGray(i, j) - original.getGray(i, j + 1));
                sumGradO += gradO * gradO;
                
                int gradR = Math.abs(reconstructed.getGray(i, j) - reconstructed.getGray(i + 1, j)) + Math.abs(reconstructed.getGray(i, j) - reconstructed.getGray(i, j + 1));
                sumGradDiff += Math.pow(gradO - gradR, 2);
            }
        }
        
        if(sumGradDiff == 0) return 0;
        double r = sumGradO / sumGradDiff;
        return 10 * Math.log10(r);
    }
    
    /**
     * Calculate PEAK signal-to-noise ratio.
     * @return PSNR.
     */
    public double getPSNR(){
        return getPSNR(256);
    }
    
    
    /**
     * Calculate PEAK signal-to-noise ratio.
     * @param l Number of gray levels.
     * @return PSNR.
     */
    public double getPSNR(int l){
        
        double sum = 0;
        for (int x = 0; x < original.getHeight(); x++) {
            for (int y = 0; y < original.getWidth(); y++) {
                sum += Math.pow(reconstructed.getGray(x, y) - original.getGray(x, y), 2);
            }
        }
        if(sum == 0) return 0;
        int size = original.getWidth() * original.getHeight();
        sum = (1D/(double)size) * sum;
        sum = l*l / sum;
        sum = 10D * Math.log10(sum);
        return sum;
    }
    
    /**
     * Calculate Universal Quality Index.
     * References: Wang, Zhou, and Alan C. Bovik. "A universal image quality index." Signal Processing Letters, IEEE 9.3 (2002): 81-84.
     * 
     * @return Universal Image Quality Index.
     */
    public double getUniversalQualityIndex(){
        
        double xMean = 0;
        double yMean = 0;
        
        //Calculate mean
        int width = original.getWidth();
        int height = original.getHeight();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                xMean += original.getGray(i, j);
                yMean += reconstructed.getGray(i, j);
            }
        }
        
        xMean /= (double)(width*height);
        yMean /= (double)(width*height);
        
        //Calculate std deviation
        double xStd = 0;
        double yStd = 0;
        double xyStd = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                xStd += Math.pow(original.getGray(i, j) - xMean, 2);
                yStd += Math.pow(reconstructed.getGray(i, j) - yMean, 2);
                xyStd += (original.getGray(i, j) - xMean) * (reconstructed.getGray(i, j) - yMean);
            }
        }
        
        xStd /= (double)(width*height - 1);
        yStd /= (double)(width*height - 1);
        xyStd /= (double)(width*height - 1);
        
        return (4 * xyStd * xMean * yMean) / ((xStd + yStd) * (xMean*xMean + yMean*yMean));
        
    }
}