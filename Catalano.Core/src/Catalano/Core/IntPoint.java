// Catalano Core Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
// Copyright © Andrew Kirillov, 2007-2008
// andrew.kirillov at gmail.com
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

package Catalano.Core;

/**
 * Class for representing a pair of coordinates of integer type.
 * @author Diego Catalano
 */
public class IntPoint {
    
    /**
     * X axis coordinate.
     */
    /**
     * Y axis coordinate.
     */
    public int x,y;

    /**
     * Initializes a new instance of the IntPoint class.
     */
    public IntPoint() {}
    
    /**
     * Initializes a new instance of the IntPoint class.
     * @param point IntPoint.
     */
    public IntPoint(IntPoint point){
        this.x = point.x;
        this.y = point.y;
    }

    /**
     * Initializes a new instance of the IntPoint class.
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     */
    public IntPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Initializes a new instance of the IntPoint class.
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     */
    public IntPoint(float x, float y) {
        this.x = (int)x;
        this.y = (int)y;
    }
    
    /**
     * Initializes a new instance of the IntPoint class.
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     */
    public IntPoint(double x, double y) {
        this.x = (int)x;
        this.y = (int)y;
    }
    
    /**
     * Initializes a new instance of the IntPoint class.
     * @param point FloatPoint.
     */
    public IntPoint(FloatPoint point){
        this.x = (int)point.x;
        this.y = (int)point.y;
    }
    
    /**
     * Initializes a new instance of the IntPoint class.
     * @param point DoublePoint.
     */
    public IntPoint(DoublePoint point){
        this.x = (int)point.x;
        this.y = (int)point.y;
    }
    
    /**
     * Sets X and Y axis coordinates.
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     */
    public void setXY(int x, int y){
        this.x = x;
        this.y = y;
    }
    
    /**
     * Adds values of two points.
     * @param point IntPoint.
     */
    public void Add(IntPoint point){
        this.x += point.x;
        this.y += point.y;
    }
    
    /**
     * Adds values of two points.
     * @param point1 IntPoint.
     * @param point2 IntPoint.
     * @return IntPoint that contains X and Y axis coordinate.
     */
    public static IntPoint Add(IntPoint point1, IntPoint point2){
        IntPoint result = new IntPoint(point1);
        result.Add(point2);
        return result;
    }
    
    /**
     * Adds values of two points.
     * @param value Value.
     */
    public void Add(int value){
        this.x += value;
        this.y += value;
    }
    
    /**
     * Subtract values of two points.
     * @param point IntPoint.
     */
    public void Subtract(IntPoint point){
        this.x -= point.x;
        this.y -= point.y;
    }
    
    /**
     * Subtract values of two points.
     * @param point1 IntPoint.
     * @param point2 IntPoint.
     * @return IntPoint that contains X and Y axis coordinate.
     */
    public static IntPoint Subtract(IntPoint point1, IntPoint point2){
        IntPoint result = new IntPoint(point1);
        result.Subtract(point2);
        return result;
    }
    
    /**
     * Subtract values of two points.
     * @param value
     */
    public void Subtract(int value){
        this.x -= value;
        this.y -= value;
    }
    
    /**
     * Multiply values of two points.
     * @param point IntPoint.
     */
    public void Multiply(IntPoint point){
        this.x *= point.x;
        this.y *= point.y;
    }
    
    /**
     * Multiply values of two points.
     * @param point1 IntPoint.
     * @param point2 IntPoint.
     * @return IntPoint that contains X and Y axis coordinate.
     */
    public static IntPoint Multiply(IntPoint point1, IntPoint point2){
        IntPoint result = new IntPoint(point1);
        result.Multiply(point2);
        return result;
    }
    
    /**
     * Multiply values of two points.
     * @param value Value.
     */
    public void Multiply(int value){
        this.x *= value;
        this.y *= value;
    }
    
    /**
     * Divides values of two points.
     * @param point IntPoint.
     */
    public void Divide(IntPoint point){
        this.x /= point.x;
        this.y /= point.y;
    }
    
    /**
     * Divides values of two points.
     * @param point1 IntPoint.
     * @param point2 IntPoint.
     * @return IntPoint that contains X and Y axis coordinate.
     */
    public static IntPoint Divide(IntPoint point1, IntPoint point2){
        IntPoint result = new IntPoint(point1);
        result.Divide(point2);
        return result;
    }
    
    /**
     * Divides values of two points.
     * @param value Value.
     */
    public void Divide(int value){
        this.x /= value;
        this.y /= value;
    }
    
    /**
     * Calculate Euclidean distance between two points.
     * @param anotherPoint Point to calculate distance to.
     * @return Euclidean distance between this point and anotherPoint points.
     */
    public float DistanceTo(IntPoint anotherPoint){
        float dx = this.x - anotherPoint.x;
        float dy = this.y - anotherPoint.y;
        
        return (float)Math.sqrt(dx * dx + dy * dy);
    }
    
    /**
     * Convert IntPoint to FloatPoint.
     * @return FloatPoint.
     */
    public FloatPoint toFloatPoint(){
        return new FloatPoint(this.x, this.y);
    }
    
    /**
     * Convert IntPoint to DoublePoint.
     * @return DoublePoint.
     */
    public DoublePoint toDoublePoint(){
        return new DoublePoint(this.x, this.y);
    }
    
    /**
     * Swap values between the coordinates.
     */
    public void Swap(){
        int temp = x;
        x = y;
        y = temp;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass().isAssignableFrom(IntPoint.class)) {
            IntPoint point = (IntPoint)obj;
            if ((this.x == point.x) && (this.y == point.y)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + this.x;
        hash = 67 * hash + this.y;
        return hash;
    }

    @Override
    public String toString() {
        return "X: " + this.x + " Y: " + this.y;
    }
}