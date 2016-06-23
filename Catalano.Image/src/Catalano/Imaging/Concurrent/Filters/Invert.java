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

import Catalano.Imaging.Concurrent.Share;
import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.IApplyInPlace;

/**
 * Invert image.
 * @author Diego Catalano
 */
public class Invert implements IApplyInPlace{

    /**
     * Initialize a new instance of the Invert class.
     */
    public Invert() {}
    
    @Override
    public void applyInPlace(FastBitmap fb){
        Parallel(fb);
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
            
            if(share.fastBitmap.isGrayscale()){
                for (int i = share.startX; i < share.endHeight; i++) {
                    for (int j = 0; j < share.fastBitmap.getWidth(); j++) {
                        int x = 255 - share.fastBitmap.getGray(i, j);
                        share.fastBitmap.setGray(i, j, x);
                    }
                }
            }
            else{
                for (int i = share.startX; i < share.endHeight; i++) {
                    for (int j = 0; j < share.fastBitmap.getWidth(); j++) {
                        int r = 255 - share.fastBitmap.getRed(i, j);
                        int g = 255 - share.fastBitmap.getGreen(i, j);
                        int b = 255 - share.fastBitmap.getBlue(i, j);
                        share.fastBitmap.setRGB(i, j, r, g, b);
                    }
                }
            }
        }
    }
}