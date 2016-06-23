// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
// Copyright © Andrew Kirillov, 2007-2008
// andrew.kirillov at gmail.com
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
 * Canny edge detector.
 * The filter searches for objects' edges by applying Canny edge detector. The implementation follows Bill Green's Canny edge detection tutorial.
 * 
 * <p>The implemented canny edge detector has one difference with the above linked algorithm.
 * The difference is in hysteresis step, which is a bit simplified (getting faster as a result).
 * On the hysteresis step each pixel is compared with two threshold values: HighThreshold and LowThreshold.
 * If pixel's value is greater or equal to HighThreshold, then it is kept as edge pixel.
 * If pixel's value is greater or equal to LowThreshold, then it is kept as edge pixel only if there is at least one neighbouring pixel (8 neighbours are checked)
 * which has value greater or equal to HighThreshold; otherwise it is none edge pixel.
 * In the case if pixel's value is less than LowThreshold, then it is marked as none edge immediately. </p>
 * 
 * <p><li>Supported types: Grayscale.
 * <br><li>Coordinate System: Matrix.
 * 
 * @author Diego Catalano
 */
public class CannyEdgeDetector implements IApplyInPlace{
    
    private double sigma = 1.4D;
    private int size = 1;
    private int lowThreshold = 20;
    private int highThreshold = 100;

    /**
     * Get Low threshold.
     * Used for Hysteresis.
     * @return Low threshold.
     */
    public int getLowThreshold() {
        return lowThreshold;
    }

    /**
     * Set Low threshold.
     * @param lowThreshold Threshold value.
     */
    public void setLowThreshold(int lowThreshold) {
        this.lowThreshold = lowThreshold;
    }

    /**
     * Get High threshold.
     * Used for Hysteresis.
     * @return Threshold value.
     */
    public int getHighThreshold() {
        return highThreshold;
    }

    /**
     * Set High threshold.
     * @param highThreshold Threshold value.
     */
    public void setHighThreshold(int highThreshold) {
        this.highThreshold = highThreshold;
    }

    /**
     * Get Gaussian sigma.
     * @return Gaussian sigma.
     */
    public double getSigma() {
        return sigma;
    }

    /**
     * Set Gaussian sigma.
     * @param sigma Gaussian sigma.
     */
    public void setSigma(double sigma) {
        this.sigma = sigma;
    }

    /**
     * Get Gaussian kernel size.
     * @return Gaussian kernel size.
     */
    public int getSize() {
        return size;
    }

    /**
     * Set Gaussian kernel size.
     * @param size Gaussian kernel size.
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * Initialize a new instance of the CannyEdgeDetector class.
     */
    public CannyEdgeDetector() {}
    
    /**
     * Initialize a new instance of the CannyEdgeDetector class.
     * @param lowThreshold Low threshold. (Used for hysteresis).
     * @param highThreshold High Threshold. (Used for hysteresis).
     */
    public CannyEdgeDetector(int lowThreshold, int highThreshold){
       this.lowThreshold = lowThreshold;
       this.highThreshold = highThreshold;
    }
    
    /**
     * Initialize a new instance of the CannyEdgeDetector class.
     * @param lowThreshold Low threshold. (Used for hysteresis).
     * @param highThreshold High Threshold. (Used for hysteresis).
     * @param sigma Gaussian sigma.
     */
    public CannyEdgeDetector(int lowThreshold, int highThreshold, double sigma){
       this.lowThreshold = lowThreshold;
       this.highThreshold = highThreshold;
       this.sigma = sigma;
    }
    
