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
 * Minkowski distance.
 * The Minkowski distance is a metric in a normed vector space which can be considered as a generalization of both the Euclidean distance and the Manhattan distance.
 * 
 * @author Diego Catalano
 */
public class MinkowskiDistance implements IDistance<double[]>{
    
    private double p = 1;

    /**
     * Get the order of the distance.
     * @return Order.
     */
    public double getOrder() {
        return p;
    }

    /**
     * Set the order of the distance.
     * @param p Order.
     */
    public void setOrder(double p) {
        if(p == 0)
            throw new IllegalArgumentException("P must be different from 0.");
        this.p = p;
    }

    /**
     * Initializes a new instance of the MinkowskiDistance class.
     * p = 1 (default).
     */
    public MinkowskiDistance() {}
    
    /**
     * Initializes a new instance of the MinkowskiDistance class.
     * @param p Order.
     */
    public MinkowskiDistance(double p){
        setOrder(p);
    }

    @Override
    public double Compute(double[] u, double[] v) {
        return Distance.Minkowski(u, v, p);
    }
}