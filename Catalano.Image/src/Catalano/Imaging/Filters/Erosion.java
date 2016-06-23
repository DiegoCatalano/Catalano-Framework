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

/**
 * Erosion operator from Mathematical Morphology.
 * <p>The filter assigns minimum value of surrounding pixels to each pixel of the result image. Surrounding pixels, which should be processed, are specified by structuring element: 1 - to process the neighbor, 0 - to skip it.
 * The filter especially useful for binary image processing, where it removes pixels, which are not surrounded by specified amount of neighbors. It gives ability to remove noisy pixels (stand-alone pixels) or shrink objects.</p>
 * 
 * <p><li>Supported types: Grayscale, RGB.
 * <br><li>Coordinate System: Matrix.
 * 
 * @author Diego Catalano
 */
public class Erosion implements IApplyInPlace{
    
    private int radius = 0;
    private int[][] kernel;

    /**
     * Initialize a new instance of the Erosion class.
     */
    public Erosion() {
        this.radius = 1;
    }

    /**
     * Initialize a new instance of the Erosion class.
     * @param radius Radius.
     */
    public Erosion(int radius) {
        this.radius = Math.max(radius,1);
    }

    /**
     * Initialize a new instance of the Erosion class.
     * @param kernel Kernel.
     */
    public Erosion(int[][] kernel) {
        this.kernel = kernel;
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        int height = fastBitmap.getHeight();
        int width = fastBitmap.getWidth();
        
        if (fastBitmap.isGrayscale()){
            if (kernel == null)
                createKernel(radius);
            
            int min;
            FastBitmap copy = new FastBitmap(fastBitmap);
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    
                    int X = 0;
                    int Y;
                    min = 255;
                    for (int x = i - radius; x < i + radius + 1; x++) {
                        Y = 0;
                        for (int y = j - radius; y < j + radius + 1; y++) {
                            if (x >= 0 && x < height && y >= 0 && y < width){
                                int val = copy.getGray(x, y) - kernel[X][Y];
                                if (val < min)
                                    min = val;
                            }
                            Y++;
                        }
                        X++;
                    }
                    min = min < 0 ? 0 : min;
                    fastBitmap.setGray(i, j, min);
                }
            }
        }
        if (fastBitmap.isRGB()){
            if (kernel == null)
                createKernel(radius);
            
            int minR, minG, minB;
            FastBitmap copy = new FastBitmap(fastBitmap);
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    
                    int X = 0,Y;
                    minR = minG = minB = 255;
                    int valR, valG, valB;
                    for (int x = i - radius; x < i + radius + 1; x++) {
                        Y = 0;
                        for (int y = j - radius; y < j + radius + 1; y++) {
                            
                            if (x >= 0 && x < height && y >= 0 && y < width){
                                valR = copy.getRed(x, y) - kernel[X][Y];
                                valG = copy.getGreen(x, y) - kernel[X][Y];
                                valB = copy.getBlue(x, y) - kernel[X][Y];
                                
                                if (valR < minR)
                                    minR = valR;
                                
                                if (valG < minG)
                                    minG = valG;
                                
                                if (valB < minB)
                                    minB = valB;
                            }
                            Y++;
                        }
                        X++;
                    }
                    
                    minR = minR <  0 ? 0 : minR;
                    minG = minG <  0 ? 0 : minG;
                    minB = minB <  0 ? 0 : minB;
                                
                    fastBitmap.setRGB(i, j, minR, minG, minB);
                }
            }
        }
    }
    
    private void createKernel(int radius){
        int size = radius * 2 + 1;
        this.kernel = new int[size][size];
        for (int i = 0; i < kernel.length; i++) {
            for (int j = 0; j < kernel[0].length; j++) {
                kernel[i][j] = 1;
            }
        }
    }
}