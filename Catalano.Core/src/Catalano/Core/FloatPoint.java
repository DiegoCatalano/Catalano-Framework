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
 * Class for representing a pair of coordinates of float type.
 * @author Diego Catalano
 */
public class FloatPoint {
    /**
     * X axis coordinate.
     */
    /**
     * Y axis coordinate.
     */
    public float x,y;

    /**
     * Initializes a new instance of the FloatPoint class.
     */
    public FloatPoint() {}
    
    /**
     * Initializes a new instance of the FloatPoint class.
     * @param point FloatPoint.
     */
    public FloatPoint(FloatPoint point){
        this.x = point.x;
        this.y = point.y;
    }

    /**
     * Initializes a new instance of the FloatPoint class.
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     */
    public FloatPoint(float x, float y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Initializes a new instance of the FloatPoint class.
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     */
    public FloatPoint(double x, double y){
        this.x = (float)x;
        this.y = (float)y;
    }
    
    /**
     * Initializes a new instance of the FloatPoint class.
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     */
    public FloatPoint(int x, int y){
        this.x = x;
        this.y = y;
    }
    
    /**
     * Initializes a new instance of the FloatPoint class.
     * @param point IntPoint.
     */
    public FloatPoint(IntPoint point){
        this.x = point.x;
        this.y = point.y;
    }
    
    /**
     * Initializes a new instance of the FloatPoint class.
     * @param point DoublePoint.
     */
    public FloatPoint(DoublePoint point){
        this.x = (float)point.x;
        this.y = (float)point.y;
    }
    
    /**
     * Sets X and Y axis coordinates.
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     */
    public void setXY(float x, float y){
        this.x = x;
        this.y = y;
    }
    
    /**
     * Adds values of two points.
     * @param point FloatPoint.
     */
    public void Add(FloatPoint point){
        this.x += point.x;
        this.y += point.y;
    }
    
    /**
     * Adds values of two points.
     * @param point1 FloatPoint.
     * @param point2 FloatPoint.
     * @return A new FloatPoint with the add operation.
     */
    public FloatPoint Add(FloatPoint point1, FloatPoint point2){
        FloatPoint result = new FloatPoint(point1);
        result.Add(point2);
        return result;
    }
    
    /**
     * Adds values of two points.
     * @param value Value.
     */
    public void Add(float value){
        this.x += value;
        this.y += value;
    }
    
    /**
     * Subtract values of two points.
     * @param point FloatPoint.
     */
    public void Subtract(FloatPoint point){
        this.x -= point.x;
        this.y -= point.y;
    }
    
    /**
     * Subtracts values of two points.
     * @param point1 FloatPoint.
     * @param point2 FloatPoint.
     * @return A new FloatPoint with the subtract operation.
     */
    public FloatPoint Subtract(FloatPoint point1, FloatPoint point2){
        FloatPoint result = new FloatPoint(point1);
        result.Subtract(point2);
        return result;
    }
    
    /**
     * Subtract values of two points.
     * @param value Value.
     */
    public void Subtract(float value){
        this.x -= value;
        this.y -= value;
    }
    
    /**
     * Multiply values of two points.
     * @param point FloatPoint.
     */
    public void Multiply(FloatPoint point){
        this.x *= point.x;
        this.y *= point.y;
    }
    
    /**
     * Multiply values of two points.
     * @param point1 FloatPoint.
     * @param point2 FloatPoint.
     * @return A new FloatPoint with the multiplication operation.
     */
    public FloatPoint Multiply(FloatPoint point1, FloatPoint point2){
        FloatPoint result = new FloatPoint(point1);
        result.Multiply(point2);
        return result;
    }
    
    /**
     * Multiply values of two points.
     * @param value Value.
     */
    public void Multiply(float value){
        this.x *= value;
        this.y *= value;
    }
    
    /**
     * Divide values of two points.
     * @param point FloatPoint.
     */
    public void Divide(FloatPoint point){
        this.x /= point.x;
        this.y /= point.y;
    }
    
    /**
     * Divide values of two points.
     * @param point1 FloatPoint.
     * @param point2 FloatPoint.
     * @return A new FloatPoint with the division operation.
     */
    public FloatPoint Divide(FloatPoint point1, FloatPoint point2){
        FloatPoint result = new FloatPoint(point1);
        result.Divide(point2);
        return result;
    }
    
    /**
     * Divide values of two points.
     * @param value Value.
     */
    public void Divide(float value){
        this.x /= value;
        this.y /= value;
    }
    
    /**
     * Calculate Euclidean distance between two points.
     * @param anotherPoint Point to calculate distance to.
     * @return Euclidean distance between this point and anotherPoint points.
     */
    public float DistanceTo(FloatPoint anotherPoint){
        float dx = this.x - anotherPoint.x;
        float dy = this.y - anotherPoint.y;
        
        return (float)Math.sqrt(dx * dx + dy * dy);
    }
    
    /**
     * Convert FloatPoint to IntPoint.
     * @return IntPoint.
     */
    public IntPoint toIntPoint(){
        return new IntPoint(this.x,this.y);
    }
    
    /**
     * Convert FloatPoint to DoublePoint.
     * @return DoublePoint.
     */
    public DoublePoint toDoublePoint(){
        return new DoublePoint(this.x,this.y);
    }
    
    /**
     * Swap values between the coordinates.
     */
    public void Swap(){
        float temp = x;
        x = y;
        y = temp;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass().isAssignableFrom(DoublePoint.class)) {
            FloatPoint point = (FloatPoint)obj;
            if ((this.x == point.x) && (this.y == point.y)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Float.floatToIntBits(this.x);
        hash = 89 * hash + Float.floatToIntBits(this.y);
        return hash;
    }

    @Override
    public String toString() {
        return "X: " + this.x + " Y: " + this.y;
    }
}