// Catalano Genetic Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
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

package Catalano.Genetic.SwarmIntelligence.PSO;

/**
 * Location.
 * Search space.
 * @author Diego Catalano
 */
public class Location {
    
    private double[] location;

    /**
     * Get value.
     * @return Value.
     */
    public double[] getValue() {
        return location;
    }

    /**
     * Set value.
     * @param location value.
     */
    public void setValue(double[] location) {
        this.location = location;
    }

    /**
     * Initializes a new instance of the Location class.
     * @param location 
     */
    public Location(double[] location) {
        this.location = location;
    }
    
}