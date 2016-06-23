// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.cm
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

import Catalano.Core.IntPoint;
import Catalano.Imaging.Color;
import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.IApplyInPlace;
import java.util.LinkedList;

/**
 * Flood Fill filter.
 * The purpose of Flood Fill is to color an entire area of connected pixels with the same color.
 * @author Diego Catalano
 */
public class FloodFill implements IApplyInPlace{
    
    /**
     * Specifies different floodfill algorithm.
     */
    public static enum Algorithm {

        /**
         * <br> 4 neighbors.
         * <br> 0 X 0
         * <br> X X X
         * <br> 0 X 0
         */
        FourWay,
        /**
         * <br> 8 neighbors.
         * <br> X X X
         * <br> X X X
         * <br> X X X
         */
        EightWay
    };
    
    private Algorithm algorithm = Algorithm.FourWay;
    
    IntPoint startPoint;
    private Color replace;
    private int gray;
    
    /**
     * Initialize a new instance of the FloodFill class.
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     * @param color Color.
     */
    public FloodFill(int x, int y, Color color){
        this.startPoint = new IntPoint(x, y);
        this.replace = color;
    }

    /**
     * Initialize a new instance of the FloodFill class.
     * @param x X-axis.
     * @param y Y-axis.
     * @param r Red channel value.
     * @param g Green channel value.
     * @param b Blue channel value.
     */
    public FloodFill(int x, int y, int r, int g, int b) {
        this.startPoint = new IntPoint(x, y);
        this.replace = new Color(r, g, b);
    }
    
    /**
     * Initialize a new instance of the FloodFill class.
     * @param x X-axis.
     * @param y Y-axis.
     * @param r Red channel value.
     * @param g Green channel value.
     * @param b Blue channel value.
     * @param algorithm Floodfill algorithm.
     */
    public FloodFill(int x, int y, int r, int g, int b, Algorithm algorithm) {
        this.startPoint = new IntPoint(x, y);
        this.replace = new Color(r, g, b);
        this.algorithm = algorithm;
    }
    
    /**
     * Initialize a new instance of the FloodFill class.
     * @param p Point (x,y);
     * @param r Red channel value.
     * @param g Green channel value.
     * @param b Blue channel value.
     */
    public FloodFill(IntPoint p, int r, int g, int b){
        this.startPoint = p;
        this.replace = new Color(r, g, b);
    }
    
    /**
     * Initialize a new instance of the FloodFill class.
     * @param p Point(x,y).
     * @param color Color.
     */
    public FloodFill(IntPoint p, Color color){
        this.startPoint = p;
        this.replace = color;
    }
    
    /**
     * Initialize a new instance of the FloodFill class.
     * @param p Point (x,y);
     * @param r Red channel value.
     * @param g Green channel value.
     * @param b Blue channel value.
     * @param algorithm Floodfill algorithm.
     */
    public FloodFill(IntPoint p, int r, int g, int b, Algorithm algorithm) {
        this.startPoint = p;
        this.replace = new Color(r,g,b);
        this.algorithm = algorithm;
    }
    
    /**
     * Initialize a new instance of the FloodFill class.
     * @param x X-axis.
     * @param y Y-axis.
     * @param gray Gray channel value.
     */
    public FloodFill(int x, int y, int gray) {
        this.startPoint = new IntPoint(x, y);
        this.gray = gray;
    }
    
    /**
     * Initialize a new instance of the FloodFill class.
     * @param x X-axis.
     * @param y Y-axis.
     * @param gray Gray channel value.
     * @param algorithm Floodfill algorithm.
     */
    public FloodFill(int x, int y, int gray, Algorithm algorithm){
        this.startPoint = new IntPoint(x, y);
        this.algorithm = algorithm;
    }
    
    /**
     * Initialize a new instance of the FloodFill class.
     * @param p Point (x,y).
     * @param gray Gray channel value.
     */
    public FloodFill(IntPoint p, int gray){
        this.startPoint = p;
        this.gray = gray;
    }
    
    /**
     * Initialize a new instance of the FloodFill class.
     * @param p Point (x,y).
     * @param gray Gray channel value.
     * @param algorithm Floodfill algorithm.
     */
    public FloodFill(IntPoint p, int gray, Algorithm algorithm){
        this.startPoint = p;
        this.gray = gray;
        this.algorithm = algorithm;
    }

    /**
     * Floodfill algorithm.
     * @return Floodfill algorithm.
     */
    public Algorithm getAlgorithm() {
        return algorithm;
    }

