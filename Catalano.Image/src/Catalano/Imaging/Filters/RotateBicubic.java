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
 * Rotate image using bicubic algorithm.
 * 
 * <para>The class implements image rotation filter using bicubic
 * algorithm, which does not assume any interpolation.</para>
 * 
 * <para><note>Rotation is performed in counterclockwise direction.</note></para>
 * 
 * @author Diego Catalano
 */
public class RotateBicubic implements IApplyInPlace {
    
    private double angle;
    private boolean keepSize;
    
    private int newWidth;
    private int newHeight;
    
    private int fillRed = 0;
    private int fillGreen = 0;
    private int fillBlue = 0;
    private int fillGray = 0;

    /**
     * Get Angle.
     * @return Angle.
     */
    public double getAngle() {
        return -angle;
    }

    /**
     * Set Angle.
     * @param angle Angle.
     */
    public void setAngle(double angle) {
        this.angle = -angle;
    }

    /**
     * Keep original size.
     * @return True if keep the original image size, otherwise false.
     */
    public boolean isKeepSize() {
        return keepSize;
    }

    /**
     * Set keep original size.
     * @param keepSize True if keep the original image size, otherwise false.
     */
    public void setKeepSize(boolean keepSize) {
        this.keepSize = keepSize;
    }
    
    /**
     * Set Fill color.
     * @param red Red channel's value.
     * @param green Green channel's value.
     * @param blue Blue channel's value.
     */
    public void setFillColor(int red, int green, int blue){
        this.fillRed = red;
        this.fillGreen = green;
        this.fillBlue = blue;
    }
    
    /**
     * Set Fill color.
     * @param gray Gray channel's value.
     */
    public void setFillColor(int gray){
        this.fillGray = gray;
    }

    /**
     * Initialize a new instance of the RotateBicubic class.
     * @param angle Angle [0..360].
     */
    public RotateBicubic(double angle) {
        this.angle = -angle;
        this.keepSize = false;
    }
    
