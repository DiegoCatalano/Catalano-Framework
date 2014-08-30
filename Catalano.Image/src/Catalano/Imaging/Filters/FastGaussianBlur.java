// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2014
// diego.catalano at live.com
//
// Copyright © Ivan Kuckir, 2013
// ivan at kuckir.com
//
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

package Catalano.Imaging.Filters;

import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.IBaseInPlace;

/**
 * Fast Gaussian Blur.
 * Reference: http://blog.ivank.net/fastest-gaussian-blur.html
 * @author Diego Catalano
 */
public class FastGaussianBlur implements IBaseInPlace{
    
    private int r = 1;
    
    /**
     * Get Radius.
     * @return Radius.
     */
    public int getRadius(){
        return r;
    }
    
    /**
     * Set Radius.
     * @param radius Radius.
     */
    public void setRadius(int radius){
        this.r = Math.max(1, radius);
    }

    /**
     * Initialize a new instance of the FastGaussianBlur class.
     */
    public FastGaussianBlur() {}
    
    /**
     * Initialize a new instance of the FastGaussianBlur class.
     * @param radius Radius.
     */
    public FastGaussianBlur(int radius){
        setRadius(radius);
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        FastBitmap copy = new FastBitmap(fastBitmap);
        
        if(fastBitmap.isGrayscale()){
            BoxBlurGray(copy, fastBitmap, r);
            BoxBlurGray(fastBitmap, copy, r);
            BoxBlurGray(copy, fastBitmap, r);
        }
        else if(fastBitmap.isRGB()){
            BoxBlurRGB(copy, fastBitmap, r);
            BoxBlurRGB(fastBitmap, copy, r);
            BoxBlurRGB(copy, fastBitmap, r);
        }
        
    }
    
    private void BoxBlurGray(FastBitmap copy, FastBitmap original, int r){
        
        int width = original.getWidth();
        int height = original.getHeight();
        
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                original.setGray(i, j, copy.getGray(i, j));
            }
        }
        
        BoxBlurH_Gray(original, copy, r);
        BoxBlurT_Gray(copy, original, r);
        
    }
    
    private void BoxBlurRGB(FastBitmap copy, FastBitmap original, int r){
        
        int width = original.getWidth();
        int height = original.getHeight();
        
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                original.setRGB(i, j, copy.getRGB(i, j));
            }
        }
        
        BoxBlurH_RGB(original, copy, r);
        BoxBlurT_RGB(copy, original, r);
        
    }
    
    private void BoxBlurH_Gray (FastBitmap copy, FastBitmap original, int r) {
        
        int width = original.getWidth();
        int height = original.getHeight();
        
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++)  {
                
                int fx = Math.max(0, j-r);
                int tx = Math.min(width, j+r+1);
                int val = 0;
                
                for(int x = fx; x < tx; x++){
                    val += copy.getGray(i, x);
                }
                original.setGray(i, j, val/(tx-fx));
            }
        }
    }
    
    private void BoxBlurH_RGB (FastBitmap copy, FastBitmap original, int r) {
        
        int width = original.getWidth();
        int height = original.getHeight();
        
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++)  {
                
                int fx = Math.max(0, j-r);
                int tx = Math.min(width, j+r+1);
                int valR = 0;
                int valG = 0;
                int valB = 0;
                
                for(int x = fx; x < tx; x++){
                    valR += copy.getRed(i, x);
                    valG += copy.getGreen(i, x);
                    valB += copy.getBlue(i, x);
                }
                
                valR /= (tx-fx);
                valG /= (tx-fx);
                valB /= (tx-fx);
                
                original.setRGB(i, j, valR, valG, valB);
            }
        }
    }
    
    private void BoxBlurT_Gray (FastBitmap copy, FastBitmap original, int r) {
        
        int width = original.getWidth();
        int height = original.getHeight();
        
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++)  {
                
                int fy = Math.max(0, i-r);
                int ty = Math.min(height, i+r+1);
                int val = 0;
                
                for(int y = fy; y < ty; y++){
                    val += copy.getGray(y, j);
                }
                
                val /= (ty-fy);
                
                original.setGray(i, j, val);
            }
        }
    }
    
    private void BoxBlurT_RGB (FastBitmap copy, FastBitmap original, int r) {
        
        int width = original.getWidth();
        int height = original.getHeight();
        
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++)  {
                
                int fy = Math.max(0, i-r);
                int ty = Math.min(height, i+r+1);
                int valR = 0;
                int valG = 0;
                int valB = 0;
                
                for(int y = fy; y < ty; y++){
                    valR += copy.getRed(y, j);
                    valG += copy.getGreen(y, j);
                    valB += copy.getBlue(y, j);
                }
                
                valR /= (ty-fy);
                valG /= (ty-fy);
                valB /= (ty-fy);
                
                original.setRGB(i, j, valR, valG, valB);
            }
        }
    }
    
}
