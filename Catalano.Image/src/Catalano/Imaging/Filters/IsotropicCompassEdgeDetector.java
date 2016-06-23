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

package Catalano.Imaging.Filters;

import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.IApplyInPlace;
import Catalano.Imaging.Tools.CompassConvolutionKernel;

/**
 * Isotropic Compass Edge Detector.
 * 
 * The operator takes a single kernel mask and rotates it in 45 degree
 * increments through all 8 compass directions: N, NW, W, SW, S, SE, E, and NE.
 * The edge magnitude of the Isotropic operator is calculated as the maximum magnitude across all directions.
 * 
 * The edge magnitude is found by convolving each mask with the image and selecting the largest value at each pxel location.
 * The edge direction at each point is defined by the direction of the edge mask that provides the maximum magnitude.
 * 
 * @author Diego Catalano
 */
public class IsotropicCompassEdgeDetector implements IApplyInPlace{

    /**
     * Initialize a new instance of the IsotropicCompassEdgeDetector class.
     */
    public IsotropicCompassEdgeDetector() {}

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        float[][] isotropic;
        
        if (fastBitmap.isGrayscale()){
            
            int height = fastBitmap.getHeight();
            int width = fastBitmap.getWidth();
            int[][] image = new int[height][width];
            int[][] copy;
            
            
            // North
            isotropic = CompassConvolutionKernel.Isotropic_North;
            copy = Convolution(fastBitmap, isotropic);
            
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    image[i][j] = copy[i][j];
                }
            }
            
            // Northwest
            isotropic = CompassConvolutionKernel.Isotropic_Northwest;
            copy = Convolution(fastBitmap, isotropic);
            
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    image[i][j] = Math.max(image[i][j], copy[i][j]);
                }
            }
            
            // West
            isotropic = CompassConvolutionKernel.Isotropic_West;
            copy = Convolution(fastBitmap, isotropic);
            
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    image[i][j] = Math.max(image[i][j], copy[i][j]);
                }
            }
            
            // Southwest
            isotropic = CompassConvolutionKernel.Isotropic_Southwest;
            copy = Convolution(fastBitmap, isotropic);
            
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    image[i][j] = Math.max(image[i][j], copy[i][j]);
                }
            }
            
            // South
            isotropic = CompassConvolutionKernel.Isotropic_South;
            copy = Convolution(fastBitmap, isotropic);
            
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    image[i][j] = Math.max(image[i][j], copy[i][j]);
                }
            }
            
            // Southeast
            isotropic = CompassConvolutionKernel.Isotropic_Southeast;
            copy = Convolution(fastBitmap, isotropic);
            
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    image[i][j] = Math.max(image[i][j], copy[i][j]);
                }
            }
            
            // West
            isotropic = CompassConvolutionKernel.Isotropic_East;
            copy = Convolution(fastBitmap, isotropic);
            
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    image[i][j] = Math.max(image[i][j], copy[i][j]);
                }
            }
            
            // Northeast
            isotropic = CompassConvolutionKernel.Isotropic_Northeast;
            copy = Convolution(fastBitmap, isotropic);
            
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    image[i][j] = Math.max(image[i][j], copy[i][j]);
                }
            }
            
            fastBitmap.matrixToImage(image);
            
        }
        
        if(fastBitmap.isRGB()){
            
            int height = fastBitmap.getHeight();
            int width = fastBitmap.getWidth();
            int[][][] image = new int[height][width][3];
            int[][][] copy;
            
            
            // North
            isotropic = CompassConvolutionKernel.Isotropic_North;
            copy = ConvolutionRGB(fastBitmap, isotropic);
            
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    image[i][j][0] = copy[i][j][0];
                    image[i][j][1] = copy[i][j][1];
                    image[i][j][2] = copy[i][j][2];
                }
            }
            
            // Northwest
            isotropic = CompassConvolutionKernel.Isotropic_Northwest;
            copy = ConvolutionRGB(fastBitmap, isotropic);
            
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    image[i][j][0] = Math.max(image[i][j][0], copy[i][j][0]);
                    image[i][j][1] = Math.max(image[i][j][1], copy[i][j][1]);
                    image[i][j][2] = Math.max(image[i][j][2], copy[i][j][2]);
                }
            }
            
            // West
            isotropic = CompassConvolutionKernel.Isotropic_West;
            copy = ConvolutionRGB(fastBitmap, isotropic);
            
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    image[i][j][0] = Math.max(image[i][j][0], copy[i][j][0]);
                    image[i][j][1] = Math.max(image[i][j][1], copy[i][j][1]);
                    image[i][j][2] = Math.max(image[i][j][2], copy[i][j][2]);
                }
            }
            
            // Southwest
            isotropic = CompassConvolutionKernel.Isotropic_Southwest;
            copy = ConvolutionRGB(fastBitmap, isotropic);
            
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    image[i][j][0] = Math.max(image[i][j][0], copy[i][j][0]);
                    image[i][j][1] = Math.max(image[i][j][1], copy[i][j][1]);
                    image[i][j][2] = Math.max(image[i][j][2], copy[i][j][2]);
                }
            }
            
            // South
            isotropic = CompassConvolutionKernel.Isotropic_South;
            copy = ConvolutionRGB(fastBitmap, isotropic);
            
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    image[i][j][0] = Math.max(image[i][j][0], copy[i][j][0]);
                    image[i][j][1] = Math.max(image[i][j][1], copy[i][j][1]);
                    image[i][j][2] = Math.max(image[i][j][2], copy[i][j][2]);
                }
            }
            
            // Southeast
            isotropic = CompassConvolutionKernel.Isotropic_Southeast;
            copy = ConvolutionRGB(fastBitmap, isotropic);
            
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    image[i][j][0] = Math.max(image[i][j][0], copy[i][j][0]);
                    image[i][j][1] = Math.max(image[i][j][1], copy[i][j][1]);
                    image[i][j][2] = Math.max(image[i][j][2], copy[i][j][2]);
                }
            }
            
            // West
            isotropic = CompassConvolutionKernel.Isotropic_East;
            copy = ConvolutionRGB(fastBitmap, isotropic);
            
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    image[i][j][0] = Math.max(image[i][j][0], copy[i][j][0]);
                    image[i][j][1] = Math.max(image[i][j][1], copy[i][j][1]);
                    image[i][j][2] = Math.max(image[i][j][2], copy[i][j][2]);
                }
            }
            
            // Northeast
            isotropic = CompassConvolutionKernel.Isotropic_Northeast;
            copy = ConvolutionRGB(fastBitmap, isotropic);
            
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    image[i][j][0] = Math.max(image[i][j][0], copy[i][j][0]);
                    image[i][j][1] = Math.max(image[i][j][1], copy[i][j][1]);
                    image[i][j][2] = Math.max(image[i][j][2], copy[i][j][2]);
                }
            }
            
            fastBitmap.matrixToImage(image);
            
        }
    }
    
    /**
     * Performs convolution.
     * @param fastBitmap Image to be processed.
     * @param kernel Kernel.
     * @return Result of convolution.
     */
    private int[][] Convolution(FastBitmap fastBitmap, float[][] kernel){
            
        int gray;
        int height = fastBitmap.getHeight();
        int width = fastBitmap.getWidth();
        
        int[][] image = new int[height][width];
        
        int Xline,Yline;
        int lines = CalcLines(kernel);
        
            for (int x = 1; x < height - 1; x++) {
                for (int y = 1; y < width - 1; y++) {
                    gray = 0;
                    for (int i = 0; i < kernel.length; i++) {
                        Xline = x + (i-lines);
                        for (int j = 0; j < kernel[0].length; j++) {
                            Yline = y + (j-lines);
                            if ((Xline >= 0) && (Xline < height) && (Yline >=0) && (Yline < width)) {
                                gray += kernel[i][j] * fastBitmap.getGray(Xline, Yline);
                            }
                        }
                    }
                    
                    if (gray < 0) gray = 0;
                    if (gray > 255) gray = 255;
                    
                    image[x][y] = gray;
                }
            }
        return image;
    }
    
    /**
     * Performs convolution.
     * @param fastBitmap Image to be processed.
     * @param kernel Kernel.
     * @return Result of convolution.
     */
    private int[][][] ConvolutionRGB(FastBitmap fastBitmap, float[][] kernel){
            
        int r,g,b;
        int height = fastBitmap.getHeight();
        int width = fastBitmap.getWidth();
        
        int[][][] image = new int[height][width][3];
        
        int Xline,Yline;
        int lines = CalcLines(kernel);
        
            for (int x = 0; x < height; x++) {
                for (int y = 0; y < width; y++) {
                    r = g = b = 0;
                    for (int i = 0; i < kernel.length; i++) {
                        Xline = x + (i-lines);
                        for (int j = 0; j < kernel[0].length; j++) {
                            Yline = y + (j-lines);
                            if ((Xline >= 0) && (Xline < height) && (Yline >=0) && (Yline < width)) {
                                r += kernel[i][j] * fastBitmap.getRed(Xline, Yline);
                                g += kernel[i][j] * fastBitmap.getGreen(Xline, Yline);
                                b += kernel[i][j] * fastBitmap.getBlue(Xline, Yline);
                            }
                        }
                    }
                    
                    if (r < 0) r = 0;
                    if (g < 0) g = 0;
                    if (b < 0) b = 0;
                    
                    if (r > 255) r = 255;
                    if (g > 255) g = 255;
                    if (b > 255) b = 255;
                    
                    image[x][y][0] = r;
                    image[x][y][1] = g;
                    image[x][y][2] = b;
                }
            }
        return image;
    }
    
    private int CalcLines(float[][] kernel){
            int lines = (kernel[0].length - 1)/2;
            return lines;
    }
}