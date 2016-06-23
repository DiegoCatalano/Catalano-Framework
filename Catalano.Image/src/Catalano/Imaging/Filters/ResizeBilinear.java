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
 * Resize image using bilinear interpolation algorithm.
 * 
 * <para>The class implements image resizing filter using bilinear
 * interpolation algorithm.</para>
 * 
 * @author Diego Catalano
 */
public class ResizeBilinear implements IApplyInPlace{
    
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
     * Initialize a new instance of the ResizeBilinear class.
     * @param newWidth Width of the new resized image.
     * @param newHeight Height of the new resize image.
     */
    public ResizeBilinear(int newWidth, int newHeight) {
        this.newWidth = newWidth;
        this.newHeight = newHeight;
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        FastBitmap dest = new FastBitmap(newWidth, newHeight, fastBitmap.getColorSpace());
        
        if (fastBitmap.isGrayscale()){
            
            int width = fastBitmap.getWidth();
            int height = fastBitmap.getHeight();
            double jFactor = (double)width / (double)newWidth;
            double iFactor = (double)height / (double)newHeight;
            
            // coordinates of source points
            double  ox, oy, dx1, dy1, dx2, dy2;
            int     ox1, oy1, ox2, oy2;
            // width and height decreased by 1
            int imax = height - 1;
            int jmax = width - 1;
            // temporary values
            int tp1, tp2;
            int p1, p2, p3, p4;
            
            for (int i = 0; i < newHeight; i++) {
                
                // Y coordinates
                oy  = (double) i * iFactor;
                oy1 = (int) oy;
                oy2 = ( oy1 == imax ) ? oy1 : oy1 + 1;
                dy1 = oy - (double) oy1;
                dy2 = 1.0 - dy1;

                // get temp pointers
                tp1 = oy1;
                tp2 = oy2;
                
                for (int j = 0; j < newWidth; j++) {
                    // X coordinates
                    ox  = (double) j * jFactor;
                    ox1 = (int) ox;
                    ox2 = ( ox1 == jmax ) ? ox1 : ox1 + 1;
                    dx1 = ox - (double) ox1;
                    dx2 = 1.0 - dx1;

                    // get four points
                    p1 = fastBitmap.getGray(tp1, ox1);//tp1 + ox1;
                    p2 = fastBitmap.getGray(tp1, ox2);//tp1 + ox2;
                    p3 = fastBitmap.getGray(tp2, ox1);//tp2 + ox1;
                    p4 = fastBitmap.getGray(tp2, ox2);//tp2 + ox2;
                    
                    int g = (int)(
                            dy2 * ( dx2 * ( p1 ) + dx1 * ( p2 ) ) +
                            dy1 * ( dx2 * ( p3 ) + dx1 * ( p4 ) ) );
                    
                    dest.setGray(i, j, g);
                }
            }
            fastBitmap.setImage(dest);
        }
        else{
            int width = fastBitmap.getWidth();
            int height = fastBitmap.getHeight();
            double jFactor = (double)width / (double)newWidth;
            double iFactor = (double)height / (double)newHeight;
            
            // coordinates of source points
            double  ox, oy, dx1, dy1, dx2, dy2;
            int     ox1, oy1, ox2, oy2;
            // width and height decreased by 1
            int imax = height - 1;
            int jmax = width - 1;
            // temporary values
            int tp1, tp2;
            int p1, p2, p3, p4;
            
            for (int i = 0; i < newHeight; i++) {
                
                // Y coordinates
                oy  = (double) i * iFactor;
                oy1 = (int) oy;
                oy2 = ( oy1 == imax ) ? oy1 : oy1 + 1;
                dy1 = oy - (double) oy1;
                dy2 = 1.0 - dy1;

                // get temp pointers
                tp1 = oy1;
                tp2 = oy2;
                
                for (int j = 0; j < newWidth; j++) {
                    // X coordinates
                    ox  = (double) j * jFactor;
                    ox1 = (int) ox;
                    ox2 = ( ox1 == jmax ) ? ox1 : ox1 + 1;
                    dx1 = ox - (double) ox1;
                    dx2 = 1.0 - dx1;

                    // get four points in red channel
                    p1 = fastBitmap.getRed(tp1, ox1);
                    p2 = fastBitmap.getRed(tp1, ox2);
                    p3 = fastBitmap.getRed(tp2, ox1);
                    p4 = fastBitmap.getRed(tp2, ox2);
                    
                    int r = (int)(
                            dy2 * ( dx2 * ( p1 ) + dx1 * ( p2 ) ) +
                            dy1 * ( dx2 * ( p3 ) + dx1 * ( p4 ) ) );
                    
                    // get four points in green channel
                    p1 = fastBitmap.getGreen(tp1, ox1);
                    p2 = fastBitmap.getGreen(tp1, ox2);
                    p3 = fastBitmap.getGreen(tp2, ox1);
                    p4 = fastBitmap.getGreen(tp2, ox2);
                    
                    int g = (int)(
                            dy2 * ( dx2 * ( p1 ) + dx1 * ( p2 ) ) +
                            dy1 * ( dx2 * ( p3 ) + dx1 * ( p4 ) ) );
                    
                    // get four points in blue channel
                    p1 = fastBitmap.getBlue(tp1, ox1);
                    p2 = fastBitmap.getBlue(tp1, ox2);
                    p3 = fastBitmap.getBlue(tp2, ox1);
                    p4 = fastBitmap.getBlue(tp2, ox2);
                    
                    int b = (int)(
                            dy2 * ( dx2 * ( p1 ) + dx1 * ( p2 ) ) +
                            dy1 * ( dx2 * ( p3 ) + dx1 * ( p4 ) ) );
                    
                    
                    
                    dest.setRGB(i, j, r, g, b);
                }
            }
            fastBitmap.setImage(dest);
        }
    }
}