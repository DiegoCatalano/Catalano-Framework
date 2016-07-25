// Catalano Android Imaging Library
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

package Catalano.Imaging.Texture.BinaryPattern;

import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.Tools.ImageHistogram;
import Catalano.Math.Matrix;

/**
 * Uniform local binary patterns (LBPu2) is a type of feature used for classification in computer vision.
 * LBPu2 Encodes 256 LBP labels in 59 labels.
 * @author Diego Catalano
 */
public class UniformLocalBinaryPattern implements IBinaryPattern{
    
    private boolean nonUniform;

    /**
     * Check if needs the non uniform label.
     * @return True if needs the non uniform label, otherwise return false.
     */
    public boolean isNonUniform() {
        return nonUniform;
    }

    /**
     * Set if needs the non uniform label.
     * @param nonUniform True if needs the non uniform label.
     */
    public void setNonUniform(boolean nonUniform) {
        this.nonUniform = nonUniform;
    }

    /**
     * Initialize a new instance of the UniformLocalBinaryPattern class.
     */
    public UniformLocalBinaryPattern() {
        this(true);
    }
    
    /**
     * Initialize a new instance of the UniformLocalBinaryPattern class.
     * @param nonUniform True if needs the non uniform label.
     */
    public UniformLocalBinaryPattern(boolean nonUniform){
        this.nonUniform = nonUniform;
    }

    @Override
    public ImageHistogram ComputeFeatures(FastBitmap fastBitmap) {
        
        if(!fastBitmap.isGrayscale())
            throw new IllegalArgumentException("Uniform LBP only works in grayscale images.");
        
        LocalBinaryPattern lbp = new LocalBinaryPattern();
        return Encode(lbp.ComputeFeatures(fastBitmap),nonUniform);
        
    }
    
    /**
     * Encode a LBP 256 histogram in Uniform LBP.
     * @param hist Image histogram.
     * @return LBPu2.
     */
    public static ImageHistogram Encode(ImageHistogram hist){
        return Encode(hist, true);
    }
    
    /**
     * Encode a LBP 256 histogram in Uniform LBP.
     * @param hist Image histogram.
     * @param nonUniform True if needs the non uniform label.
     * @return LBPu2.
     */
    public static ImageHistogram Encode(ImageHistogram hist, boolean nonUniform){
        
        int[] h = new int[59];
        int[] values = hist.getValues();
        
        int idx = 0;
        int nonU = 0;
        for (int i = 0; i < 256; i++) {
            if(isUniform(i)){
                h[idx] = values[i];
                idx++;
            }
            else{
                nonU += values[i];
            }
        }
        
        h[58] = nonU;
        
        if(!nonUniform){
            h = Matrix.RemoveColumn(h, 58);
        }
        
        return new ImageHistogram(h);
        
    }
    
    private static boolean isUniform(int x){
        int n = trans(x);
        return n <= 2;
    }
    
    private static int trans(int x){
        
        String a = String.format("%8s", Integer.toBinaryString(x)).replace(' ', '0');
        int dif = 0;
        for (int i = 1; i < a.length(); i++) 
            if(a.charAt(i-1) != a.charAt(i)) dif++;
        
        if(a.charAt(7) != a.charAt(0)) dif++;
        
        return dif;
    }
    
}