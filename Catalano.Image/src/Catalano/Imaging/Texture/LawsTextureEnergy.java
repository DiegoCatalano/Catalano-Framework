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

package Catalano.Imaging.Texture;

import Catalano.Core.IntPoint;
import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.Filters.Convolution;

/**
 * Laws Texture Energy Masks works by finding the average gray: Level, Edges, Spots, Ripples, and Waves in the image.
 * @author Diego Catalano
 */
public class LawsTextureEnergy{
    
    /**
     * Level
     */
    public static final int[] Level =   {1, 4, 6, 4, 1};
    
    /**
     * Edges
     */
    public static final int[] Edges =   {-1, -2, 0, 2, 1};
    
    /**
     * Spots
     */
    public static final int[] Spots =   {-1, 0, 2, 0, -1};
    
    /**
     * Ripples
     */
    public static final int[] Ripples = {1, -4, 6, -4, 1};
    
    /**
     * Waves
     */
    public static final int[] Waves =   {-1, 2, 0, -2, 1};
    
    private int[][] mask;
    private int[] vector1, vector2;
    private FastBitmap fb;

    /**
     * Initialize a new instance of the LawsTextureEnergy class.
     */
    public LawsTextureEnergy() {}

    /**
     * Initialize a new instance of the LawsTextureEnergy class.
     * @param vector1 Vector1.
     * @param vector2 Vector2.
     */
    public LawsTextureEnergy(int[] vector1, int[] vector2) {
        this.vector1 = vector1;
        this.vector2 = vector2;
    }

    /**
     * Initialize a new instance of the LawsTextureEnergy class.
     * @param mask Mask.
     */
    public LawsTextureEnergy(int[][] mask) {
        this.mask = mask;
    }

    /**
     * Get vector1.
     * @return Vector1.
     */
    public int[] getVector1() {
        return vector1;
    }

    /**
     * Set vector1.
     * @param vector1 Vector1.
     */
    public void setVector1(int[] vector1) {
        this.vector1 = vector1;
    }

    /**
     * Get vector2.
     * @return Vector2.
     */
    public int[] getVector2() {
        return vector2;
    }

    /**
     * Set vector2.
     * @param vector2 Vector2.
     */
    public void setVector2(int[] vector2) {
        this.vector2 = vector2;
    }

    /**
     * Process image to produce the texture filtered images.
     * @param fastBitmap Image to be processed.
     */
    public void ProcessImage(FastBitmap fastBitmap) {
        if (fastBitmap.isGrayscale()){
            this.fb = fastBitmap;
            mask = CreateMask(vector1, vector2);
            Convolution c = new Convolution(mask);
            c.applyInPlace(fb);
        }
    }
    
    /**
     * Returns image processed.
     * @return Image processed.
     */
    public FastBitmap toFastBitmap(){
        return fb;
    }
    
    /**
     * Compute texture energy map. (Entire image)
     * @return Texture energy map.
     */
    public long getTextureEnergyMap(){
        
        int width = fb.getWidth();
        int height = fb.getHeight();
        
        long energy = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                energy += fb.getGray(i, j);
            }
        }
        
        return energy;
    }
    
    /**
     * Compute texture energy map.
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     * @param width Width.
     * @param height Height.
     * @return Texture energy map.
     */
    public long getTextureEnergyMap(int x, int y, int width, int height){
        
        long energy = 0;
        for (int i = x; i < i + height; i++) {
            for (int j = y; j < j + width; j++) {
                energy += fb.getGray(i, j);
            }
        }
        
        return energy;
    }
    
    /**
     * Compute texture energy map.
     * @param point Point contains X and Y axis coordinates.
     * @param width Width.
     * @param height Height.
     * @return Texture energy map.
     */
    public long getTextureEnergyMap(IntPoint point, int width, int height){
        return getTextureEnergyMap(point.x, point.y, width, height);
    }
    
    /**
     * Create mask based in vectors.
     * @param vector1 Vector1.
     * @param vector2 Vector2.
     * @return Mask.
     */
    private int[][] CreateMask(int[] vector1, int[] vector2){
        mask = new int[vector1.length][vector2.length];
        
        for (int i = 0; i < vector1.length; i++) {
            for (int j = 0; j < vector2.length; j++) {
                mask[i][j] = vector1[i] * vector2[j];
            }
        }
        
        return mask;
    }
}