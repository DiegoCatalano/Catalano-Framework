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
 * Membership function used in fuzzy singletons: fuzzy sets that have just one point with membership value 1.
 * @author Diego Catalano
 */
public class SingletonFunction implements IMembershipFunction{
    
    /**
     * The unique point where the membership value is 1.
     */
    protected float support;

    /**
     * Initializes a new instance of the SingletonFunction class.
     * @param support Support is the only value of x where the membership function is 1.
     */
    public SingletonFunction(float support) {
        this.support = support;
    }

    @Override
    public float GetMembership(float x) {
        // if x is the support, returns 1, otherwise, returns 0
        return ( support == x ) ? 1 : 0;
    }

    @Override
    public float LeftLimit() {
        return support;
    }

    @Override
    public float RightLimit() {
        return support;
    }
    
}
