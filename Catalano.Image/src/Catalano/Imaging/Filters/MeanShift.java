// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
// Kai Uwe Barthel
// The original algorithm: http://rsbweb.nih.gov/ij/plugins/mean-shift.html
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
 * Mean Shift filter.
 * <br /> Mean Shift filter can be used for edge-preserving smoothing or for segmentation. Important edges of an image might be easier detected after mean shift filtering.
 * <br /> It uses a circular flat kernel and the color distance is calculated in the YIQ-color space.
 * @author Diego Catalano
 */
public class MeanShift implements IApplyInPlace{
    
    private int radius;
    private float colorDistance;

    /**
     * Initialize a new instance of the MeanShift class.
     */
    public MeanShift() {}

    /**
     * Initialize a new instance of the MeanShift class.
     * @param radius Radius.
     * @param colorDistance Color distance.
     */
    public MeanShift(int radius, float colorDistance) {
        this.radius = radius;
        this.colorDistance = colorDistance;
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        int width = fastBitmap.getWidth();
        int height = fastBitmap.getHeight();
        
        if (fastBitmap.isRGB()) {
            float[][][] pixelsF = new float[height][width][3];

            int r,g,b;
            for (int x = 0; x < height; x++) {
                for (int y = 0; y < width; y++) {
                    r = fastBitmap.getRed(x, y);
                    g = fastBitmap.getGreen(x, y);
                    b = fastBitmap.getBlue(x, y);

                    // You can use ColorConverter.RGBtoYIQ but you need to multiply the result with 255.
                    // In this way its more fast because we spend less processor.
                    pixelsF[x][y][0] = 0.299f  *r + 0.587f *g + 0.114f  *b;
                    pixelsF[x][y][1] = 0.5957f *r - 0.2744f*g - 0.3212f *b;
                    pixelsF[x][y][2] = 0.2114f *r - 0.5226f*g + 0.3111f *b;
                }
            }

            float shift;
            int iters;

            for (int x = 0; x < height; x++) {
                for (int y = 0; y < width; y++) {
                    int yc = y;
                    int xc = x;
                    int xcOld, ycOld;
                    float YcOld, IcOld, QcOld;
                    float[] yiq = pixelsF[x][y];
                    float Yc = yiq[0];
                    float Ic = yiq[1];
                    float Qc = yiq[2];

                    iters = 0;
                    do {
                        xcOld = xc;
                        ycOld = yc;
                        YcOld = Yc;
                        IcOld = Ic;
                        QcOld = Qc;

                        float mx = 0;
                        float my = 0;
                        float mY = 0;
                        float mI = 0;
                        float mQ = 0;
                        int num=0;

                        int radius2 = radius * radius;
                        float colorDistance2 = colorDistance * colorDistance;
                        for (int rx=-radius; rx <= radius; rx++) {
                                int x2 = xc + rx; 
                                if (x2 >= 0 && x2 < height) {
                                        for (int ry=-radius; ry <= radius; ry++) {
                                                int y2 = yc + ry; 
                                                if (y2 >= 0 && y2 < width) {
                                                        if (rx*rx + ry*ry <= radius2) {
                                                                yiq = pixelsF[x2][y2];

                                                                float Y2 = yiq[0];
                                                                float I2 = yiq[1];
                                                                float Q2 = yiq[2];

                                                                float dY = Yc - Y2;
                                                                float dI = Ic - I2;
                                                                float dQ = Qc - Q2;

                                                                if (dY*dY+dI*dI+dQ*dQ <= colorDistance2) {
                                                                        mx += x2;
                                                                        my += y2;
                                                                        mY += Y2;
                                                                        mI += I2;
                                                                        mQ += Q2;
                                                                        num++;
                                                                }
                                                        }
                                                }
                                        }
                                }
                        }
                        float num_ = 1f/num;
                        Yc = mY*num_;
                        Ic = mI*num_;
                        Qc = mQ*num_;
                        xc = (int) (mx*num_+0.5);
                        yc = (int) (my*num_+0.5);
                        int dx = xc-xcOld;
                        int dy = yc-ycOld;
                        float dY = Yc-YcOld;
                        float dI = Ic-IcOld;
                        float dQ = Qc-QcOld;

                        shift = dx*dx+dy*dy+dY*dY+dI*dI+dQ*dQ; 
                        iters++;
                    }
                    while (shift > 3 && iters < 100);

                    int r_ = (int)(Yc + 0.9563f*Ic + 0.6210f*Qc);
                    int g_ = (int)(Yc - 0.2721f*Ic - 0.6473f*Qc);
                    int b_ = (int)(Yc - 1.1070f*Ic + 1.7046f*Qc);

                    fastBitmap.setRGB(x, y, r_, g_, b_);
                }
            }
        }
        if (fastBitmap.isGrayscale()) {
            float shift;
            int iters;

            for (int x = 0; x < height; x++) {
                for (int y = 0; y < width; y++) {
                    int yc = y;
                    int xc = x;
                    int xcOld, ycOld;
                    float YcOld;
                    float Yc = fastBitmap.getGray(x, y);

                    iters = 0;
                    do {
                        xcOld = xc;
                        ycOld = yc;
                        YcOld = Yc;

                        float mx = 0;
                        float my = 0;
                        float mY = 0;
                        int num=0;

                        int radius2 = radius * radius;
                        float colorDistance2 = colorDistance * colorDistance;
                        for (int rx=-radius; rx <= radius; rx++) {
                                int x2 = xc + rx; 
                                if (x2 >= 0 && x2 < height) {
                                        for (int ry=-radius; ry <= radius; ry++) {
                                                int y2 = yc + ry; 
                                                if (y2 >= 0 && y2 < width) {
                                                        if (rx*rx + ry*ry <= radius2) {

                                                                float Y2 = fastBitmap.getGray(x2, y2);

                                                                float dY = Yc - Y2;

                                                                if (dY*dY <= colorDistance2) {
                                                                        mx += x2;
                                                                        my += y2;
                                                                        mY += Y2;
                                                                        num++;
                                                                }
                                                        }
                                                }
                                        }
                                }
                        }
                        float num_ = 1f/num;
                        Yc = mY*num_;
                        xc = (int) (mx*num_+0.5);
                        yc = (int) (my*num_+0.5);
                        int dx = xc-xcOld;
                        int dy = yc-ycOld;
                        float dY = Yc-YcOld;

                        shift = dx*dx+dy*dy+dY*dY;
                        iters++;
                    }
                    while (shift > 3 && iters < 100);

                    fastBitmap.setGray(x, y, (int)Yc);
                }
            }
        }
    }
}