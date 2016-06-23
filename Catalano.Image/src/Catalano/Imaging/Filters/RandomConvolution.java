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

import Catalano.Core.DoubleRange;
import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.IApplyInPlace;
import Catalano.Math.Random;

/**
 * Random Convolution filter.
 * 
 * <p><li>Supported types: Grayscale, RGB.
 * <br><li>Coordinate System: Matrix.
 * 
 * @author Diego Catalano
 */
public class RandomConvolution implements IApplyInPlace{
    
    private int size;
    private DoubleRange range;
    private double[][] kernel;
    private boolean replicate = false;
    private boolean genKernel = true;

    /**
     * Get generated kernel.
     * @return Generated kernel.
     */
    public double[][] getKernel() {
        return kernel;
    }

    /**
     * Set custom kernel.
     * @param kernel Kernel.
     */
    public void setKernel(double[][] kernel) {
        this.kernel = kernel;
        genKernel = false;
    }

    /**
     * Verify if needs replicate pixels when is out of border.
     * @return Replicate.
     */
    public boolean isReplicate() {
        return replicate;
    }

    /**
     * Replicate pixels out of border.
     * @param replicate Replicate.
     */
    public void setReplicate(boolean replicate) {
        this.replicate = replicate;
    }

    /**
     * Initialize a new instance of the RandomConvolution class.
     */
    public RandomConvolution() {
        this(3);
    }
    
    /**
     * Initialize a new instance of the RandomConvolution class.
     * @param size Size of kernel.
     */
    public RandomConvolution(int size){
        this(size, new DoubleRange(-2.5,2.5));
    }

    /**
     * Initialize a new instance of the RandomConvolution class.
     * @param size Size of kernel.
     * @param range The range of the values.
     */
    public RandomConvolution(int size, DoubleRange range) {
        this(size, range, true);
    }
    
    /**
     * Initialize a new instance of the RandomConvolution class.
     * @param size Size of kernel.
     * @param range The range of the values.
     * @param replicate If need to replicate the elements of the border.
     */
    public RandomConvolution(int size, DoubleRange range, boolean replicate) {
        this.size = size;
        this.range = range;
        this.replicate = replicate;
        Generate();
    }
    
    public void Generate(){
        Random rand = new Random(System.currentTimeMillis());
        
        //Generate kernel if needed
        if(genKernel == true){
            kernel = new double[size][size];
            for (int i = 0; i < kernel.length; i++) {
                for (int j = 0; j < kernel[0].length; j++) {
                    kernel[i][j] = rand.nextDouble(range.getMin(), range.getMax());
                }
            }
        }
    }
    
    @Override
    public void applyInPlace(FastBitmap fastBitmap){
        
        int height = fastBitmap.getHeight();
        int width = fastBitmap.getWidth();
        int Xline,Yline;
        int lines = CalcLines(kernel);
        
        FastBitmap copy = new FastBitmap(fastBitmap);
        
        if (fastBitmap.isGrayscale()) {
            double gray;
            for (int x = 0; x < height; x++) {
                for (int y = 0; y < width; y++) {
                    gray = 0;
                    for (int i = 0; i < kernel.length; i++) {
                        Xline = x + (i-lines);
                        for (int j = 0; j < kernel[0].length; j++) {
                            Yline = y + (j-lines);
                            if ((Xline >= 0) && (Xline < height) && (Yline >=0) && (Yline < width)) {
                                gray += kernel[i][j] * (double)copy.getGray(Xline, Yline);
                            }
                            else if (replicate){
                                
                                int r = x + i - lines;
                                int c = y + j - lines;
                                
                                if (r < 0) r = 0;
                                if (r >= height) r = height - 1;
                                
                                if (c < 0) c = 0;
                                if (c >= width) c = width - 1;
                                
                                gray += kernel[i][j] * (double)copy.getGray(r, c);
                            }
                        }
                    }
                    
                    gray = gray > 255 ? 255 : gray;
                    gray = gray < 0 ? 0 : gray;
                    
                    fastBitmap.setGray(x, y, (int)gray);
                }
            }
        }
        else{
            double r,g,b;
            for (int x = 0; x < height; x++) {
                for (int y = 0; y < width; y++) {
                    r = g = b = 0;
                    for (int i = 0; i < kernel.length; i++) {
                        Xline = x + (i-lines);
                        for (int j = 0; j < kernel[0].length; j++) {
                            Yline = y + (j-lines);
                            if ((Xline >= 0) && (Xline < height) && (Yline >=0) && (Yline < width)) {
                                r += kernel[i][j] * (double)copy.getRed(Xline, Yline);
                                g += kernel[i][j] * (double)copy.getGreen(Xline, Yline);
                                b += kernel[i][j] * (double)copy.getBlue(Xline, Yline);
                            }
                            else if (replicate){
                                
                                int rr = x + i - lines;
                                int cc = y + j - lines;
                                
                                if (rr < 0) rr = 0;
                                if (rr >= height) rr = height - 1;
                                
                                if (cc < 0) cc = 0;
                                if (cc >= width) cc = width - 1;
                                
                                r += kernel[i][j] * (double)copy.getRed(rr, cc);
                                g += kernel[i][j] * (double)copy.getGreen(rr, cc);
                                b += kernel[i][j] * (double)copy.getBlue(rr, cc);
                            }
                        }
                    }
                    
                    r = r > 255 ? 255 : r;
                    g = g > 255 ? 255 : g;
                    b = b > 255 ? 255 : b;
                    
                    r = r < 0 ? 0 : r;
                    g = g < 0 ? 0 : g;
                    b = b < 0 ? 0 : b;
                    
                    fastBitmap.setRGB(x, y, (int)r, (int)g, (int)b);
                }
            }
        }
    }
    
    private int CalcLines(double[][] kernel){
            int lines = (kernel[0].length - 1)/2;
            return lines;
    }
}