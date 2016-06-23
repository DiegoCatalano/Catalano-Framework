// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
// Copyright © Peter Kovesi, 2004-2010
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

import Catalano.Core.ArraysUtil;
import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.IApplyInPlace;
import Catalano.Math.Matrix;

/**
 * Fast Radial Symmetry Transform.
 * 
 * References: Loy, G. Zelinsky, A. "Fast radial symmetry for detecting points of interest". IEEE PAMI, Vol. 25, No. 8, August 2003. pp 959-973.
 * @author Diego Catalano
 */
public class FastRadialSymmetryTransform implements IApplyInPlace{
    
    private int radius = 2;
    private float kappa = 8;
    private int alpha = 1;

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
        this.radius = Math.max(2, radius);
    }

    /**
     * Get Radial strictness.
     * @return Radial strictness.
     */
    public int getAlpha() {
        return alpha;
    }

    /**
     * Set Radial strictness.
     * @param alpha Radial strictness.
     */
    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    /**
     * Initialize a new instance of the FastRadialSymmetryTransform class.
     */
    public FastRadialSymmetryTransform() {}
    
    /**
     * Initialize a new instance of the FastRadialSymmetryTransform class.
     * @param radius Radius.
     */
    public FastRadialSymmetryTransform(int radius) {
        setRadius(radius);
    }
    
    /**
     * Initialize a new instance of the FastRadialSymmetryTransform class.
     * @param radius Radius.
     * @param alpha Radial strictness.
     */
    public FastRadialSymmetryTransform(int radius, int alpha) {
        this.radius = radius;
        this.alpha = alpha;
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        if (fastBitmap.isGrayscale()){
            
            int width = fastBitmap.getWidth();
            int height = fastBitmap.getHeight();

            double[][] imageH = Convolution(fastBitmap, ArraysUtil.toDouble(ConvolutionKernel.SobelHorizontal));
            double[][] imageV = Convolution(fastBitmap, ArraysUtil.toDouble(ConvolutionKernel.SobelVertical));
            double[][] symmetryMap = new double[height][width];
            
            double[][] mag = Magnitude(imageH, imageV);
            
            Normalize(imageH, mag);
            Normalize(imageV, mag);
            
            // Create a map XY;
            int[][] mapX = new int[height][width];
            int[][] mapY = new int[height][width];
            
            for (int i = 0; i < height; i++) {
                int index = 1;
                for (int j = 0; j < width; j++) {
                    mapX[i][j] = index;
                    index++;
                }
            }
            
            int index = 1;
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    mapY[i][j] = index;
                }
                index++;
            }
            
            // Create a map positive and negative XY;
            int[][] posX = new int[height][width];
            int[][] posY = new int[height][width];
            int[][] negX = new int[height][width];
            int[][] negY = new int[height][width];
            
            // Start algorithm
            for (int r = 1; r < radius; r++) {
                double[][] m = new double[height][width];
                double[][] o = new double[height][width];
                
                // Create a map positive and negative XY;
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < width; j++) {
                        posX[i][j] = mapX[i][j] + (int)Math.round(r * imageV[i][j]);
                        negX[i][j] = mapX[i][j] - (int)Math.round(r * imageV[i][j]);
                        
                        if (posX[i][j] < 1) posX[i][j] = 1;
                        if (negX[i][j] < 1) negX[i][j] = 1;
                        
                        if (posX[i][j] > width) posX[i][j] = width;
                        if (negX[i][j] > width) negX[i][j] = width;
                    }
                }

                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < width; j++) {
                        posY[i][j] = mapY[i][j] + (int)Math.round(r * imageH[i][j]);
                        negY[i][j] = mapY[i][j] - (int)Math.round(r * imageH[i][j]);
                        
                        if (posY[i][j] < 1) posY[i][j] = 1;
                        if (negY[i][j] < 1) negY[i][j] = 1;
                        
                        if (posY[i][j] > height) posY[i][j] = height;
                        if (negY[i][j] > height) negY[i][j] = height;
                    }
                }
                
                //Problema está aki
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < width; j++) {
                        o[posY[i][j] - 1][posX[i][j] - 1]++;
                        o[negY[i][j] - 1][negX[i][j] - 1]--;
                        
                        m[posY[i][j] - 1][posX[i][j] - 1] += mag[i][j];
                        m[negY[i][j] - 1][negX[i][j] - 1] -= mag[i][j];
                        
                    }
                }
                
                if (r == 1)
                    this.kappa = 8f;
                else 
                    this.kappa = 9.9f;
                
                //Clamp Orientation projection matrix values to a maximum of
                //+/-kappa,  but first set the normalization parameter kappa to the
                //values suggested by Loy and Zelinski
                for (int i = 0; i < o.length; i++) {
                    for (int j = 0; j < o[0].length; j++) {
                        if (o[i][j] > kappa) o[i][j] = kappa;
                        if (o[i][j] < -kappa) o[i][j] = -kappa;
                    }
                }
                
                //Unsmoothed symmetry measure at this radius value
                double[][] f = new double[height][width];
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < width; j++) {
                        f[i][j] = m[i][j] / kappa * Math.pow((Math.abs(o[i][j]) / kappa),alpha);
                    }
                }
                
                double[][] gk = Gaussian(radius, 0.25D * radius);
                gk = Matrix.Multiply(gk, radius);
                
                double[][] fin = Convolution(f, gk);
                
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < width; j++) {
                        symmetryMap[i][j] += fin[i][j];
                    }
                }
            }
            
            //Average
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    symmetryMap[i][j] /= (double)radius;
                }
            }
            
            // Scale image
            double min = Catalano.Math.Matrix.Min(symmetryMap);
            double max = Catalano.Math.Matrix.Max(symmetryMap);
            
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    symmetryMap[i][j] = Catalano.Math.Tools.Scale(min, max, 0, 255, symmetryMap[i][j]);
                }
            }
            
            fastBitmap.matrixToImage(symmetryMap);
            
        }
        else{
            throw new IllegalArgumentException("Fast Radial Symmetry Transform only works in grayscale images.");
        }
    }
    
    /**
     * Create gaussian kernel
     * @param size Size.
     * @param sigma Sigma.
     * @return Gaussian kernel.
     */
    private double[][] Gaussian(int size, double sigma){
        double[][] k = new double[size][size];
        
        double sum = 0;
        for (int i = 0; i < k.length; i++) {
            for (int j = 0; j < k[0].length; j++) {
                double ij = i*i + j*j;
                double v = Math.exp(-ij/(Math.pow(2*sigma,2)));
                k[i][j] = v;
                sum += v;
            }
        }
        
        for (int i = 0; i < k.length; i++) {
            for (int j = 0; j < k[0].length; j++) {
                k[i][j] /= sum;
            }
        }
        
        return k;
    }  
    
    /**
     * Normalize image
     * @param image Image as array.
     * @param mag Magnitude.
     */
    private void Normalize(double[][] image, double[][] mag){
        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[0].length; j++) {
                image[i][j] /= mag[i][j];
            }
        }
    }
    
    /**
     * Performs magnitude between two images.
     * @param H Horizontal image.
     * @param V Vertical image.
     * @return Magnitude.
     */
    private double[][] Magnitude(double[][] H, double[][] V){
        double[][] mag = new double[H.length][H[0].length];
        
        for (int i = 0; i < H.length; i++) {
            for (int j = 0; j < H[0].length; j++) {
                mag[i][j] = (float)Math.sqrt(H[i][j] * H[i][j] + V[i][j] * V[i][j]) + 2.2204e-16;
            }
        }
        
        return mag;
    }
    
    /**
     * Performs convolution
     * @param A Image.
     * @param kernel Kernel
     * @return Result of convolution.
     */
    private double[][] Convolution(double[][] A, double[][] kernel){
            
        int gray;
        int height = A.length;
        int width = A[0].length;
        
        double[][] image = new double[height][width];
        
        int Xline,Yline;
        int lines = CalcLines(kernel);
        
            for (int x = 0; x < height; x++) {
                for (int y = 0; y < width; y++) {
                    gray = 0;
                    for (int i = 0; i < kernel.length; i++) {
                        Xline = x + (i-lines);
                        for (int j = 0; j < kernel[0].length; j++) {
                            Yline = y + (j-lines);
                            if ((Xline >= 0) && (Xline < height) && (Yline >=0) && (Yline < width)) {
                                gray += kernel[i][j] * A[Xline][Yline];
                            }
                        }
                    }
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
    private double[][] Convolution(FastBitmap fastBitmap, double[][] kernel){
            
        int gray;
        int height = fastBitmap.getHeight();
        int width = fastBitmap.getWidth();
        
        double[][] image = new double[height][width];
        
        int Xline,Yline;
        int lines = CalcLines(kernel);
        
            for (int x = 0; x < height; x++) {
                for (int y = 0; y < width; y++) {
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
                    image[x][y] = gray;
                }
            }
        return image;
    }
    
    private int CalcLines(double[][] kernel){
            int lines = (kernel[0].length - 1)/2;
            return lines;
    }
}