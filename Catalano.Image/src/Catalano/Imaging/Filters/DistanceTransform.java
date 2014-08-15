// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2014
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

/**
 * Distance Transform.
 * @author Diego Catalano
 */
public class DistanceTransform {
    
    /**
     * Distance metric.
     */
    public static enum Distance {
        /**
         * Chessboard metric.
         */
        Chessboard,
        /**
         * Euclidean metric.
         */
        Euclidean,
        /**
         * Manhattan metric.
         */
        Manhattan};
    private Distance distance = Distance.Euclidean;
    
    private float[][] maskDistance = {
        {1.4142f,1,1.4142f},
        {1,0,1},
        {1.4142f,1,1.4142f}
    };
    
    private float[][] image;
    private float max;
    
    /**
     * Get Distance metric.
     * @return Distance metric.
     */
    public Distance getDistance(){
        return distance;
    }
    
    /**
     * Set Distance metric.
     * @param distance Distance metric.
     */
    public void setDistance(Distance distance){
        
        this.distance = distance;
        switch(distance){
            case Chessboard:
                this.maskDistance = new float[][]{
                {1,1,1},
                {1,0,1},
                {1,1,1}
                };
            break;
            case Manhattan:
                this.maskDistance = new float[][]{
                {2,1,2},
                {1,0,1},
                {2,1,2}
                };
            break;
            case Euclidean:
                this.maskDistance = new float[][]{
                {1.4142135f,1,1.4142135f},
                {1,0,1},
                {1.4142135f,1,1.4142135f}
                };
            break;
        }
    }

    /**
     * Get Mask distance.
     * @return Mask distance.
     */
    public float[][] getMaskDistance() {
        return maskDistance;
    }

    /**
     * Set Mask distance.
     * @param maskDistance Mask distance.
     */
    public void setMaskDistance(float[][] maskDistance) {
        this.maskDistance = maskDistance;
    }

    /**
     * Get Maximum distance from transform.
     * @return Maximum distance.
     */
    public float getMaximumDistance() {
        return max;
    }
    
    /**
     * Initialize a new instance of the DistanceTransform class.
     * Default distance: Euclidean.
     */
    public DistanceTransform() {}
    
    /**
     * Initialize a new instance of the DistanceTransform class.
     * @param distance Distance metric.
     */
    public DistanceTransform(Distance distance){
        setDistance(distance);
    }
    
    /**
     * Initialize a new instance of the DistanceTransform class.
     * @param maskDistance Distance mask.
     */
    public DistanceTransform(float[][] maskDistance){
        this.maskDistance = maskDistance;
    }
    
    /**
     * Compute Distance Transform.
     * @param fastBitmap Image to be processed.
     * @return Distance map.
     */
    public float[][] Compute(FastBitmap fastBitmap){
        
        if (fastBitmap.isGrayscale()){
            int width = fastBitmap.getWidth();
            int height = fastBitmap.getHeight();

            image = new float[height][width];

            //Initialize Distance Map
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {

                    if (fastBitmap.getGray(i, j) == 0){
                        image[i][j] = 0;
                    }
                    else{
                        image[i][j] = Float.POSITIVE_INFINITY;
                    }
                }
            }

            //Top -> Bottom - Left -> Right
            for (int i = 1; i < height - 1; i++) {
                for (int j = 1; j < width - 1; j++) {

                    if (image[i][j] > 0){
                        float d1 = maskDistance[1][0] + image[i][j - 1];
                        float d2 = maskDistance[0][0] + image[i - 1][j - 1];
                        float d3 = maskDistance[0][1] + image[i - 1][j];
                        float d4 = maskDistance[0][2] + image[i - 1][j + 1];
                        image[i][j] = Math.min(d1, Math.min(d2, Math.min(d3, d4)));
                    }

                }
            }

            //Bottom -> Top - Right -> Left
            for (int i = height - 2; i > 1; i--) {
                for (int j = width - 3; j > 1; j--) {

                    if (image[i][j] > 0){
                        float d1 = maskDistance[1][2] + image[i][j + 1];
                        float d2 = maskDistance[2][2] + image[i + 1][j + 1];
                        float d3 = maskDistance[2][1] + image[i + 1][j];
                        float d4 = maskDistance[2][0] + image[i + 1][j - 1];
                        image[i][j] = Math.min(image[i][j], Math.min(d1, Math.min(d2, Math.min(d3, d4))));
                    }

                }
            }
            
            max = -Float.MAX_VALUE;
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (image[i][j] == Float.POSITIVE_INFINITY){
                        image[i][j] = 0;
                    }
                    if(image[i][j] > max){
                        max = image[i][j];
                    }
                }
            }
            
            return image;
        }
        else{
            throw new IllegalArgumentException("Distance Transform only works in grayscale images.");
        }
        
    }
    
    /**
     * Convert Distance map to FastBitmap.
     * @return FastBitmap.
     */
    public FastBitmap toFastBitmap(){
        
        int width = image[0].length;
        int height = image.length;
        
        double max = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
//                if (image[i][j] == Float.POSITIVE_INFINITY){
//                    image[i][j] = 0;
//                }
                if (image[i][j] > max){
                    max = image[i][j];
                }
            }
        }
        
        FastBitmap fb = new FastBitmap(width, height, FastBitmap.ColorSpace.Grayscale);
        
        if (max > 255){
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    fb.setGray(i, j, (int)Catalano.Math.Tools.Scale(0, max, 0, 255, image[i][j]));
                }
            }
        }
        else{
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < width; j++) {
                        fb.setGray(i, j, (int)image[i][j]);
                    }
                }
        }
        
        return fb;
        
    }
}