    /**
     * Floodfill algorithm.
     * @param algorithm Floodfill algorithm.
     */
    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }
    
    /**
     * Sets RGB.
     * @param r Red channel value.
     * @param g Green channel value.
     * @param b Blue channel value.
     */
    public void setRGB(int r, int g, int b){
        this.replace = new Color(r, g, b);
    }
    
    /**
     * Get point.
     * @return IntPoint.
     */
    public IntPoint getPoint(){
        return startPoint;
    }
    
    /**
     * Sets point.
     * @param x X-axis.
     * @param y Y-axis.
     */
    public void setPoint(int x, int y){
        this.startPoint = new IntPoint(x, y);
    }
    
    @Override
    public void applyInPlace(FastBitmap fastBitmap){
        
        if(fastBitmap.isRGB()){
            int width = fastBitmap.getWidth();
            int height = fastBitmap.getHeight();
            LinkedList<IntPoint> examList = new LinkedList();

            Color old = new Color(fastBitmap.getRGB(startPoint));

            switch(algorithm){
                case FourWay:
                    if (!Color.isEqual(old, replace)) {
                        examList.addFirst(new IntPoint(startPoint));
                        
                        while (examList.size() > 0) {
                            IntPoint p = examList.removeLast();
                            old = new Color(fastBitmap.getRGB(p));

                            if (!Color.isEqual(old, replace)) {
                                int x = p.x;
                                int y = p.y;

                                fastBitmap.setRGB(x, y, replace);

                                if (y-1 > 0) {
                                    examList.addFirst(new IntPoint(x,y-1));        // check west neighbor
                                }
                                if (y+1 < width) {
                                    examList.addFirst(new IntPoint(x,y+1));        // check east neighbor
                                }
                                if (x+1 < height) {
                                    examList.addFirst(new IntPoint(x+1,y));        // check south neighbor
                                }
                                if (x-1 > 0) {
                                    examList.addFirst(new IntPoint(x-1,y));        // check north neighbor
                                }
                            }
                        }
                    }
                    break;

                case EightWay:
                    if (!Color.isEqual(old, replace)) {
                        examList.addFirst(new IntPoint(startPoint));
                        while (examList.size() > 0) {
                            IntPoint p = examList.pop();

                            if (Color.isEqual(old, replace)) {
                                int x = p.x;
                                int y = p.y;

                                fastBitmap.setRGB(x, y, replace);

                                if ((x-1 > 0) && (y-1 > 0)) {
                                    examList.addFirst(new IntPoint(x-1,y-1));        // check north-west neighbor
                                }
                                if (x-1 > 0) {
                                    examList.addFirst(new IntPoint(x-1,y));        // check north neighbor
                                }
                                if ((x+1 < height) && (y+1 < width)) {
                                    examList.addFirst(new IntPoint(x+1,y+1));        // check north-east neighbor
                                }
                                if (y-1 > 0) {
                                    examList.addFirst(new IntPoint(x,y-1));        // check west neighbor
                                }
                                if (y+1 < width) {
                                    examList.addFirst(new IntPoint(x,y+1));        // check east neighbor
                                }
                                if ((x+1 < height) && (y-1 > 0)) {
                                    examList.addFirst(new IntPoint(x+1,y-1));        // check south-west neighbor
                                }
                                if (x+1 < height) {
                                    examList.addFirst(new IntPoint(x+1,y));        // check south neighbor
                                }
                                if ((x+1 < height) && (y+1 < width)) {
                                    examList.addFirst(new IntPoint(x+1,y+1));        // check south-east neighbor
                                }
                            }
                        }
                    }
                    break;
            }
        }
        else if (fastBitmap.isGrayscale()){
            int width = fastBitmap.getWidth();
            int height = fastBitmap.getHeight();
            LinkedList<IntPoint> examList = new LinkedList();

            int iGray = fastBitmap.getGray(startPoint);
            
            int _gray = gray;
            int _Gray = _gray;


            switch(algorithm){
                case FourWay:
                    if (iGray != _Gray) {
                        examList.addFirst(new IntPoint(startPoint));
                        while (examList.size() > 0) {
                            IntPoint p = examList.removeLast();
                            _gray = fastBitmap.getGray(p.x, p.y);
                            _Gray = _gray;

                            if (_Gray == iGray) {
                                int x = p.x;
                                int y = p.y;

                                fastBitmap.setGray(x, y, gray);

                                if (y-1 > 0) {
                                    examList.addFirst(new IntPoint(x,y-1));        // check west neighbor
                                }
                                if (y+1 < width) {
                                    examList.addFirst(new IntPoint(x,y+1));        // check east neighbor
                                }
                                if (x+1 < height) {
                                    examList.addFirst(new IntPoint(x+1,y));        // check south neighbor
                                }
                                if (x-1 > 0) {
                                    examList.addFirst(new IntPoint(x-1,y));        // check north neighbor
                                }
                            }
                        }
                    }
                    break;

                case EightWay:
                    if (iGray != _Gray) {
                        examList.addFirst(new IntPoint(startPoint));
                        while (examList.size() > 0) {
                            IntPoint p = examList.pop();
                            _gray = fastBitmap.getGray(p.x, p.y);
                            _Gray = _gray;

                            if (_Gray == iGray) {
                                int x = p.x;
                                int y = p.y;

                                fastBitmap.setGray(x, y, gray);

                                if ((x-1 > 0) && (y-1 > 0)) {
                                    examList.addFirst(new IntPoint(x-1,y-1));        // check north-west neighbor
                                }
                                if (x-1 > 0) {
                                    examList.addFirst(new IntPoint(x-1,y));        // check north neighbor
                                }
                                if ((x+1 < height) && (y+1 < width)) {
                                    examList.addFirst(new IntPoint(x+1,y+1));        // check north-east neighbor
                                }
                                if (y-1 > 0) {
                                    examList.addFirst(new IntPoint(x,y-1));        // check west neighbor
                                }
                                if (y+1 < width) {
                                    examList.addFirst(new IntPoint(x,y+1));        // check east neighbor
                                }
                                if ((x+1 < height) && (y-1 > 0)) {
                                    examList.addFirst(new IntPoint(x+1,y-1));        // check south-west neighbor
                                }
                                if (x+1 < height) {
                                    examList.addFirst(new IntPoint(x+1,y));        // check south neighbor
                                }
                                if ((x+1 < height) && (y+1 < width)) {
                                    examList.addFirst(new IntPoint(x+1,y+1));        // check south-east neighbor
                                }
                            }
                        }
                    }
                    break;
            }
        }
        else{
            throw new IllegalArgumentException("Flood fill only works in RGB and grayscale images.");
        }
    }
}