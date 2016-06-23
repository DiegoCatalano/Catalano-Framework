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
 * Mean Filter.
 * @author Diego Catalano
 */
public class Mean implements IApplyInPlace{
    
    private int radius = 1;
    private FastBitmap copy;

    /**
     * Common arithmetic for compute mean.
     */
    public enum Arithmetic {

        /**
         * Mean.
         */
        Mean,
        
        /**
         * Harmonic.
         */
        Harmonic,
        
        /**
         * Contra Harmonic.
         */
        ContraHarmonic,
        
        /**
         * Geometry.
         */
        Geometry
    };
    
    private Arithmetic arithmetic = Arithmetic.Mean;
    private int order = 1;

    /**
     * Initialize a new instance of the Mean class.
     */
    public Mean() {}

    /**
     * Initialize a new instance of the Mean class.
     * @param radius Radius.
     */
    public Mean(int radius) {
        radius = radius < 1 ? 1 : radius;
        this.radius = radius;
    }

    /**
     * Initialize a new instance of the Mean class.
     * @param arithmetic Mean.
     */
    public Mean(Arithmetic arithmetic) {
        this.arithmetic = arithmetic;
    }
    
    /**
     * Initialize a new instance of the Mean class.
     * @param radius Radius.
     * @param arithmetic Mean.
     */
    public Mean(int radius, Arithmetic arithmetic) {
        radius = radius < 1 ? 1 : radius;
        this.radius = radius;
        this.arithmetic = arithmetic;
    }

    /**
     * Get arithmetic.
     * @return Mean.
     */
    public Arithmetic getArithmetic() {
        return arithmetic;
    }

    /**
     * Set arithmetic.
     * @param arithmetic Mean.
     */
    public void setArithmetic(Arithmetic arithmetic) {
        this.arithmetic = arithmetic;
    }

    /**
     * Get radius.
     * @return Radius.
     */
    public int getRadius() {
        return radius;
    }

    /**
     * Set radius.
     * @param radius Radus.
     */
    public void setRadius(int radius) {
        this.radius = radius;
    }

    /**
     * Get order 
     * @return Order.
     */
    public int getOrder() {
        return order;
    }

    /**
     * Set order.
     * @param order Order.
     */
    public void setOrder(int orderFilter) {
        this.order = orderFilter;
    }
    
    @Override
    public void applyInPlace(FastBitmap fastBitmap){
        copy = new FastBitmap(fastBitmap);
        Parallel(fastBitmap);
    }
    
