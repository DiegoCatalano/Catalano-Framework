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

import java.io.Serializable;

/**
 * Represents a double range with minimum and maximum values.
 * @author Diego Catalano
 */
public class DoubleRange implements Serializable{
    
    double min,max;

    /**
     * Initializes a new instance of the DoubleRange class.
     */
    public DoubleRange() {}

    /**
     * Initializes a new instance of the DoubleRange class.
     * @param min Minimum value of the range.
     * @param max Maximum value of the range.
     */
    public DoubleRange(double min, double max) {
        this.min = min;
        this.max = max;
    }

    /**
     * Get minimum value of the range.
     * @return Minimum value.
     */
    public double getMin() {
        return min;
    }

    /**
     * Set minimum value of the range.
     * @param min Minimum value.
     */
    public void setMin(double min) {
        this.min = min;
    }

    /**
     * Get maximum value of the range.
     * @return Maximum value.
     */
    public double getMax() {
        return max;
    }

    /**
     * Set maximum value of the range.
     * @param max Maximum value.
     */
    public void setMax(double max) {
        this.max = max;
    }
    
    /**
     * Length of the range (difference between maximum and minimum values).
     * @return Length.
     */
    public double length(){
        return max - min;
    }
    
    /**
     * Check if the specified range is inside of the range.
     * @param x Value.
     * @return True if the value is inside of the range, otherwise returns false.
     */
    public boolean isInside(double x){
        return (x >= min) && (x <= max);
    }
    
    /**
     * Check if the specified range overlaps with the range.
     * @param range DoubleRange.
     * @return True if the range overlaps with the range, otherwise returns false.
     */
    public boolean IsOverlapping( DoubleRange range ){
            return ( ( isInside( range.min ) ) || ( isInside( range.max ) ) ||
                     ( range.isInside( min ) ) || ( range.isInside( max ) ) );
    }
    
    /**
     * Convert DoubleRange to IntRange.
     * @return IntRange.
     */
    public IntRange toIntRange(){
        return new IntRange((int)this.min, (int)this.max);
    }
    
    /**
     * Convert DoubleRange to FloatRange.
     * @return FloatRange.
     */
    public FloatRange toFloatRange(){
        return new FloatRange((float)this.min, (float)this.max);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass().isAssignableFrom(DoubleRange.class)) {
            DoubleRange range = (DoubleRange)obj;
            return this.min == range.getMin() && this.max == range.getMax();
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 61 * hash + (int) (Double.doubleToLongBits(this.min) ^ (Double.doubleToLongBits(this.min) >>> 32));
        hash += 61 * hash + (int) (Double.doubleToLongBits(this.max) ^ (Double.doubleToLongBits(this.max) >>> 32));
        return hash;
    }

    @Override
    public String toString() {
        return "Minimum: " + this.min + " Maximum: " + this.max;
    }
    
}