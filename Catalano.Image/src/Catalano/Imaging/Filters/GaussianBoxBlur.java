// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
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
import Catalano.Imaging.IApplyInPlace;

/**
 * Gaussian Box Blur.
 * Reference: http://blog.ivank.net/fastest-gaussian-blur.html
 * @author Diego Catalano
 */
public class GaussianBoxBlur implements IApplyInPlace{
    
    private double std;
    private int r;
    
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
     * Initialize a new instance of the GaussianBoxBlur class.
     */
    public GaussianBoxBlur() {
        this(1);
    }
    
    /**
     * Initialize a new instance of the GaussianBoxBlur class.
     * @param standardDeviation Standard deviation.
     */
    public GaussianBoxBlur(double standardDeviation){
        this(standardDeviation, 3);
    }
    
    /**
     * Initialize a new instance of the GaussianBoxBlur class.
     * @param standartDeviation Standard deviation.
     * @param radius Radius.
     */
    public GaussianBoxBlur(double standartDeviation, int radius){
        this.std = standartDeviation;
        this.r = radius;
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        FastBitmap copy = new FastBitmap(fastBitmap.getWidth(), fastBitmap.getHeight(), fastBitmap.getColorSpace());
        int[] boxs = BoxesForGauss(std, r);
        
        if(fastBitmap.isGrayscale()){
            BoxBlurGray(fastBitmap, copy, (boxs[0] - 1) / 2);
            BoxBlurGray(copy, fastBitmap, (boxs[1] - 1) / 2);
            BoxBlurGray(fastBitmap, copy, (boxs[2] - 1) / 2);
        }
        else if(fastBitmap.isRGB()){
            BoxBlurRGB(fastBitmap, copy, (boxs[0] - 1) / 2);
            BoxBlurRGB(copy, fastBitmap, (boxs[1] - 1) / 2);
            BoxBlurRGB(fastBitmap, copy, (boxs[2] - 1) / 2);
        }
        
    }
    
    private void BoxBlurGray(FastBitmap source, FastBitmap dest, int r){

        int size = source.getSize();
        for (int i = 0; i < size; i++) {
            dest.setGray(i, source.getGray(i));
        }
        
        BoxBlurH_Gray(dest, source, r);
        BoxBlurT_Gray(source, dest, r);
        
    }
    
    private void BoxBlurRGB(FastBitmap source, FastBitmap dest, int r){
        
        int size = source.getSize();
        
        for (int i = 0; i < size; i++) {
            dest.setRGB(i, source.getRGB(i));
        }
        
        BoxBlurH_RGB(dest, source, r);
        BoxBlurT_RGB(source, dest, r);
        
    }
    
    private void BoxBlurH_Gray (FastBitmap source, FastBitmap dest, int r) {
        
        int w = source.getWidth();
        int h = source.getHeight();
        
        double iarr = 1 / (double)(r+r+1);
        for(int i=0; i<h; i++) {
            int ti = i*w, li = ti, ri = ti+r;
            int fv = source.getGray(ti), lv = source.getGray(ti+w-1), val = (r+1)*fv;
            for(int j=0; j<r; j++) val += source.getGray(ti+j);
            for(int j=0  ; j<=r ; j++) { val += source.getGray(ri++) - fv;                     dest.setGray(ti++, (int)Math.round(val*iarr)); }
            for(int j=r+1; j<w-r; j++) { val += source.getGray(ri++) - source.getGray(li++);   dest.setGray(ti++, (int)Math.round(val*iarr)); }
            for(int j=w-r; j<w  ; j++) { val += lv - source.getGray(li++);                     dest.setGray(ti++, (int)Math.round(val*iarr)); }
        }

    }
    
    private void BoxBlurH_RGB (FastBitmap source, FastBitmap dest, int r) {
        
        int w = source.getWidth();
        int h = source.getHeight();
        
        double iarr = 1 / (double)(r+r+1);
        
        //Red channel
        for(int i=0; i<h; i++) {
            int ti = i*w, li = ti, ri = ti+r;
            int fv = source.getRed(ti), lv = source.getRed(ti+w-1), val = (r+1)*fv;
            for(int j=0; j<r; j++) val += source.getRed(ti+j);
            for(int j=0  ; j<=r ; j++) { val += source.getRed(ri++) - fv;                     dest.setRed(ti++, (int)Math.round(val*iarr)); }
            for(int j=r+1; j<w-r; j++) { val += source.getRed(ri++) - source.getRed(li++);   dest.setRed(ti++, (int)Math.round(val*iarr)); }
            for(int j=w-r; j<w  ; j++) { val += lv - source.getRed(li++);                     dest.setRed(ti++, (int)Math.round(val*iarr)); }
        }
        
        //Green channel
        for(int i=0; i<h; i++) {
            int ti = i*w, li = ti, ri = ti+r;
            int fv = source.getGreen(ti), lv = source.getGreen(ti+w-1), val = (r+1)*fv;
            for(int j=0; j<r; j++) val += source.getGreen(ti+j);
            for(int j=0  ; j<=r ; j++) { val += source.getGreen(ri++) - fv;                     dest.setGreen(ti++, (int)Math.round(val*iarr)); }
            for(int j=r+1; j<w-r; j++) { val += source.getGreen(ri++) - source.getGreen(li++);   dest.setGreen(ti++, (int)Math.round(val*iarr)); }
            for(int j=w-r; j<w  ; j++) { val += lv - source.getGreen(li++);                     dest.setGreen(ti++, (int)Math.round(val*iarr)); }
        }
        
        //Blue channel
        for(int i=0; i<h; i++) {
            int ti = i*w, li = ti, ri = ti+r;
            int fv = source.getBlue(ti), lv = source.getBlue(ti+w-1), val = (r+1)*fv;
            for(int j=0; j<r; j++) val += source.getBlue(ti+j);
            for(int j=0  ; j<=r ; j++) { val += source.getBlue(ri++) - fv;                     dest.setBlue(ti++, (int)Math.round(val*iarr)); }
            for(int j=r+1; j<w-r; j++) { val += source.getBlue(ri++) - source.getBlue(li++);   dest.setBlue(ti++, (int)Math.round(val*iarr)); }
            for(int j=w-r; j<w  ; j++) { val += lv - source.getBlue(li++);                     dest.setBlue(ti++, (int)Math.round(val*iarr)); }
        }
        
    }
    
