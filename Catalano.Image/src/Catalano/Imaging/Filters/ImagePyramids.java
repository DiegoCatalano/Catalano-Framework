// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
// Copyright © Edward Wiggin
// xjed09 at gmail.com
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

/**
 * Image Pyramids.
 * @author Diego Catalano
 */
public class ImagePyramids {
    
    private int level = 1;
    
    float[][] gaussianDownscale = {
        {0.00390625f,0.015625f,0.0234375f,0.015625f,0.00390625f},
        {0.015625f,0.062500f,0.093750f,0.062500f,0.015625f},
        {0.0234375f,0.093750f,0.140625f,0.093750f,0.0234375f},
        {0.015625f,0.062500f,0.093750f,0.062500f,0.015625f},
        {0.00390625f,0.015625f,0.0234375f,0.015625f,0.00390625f},
    };
    
    float[][] gaussianUpscale = {
        {0.015625f,0.0625f,0.09375f,0.0625f,0.015625f},
        {0.0625f,0.25f,0.375f,0.25f,0.0625f},
        {0.09375f,0.375f,0.5625f,0.375f,0.09375f},
        {0.0625f,0.25f,0.375f,0.25f,0.0625f},
        {0.015625f,0.0625f,0.09375f,0.0625f,0.015625f},
    };

    /**
     * Get Level.
     * @return Level.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Set Level.
     * @param level Level.
     */
    public void setLevel(int level) {
        this.level = Math.max(1, level);
    }

    /**
     * Initialize a new instance of the ImagePyramids class.
     */
    public ImagePyramids() {}
    
    /**
     * Initialize a new instance of the ImagePyramids class.
     * @param level Level.
     */
    public ImagePyramids(int level){
        setLevel(level);
    }
    
    public void Downscale(FastBitmap fastBitmap){
        
        if (fastBitmap.isGrayscale()){
            for (int x = 0; x < level; x++) {

                float[][] image = ConvolutionGray(fastBitmap, gaussianDownscale, true);

                int width = image[0].length;
                int height = image.length;

                FastBitmap copy = new FastBitmap(width / 2, height / 2, FastBitmap.ColorSpace.Grayscale);

                for (int i = 0; i < height - 1; i+=2) {
                    for (int j = 0; j < width - 1; j+=2) {
                        copy.setGray(i / 2, j / 2, (int)(image[i][j] * 255));
                    }
                }

                fastBitmap.setImage(copy);
            }
        }
        else{
            for (int x = 0; x < level; x++) {

                float[][][] image = ConvolutionRGB(fastBitmap, gaussianDownscale);

                int width = image[0].length;
                int height = image.length;

                FastBitmap copy = new FastBitmap(width / 2, height / 2);

                for (int i = 0; i < height - 1; i+=2) {
                    for (int j = 0; j < width - 1; j+=2) {
                        copy.setRGB(i/2, j/2, (int)(image[i][j][0] * 255), (int)(image[i][j][1] * 255), (int)(image[i][j][2] * 255));
                    }
                }

                fastBitmap.setImage(copy);
            }
        }
        
    }
    
    private float[][] ConvolutionGray(FastBitmap fastBitmap, float[][] kernel, boolean replicate){
        
        int width = fastBitmap.getWidth();
        int height = fastBitmap.getHeight();
        
        float[][] image = new float[height][width];
        
        
        int lines = (kernel[0].length - 1) / 2;
        
        float gray;
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                    gray = 0;
                    for (int i = 0; i < kernel.length; i++) {
                        int Xline = r + (i-lines);
                        for (int j = 0; j < kernel[0].length; j++) {
                            int Yline = c + (j-lines);
                            if ((Xline >= 0) && (Xline < height) && (Yline >= 0) && (Yline < width)) {
                                gray += kernel[i][j] * (fastBitmap.getGray(Xline, Yline) / 255f);
                            }
                            else if(replicate){
                                int R = r + i - 1;
                                int C = c + j - 1;
                                
                                if (R < 0) R = 0;
                                if (R >= height) R = height - 1;
                                
                                if (C < 0) C = 0;
                                if (C >= width) C = width - 1;
                                
                                int val = fastBitmap.getGray(R, C);
                                
                                gray += kernel[i][j] * (fastBitmap.getGray(R, C) / 255f);
                            }
                        }
                    }

                    
                    gray = gray > 1 ? 1 : gray;
                    gray = gray < 0 ? 0 : gray;
                    
                    image[r][c] = gray;
            }
        }
        
        return image;
    }
    
    private float[][][] ConvolutionRGB(FastBitmap fastBitmap, float[][] kernel){
        
        int width = fastBitmap.getWidth();
        int height = fastBitmap.getHeight();
        
        float[][][] image = new float[height][width][3];
        
        float red,green,blue;
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                    red = green = blue = 0;
                    for (int i = 0; i < kernel.length; i++) {
                        int Xline = r + (i-2);
                        for (int j = 0; j < kernel[0].length; j++) {
                            int Yline = c + (j-2);
                            if ((Xline >= 0) && (Xline < height) && (Yline >=0) && (Yline < width)) {
                                red += kernel[i][j] * fastBitmap.getRed(Xline, Yline) / 255;
                                green += kernel[i][j] * fastBitmap.getGreen(Xline, Yline) / 255;
                                blue += kernel[i][j] * fastBitmap.getBlue(Xline, Yline) / 255;
                            }
                            else{
                                red += kernel[i][j] * fastBitmap.getRed(r, c) / 255;
                                green += kernel[i][j] * fastBitmap.getGreen(r, c) / 255;
                                blue += kernel[i][j] * fastBitmap.getBlue(r, c) / 255;
                            }
                        }
                    }
                    
                    red = red > 1 ? 1 : red;
                    red = red < 0 ? 0 : red;
                    
                    green = green > 1 ? 1 : green;
                    green = green < 0 ? 0 : green;
                    
                    blue = blue > 1 ? 1 : blue;
                    blue = blue < 0 ? 0 : blue;
                    
                    image[r][c][0] = red;
                    image[r][c][1] = green;
                    image[r][c][2] = blue;
            }
        }
        
        return image;
    }
}