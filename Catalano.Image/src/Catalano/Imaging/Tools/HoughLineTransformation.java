// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
// Copyright © Adrian F Clark
// alien at essex.ac.uk
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

package Catalano.Imaging.Tools;

import Catalano.Core.IntPoint;
import Catalano.Imaging.FastBitmap;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Hough line transfomation.
 * @author Diego Catalano
 */
public class HoughLineTransformation {
    
    // The size of the neighbourhood in which to search for other local maxima 
    int radius = 4;
 
    // How many discrete values of theta shall we check? 
    final int maxTheta = 180;
 
    // Using maxTheta, work out the step 
    double thetaStep = Math.PI / maxTheta;
    
    int stepsPerDegree = 1;
    
    int minIntensity = 0;
    
    int maxIntensity = Integer.MAX_VALUE;
    
    boolean sort = true;
 
    // the width and height of the image 
    protected int width, height;
 
    // the hough array 
    protected int[][] houghArray;
 
    // the coordinates of the centre of the image 
    protected float centerX, centerY;
 
    // the height of the hough array 
    protected int houghHeight;
 
    // double the hough height (allows for negative numbers) 
    protected int doubleHeight;
 
    // the number of points that have been added 
    protected int numPoints;
 
    // cache of values of sin and cos for different theta values. Has a significant performance improvement. 
    private double[] sinCache;
    private double[] cosCache;

    /**
     * Initialize a new instance of the HoughLineTransformation class.
     */
    public HoughLineTransformation() {}
    
    /**
     * Initialize a new instance of the HoughLineTransformation class.
     * @param minIntensity Minimum intensity.
     */
    public HoughLineTransformation(int minIntensity) {
        this.minIntensity = Math.max(1,minIntensity);
    }
    
    /**
     * Initialize a new instance of the HoughLineTransformation class.
     * @param minIntensity Minimum intensity.
     * @param maxIntensity Minimum intensity.
     */
    public HoughLineTransformation(int minIntensity, int maxIntensity) {
        this.minIntensity = Math.max(1,minIntensity);
        this.maxIntensity = Math.max(1,maxIntensity);
    }
    
    /**
     * Initialize a new instance of the HoughLineTransformation class.
     * @param minIntensity Minimum intensity.
     * @param maxIntensity Minimum intensity.
     * @param sort Sort Hough lines per intensity.
     */
    public HoughLineTransformation(int minIntensity, int maxIntensity, boolean sort) {
        this.minIntensity = Math.max(1,minIntensity);
        this.maxIntensity = Math.max(1,maxIntensity);
        this.sort = sort;
    }

    /**
     * Get Radius.
     * @return Radius.
     */
    public int getRadius() {
        return radius;
    }

    /**
     * Set Radius.
     * @param radius Radius.
     */
    public void setRadius(int radius) {
        this.radius = radius;
    }

    /**
     * Get Intensity.
     * @return Intensity.
     */
    public int getIntensity() {
        return minIntensity;
    }

    /**
     * Set Intensity.
     * @param intensity Intensity.
     */
    public void setIntensity(int intensity) {
        this.minIntensity = Math.max(1,intensity);
    }

    public int getStepsPerDegree() {
        return stepsPerDegree;
    }

    public void setStepsPerDegree(int stepsPerDegree) {
        this.stepsPerDegree = stepsPerDegree;
        
        stepsPerDegree = Math.max( 1, Math.min( 10, stepsPerDegree ) );
        houghHeight = 180 * stepsPerDegree;
        thetaStep = Math.PI / houghHeight;

        // precalculate Sine and Cosine values
        sinCache = new double[houghHeight];
        cosCache = new double[houghHeight];

        for ( int i = 0; i < houghHeight; i++ )
        {
            sinCache[i] = Math.sin( i * thetaStep );
            cosCache[i] = Math.cos( i * thetaStep );
        }
    }
    
