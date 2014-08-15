// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2014
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

package Catalano.Imaging.Tools;

import Catalano.Core.IntPoint;
import Catalano.Imaging.FastBitmap;
import java.util.ArrayList;

/**
 * Extract blobs with specified ID.
 * @author Diego Catalano
 */
public class ExtractBlob {
    private ArrayList<Blob> blobs;

    /**
     * Initialize a new instance of the ExtractBlob class.
     */
    public ExtractBlob() {}

    /**
     * Initialize a new instance of the ExtractBlob class.
     * @param blobs Blobs list.
     */
    public ExtractBlob(ArrayList<Blob> blobs) {
        this.blobs = blobs;
    }
    
    /**
     * Extract blob.
     * @param id ID.
     * @param fastBitmap Image to be processed.
     * @return Image with the extracted blob.
     */
    public FastBitmap Extract(int id, FastBitmap fastBitmap){
        
        // Check if blobs list is null.
        if (this.blobs == null)
            this.blobs = new BlobDetection().ProcessImage(fastBitmap);
        
        FastBitmap image;
        
        if (fastBitmap.isGrayscale()) {
            image = new FastBitmap(fastBitmap.getWidth(), fastBitmap.getHeight(), FastBitmap.ColorSpace.Grayscale);
            for (IntPoint p : blobs.get(id).getPoints()) {
                image.setGray(p.x, p.y, fastBitmap.getGray(p.x, p.y));
            }
        }
        else{
            image = new FastBitmap(fastBitmap.getWidth(), fastBitmap.getHeight(), FastBitmap.ColorSpace.RGB);
            for (IntPoint p : blobs.get(id).getPoints()) {
                image.setRed(p.x, p.y, fastBitmap.getRed(p.x, p.y));
                image.setGreen(p.x, p.y, fastBitmap.getGreen(p.x, p.y));
                image.setBlue(p.x, p.y, fastBitmap.getBlue(p.x, p.y));
            }
        }
        return image;
    }
}