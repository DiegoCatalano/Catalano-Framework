// Catalano Fuzzy Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2013
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

package Catalano.Fuzzy;

/**
 * Interface which specifies set of methods required to be implemented by all membership functions.
 * <br /> All membership functions must implement this interface, which is used by
 * <br /> class to calculate value's membership to a particular fuzzy set.
 * @author Diego Catalano
 */
public interface IMembershipFunction {
    /**
     * Calculate membership of a given value to the fuzzy set.
     * @param x Value which membership will to be calculated.
     * @return Degree of membership [0..1] of the value to the fuzzy set.
     */
    float GetMembership(float x);
    /**
     * The leftmost x value of the membership function.
     * @return x.
     */
    float LeftLimit();
    /**
     * The rightmost x value of the membership function.
     * @return x.
     */
    float RightLimit();
}