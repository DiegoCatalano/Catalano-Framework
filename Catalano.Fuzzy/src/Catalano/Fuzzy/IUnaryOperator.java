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
 * Interface with the common methods of Fuzzy Unary Operator.
 * @author Diego Catalano
 */
public interface IUnaryOperator {
    
    
    /**
     * Calculates the numerical result of a Unary operation applied to one fuzzy membership value.
     * @param membership A fuzzy membership value, [0..1].
     * @return The numerical result of the operation applied to <paramref name="membership"/>.
     */
    float Evaluate( float membership );
}
