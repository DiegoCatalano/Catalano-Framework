/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Catalano.Vision.Tools;

import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.Filters.Subtract;
import Catalano.Imaging.Filters.Threshold;
import java.util.ArrayList;

/**
 *
 * @author Diego Catalano
 */
public class MotionEnergyImage {
    
    private FastBitmap background;
    private int threshold;

    public MotionEnergyImage() {
    }

    public MotionEnergyImage(FastBitmap background, int threshold) {
        this.background = background;
        this.threshold = threshold;
    }
    
    public FastBitmap Process(ArrayList<FastBitmap> sequence){
        
        int width = sequence.get(0).getWidth();
        int height = sequence.get(0).getHeight();
        
        FastBitmap image = new FastBitmap(width, height, FastBitmap.ColorSpace.Grayscale);
        
        int size = sequence.size();
        
        for (int i = 0; i < size; i++) {
            
        }
        
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                for (int k = 0; k < size; k++) {
                    
                    FastBitmap fb = sequence.get(k);
                    fb.toGrayscale();
                    
                    Subtract sub = new Subtract(background);
                    sub.applyInPlace(fb);
                    
                    Threshold t = new Threshold(threshold);
                    t.applyInPlace(fb);
                    
                    for (int l = 0; l < height; l++) {
                        for (int m = 0; m < width; m++) {
                            if(fb.getGray(l, m) == 255){
                                image.setGray(l, m, 255);
                            }
                        }
                    }
                }
            }
        }
        
        int energy = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if(image.getGray(i, j) == 255)
                    energy++;
            }
        }
        
        return image;
    }
    
    
    
}
