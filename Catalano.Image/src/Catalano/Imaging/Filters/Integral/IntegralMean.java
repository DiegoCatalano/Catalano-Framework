/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Catalano.Imaging.Filters.Integral;

import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.IBaseInPlace;
import Catalano.Imaging.Tools.IntegralImage;

/**
 *
 * @author Diego Catalano
 */
public class IntegralMean implements IBaseInPlace{
    
    private int radius;

    public IntegralMean() {}

    public IntegralMean(int radius) {
        this.radius = radius;
    }
    
    @Override
    public void applyInPlace(FastBitmap fastBitmap){
        
        if(fastBitmap.isGrayscale()){
            
            //Compute the integral image
            IntegralImage ii = new IntegralImage(fastBitmap);
            
            int width = fastBitmap.getWidth();
            int height = fastBitmap.getHeight();
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    int v = (int)ii.getRectangleMean(i, j, radius);
                    fastBitmap.setGray(i, j, fastBitmap.clampValues(v, 0, 255));
                }
            }
        }
        else{
            throw new IllegalArgumentException("IntegralMean only works in grayscale images.");
        }
    }
}