    /**
     * Initialize a new instance of the CannyEdgeDetector class.
     * @param lowThreshold Low threshold. (Used for hysteresis).
     * @param highThreshold High Threshold. (Used for hysteresis).
     * @param sigma Gaussian sigma.
     * @param size Size of gaussian kernel.
     */
    public CannyEdgeDetector(int lowThreshold, int highThreshold, double sigma, int size){
       this.lowThreshold = lowThreshold;
       this.highThreshold = highThreshold;
       this.sigma = sigma;
       this.size = size;
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        if (fastBitmap.isGrayscale()){
            
            int width = fastBitmap.getWidth();
            int height = fastBitmap.getHeight();
            
            int gx, gy;
            double orientation, toAngle = 180.0 / Math.PI;
            float leftPixel = 0, rightPixel = 0;
            
            // STEP 1 - Apply Gaussian Blur
            FastBitmap blurredImage = new FastBitmap(fastBitmap);
            GaussianBlur g = new GaussianBlur(sigma, size);
            g.applyInPlace(blurredImage);
            
            int[] orients = new int[width * height];
            float[][] gradients = new float[width][height];
            float maxGradient = Float.NEGATIVE_INFINITY;
            
            // STEP 2 - calculate magnitude and edge orientation
            int p = 0;
            
            for (int x = 1; x < height - 1; x++) {
                for (int y = 1; y < width - 1; y++, p++) {
                    int p1 = blurredImage.getGray(x - 1, y + 1);
                    int p2 = blurredImage.getGray(x + 1, y + 1);
                    int p3 = blurredImage.getGray(x - 1, y - 1);
                    int p4 = blurredImage.getGray(x + 1, y - 1);
                    int p5 = blurredImage.getGray(x, y + 1);
                    int p6 = blurredImage.getGray(x, y - 1);
                    int p7 = blurredImage.getGray(x - 1, y);
                    int p8 = blurredImage.getGray(x + 1, y);
                    
                    gx = p1 + p2 - p3 - p4 + 2 * (p5 - p6);
                    
                    gy = p3 + p1 - p4 - p2 + 2 * (p7 - p8);
                    
                    
                    // get gradient value
                    gradients[y][x] = (float) Math.sqrt( gx * gx + gy * gy );
                    if ( gradients[y][x] > maxGradient )
                        maxGradient = gradients[y][x];

                    // --- get orientation
                    if ( gx == 0 )
                    {
                        // can not divide by zero
                        orientation = ( gy == 0 ) ? 0 : 90;
                    }
                    else
                    {
                        double div = (double) gy / gx;

                        // handle angles of the 2nd and 4th quads
                        if ( div < 0 )
                        {
                            orientation = 180 - Math.atan( -div ) * toAngle;
                        }
                        // handle angles of the 1st and 3rd quads
                        else
                        {
                            orientation = Math.atan( div ) * toAngle;
                        }

                        // get closest angle from 0, 45, 90, 135 set
                        if ( orientation < 22.5 )
                            orientation = 0;
                        else if ( orientation < 67.5 )
                            orientation = 45;
                        else if ( orientation < 112.5 )
                            orientation = 90;
                        else if ( orientation < 157.5 )
                            orientation = 135;
                        else orientation = 0;
                    }

                    // save orientation
                    orients[p] = (int)orientation;
                }
            }
            
            p = 0;
            
            // STEP 3 - suppres non maximums
            for (int x = 1; x < height - 1; x++) {
                for (int y = 1; y < width - 1; y++, p++) {
                    // get two adjacent pixels
                    switch ( orients[p] )
                    {
                        case 0:
                            leftPixel  = gradients[y - 1][x];
                            rightPixel = gradients[y + 1][x];
                            break;
                        case 45:
                            leftPixel  = gradients[y - 1][x + 1];
                            rightPixel = gradients[y + 1][x - 1];
                            break;
                        case 90:
                            leftPixel  = gradients[y][x + 1];
                            rightPixel = gradients[y][x - 1];
                            break;
                        case 135:
                            leftPixel  = gradients[y + 1][x + 1];
                            rightPixel = gradients[y - 1][x - 1];
                            break;
                    }
                    // compare current pixels value with adjacent pixels
                    if ( ( gradients[y][x] < leftPixel ) || ( gradients[y][x] < rightPixel ) )
                    {
                        fastBitmap.setGray(x, y, 0);
                    }
                    else
                    {
                        fastBitmap.setGray(x, y, (int)( gradients[y][x] / maxGradient * 255 ));
                    }
                }
            }
            
            // STEP 4 - Hysteresis Threshold
            HysteresisThreshold threshold = new HysteresisThreshold(lowThreshold, highThreshold);
            threshold.applyInPlace(fastBitmap);
            }
        else{
            throw new IllegalArgumentException("CannyEdgeDetector only works in grayscale images.");
        }
    }
}