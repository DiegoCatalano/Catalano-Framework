// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
// Code adapted from ImageJ, thanks to Wayne Rasband.
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

import Catalano.Core.IntPoint;
import Catalano.Imaging.FastBitmap;

/**
 * Distance Transform.
 * 
 * <p><li>Supported types: Grayscale.
 * <br><li>Coordinate System: Matrix.
 * 
 * @author Diego Catalano
 */
public class DistanceTransform {
    
    /**
     * Distance.
     */
    public static enum Distance {
        /**
         * Chessboard.
         */
        Chessboard,
        
        /**
         * Euclidean.
         */
        Euclidean,
        
        /**
         * Manhattan.
         */
        Manhattan,
        
        /**
         * Squared Euclidean.
         */
        SquaredEuclidean};
    
    private float[][] image;
    private float max = 0;
    private IntPoint ued;
    private Distance distance = Distance.Euclidean;

    /**
     * Get Maximum distance from transform.
     * @return Maximum distance.
     */
    public float getMaximumDistance() {
        return max;
    }

    /**
     * Get the Ultimate eroded point.
     * @return UED.
     */
    public IntPoint getUltimateErodedPoint() {
        return ued;
    }
    
    /**
     * Initialize a new instance of the DistanceTransform class.
     * Default distance: Euclidean.
     */
    public DistanceTransform() {}
    
    /**
     * Initialize a new instance of the DistanceTransform class.
     * @param distance Distance.
     */
    public DistanceTransform(Distance distance){
        this.distance = distance;
    }
    
