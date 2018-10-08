// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2018
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
import Catalano.Imaging.IApplyInPlace;
import java.util.List;

/**
 * Hit-And-Miss Transform.
 * @author Diego Catalano
 */
public class HitAndMiss implements IApplyInPlace{
    
    /**
     * Mode.
     */
    public static enum Mode{
        /**
         * If the pixel match, set pixel white, otherwise set black.
         */
        HitAndMiss,
        
        /**
         * If the pixel match, set pixel black, otherwise set white.
         */
        Thinning
    }
    
    /**
     * Logic
     */
    public static enum Logic{
        
        /**
         * And.
         */
        And,
        
        /**
         * Or.
         */
        Or
    }
    
    private int[][] kernel;
    private List<int[][]> kernels;
    
    private Mode mode;
    private Logic logic;

    /**
     * Initializes a new instance of the HitAndMiss class.
     * 
     * 1  - Foreground
     * 0  - Background
     * -1 - Don't care
     * 
     * @param kernel Kernel.
     */
    public HitAndMiss(int[][] kernel) {
        this(kernel, Mode.HitAndMiss);
    }

    /**
     * Initializes a new instance of the HitAndMiss class.
     * 
     * 1  - Foreground
     * 0  - Background
     * -1 - Don't care
     * 
     * @param kernel Kernel.
     * @param mode Mode.
     */
    public HitAndMiss(int[][] kernel, Mode mode) {
        this.kernel = kernel;
        this.mode = mode;
    }
    
    /**
     * Initializes a new instance of the HitAndMiss class.
     * 
     * 1  - Foreground
     * 0  - Background
     * -1 - Don't care
     * 
     * @param kernels Kernels.
     * @param logic Logic.
     */
    public HitAndMiss(List<int[][]> kernels, Logic logic){
        this.kernels = kernels;
        this.logic = logic;
    }
    
    /**
     * Initializes a new instance of the HitAndMiss class.
     * 
     * 1  - Foreground
     * 0  - Background
     * -1 - Don't care
     * 
     * @param kernels Kernels.
     * @param logic Logic.
     * @param mode Mode.
     */
    public HitAndMiss(List<int[][]> kernels, Logic logic, Mode mode){
        this.kernels = kernels;
        this.logic = logic;
        this.mode = mode;
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        if(!fastBitmap.isGrayscale())
            throw new IllegalArgumentException("The image must be in grayscale.");
        
        if(kernels != null){
            apply(fastBitmap, kernels);
        }
        
        FastBitmap copy = new FastBitmap(fastBitmap);
        
        int width = fastBitmap.getWidth();
        int height = fastBitmap.getHeight();
        int l;
        
        int p = mode == Mode.HitAndMiss ? 255 : 0;
        
        int Xline,Yline;
        int lines = CalcLines(kernel);
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                l = copy.getGray(x, y);
                if (l == 255) {
                    int hits = 0;
                    for (int i = 0; i < kernel.length; i++) {
                        Xline = x + (i-lines);
                        for (int j = 0; j < kernel[0].length; j++) {
                            Yline = y + (j-lines);
                            if ((Xline >= 0) && (Xline < height) && (Yline >=0) && (Yline < width)) {
                                if (kernel[i][j] >= 0) {
                                    if(kernel[i][j] == 1 && copy.getGray(Xline, Yline) == 255)
                                        hits++;
                                    if(kernel[i][j] == 0 && copy.getGray(Xline, Yline) == 0)
                                        hits++;
                                }
                                else{
                                    hits++;
                                }
                            }
                        }
                    }
                    if(hits == 9) fastBitmap.setGray(x, y, p);
                }
            }
        }
    }
    
    private void apply(FastBitmap fastBitmap, List<int[][]> kernels){
        if(!fastBitmap.isGrayscale())
            throw new IllegalArgumentException("The image must be in grayscale.");
        
        FastBitmap copy = new FastBitmap(fastBitmap);
        
        int width = fastBitmap.getWidth();
        int height = fastBitmap.getHeight();
        int l;
        
        int p = mode == Mode.HitAndMiss ? 255 : 0;
        int pMatches = logic == Logic.And ? 9*kernels.size() : 9;
        
        int Xline,Yline;
        int lines = CalcLines(kernel);
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                l = copy.getGray(x, y);
                if (l == 255) {
                    for (int[][] se : kernels) {
                        int hits = 0;
                        for (int i = 0; i < se.length; i++) {
                            Xline = x + (i-lines);
                            for (int j = 0; j < se[0].length; j++) {
                                Yline = y + (j-lines);
                                if ((Xline >= 0) && (Xline < height) && (Yline >=0) && (Yline < width)) {
                                    if (se[i][j] >= 0) {
                                        if(se[i][j] == 1 && copy.getGray(Xline, Yline) == 255)
                                            hits++;
                                        if(se[i][j] == 0 && copy.getGray(Xline, Yline) == 0)
                                            hits++;
                                    }
                                    else{
                                        hits++;
                                    }
                                }
                            }
                        }
                        if(hits == pMatches){
                            fastBitmap.setGray(x, y, p);
                            break;
                        }
                    }
                }
            }
        }
    }
            
    
    private int CalcLines(int[][] se){
            int lines = (se[0].length - 1)/2;
            return lines;
    }
    
}