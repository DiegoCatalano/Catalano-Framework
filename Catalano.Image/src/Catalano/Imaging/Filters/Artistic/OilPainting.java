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
import Catalano.Imaging.Tools.ColorConverter;

/**
 * Oil Paiting effect.
 * @author Diego Catalano
 */
public class OilPainting implements IApplyInPlace{
    
    private int radius = 2;

    /**
     * Get radius.
     * @return Radius.
     */
    public int getRadius() {
        return radius;
    }

    /**
     * Set radius.
     * @param radius Radius.
     */
    public void setRadius(int radius) {
        this.radius = Math.max(2, radius);
    }

    /**
     * Initialize a new instance of the OilPainting class.
     */
    public OilPainting() {}

    /**
     * Initialize a new instance of the OilPainting class.
     * @param radius Radius.
     */
    public OilPainting(int radius) {
        setRadius(radius);
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        int width = fastBitmap.getWidth();
        int height = fastBitmap.getHeight();
        
        if(fastBitmap.isGrayscale()){
            
            FastBitmap copy = new FastBitmap(fastBitmap);
            
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    
                    int[] intensityCount = new int[256];
                    int[] averageG = new int[256];
                    int maxIntensity = 0;
                    int maxIndex = 0;
                    
                    for (int k = i - radius; k < i + radius; k++) {
                        for (int l = j - radius; l < j + radius; l++) {
                            
                            if ((k > 0 && k < height) && (l > 0 && l < width)){
                                int g = copy.getGray(k, l);
                                intensityCount[g]++;
                                averageG[g] += g;
                                
                                if (intensityCount[g] > maxIntensity){
                                    maxIntensity = intensityCount[g];
                                    maxIndex = g;
                                }
                            }
                        }
                    }
                    
                    fastBitmap.setGray(i, j, (int) (averageG[maxIndex] / maxIntensity));
                    
                    
                }
            }
        }
        else{
            
            FastBitmap copy = new FastBitmap(fastBitmap);
            
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    
                    int[] intensityCount = new int[256];
                    
                    int[] averageR = new int[256];
                    int[] averageG = new int[256];
                    int[] averageB = new int[256];
                    
                    int maxIntensity = 0;
                    int maxIndex = 0;
                    
                    for (int k = i - radius; k < i + radius; k++) {
                        for (int l = j - radius; l < j + radius; l++) {
                            
                            if ((k > 0 && k < height) && (l > 0 && l < width)){
                                int r = copy.getRed(k, l);
                                int g = copy.getGreen(k, l);
                                int b = copy.getBlue(k, l);
                                
                                int gray = (int)ColorConverter.RGBtoGrayscale(r, g, b);
                                
                                intensityCount[gray]++;
                                
                                averageR[gray] += r;
                                averageG[gray] += g;
                                averageB[gray] += b;
                                
                                if (intensityCount[gray] > maxIntensity){
                                    maxIntensity = intensityCount[gray];
                                    maxIndex = gray;
                                }
                            }
                        }
                    }
                    
                    fastBitmap.setRGB(i, j,
                            (int) (averageR[maxIndex] / maxIntensity),
                            (int) (averageG[maxIndex] / maxIntensity),
                            (int) (averageB[maxIndex] / maxIntensity));
                }
            }
        }
    }
}