    /**
     * Compute Distance Transform.
     * @param fastBitmap Image to be processed.
     * @return Distance map.
     */
    public float[][] Compute(FastBitmap fastBitmap){
        
        if (fastBitmap.isGrayscale()){
            int width = fastBitmap.getWidth();
            int height = fastBitmap.getHeight();
            byte[] bPixels = fastBitmap.getGrayData();
            float[] fPixels = new float[bPixels.length];

            for (int i=0; i<width*height; i++)
                if (bPixels[i]!=0)
                    fPixels[i] = Float.MAX_VALUE;

            int[][] pointBufs = new int[2][width];

            // pass 1 & 2: increasing y
            for (int x=0; x<width; x++) {
                pointBufs[0][x] = -1;
                pointBufs[1][x] = -1;
            }
            for (int y=0; y<height; y++)
                edmLine(bPixels, fPixels, pointBufs, width, y*width, y);

            //pass 3 & 4: decreasing y
            for (int x=0; x<width; x++) {
                pointBufs[0][x] = -1;
                pointBufs[1][x] = -1;
            }
            for (int y=height-1; y>=0; y--)
                edmLine(bPixels, fPixels, pointBufs, width, y*width, y);

            image = new float[height][width];
            int p = 0;
            
            if(distance == Distance.Euclidean){
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < width; j++) {
                        if(fPixels[p] < 0f)
                            image[i][j] = 0;
                        else
                            image[i][j] = (float)Math.sqrt(fPixels[p]);
                        if(image[i][j] > max){
                            max = image[i][j];
                            ued = new IntPoint(i, j);
                        }
                        p++;
                    }
                }
            }
            else{
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < width; j++) {
                        if(fPixels[p] < 0f)
                            image[i][j] = 0;
                        else
                            image[i][j] = fPixels[p];
                        if(image[i][j] > max){
                            max = image[i][j];
                            ued = new IntPoint(i, j);
                        }
                        p++;
                    }
                }
            }
        
            return image;
        }
        else{
            throw new IllegalArgumentException("Distance Transform only works in grayscale images.");
        }
        
    }
    
    // Handle a line; two passes: left-to-right and right-to-left
    private void edmLine(byte[] bPixels, float[] fPixels, int[][] pointBufs, int width, int offset, int y) {
        int[] points = pointBufs[0];        // the buffer for the left-to-right pass
        int pPrev = -1;
        int pDiag = -1;               // point at (-/+1, -/+1) to current one (-1,-1 in the first pass)
        int pNextDiag;
        int distSqr = Integer.MAX_VALUE;    // this value is used only if edges are not background
        for (int x=0; x<width; x++, offset++) {
            pNextDiag = points[x];
            if (bPixels[offset] == 0) {
                points[x] = x | y<<16;      // remember coordinates as a candidate for nearest background point
            } else {                        // foreground pixel:
                float dist2 = minDist2(points, pPrev, pDiag, x, y, distSqr, distance);
                if (fPixels[offset] > dist2) fPixels[offset] = dist2;
            }
            pPrev = points[x];
            pDiag = pNextDiag;
        }
        offset--; //now points to the last pixel in the line
        points = pointBufs[1];              // the buffer for the right-to-left pass. Low short contains x, high short y
        pPrev = -1;
        pDiag = -1;
        for (int x=width-1; x>=0; x--, offset--) {
            pNextDiag = points[x];
            if (bPixels[offset] == 0) {
                points[x] = x | y<<16;      // remember coordinates as a candidate for nearest background point
            } else {                        // foreground pixel:
                float dist2 = minDist2(points, pPrev, pDiag, x, y, distSqr, distance);
                if (fPixels[offset] > dist2) fPixels[offset] = dist2;
            }
            pPrev = points[x];
            pDiag = pNextDiag;
        }
    }
    
    private float minDist2 (int[] points, int pPrev, int pDiag, int x, int y, int distSqr, Distance distance) {
        int p0 = points[x];              // the nearest background point for the same x in the previous line
        int nearestPoint = p0;
        if (p0 != -1) {
            int x0 = p0& 0xffff; int y0 = (p0>>16)&0xffff;
            int dist1Sqr = calcDistance(x, y, x0, y0, distance);
            if (dist1Sqr < distSqr)
                distSqr = dist1Sqr;
        }
        if (pDiag!=p0 && pDiag!=-1) {
            int x1 = pDiag&0xffff; int y1 = (pDiag>>16)&0xffff;
            int dist1Sqr = calcDistance(x, y, x1, y1, distance);
            if (dist1Sqr < distSqr) {
                nearestPoint = pDiag;
                distSqr = dist1Sqr;
            }
        }
        if (pPrev!=pDiag && pPrev!=-1) {
            int x1 = pPrev& 0xffff; int y1 = (pPrev>>16)&0xffff;
            int dist1Sqr = calcDistance(x, y, x1, y1, distance);
            if (dist1Sqr < distSqr) {
                nearestPoint = pPrev;
                distSqr = dist1Sqr;
            }
        }
        points[x] = nearestPoint;
        return (float)distSqr;
    }
    
    private int calcDistance(int x, int y, int x0, int y0, Distance distance){
        int v = 0;
        switch(distance){
            case Euclidean:
                v = (x-x0)*(x-x0)+(y-y0)*(y-y0);
            break;
            case Manhattan:
                v = Math.abs(x-x0) + Math.abs(y-y0);
            break;
            case Chessboard:
                v = Math.max(Math.abs(x-x0), Math.abs(y-y0));
            break;
            case SquaredEuclidean:
                v = (x-x0)*(x-x0)+(y-y0)*(y-y0);
            break;
        }
        return v;
    }
    
    /**
     * Convert Distance map to FastBitmap.
     * @return FastBitmap.
     */
    public FastBitmap toFastBitmap(){
        
        int width = image[0].length;
        int height = image.length;
        
        FastBitmap fb = new FastBitmap(width, height, FastBitmap.ColorSpace.Grayscale);
        
        if (max > 255){
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    fb.setGray(i, j, (int)Catalano.Math.Tools.Scale(0, max, 0, 255, image[i][j]));
                }
            }
        }
        else{
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < width; j++) {
                        fb.setGray(i, j, (int)image[i][j]);
                    }
                }
        }
        
        return fb;
    }
}