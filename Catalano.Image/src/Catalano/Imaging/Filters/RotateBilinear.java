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
 * Rotate image using bilinear algorithm.
 * 
 * <para>The class implements image rotation filter using bilinear
 * algorithm, which does not assume any interpolation.</para>
 * 
 * <para><note>Rotation is performed in counterclockwise direction.</note></para>
 * 
 * @author Diego Catalano
 */
public class RotateBilinear implements IApplyInPlace {
    
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
     * Initialize a new instance of the RotateBilinear class.
     * @param angle Angle [0..360].
     */
    public RotateBilinear(double angle) {
        this.angle = -angle;
        this.keepSize = false;
    }
    
    /**
     * Initialize a new instance of the RotateBilinear class.
     * @param angle Angle [0..360].
     * @param keepSize Keep original size.
     */
    public RotateBilinear(double angle, boolean keepSize) {
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
            
            // coordinates of source points
            double  oi, oj, ti, tj, di1, dj1, di2, dj2;
            int     oi1, oj1, oi2, oj2;
            
            // width and height decreased by 1
            int imax = height - 1;
            int jmax = width - 1;
            
            ci = -newIradius;
            for (int i = 0; i < newHeight; i++) {
                
                // do some pre-calculations of source points' coordinates
                // (calculate the part which depends on y-loop, but does not
                // depend on x-loop)
                ti = angleCos * ci + oldIradius;
                tj = -angleSin * ci + oldJradius;
                
                cj = -newJradius;
                for (int j = 0; j < newWidth; j++) {
                    
                    // coordinates of source point
                    oi = ti + angleSin * cj;
                    oj = tj + angleCos * cj;
                    
                    // top-left coordinate
                    oi1 = (int) oi;
                    oj1 = (int) oj;
                    
                    // validate source pixel's coordinates
                    if ( ( oi1 < 0 ) || ( oj1 < 0 ) || ( oi1 >= height ) || ( oj1 >= width ) ){
                        // fill destination image with filler
                        destinationData.setGray(i, j, fillGray);
                    }
                    else{
                        // bottom-right coordinate
                        oi2 = ( oi1 == imax ) ? oi1 : oi1 + 1;
                        oj2 = ( oj1 == jmax ) ? oj1 : oj1 + 1;

                        if ( ( di1 = oi - (double) oi1 ) < 0 )
                            di1 = 0;
                        di2 = 1.0 - di1;

                        if ( ( dj1 = oj - (double) oj1 ) < 0 )
                            dj1 = 0;
                        dj2 = 1.0 - dj1;
                        
                        // get four points
                        int p1 = fastBitmap.getGray(oi1, oj1);
                        int p2 = fastBitmap.getGray(oi1, oj2);
                        int p3 = fastBitmap.getGray(oi2, oj1);
                        int p4 = fastBitmap.getGray(oi2, oj2);
                    
                        int g = (int)(
                                di2 * ( dj2 * ( p1 ) + dj1 * ( p2 ) ) +
                                di1 * ( dj2 * ( p3 ) + dj1 * ( p4 ) ) );
                        
                        destinationData.setGray(i, j, g);
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
            
            // coordinates of source points
            double  oi, oj, ti, tj, di1, dj1, di2, dj2;
            int     oi1, oj1, oi2, oj2;
            
            // width and height decreased by 1
            int imax = height - 1;
            int jmax = width - 1;
            
            ci = -newIradius;
            for (int i = 0; i < newHeight; i++) {
                
                // do some pre-calculations of source points' coordinates
                // (calculate the part which depends on y-loop, but does not
                // depend on x-loop)
                ti = angleSin * ci + oldIradius;
                tj = angleCos * ci + oldJradius;
                
                cj = -newJradius;
                for (int j = 0; j < newWidth; j++) {
                    
                    // coordinates of source point
                    oi = ti + angleCos * cj;
                    oj = tj - angleSin * cj;

                    // top-left coordinate
                    oi1 = (int) oi;
                    oj1 = (int) oj;
                    
                    // validate source pixel's coordinates
                    if ( ( oi1 < 0 ) || ( oj1 < 0 ) || ( oi1 >= height ) || ( oj1 >= width ) ){
                        // fill destination image with filler
                        destinationData.setRGB(i, j, fillRed, fillGreen, fillBlue);
                    }
                    else{
                        // bottom-right coordinate
                        oi2 = ( oi1 == imax ) ? oi1 : oi1 + 1;
                        oj2 = ( oj1 == jmax ) ? oj1 : oj1 + 1;

                        if ( ( di1 = oi - (double) oi1 ) < 0 )
                            di1 = 0;
                        di2 = 1.0 - di1;

                        if ( ( dj1 = oj - (double) oj1 ) < 0 )
                            dj1 = 0;
                        dj2 = 1.0 - dj1;
                        
                        // get four points (red)
                        int p1 = fastBitmap.getRed(oi1, oj1);
                        int p2 = fastBitmap.getRed(oi1, oj2);
                        int p3 = fastBitmap.getRed(oi2, oj1);
                        int p4 = fastBitmap.getRed(oi2, oj2);
                    
                        int r = (int)(
                                di2 * ( dj2 * ( p1 ) + dj1 * ( p2 ) ) +
                                di1 * ( dj2 * ( p3 ) + dj1 * ( p4 ) ) );
                        
                        // get four points (green)
                        p1 = fastBitmap.getGreen(oi1, oj1);
                        p2 = fastBitmap.getGreen(oi1, oj2);
                        p3 = fastBitmap.getGreen(oi2, oj1);
                        p4 = fastBitmap.getGreen(oi2, oj2);
                    
                        int g = (int)(
                                di2 * ( dj2 * ( p1 ) + dj1 * ( p2 ) ) +
                                di1 * ( dj2 * ( p3 ) + dj1 * ( p4 ) ) );
                        
                        // get four points (blue)
                        p1 = fastBitmap.getBlue(oi1, oj1);
                        p2 = fastBitmap.getBlue(oi1, oj2);
                        p3 = fastBitmap.getBlue(oi2, oj1);
                        p4 = fastBitmap.getBlue(oi2, oj2);
                    
                        int b = (int)(
                                di2 * ( dj2 * ( p1 ) + dj1 * ( p2 ) ) +
                                di1 * ( dj2 * ( p3 ) + dj1 * ( p4 ) ) );
                        
                        
                        
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