    private void init(){
        // Calculate the maximum height the hough array needs to have 
        houghHeight = (int) (Math.sqrt(2) * Math.max(height, width)) / 2; 
 
        // Double the height of the hough array to cope with negative r values 
        doubleHeight = 2 * houghHeight;
 
        // Create the hough array 
        houghArray = new int[maxTheta][doubleHeight];
 
        // Find edge points and vote in array 
        centerX = width / 2;
        centerY = height / 2;
 
        // Count how many points there are 
        numPoints = 0;
 
        // cache the values of sin and cos for faster processing 
        sinCache = new double[maxTheta];
        cosCache = sinCache.clone();
        for (int t = 0; t < maxTheta; t++) {
            double realTheta = t * thetaStep;
            sinCache[t] = Math.sin(realTheta);
            cosCache[t] = Math.cos(realTheta);
        }
    }
    
    /**
     * Process Image.
     * @param fastBitmap Image to be processed.
     */
    public void ProcessImage(FastBitmap fastBitmap) { 
        
        if (fastBitmap.isGrayscale()){
            this.width = fastBitmap.getWidth();
            this.height = fastBitmap.getHeight();

            init();
            // Now find edge points and update the hough array 
            for (int x = 0; x < height; x++) {
                for (int y = 0; y < width; y++) {
                    // Find non-black pixels
                    if (fastBitmap.getGray(x, y) == 255) {
                        addPoint(x, y);
                    }
                }
            }
        }
        else{
            try {
                throw new IllegalArgumentException("HoughLineTransformation only works with grayscale images.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void addEdgePoints(ArrayList<IntPoint> edgePoints){
        for (IntPoint point : edgePoints) {
            addPoint(point.x, point.y);
        }
    }
    
    private void addPoint(int x, int y) {
 
        // Go through each value of theta
        for (int t = 0; t < maxTheta; t++) {
 
            //Work out the r values for each theta step
            int r = (int) (((x - centerX) * cosCache[t]) + ((y - centerY) * sinCache[t]));
 
            // this copes with negative values of r
            r += houghHeight;
 
            if (r < 0 || r >= doubleHeight) continue;
 
            // Increment the hough array
            houghArray[t][r]++;
 
        }
 
        numPoints++;
    }
    
    public ArrayList<HoughLine> getLines() {
 
        // Initialise the vector of lines that we'll return 
        ArrayList<HoughLine> lines = new ArrayList<HoughLine>(); 
 
        // Only proceed if the hough array is not empty 
        if (numPoints == 0) return lines;
        
        // Used for set relative intensity.
        double max = getMaximumValue();
        
        // Search for local peaks above threshold to draw 
        for (int t = 0; t < maxTheta; t++) { 
            loop: 
            for (int r = radius; r < doubleHeight - radius; r++) { 
 
                // Only consider points above threshold 
                if (houghArray[t][r] > minIntensity && houghArray[t][r] < maxIntensity) { 
 
                    int peak = houghArray[t][r]; 
                    
 
                    // Check that this peak is indeed the local maxima 
                    for (int dx = -radius; dx <= radius; dx++) {
                        for (int dy = -radius; dy <= radius; dy++) {
                            int dt = t + dx;
                            int dr = r + dy;
                            if (dt < 0) dt = dt + maxTheta;
                            else if (dt >= maxTheta) dt = dt - maxTheta;
                            if (houghArray[dt][dr] > peak) {
                                // found a bigger point nearby, skip 
                                continue loop;
                            }
                        }
                    }
 
                    // calculate the true value of theta 
                    double theta = t * thetaStep;
 
                    // add the line to the vector 
                    lines.add(new HoughLine(theta, r, peak, (double)peak / max));
 
                }
            }
        }
        if (sort) Collections.sort(lines);
        return lines;
    }
    
    /**
     * Maximum value.
     * @return Maximum value.
     */
    private int getMaximumValue() {
        int max = 0;
        for (int t = 0; t < maxTheta; t++) {
            for (int r = 0; r < doubleHeight; r++) {
                if (houghArray[t][r] > max) {
                    max = houghArray[t][r];
                }
            }
        }
        return max;
    }
    
    public FastBitmap getHoughArrayImage() {
        int max = getMaximumValue();
        FastBitmap fastBitmap = new FastBitmap(maxTheta, doubleHeight);
        for (int t = 0; t < maxTheta; t++) {
            for (int r = 0; r < doubleHeight; r++) {
                double value = 255 * ((double) houghArray[t][r]) / max;
                int v = 255 - (int) value;
                fastBitmap.setRGB(r, t, v, v, v);
            }
        }
        return fastBitmap;
    }
}