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
import Catalano.Imaging.Tools.Interpolation;

/**
 * Resize image using bicubic interpolation algorithm.
 * 
 * <para>The class implements image resizing filter using bicubic
 * interpolation algorithm. It uses bicubic kernel W(x) as described on
 * http://en.wikipedia.org/wiki/Bicubic_interpolation#Bicubic_convolution_algorithm
 * (coefficient <b>a</b> is set to <b>-0.5</b>).</para>
 * 
 * @author Diego Catalano
 */
public class ResizeBicubic implements IApplyInPlace{
    
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
     * Initialize a new instance of the ResizeBicubic class.
     * @param newWidth Width of the new resized image.
     * @param newHeight Height of the new resize image.
     */
    public ResizeBicubic(int newWidth, int newHeight) {
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
            double  ox, oy, dx, dy, k1, k2;
            int     ox1, oy1, ox2, oy2;
            
            // width and height decreased by 1
            int imax = height - 1;
            int jmax = width - 1;
            
            for (int i = 0; i < newHeight; i++) {
                
                // Y coordinates
                oy  = (double) i * iFactor - 0.5;
                oy1 = (int) oy;
                dy  = oy - (double) oy1;
                
                for (int j = 0; j < newWidth; j++) {
                    
                    // X coordinates
                    ox  = (double) j * jFactor - 0.5f;
                    ox1 = (int) ox;
                    dx  = ox - (double) ox1;
                    
                    int g = 0;
                    
                    for ( int n = -1; n < 3; n++ ){
                        // get Y cooefficient
                        k1 = Interpolation.BiCubicKernel( dy - (double) n );

                        oy2 = oy1 + n;
                        if ( oy2 < 0 )
                            oy2 = 0;
                        if ( oy2 > imax )
                            oy2 = imax;

                        for ( int m = -1; m < 3; m++ )
                        {
                            // get X cooefficient
                            k2 = k1 * Interpolation.BiCubicKernel( (double) m - dx );

                            ox2 = ox1 + m;
                            if ( ox2 < 0 )
                                ox2 = 0;
                            if ( ox2 > jmax )
                                ox2 = jmax;

                            g += k2 * fastBitmap.getGray(oy2, ox2);
                        }
                    }
                    
                    g = Math.max( 0, Math.min( 255, g ) );
                    
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
            double  ox, oy, dx, dy, k1, k2;
            int     ox1, oy1, ox2, oy2;
            
            // width and height decreased by 1
            int imax = height - 1;
            int jmax = width - 1;
            
            for (int i = 0; i < newHeight; i++) {
                
                // Y coordinates
                oy  = (double) i * iFactor - 0.5;
                oy1 = (int) oy;
                dy  = oy - (double) oy1;
                
                for (int j = 0; j < newWidth; j++) {
                    
                    // X coordinates
                    ox  = (double) j * jFactor - 0.5f;
                    ox1 = (int) ox;
                    dx  = ox - (double) ox1;
                    
                    int r, g, b;
                    r = g = b = 0;
                    
                    for ( int n = -1; n < 3; n++ ){
                        
                        // get Y cooefficient
                        k1 = Interpolation.BiCubicKernel( dy - (double) n );

                        oy2 = oy1 + n;
                        if ( oy2 < 0 )
                            oy2 = 0;
                        if ( oy2 > imax )
                            oy2 = imax;

                        for ( int m = -1; m < 3; m++ ){
                            
                            // get X cooefficient
                            k2 = k1 * Interpolation.BiCubicKernel( (double) m - dx );

                            ox2 = ox1 + m;
                            if ( ox2 < 0 )
                                ox2 = 0;
                            if ( ox2 > jmax )
                                ox2 = jmax;

                            r += k2 * fastBitmap.getRed(oy2, ox2);
                            g += k2 * fastBitmap.getGreen(oy2, ox2);
                            b += k2 * fastBitmap.getBlue(oy2, ox2);
                        }
                    }
                    
                    r = Math.max( 0, Math.min( 255, r ) );
                    g = Math.max( 0, Math.min( 255, g ) );
                    b = Math.max( 0, Math.min( 255, b ) );
                    
                    dest.setRGB(i, j, r, g, b);
                }
            }
            fastBitmap.setImage(dest);
        }
    }
}