    /**
     * Initialize a new instance of the RotateBicubic class.
     * @param angle Angle [0..360].
     * @param keepSize Keep original size.
     */
    public RotateBicubic(double angle, boolean keepSize) {
        this.angle = -angle;
        this.keepSize = keepSize;
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        if (fastBitmap.isGrayscale()){
            
            int width = fastBitmap.getWidth();
            int height = fastBitmap.getHeight();
            double oldIradius = (double) ( height  - 1 ) / 2;
            double oldJradius = (double) ( width - 1 ) / 2;
            
            CalculateNewSize(fastBitmap);
            FastBitmap destinationData = new FastBitmap(newWidth, newHeight, FastBitmap.ColorSpace.Grayscale);
            
            // get destination image size
            double newIradius = (double) ( newHeight  - 1 ) / 2;
            double newJradius = (double) ( newWidth - 1 ) / 2;

            // angle's sine and cosine
            double angleRad = -angle * Math.PI / 180;
            double angleCos = Math.cos( angleRad );
            double angleSin = Math.sin( angleRad );
            
            // destination pixel's coordinate relative to image center
            double ci, cj;
            
            // coordinates of source points and cooefficiens
            double  oi, oj, di, dj, k1, k2;
            int     oi1, oj1, oi2, oj2;
            
            // width and height decreased by 1
            int imax = height - 1;
            int jmax = width - 1;
            
            ci = -newIradius;
            for (int i = 0; i < newHeight; i++) {
                cj = -newJradius;
                for (int j = 0; j < newWidth; j++) {
                    
                    // coordinate of the nearest point
                    oi = angleCos * ci + angleSin * cj + oldIradius;
                    oj = -angleSin * ci + angleCos * cj + oldJradius;
                    
                    oi1 = (int)oi;
                    oj1 = (int)oj;
                    
                    // validate source pixel's coordinates
                    if ( ( oi1 < 0 ) || ( oj1 < 0 ) || ( oi1 >= height ) || ( oj1 >= width ) ){
                        // fill destination image with filler
                        destinationData.setGray(i, j, fillGray);
                    }
                    else
                    {
                        
                        di = oi - (double) oi1;
                        dj = oj - (double) oj1;

                        // initial pixel value
                        int g = 0;
                        
                        for ( int n = -1; n < 3; n++ ) {
                            // get Y cooefficient
                            k1 = Interpolation.BiCubicKernel( dj - (double) n );

                            oj2 = oj1 + n;
                            if ( oj2 < 0 )
                                oj2 = 0;
                            if ( oj2 > jmax )
                                oj2 = jmax;

                            for ( int m = -1; m < 3; m++ )
                            {
                                // get X cooefficient
                                k2 = k1 * Interpolation.BiCubicKernel( (double) m - di );

                                oi2 = oi1 + m;
                                if ( oi2 < 0 )
                                    oi2 = 0;
                                if ( oi2 > imax )
                                    oi2 = imax;

                                g += k2 * fastBitmap.getGray(oi2, oj2);
                            }
                        }
                        
                        destinationData.setGray(i, j, Math.max( 0, Math.min( 255, g )));
                    }
                    cj++;
                }
                ci++;
            }
            
            fastBitmap.setImage(destinationData);
            
        }
        else if (fastBitmap.isRGB()){
            int width = fastBitmap.getWidth();
            int height = fastBitmap.getHeight();
            double oldIradius = (double) ( height  - 1 ) / 2;
            double oldJradius = (double) ( width - 1 ) / 2;
            
            CalculateNewSize(fastBitmap);
            FastBitmap destinationData = new FastBitmap(newWidth, newHeight, FastBitmap.ColorSpace.RGB);
            
            // get destination image size
            double newIradius = (double) ( newHeight  - 1 ) / 2;
            double newJradius = (double) ( newWidth - 1 ) / 2;

            // angle's sine and cosine
            double angleRad = -angle * Math.PI / 180;
            double angleCos = Math.cos( angleRad );
            double angleSin = Math.sin( angleRad );
            
            // destination pixel's coordinate relative to image center
            double ci, cj;
            
            // coordinates of source points and cooefficiens
            double  oi, oj, di, dj, k1, k2;
            int     oi1, oj1, oi2, oj2;
            
            // width and height decreased by 1
            int imax = height - 1;
            int jmax = width - 1;
            
            ci = -newIradius;
            for (int i = 0; i < newHeight; i++) {
                cj = -newJradius;
                for (int j = 0; j < newWidth; j++) {
                    
                    // coordinate of the nearest point
                    oi = angleCos * ci + angleSin * cj + oldIradius;
                    oj = -angleSin * ci + angleCos * cj + oldJradius;
                    
                    oi1 = (int)oi;
                    oj1 = (int)oj;
                    
                    // validate source pixel's coordinates
                    if ( ( oi < 0 ) || ( oj < 0 ) || ( oi >= height ) || ( oj >= width ) ){
                        // fill destination image with filler
                        destinationData.setRGB(i, j, fillRed, fillGreen, fillBlue);
                    }
                    else
                    {
                        
                        di = oi - (double) oi1;
                        dj = oj - (double) oj1;

                        // initial pixel value
                        int r = 0;
                        int g = 0;
                        int b = 0;
                        
                        for ( int n = -1; n < 3; n++ ) {
                            // get Y cooefficient
                            k1 = Interpolation.BiCubicKernel( dj - (double) n );

                            oj2 = oj1 + n;
                            if ( oj2 < 0 )
                                oj2 = 0;
                            if ( oj2 > jmax )
                                oj2 = jmax;

                            for ( int m = -1; m < 3; m++ )
                            {
                                // get X cooefficient
                                k2 = k1 * Interpolation.BiCubicKernel( (double) m - di );

                                oi2 = oi1 + m;
                                if ( oi2 < 0 )
                                    oi2 = 0;
                                if ( oi2 > imax )
                                    oi2 = imax;

                                r += k2 * fastBitmap.getRed(oi2, oj2);
                                g += k2 * fastBitmap.getGreen(oi2, oj2);
                                b += k2 * fastBitmap.getBlue(oi2, oj2);
                            }
                        }
                        
                        r = Math.max( 0, Math.min( 255, r ));
                        g = Math.max( 0, Math.min( 255, g ));
                        b = Math.max( 0, Math.min( 255, b ));
                        destinationData.setRGB(i, j, r, g, b);
                    }
                    cj++;
                }
                ci++;
            }
            
            fastBitmap.setImage(destinationData);
        }
        
    }
    
    private void CalculateNewSize(FastBitmap fastBitmap){
        // return same size if original image size should be kept
        if ( keepSize ){
            this.newWidth = fastBitmap.getWidth();
            this.newHeight = fastBitmap.getHeight();
            return;
        }

        // angle's sine and cosine
        double angleRad = -angle * Math.PI / 180;
        double angleCos = Math.cos( angleRad );
        double angleSin = Math.sin( angleRad );

        // calculate half size
        double halfWidth  = (double) fastBitmap.getWidth() / 2;
        double halfHeight = (double) fastBitmap.getHeight() / 2;

        // rotate corners
        double cx1 = halfWidth * angleCos;
        double cy1 = halfWidth * angleSin;

        double cx2 = halfWidth * angleCos - halfHeight * angleSin;
        double cy2 = halfWidth * angleSin + halfHeight * angleCos;

        double cx3 = -halfHeight * angleSin;
        double cy3 =  halfHeight * angleCos;

        double cx4 = 0;
        double cy4 = 0;

        // recalculate image size
        halfWidth  = Math.max( Math.max( cx1, cx2 ), Math.max( cx3, cx4 ) ) - Math.min( Math.min( cx1, cx2 ), Math.min( cx3, cx4 ) );
        halfHeight = Math.max( Math.max( cy1, cy2 ), Math.max( cy3, cy4 ) ) - Math.min( Math.min( cy1, cy2 ), Math.min( cy3, cy4 ) );

        this.newWidth = (int)(halfWidth * 2 + 0.5);
        this.newHeight = (int)(halfHeight * 2 + 0.5);
    }
}