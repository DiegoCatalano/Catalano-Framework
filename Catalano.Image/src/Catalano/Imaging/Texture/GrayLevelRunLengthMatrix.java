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
import Catalano.Imaging.Tools.ImageStatistics;

/**
 * Gray Level Run Length Matrix (GLRLM).
 * @author Diego Catalano
 */
public class GrayLevelRunLengthMatrix {
    
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
    private int numPrimitives;
    private boolean autoGray = true;

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
     * Get number of primitives.
     * @return Number of primitives.
     */
    public int getNumberPrimitives() {
        return numPrimitives;
    }

    /**
     * Set number of primitives.
     * @param numberPrimitives Number of primitives.
     */
    public void setNumberPrimitives(int numberPrimitives) {
        this.numPrimitives = numberPrimitives;
    }

    /**
     * Initialize a new instance of the GrayLevelRunLengthMatrix class.
     * @param degree Degree.
     */
    public GrayLevelRunLengthMatrix(Degree degree) {
        this.degree = degree;
    }
    
    /**
     * Initialize a new instance of the GrayLevelRunLengthMatrix class.
     * @param degree Degree.
     * @param autoGray Automatic gray.
     */
    public GrayLevelRunLengthMatrix(Degree degree, boolean autoGray) {
        this.degree = degree;
        this.autoGray = autoGray;
    }
    
    /**
     * Compute GLRLM.
     * @param fastBitmap Image to be processed.
     * @return GLRLM.
     */
    public double[][] Compute(FastBitmap fastBitmap){
        
        int maxGray = 255;
        if (autoGray) maxGray = ImageStatistics.Maximum(fastBitmap);
        
        int height = fastBitmap.getHeight();
        int width = fastBitmap.getWidth();
        
        double[][] runMatrix = new double[maxGray + 1][width + 1];
        
        switch(degree){
            case Degree_0:
                for (int i = 0; i < height; i++) {
                    int runs = 1;
                    for (int j = 1; j < width; j++) {
                        int g1 = fastBitmap.getGray(i, j - 1);
                        int g2 = fastBitmap.getGray(i, j);
                        if (g1 == g2) {
                            runs++;
                        }
                        else{
                            runMatrix[g1][runs]++;
                            numPrimitives++;
                            runs = 1;
                        }
                        if ((g1 == g2) && (j == width - 1)) {
                            runMatrix[g1][runs]++;
                        }
                        if ((g1 != g2) && (j == width - 1)) {
                            runMatrix[g2][1]++;
                        }
                    }
                }
            break;
            
            case Degree_45:
                
                // Compute I(0,0) and I(height,width)
                runMatrix[0][1]++;
                runMatrix[height - 1][width - 1]++;
                
                // Compute height
                for (int i = 1; i < height; i++) {
                    int runs = 1;
                    int steps = i;
                    for (int j = 0; j < steps; j++) {
                        int g1 = fastBitmap.getGray(i - j, j);
                        int g2 = fastBitmap.getGray(i - j - 1, j + 1);
                        if (g1 == g2) {
                            runs++;
                        }
                        else{
                            runMatrix[g1][runs]++;
                            numPrimitives++;
                            runs = 1;
                        }
                        if ((g1 == g2) && (j == steps - 1)) {
                            runMatrix[g1][runs]++;
                        }
                        if ((g1 != g2) && (j == steps - 1)) {
                            runMatrix[g2][1]++;
                        }
                    }
                }
                
                // Compute width
                for (int j = 1; j < width - 1; j++) {
                    int runs = 1;
                    int steps = height - j;
                    for (int i = 1; i < steps; i++) {
                        int g1 = fastBitmap.getGray(height - i, j + i - 1);
                        int g2 = fastBitmap.getGray(height - i - 1, j + i);
                        if (g1 == g2) {
                            runs++;
                        }
                        else{
                            runMatrix[g1][runs]++;
                            numPrimitives++;
                            runs = 1;
                        }
                        if ((g1 == g2) && (i == steps - 1)) {
                            runMatrix[g1][runs]++;
                        }
                        if ((g1 != g2) && (i == steps - 1)) {
                            runMatrix[g2][1]++;
                        }
                    }
                }
            break;
                
            case Degree_90:
                for (int j = 0; j < width; j++) {
                    int runs = 1;
                    for (int i = 0; i < height - 1; i++) {
                        int g1 = fastBitmap.getGray(height - i - 1, j);
                        int g2 = fastBitmap.getGray(height - i - 2, j);
                        if (g1 == g2) {
                            runs++;
                        }
                        else{
                            runMatrix[g1][runs]++;
                            numPrimitives++;
                            runs = 1;
                        }
                        if ((g1 == g2) && (i == height - 2)) {
                            runMatrix[g1][runs]++;
                        }
                        if ((g1 != g2) && (i == height - 2)) {
                            runMatrix[g2][1]++;
                        }
                    }
                }
            break;
            
            case Degree_135:
                
                // Compute I(0,width) and I(height,0)
                runMatrix[0][width - 1]++;
                runMatrix[height - 1][0]++;
                
                // Compute height
                for (int i = 1; i < width; i++) {
                    int runs = 1;
                    int steps = i;
                    int w = width - 1;
                    for (int j = 0; j < steps; j++) {
                        int g1 = fastBitmap.getGray(i - j, w);
                        int g2 = fastBitmap.getGray(i - j - 1, --w);
                        if (g1 == g2) {
                            runs++;
                        }
                        else{
                            runMatrix[g1][runs]++;
                            numPrimitives++;
                            runs = 1;
                        }
                        if ((g1 == g2) && (j == steps - 1)) {
                            runMatrix[g1][runs]++;
                        }
                        if ((g1 != g2) && (j == steps - 1)) {
                            runMatrix[g2][1]++;
                        }
                    }
                }
                // Compute width
                for (int j = 1; j < width - 1; j++) {
                    int runs = 1;
                    int steps = height - j;
                    int w = width - 1 - j;
                    for (int i = 1; i < steps; i++) {
                        int g1 = fastBitmap.getGray(height - i, w);
                        int g2 = fastBitmap.getGray(height - i - 1, --w);
                        if (g1 == g2) {
                            runs++;
                        }
                        else{
                            runMatrix[g1][runs]++;
                            numPrimitives++;
                            runs = 1;
                        }
                        if ((g1 == g2) && (i == steps - 1)) {
                            runMatrix[g1][runs]++;
                        }
                        if ((g1 != g2) && (i == steps - 1)) {
                            runMatrix[g2][1]++;
                        }
                    }
                }
            break;
        }
        return runMatrix;
    }
}