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

package Catalano.Imaging.Concurrent.Filters;

import Catalano.Core.IntRange;
import Catalano.Imaging.Concurrent.Share;
import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.IApplyInPlace;

/**
 * Invert image.
 * @author Diego Catalano
 */
public class ColorFiltering implements IApplyInPlace{
    
    private IntRange red, green, blue;

    /**
     * Initialize a new instance of the BottomHat class.
     */
    public ColorFiltering() {}

    /**
     * Initialize a new instance of the BottomHat class.
     * @param red Range of red color component.
     * @param green Range of green color component.
     * @param blue Range of blue color component.
     */
    public ColorFiltering(IntRange red, IntRange green, IntRange blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }
    
    @Override
    public void applyInPlace(FastBitmap fastBitmap){
        if (fastBitmap.isRGB())
            Parallel(fastBitmap);
        else
            throw new IllegalArgumentException("Color Filtering only works in RGB images.");
    }
    
    private void Parallel(FastBitmap fastBitmap){
        int cores = Runtime.getRuntime().availableProcessors();
        
        Thread[] t = new Thread[cores];
        int part = fastBitmap.getHeight() / cores;
        
        int startX = 0;
        for (int i = 0; i < cores; i++) {
            t[i] = new Thread(new Run(new Share(fastBitmap, startX, startX + part)));
            t[i].start();
            startX += part;
        }
        
        try {
            
            for (int i = 0; i < cores; i++) {
                t[i].join();
            }
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    private class Run implements Runnable {

        private Share share;
        
        public Run(Share obj) {
            this.share = obj;
        }

        @Override
        public void run() {
            
            for (int x = share.startX; x < share.endHeight; x++) {
                for (int y = 0; y < share.fastBitmap.getWidth(); y++) {
                    int r = share.fastBitmap.getRed(x, y);
                    int g = share.fastBitmap.getGreen(x, y);
                    int b = share.fastBitmap.getBlue(x, y);

                    if (
                            (r >= red.getMin()) && (r <= red.getMax()) && 
                            (g >= green.getMin()) && (g <= green.getMax()) && 
                            (b >= blue.getMin()) && (b <= blue.getMax())
                    ){
                        share.fastBitmap.setRGB(x, y, r, g, b);
                    }
                    else{
                        share.fastBitmap.setRGB(x, y, 0, 0, 0);
                    }

                }
            }
        }
    }
}