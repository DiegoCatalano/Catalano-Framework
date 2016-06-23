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

package Catalano.Imaging.Filters.Artistic;

import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.IApplyInPlace;

/**
 * Blend Filter.
 * Blend filter is used to determine how two layers are blended into each other.
 * Reference: http://en.wikipedia.org/wiki/Blend_modes
 * @author Diego Catalano
 */
public class Blend implements IApplyInPlace{
    
    /**
     * Blend algorithm.
     */
    public static enum Algorithm {

        /**
         * It selects the maximum of each component from the foreground and background pixels.
         */
        Lighten,

        /**
         * Creates a pixel that retains the smallest components of the foreground and background pixels.
         */
        Darken,

        /**
         * Simply multiplies each component in the two layers.
         */
        Multiply,

        /**
         * Simply average each component in the two layers.
         */
        Average,

        /**
         * This blend mode simply adds pixel values of one layer with the other. In case of values above 1 (in the case of RGB), white is displayed.
         */
        Add,

        /**
         * This blend mode simply subtracts pixel values of one layer with the other. In case of negative values, black is displayed.
         */
        Subtract,

        /**
         * Difference subtracts the top layer from the bottom layer or the other way round, to always get a positive value.
         * Blending with black produces no change, as values for all colors are 0. (The RGB value for black is 0,0,0). Blending with white inverts the picture.
         */
        Difference,

        /**
         * Produces the opposite effect to Difference. Instead of making colors darker, it makes them brighter.
         */
        Negation,

        /**
         * The Screen blend mode inverts both layers, multiplies them, and then inverts that result.
         */
        Screen,

        /**
         * Exclusion blending mode inverts lower layers according to the brightness values in the active layer.
         * White inverts the composite pixels absolutely, black inverts them not at all, and the other brightness values invert them to some degree in between.
         */
        Exclusion,

        /**
         * Overlay combines Multiply and Screen blend modes.
         * The parts of the top layer where base layer is light become lighter, the parts where the base layer is dark become darker. An overlay with the same picture looks like an S-curve.
         */
        Overlay,

        /**
         * This is a softer version of Hard Light. Applying pure black or white does not result in pure black or white.
         */
        SoftLight,

        /**
         * Hard Light combines Multiply and Screen blend modes. Equivalent to Overlay, but with the bottom and top images swapped.
         */
        HardLight,

        /**
         * Color Dodge blend mode divides the bottom layer by the inverted top layer.
         */
        ColorDodge,

        /**
         * Color Burn mode divides the inverted bottom layer by the top layer, and then inverts the result.
         */
        ColorBurn,

        /**
         * Linear Light combines Linear Dodge and Linear Burn (rescaled so that neutral colors become middle gray).
         * Dodge applies to values of top layer lighter than middle gray, and burn to darker values.
         * The calculation simplifies to the sum of bottom layer and twice the top layer, subtract 1. The contrast decreases.
         */
        LinearLight,

        /**
         * Vivid Light combines Color Dodge and Color Burn (rescaled so that neutral colors become middle gray).
         * Dodge applies when values in the top layer are lighter than middle gray, and burn to darker values.
         * The middle gray is the neutral color. When color is lighter than this, this effectively moves the white point of the bottom 
         * layer down by twice the difference; when it is darker, the black point is moved up by twice the difference. (The perceived contrast increases.)
         */
        VividLight,

        /**
         * Pin Light combines lighten and darken modes.
         */
        PinLight,

        /**
         * Reflect can be used for adding shiny objects or areas of light.
         */
        Reflect,

        /**
         * Phoenix mode.
         */
        Phoenix};
    private Algorithm algorithm;
    private FastBitmap overlay;

    /**
     * Get Overlay image.
     * @return Overlay image.
     */
    public FastBitmap getOverlay() {
        return overlay;
    }

    /**
     * Set Overlay image.
     * @param overlay Overlay image.
     */
    public void setOverlay(FastBitmap overlay) {
        this.overlay = overlay;
    }

