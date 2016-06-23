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

import Catalano.Imaging.Filters.*;
import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.IApplyInPlace;
import Catalano.Imaging.Tools.CompassConvolutionKernel;

/**
 * Kirsh Compass Edge Detector.
 * 
 * The operator takes a single kernel mask and rotates it in 45 degree
 * increments through all 8 compass directions: N, NW, W, SW, S, SE, E, and NE.
 * The edge magnitude of the Kirsch operator is calculated as the maximum magnitude across all directions.
 * 
 * The edge magnitude is found by convolving each mask with the image and selecting the largest value at each pxel location.
 * The edge direction at each point is defined by the direction of the edge mask that provides the maximum magnitude.
 * 
 * @author Diego Catalano
 */
public class KirschCompassEdgeDetector implements IApplyInPlace{

    /**
     * Initialize a new instance of the KirschCompassEdgeDetector class.
     */
    public KirschCompassEdgeDetector() {}

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        int[][] kirsch;
        Convolution c = new Convolution();
        FastBitmap copy = new FastBitmap(fastBitmap);
        
        if (fastBitmap.isGrayscale()){
            
            int height = fastBitmap.getHeight();
            int width = fastBitmap.getWidth();
            int[][] image = new int[height][width];
            
            
            // North
            kirsch = CompassConvolutionKernel.Kirsch_North;
            c.setKernel(kirsch);
            c.applyInPlace(copy);
            
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    image[i][j] = copy.getGray(i, j);
                }
            }
            copy = new FastBitmap(fastBitmap);
            
            // Northwest
            kirsch = CompassConvolutionKernel.Kirsch_Northwest;
            c.setKernel(kirsch);
            c.applyInPlace(copy);
            
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    image[i][j] = Math.max(image[i][j], copy.getGray(i, j));
                }
            }
            copy = new FastBitmap(fastBitmap);
            
            // West
            kirsch = CompassConvolutionKernel.Kirsch_West;
            c.setKernel(kirsch);
            c.applyInPlace(copy);
            
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    image[i][j] = Math.max(image[i][j], copy.getGray(i, j));
                }
            }
            copy = new FastBitmap(fastBitmap);
            
            // Southwest
            kirsch = CompassConvolutionKernel.Kirsch_Southwest;
            c.setKernel(kirsch);
            c.applyInPlace(copy);
            
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    image[i][j] = Math.max(image[i][j], copy.getGray(i, j));
                }
            }
            copy = new FastBitmap(fastBitmap);
            
            // South
            kirsch = CompassConvolutionKernel.Kirsch_South;
            c.setKernel(kirsch);
            c.applyInPlace(copy);
            
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    image[i][j] = Math.max(image[i][j], copy.getGray(i, j));
                }
            }
            copy = new FastBitmap(fastBitmap);
            
            // Southeast
            kirsch = CompassConvolutionKernel.Kirsch_Southeast;
            c.setKernel(kirsch);
            c.applyInPlace(copy);
            
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    image[i][j] = Math.max(image[i][j], copy.getGray(i, j));
                }
            }
            copy = new FastBitmap(fastBitmap);
            
            // West
            kirsch = CompassConvolutionKernel.Kirsch_East;
            c.setKernel(kirsch);
            c.applyInPlace(copy);
            
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    image[i][j] = Math.max(image[i][j], copy.getGray(i, j));
                }
            }
            copy = new FastBitmap(fastBitmap);
            
            // Northeast
            kirsch = CompassConvolutionKernel.Kirsch_Northeast;
            c.setKernel(kirsch);
            c.applyInPlace(copy);
            
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    image[i][j] = Math.max(image[i][j], copy.getGray(i, j));
                }
            }
            
            fastBitmap.matrixToImage(image);
            
        }
        if (fastBitmap.isRGB()){
            
            int height = fastBitmap.getHeight();
            int width = fastBitmap.getWidth();
            int[][][] image = new int[height][width][3];
            
            
            // North
            kirsch = CompassConvolutionKernel.Kirsch_North;
            c.setKernel(kirsch);
            c.applyInPlace(copy);
            
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    image[i][j][0] = copy.getRed(i, j);
                    image[i][j][1] = copy.getGreen(i, j);
                    image[i][j][2] = copy.getBlue(i, j);
                }
            }
            copy = new FastBitmap(fastBitmap);
            
            // Northwest
            kirsch = CompassConvolutionKernel.Kirsch_Northwest;
            c.setKernel(kirsch);
            c.applyInPlace(copy);
            
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    image[i][j][0] = Math.max(image[i][j][0], copy.getRed(i, j));
                    image[i][j][1] = Math.max(image[i][j][1], copy.getGreen(i, j));
                    image[i][j][2] = Math.max(image[i][j][2], copy.getBlue(i, j));
                }
            }
            copy = new FastBitmap(fastBitmap);
            
            // West
            kirsch = CompassConvolutionKernel.Kirsch_West;
            c.setKernel(kirsch);
            c.applyInPlace(copy);
            
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    image[i][j][0] = Math.max(image[i][j][0], copy.getRed(i, j));
                    image[i][j][1] = Math.max(image[i][j][1], copy.getGreen(i, j));
                    image[i][j][2] = Math.max(image[i][j][2], copy.getBlue(i, j));
                }
            }
            copy = new FastBitmap(fastBitmap);
            
            // Southwest
            kirsch = CompassConvolutionKernel.Kirsch_Southwest;
            c.setKernel(kirsch);
            c.applyInPlace(copy);
            
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    image[i][j][0] = Math.max(image[i][j][0], copy.getRed(i, j));
                    image[i][j][1] = Math.max(image[i][j][1], copy.getGreen(i, j));
                    image[i][j][2] = Math.max(image[i][j][2], copy.getBlue(i, j));
                }
            }
            copy = new FastBitmap(fastBitmap);
            
            // South
            kirsch = CompassConvolutionKernel.Kirsch_South;
            c.setKernel(kirsch);
            c.applyInPlace(copy);
            
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    image[i][j][0] = Math.max(image[i][j][0], copy.getRed(i, j));
                    image[i][j][1] = Math.max(image[i][j][1], copy.getGreen(i, j));
                    image[i][j][2] = Math.max(image[i][j][2], copy.getBlue(i, j));
                }
            }
            copy = new FastBitmap(fastBitmap);
            
            // Southeast
            kirsch = CompassConvolutionKernel.Kirsch_Southeast;
            c.setKernel(kirsch);
            c.applyInPlace(copy);
            
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    image[i][j][0] = Math.max(image[i][j][0], copy.getRed(i, j));
                    image[i][j][1] = Math.max(image[i][j][1], copy.getGreen(i, j));
                    image[i][j][2] = Math.max(image[i][j][2], copy.getBlue(i, j));
                }
            }
            copy = new FastBitmap(fastBitmap);
            
            // West
            kirsch = CompassConvolutionKernel.Kirsch_East;
            c.setKernel(kirsch);
            c.applyInPlace(copy);
            
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    image[i][j][0] = Math.max(image[i][j][0], copy.getRed(i, j));
                    image[i][j][1] = Math.max(image[i][j][1], copy.getGreen(i, j));
                    image[i][j][2] = Math.max(image[i][j][2], copy.getBlue(i, j));
                }
            }
            copy = new FastBitmap(fastBitmap);
            
            // Northeast
            kirsch = CompassConvolutionKernel.Kirsch_Northeast;
            c.setKernel(kirsch);
            c.applyInPlace(copy);
            
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    image[i][j][0] = Math.max(image[i][j][0], copy.getRed(i, j));
                    image[i][j][1] = Math.max(image[i][j][1], copy.getGreen(i, j));
                    image[i][j][2] = Math.max(image[i][j][2], copy.getBlue(i, j));
                }
            }
            fastBitmap.matrixToImage(image);
        }
    }
}