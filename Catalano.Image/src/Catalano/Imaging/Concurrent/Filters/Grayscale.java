// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
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

package Catalano.Imaging.Concurrent.Filters;

import Catalano.Imaging.Concurrent.Share;
import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.IApplyInPlace;

/**
 * Base class for image grayscaling.
 * @author Diego Catalano
 */
public class Grayscale implements IApplyInPlace {
    
    private double redCoefficient = 0.2125;
    private double greenCoefficient = 0.7154;
    private double blueCoefficient = 0.0721;
    private FastBitmap result;

    /**
     * Three methods for grayscale.
     */
    public static enum Algorithm {

        /**
         * (Max(red, green, blue) + Min(red, green, blue)) / 2
         */
        Lightness,
        
        /**
         * (red + green + blue) / 3
         */
        Average,
        
        /**
         * (red * green * blue) ^ 1/3
         */
        GeometricMean,
        
        /**
         * 0.2125R + 0.7154G + 0.0721B
         */
        Luminosity,
        
        /**
         * Min(red, green, blue)
         */
        MinimumDecomposition,
        
        /**
         * Max(red, green, blue)
         */
        MaximumDecomposition
    };
    private Algorithm grayscaleMethod = Algorithm.Luminosity;
    private boolean isAlgorithm = false;

    /**
     * Initializes a new instance of the Grayscale class.
     * In this constructor, will be 0.2125B + 0.7154G + 0.0721B.
     */
    public Grayscale() {}

    /**
     * Initializes a new instance of the Grayscale class. 
     * @param redCoefficient Portion of red channel's value to use during conversion from RGB to grayscale. 
     * @param greenCoefficient Portion of green channel's value to use during conversion from RGB to grayscale. 
     * @param blueCoefficient Portion of blue channel's value to use during conversion from RGB to grayscale. 
     */
    public Grayscale(double redCoefficient, double greenCoefficient, double blueCoefficient) {
        this.redCoefficient = redCoefficient;
        this.greenCoefficient = greenCoefficient;
        this.blueCoefficient = blueCoefficient;
        this.isAlgorithm = false;
    }
    
    /**
     * Initializes a new instance of the Grayscale class. 
     * @param grayscaleMethod Methods for grayscaling.
     */
    public Grayscale(Algorithm grayscaleMethod){
        this.grayscaleMethod = grayscaleMethod;
        this.isAlgorithm = true;
    }

    /**
     * Get red coefficient
     * @return red coefficient
     */
    public double getRedCoefficient() {
        return redCoefficient;
    }

    /**
     * Set red coefficient
     * @param redCoefficient red coefficient
     */
    public void setRedCoefficient(double redCoefficient) {
        this.redCoefficient = redCoefficient;
    }

    /**
     * Get green coefficient
     * @return green coefficient
     */
    public double getGreenCoefficient() {
        return greenCoefficient;
    }

    /**
     * Set green coefficient
     * @param greenCoefficient green coefficient
     */
    public void setGreenCoefficient(double greenCoefficient) {
        this.greenCoefficient = greenCoefficient;
    }

    /**
     * Get blue coefficient
     * @return blue coefficient
     */
    public double getBlueCoefficient() {
        return blueCoefficient;
    }

    /**
     * Set blue coefficient
     * @param blueCoefficient blue coefficient
     */
    public void setBlueCoefficient(double blueCoefficient) {
        this.blueCoefficient = blueCoefficient;
    }

    /**
     * Get Grayscale Method
     * @return Grayscale Method
     */
    public Algorithm getGrayscaleMethod() {
        return grayscaleMethod;
    }

    /**
     * Set Grayscale Method
     * @param grayscaleMethod Grayscale Method
     */
    public void setGrayscaleMethod(Algorithm grayscaleMethod) {
        this.grayscaleMethod = grayscaleMethod;
    }
    
    @Override
    public void applyInPlace(FastBitmap fb){

        if (fb.isRGB()){
            result = new FastBitmap(fb.getWidth(), fb.getHeight(), FastBitmap.ColorSpace.Grayscale);
            Parallel(fb);
        }
        else{
            throw new IllegalArgumentException("(Concurrent) Grayscale only works in RGB images.");
        }

    }
    
