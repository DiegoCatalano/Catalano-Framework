// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2014
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
import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.IBaseInPlace;
import java.util.Stack;

/**
 * Flood Fill filter.
 * The purpose of Flood Fill is to color an entire area of connected pixels with the same color.
 * @author Diego Catalano
 */
public class FloodFill implements IBaseInPlace{
    
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
    int x,y;
    int r,g,b;
    int gray;

    /**
     * Initialize a new instance of the FloodFill class.
     * @param x X-axis.
     * @param y Y-axis.
     * @param r Red channel value.
     * @param g Green channel value.
     * @param b Blue channel value.
     */
    public FloodFill(int x, int y, int r, int g, int b) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.g = g;
        this.b = b;
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
        this.x = x;
        this.y = y;
        this.r = r;
        this.g = g;
        this.b = b;
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
        this.x = p.x;
        this.y = p.y;
        this.r = r;
        this.g = g;
        this.b = b;
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
        this.x = p.x;
        this.y = p.y;
        this.r = r;
        this.g = g;
        this.b = b;
        this.algorithm = algorithm;
    }
    
    /**
     * Initialize a new instance of the FloodFill class.
     * @param x X-axis.
     * @param y Y-axis.
     * @param gray Gray channel value.
     */
    public FloodFill(int x, int y, int gray) {
        this.x = x;
        this.y = y;
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
        this.x = x;
        this.y = y;
        this.algorithm = algorithm;
    }
    
    /**
     * Initialize a new instance of the FloodFill class.
     * @param p Point (x,y).
     * @param gray Gray channel value.
     */
    public FloodFill(IntPoint p, int gray){
        this.x = p.x;
        this.y = p.y;
        this.gray = gray;
    }
    
    /**
     * Initialize a new instance of the FloodFill class.
     * @param p Point (x,y).
     * @param gray Gray channel value.
     * @param algorithm Floodfill algorithm.
     */
    public FloodFill(IntPoint p, int gray, Algorithm algorithm){
        this.x = p.x;
        this.y = p.y;
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
        this.r = r;
        this.g = g;
        this.b = b;
    }

    /**
     * X-axis.
     * @return X-axis.
     */
    public int getX() {
        return x;
    }

    /**
     * X-axis.
     * @param x X-axis.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Y-axis.
     * @return Y-axis.
     */
    public int getY() {
        return y;
    }

    /**
     * Y-axis.
     * @param y Y-axis.
     */
    public void setY(int y) {
        this.y = y;
    }
    
    /**
     * Get point.
     * @return IntPoint.
     */
    public IntPoint getPoint(){
        return new IntPoint(x,y);
    }
    
    /**
     * Sets point.
     * @param x X-axis.
     * @param y Y-axis.
     */
    public void setPoint(int x, int y){
        this.x = x;
        this.y = y;
    }
    
    @Override
    public void applyInPlace(FastBitmap fastBitmap){
        
        if(fastBitmap.isRGB()){
            int width = fastBitmap.getWidth();
            int height = fastBitmap.getHeight();
            Stack<IntPoint> examList = new Stack();

            int iR = fastBitmap.getRed(x, y);
            int iG = fastBitmap.getGreen(x, y);
            int iB = fastBitmap.getBlue(x, y);
            int iRGB = iR+iG+iB;

            int _r = r;
            int _g = g;
            int _b = b;
            int _RGB = _r + _g + _b;


            switch(algorithm){
                case FourWay:
                    if (iRGB != _RGB) {
                        examList.push(new IntPoint(x,y));
                        while (examList.size() > 0) {
                            IntPoint p = examList.pop();
                            _r = fastBitmap.getRed(p.x, p.y);
                            _g = fastBitmap.getGreen(p.x, p.y);
                            _b = fastBitmap.getBlue(p.x, p.y);
                            _RGB = _r + _g + _b;

                            if (_RGB == iRGB) {
                                x = p.x;
                                y = p.y;

                                fastBitmap.setRGB(x, y, r, g, b);

                                if (y-1 > 0) {
                                    examList.push(new IntPoint(x,y-1));        // check west neighbor
                                }
                                if (y+1 < width) {
                                    examList.push(new IntPoint(x,y+1));        // check east neighbor
                                }
                                if (x+1 < height) {
                                    examList.push(new IntPoint(x+1,y));        // check south neighbor
                                }
                                if (x-1 > 0) {
                                    examList.push(new IntPoint(x-1,y));        // check north neighbor
                                }
                            }
                        }
                    }
                    break;

                case EightWay:
                    if (iRGB != _RGB) {
                        examList.push(new IntPoint(x,y));
                        while (examList.size() > 0) {
                            IntPoint p = examList.pop();
                            _r = fastBitmap.getRed(p.x, p.y);
                            _g = fastBitmap.getGreen(p.x, p.y);
                            _b = fastBitmap.getBlue(p.x, p.y);
                            _RGB = _r + _g + _b;

                            if (_RGB == iRGB) {
                                x = p.x;
                                y = p.y;

                                fastBitmap.setRGB(x, y, r, g, b);

                                if ((x-1 > 0) && (y-1 > 0)) {
                                    examList.push(new IntPoint(x-1,y-1));        // check north-west neighbor
                                }
                                if (x-1 > 0) {
                                    examList.push(new IntPoint(x-1,y));        // check north neighbor
                                }
                                if ((x+1 < height) && (y+1 < width)) {
                                    examList.push(new IntPoint(x+1,y+1));        // check north-east neighbor
                                }
                                if (y-1 > 0) {
                                    examList.push(new IntPoint(x,y-1));        // check west neighbor
                                }
                                if (y+1 < width) {
                                    examList.push(new IntPoint(x,y+1));        // check east neighbor
                                }
                                if ((x+1 < height) && (y-1 > 0)) {
                                    examList.push(new IntPoint(x+1,y-1));        // check south-west neighbor
                                }
                                if (x+1 < height) {
                                    examList.push(new IntPoint(x+1,y));        // check south neighbor
                                }
                                if ((x+1 < height) && (y+1 < width)) {
                                    examList.push(new IntPoint(x+1,y+1));        // check south-east neighbor
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
            Stack<IntPoint> examList = new Stack();

            int iGray = fastBitmap.getGray(x, y);
            
            int _gray = gray;
            int _Gray = _gray;


            switch(algorithm){
                case FourWay:
                    if (iGray != _Gray) {
                        examList.push(new IntPoint(x,y));
                        while (examList.size() > 0) {
                            IntPoint p = examList.pop();
                            _gray = fastBitmap.getGray(p.x, p.y);
                            _Gray = _gray;

                            if (_Gray == iGray) {
                                x = p.x;
                                y = p.y;

                                fastBitmap.setGray(x, y, gray);

                                if (y-1 > 0) {
                                    examList.push(new IntPoint(x,y-1));        // check west neighbor
                                }
                                if (y+1 < width) {
                                    examList.push(new IntPoint(x,y+1));        // check east neighbor
                                }
                                if (x+1 < height) {
                                    examList.push(new IntPoint(x+1,y));        // check south neighbor
                                }
                                if (x-1 > 0) {
                                    examList.push(new IntPoint(x-1,y));        // check north neighbor
                                }
                            }
                        }
                    }
                    break;

                case EightWay:
                    if (iGray != _Gray) {
                        examList.push(new IntPoint(x,y));
                        while (examList.size() > 0) {
                            IntPoint p = examList.pop();
                            _gray = fastBitmap.getGray(p.x, p.y);
                            _Gray = _gray;

                            if (_Gray == iGray) {
                                x = p.x;
                                y = p.y;

                                fastBitmap.setGray(x, y, gray);

                                if ((x-1 > 0) && (y-1 > 0)) {
                                    examList.push(new IntPoint(x-1,y-1));        // check north-west neighbor
                                }
                                if (x-1 > 0) {
                                    examList.push(new IntPoint(x-1,y));        // check north neighbor
                                }
                                if ((x+1 < height) && (y+1 < width)) {
                                    examList.push(new IntPoint(x+1,y+1));        // check north-east neighbor
                                }
                                if (y-1 > 0) {
                                    examList.push(new IntPoint(x,y-1));        // check west neighbor
                                }
                                if (y+1 < width) {
                                    examList.push(new IntPoint(x,y+1));        // check east neighbor
                                }
                                if ((x+1 < height) && (y-1 > 0)) {
                                    examList.push(new IntPoint(x+1,y-1));        // check south-west neighbor
                                }
                                if (x+1 < height) {
                                    examList.push(new IntPoint(x+1,y));        // check south neighbor
                                }
                                if ((x+1 < height) && (y+1 < width)) {
                                    examList.push(new IntPoint(x+1,y+1));        // check south-east neighbor
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
//
///**
// * A simple stack in points for image manipulation.
// * @author Diego Catalano
// */
// class PointStack {
//    
//    int[] dataX;
//    int[] dataY;
//    
//    int walk = 0;
//
//    /**
//     * Create stack.
//     * @param width width of image
//     * @param height height of image
//     */
//    public PointStack(int width, int height) {
//        dataX = new int[height*height*3];
//        dataY = new int[width*width*3];
//    }
//    
//    /**
//     * Pushes a value into the stack.
//     */
//    public void push(IntPoint p){
//        dataX[walk] = p.x;
//        dataY[walk] = p.y;
//        walk++;
//    }
//    
//    /**
//     * Pops a value off of the stack.
//     */
//    public IntPoint pop(){
//        walk--;
//        return new IntPoint(dataX[walk],dataY[walk]);
//    }
//    
//    /**
//     * Peeks at the top of the stack.
//     */
//    public IntPoint peak(){
//        return new IntPoint(dataX[walk - 1], dataY[walk - 1]);
//    }
//    
//    /**
//     * Clears the stack.
//     */
//    public void clear(){
//        walk = 0;
//    }
//    
//    /**
//     * Returns the size of the stack.
//     */
//    public int size(){
//        return walk;
//    }
//    
//    /**
//     * Return true if is empty, otherwise false.
//     */
//    public boolean isEmpty(){
//        if (walk > 0) {
//            return false;
//        }
//        return true;
//    }
//}