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

package Catalano.Imaging.Filters;

import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.IApplyInPlace;
import Catalano.Imaging.Tools.ColorConverter;

/**
 * RGChromaticity filter.
 * @see http://en.wikipedia.org/wiki/Rg_chromaticity
 * @author Diego Catalano
 */
public class RGChromaticity implements IApplyInPlace{

    /**
     * Initialize a new instance of the RGChromaticity class.
     */
    public RGChromaticity() {}

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        if (fastBitmap.isRGB()) {

            int size = fastBitmap.getSize();
            for (int i = 0; i < size; i++) {
                double[] color = ColorConverter.RGChromaticity(fastBitmap.getRed(i), fastBitmap.getGreen(i), fastBitmap.getBlue(i));
                fastBitmap.setRGB(i, (int)(color[0] * 255), (int)(color[1] * 255), (int)(color[2] * 255));
            }
        }
        else{
            try {
                throw new IllegalArgumentException("RGChromaticity only works with RGB images");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}