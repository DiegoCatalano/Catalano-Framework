// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2015
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

import Catalano.Math.Matrix;

/**
 * Common operations in the image.
 * @author Diego Catalano
 */
public class ImageUtils {
    
    /**
     * Convolution operator.
     * @param image Image.
     * @param kernel Kernel.
     * @return Result of the convolution.
     */
    public static double[][] Convolution(double[][] image, double[][] kernel){
        return Convolution(image, kernel, true);
    }
    
    /**
     * Convolution operator.
     * @param image Image.
     * @param kernel Kernel.
     * @param replicate Replicate border.
     * @return Result of the convolution.
     */
    public static double[][] Convolution(double[][] image, double[][] kernel, boolean replicate){
        int width = image[0].length;
        int height = image.length;
        double[][] result = new double[height][width];
        
        int Xline,Yline;
        int lines = (kernel.length - 1)/2;
        double gray;
        
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                gray = 0;
                for (int i = 0; i < kernel.length; i++) {
                    Xline = x + (i-lines);
                    for (int j = 0; j < kernel[0].length; j++) {
                        Yline = y + (j-lines);
                        if ((Xline >= 0) && (Xline < height) && (Yline >=0) && (Yline < width)) {
                            gray += kernel[i][j] * image[Xline][Yline];
                        }
                        else if(replicate){

                            int r = x + i - lines;
                            int c = y + j - lines;

                            if (r < 0) r = 0;
                            if (r >= height) r = height - 1;

                            if (c < 0) c = 0;
                            if (c >= width) c = width - 1;

                            gray += kernel[i][j] * image[r][c];
                        }
                    }
                }
                result[x][y] = gray;
            }
        }
        
        return result;
    }
    
    /**
     * Separable convolution operator.
     * @param image Image.
     * @param row Row vector.
     * @param col Column vector.
     * @return Result of the convolution.
     */
    public static double[][] Convolution(double[][] image, double[] row, double[] col){
        return Convolution(image, row, col, true);
    }
    
    /**
     * Separable convolution operator.
     * @param image Image.
     * @param row Row vector.
     * @param col Column vector.
     * @param replicate Replicate the border.
     * @return Result of the convolution.
     */
    public static double[][] Convolution(double[][] image, double[] row, double[] col, boolean replicate){
        
        int width = image[0].length;
        int height = image.length;
        int Xline,Yline;
        int lines = (row.length - 1) / 2;
        double[][] result = new double[height][width];

        double[][] copy = new double[height][width];
        for (int i = 0; i < copy.length; i++) {
            for (int j = 0; j < copy[0].length; j++) {
                copy[i][j] = image[i][j];
            }
        }
        double gray;

        //Horizontal orientation
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                gray = 0;
                for (int k = 0; k < row.length; k++) {
                    Yline = j - lines + k;
                    if ((Yline >=0) && (Yline < width)) {
                        gray += row[k] * image[i][Yline];
                    }
                    else if (replicate){

                        int c = j + k - lines;

                        if (c < 0) c = 0;
                        if (c >= width) c = width - 1;

                        gray += row[row.length - k - 1] * image[i][c];
                     }
                }

                copy[i][j] = gray;

            }
        }

        //Vertical orientation
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                gray = 0;
                for (int k = 0; k < col.length; k++) {
                    Xline = i - lines + k;
                    if ((Xline >=0) && (Xline < height)) {
                        gray += col[k] * copy[Xline][j];
                    }
                     else if (replicate){

                        int r = i + k - lines;

                        if (r < 0) r = 0;
                        if (r >= height) r = height - 1;

                        gray += col[k] * copy[r][j];
                     }
                }

                result[i][j] = gray;
            }
        }
        return result;
    }
    
    /**
     * Normalize the image within the range [0..255].
     * @param image Image.
     */
    public static void Normalize(double[][] image){
        Normalize(image,0,255);
    }
    
    /**
     * Normalize the image.
     * @param image Image.
     * @param min Minimum value.
     * @param max Maximum value.
     */
    public static void Normalize(double[][] image, double min, double max){
        double[] mm = Matrix.MinMax(image);
        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[0].length; j++) {
                image[i][j] = (int)Catalano.Math.Tools.Scale(mm[0], mm[1], min, max, image[i][j]);
            }
        }
    }
    
    /**
     * Normalize the image within the range [0..255].
     * @param image Image.
     */
    public static void Normalize(double[][][] image){
        Normalize(image,0,255);
    }
    
    /**
     * Normalize the image.
     * @param image Image.
     * @param min Minimum value.
     * @param max Maximum value.
     */
    public static void Normalize(double[][][] image, double min, double max){
        
        double minRed, minGreen, minBlue;
        double maxRed, maxGreen, maxBlue;
        
        minRed = minGreen = minBlue = Double.MAX_VALUE;
        maxRed = maxGreen = maxBlue = -Double.MAX_VALUE;
        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[0].length; j++) {
                minRed = Math.min(minRed, image[i][j][0]);
                minGreen = Math.min(minGreen, image[i][j][1]);
                minBlue = Math.min(minBlue, image[i][j][2]);
                
                maxRed = Math.max(maxRed, image[i][j][0]);
                maxGreen = Math.max(maxGreen, image[i][j][1]);
                maxBlue = Math.max(maxBlue, image[i][j][2]);
            }
        }
        
        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[0].length; j++) {
                image[i][j][0] = (int)Catalano.Math.Tools.Scale(minRed, maxRed, min, max, image[i][j][0]);
                image[i][j][1] = (int)Catalano.Math.Tools.Scale(minGreen, maxGreen, min, max, image[i][j][1]);
                image[i][j][2] = (int)Catalano.Math.Tools.Scale(minBlue, maxBlue, min, max, image[i][j][2]);
            }
        }
    }
}