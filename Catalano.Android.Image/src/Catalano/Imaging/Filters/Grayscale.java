// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2014
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

package Catalano.Imaging.Filters;

import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.IBaseInPlace;

/**
 * Base class for image grayscaling.
 * @author Diego Catalano
 */
public class Grayscale implements IBaseInPlace{
    
    double redCoefficient = 0.2125, greenCoefficient = 0.7154, blueCoefficient = 0.0721;
    
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
         * 0.2125R + 0.7154G + 0.0721B
         */
        Luminosity,
        
        /**
         * Min(red, green, max)
         */
        MinimumDecomposition,
        
        /**
         * Max(red, gree, blue)
         */
        MaximumDecomposition
    };
    private Algorithm grayscaleMethod;
    private boolean isAlgorithm = false;

    /**
     * Initializes a new instance of the Grayscale class.
     * In this constructor, will be 0.2125B + 0.7154G + 0.0721B.
     */
    public Grayscale() {
        
    }

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
    
    /**
     * Apply filter to a FastBitmap.
     * @param fastBitmap Image to be processed.
     */
    @Override
    public void applyInPlace(FastBitmap fastBitmap){
            if(!isAlgorithm){
                int width = fastBitmap.getWidth();
                int height = fastBitmap.getHeight();
                double r,g,b,gray;

                FastBitmap l = new FastBitmap(width, height, FastBitmap.ColorSpace.Grayscale);

                for (int x = 0; x < height; x++) {
                    for (int y = 0; y < width; y++) {
                        r = fastBitmap.getRed(x, y);
                        g = fastBitmap.getGreen(x, y);
                        b = fastBitmap.getBlue(x, y);

                        gray = (r*redCoefficient+g*greenCoefficient+b*blueCoefficient);

                        l.setGray(x, y, (int)gray);
                    }
                }
                fastBitmap.setImage(l);
            }
            else{
                Apply(fastBitmap, this.grayscaleMethod);
            }
    }
    
    private void Apply(FastBitmap fastBitmap,Algorithm grayMethod){
            int width = fastBitmap.getWidth();
            int height = fastBitmap.getHeight();
            double r,g,b,gray;

            FastBitmap l = new FastBitmap(width, height, FastBitmap.ColorSpace.Grayscale);

            switch(grayMethod){
                case Lightness:

                    double max,min;
                    for (int x = 0; x < height; x++) {
                        for (int y = 0; y < width; y++) {
                            r = fastBitmap.getRed(x, y);
                            g = fastBitmap.getGreen(x, y);
                            b = fastBitmap.getBlue(x, y);

                            max = Math.max(r, g);
                            max = Math.max(max, b);
                            min = Math.min(r, g);
                            min = Math.min(min, b);
                            gray = (max+min)/2;

                            l.setGray(x, y, (int)gray);
                        }
                    }
                break;

                case Average:
                    for (int x = 0; x < height; x++) {
                        for (int y = 0; y < width; y++) {
                            r = fastBitmap.getRed(x, y);
                            g = fastBitmap.getGreen(x, y);
                            b = fastBitmap.getBlue(x, y);

                            gray = (r+g+b)/3;

                            l.setGray(x, y, (int)gray);
                        }
                    }
                break;

                case Luminosity:
                    for (int x = 0; x < height; x++) {
                        for (int y = 0; y < width; y++) {
                            r = fastBitmap.getRed(x, y);
                            g = fastBitmap.getGreen(x, y);
                            b = fastBitmap.getBlue(x, y);

                            gray = (r*0.2125+g*0.7154+b*0.0721);

                            l.setGray(x, y, (int)gray);
                        }
                    }
                break;
                    
                case MinimumDecomposition:
                    for (int x = 0; x < height; x++) {
                        for (int y = 0; y < width; y++) {
                            gray = fastBitmap.getRed(x, y);
                            gray = Math.min(gray, fastBitmap.getGreen(x, y));
                            gray = Math.min(gray, fastBitmap.getBlue(x, y));

                            l.setGray(x, y, (int)gray);
                        }
                    }
                break;
                    
                case MaximumDecomposition:
                    for (int x = 0; x < height; x++) {
                        for (int y = 0; y < width; y++) {
                            gray = fastBitmap.getRed(x, y);
                            gray = Math.max(gray, fastBitmap.getGreen(x, y));
                            gray = Math.max(gray, fastBitmap.getBlue(x, y));

                            l.setGray(x, y, (int)gray);
                        }
                    }
                break;
            }
            fastBitmap.setImage(l);
    }   
}