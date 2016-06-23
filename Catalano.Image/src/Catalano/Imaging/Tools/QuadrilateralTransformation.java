// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
// Copyright © Andrew Kirillov, 2007-2008
// andrew.kirillov at gmail.com
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

package Catalano.Imaging.Tools;

import Catalano.Core.IntPoint;
import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.IProcessImage;
import Catalano.Math.Geometry.QuadrilateralTransformationCalc;
import java.util.ArrayList;

/**
 * Performs quadrilateral transformation of an area in a given source image.
 * 
 * The class implements quadrilateral transformation algorithm, which allows to transform any quadrilateral from a given source image to a rectangular image.
 * The idea of the algorithm is based on homogeneous transformation and its math is described by Paul Heckbert in his "Projective Mappings for Image Warping" paper.
 * 
 * @author Diego Catalano
 */
public class QuadrilateralTransformation implements IProcessImage{
    
    private boolean automaticSizeCalculaton = true;
    private boolean useInterpolation = true;
    private int newWidth;
    private int newHeight;
    private ArrayList<IntPoint> sourceQuadrilateral;

    /**
     * Is Automatic calculation of destination image.
     * @return True or false.
     */
    public boolean isAutomaticSizeCalculaton() {
        return automaticSizeCalculaton;
    }

    /**
     * Set Automatic calculation of destination image 
     * @param automaticSizeCalculaton True or false.
     */
    public void setAutomaticSizeCalculaton(boolean automaticSizeCalculaton) {
        this.automaticSizeCalculaton = automaticSizeCalculaton;
        if (automaticSizeCalculaton) CalculateDestinationSize();
    }

    /**
     * Get Quadrilateral's corners in source image.
     * @return Quadrilateral's corners.
     */
    public ArrayList<IntPoint> getSourceQuadrilateral() {
        return sourceQuadrilateral;
    }

    /**
     * Set Quadrilateral's corners in source image.
     * @param sourceQuadrilateral Quadrilateral's corners.
     */
    public void setSourceQuadrilateral(ArrayList<IntPoint> sourceQuadrilateral) {
        this.sourceQuadrilateral = sourceQuadrilateral;
        if (automaticSizeCalculaton) CalculateDestinationSize();
    }

    /**
     * Get New width.
     * @return New width.
     */
    public int getNewWidth() {
        return newWidth;
    }

    /**
     * Set New width.
     * @param newWidth New width.
     */
    public void setNewWidth(int newWidth) {
        this.newWidth = newWidth;
        if (!automaticSizeCalculaton) this.newWidth = Math.max( 1, newWidth );
    }

    /**
     * Get New height.
     * @return New height.
     */
    public int getNewHeight() {
        return newHeight;
    }

    /**
     * Set New height.
     * @param newHeight New height.
     */
    public void setNewHeight(int newHeight) {
        this.newHeight = newHeight;
        if (!automaticSizeCalculaton) this.newHeight = Math.max(1, newHeight);
    }

    /**
     * Specifies if bilinear interpolation should be used or not.
     * @return True or false.
     */
    public boolean isUseInterpolation() {
        return useInterpolation;
    }

    /**
     * Set if bilinear interpolation should be used or not.
     * @param useInterpolation True or false.
     */
    public void setUseInterpolation(boolean useInterpolation) {
        this.useInterpolation = useInterpolation;
    }
    
    /**
     * Initializes a new instance of the QuadrilateralTransformation class.
     * @param sourceQuadrilateral Quadrilateral's corners.
     */
    public QuadrilateralTransformation(ArrayList<IntPoint> sourceQuadrilateral){
        this.automaticSizeCalculaton = true;
        this.sourceQuadrilateral = sourceQuadrilateral;
        CalculateDestinationSize();
    }
    
    /**
     * Initializes a new instance of the QuadrilateralTransformation class.
     * @param sourceQuadrilateral Quadrilateral's corners.
     * @param newWidth New width.
     * @param newHeight New height.
     */
    public QuadrilateralTransformation(ArrayList<IntPoint> sourceQuadrilateral, int newWidth, int newHeight){
        this.automaticSizeCalculaton = false;
        this.sourceQuadrilateral = sourceQuadrilateral;
        this.newWidth  = newWidth;
        this.newHeight = newHeight;
    }
    
