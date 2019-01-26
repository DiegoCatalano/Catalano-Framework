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

package Catalano.Imaging.Filters;

import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.IApplyInPlace;
import Catalano.Imaging.Tools.ColorConverter;

/**
 * Hue modifier.
 * Only modifies the hue in the image from HLS color space.
 * @author Diego Catalano
 */
public class HueModifier implements IApplyInPlace{
    
    private float degree;

    /**
     * Get the degree of the hue angle.
     * @return Degree.
     */
    public float getDegree() {
        return degree;
    }

    /**
     * Set the degree of the hue angle.
     * @param degree Degree.
     */
    public void setDegree(float degree) {
        this.degree = degree;
    }
    
    /**
     * Initialize a new instance of HueModifier class.
     * @param degree 
     */
    public HueModifier(float degree) {
        this.degree = degree;
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        if(fastBitmap.isRGB()){
            int size = fastBitmap.getWidth() * fastBitmap.getHeight();
            for (int i = 0; i < size; i++) {
                int r = fastBitmap.getRed(i);
                int g = fastBitmap.getGreen(i);
                int b = fastBitmap.getBlue(i);
                
                double[] color = ColorConverter.RGBtoHSL(r, g, b);
                int[] newColor = ColorConverter.HSLtoRGB(degree,color[1],color[2]);
                
                newColor[0] = newColor[0] > 255 ? 255 : newColor[0];
                newColor[0] = newColor[0] < 0 ? 0 : newColor[0];
                
                newColor[1] = newColor[1] > 255 ? 255 : newColor[1];
                newColor[1] = newColor[1] < 0 ? 0 : newColor[1];
                
                newColor[2] = newColor[2] > 255 ? 255 : newColor[2];
                newColor[2] = newColor[2] < 0 ? 0 : newColor[2];
                
                fastBitmap.setRGB(i, newColor);
            }
        }
        else{
            throw new IllegalArgumentException("Hue modifier only works in RGB images.");
        }
    }
}