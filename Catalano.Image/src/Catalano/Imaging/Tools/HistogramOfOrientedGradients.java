// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
// Copyright © César Souza, 2009-2013
// cesarsouza at gmail.com
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

import Catalano.Imaging.FastBitmap;
import java.util.ArrayList;

/**
 * Histograms of Oriented Gradients.
 * 
 * <para>
 * References:
 * <list type="bullet">
 * <item><description>
 * Navneet Dalal and Bill Triggs, "Histograms of Oriented Gradients for Human Detection",
 * CVPR 2005. Available at: <a href="http://lear.inrialpes.fr/people/triggs/pubs/Dalal-cvpr05.pdf">
 * http://lear.inrialpes.fr/people/triggs/pubs/Dalal-cvpr05.pdf </a> </description></item>
 * </list></para>
 * 
 * @author Diego Catalano
 */
public class HistogramOfOrientedGradients {
    
    private int numberOfBins = 9;
    private int cellSize = 6;  // size of the cell, in number of pixels
    private int blockSize = 3; // size of the block, in number of cells
    
    private double epsilon = 1e-10;
    private double binWidth = 0.69813170079773183076947630739545;

    /**
     * Gets the number of histogram bins.
     * @return Number of histogram bins.
     */
    public int getNumberOfBins() {
        return numberOfBins;
    }

    /**
     * Gets the size of a cell, in pixels.
     * @return Size of a cell.
     */
    public int getCellSize() {
        return cellSize;
    }

    /**
     * Gets the size of a block, in pixels.
     * @return Size of a block.
     */
    public int getBlockSize() {
        return blockSize;
    }

    /**
     * Initializes a new instance of the HistogramOfOrientedGradients class.
     */
    public HistogramOfOrientedGradients() {}
    
    /**
     * Initializes a new instance of the HistogramOfOrientedGradients class.
     * @param numberOfBins The number of histogram bins.
     * @param blockSize The size of a block, measured in cells.
     * @param cellSize The size of a cell, measured in pixels.
     */
    public HistogramOfOrientedGradients(int numberOfBins, int blockSize, int cellSize){
        this.numberOfBins = numberOfBins;
        this.blockSize = blockSize;
        this.cellSize = cellSize;
        this.binWidth = (2.0 * Math.PI) / numberOfBins; // 0 to 360
    }
    
    /**
     * Process image looking for corners.
     * @param fastBitmap Image to be processed.
     * @return Returns list of found corners (X-Y coordinates).
     */
    public ArrayList<double[]> ProcessImage(FastBitmap fastBitmap){
        
        if (fastBitmap.isGrayscale()){
            
            int width = fastBitmap.getWidth();
            int height = fastBitmap.getHeight();
            
            // 1. Calculate partial differences
            float[][] direction = new float[height][width];
            float[][] magnitude = new float[height][width];
            
            for (int i = 1; i < height - 1; i++) {
                for (int j = 1; j < width - 1; j++) {
                    
                    int p1 = fastBitmap.getGray(i - 1, j + 1);
                    int p2 = fastBitmap.getGray(i, j + 1);
                    int p3 = fastBitmap.getGray(i + 1, j + 1);
                    int p4 = fastBitmap.getGray(i - 1, j - 1);
                    int p5 = fastBitmap.getGray(i, j - 1);
                    int p6 = fastBitmap.getGray(i + 1, j - 1);
                    int p7 = fastBitmap.getGray(i + 1, j);
                    int p8 = fastBitmap.getGray(i - 1, j);
                    
                    float h = ((p1 + p2 + p3) - (p4 + p5 + p6)) * 0.166666667f;
                    float v = ((p6 + p7 + p3) - (p4 + p8 + p1)) * 0.166666667f;
                    
                    direction[i][j] = (float)Math.atan2(v, h);
                    magnitude[i][j] = (float)Math.sqrt(h * h + v * v);
                }
            }
            
            // 2. Compute cell histograms
            int cellCountX = (int)Math.floor(height / (double)cellSize);
            int cellCountY = (int)Math.floor(width / (double)cellSize);
            double[][][] histograms = new double[cellCountX][cellCountY][];
            
            for (int i = 0; i < cellCountX; i++){
                for (int j = 0; j < cellCountY; j++){
                    // Compute the histogram
                    double[] histogram = new double[numberOfBins];

                    int startCellX = i * cellSize;
                    int startCellY = j * cellSize;

                    // for each pixel in the cell
                    for (int x = 0; x < cellSize; x++)
                    {
                        for (int y = 0; y < cellSize; y++)
                        {
                            double ang = direction[startCellX + x][startCellY + y];
                            double mag = magnitude[startCellX + x][startCellY + y];

                            // Get its angular bin
                            int bin = (int)Math.floor((ang + Math.PI) * binWidth);

                            histogram[bin] += mag;
                        }
                    }

                    histograms[i][j] = histogram;
                }
            }
            
            // 3. Group the cells into larger, normalized blocks
            int blocksCountX = (int)Math.floor(cellCountX / (double)blockSize);
            int blocksCountY = (int)Math.floor(cellCountY / (double)blockSize);

            ArrayList<double[]> blocks = new ArrayList<double[]>();

            for (int i = 0; i < blocksCountX; i++)
            {
                for (int j = 0; j < blocksCountY; j++)
                {
                    double[] v = new double[blockSize * blockSize * numberOfBins];

                    int startBlockX = i * blockSize;
                    int startBlockY = j * blockSize;
                    int c = 0;

                    // for each cell in the block
                    for (int x = 0; x < blockSize; x++)
                    {
                        for (int y = 0; y < blockSize; y++)
                        {
                            double[] histogram =
                                histograms[startBlockX + x][startBlockY + y];

                            // Copy all histograms to the block vector
                            for (int k = 0; k < histogram.length; k++)
                                v[c++] = histogram[k];
                        }
                    }

                    double[] block = Divide(v, Euclidean(v) + epsilon);

                    blocks.add(block);
                }
            }

            return blocks;
        }
        else{
            throw new IllegalArgumentException("HistogramOfOrientedGradients only works in grayscale images.");
        }
    }
    
    private double Euclidean(double[] a){
        double sum = 0.0;
        for (int i = 0; i < a.length; i++)
            sum += a[i] * a[i];
        return Math.sqrt(sum);
    }
    
    private double[] Divide(double[] vector, double x){
        
        double[] r = new double[vector.length];
        
        for (int i = 0; i < vector.length; i++) {
            r[i] = vector[i] / x;
        }
        
        return r;
    }
}