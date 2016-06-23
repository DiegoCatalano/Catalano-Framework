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

package Catalano.Imaging.Texture;

import Catalano.Imaging.FastBitmap;

/**
 * Gray Level Coocurrence Matrix (GLCM).
 * @see Haralick
 * @author Diego Catalano
 */
public class GrayLevelCooccurrenceMatrix {
    
    /**
     * Degree to perform the Run length.
     */
    public static enum Degree{

        /**
         * 0 Degree.
         */
        Degree_0,

        /**
         * 45 Degree.
         */
        Degree_45,

        /**
         * 90 Degree.
         */
        Degree_90,

        /**
         * 135 Degree.
         */
        Degree_135 };
    
    private Degree degree;
    
    private boolean autoGray = true;
    
    private boolean normalize = true;
    
    private int numPairs = 0;
    
    private int distance = 1;

    /**
     * Verify Automatic gray.
     * @return True if need to find maximum gray in current image, otherwise is set 255.
     */
    public boolean isAutoGray() {
        return autoGray;
    }

    /**
     * Set Automatic gray.
     * @param autoGray True if need to find maximum gray in current image, otherwise is set 255.
     */
    public void setAutoGray(boolean autoGray) {
        this.autoGray = autoGray;
    }

    /**
     * Verify if te GLCM is normalized.
     * @return True if the GLCM is normalized, otherwise false.
     */
    public boolean isNormalize() {
        return normalize;
    }

    /**
     * Set Normalize.
     * @param normalize True for normalize the GLCM, otherwise false.
     */
    public void setNormalize(boolean normalize) {
        this.normalize = normalize;
    }

    /**
     * Get Degree.
     * @return Degree.
     */
    public Degree getDegree() {
        return degree;
    }

    /**
     * Set Degree.
     * @param degree Degree.
     */
    public void setDegree(Degree degree) {
        this.degree = degree;
    }

    /**
     * Get distance.
     * @return Distance.
     */
    public int getDistance() {
        return distance;
    }

    /**
     * Set distance.
     * @param distance Distance.
     */
    public void setDistance(int distance) {
        this.distance = distance;
    }
    
    /**
     * Get Number of pairs.
     * @return Number of pairs.
     */
    public int getNumPairs() {
        return numPairs;
    }
    
    /**
     * Initialize a new instance of the GrayLevelCooccurrenceMatrix class.
     */
    public GrayLevelCooccurrenceMatrix(){}
    
    /**
     * Initialize a new instance of the GrayLevelCooccurrenceMatrix class.
     * @param distance Specifies the scale at which the texture is analysed.
     */
    public GrayLevelCooccurrenceMatrix(int distance){
        this.distance = distance;
    }
    
    /**
     * Initialize a new instance of the GrayLevelCooccurrenceMatrix class.
     * @param distance Specifies the scale at which the texture is analysed.
     * @param degree Indicates the direction where the coocurrence is found.
     */
    public GrayLevelCooccurrenceMatrix(int distance, Degree degree){
        this.distance = distance;
        this.degree = degree;
    }
    
    /**
     * Initialize a new instance of the GrayLevelCooccurrenceMatrix class.
     * @param distance Specifies the scale at which the texture is analysed.
     * @param degree Indicates the direction where the coocurrence is found.
     * @param normalize Normalize GLCM. Divides each element per number of pairs.
     */
    public GrayLevelCooccurrenceMatrix(int distance, Degree degree, boolean normalize){
        this.distance = distance;
        this.degree = degree;
        this.normalize = normalize;
    }
    
    /**
     * Initialize a new instance of the GrayLevelCooccurrenceMatrix class.
     * @param distance Specifies the scale at which the texture is analysed.
     * @param degree Indicates the direction where the coocurrence is found.
     * @param normalize Normalize GLCM. Divides each element per number of pairs.
     * @param autoGray Determine the GLCM for 255 (false) or maximum gray found (true).
     */
    public GrayLevelCooccurrenceMatrix(int distance, Degree degree, boolean normalize, boolean autoGray){
        this.distance = distance;
        this.degree = degree;
        this.normalize = normalize;
        this.autoGray = autoGray;
    }
    
    /**
     * Compute GLCM.
     * @param fastBitmap Image to be processed.
     * @return GLCM.
     */
    public double[][] Compute(FastBitmap fastBitmap){
        
        int maxGray = 255;
        if (autoGray) maxGray = getMax(fastBitmap);
        this.numPairs = 0;
        
        double[][] coocurrence = new double[maxGray + 1][maxGray + 1];
        
        int height = fastBitmap.getHeight();
        int width = fastBitmap.getWidth();
        
        switch(degree){
            case Degree_0:
                for (int i = 0; i < height; i++) {
                    for (int j = distance; j < width; j++) {
                        coocurrence[fastBitmap.getGray(i, j - distance)][fastBitmap.getGray(i, j)]++;
                        numPairs++;
                    }
                }
            break;
            case Degree_45:
                for (int x = distance; x < height; x++) {
                    for (int y = 0; y < width - distance; y++) {
                        coocurrence[fastBitmap.getGray(x, y)][fastBitmap.getGray(x - distance, y + distance)]++;
                        numPairs++;
                    }
                }
            break;
            case Degree_90:
                for (int i = distance; i < height; i++) {
                    for (int j = 0; j < width; j++) {
                        coocurrence[fastBitmap.getGray(i - distance, j)][fastBitmap.getGray(i, j)]++;
                        numPairs++;
                    }
                }
            break;
            case Degree_135:
                for (int x = distance; x < height; x++) {
                    int steps = width - 1;
                    for (int y = 0; y < width - distance; y++) {
                        coocurrence[fastBitmap.getGray(x, steps - y)][fastBitmap.getGray(x - distance, steps -distance - y)]++;
                        numPairs++;
                    }
                }
            break;
        }
        
        if (normalize) Normalize(coocurrence, numPairs == 0 ? 1 : numPairs);
        return coocurrence;
        
    }
    
    /**
     * Normalize GLCM.
     * @param coocurrenceMatrix GLCM.
     * @param numPairs Number of Pairs.
     */
    private void Normalize(double[][] coocurrenceMatrix, int numPairs){
        for (int i = 0; i < coocurrenceMatrix.length; i++) {
            for (int j = 0; j < coocurrenceMatrix[0].length; j++) {
                coocurrenceMatrix[i][j] /= numPairs;
            }
        }
    }
    
    /**
     * Gets maximum gray in the image.
     * @param fastBitmap Image to be processed.
     * @return Max intensity.
     */
    private int getMax(FastBitmap fastBitmap){
        int max = 0;
        for (int i = 0; i < fastBitmap.getHeight(); i++) {
            for (int j = 0; j < fastBitmap.getWidth(); j++) {
                int gray = fastBitmap.getGray(i, j);
                if (gray > max) {
                    max = gray;
                }
            }
        }
        return max;
    }
}