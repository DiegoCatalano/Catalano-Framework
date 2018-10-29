// Catalano Math Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2018
// diego.catalano at live.com
//
// Copyright © Seyedali Mirjalili, 2018
// ali.mirjalili at gmail.com
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

/**
 * Singer map.
 * @author Diego Catalano
 */
public class SingerMap implements IChaoticFunction{
    
    private double u;

    /**
     * Initialize a new instance of the SingerMap class.
     */
    public SingerMap() {
        this(1.07);
    }

    /**
     * Initialize a new instance of the SingerMap class.
     * @param u Coefficient.
     */
    public SingerMap(double u) {
        this.u = u;
    }

    @Override
    public double Generate(double x) {
        
        return u*(7.86*x - 23.31*(x*x) + 28.75*(x*x*x) - 13.302875*(x*x*x*x));
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