    private void Parallel(FastBitmap fastBitmap){
        
        int processors = Runtime.getRuntime().availableProcessors();
        
        Thread[] t = new Thread[processors];
        int part = fastBitmap.getHeight() / processors;
        int last = processors - 1;

        int startX = 0;
        for (int i = 0; i < processors; i++){
            if (i == last) part = fastBitmap.getHeight() - startX;
            t[i] = new Thread(new Run(new Share(fastBitmap, startX, startX += part)));
            t[i].start();
        }

        try {
            for (int i = 0; i < processors; i++) {
                t[i].join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        fastBitmap.setImage(result);
    }
    
    private class Run implements Runnable {

        private Share share;
        
        public Run(Share obj) {
            this.share = obj;
        }

        @Override
        public void run() {

            if (!isAlgorithm){
                double r,g,b,gray;
                
                for (int x = share.startX; x < share.endHeight; x++) {
                    for (int y = 0; y < share.fastBitmap.getWidth(); y++) {
                        r = share.fastBitmap.getRed(x, y);
                        g = share.fastBitmap.getGreen(x, y);
                        b = share.fastBitmap.getBlue(x, y);

                        gray = (r*redCoefficient+g*greenCoefficient+b*blueCoefficient);

                        result.setGray(x, y, (int)gray);
                    }
                }
            }
            else{
                double r,g,b,gray;
                
                switch(grayscaleMethod){
                    case Lightness:
                        // Lightness method
                        double max,min;
                        for (int x = share.startX; x < share.endHeight; x++) {
                            for (int y = 0; y < share.fastBitmap.getWidth(); y++) {
                                r = share.fastBitmap.getRed(x, y);
                                g = share.fastBitmap.getGreen(x, y);
                                b = share.fastBitmap.getBlue(x, y);

                                max = Math.max(r, g);
                                max = Math.max(max, b);
                                min = Math.min(r, g);
                                min = Math.min(min, b);
                                gray = (max+min)/2;

                                result.setGray(x, y, (int)gray);
                            }
                        }
                    break;

                    case Average:
                        for (int x = share.startX; x < share.endHeight; x++) {
                            for (int y = 0; y < share.fastBitmap.getWidth(); y++) {
                                r = share.fastBitmap.getRed(x, y);
                                g = share.fastBitmap.getGreen(x, y);
                                b = share.fastBitmap.getBlue(x, y);

                                gray = (r+g+b)/3;

                                result.setGray(x, y, (int)gray);
                            }
                        }
                    break;
                        
                    case GeometricMean:
                        for (int x = share.startX; x < share.endHeight; x++) {
                            for (int y = 0; y < share.fastBitmap.getWidth(); y++) {
                                r = share.fastBitmap.getRed(x, y);
                                g = share.fastBitmap.getGreen(x, y);
                                b = share.fastBitmap.getBlue(x, y);

                                gray = Math.pow(r*g*b,0.33);

                                result.setGray(x, y, (int)gray);
                            }
                        }
                    break;

                    case Luminosity:
                        for (int x = share.startX; x < share.endHeight; x++) {
                            for (int y = 0; y < share.fastBitmap.getWidth(); y++) {
                                r = share.fastBitmap.getRed(x, y);
                                g = share.fastBitmap.getGreen(x, y);
                                b = share.fastBitmap.getBlue(x, y);

                                gray = (r*0.2125+g*0.7154+b*0.0721);

                                result.setGray(x, y, (int)gray);
                            }
                        }
                    break;
                        
                    case MinimumDecomposition:
                        for (int x = share.startX; x < share.endHeight; x++) {
                            for (int y = 0; y < share.fastBitmap.getWidth(); y++) {
                                gray = share.fastBitmap.getRed(x, y);
                                gray = Math.min(gray, share.fastBitmap.getGreen(x, y));
                                gray = Math.min(gray, share.fastBitmap.getBlue(x, y));

                                result.setGray(x, y, (int)gray);
                            }
                        }
                    break;
                        
                    case MaximumDecomposition:
                        for (int x = share.startX; x < share.endHeight; x++) {
                            for (int y = 0; y < share.fastBitmap.getWidth(); y++) {
                                gray = share.fastBitmap.getRed(x, y);
                                gray = Math.max(gray, share.fastBitmap.getGreen(x, y));
                                gray = Math.max(gray, share.fastBitmap.getBlue(x, y));

                                result.setGray(x, y, (int)gray);
                            }
                        }
                    break;
                }
            }
        }
    }
}