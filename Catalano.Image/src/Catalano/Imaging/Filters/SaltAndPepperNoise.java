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
import java.util.Random;

/**
 * Salt and pepper noise.
 * <br /> The filter adds random salt and pepper noise - sets maximum or minimum values to randomly selected pixels.
 * @author Diego Catalano
 */
public class SaltAndPepperNoise implements IApplyInPlace{
    private int noiseAmount = 10;
    private Random random = new Random();

    /**
     * Initializes a new instance of the OtsuThreshold class.
     */
    public SaltAndPepperNoise() {}
    
    /**
     * Initializes a new instance of the OtsuThreshold class.
     * @param noiseAmount Amount of noise to generate in percents, [0, 100].
     */
    public SaltAndPepperNoise(int noiseAmount) {
        this.noiseAmount = Math.max(0, Math.min(100,noiseAmount));
    }
    
    @Override
    public void applyInPlace(FastBitmap fastBitmap){
        int width = fastBitmap.getWidth();
        int height = fastBitmap.getHeight();
        int noise = (width*height*noiseAmount) / 200;
        
        if (fastBitmap.isGrayscale()) {
            for (int i = 0; i < noise; i++) {
                int x = random.nextInt(height);
                int y = random.nextInt(width);

                int[] c = new int[] {0,255};
                int color = random.nextInt(2);
                
                fastBitmap.setGray(x, y, c[color]);
            }
        }
        else if(fastBitmap.isRGB()){
            for (int i = 0; i < noise; i++) {
                int x = random.nextInt(height);
                int y = random.nextInt(width);

                int[] c = new int[] {0,255};
                int band = random.nextInt(2);
                int color = random.nextInt(2);

                if (band == 0) {
                    fastBitmap.setRed(x, y, c[color]);
                }
                else if (band == 1) {
                    fastBitmap.setGreen(x, y, c[color]);
                }
                else if (band == 2) {
                    fastBitmap.setBlue(x, y, c[color]);
                }
            }
        }
    }
}