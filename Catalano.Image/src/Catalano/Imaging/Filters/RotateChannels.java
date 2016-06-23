// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
// Copyright © Andrew Kirillov, 2007-2008
// andrew.kirillov@gmail.com
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
 * Rotate RGB channels.
 * <br /> The filter rotates RGB channels: red channel is replaced with green, green channel is replaced with blue, blue channel is replaced with red.
 * @author Diego Catalano
 */
public class RotateChannels implements IApplyInPlace{

    /**
     * Initialize a new instance of the RotateChannels class.
     */
    public RotateChannels() {}
    
    @Override
    public void applyInPlace(FastBitmap fastBitmap){
        
        if(fastBitmap.isRGB()){
            int r,g,b,z;
            
            int size = fastBitmap.getSize();
            for (int i = 0; i < size; i++) {
                r = fastBitmap.getRed(i); //red
                g = fastBitmap.getGreen(i); //green
                b = fastBitmap.getBlue(i); //blue

                z = g; //green

                fastBitmap.setRGB(i, z, b, r);
            }
        }
        else{
            throw new IllegalArgumentException("RotateChannels only works in RGB imagens.");
        }
    }
}