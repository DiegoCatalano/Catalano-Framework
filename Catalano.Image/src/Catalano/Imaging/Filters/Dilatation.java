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
 * Dilatation operator from Mathematical Morphology.
 * The filter assigns maximum value of surrounding pixels to each pixel of the result image. Surrounding pixels, which should be processed, are specified by structuring element: 1 - to process the neighbor, 0 - to skip it.
 * The filter especially useful for binary image processing, where it allows to grow separate objects or join objects.
 * 
 * <p><li>Supported types: Grayscale, RGB.
 * <br><li>Coordinate System: Matrix.
 * 
 * @author Diego Catalano
 */
public class Dilatation implements IApplyInPlace{
    
    private int radius = 0;
    private int[][] kernel;

    /**
     * Initialize a new instance of the Dilatation class.
     */
    public Dilatation() {
        this.radius = 1;
    }

    /**
     * Initialize a new instance of the Dilatation class.
     * @param radius Radius.
     */
    public Dilatation(int radius) {
        this.radius = Math.max(radius,1);
    }

    /**
     * Initialize a new instance of the Dilatation class.
     * @param kernel Kernel.
     */
    public Dilatation(int[][] kernel) {
        this.kernel = kernel;
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        int height = fastBitmap.getHeight();
        int width = fastBitmap.getWidth();
        
        if (fastBitmap.isGrayscale()){
            if (kernel == null)
                createKernel(radius);
            
            FastBitmap copy = new FastBitmap(fastBitmap);
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    
                    int X = 0,Y;
                    int max = 0;
                    for (int x = i - radius; x < i + radius + 1; x++) {
                        Y = 0;
                        for (int y = j - radius; y < j + radius + 1; y++) {
                            
                            if (x >= 0 && x < height && y >= 0 && y < width){
                                int val = copy.getGray(x, y) + kernel[X][Y];
                                
                                if (val > max)
                                    max = val;
                                
                            }
                            Y++;
                        }
                        X++;
                    }
                    
                    max = max > 255 ? 255 : max;
                    fastBitmap.setGray(i, j, max);
                }
            }
        }
        if (fastBitmap.isRGB()){
            if (kernel == null)
                createKernel(radius);
            
            FastBitmap copy = new FastBitmap(fastBitmap);
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    
                    int X = 0,Y;
                    int maxR = 0, maxG = 0, maxB = 0;
                    for (int x = i - radius; x < i + radius + 1; x++) {
                        Y = 0;
                        for (int y = j - radius; y < j + radius + 1; y++) {
                            
                            if (x >= 0 && x < height && y >= 0 && y < width){
                                int valR = copy.getRed(x, y) + kernel[X][Y];
                                int valG = copy.getGreen(x, y) + kernel[X][Y];
                                int valB = copy.getBlue(x, y) + kernel[X][Y];
                                
                                if (valR > maxR)
                                    maxR = valR;
                                
                                if (valG > maxG)
                                    maxG = valG;
                                
                                if (valB > maxB)
                                    maxB = valB;
                                
                            }
                            Y++;
                        }
                        X++;
                    }
                    
                    maxR = maxR >  255 ? 255 : maxR;
                    maxG = maxG >  255 ? 255 : maxG;
                    maxB = maxB >  255 ? 255 : maxB;
                    fastBitmap.setRGB(i, j, maxR, maxG, maxB);
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