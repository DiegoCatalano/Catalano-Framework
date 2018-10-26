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

package Catalano.Math.Functions.Chaotic;

import Catalano.Math.Constants;

/**
 * Circle map.
 * @author Diego Catalano
 */
public class CircleMap implements IChaoticFunction{
    
    private double a;
    private double b;

    /**
     * Initialize a new instance of the CircleMap class.
     */
    public CircleMap() {
        this(0.5,0.2);
    }

    /**
     * Initialize a new instance of the CircleMap class.
     * @param a Alfa.
     * @param b Beta.
     */
    public CircleMap(double a, double b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public double Generate(double x) {
        
        double r = x + b - (a / Constants.DoublePI) * Math.sin(Constants.DoublePI*x);
        return r % 1;
        
    }

    @Override
    public double[] Generate(double initialState, int iterations) {
        double[] map = new double[iterations];
        map[0] = initialState;
        
        for (int i = 1; i < iterations; i++) {
            map[i] = Generate(map[i - 1]);
        }
        
        return map;
    }
}