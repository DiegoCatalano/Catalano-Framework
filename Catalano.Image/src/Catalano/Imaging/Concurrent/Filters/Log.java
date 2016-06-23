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
 * Log filter.
 * @author Diego
 */
public class Log implements IApplyInPlace{

    /**
     * Initialize a new instance of the Log class.
     */
    public Log() {}
    
    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        Parallel(fastBitmap);
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

            // Scale log
            double scale = 255 / Math.log(255);
            if (share.fastBitmap.isGrayscale()){
                for (int i = share.startX; i < share.endHeight; i++) {
                    for (int j = 0; j < share.fastBitmap.getWidth(); j++) {
                        double v = share.fastBitmap.getGray(i, j);

                        //Compute log
                        if (v != 0) v = Math.log(v) * scale;

                        // Clip value
                        if (v < 0) v = 0;
                        if (v > 255) v = 255;

                        share.fastBitmap.setGray(i, j, (int)(v));
                    }
                }
            }
            if (share.fastBitmap.isRGB()){
                for (int i = share.startX; i < share.endHeight; i++) {
                    for (int j = 0; j < share.fastBitmap.getWidth(); j++) {
                        double r = share.fastBitmap.getRed(i, j);
                        double g = share.fastBitmap.getGreen(i, j);
                        double b = share.fastBitmap.getBlue(i, j);

                        //Compute log
                        if (r != 0) r = Math.log(r) * scale;
                        if (g != 0) g = Math.log(g) * scale;
                        if (b != 0) b = Math.log(b) * scale;

                        //Clip value
                        if (r < 0) r = 0;
                        if (r > 255) r = 255;

                        if (g < 0) g = 0;
                        if (g > 255) g = 255;

                        if (b < 0) b = 0;
                        if (b > 255) b = 255;


                        share.fastBitmap.setRed(i, j, (int)(r));
                        share.fastBitmap.setGreen(i, j, (int)(g));
                        share.fastBitmap.setBlue(i, j, (int)(b));
                    }
                }
            }
        }
    }
}