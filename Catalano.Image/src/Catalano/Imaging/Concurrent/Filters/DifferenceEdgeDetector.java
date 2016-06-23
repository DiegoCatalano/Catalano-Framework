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

package Catalano.Imaging.Concurrent.Filters;

import Catalano.Imaging.Concurrent.Share;
import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.IApplyInPlace;

/**
 * Difference edge detector.
 * 
 * <para>The filter finds objects' edges by calculating maximum difference
 * between pixels in 4 directions around the processing pixel.</para>
 * 
 * <para>Suppose 3x3 square element of the source image (x - is currently processed pixel):
 * <code lang="none">
 * P1 P2 P3
 * P8  x P4
 * P7 P6 P5
 * </code>
 * The corresponding pixel of the result image equals to:
 * <code lang="none">
 * max( |P1-P5|, |P2-P6|, |P3-P7|, |P4-P8| )
 * </code>
 * </para>
 * 
 * @author Diego Catalano
 */
public class DifferenceEdgeDetector implements IApplyInPlace{
    
    private FastBitmap copy;
    private int max = 0;

    /**
     * Initialize a new instance of the SobelEdgeDetector class.
     */
    public DifferenceEdgeDetector() {}
    
    @Override
    public void applyInPlace(FastBitmap fb){
        int cores = Runtime.getRuntime().availableProcessors();
        this.copy = new FastBitmap(fb);
        
        Thread[] t = new Thread[cores];
        int part = fb.getHeight() / cores;
        int lastT = cores - 1;
        int endWidth = fb.getWidth() - 1;
        
        int startX = 1;
        for (int i = 0; i < cores; i++) {
            if(i == lastT) part = fb.getHeight() - startX - 1;
            t[i] = new Thread(new Run(new Share(fb, startX, 1, endWidth, startX + part)));
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
            
            int max, diff;
            for (int x = share.startX; x < share.endHeight; x++) {
                for (int y = share.startY; y < share.endWidth; y++) {
                    
                    max = 0;
                    
                    diff = copy.getGray(x - 1, y - 1) - copy.getGray(x + 1, y + 1);
                    if (diff < 0) diff = -diff;
                    if (diff > max) max = diff;
                    
                    diff = copy.getGray(x - 1, y) - copy.getGray(x + 1, y);
                    if (diff < 0) diff = -diff;
                    if (diff > max) max = diff;
                    
                    diff = copy.getGray(x - 1, y + 1) - copy.getGray(x + 1, y - 1);
                    if (diff < 0) diff = -diff;
                    if (diff > max) max = diff;
                    
                    diff = copy.getGray(x, y + 1) - copy.getGray(x, y - 1);
                    if (diff < 0) diff = -diff;
                    if (diff > max) max = diff;
                    
                    share.fastBitmap.setGray(x, y, max);
                }
            }
        }
    }
}