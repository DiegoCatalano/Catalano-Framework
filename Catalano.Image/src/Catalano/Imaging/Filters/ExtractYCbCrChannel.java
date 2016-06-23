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
import Catalano.Imaging.Tools.ColorConverter;

/**
 * Extract YCbCr channel from image.
 * <br />The filter extracts specified YCbCr channel of color image and returns it in the form of grayscale image.
 * @author Diego Catalano
 */
public class ExtractYCbCrChannel implements IApplyInPlace{
    /**
     * Enumeration of components for extraction of channels.
     */
    public enum Channel {

        /**
         *
         */
        Y,
        /**
         *
         */
        Cb,
        /**
         *
         */
        Cr
    };
    private Channel YCbCr;

    /**
     * Initialize a new instance of the ExtractYCbCrChannel class.
     * @param rgb
     */
    public ExtractYCbCrChannel(Channel rgb) {
        this.YCbCr = rgb;
    }
    
    @Override
    public void applyInPlace(FastBitmap fastBitmap){
        if (fastBitmap.isGrayscale()) {
            try {
                throw new Exception("Extract Channel works only with RGB images");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else{
            int width = fastBitmap.getWidth();
            int height = fastBitmap.getHeight();
            
            FastBitmap l = new FastBitmap(width, height, FastBitmap.ColorSpace.Grayscale);
            
            int r,g,b;
            double[] ycbcr;
            switch(YCbCr){
                case Y:
                    for (int x = 0; x < height; x++) {
                        for (int y = 0; y < width; y++) {
                            r = fastBitmap.getRed(x, y);
                            g = fastBitmap.getGreen(x, y);
                            b = fastBitmap.getBlue(x, y);
                            ycbcr = ColorConverter.RGBtoYCbCr(r, g, b, ColorConverter.YCbCrColorSpace.ITU_BT_601);
                            l.setGray(x, y, (int)(ycbcr[0] * 255));
                        }
                    }
                break;
                case Cb:
                    for (int x = 0; x < height; x++) {
                        for (int y = 0; y < width; y++) {
                            r = fastBitmap.getRed(x, y);
                            g = fastBitmap.getGreen(x, y);
                            b = fastBitmap.getBlue(x, y);
                            ycbcr = ColorConverter.RGBtoYCbCr(r, g, b, ColorConverter.YCbCrColorSpace.ITU_BT_601);
                            l.setGray(x, y, (int)((0.5f + ycbcr[1]) * 255));
                        }
                    }
                break;
                case Cr:
                    for (int x = 0; x < height; x++) {
                        for (int y = 0; y < width; y++) {
                            r = fastBitmap.getRed(x, y);
                            g = fastBitmap.getGreen(x, y);
                            b = fastBitmap.getBlue(x, y);
                            ycbcr = ColorConverter.RGBtoYCbCr(r, g, b, ColorConverter.YCbCrColorSpace.ITU_BT_601);
                            l.setGray(x, y, (int)((0.5f + ycbcr[2]) * 255));
                        }
                    }
                break;
            }
            fastBitmap.setImage(l);
        }
    }    
}