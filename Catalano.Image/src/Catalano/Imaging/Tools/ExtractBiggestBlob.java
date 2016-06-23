// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
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
import java.util.List;

/**
 * Extract Biggest Blob from an image.
 * @author Diego Catalano
 */
public class ExtractBiggestBlob {
    
    private boolean keepOriginalImage;

    /**
     * Initialize a new instance of the ExtractBiggestBlob class.
     */
    public ExtractBiggestBlob() {
        this(false);
    }
    
    /**
     * Initialize a new instance of the ExtractBiggestBlob class.
     * @param keepOriginalImage Keep original image when extract the blob.
     */
    public ExtractBiggestBlob(boolean keepOriginalImage){
        this.keepOriginalImage = keepOriginalImage;
    }
    
    /**
     * Extract the biggest blob.
     * @param fastBitmap Image to be processed.
     * @return A new image that contains the biggest blob.
     */
    public FastBitmap Extract(FastBitmap fastBitmap){
        
        if(!fastBitmap.isGrayscale())
            throw new IllegalArgumentException("Extract Biggest Blob only works in grayscale images.");
        
        BlobDetection bd = new BlobDetection();
        List<Blob> blobs = bd.ProcessImage(fastBitmap);
        
        Blob blob = blobs.get(bd.getIdBiggestBlob());
        if(keepOriginalImage == false){
            BlobExtractor e = new BlobExtractor();
            return e.Extract(fastBitmap, blob);
        }
        
        FastBitmap fb = new FastBitmap(fastBitmap.getWidth(), fastBitmap.getHeight(), FastBitmap.ColorSpace.Grayscale);
        
        List<IntPoint> points = blob.getPoints();
        for (IntPoint p : points)
            fb.setGray(p, fastBitmap.getGray(p));
        
        return fb;
    }
}