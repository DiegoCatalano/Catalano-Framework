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

import Catalano.Core.IntPoint;
import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.Filters.Convolution;

/**
 * Computes gradients.
 * @author Diego Catalano
 */
public class GradientImage {
    
    private int[][] h, v;
    private FastBitmap horizontalBitmap;
    private FastBitmap verticalBitmap;
    private float[][] magnitude, orientation;
    private int division = 1;

    /**
     * Get division.
     * @return Division.
     */
    public int getDivision() {
        return division;
    }

    /**
     * Set division.
     * @param division Division.
     */
    public void setDivision(int division) {
        this.division = division;
    }

    /**
     * Initialize a new instance of the GradientImage class.
     * @param fastBitmap Image to be processed.
     */
    public GradientImage(FastBitmap fastBitmap) {
        Compute(fastBitmap);
    }

    /**
     * Initialize a new instance of the GradientImage class.
     * @param fastBitmap Image to be processed.
     * @param horizontal Gradient mask for horizontal convolution.
     * @param vertical Gradient mask for vertical convolution.
     */
    public GradientImage(FastBitmap fastBitmap, int[][] horizontal, int[][] vertical) {
        this.h = horizontal;
        this.v = vertical;
        Compute(fastBitmap);
    }
    
    /**
     * Initialize a new instance of the GradientImage class.
     * @param fastBitmap Image to be processed.
     * @param horizontal Gradient mask for horizontal convolution.
     * @param vertical Gradient mask for vertical convolution.
     * @param division Divides the result of convolution.
     */
    public GradientImage(FastBitmap fastBitmap, int[][] horizontal, int[][] vertical, int division) {
        this.h = horizontal;
        this.v = vertical;
        this.division = division;
        Compute(fastBitmap);
    }
    
    /**
     * Compute gradients.
     * @param fastBitmap Image to be processed.
     */
    private void Compute(FastBitmap fastBitmap){
        horizontalBitmap = new FastBitmap(fastBitmap);
        verticalBitmap = new FastBitmap(fastBitmap);
        
        int width = fastBitmap.getWidth();
        int height = fastBitmap.getHeight();
        
        // Horizontal convolution
        Convolution c = new Convolution(h, division);
        c.applyInPlace(horizontalBitmap);
        
        //Vertical convolution
        c.setKernel(v);
        c.applyInPlace(verticalBitmap);
        
        // Compute gradients
        magnitude = new float[height][width];
        orientation = new float[height][width];
        
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                double H = horizontalBitmap.getGray(x, y);
                double V = verticalBitmap.getGray(x, y);
                magnitude[x][y] = (float)Math.sqrt((H * H) + (V * V));
                orientation[x][y] = (float)Math.atan2(V, H);
            }
        }
    }
    
    /**
     * Get magnitude at point.
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     * @return Magnitude
     */
    public float getMagnitude(int x, int y){
        return magnitude[x][y];
    }
    
    /**
     * Get magnitude at point.
     * @param point IntPoint.
     * @return Magnitude.
     */
    public float getMagnitude(IntPoint point){
        return magnitude[point.x][point.y];
    }
    
    /**
     * Get magnitude.
     * @return Magnitude.
     */
    public float[][] getMagnitude(){
        return magnitude;
    }
    
    /**
     * Get orientation at X and Y coordinates.
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     * @return Orientation.
     */
    public float getOrientation(int x, int y){
        return orientation[x][y];
    }
    
    /**
     * Get orientation at point.
     * @param point IntPoint.
     * @return Orientation.
     */
    public float getOrientation(IntPoint point){
        return orientation[point.x][point.y];
    }
    
    /**
     * Get orientation.
     * @return Orientation.
     */
    public float[][] getOrientation(){
        return orientation;
    }
    
    public FastBitmap getVerticalImage(){
        return verticalBitmap;
    }
    
    public FastBitmap getHorizontalImage(){
        return horizontalBitmap;
    }
    
    public FastBitmap getMaximumGradient(){
        
        int width = horizontalBitmap.getWidth();
        int height = horizontalBitmap.getHeight();
        
        FastBitmap image = new FastBitmap(width, height, FastBitmap.ColorSpace.Grayscale);
        
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int g = Math.min(255, horizontalBitmap.getGray(i, j) + verticalBitmap.getGray(i, j));
                image.setGray(i, j, g);
            }
        }
        
        return image;
    }
}