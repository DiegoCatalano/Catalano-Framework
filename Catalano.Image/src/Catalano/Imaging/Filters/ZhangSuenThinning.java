// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
// Code adapted from ImageJ, thanks to Wayne Rasband.
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
 * Zhang-Suen Thinning.
 * @author Diego Catalano
 */
public class ZhangSuenThinning implements IApplyInPlace{
    
	private int[] table  =
		 {0,0,0,0,0,0,1,3,0,0,3,1,1,0,1,3,0,0,0,0,0,0,0,0,0,0,2,0,3,0,3,3,
		  0,0,0,0,0,0,0,0,3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,3,0,2,2,
		  0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
		  2,0,0,0,0,0,0,0,2,0,0,0,2,0,0,0,3,0,0,0,0,0,0,0,3,0,0,0,3,0,2,0,
		  0,0,3,1,0,0,1,3,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
		  3,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
		  2,3,1,3,0,0,1,3,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
		  2,3,0,1,0,0,0,1,0,0,0,0,0,0,0,0,3,3,0,1,0,0,0,0,2,2,0,0,2,0,0,0};
		  
	private int[] table2  =
		 {0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,2,2,0,0,0,0,
		  0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,2,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,
		  0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
		  0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
		  0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,
		  0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,
		  0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
		  0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};

    /**
     * Initialize a new instance of the ZhangSuenThinning class.
     */
    public ZhangSuenThinning() {}

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        if (fastBitmap.isGrayscale()) {
            int pass = 0;
            int pixelsRemoved;
            do {
                    pixelsRemoved = thin(pass++, table, fastBitmap);
                    pixelsRemoved += thin(pass++, table, fastBitmap);
            } while (pixelsRemoved>0);
            do {
                    // use a second table to remove "stuck" pixels
                    pixelsRemoved = thin(pass++, table2, fastBitmap);
                    pixelsRemoved += thin(pass++, table2, fastBitmap);
            } while (pixelsRemoved>0);
        }
        else{
            throw new IllegalArgumentException("Zhang Suen Thinning only works with grayscale image.");
        }
        
    }
    
    private int thin(int pass, int[] table, FastBitmap fastBitmap) {
        int p1, p2, p3, p4, p5, p6, p7, p8, p9;
        int height = fastBitmap.getHeight();
        int width = fastBitmap.getWidth();

        byte[] pixels = fastBitmap.getGrayData();
        byte[] pixels2 = new byte[width * height];
        System.arraycopy(fastBitmap.getGrayData(), 0, pixels2, 0, width * height);
        int v, index, code;
        int offset, rowOffset = width;
        int pixelsRemoved = 0;
        for (int y=1; y<=height - 2; y++) {
                offset = y * width + 1;
                for (int x=1; x<=width - 2; x++) {
                        p5 = pixels2[offset];
                        v = p5;
                        if (v!=0) {
                                p1 = pixels2[offset-rowOffset-1];
                                p2 = pixels2[offset-rowOffset];
                                p3 = pixels2[offset-rowOffset+1];
                                p4 = pixels2[offset-1];
                                p6 = pixels2[offset+1];
                                p7 = pixels2[offset+rowOffset-1];
                                p8 = pixels2[offset+rowOffset];
                                p9 = pixels2[offset+rowOffset+1];
                                index = 0;
                                if (p1!=0) index |= 1;
                                if (p2!=0) index |= 2;
                                if (p3!=0) index |= 4;
                                if (p6!=0) index |= 8;
                                if (p9!=0) index |= 16;
                                if (p8!=0) index |= 32;
                                if (p7!=0) index |= 64;
                                if (p4!=0) index |= 128;
                                code = table[index];
                                if ((pass&1)==1) { //odd pass
                                        if (code==2||code==3) {
                                                v = 0;
                                                pixelsRemoved++;
                                        }
                                } else { //even pass
                                        if (code==1||code==3) {
                                                v = 0;
                                                pixelsRemoved++;
                                        }
                                }
                        }
                        pixels[offset++] = (byte)v;
                }
        }
        return pixelsRemoved;
    }
}