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

package Catalano.Imaging.Tools;

import Catalano.Imaging.FastBitmap;

/**
 * Color Moments.
 * 
 * Color moments are measures that can be used differentiate images based on their features of color.
 * These moments provide a measurement for color similarity between images.
 * These values of similarity can then be compared to the values of images indexed in a database for tasks like image retrieval.
 * Reference: http://homepages.inf.ed.ac.uk/rbf/CVonline/LOCAL_COPIES/AV0405/KEEN/av_as2_nkeen.pdf
 * 
 * @author Diego Catalano
 */
public class ColorMoments {
    
    private double[] weight = {1, 1, 1};

    /**
     * Get weight.
     * @return Weight.
     */
    public double[] getWeight() {
        return weight;
    }

    /**
     * Set weight.
     * @param weight Weight.
     */
    public void setWeight(double[] weight) {
        this.weight = weight;
    }

    /**
     * Initialize a new instance of the ColorMoments class.
     */
    public ColorMoments() {}
    
    /**
     * Initialize a new instance of the ColorMoments class.
     * @param weight1 Weight 1.
     * @param weight2 Weight 2.
     * @param weight3 Weight 3.
     */
    public ColorMoments(double weight1, double weight2, double weight3){
        this.weight = new double[] {weight1, weight2, weight3};
    }
    
    /**
     * Compute moment.
     * @param fastBitmap1 Image to be processed.
     * @param fastBitmap2 Image to be processed.
     * @return Moment.
     */
    public double Compute(FastBitmap fastBitmap1, FastBitmap fastBitmap2){
        
        double[][] momentA = ComputeMatrixMoment(fastBitmap1);
        double[][] momentB = ComputeMatrixMoment(fastBitmap2);
        
        return Compute(momentA, momentB);
        
    }
    
    /**
     * Compute moment.
     * @param momentA Matrix contains moment.
     * @param momentB Matrix contains moment.
     * @return Moment.
     */
    public double Compute(double[][] momentA, double[][] momentB){
        double moment = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                moment += weight[j] * Math.abs(momentA[i][j] - momentB[i][j]);
            }
        }
        
        return moment;
    }
    
    /**
     * Compute matrix moment.
     * @param fastBitmap Image to be processed.
     * @return Matrix moment.
     */
    public double[][] ComputeMatrixMoment(FastBitmap fastBitmap){
        
        int width = fastBitmap.getWidth();
        int height = fastBitmap.getHeight();
        double size = width * height;
        
        int sumR = 0;
        int sumG = 0;
        int sumB = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                sumR += fastBitmap.getRed(i, j);
                sumG += fastBitmap.getGreen(i, j);
                sumB += fastBitmap.getBlue(i, j);
            }
        }
        
        //1: Compute Mean
        double meanR = sumR / size;
        double meanG = sumG / size;
        double meanB = sumB / size;
        
        //2: Compute Standart Deviation and Skewness
        double stdR = 0;
        double stdG = 0;
        double stdB = 0;
        
        double skwR = 0;
        double skwG = 0;
        double skwB = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                stdR += Math.pow(fastBitmap.getRed(i, j) - meanR, 2);
                stdG += Math.pow(fastBitmap.getGreen(i, j) - meanG, 2);
                stdB += Math.pow(fastBitmap.getBlue(i, j) - meanB, 2);
                
                skwR += Math.pow(fastBitmap.getRed(i, j) - meanR, 3);
                skwG += Math.pow(fastBitmap.getGreen(i, j) - meanG, 3);
                skwB += Math.pow(fastBitmap.getBlue(i, j) - meanB, 3);
            }
        }
        
        stdR = Math.sqrt(stdR / size);
        stdG = Math.sqrt(stdG / size);
        stdB = Math.sqrt(stdB / size);
        
        skwR = Math.pow(skwR, 0.33);
        skwG = Math.pow(skwG, 0.33);
        skwB = Math.pow(skwB, 0.33);
        
        double[][] moment = new double[3][3];
        moment[0][0] = meanR;
        moment[0][1] = stdR;
        moment[0][2] = skwR;
        
        moment[1][0] = meanG;
        moment[1][1] = stdG;
        moment[1][2] = skwG;
        
        moment[2][0] = meanB;
        moment[2][1] = stdB;
        moment[2][2] = skwB;
        
        return moment;
        
    }
}