    /**
     * Get Blend algorithm.
     * @return Blend algorithm.
     */
    public Algorithm getAlgorithm() {
        return algorithm;
    }

    /**
     * Set Blend algorithm.
     * @param algorithm Blend algorithm.
     */
    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    /**
     * Initialize a new instance of the Blend class.
     * @param overlay Overlay image.
     * @param algorithm Blend algorithm.
     */
    public Blend(FastBitmap overlay, Algorithm algorithm) {
        this.overlay = overlay;
        this.algorithm = algorithm;
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        if (fastBitmap.isRGB() && overlay.isRGB()){
            
            int size = fastBitmap.getWidth() * fastBitmap.getHeight();
            
            switch(algorithm){
                case Lighten:
                    for (int i = 0; i < size; i++) {
                        if (overlay.getRed(i) > fastBitmap.getRed(i)){
                            fastBitmap.setRed(i, overlay.getRed(i));
                        }
                        if (overlay.getGreen(i) > fastBitmap.getGreen(i)){
                            fastBitmap.setGreen(i, overlay.getGreen(i));
                        }
                        if (overlay.getBlue(i) > fastBitmap.getBlue(i)){
                            fastBitmap.setBlue(i, overlay.getBlue(i));
                        }
                    }
                break;
                case Darken:
                    for (int i = 0; i < size; i++) {
                        if (overlay.getRed(i) < fastBitmap.getRed(i)){
                            fastBitmap.setRed(i, overlay.getRed(i));
                        }
                        if (overlay.getGreen(i) < fastBitmap.getGreen(i)){
                            fastBitmap.setGreen(i, overlay.getGreen(i));
                        }
                        if (overlay.getBlue(i) < fastBitmap.getBlue(i)){
                            fastBitmap.setBlue(i, overlay.getBlue(i));
                        }
                    }
                break;
                case Multiply:
                    for (int i = 0; i < size; i++) {
                        int r = fastBitmap.getRed(i) * overlay.getRed(i) / 255;
                        int g = fastBitmap.getGreen(i) * overlay.getGreen(i) / 255;
                        int b = fastBitmap.getBlue(i) * overlay.getBlue(i) / 255;
                        fastBitmap.setRGB(i, r, g, b);
                    }
                break;
                case Average:
                    for (int i = 0; i < size; i++) {
                        int r = fastBitmap.getRed(i) * overlay.getRed(i) / 2;
                        int g = fastBitmap.getGreen(i) * overlay.getGreen(i) / 2;
                        int b = fastBitmap.getBlue(i) * overlay.getBlue(i) / 2;
                        fastBitmap.setRGB(i, r, g, b);
                    }
                break;
                case Add:
                    for (int i = 0; i < size; i++) {
                        int r = Math.min(fastBitmap.getRed(i) + overlay.getRed(i), 255);
                        int g = Math.min(fastBitmap.getGreen(i) + overlay.getGreen(i), 255);
                        int b = Math.min(fastBitmap.getBlue(i) + overlay.getBlue(i), 255);
                        fastBitmap.setRGB(i, r, g, b);
                    }
                break;
                case Subtract:
                    for (int i = 0; i < size; i++) {
                        int temp = fastBitmap.getRed(i) + overlay.getRed(i);
                        if(temp < 255){
                            fastBitmap.setRed(i, 0);
                        }
                        else{
                            fastBitmap.setRed(i, temp - 255);
                        }

                        temp = fastBitmap.getGreen(i) + overlay.getGreen(i);
                        if(temp < 255){
                            fastBitmap.setGreen(i, 0);
                        }
                        else{
                            fastBitmap.setGreen(i, temp - 255);
                        }

                        temp = fastBitmap.getBlue(i) + overlay.getBlue(i);
                        if(temp < 255){
                            fastBitmap.setBlue(i, 0);
                        }
                        else{
                            fastBitmap.setBlue(i, temp - 255);
                        }
                    }
                break;
                case Difference:
                    for (int i = 0; i < size; i++) {
                        int r = Math.abs(fastBitmap.getRed(i) - overlay.getRed(i));
                        int g = Math.abs(fastBitmap.getGreen(i) - overlay.getGreen(i));
                        int b = Math.abs(fastBitmap.getBlue(i) - overlay.getBlue(i));
                        fastBitmap.setRGB(i, r, g, b);
                    }
                break;
                case Negation:
                    for (int i = 0; i < size; i++) {
                        int r = 255 - Math.abs(255 - fastBitmap.getRed(i) - overlay.getRed(i));
                        int g = 255 - Math.abs(255 - fastBitmap.getGreen(i) - overlay.getGreen(i));
                        int b = 255 - Math.abs(255 - fastBitmap.getBlue(i) - overlay.getBlue(i));
                        fastBitmap.setRGB(i, r, g, b);
                    }
                break;
                case Screen:
                    for (int i = 0; i < size; i++) {
                        int r = ((255 - (((255 - fastBitmap.getRed(i)) * (255 - overlay.getRed(i))) >> 8)));
                        int g = ((255 - (((255 - fastBitmap.getGreen(i)) * (255 - overlay.getGreen(i))) >> 8)));
                        int b = ((255 - (((255 - fastBitmap.getBlue(i)) * (255 - overlay.getBlue(i))) >> 8)));
                        fastBitmap.setRGB(i, r, g, b);
                    }
                break;
                case Exclusion:
                    for (int i = 0; i < size; i++) {
                        int r = ((fastBitmap.getRed(i) + overlay.getRed(i) - 2 * fastBitmap.getRed(i) * overlay.getRed(i) / 255));
                        int g = ((fastBitmap.getGreen(i) + overlay.getGreen(i) - 2 * fastBitmap.getGreen(i) * overlay.getGreen(i) / 255));
                        int b = ((fastBitmap.getBlue(i) + overlay.getBlue(i) - 2 * fastBitmap.getBlue(i) * overlay.getBlue(i) / 255));
                        fastBitmap.setRGB(i, r, g, b);
                    }
                break;
                case Overlay:
                    for (int i = 0; i < size; i++) {
                        int temp;
                        if(overlay.getRed(i) < 128){
                            temp = (2 * fastBitmap.getRed(i) * overlay.getRed(i) / 255);
                            temp = Math.min(255, temp);
                            fastBitmap.setRed(i, temp);
                        }
                        else{
                            temp = (255 - 2 * (255 - fastBitmap.getRed(i)) * (255 - overlay.getRed(i)) / 255);
                            temp = Math.min(255, temp);
                            fastBitmap.setRed(i, temp);
                        }

                        if(overlay.getGreen(i) < 128){
                            temp = (2 * fastBitmap.getGreen(i) * overlay.getGreen(i) / 255);
                            temp = Math.min(255, temp);
                            fastBitmap.setGreen(i, temp);
                        }
                        else{
                            temp = (255 - 2 * (255 - fastBitmap.getGreen(i)) * (255 - overlay.getGreen(i)) / 255);
                            temp = Math.min(255, temp);
                            fastBitmap.setGreen(i, temp);
                        }

                        if(overlay.getBlue(i) < 128){
                            temp = (2 * fastBitmap.getBlue(i) * overlay.getBlue(i) / 255);
                            temp = Math.min(255, temp);
                            fastBitmap.setBlue(i, temp);
                        }
                        else{
                            temp = (255 - 2 * (255 - fastBitmap.getBlue(i)) * (255 - overlay.getBlue(i)) / 255);
                            temp = Math.min(255, temp);
                            fastBitmap.setBlue(i, temp);
                        }
                    }
                break;
                case SoftLight:
                    for (int i = 0; i < size; i++) {
                        int temp;
                        if(fastBitmap.getRed(i) < 128){
                            temp = (2 * overlay.getRed(i) * fastBitmap.getRed(i) / 255);
                            temp = Math.min(255, temp);
                            fastBitmap.setRed(i, temp);
                        }
                        else{
                            temp = (255 - 2 * (255 - overlay.getRed(i)) * (255 - fastBitmap.getRed(i)) / 255);
                            temp = Math.min(255, temp);
                            overlay.setRed(i, temp);
                        }

                        if(fastBitmap.getGreen(i) < 128){
                            temp = (2 * overlay.getGreen(i) * fastBitmap.getGreen(i) / 255);
                            temp = Math.min(255, temp);
                            fastBitmap.setGreen(i, temp);
                        }
                        else{
                            temp = (255 - 2 * (255 - overlay.getGreen(i)) * (255 - fastBitmap.getGreen(i)) / 255);
                            temp = Math.min(255, temp);
                            fastBitmap.setGreen(i, temp);
                        }

                        if(fastBitmap.getBlue(i) < 128){
                            temp = (2 * overlay.getBlue(i) * fastBitmap.getBlue(i) / 255);
                            temp = Math.min(255, temp);
                            fastBitmap.setBlue(i, temp);
                        }
                        else{
                            temp = (255 - 2 * (255 - overlay.getBlue(i)) * (255 - fastBitmap.getBlue(i)) / 255);
                            temp = Math.min(255, temp);
                            fastBitmap.setBlue(i, temp);
                        }
                    }
                break;
                case HardLight:
                    for (int i = 0; i < size; i++) {
                        float temp;
                        if(overlay.getRed(i) < 128){
                            temp = (2 * ((fastBitmap.getRed(i) >> 1) + 64)) * ((float)overlay.getRed(i) / 255);
                            fastBitmap.setRed(i, (int)temp);
                        }
                        else{
                            temp = (255 - (2 * (255 - ((fastBitmap.getRed(i) >> 1) + 64)) * (float)(255 - overlay.getRed(i)) / 255));
                            fastBitmap.setRed(i, (int)temp);
                        }

                        if(overlay.getGreen(i) < 128){
                            temp = (2 * ((fastBitmap.getGreen(i) >> 1) + 64)) * ((float)overlay.getGreen(i) / 255);
                            fastBitmap.setGreen(i, (int)temp);
                        }
                        else{
                            temp = (255 - (2 * (255 - ((fastBitmap.getGreen(i) >> 1) + 64)) * (float)(255 - overlay.getGreen(i)) / 255));
                            fastBitmap.setGreen(i, (int)temp);
                        }

                        if(overlay.getBlue(i) < 128){
                            temp = (2 * ((fastBitmap.getBlue(i) >> 1) + 64)) * ((float)overlay.getBlue(i) / 255);
                            fastBitmap.setBlue(i, (int)temp);
                        }
                        else{
                            temp = (255 - (2 * (255 - ((fastBitmap.getBlue(i) >> 1) + 64)) * (float)(255 - overlay.getBlue(i)) / 255));
                            fastBitmap.setBlue(i, (int)temp);
                        }
                    }
                break;
                case ColorDodge:
                    for (int i = 0; i < size; i++) {
                        if (overlay.getRed(i) == 255){
                            fastBitmap.setRed(i, 255);
                        }
                        else{
                            int x = Math.min(255, ((fastBitmap.getRed(i) << 8) / (255 - overlay.getRed(i))));
                            fastBitmap.setRed(i, x);
                        }

                        if (overlay.getGreen(i) == 255){
                            fastBitmap.setGreen(i, 255);
                        }
                        else{
                            int x = Math.min(255, ((fastBitmap.getGreen(i) << 8) / (255 - overlay.getGreen(i))));
                            fastBitmap.setGreen(i, x);
                        }

                        if (overlay.getBlue(i) == 255){
                            fastBitmap.setBlue(i, 255);
                        }
                        else{
                            int x = Math.min(255, ((fastBitmap.getBlue(i) << 8) / (255 - overlay.getBlue(i))));
                            fastBitmap.setBlue(i, x);
                        }
                    }
                break;
                case ColorBurn:
                    for (int i = 0; i < size; i++) {
                        if (overlay.getRed(i) == 0){
                            fastBitmap.setRed(i, overlay.getRed(i));
                        }
                        else{
                            int x = Math.max(0, (255 - ((255 - fastBitmap.getRed(i)) << 8) / overlay.getRed(i)));
                            fastBitmap.setRed(i, x);
                        }

                        if (overlay.getGreen(i) == 0){
                            fastBitmap.setGreen(i, overlay.getGreen(i));
                        }
                        else{
                            int x = Math.max(0, (255 - ((255 - fastBitmap.getGreen(i)) << 8) / overlay.getGreen(i)));
                            fastBitmap.setGreen(i, x);
                        }

                        if (overlay.getBlue(i) == 0){
                            fastBitmap.setBlue(i, overlay.getBlue(i));
                        }
                        else{
                            int x = Math.max(0, (255 - ((255 - fastBitmap.getBlue(i)) << 8) / overlay.getBlue(i)));
                            fastBitmap.setBlue(i, x);
                        }
                    }
                break;
                case LinearLight:
                    for (int i = 0; i < size; i++) {
                        int temp;

                        if (overlay.getRed(i) < 128){
                            temp = fastBitmap.getRed(i) + (2 * overlay.getRed(i));
                            if(temp < 255){
                                fastBitmap.setRed(i, 0);
                            }
                            else{
                                fastBitmap.setRed(i, (temp - 255));
                            }
                        }
                        else{
                            int x = Math.min(fastBitmap.getRed(i) + (2 * (overlay.getRed(i) - 128)), 255);
                            fastBitmap.setRed(i, x);
                        }

                        if (overlay.getGreen(i) < 128){
                            temp = fastBitmap.getGreen(i) + (2 * overlay.getGreen(i));
                            if(temp < 255){
                                fastBitmap.setGreen(i, 0);
                            }
                            else{
                                fastBitmap.setGreen(i, (temp - 255));
                            }
                        }
                        else{
                            int x = Math.min(fastBitmap.getGreen(i) + (2 * (overlay.getGreen(i) - 128)), 255);
                            fastBitmap.setGreen(i, x);
                        }

                        if (overlay.getBlue(i) < 128){
                            temp = fastBitmap.getBlue(i) + (2 * overlay.getBlue(i));
                            if(temp < 255){
                                fastBitmap.setBlue(i, 0);
                            }
                            else{
                                fastBitmap.setBlue(i, (temp - 255));
                            }
                        }
                        else{
                            int x = Math.min(fastBitmap.getBlue(i) + (2 * (overlay.getBlue(i) - 128)), 255);
                            fastBitmap.setBlue(i, x);
                        }
                    }
                break;
                case VividLight:
                    for (int i = 0; i < size; i++) {
                        if(overlay.getRed(i) < 128){
                            //Color Burn
                            int o = overlay.getRed(i) * 2;
                            if (o == 0){
                                fastBitmap.setRed(i, o);
                            }
                            else{
                                int x = Math.max(0, (255 - ((255 - fastBitmap.getRed(i)) << 8) / o));
                                fastBitmap.setRed(i, x);
                            }
                        }
                        else{
                            //Color Dodge
                            int o = 2 * (overlay.getRed(i) - 128);
                            if (o == 255){
                                fastBitmap.setRed(i, 255);
                            }
                            else{
                                int x = Math.min(255, ((fastBitmap.getRed(i) << 8) / (255 - o)));
                                fastBitmap.setRed(i, x);
                            }
                        }

                        if(overlay.getGreen(i) < 128){
                            //Color Burn
                            int o = overlay.getGreen(i) * 2;
                            if (o == 0){
                                fastBitmap.setGreen(i, o);
                            }
                            else{
                                int x = Math.max(0, (255 - ((255 - fastBitmap.getGreen(i)) << 8) / o));
                                fastBitmap.setGreen(i, x);
                            }
                        }
                        else{
                            //Color Dodge
                            int o = 2 * (overlay.getGreen(i) - 128);
                            if (o == 255){
                                fastBitmap.setGreen(i, 255);
                            }
                            else{
                                int x = Math.min(255, ((fastBitmap.getGreen(i) << 8) / (255 - o)));
                                fastBitmap.setGreen(i, x);
                            }
                        }

                        if(overlay.getBlue(i) < 128){
                            //Color Burn
                            int o = overlay.getBlue(i) * 2;
                            if (o == 0){
                                fastBitmap.setBlue(i, o);
                            }
                            else{
                                int x = Math.max(0, (255 - ((255 - fastBitmap.getBlue(i)) << 8) / o));
                                fastBitmap.setBlue(i, x);
                            }
                        }
                        else{
                            //Color Dodge
                            int o = 2 * (overlay.getBlue(i) - 128);
                            if (o == 255){
                                fastBitmap.setGreen(i, 255);
                            }
                            else{
                                int x = Math.min(255, ((fastBitmap.getBlue(i) << 8) / (255 - o)));
                                fastBitmap.setBlue(i, x);
                            }
                        }
                    }
                break;
                case PinLight:
                    for (int i = 0; i < size; i++) {
                        int o = overlay.getRed(i) * 2;
                        if (overlay.getRed(i) < 128){
                            //Darken
                            if (o < fastBitmap.getRed(i)){
                                fastBitmap.setRed(i, o);
                            }
                        }
                        else{
                            //Lighten
                            if (o > fastBitmap.getRed(i)){
                                fastBitmap.setRed(i, o);
                            }
                        }

                        o = overlay.getGreen(i) * 2;
                        if (overlay.getGreen(i) < 128){
                            //Darken
                            if (o < fastBitmap.getGreen(i)){
                                fastBitmap.setGreen(i, o);
                            }
                        }
                        else{
                            //Lighten
                            if (o > fastBitmap.getGreen(i)){
                                fastBitmap.setGreen(i, o);
                            }
                        }

                        o = overlay.getBlue(i) * 2;
                        if (overlay.getBlue(i) < 128){
                            //Darken
                            if (o < fastBitmap.getBlue(i)){
                                fastBitmap.setBlue(i, o);
                            }
                        }
                        else{
                            //Lighten
                            if (o > fastBitmap.getBlue(i)){
                                fastBitmap.setBlue(i, o);
                            }
                        }
                    }
                    
                break;
                case Reflect:
                    for (int i = 0; i < size; i++) {
                        if (overlay.getRed(i) == 255){
                            fastBitmap.setRed(i, 255);
                        }
                        else{
                            int x = Math.min(255, (fastBitmap.getRed(i) * fastBitmap.getRed(i) / (255 - overlay.getRed(i))));
                            fastBitmap.setRed(i, x);
                        }

                        if (overlay.getGreen(i) == 255){
                            fastBitmap.setGreen(i, 255);
                        }
                        else{
                            int x = Math.min(255, (fastBitmap.getGreen(i) * fastBitmap.getGreen(i) / (255 - overlay.getGreen(i))));
                            fastBitmap.setGreen(i, x);
                        }

                        if (overlay.getBlue(i) == 255){
                            fastBitmap.setBlue(i, 255);
                        }
                        else{
                            int x = Math.min(255, (fastBitmap.getBlue(i) * fastBitmap.getBlue(i) / (255 - overlay.getBlue(i))));
                            fastBitmap.setBlue(i, x);
                        }
                    }
                break;
                case Phoenix:
                    for (int i = 0; i < size; i++) {
                        int r = ((Math.min(fastBitmap.getRed(i), overlay.getRed(i)) - Math.max(fastBitmap.getRed(i), overlay.getRed(i)) + 255));
                        int g = ((Math.min(fastBitmap.getGreen(i), overlay.getGreen(i)) - Math.max(fastBitmap.getGreen(i), overlay.getGreen(i)) + 255));
                        int b = ((Math.min(fastBitmap.getBlue(i), overlay.getBlue(i)) - Math.max(fastBitmap.getBlue(i), overlay.getBlue(i)) + 255));

                        fastBitmap.setRGB(i, r, g, b);
                    }
                break;
            }
            
        }
        else{
            throw new IllegalArgumentException("Blend only works in RGB images.");
        }
    }
}