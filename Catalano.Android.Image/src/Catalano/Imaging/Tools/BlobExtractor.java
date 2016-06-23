// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
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
package Catalano.Imaging.Tools;

import Catalano.Core.IntPoint;
import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.Shapes.IntRectangle;
import java.util.List;

/**
 * Blob Extractor.
 * Extract a specified blob in the image.
 * 
 * @author Diego Catalano
 */
public class BlobExtractor {
    
    private boolean keepOriginalImage;

    /**
     * Check if needs to keep the original image size.
     * @return True if needs to keep the original image size.
     */
    public boolean isKeepOriginalImage() {
        return keepOriginalImage;
    }

    /**
     * Set if needs to keep the original image size.
     * @param keepOriginalImage True if needs to keep the original image size.
     */
    public void setKeepOriginalImage(boolean keepOriginalImage) {
        this.keepOriginalImage = keepOriginalImage;
    }

    /**
     * Initializes a new instance of the BlobExtractor class.
     */
    public BlobExtractor() {
        this(false);
    }

    /**
     * Initializes a new instance of the BlobExtractor class.
     * @param keepOriginalImage Keep original size.
     */
    public BlobExtractor(boolean keepOriginalImage) {
        this.keepOriginalImage = keepOriginalImage;
    }
    
    /**
     * Extract a blob in the image.
     * @param fastBitmap Reference image.
     * @param blob Blob information.
     * @return New image that contains the reconstructed blob.
     */
    public FastBitmap Extract(FastBitmap fastBitmap, Blob blob){
        int height = blob.getHeight();
        int width = blob.getWidth();
        
        List<IntPoint> points = blob.getPoints();
        IntRectangle rect = blob.getBoundingBox();
        
        if(!keepOriginalImage){
            FastBitmap image = new FastBitmap(width, height, fastBitmap.getColorSpace());
            if(image.isGrayscale()){
                for (IntPoint p : points) {
                    image.setGray(p.x - rect.x, p.y - rect.y, fastBitmap.getGray(p));
                }
            }
            else{
                for (IntPoint p : points) {
                    image.setRGB(p.x - rect.x, p.y - rect.y, fastBitmap.getRGB(p));
                }
            }

            return image;
        }
        
        FastBitmap image = new FastBitmap(fastBitmap.getWidth(), fastBitmap.getHeight(), fastBitmap.getColorSpace());
        if(image.isGrayscale()){
            for (IntPoint p : points) {
                image.setGray(p, fastBitmap.getGray(p));
            }
        }
        else{
            for (IntPoint p : points) {
                image.setRGB(p, fastBitmap.getRGB(p));
            }
        }
        
        return image;
    }
    
    /**
     * Extract a blob in the image using bounding box.
     * @param fastBitmap Reference image.
     * @param blob Blob information.
     * @return New image that contains the reconstructed blob.
     */
    public FastBitmap ExtractBox(FastBitmap fastBitmap, Blob blob){
        
        int height = blob.getHeight()-1;
        int width = blob.getWidth()-1;
        
        IntRectangle rect = blob.getBoundingBox();
        
        if(!keepOriginalImage){
            FastBitmap image = new FastBitmap(width, height, fastBitmap.getColorSpace());
            if(image.isGrayscale()){
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < width; j++) {
                        image.setGray(i, j, fastBitmap.getGray(rect.x + i + 1, rect.y + j + 1));
                    }
                }
            }
            else{
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < width; j++) {
                        image.setRGB(i, j, fastBitmap.getRGB(rect.x + i + 1, rect.y + j + 1));
                    }
                }
            }

            return image;
        }
        
        FastBitmap image = new FastBitmap(fastBitmap.getWidth(), fastBitmap.getHeight(), fastBitmap.getColorSpace());
        
        if(image.isGrayscale()){
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    image.setGray(rect.x+i, rect.y+j, fastBitmap.getGray(rect.x + i, rect.y + j));
                }
            }
        }
        else{
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    image.setRGB(rect.x+i, rect.y+j, fastBitmap.getRGB(rect.x + i, rect.y + j));
                }
            }
        }
        
        return image;
    }
}