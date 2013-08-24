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
 * Interface which specifies set of methods required to be implemented by all defuzzification methods that can be used in Fuzzy Inference Systems.
 * @author Diego Catalano
 */
public interface IDefuzzifier {
    
    /**
     * Defuzzification method to obtain the numerical representation of a fuzzy output.
     * @param fuzzyOutput A <see cref="FuzzyOutput"/> containing the output of several rules of a Fuzzy Inference System.
     * @param normOperator A <see cref="INorm"/> operator to be used when constraining the label to the firing strength.
     * @return The numerical representation of the fuzzy output.
     */
    float Defuzzify(FuzzyOutput fuzzyOutput, INorm normOperator);
}
