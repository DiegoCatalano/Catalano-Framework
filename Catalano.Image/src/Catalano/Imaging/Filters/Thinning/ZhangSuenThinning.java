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

package Catalano.Imaging.Filters.Thinning;

import Catalano.Core.IntPoint;
import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.IBaseInPlace;
import java.util.ArrayList;

/**
 * Zhang-Suen Thinning.
 * @author Diego Catalano
 */
public class ZhangSuenThinning implements IBaseInPlace, IThinning{

    /**
     * Initialize a new instance of the ZhangSuenThinning class.
     */
    public ZhangSuenThinning() {}

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        if (fastBitmap.isGrayscale()) {
            ArrayList<IntPoint> skeleton = getSkeletonPoints(fastBitmap);
            fastBitmap.Clear();
            
            for (IntPoint p : skeleton) {
                fastBitmap.setGray(p, 255);
            }
        }
        else{
            throw new IllegalArgumentException("Zhang Suen Thinning only works with grayscale image.");
        }
        
    }

    @Override
    public ArrayList<IntPoint> getSkeletonPoints(FastBitmap fastBitmap) {
        
        ArrayList<IntPoint> lstSkeleton = new ArrayList<IntPoint>();
        
        if (!fastBitmap.isGrayscale()) {
            try {
                throw new Exception("works only with grayscale image");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        //Zhang-Suen Thinning
        int height = fastBitmap.getHeight();
        int width = fastBitmap.getWidth();
        
        int image[][] = new int[height][width];
        int mark[][] = new int[height][width];
        
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                image[x][y] = (fastBitmap.getGray(x, y) == 255) ? 1 : 0;
            }
        }

        boolean hasdelete = true;
        while (hasdelete) {
            
            hasdelete = false;
            for (int x = 0; x < height; x++) {
                for (int y = 0; y < width; y++) {
                    if (image[x][y] == 1) {
                        int nb[] = getNeighbors(image, x, y, width, height);
                        int a = 0;
                        for (int i = 2; i < 9; i++) {
                            if ((nb[i] == 0) && (nb[i + 1] == 1)) {
                                a++;
                            }
                        }
                        if ((nb[9] == 0) && (nb[2] == 1)) {
                            a++;
                        }
                        int b = nb[2] + nb[3] + nb[4] + nb[5] + nb[6] + nb[7] + nb[8] + nb[9];
                        int p1 = nb[2] * nb[4] * nb[6];
                        int p2 = nb[4] * nb[6] * nb[8];
                        if ((a == 1) && ((b >= 2) && (b <= 6))
                                && (p1 == 0) && (p2 == 0)) {
                            mark[x][y] = 0;
                            hasdelete = true;
                        } else {
                            mark[x][y] = 1;
                        }
                    } else {
                        mark[x][y] = 0;
                    }
                }
            }
            for (int x = 0; x < height; x++) {
                for (int y = 0; y < width; y++) {
                    image[x][y] = mark[x][y];
                }
            }

            for (int x = 0; x < height; x++) {
                for (int y = 0; y < width; y++) {
                    if (image[x][y] == 1) {
                        int nb[] = getNeighbors(image, x, y, width, height);
                        int a = 0;
                        for (int i = 2; i < 9; i++) {
                            if ((nb[i] == 0) && (nb[i + 1] == 1)) {
                                a++;
                            }
                        }
                        if ((nb[9] == 0) && (nb[2] == 1)) {
                            a++;
                        }
                        int b = nb[2] + nb[3] + nb[4] + nb[5] + nb[6] + nb[7] + nb[8] + nb[9];
                        int p1 = nb[2] * nb[4] * nb[8];
                        int p2 = nb[2] * nb[6] * nb[8];
                        if ((a == 1) && ((b >= 2) && (b <= 6))
                                && (p1 == 0) && (p2 == 0)) {
                            mark[x][y] = 0;
                            hasdelete = true;
                        } else {
                            mark[x][y] = 1;
                        }
                    } else {
                        mark[x][y] = 0;
                    }
                }
            }
            for (int x = 0; x < height; x++) {
                for (int y = 0; y < width; y++) {
                    image[x][y] = mark[x][y];
                }
            }
        }
        
        //retrieve skeleton points
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                if (image[x][y] == 1)
                    lstSkeleton.add(new IntPoint(x,y));
            }
        }

        return lstSkeleton;
    }
    
    private int[] getNeighbors(int image[][], int x, int y, int height, int width) {
        
        int a[] = new int[10];
        for (int n = 1; n < 10; n++) {
            a[n] = 0;
        }
        if (y - 1 >= 0) {
            a[2] = image[x][y - 1];
            if (x + 1 < width) {
                a[3] = image[x + 1][y - 1];
            }
            if (x - 1 >= 0) {
                a[9] = image[x - 1][y - 1];
            }
        }
        if (y + 1 < height) {
            a[6] = image[x][y + 1];
            if (x + 1 < width) {
                a[5] = image[x + 1][y + 1];
            }
            if (x - 1 >= 0) {
                a[7] = image[x - 1][y + 1];
            }
        }
        if (x + 1 < width) {
            a[4] = image[x + 1][y];
        }
        if (x - 1 >= 0) {
            a[8] = image[x - 1][y];
        }
        return a;
    }
}