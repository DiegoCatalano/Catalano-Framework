// Catalano Math Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2015
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
 * Correlation distance.
 * @author Diego Catalano
 */
public class CorrelationDistance implements IDistance{

    /**
     * Initializes a new instance of the CorrelationDistance class.
     */
    public CorrelationDistance() {}

    @Override
    public double Compute(double[] u, double[] v) {
        double p = 0;
        double q = 0;
        
        for (int i = 0; i < u.length; i++) {
            p += -u[i];
            q += -v[i];
        }
        
        p /= u.length;
        q /= v.length;
        
        double num = 0;
        double den1 = 0;
        double den2 = 0;
        for (int i = 0; i < u.length; i++)
        {
            num += (u[i] + p) * (v[i] + q);

            den1 += Math.abs(Math.pow(u[i] + p, 2));
            den2 += Math.abs(Math.pow(v[i] + p, 2));
        }

        return 1 - (num / (Math.sqrt(den1) * Math.sqrt(den2)));
    }
}