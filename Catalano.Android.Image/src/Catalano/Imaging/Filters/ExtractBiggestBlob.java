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

package Catalano.Imaging.Filters;

import Catalano.Core.IntPoint;
import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.IBaseInPlace;
import Catalano.Imaging.Tools.Blob;
import Catalano.Imaging.Tools.BlobDetection;
import Catalano.Math.Geometry.PointsCloud;
import java.util.ArrayList;

/**
 * Extract Biggest Blob from an image.
 * @author Diego Catalano
 */
public class ExtractBiggestBlob implements IBaseInPlace{
    
    private boolean keepOriginalImage = true;

    /**
     * Initialize a new instance of the ExtractBiggestBlob class.
     */
    public ExtractBiggestBlob() {}
    
    /**
     * Initialize a new instance of the ExtractBiggestBlob class.
     * @param keepOriginalImage Keep original image when extract the blob.
     */
    public ExtractBiggestBlob(boolean keepOriginalImage){
        this.keepOriginalImage = keepOriginalImage;
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        if(fastBitmap.isGrayscale()){
            
            BlobDetection bd = new BlobDetection();
            ArrayList<Blob> blobs = bd.ProcessImage(fastBitmap);
            
            int id = bd.getIdBiggestBlob();
            
            ArrayList<IntPoint> lst = blobs.get(id).getPoints();
            
            if (keepOriginalImage){
                
                FastBitmap fb = new FastBitmap(fastBitmap.getWidth(), fastBitmap.getHeight(), FastBitmap.ColorSpace.Grayscale);
                for (int i = 0; i < lst.size(); i++) {
                    fb.setGray(lst.get(i), 255);
                }
                fastBitmap.setImage(fb);
            }
            else{
                
                lst = PointsCloud.GetBoundingRectangle(lst);
                
                int height = Math.abs(lst.get(0).x - lst.get(1).x);
                int width = Math.abs(lst.get(0).y - lst.get(1).y);
                
                Crop crop = new Crop(lst.get(0).x - 2, lst.get(0).y - 2, width + 5, height + 4);
                crop.ApplyInPlace(fastBitmap);
                
            }
        }
        else{
            throw new IllegalArgumentException("Extract Biggest Blob only works in grayscale images.");
        }
    }
}