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
 * The class represents a fuzzy set.
 * @author Diego Catalano
 */
public class FuzzySet {
    
     private String name;
     private IMembershipFunction function;

     /**
      * Initializes a new instance of the FuzzySet class.
      * @param name Name of the fuzzy set.
      * @param function Membership function that will define the shape of the fuzzy set.
      */
     public FuzzySet(String name, IMembershipFunction function) {
        this.name = name;
        this.function = function;
    }
     
     /**
      * Name of the fuzzy set.
      * @return Name.
      */
     public String Name(){
         return name;
     }
     
     /**
      * The leftmost x value of the fuzzy set's membership function.
      * @return X.
      */
     public float LeftLimit(){
         return function.LeftLimit();
     }
     
     /**
      * The rightmost x value of the fuzzy set's membership function.
      * @return X.
      */
     public float RightLimit(){
         return function.RightLimit();
     }
     
     /**
      * Calculate membership of a given value to the fuzzy set.
      * @param x Value which membership needs to be calculated.
      * @return Degree of membership [0..1] of the value to the fuzzy set.
      */
     public float getMembership(float x){
         return function.GetMembership(x);
     }
}
