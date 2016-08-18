/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Catalano.Imaging.Filters;

import Catalano.Imaging.FastBitmap;

/**
 *
 * @author Diego Catalano
 */
public class DisparityMap {
    
    private int sizeBlock = 15;
    private int distance = 64;

    /**
     * Disparity Map.
     */
    public DisparityMap() {}
    
    /**
     * Disparity Map.
     * @param sizeBlock Size block.
     */
    public DisparityMap(int sizeBlock){
        this.sizeBlock = sizeBlock;
    }
    
    public double[][] Process(FastBitmap left, FastBitmap right){
        
        if(left.isRGB())
            left.toGrayscale();
        
        if(right.isRGB())
            right.toGrayscale();
        
        int width = left.getWidth();
        int height = left.getHeight();
        
        double[][] map = new double[height][width];
        
        for (int i = 0; i < height - sizeBlock; i++) {
            for (int j = 0; j < width - sizeBlock; j++) {
                
                double min = Double.MAX_VALUE;
                double z = 0;
                
                //Get left block
                FastBitmap a = getSubimage(left, i, j, sizeBlock, sizeBlock);
                for (int k = j - distance; k < j; k++) {
                    if(k < width-sizeBlock & k > 0){
                        FastBitmap b = getSubimage(right, i, k, sizeBlock, sizeBlock);
                        
                        double dist = PearsonDistance(a,b);
                        if(dist <= min){
                            min = dist;
                            z = Math.abs(j-k);
                        }
                    }
                }
                
                map[i+sizeBlock][j+sizeBlock] = z;
            }
        }
        
        return map;
    }
    
    private double PearsonDistance(FastBitmap a, FastBitmap b){
        int size = sizeBlock*sizeBlock;
        double[] x = new double[size];
        double[] y = new double[size];
        for (int i = 0; i < size; i++) {
            x[i] = a.getGray(i);
            y[i] = b.getGray(i);
        }
        return 1 - Catalano.Statistics.Correlations.PearsonCorrelation(x, y);
    }
    
    private FastBitmap getSubimage(FastBitmap fastBitmap, int x, int y, int width, int height){
        FastBitmap copy = new FastBitmap(sizeBlock,sizeBlock,FastBitmap.ColorSpace.Grayscale);
        
        for (int i = x; i < x + height; i++) {
            for (int j = y; j < y + width; j++) {
                copy.setGray(i-x, j-y, fastBitmap.getGray(i, j));
            }
        }
        
        return copy;
    }
    
}