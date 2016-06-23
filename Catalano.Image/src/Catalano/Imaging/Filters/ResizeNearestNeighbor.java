// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
// Copyright © Andrew Kirillov, 2005-2008
// andrew.kirillov@gmail.com
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
import Catalano.Imaging.IApplyInPlace;

/**
 * Resize image using nearest neighbor algorithm.
 * 
 * <para>The class implements image resizing filter using nearest
 * neighbor algorithm, which does not assume any interpolation.</para>
 * 
 * @author Diego Catalano
 */
public class ResizeNearestNeighbor implements IApplyInPlace{
    
    private int newWidth;
    private int newHeight;

    /**
     * Get Width of the new resized image.
     * @return New width.
     */
    public int getNewWidth() {
        return newWidth;
    }

    /**
     * Set Width of the new resized image.
     * @param newWidth New width.
     */
    public void setNewWidth(int newWidth) {
        this.newWidth = newWidth;
    }

    /**
     * Get Height of the new resized image.
     * @return New height.
     */
    public int getNewHeight() {
        return newHeight;
    }

    /**
     * Set Height of the new resized image.
     * @param newHeight New height.
     */
    public void setNewHeight(int newHeight) {
        this.newHeight = newHeight;
    }
    
    /**
     * Set Size of the new resized image.
     * @param newWidth Width of the new resized image.
     * @param newHeight Height of the new resized image.
     */
    public void setNewSize(int newWidth, int newHeight){
        this.newWidth = newWidth;
        this.newHeight = newHeight;
    }

    /**
     * Initialize a new instance of the ResizeNearestNeighbor class.
     * @param newWidth Width of the new resized image.
     * @param newHeight Height of the new resize image.
     */
    public ResizeNearestNeighbor(int newWidth, int newHeight) {
        this.newWidth = newWidth;
        this.newHeight = newHeight;
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        FastBitmap dest = new FastBitmap(newWidth, newHeight, fastBitmap.getColorSpace());
        
        int width = fastBitmap.getWidth();
        int height = fastBitmap.getHeight();
        double jFactor = (double)width / (double)newWidth;
        double iFactor = (double)height / (double)newHeight;
        
        if(fastBitmap.isGrayscale()){
            for (int i = 0; i < newHeight; i++) {

                int I = (int)(i * iFactor);
                int p;

                for (int j = 0; j < newWidth; j++) {

                    int J = (int)(j * jFactor);
                    p = fastBitmap.getGray(I, J);

                    dest.setGray(i, j, p);
                }
            }

            fastBitmap.setImage(dest);
        }
        else{
            for (int i = 0; i < newHeight; i++) {

                int I = (int)(i * iFactor);
                int r, g, b;

                for (int j = 0; j < newWidth; j++) {

                    int J = (int)(j * jFactor);
                    r = fastBitmap.getRed(I, J);
                    g = fastBitmap.getGreen(I, J);
                    b = fastBitmap.getBlue(I, J);

                    dest.setRGB(i, j, r, g, b);
                }
            }

            fastBitmap.setImage(dest);
        }
    }
}