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

package Catalano.Imaging.Filters;

import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.IApplyInPlace;

/**
 * Vertical run length smoothing algorithm.
 * 
 * <para>The class implements vertical run length smoothing algorithm, which
 * is described in: <b>K.Y. Wong, R.G. Casey and F.M. Wahl, "Document analysis system,"
 * IBM J. Res. Devel., Vol. 26, NO. 6,111). 647-656, 1982.</b></para>
 * 
 * <para>Unlike the original description of this algorithm, this implementation must be applied
 * to inverted binary images containing document, i.e. white text on black background. So this
 * implementation fills horizontal black gaps between white pixels.</para>
 * 
 * @author Diego Catalano
 */
public class VerticalRunLengthSmoothing implements IApplyInPlace{
    
    private int maxGapSize = 10;
    private boolean processGapsWithImageBorders = false;

    /**
     * Get Maximum gap size to fill (in pixels).
     * 
     * <para>The property specifies maximum horizontal gap between white pixels to fill.
     * If number of black pixels between some white pixels is bigger than this value, then those
     * black pixels are left as is; otherwise the gap is filled with white pixels.</para>
     * 
     * @return Gap size.
     */
    public int getMaxGapSize() {
        return maxGapSize;
    }

    /**
     * Set Maximum gap size to fill (in pixels).
     * 
     * <para>The property specifies maximum horizontal gap between white pixels to fill.
     * If number of black pixels between some white pixels is bigger than this value, then those
     * black pixels are left as is; otherwise the gap is filled with white pixels.</para>
     * 
     * @param maxGapSize Gap size.
     */
    public void setMaxGapSize(int maxGapSize) {
        this.maxGapSize = Math.max( 1, Math.min( 1000, maxGapSize ));
    }
    
    /**
     * Verify Process gaps between objects and image borders or not.
     * 
     * <para>The property sets if gaps between image borders and objects must be treated as
     * gaps between objects and also filled.</para>
     * 
     * @return True if process
     */
    public boolean isProcessGapsWithImageBorders() {
        return processGapsWithImageBorders;
    }

    /**
     * Set Process gaps between objects and image borders or not.
     * 
     * <para>The property sets if gaps between image borders and objects must be treated as
     * gaps between objects and also filled.</para>
     * 
     * @param processGapsWithImageBorders True of false.
     */
    public void setProcessGapsWithImageBorders(boolean processGapsWithImageBorders) {
        this.processGapsWithImageBorders = processGapsWithImageBorders;
    }
    
    /**
     * Initialize a new instance of the VerticalRunLengthSmoothing class.
     */
    public VerticalRunLengthSmoothing() {}

    /**
     * Initialize a new instance of the VerticalRunLengthSmoothing class.
     * @param maxGapSize Maximum gap size to fill.
     */
    public VerticalRunLengthSmoothing(int maxGapSize) {
        setMaxGapSize(maxGapSize);
    }
    
    /**
     * Initialize a new instance of the VerticalRunLengthSmoothing class.
     * @param maxGapSize Maximum gap size to fill.
     * @param processGapsWithImageBorders Process gaps between objects and image borders or not.
     */
    public VerticalRunLengthSmoothing(int maxGapSize, boolean processGapsWithImageBorders) {
        setMaxGapSize(maxGapSize);
        setProcessGapsWithImageBorders(processGapsWithImageBorders);
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        if (fastBitmap.isGrayscale()){
            
            int width = fastBitmap.getWidth();
            int height = fastBitmap.getHeight();
            
            for (int j = 0; j < width; j++) {
                
                int position = 0;
                int columnEndPtr = height;
                int columnStart = position;
                
                // fill gaps between white pixels
                while(position < columnEndPtr){
                    
                    int gapStart = position;
                    int gapSize = 0;
                    
                    // look for non black pixel
                    while ( ( position < columnEndPtr ) && ( fastBitmap.getGray(position, j) == 0 ) ){
                        position++;
                        gapSize++;
                    }
                    
                    // fill the gap between white areas
                    if ( gapSize <= maxGapSize ){
                        if ((processGapsWithImageBorders) || ((gapStart != columnStart) && (position != columnEndPtr))){
                            while ( gapStart < position ){
                                fastBitmap.setGray(gapStart, j, 255);
                                gapStart++;
                            }
                        }
                    }
                    
                    // skip all non black pixels
                    while ( ( position < columnEndPtr ) && ( fastBitmap.getGray(position, j) != 0 ) ){
                        position++;
                    }
                }
            }
        }
        else{
            throw new IllegalArgumentException("HorizontalRunLengthSmoothing only works in grayscale images.");
        }
    }
}