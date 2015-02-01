/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Catalano.Vision.Temporal;

import Catalano.Imaging.FastBitmap;
import Catalano.Vision.ITemporal;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Diego
 */
public class MedianBackgroundDetector implements ITemporal{

    public MedianBackgroundDetector() {}

    @Override
    public FastBitmap Process(List<FastBitmap> sequenceImage) {
        
        int width = sequenceImage.get(0).getWidth();
        int height = sequenceImage.get(0).getHeight();
        
        FastBitmap background = new FastBitmap(width, height, sequenceImage.get(0).getColorSpace());
        
        if (background.isGrayscale()){
            
            int size = sequenceImage.size();
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    int[] values = new int[size];
                    for (int k = 0; k < size; k++) {
                        values[k] = sequenceImage.get(k).getGray(i, j);
                    }
                    Arrays.sort(values);
                    background.setGray(i, j, values[size / 2]);
                }
            }
        }
        else if (background.isRGB()){
            int size = sequenceImage.size();
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    int[] valuesR = new int[size];
                    int[] valuesG = new int[size];
                    int[] valuesB = new int[size];
                    for (int k = 0; k < size; k++) {
                        valuesR[k] = sequenceImage.get(k).getRed(i, j);
                        valuesG[k] = sequenceImage.get(k).getGreen(i, j);
                        valuesB[k] = sequenceImage.get(k).getBlue(i, j);
                    }
                    Arrays.sort(valuesR);
                    Arrays.sort(valuesG);
                    Arrays.sort(valuesB);
                    background.setRGB(i, j, valuesR[size/2], valuesG[size/2], valuesB[size/2]);
                }
            }
        }
        else{
            throw new IllegalArgumentException("Median Background Detector only works with grayscale or rgb images.");
        }
        
        return background;
        
    }
    
}