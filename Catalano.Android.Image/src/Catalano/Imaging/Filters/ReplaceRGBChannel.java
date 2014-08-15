/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Catalano.Imaging.Filters;

import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.IBaseInPlace;

/**
 *
 * @author Diego Catalano
 */
public class ReplaceRGBChannel implements IBaseInPlace{
    private FastBitmap band;
    public enum RGB{R, G, B};
    private RGB rgb;

    public ReplaceRGBChannel() {
        
    }
    
    public ReplaceRGBChannel(FastBitmap fastBitmap, RGB rgb) {
        this.band = fastBitmap;
        this.rgb = rgb;
    }

    public FastBitmap getBand() {
        return band;
    }

    public void setBand(FastBitmap band) {
        this.band = band;
    }

    public RGB getRGB() {
        return rgb;
    }

    public void setRGB(RGB rgb) {
        this.rgb = rgb;
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        if ((!band.isGrayscale()) || (!fastBitmap.isRGB())) {
            try {
                throw new Exception("ReplaceRGBChannel needs one image grayscale and another RGB image");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        if ((band.getWidth() != fastBitmap.getWidth()) || (band.getHeight() != fastBitmap.getHeight())) {
            try {
                throw new Exception("The image must be the same dimension");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        int width = fastBitmap.getWidth();
        int height = fastBitmap.getHeight();
        switch(rgb){
            case R:
                for (int x = 0; x < height; x++) {
                    for (int y = 0; y < width; y++) {
                        fastBitmap.setRed(x, y, band.getGray(x, y));
                    }
                }
            break;
                
            case G:
                for (int x = 0; x < height; x++) {
                    for (int y = 0; y < width; y++) {
                        fastBitmap.setGreen(x, y, band.getGray(x, y));
                    }
                }
            break;
                
            case B:
                for (int x = 0; x < height; x++) {
                    for (int y = 0; y < width; y++) {
                        fastBitmap.setBlue(x, y, band.getGray(x, y));
                    }
                }
            break;
        }
    }
}