    private void BoxBlurT_Gray (FastBitmap copy, FastBitmap original, int r) {
        
        int w = original.getWidth();
        int h = original.getHeight();
        
        double iarr = 1 / (double)(r+r+1);
        for(int i=0; i<w; i++) {
            int ti = i, li = ti, ri = ti+r*w;
            int fv = copy.getGray(ti), lv = copy.getGray(ti+w*(h-1)), val = (r+1)*fv;
            for(int j=0; j<r; j++) val += copy.getGray(ti+j*w);
            for(int j=0  ; j<=r ; j++) { val += copy.getGray(ri) - fv     ;           original.setGray(ti, (int)Math.round(val*iarr));  ri+=w; ti+=w; }
            for(int j=r+1; j<h-r; j++) { val += copy.getGray(ri) - copy.getGray(li);  original.setGray(ti, (int)Math.round(val*iarr));  li+=w; ri+=w; ti+=w; }
            for(int j=h-r; j<h  ; j++) { val += lv      - copy.getGray(li);           original.setGray(ti, (int)Math.round(val*iarr));  li+=w; ti+=w; }
        }
    
    }
    
    private void BoxBlurT_RGB (FastBitmap copy, FastBitmap original, int r) {
        
        int w = original.getWidth();
        int h = original.getHeight();
        
        double iarr = 1 / (double)(r+r+1);
        
        //Red channel
        for(int i=0; i<w; i++) {
            int ti = i, li = ti, ri = ti+r*w;
            int fv = copy.getRed(ti), lv = copy.getRed(ti+w*(h-1)), val = (r+1)*fv;
            for(int j=0; j<r; j++) val += copy.getRed(ti+j*w);
            for(int j=0  ; j<=r ; j++) { val += copy.getRed(ri) - fv     ;           original.setRed(ti, (int)Math.round(val*iarr));  ri+=w; ti+=w; }
            for(int j=r+1; j<h-r; j++) { val += copy.getRed(ri) - copy.getRed(li);  original.setRed(ti, (int)Math.round(val*iarr));  li+=w; ri+=w; ti+=w; }
            for(int j=h-r; j<h  ; j++) { val += lv      - copy.getRed(li);           original.setRed(ti, (int)Math.round(val*iarr));  li+=w; ti+=w; }
        }
        
        //Green channel
        for(int i=0; i<w; i++) {
            int ti = i, li = ti, ri = ti+r*w;
            int fv = copy.getGreen(ti), lv = copy.getGreen(ti+w*(h-1)), val = (r+1)*fv;
            for(int j=0; j<r; j++) val += copy.getGreen(ti+j*w);
            for(int j=0  ; j<=r ; j++) { val += copy.getGreen(ri) - fv     ;           original.setGreen(ti, (int)Math.round(val*iarr));  ri+=w; ti+=w; }
            for(int j=r+1; j<h-r; j++) { val += copy.getGreen(ri) - copy.getGreen(li);  original.setGreen(ti, (int)Math.round(val*iarr));  li+=w; ri+=w; ti+=w; }
            for(int j=h-r; j<h  ; j++) { val += lv      - copy.getGreen(li);           original.setGreen(ti, (int)Math.round(val*iarr));  li+=w; ti+=w; }
        }
        
        //Blue channel
        for(int i=0; i<w; i++) {
            int ti = i, li = ti, ri = ti+r*w;
            int fv = copy.getBlue(ti), lv = copy.getBlue(ti+w*(h-1)), val = (r+1)*fv;
            for(int j=0; j<r; j++) val += copy.getBlue(ti+j*w);
            for(int j=0  ; j<=r ; j++) { val += copy.getBlue(ri) - fv     ;           original.setBlue(ti, (int)Math.round(val*iarr));  ri+=w; ti+=w; }
            for(int j=r+1; j<h-r; j++) { val += copy.getBlue(ri) - copy.getBlue(li);  original.setBlue(ti, (int)Math.round(val*iarr));  li+=w; ri+=w; ti+=w; }
            for(int j=h-r; j<h  ; j++) { val += lv      - copy.getBlue(li);           original.setBlue(ti, (int)Math.round(val*iarr));  li+=w; ti+=w; }
        }
        
    }
    
    private int[] BoxesForGauss(double sigma, int n)  // standard deviation, number of boxes
    {
        double wIdeal = Math.sqrt((12*sigma*sigma/(double)n)+1);  // Ideal averaging filter width 
        double wl = Math.floor(wIdeal);  if(wl%2==0) wl--;
        double wu = wl+2;

        double mIdeal = (12*sigma*sigma - n*wl*wl - 4*n*wl - 3*n)/(-4*wl - 4);
        double m = Math.round(mIdeal);
        // var sigmaActual = Math.sqrt( (m*wl*wl + (n-m)*wu*wu - n)/12 );

        int[] sizes = new int[n];
        for(int i=0; i<n; i++){
            if (i < m)
                sizes[i] = (int)wl;
            else
                sizes[i] = (int)wu;
        }
        
        return sizes;
    }
}