    private void Parallel(FastBitmap fastBitmap){
        
        int processors = Runtime.getRuntime().availableProcessors();
        
        Thread[] t = new Thread[processors];
        int part = fastBitmap.getHeight() / processors;
        int endHeight = part;
        int last = processors - 1;
        boolean lastThread = false;

        int startX = 0;
        for (int i = 0; i < processors; i++){
            if (i == last) lastThread = true;
            t[i] = new Thread(new Run(new Share(fastBitmap, startX, endHeight, lastThread)));
            t[i].start();
            startX = endHeight;
            endHeight += part;
        }

        try {
            for (int i = 0; i < processors; i++) {
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

            int Xline,Yline;
            int lines = CalcLines(radius);
            int c;

            int safe = radius;

            if (share.lastThread){
                safe = 0;
                share.endHeight = share.fastBitmap.getHeight();
            }

            switch(arithmetic){
                case Mean:
                    if (share.fastBitmap.isGrayscale()) {
                        int sumGray;

                        for (int x = share.startX; x < share.endHeight; x++) {
                            for (int y = 0; y < share.fastBitmap.getWidth(); y++) {
                                c = 0;
                                sumGray = 0;
                                for (int i = 0; i < lines; i++) {
                                    Xline = x + (i-radius);
                                    for (int j = 0; j < lines; j++) {
                                        Yline = y + (j-radius);
                                        if ((Xline >= 0) && (Xline < share.endHeight + safe) && (Yline >=0) && (Yline < share.fastBitmap.getWidth())) {
                                            sumGray += copy.getGray(Xline, Yline);
                                            c++;
                                        }
                                    }
                                }
                                sumGray /= c;
                                share.fastBitmap.setGray(x, y, sumGray);
                            }
                        }
                    }
                    else if(share.fastBitmap.isRGB()){
                        int sumR;
                        int sumG;
                        int sumB;

                        for (int x = share.startX; x < share.endHeight; x++) {
                            for (int y = 0; y < share.fastBitmap.getWidth(); y++) {
                                c = 0;
                                sumR = sumG = sumB = 0;
                                for (int i = 0; i < lines; i++) {
                                    Xline = x + (i-radius);
                                    for (int j = 0; j < lines; j++) {
                                        Yline = y + (j-radius);
                                        if ((Xline >= 0) && (Xline < share.endHeight + safe) && (Yline >=0) && (Yline < share.fastBitmap.getWidth())) {
                                            sumR += copy.getRed(Xline, Yline);
                                            sumG += copy.getGreen(Xline, Yline);
                                            sumB += copy.getBlue(Xline, Yline);
                                            c++;
                                        }
                                    }
                                }
                                sumR /= c;
                                sumG /= c;
                                sumB /= c;
                                share.fastBitmap.setRGB(x, y, sumR, sumG, sumB);
                            }
                        }
                    }
                break;

                case Harmonic:
                    if (share.fastBitmap.isGrayscale()) {
                        double sumGray;
                        for (int x = share.startX; x < share.endHeight; x++) {
                            for (int y = 0; y < share.fastBitmap.getWidth(); y++) {
                                c = 0;
                                sumGray = 0;
                                for (int i = 0; i < lines; i++) {
                                    Xline = x + (i-radius);
                                    for (int j = 0; j < lines; j++) {
                                        Yline = y + (j-radius);
                                        if ((Xline >= 0) && (Xline < share.endHeight + safe) && (Yline >=0) && (Yline < share.fastBitmap.getWidth())) {
                                            sumGray += 1/(double)copy.getGray(Xline, Yline);
                                            c++;
                                        }
                                    }
                                }
                                sumGray = c / sumGray;
                                share.fastBitmap.setGray(x, y, (int)sumGray);
                            }
                        }
                    }
                    else if(share.fastBitmap.isRGB()){
                        double sumR;
                        double sumG;
                        double sumB;

                        for (int x = share.startX; x < share.endHeight; x++) {
                            for (int y = 0; y < share.fastBitmap.getWidth(); y++) {
                                c = 0;
                                sumR = sumG = sumB = 0;
                                for (int i = 0; i < lines; i++) {
                                    Xline = x + (i-radius);
                                    for (int j = 0; j < lines; j++) {
                                        Yline = y + (j-radius);
                                        if ((Xline >= 0) && (Xline < share.endHeight + safe) && (Yline >=0) && (Yline < share.fastBitmap.getWidth())) {
                                            sumR += 1/(double)copy.getRed(Xline, Yline);
                                            sumG += 1/(double)copy.getGreen(Xline, Yline);
                                            sumB += 1/(double)copy.getBlue(Xline, Yline);
                                            c++;
                                        }
                                    }
                                }
                                sumR = c / sumR;
                                sumG = c / sumG;
                                sumB = c / sumB;
                                share.fastBitmap.setRGB(x, y, (int)sumR, (int)sumG, (int)sumB);
                            }
                        }
                    }
                break;

                case ContraHarmonic:
                    if (share.fastBitmap.isGrayscale()) {
                        double sumGray;
                        double sumGrayOne, sumGrayTwo;
                        for (int x = share.startX; x < share.endHeight; x++) {
                            for (int y = 0; y < share.fastBitmap.getWidth(); y++) {
                                sumGrayOne = sumGrayTwo = 0;
                                for (int i = 0; i < lines; i++) {
                                    Xline = x + (i-radius);
                                    for (int j = 0; j < lines; j++) {
                                        Yline = y + (j-radius);
                                        if ((Xline >= 0) && (Xline < share.endHeight + safe) && (Yline >=0) && (Yline < share.fastBitmap.getWidth())) {
                                            sumGrayOne += Math.pow((double)copy.getGray(Xline, Yline),order+1);
                                            sumGrayTwo += Math.pow((double)copy.getGray(Xline, Yline),order);
                                        }
                                    }
                                }
                                sumGray = sumGrayOne / sumGrayTwo;
                                share.fastBitmap.setGray(x, y, (int)sumGray);
                            }
                        }
                    }
                    else if(share.fastBitmap.isRGB()){
                        double sumR, sumG, sumB;
                        double sumRone, sumGone, sumBone;
                        double sumRtwo, sumGtwo, sumBtwo;
                        for (int x = share.startX; x < share.endHeight; x++) {
                            for (int y = 0; y < share.fastBitmap.getWidth(); y++) {
                                sumRone = sumGone = sumBone = 0;
                                sumRtwo = sumGtwo = sumBtwo = 0;
                                for (int i = 0; i < lines; i++) {
                                    Xline = x + (i-radius);
                                    for (int j = 0; j < lines; j++) {
                                        Yline = y + (j-radius);
                                        if ((Xline >= 0) && (Xline < share.endHeight + safe) && (Yline >=0) && (Yline < share.fastBitmap.getWidth())) {
                                            sumRone += Math.pow((double)copy.getRed(Xline, Yline),order + 1);
                                            sumGone += Math.pow((double)copy.getGreen(Xline, Yline),order + 1);
                                            sumBone += Math.pow((double)copy.getBlue(Xline, Yline),order + 1);

                                            sumRtwo += Math.pow((double)copy.getRed(Xline, Yline),order);
                                            sumGtwo += Math.pow((double)copy.getGreen(Xline, Yline),order);
                                            sumBtwo += Math.pow((double)copy.getBlue(Xline, Yline),order);
                                        }
                                    }
                                }
                                sumR = sumRone / sumRtwo;
                                sumG = sumGone / sumGtwo;
                                sumB = sumBone / sumBtwo;
                                share.fastBitmap.setRGB(x, y, (int)sumR, (int)sumG, (int)sumB);
                            }
                        }
                    }
                break;

                case Geometry:
                    if (share.fastBitmap.isGrayscale()) {
                        double sumGray;
                        for (int x = share.startX; x < share.endHeight; x++) {
                            for (int y = 0; y < share.fastBitmap.getWidth(); y++) {
                                c = 0;
                                sumGray = 1;
                                for (int i = 0; i < lines; i++) {
                                    Xline = x + (i-radius);
                                    for (int j = 0; j < lines; j++) {
                                        Yline = y + (j-radius);
                                        if ((Xline >= 0) && (Xline < share.endHeight + safe) && (Yline >=0) && (Yline < share.fastBitmap.getWidth())) {
                                            sumGray *= (double)copy.getGray(Xline, Yline);
                                            c++;
                                        }
                                    }
                                }
                                sumGray = Math.pow(sumGray , (double)1/c);
                                share.fastBitmap.setGray(x, y, (int)sumGray);
                            }
                        }
                    }
                    else if(share.fastBitmap.isRGB()){
                        double sumR;
                        double sumG;
                        double sumB;

                        for (int x = share.startX; x < share.endHeight; x++) {
                            for (int y = 0; y < share.fastBitmap.getWidth(); y++) {
                                c = 0;
                                sumR = sumG = sumB = 1;
                                for (int i = 0; i < lines; i++) {
                                    Xline = x + (i-radius);
                                    for (int j = 0; j < lines; j++) {
                                        Yline = y + (j-radius);
                                        if ((Xline >= 0) && (Xline < share.endHeight + safe) && (Yline >=0) && (Yline < share.fastBitmap.getWidth())) {
                                            sumR *= (double)copy.getRed(Xline, Yline);
                                            sumG *= (double)copy.getGreen(Xline, Yline);
                                            sumB *= (double)copy.getBlue(Xline, Yline);
                                            c++;
                                        }
                                    }
                                }
                                sumR = Math.pow(sumR , (double)1/c);
                                sumG = Math.pow(sumG , (double)1/c);
                                sumB = Math.pow(sumB , (double)1/c);
                                share.fastBitmap.setRGB(x, y, (int)sumR, (int)sumG, (int)sumB);
                            }
                        }
                    }
                break;
            }
        }
    }
    
    private int CalcLines(int radius){
        return radius * 2 + 1;
    }
}