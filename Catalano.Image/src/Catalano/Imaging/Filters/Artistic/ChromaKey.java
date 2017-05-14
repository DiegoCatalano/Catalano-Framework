/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Catalano.Imaging.Filters.Artistic;

import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.Filters.HSLFiltering;
import Catalano.Imaging.IApplyInPlace;

/**
 *
 * @author Diego
 */
public class ChromaKey implements IApplyInPlace{
    
    private FastBitmap background;
    private HSLFiltering filter;
    
    public void setBackgroundImage(FastBitmap image){
        this.background = image;
    }

    public ChromaKey(HSLFiltering filter, FastBitmap background) {
        this.filter = filter;
        this.background = background;
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        filter.setFillOutsideRange(false);
        filter.applyInPlace(fastBitmap);
        
        int size = fastBitmap.getSize();
        for (int i = 0; i < size; i++) 
            if(fastBitmap.getRed(i) == 0 && fastBitmap.getGreen(i) == 0 && fastBitmap.getBlue(i) == 0)
                fastBitmap.setRGB(i, background.getRGB(i));
    }
}