// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2014
// diego.catalano at live.com
//
// Copyright © Andrew Kirillov, 2007-2008
// andrew.kirillov at gmail.com
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

package Catalano.Imaging.Tools;

import Catalano.Imaging.FastBitmap;
import Catalano.Statistics.Histogram;

/**
 * Gather statistics about image in Gray or RGB color space.
 * @author Diego Catalano
 */
public class ImageStatistics {
    
    private Histogram gray;
    private Histogram red;
    private Histogram green;
    private Histogram blue;
    
    private int pixels;

    /**
     * Histogram of gray channel.
     * @return Histogram.
     */
    public Histogram getHistogramGray(){
        if (gray == null) {
            try {
                throw new Exception("Histogram gray null");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return gray;
    }
    
    /**
     * Histogram of red channel.
     * @return Histogram.
     */
    public Histogram getHistogramRed() {
        if (red == null) {
            try {
                throw new Exception("Histogram red null");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return red;
    }
    
    /**
     * Histogram of green channel.
     * @return Histogram.
     */
    public Histogram getHistogramGreen() {
        if (green == null) {
            try {
                throw new Exception("Histogram green null");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return green;
    }
        
    /**
     * Histogram of blue channel.
     * @return Histogram.
     */
    public Histogram getHistogramBlue() {
        if (blue == null) {
            try {
                throw new Exception("Histogram blue null");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return blue;
    }
    
    
    
    /**
     * Initialize a new instance of the ImageStatistics class.
     * @param fastBitmap Image to be processed.
     */
    public ImageStatistics(FastBitmap fastBitmap) {
        int width = fastBitmap.getWidth();
        int height = fastBitmap.getHeight();
        
        pixels = 0;
        red = green = blue = gray = null;
        
        if (fastBitmap.isGrayscale()) {
            int[] g = new int[256];
            
            int G;
            
            for (int x = 0; x < height; x++) {
                for (int y = 0; y < width; y++) {
                    G = fastBitmap.getGray(x, y);
                    
                    g[G]++;
                    pixels++;
                }
            }
            
            gray = new Histogram(g);
            
        }
        else if (fastBitmap.isRGB()){
            int[] r = new int[256];
            int[] g = new int[256];
            int[] b = new int[256];

            int R,G,B;

            for (int x = 0; x < height; x++) {
                for (int y = 0; y < width; y++) {
                    R = fastBitmap.getRed(x, y);
                    G = fastBitmap.getGreen(x, y);
                    B = fastBitmap.getBlue(x, y);

                    r[R]++;
                    g[G]++;
                    b[B]++;
                    pixels++;
                }
            }
            red = new Histogram(r);
            green = new Histogram(g);
            blue = new Histogram(b);
        }
    }
    
    /**
     * Initialize a new instance of the ImageStatistics class.
     * @param fastBitmap Image to be processed.
     * @param bins Number of bins.
     */
    public ImageStatistics(FastBitmap fastBitmap, int bins) {
        int width = fastBitmap.getWidth();
        int height = fastBitmap.getHeight();
        
        pixels = 0;
        red = green = blue = gray = null;
        
        if (fastBitmap.isGrayscale()) {
            int[] g = new int[bins];
            
            int G;
            
            for (int x = 0; x < height; x++) {
                for (int y = 0; y < width; y++) {
                    G = fastBitmap.getGray(x, y);
                    
                    int bG = G * bins / 256;
                    g[bG] = g[bG] + 1;
                    pixels++;
                }
            }
            
            gray = new Histogram(g);
            
        }
        else if (fastBitmap.isRGB()){
            int[] r = new int[bins];
            int[] g = new int[bins];
            int[] b = new int[bins];

            int R,G,B;

            for (int x = 0; x < height; x++) {
                for (int y = 0; y < width; y++) {
                    R = fastBitmap.getRed(x, y);
                    G = fastBitmap.getGreen(x, y);
                    B = fastBitmap.getBlue(x, y);
                    
                    int bR, bG, bB;
                    bR = R * bins / 256;
                    bG = G * bins / 256;
                    bB = B * bins / 256;
                    
                    r[bR] = r[bR] + 1;
                    r[bG] = r[bG] + 1;
                    r[bB] = r[bB] + 1;
                    
                    
                    pixels++;
                }
            }
            red = new Histogram(r);
            green = new Histogram(g);
            blue = new Histogram(b);
        }
        
    }

    /**
     * Count pixels.
     * @return amount of pixels.
     */
    public int PixelsCount() {
        return pixels;
    }
    
    /**
     * Calculate Mean value.
     * @param fastBitmap Image to be processed.
     * @return Mean.
     */
    public static float Mean(FastBitmap fastBitmap){
        return Mean(fastBitmap, 0, 0, fastBitmap.getWidth(), fastBitmap.getHeight());
    }
    
    /**
     * Calculate Mean value.
     * @param fastBitmap Image to be processed.
     * @param startX Initial X axis coordinate.
     * @param startY Initial Y axis coordinate.
     * @param width Width.
     * @param height Height.
     * @return Mean.
     */
    public static float Mean(FastBitmap fastBitmap, int startX, int startY, int width, int height){
        float mean = 0;
        if (fastBitmap.isGrayscale()){
            for (int i = startX; i < height; i++) {
                for (int j = startY; j < width; j++) {
                    mean += fastBitmap.getGray(i, j);
                }
            }
            return mean / (width * height);
        }
        else{
            throw new IllegalArgumentException("ImageStatistics: Only compute mean in grayscale images.");
        }
    }
    
    /**
     * Calculate Variance.
     * @param fastBitmap Image to be processed.
     * @return Variance.
     */
    public static float Variance(FastBitmap fastBitmap){
        float mean = Mean(fastBitmap);
        return Variance(fastBitmap, mean);
    }
    
    /**
     * Calculate Variance.
     * @param fastBitmap Image to be processed.
     * @param mean Mean.
     * @return Variance.
     */
    public static float Variance(FastBitmap fastBitmap, float mean){
        return Variance(fastBitmap, mean, 0, 0, fastBitmap.getWidth(), fastBitmap.getHeight());
    }
    
    /**
     * Calculate Variance.
     * @param fastBitmap Image to be processed.
     * @param mean Mean.
     * @param startX Initial X axis coordinate.
     * @param startY Initial Y axis coordinate.
     * @param width Width.
     * @param height Height.
     * @return Variance.
     */
    public static float Variance(FastBitmap fastBitmap, float mean, int startX, int startY, int width, int height){
        
        float sum = 0;
        if (fastBitmap.isGrayscale()){
            for (int i = startX; i < height; i++) {
                for (int j = startY; j < width; j++) {
                    sum += Math.pow(fastBitmap.getGray(i, j) - mean, 2);
                }
            }
            return sum / (float)((width * height) - 1);
        }
        else{
            throw new IllegalArgumentException("ImageStatistics: Only compute variance in grayscale images.");
        }
    }
    
    /**
     * Get maximum gray value in the image.
     * @param fastBitmap Image to be processed.
     * @return Maximum gray.
     */
    public static int Maximum (FastBitmap fastBitmap){
        return Maximum(fastBitmap, 0, 0, fastBitmap.getWidth(), fastBitmap.getHeight());
    }
    
    /**
     * Get maximum gray value in the image.
     * @param fastBitmap Image to be processed.
     * @param startX Initial X axis coordinate.
     * @param startY Initial Y axis coordinate.
     * @param width Width.
     * @param height Height.
     * @return Maximum gray.
     */
    public static int Maximum(FastBitmap fastBitmap, int startX, int startY, int width, int height){
        int max = 0;
        for (int i = startX; i < height; i++) {
            for (int j = startY; j < width; j++) {
                int gray = fastBitmap.getGray(i, j);
                if (gray > max) {
                    max = gray;
                }
            }
        }
        return max;
    }
    
    /**
     * Get minimum gray value in the image.
     * @param fastBitmap Image to be processed.
     * @return minimum gray.
     */
    public static int Minimum (FastBitmap fastBitmap){
        return Minimum(fastBitmap, 0, 0, fastBitmap.getWidth(), fastBitmap.getHeight());
    }
    
    /**
     * Get minimum gray value in the image.
     * @param fastBitmap Image to be processed.
     * @param startX Initial X axis coordinate.
     * @param startY Initial Y axis coordinate.
     * @param width Width.
     * @param height Height.
     * @return Minimum gray.
     */
    public static int Minimum(FastBitmap fastBitmap, int startX, int startY, int width, int height){
        int min = 255;
        for (int i = startX; i < height; i++) {
            for (int j = startY; j < width; j++) {
                int gray = fastBitmap.getGray(i, j);
                if (gray < min) {
                    min = gray;
                }
            }
        }
        return min;
    }
}