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
 * @author Diego
 */
public class IntegralVariance implements IBaseInPlace{
    
    private int radius;

    public IntegralVariance() {
    }

    public IntegralVariance(int radius) {
        this.radius = radius;
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        if(fastBitmap.isGrayscale()){
            //Compute the integral image with power 1.
            IntegralImage intImage = new IntegralImage(fastBitmap);

            //Compute the integral image with power 2.
            IntegralImage intImage2 = new IntegralImage(fastBitmap, 2);

            int width = fastBitmap.getWidth();
            int height = fastBitmap.getHeight();
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    float m1 = intImage.getRectangleMean(i, j, radius);
                    float m2 = intImage2.getRectangleMean(i, j, radius);
                    float val = m2 - (m1*m1);
                    fastBitmap.setGray(i, j, fastBitmap.clampValues((int)val, 0, 255));
                }
            }
        }
        else{
            throw new IllegalArgumentException("Integral variance only works in grayscale images.");
        }
    }
}