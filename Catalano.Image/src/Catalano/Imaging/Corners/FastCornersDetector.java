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

package Catalano.Imaging.Corners;

import Catalano.Imaging.FastBitmap;
import java.util.List;

/**
 * Features from Accelerated Segment Test (FAST) corners detector.
 * @author Diego Catalano
 */
public class FastCornersDetector implements ICornersFeatureDetector{

    public static enum Algorithm {FAST_9, FAST_12};
    private int threshold = 20;
    private boolean suppress = true;
    private Algorithm algorithm = Algorithm.FAST_9;

    /**
     * Get Threshold.
     * 
     * A number denoting how much brighter or darker the pixels surrounding the pixel in question
     * should be in order to be considered a corner.
     * 
     * @return Threshold.
     */
    public int getThreshold() {
        return threshold;
    }

    /**
     * Set Threshold.
     * 
     * A number denoting how much brighter or darker the pixels surrounding the pixel in question
     * should be in order to be considered a corner.
     * 
     * @param threshold Threshold.
     */
    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    /**
     * Check if needs apply a non-maximum suppression algorithm on the results, to allow only maximal corners.
     * @return If true, allow only maximal corners, otherwise false.
     */
    public boolean isSuppressed() {
        return suppress;
    }

    /**
     * Set suppression if needs apply a non-maximum suppression algorithm on the results, to allow only maximal corners.
     * @param suppress If true, allow only maximal corners, otherwise false.
     */
    public void setSuppression(boolean suppress) {
        this.suppress = suppress;
    }

    /**
     * Get Fast algorithm.
     * @return Fast algorithm.
     */
    public Algorithm getAlgorithm() {
        return algorithm;
    }

    /**
     * Set Fast algorithm.
     * @param algorithm Fast algorithm.
     */
    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    /**
     * Initializes a new instance of the FastCornersDetector class.
     */
    public FastCornersDetector() {}
    
    /**
     * Initializes a new instance of the FastCornersDetector class.
     * @param threshold Threshold.
     */
    public FastCornersDetector(int threshold){
        this.threshold = threshold;
    }
    
    /**
     * Initializes a new instance of the FastCornersDetector class.
     * @param algorithm Algorithm.
     */
    public FastCornersDetector(Algorithm algorithm){
        this.algorithm = algorithm;
    }
    
    /**
     * Initializes a new instance of the FastCornersDetector class.
     * @param threshold Threshold.
     * @param suppress Supress.
     */
    public FastCornersDetector(int threshold, boolean suppress){
        this.threshold = threshold;
        this.suppress = suppress;
    }
    
    /**
     * Initializes a new instance of the FastCornersDetector class.
     * @param threshold Threshold.
     * @param suppress Supress.
     * @param algorithm Algorithm.
     */
    public FastCornersDetector(int threshold, boolean suppress, Algorithm algorithm){
        this.threshold = threshold;
        this.suppress = suppress;
        this.algorithm = algorithm;
    }
    
    @Override
    public List<FeaturePoint> ProcessImage(FastBitmap fastBitmap){
        
        switch (algorithm){
            case FAST_9:
                Fast9 fast9 = new Fast9(threshold, suppress);
                return fast9.ProcessImage(fastBitmap);
            case FAST_12:
                Fast12 fast12 = new Fast12(threshold, suppress);
                return fast12.ProcessImage(fastBitmap);
        }
        return null;
        
    }
}