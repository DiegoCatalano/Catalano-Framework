// Catalano Math Library
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

package Catalano.Math.Distances;

/**
 * Chebyshev distance.
 * Is a metric defined on a vector space where the distance between two vectors is the greatest of their differences along any coordinate dimension.
 * 
 * @author Diego Catalano
 */
public class ChebyshevDistance implements IDistance<double[]>{

    /**
     * Initializes a new instance of the ChevyshevDistance class.
     */
    public ChebyshevDistance() {}

    @Override
    public double Compute(double[] u, double[] v) {
        return Distance.Chebyshev(u, v);
    }
}