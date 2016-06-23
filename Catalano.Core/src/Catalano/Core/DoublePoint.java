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
 * Class for representing a pair of coordinates of double type.
 * @author Diego Catalano
 */
public class DoublePoint {
    
    /**
     * X axis coordinate.
     */
    /**
     * Y axis coordinate.
     */
    public double x,y;

    /**
     * Initializes a new instance of the DoublePoint class.
     */
    public DoublePoint() {}
    
    /**
     * Initializes a new instance of the DoublePoint class.
     * @param point DoublePoint.
     */
    public DoublePoint(DoublePoint point){
        this.x = point.x;
        this.y = point.y;
    }

    /**
     * Initializes a new instance of the DoublePoint class.
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     */
    public DoublePoint(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Initializes a new instance of the DoublePoint class.
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     */
    public DoublePoint(float x, float y){
        this.x = x;
        this.y = y;
    }
    
    /**
     * Initializes a new instance of the DoublePoint class.
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     */
    public DoublePoint(int x, int y){
        this.x = x;
        this.y = y;
    }
    
    /**
     * Initializes a new instance of the DoublePoint class.
     * @param point DoublePoint.
     */
    public DoublePoint(IntPoint point){
        this.x = point.x;
        this.y = point.y;
    }
    
    /**
     * Initializes a new instance of the DoublePoint class.
     * @param point FloatPoint.
     */
    public DoublePoint(FloatPoint point){
        this.x = point.x;
        this.y = point.y;
    }
    
    /**
     * Sets X and Y axis coordinates.
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     */
    public void setXY(double x, double y){
        this.x = x;
        this.y = y;
    }
    
    /**
     * Adds value of point.
     * @param point DoublePoint.
     */
    public void Add(DoublePoint point){
        this.x += point.x;
        this.y += point.y;
    }
    
    /**
     * Adds values of two points.
     * @param point1 DoublePoint.
     * @param point2 DoublePoint.
     * @return A new DoublePoint with the add operation.
     */
    public DoublePoint Add(DoublePoint point1, DoublePoint point2){
        DoublePoint result = new DoublePoint(point1);
        result.Add(point2);
        return result;
    }
    
    /**
     * Adds value of point.
     * @param value Value.
     */
    public void Add(double value){
        this.x += value;
        this.y += value;
    }
    
    /**
     * Subtract value of point.
     * @param point DoublePoint.
     */
    public void Subtract(DoublePoint point){
        this.x -= point.x;
        this.y -= point.y;
    }
    
    /**
     * Subtract values of two points.
     * @param point1 DoublePoint.
     * @param point2 DoublePoint.
     * @return A new DoublePoint with the subtraction operation.
     */
    public DoublePoint Subtract(DoublePoint point1, DoublePoint point2){
        DoublePoint result = new DoublePoint(point1);
        result.Subtract(point2);
        return result;
    }
    
    /**
     * Subtract value of point.
     * @param value Value.
     */
    public void Subtract(double value){
        this.x -= value;
        this.y -= value;
    }
    
    /**
     * Multiply value of point.
     * @param point DoublePoint.
     */
    public void Multiply(DoublePoint point){
        this.x *= point.x;
        this.y *= point.y;
    }
    
    /**
     * Multiply values of two points.
     * @param point1 DoublePoint.
     * @param point2 DoublePoint.
     * @return A new DoublePoint with the multiplication operation.
     */
    public DoublePoint Multiply(DoublePoint point1, DoublePoint point2){
        DoublePoint result = new DoublePoint(point1);
        result.Multiply(point2);
        return result;
    }
    
    /**
     * Multiply value of point.
     * @param value Value.
     */
    public void Multiply(double value){
        this.x *= value;
        this.y *= value;
    }
    
    /**
     * Divides values of two points.
     * @param point DoublePoint.
     */
    public void Divide(DoublePoint point){
        this.x /= point.x;
        this.y /= point.y;
    }
    
    /**
     * Divides values of two points.
     * @param point1 DoublePoint.
     * @param point2 DoublePoint.
     * @return A new DoublePoint with the division operation.
     */
    public DoublePoint Divide(DoublePoint point1, DoublePoint point2){
        DoublePoint result = new DoublePoint(point1);
        result.Divide(point2);
        return result;
    }
    
    /**
     * Divides values of two points.
     * @param value Value.
     */
    public void Divide(double value){
        this.x /= value;
        this.y /= value;
    }
    
    /**
     * Calculate Euclidean distance between two points.
     * @param anotherPoint Point to calculate distance to.
     * @return Euclidean distance between this point and anotherPoint points.
     */
    public double DistanceTo(DoublePoint anotherPoint){
        double dx = this.x - anotherPoint.x;
        double dy = this.y - anotherPoint.y;
        
        return Math.sqrt(dx * dx + dy * dy);
    }
    
    /**
     * Swap values between the coordinates.
     */
    public void Swap(){
        double temp = x;
        x = y;
        y = temp;
    }
    
    /**
     * Convert DoublePoint to IntPoint
     * @return IntPoint.
     */
    public IntPoint toIntPoint(){
        return new IntPoint(this.x,this.y);
    }
    
    /**
     * Convert DoublePoint to FloatPoint.
     * @return FloatPoint.
     */
    public FloatPoint toFloatPoint(){
        return new FloatPoint(this.x, this.y);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass().isAssignableFrom(DoublePoint.class)) {
            DoublePoint point = (DoublePoint)obj;
            if ((this.x == point.x) && (this.y == point.y)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> 32));
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.y) ^ (Double.doubleToLongBits(this.y) >>> 32));
        return hash;
    }

    @Override
    public String toString() {
        return "X: " + this.x + " Y: " + this.y;
    }
}