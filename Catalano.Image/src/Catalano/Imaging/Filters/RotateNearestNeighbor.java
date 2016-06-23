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
 * Rotate image using nearest neighbor algorithm.
 * 
 * <para>The class implements image rotation filter using nearest
 * neighbor algorithm, which does not assume any interpolation.</para>
 * 
 * <para><note>Rotation is performed in counterclockwise direction.</note></para>
 * 
 * @author Diego Catalano
 */
public class RotateNearestNeighbor implements IApplyInPlace {
    
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
     * Initialize a new instance of the RotateNearestNeighbor class.
     * @param angle Angle [0..360].
     */
    public RotateNearestNeighbor(double angle) {
        this.angle = -angle;
        this.keepSize = false;
    }
    
    /**
     * Initialize a new instance of the RotateNearestNeighbor class.
     * @param angle Angle [0..360].
     * @param keepSize Keep original size.
     */
    public RotateNearestNeighbor(double angle, boolean keepSize) {
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
            
            // source pixel's coordinates
            int oi, oj;
            
            ci = -newIradius;
            for (int i = 0; i < newHeight; i++) {
                cj = -newJradius;
                for (int j = 0; j < newWidth; j++) {
                    
                    oi = (int) ( angleCos * ci + angleSin * cj + oldIradius );
                    oj = (int) ( -angleSin * ci + angleCos * cj + oldJradius );
                    
                    // validate source pixel's coordinates
                    if ( ( oi < 0 ) || ( oj < 0 ) || ( oi >= height ) || ( oj >= width ) )
                    {
                        // fill destination image with filler
                        destinationData.setGray(i, j, fillGray);
                    }
                    else
                    {
                        destinationData.setGray(i, j, fastBitmap.getGray(oi, oj));
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
            
            // source pixel's coordinates
            int oi, oj;
            
            ci = -newIradius;
            for (int i = 0; i < newHeight; i++) {
                cj = -newJradius;
                for (int j = 0; j < newWidth; j++) {
                    
                    // coordinate of the nearest point
                    oi = (int) ( angleCos * ci + angleSin * cj + oldIradius );
                    oj = (int) ( -angleSin * ci + angleCos * cj + oldJradius );
                    
                        // validate source pixel's coordinates
                        if ( ( oi < 0 ) || ( oj < 0 ) || ( oi >= height ) || ( oj >= width ) )
                        {
                            // fill destination image with filler
                            destinationData.setRGB(i, j, fillRed, fillGreen, fillBlue);
                        }
                        else
                        {
                            int r = fastBitmap.getRed(oi, oj);
                            int g = fastBitmap.getGreen(oi, oj);
                            int b = fastBitmap.getBlue(oi, oj);
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