    @Override
    public FastBitmap ProcessImage(FastBitmap fastBitmap) {
        
        FastBitmap dst = new FastBitmap(newWidth, newHeight, fastBitmap.getColorSpace());
        
        int srcWidth = fastBitmap.getWidth();
        int srcHeight = fastBitmap.getHeight();

        int dstWidth = newWidth;
        int dstHeight = newHeight;

        ArrayList<IntPoint> dstRect = new ArrayList<IntPoint>( );
        dstRect.add( new IntPoint( 0, 0 ) );
        dstRect.add( new IntPoint( dstWidth - 1, 0 ) );
        dstRect.add( new IntPoint( dstWidth - 1, dstHeight - 1 ) );
        dstRect.add( new IntPoint( 0, dstHeight - 1 ) );

        // calculate tranformation matrix
        double[][] matrix = QuadrilateralTransformationCalc.MapQuadToQuad( dstRect, sourceQuadrilateral );
        
        if(!useInterpolation){
            if (fastBitmap.isRGB()){
                for (int i = 0; i < dstHeight; i++) {
                    for (int j = 0; j < dstWidth; j++) {
                        double factor = matrix[2][0] * j + matrix[2][1] * i + matrix[2][2];
                        double srcX = ( matrix[0][0] * j + matrix[0][1] * i + matrix[0][2] ) / factor;
                        double srcY = ( matrix[1][0] * j + matrix[1][1] * i + matrix[1][2] ) / factor;

                        if ( ( srcX >= 0 ) && ( srcY >= 0 ) && ( srcX < srcWidth ) && ( srcY < srcHeight ) )
                        {
                            int r = fastBitmap.getRed((int)srcY, (int)srcX);
                            int g = fastBitmap.getGreen((int)srcY, (int)srcX);
                            int b = fastBitmap.getBlue((int)srcY, (int)srcX);
                            dst.setRGB(i, j, r, g, b);
                        }
                    }
                }
            }
            if (fastBitmap.isGrayscale()){
                for (int i = 0; i < dstHeight; i++) {
                    for (int j = 0; j < dstWidth; j++) {
                        double factor = matrix[2][0] * j + matrix[2][1] * i + matrix[2][2];
                        double srcX = ( matrix[0][0] * j + matrix[0][1] * i + matrix[0][2] ) / factor;
                        double srcY = ( matrix[1][0] * j + matrix[1][1] * i + matrix[1][2] ) / factor;

                        if ( ( srcX >= 0 ) && ( srcY >= 0 ) && ( srcX < srcWidth ) && ( srcY < srcHeight ) )
                        {
                            int g = fastBitmap.getGray((int)srcY, (int)srcX);
                            dst.setGray(i, j, g);
                        }
                    }
                }
            }
        }
        else{
            if (fastBitmap.isRGB()){
                int srcWidthM1  = srcWidth - 1;
                int srcHeightM1 = srcHeight - 1;

                // coordinates of source points
                double dx1, dy1, dx2, dy2;
                int sx1, sy1, sx2, sy2;
                
                int p1r, p2r, p3r, p4r;
                int p1g, p2g, p3g, p4g;
                int p1b, p2b, p3b, p4b;
                
                for (int i = 0; i < dstHeight; i++) {
                    for (int j = 0; j < dstWidth; j++) {
                        double factor = matrix[2][0] * j + matrix[2][1] * i + matrix[2][2];
                        double srcX = ( matrix[0][0] * j + matrix[0][1] * i + matrix[0][2] ) / factor;
                        double srcY = ( matrix[1][0] * j + matrix[1][1] * i + matrix[1][2] ) / factor;
                        
                        if ( ( srcX >= 0 ) && ( srcY >= 0 ) && ( srcX < srcWidth ) && ( srcY < srcHeight ) ){
                            sx1 = (int) srcX;
                            sx2 = ( sx1 == srcWidthM1 ) ? sx1 : sx1 + 1;
                            dx1 = srcX - sx1;
                            dx2 = 1.0 - dx1;

                            sy1 = (int) srcY;
                            sy2 = ( sy1 == srcHeightM1 ) ? sy1 : sy1 + 1;
                            dy1 = srcY - sy1;
                            dy2 = 1.0 - dy1;

                            // get four points in Red channel
                            p1r = fastBitmap.getRed(sy1, sx1);
                            p2r = fastBitmap.getRed(sy1, sx2);
                            p3r = fastBitmap.getRed(sy2, sx1);
                            p4r = fastBitmap.getRed(sy2, sx2);
                            
                            // get four points in Green channel
                            p1g = fastBitmap.getGreen(sy1, sx1);
                            p2g = fastBitmap.getGreen(sy1, sx2);
                            p3g = fastBitmap.getGreen(sy2, sx1);
                            p4g = fastBitmap.getGreen(sy2, sx2);
                            
                            // get four points in Blue channel
                            p1b = fastBitmap.getBlue(sy1, sx1);
                            p2b = fastBitmap.getBlue(sy1, sx2);
                            p3b = fastBitmap.getBlue(sy2, sx1);
                            p4b = fastBitmap.getBlue(sy2, sx2);
                            
                            int r = (int)(dy2 * (dx2 * (p1r) + dx1 * (p2r)) + dy1 * (dx2 * (p3r) + dx1 * (p4r)));
                            int g = (int)(dy2 * (dx2 * (p1g) + dx1 * (p2g)) + dy1 * (dx2 * (p3g) + dx1 * (p4g)));
                            int b = (int)(dy2 * (dx2 * (p1b) + dx1 * (p2b)) + dy1 * (dx2 * (p3b) + dx1 * (p4b)));
                            
                            dst.setRGB(i, j, r, g, b);
                        }
                    }
                }
            }
            
            if (fastBitmap.isGrayscale()){
                int srcWidthM1  = srcWidth - 1;
                int srcHeightM1 = srcHeight - 1;

                // coordinates of source points
                double dx1, dy1, dx2, dy2;
                int sx1, sy1, sx2, sy2;
                
                int p1, p2, p3, p4;
                
                for (int i = 0; i < dstHeight; i++) {
                    for (int j = 0; j < dstWidth; j++) {
                        double factor = matrix[2][0] * j + matrix[2][1] * i + matrix[2][2];
                        double srcX = ( matrix[0][0] * j + matrix[0][1] * i + matrix[0][2] ) / factor;
                        double srcY = ( matrix[1][0] * j + matrix[1][1] * i + matrix[1][2] ) / factor;
                        
                        if ( ( srcX >= 0 ) && ( srcY >= 0 ) && ( srcX < srcWidth ) && ( srcY < srcHeight ) ){
                            sx1 = (int) srcX;
                            sx2 = ( sx1 == srcWidthM1 ) ? sx1 : sx1 + 1;
                            dx1 = srcX - sx1;
                            dx2 = 1.0 - dx1;

                            sy1 = (int) srcY;
                            sy2 = ( sy1 == srcHeightM1 ) ? sy1 : sy1 + 1;
                            dy1 = srcY - sy1;
                            dy2 = 1.0 - dy1;

                            // get four points
                            p1 = fastBitmap.getGray(sy1, sx1);
                            p2 = fastBitmap.getGray(sy1, sx2);
                            p3 = fastBitmap.getGray(sy2, sx1);
                            p4 = fastBitmap.getGray(sy2, sx2);
                            
                            int g = (int)(dy2 * (dx2 * (p1) + dx1 * (p2)) + dy1 * (dx2 * (p3) + dx1 * (p4)));
                            dst.setGray(i, j, g);
                        }
                        
                    }
                }
            }
        }
        
        return dst;
    }
    
    // Calculates size of destination image
    private void CalculateDestinationSize( ){
        if ( sourceQuadrilateral == null )
            throw new IllegalArgumentException( "Source quadrilateral was not set." );

        newWidth  = (int) Math.max( sourceQuadrilateral.get(0).DistanceTo( sourceQuadrilateral.get(1) ),
                                    sourceQuadrilateral.get(2).DistanceTo( sourceQuadrilateral.get(3) ) );
        newHeight = (int) Math.max( sourceQuadrilateral.get(1).DistanceTo( sourceQuadrilateral.get(2) ),
                                    sourceQuadrilateral.get(3).DistanceTo( sourceQuadrilateral.get(0) ) );
    }
}
