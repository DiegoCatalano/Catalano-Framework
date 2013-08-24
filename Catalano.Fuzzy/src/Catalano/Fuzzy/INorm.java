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
 * Interface with the common methods of a Fuzzy Norm.
 * <p>All fuzzy operators that act as a Norm must implement this interface.</p>
 * 
 * @author Diego Catalano
 */
public interface INorm {
    
    /**
     * Calculates the numerical result of a Norm (AND) operation applied to two fuzzy membership values.
     * @param membershipA A fuzzy membership value, [0..1].
     * @param membershipB A fuzzy membership value, [0..1].
     * @return The numerical result the operation AND applied to <paramref name="membershipA"/> and <paramref name="membershipB"/>.
     */
    float Evaluate( float membershipA, float membershipB );
}
