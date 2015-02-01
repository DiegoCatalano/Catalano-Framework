/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Catalano.Vision.Temporal;

import Catalano.Imaging.FastBitmap;
import Catalano.Vision.ITemporal;
import java.util.List;

/**
 *
 * @author Diego
 */
public class MeanBackgroundDetector implements ITemporal{

    public MeanBackgroundDetector() {}

    @Override
    public FastBitmap Process(List<FastBitmap> sequenceImage) {
        
        int width = sequenceImage.get(0).getWidth();
        int height = sequenceImage.get(0).getHeight();
        
        FastBitmap background = new FastBitmap(width, height, sequenceImage.get(0).getColorSpace());
        
        if (background.isGrayscale()){
            
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    int mean = 0;
                    for (FastBitmap fb : sequenceImage) {
                        mean += fb.getGray(i, j);
                    }
                    background.setGray(i, j, mean / sequenceImage.size());
                }
            }
        }
        else if (background.isRGB()){
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    int meanR = 0;
                    int meanG = 0;
                    int meanB = 0;
                    for (FastBitmap fb : sequenceImage) {
                        meanR += fb.getRed(i, j);
                        meanG += fb.getGreen(i, j);
                        meanB += fb.getBlue(i, j);
                    }
                    meanR /= sequenceImage.size();
                    meanG /= sequenceImage.size();
                    meanB /= sequenceImage.size();
                    background.setRGB(i, j, meanR, meanG, meanB);
                }
            }
        }
        else{
            throw new IllegalArgumentException("Mean Background Detector only works with grayscale or rgb images.");
        }
        
        return background;
        
    }
    
}