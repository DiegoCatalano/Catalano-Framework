/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Catalano.Imaging.Filters;

import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.IBaseInPlace;

/**
 * Sepia filter - old brown photo.
 * @author Diego Catalano
 */
public class Sepia implements IBaseInPlace{
    
    /**
     * Initializes a new instance of the FastBitmap.Filters.Sepia class.
     */
    public Sepia(){}
    
    @Override
    public void applyInPlace(FastBitmap fastBitmap){
        
        int width = fastBitmap.getWidth();
        int height = fastBitmap.getHeight();
        int r,g,b;
        double Y,I,Q;
        
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                
                r = fastBitmap.getRed(x, y);
                g = fastBitmap.getGreen(x, y);
                b = fastBitmap.getBlue(x, y);
                
                //YIQ Color Space
                Y = (0.299 * r) + (0.587 * g) + (0.114 * b);
                //I = (0.596 * r) - (0.274 * g) - (0.322 * b);
                //Q = (0.212 * r) - (0.523 * g) + (0.311 * b);
                
                //Update it
                I = 51;
                Q = 0;
                
                //Transform to RGB
                r = (int)((1.0 * Y) + (0.956 * I) + (0.621 * Q));
                g = (int)((1.0 * Y) - (0.272 * I) - (0.647 * Q));
                b = (int)((1.0 * Y) - (1.105 * I) + (1.702 * Q));
                
                //Fix values
                r = r < 0 ? 0 : r;
                r = r > 255 ? 255 : r;
                
                g = g < 0 ? 0 : g;
                g = g > 255 ? 255 : g;
                
                b = b < 0 ? 0 : b;
                b = b > 255 ? 255 : b;
                
                //Set pixels now
                fastBitmap.setRGB(x, y, r, g, b);
            }
        }
    }
}